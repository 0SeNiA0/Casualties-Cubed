package net.zaharenko424.casualties_cubed.hitbox;

import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.zaharenko424.casualties_cubed.CasualtiesCubedTags;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.config.ServerConfig;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.LimbStatistics;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import net.zaharenko424.casualties_cubed.registry.ModSounds;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber
public class HitboxEvents {

    private static final Map<UUID, DamageContext> contextMap = new ConcurrentHashMap<>();

    private static class DamageContext {
        public DamageSource source;
        public Entity directEntity; // projectile, attacker, etc.
        public float preArmorAmount = -1f;
        public Vec3 projectileHitPos = null;
        // add whatever else you need (e.g. flags for special sources)
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingAttack(LivingAttackEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (event.isCanceled()) return;

        DamageSource src = event.getSource();
        UUID id = player.getUUID();

        DamageContext ctx = new DamageContext();
        ctx.source = src;
        ctx.directEntity = src.getDirectEntity();
        contextMap.put(id, ctx);

        // If projectile, compute accurate intersection now and store position
        Entity direct = ctx.directEntity;
        if (direct instanceof Projectile proj) {
            // Use your sweep logic to compute hit position against this player
            ctx.projectileHitPos = sweepProjectileStep(proj, player);
        }

        // You can do other detection here if needed (e.g. store attacker pos)
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (event.isCanceled()) return;

        UUID id = player.getUUID();
        DamageContext ctx = contextMap.computeIfAbsent(id, k -> new DamageContext());
        ctx.preArmorAmount = event.getAmount();

        // Do small pre-armor reductions like absorption/resistance if you want here.
        // But prefer to do the full authoritative calculation in LivingDamageEvent.
    }

    private static final List<HitSector> CBC_CHANCES = List.of(
            HitSector.HEAD,
            HitSector.TORSO, HitSector.TORSO, HitSector.TORSO,
            HitSector.RIGHT_ARM, HitSector.LEFT_ARM,
            HitSector.LEGS, HitSector.LEGS
    );

    private static HitSector randomCBCSector(Player player) {
        return Util.getRandom(CBC_CHANCES, player.getRandom());
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingDamage(LivingDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (event.isCanceled()) return;

        UUID id = player.getUUID();
        DamageContext ctx = contextMap.remove(id); // consume context

        float damageamount = event.getAmount(); // final damage after armor/enchantments
        DamageSource src = (ctx != null && ctx.source != null) ? ctx.source : event.getSource();

        if (src.is(CasualtiesCubedTags.DamageType.IGNORE)) return;
        if (damageamount == Float.MAX_VALUE || Float.isNaN(damageamount)
                || (ctx != null && ctx.preArmorAmount == Float.MAX_VALUE)) return;

        if (src.is(DamageTypeTags.IS_DROWNING) || src.is(DamageTypes.IN_WALL)
                || src.is(DamageTypes.STARVE)) {//Handled in PlayerHealthData
            event.setAmount(0);
            return;
        }

        PlayerHealthData data = player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).resolve().orElse(null);
        if (data == null) return;

        if (src.is(DamageTypes.STARVE)) {
            for (Limb limb : Limb.values()) {
                data.getLimb(limb).addMuscleHealth(-5);
            }
            event.setAmount(0);
            return;
        }

        if (src.is(DamageTypes.CRAMMING)) {
            List<Limb> nonAmputated = new ArrayList<>();
            for (Limb limb : Limb.values()) {
                if (!data.isAmputated(limb)) nonAmputated.add(limb);
            }

            float dmg = damageamount / nonAmputated.size();
            for (Limb limb : nonAmputated) {
                data.applyMuscleDamage(limb, dmg, player);
            }

            event.setAmount(0);
            return;
        }

        if (src.is(DamageTypes.FLY_INTO_WALL)) {
            handleBluntDamage(data, damageamount, player, Limb.HEAD);
            data.brainHealth(data.brainHealth() - damageamount);
            event.setAmount(0);
            return;
        }

        if (src.is(DamageTypes.SONIC_BOOM)) {
            handleSonicDamage(data, damageamount, player);
            event.setAmount(0);
            return;
        }

        if (src.is(DamageTypes.FREEZE)) {
            data.setTemperature(data.getTemperature() - damageamount * 1.8f);
            event.setAmount(0);
            return;
        }

        if (src.is(DamageTypes.FALL)) {
            handleFallDamage(data, damageamount, player);
            event.setAmount(0);
            return;
        }

        if (src.is(CBCmg) || src.is(CBCmgwat)) {
            handleProjectileDamage(data, randomCBCSector(player), damageamount, player);
            event.setAmount(0);
            return;
        }

        if (src.is(CBCproj) || src.is(CBCprojbig) || src.is(CBCtraff)) {
            if (damageamount < 5) {
                float finalDamage = damageamount * 15;
                for (int i = 0; i < 8; i++) {
                    handleProjectileDamage(data, randomCBCSector(player), finalDamage, player);
                }
            } else {
                handleExplosionDamage(data, damageamount, true, player);
            }

            event.setAmount(0);
            return;
        }

        if (src.is(DamageTypeTags.IS_EXPLOSION)) {
            handleExplosionDamage(data, damageamount, event.getSource().is(CasualtiesCubedTags.DamageType.SHRAPNELL), player);
            event.setAmount(0);
            return;
        }

        if (isAnyProjectile(src)) {
            Entity directEntity = event.getSource().getDirectEntity();
            if ((directEntity instanceof Projectile projectile)) {
                Vec3 hitPos = sweepProjectileStep(projectile, player);
            /*
            if (event.getSource().getEntity() instanceof Player pp){
                pp.sendSystemMessage(Component.literal("hitpos "+hitPos));
                pp.sendSystemMessage(Component.literal("projspeed "+projectile.getDeltaMovement().length()));
            }

             */


                // Your custom hit sector logic
                HitSector hit = detectHit(player, hitPos);
                // This damage value is AFTER vanilla reductions (armor, resistance, etc.)

                // Call into your capability with final damage
                handleProjectileDamage(data, hit, damageamount, player);
                event.setAmount(0);
                return;
            }
        } else if (src.is(CasualtiesCubedTags.DamageType.MAGIC) || event.getSource().is(DamageTypes.MAGIC)) {
            handleMagicDamage(data, damageamount, (ServerPlayer) player);
            event.setAmount(0);
            return;
        } else if (src.is(DamageTypeTags.IS_FIRE)) {
            handleFireDamage(data, damageamount, player);
            event.setAmount(0);
            return;
        } else if (src.is(DamageTypeTags.BYPASSES_ARMOR)) {
            handleRandomDamage(data, damageamount, player);
            event.setAmount(0);
            return;
        }
        //fuck you warium

        //TODO replace with specific non projectile damage
        //if (src.getSourcePosition() != null) {
        //    data.handleProjectileDamage(detectHit(player, src.getSourcePosition()), damageamount, player);
        //}

        handleRandomDamage(data, damageamount, player);
        event.setAmount(0);
    }

    private static boolean handleAmputation(PlayerHealthData data, Limb limb, LimbStatistics stats, float damage, float base_damage_treshhold, Player player) {
        if (!ServerConfig.PERMANENT_DAMAGE.get()) return false;

        boolean skipOthers = false;
        float damage_treshold = base_damage_treshhold;
        if ((limb == Limb.HEAD && data.disfigured() && data.isLeftEyeBlind() && data.isRightEyeBlind()) || limb == Limb.THORAX) {
            damage_treshold *= 2;
            skipOthers = true;
        } else if (limb == Limb.HEAD && (!data.isLeftEyeBlind() || !data.isRightEyeBlind())) {
            damage_treshold -= 10;
        }
        float musclepenalty = (100 - stats.getMuscleHealth()) / 100 * -10;
        float skinpenalty = (100 - stats.getSkinHealth()) / 100 * -5;
        damage_treshold += musclepenalty + skinpenalty;
        if (damage >= damage_treshold / 2) {
            if (Math.random() > damage / (damage_treshold)) return false;
            if (limb == Limb.HEAD && !skipOthers) {
                Random random = new Random();
                int chance = random.nextInt(3);
                if (chance == 1 && !data.isRightEyeBlind()) {
                    data.setRightEyeBlind(true);
                } else if ((chance == 2 || chance == 1) && !data.isLeftEyeBlind()) {
                    data.setLeftEyeBlind(true);
                } else if (!data.disfigured()) {
                    data.disfigured(true);
                }
                player.playSound(ModSounds.AMPUTATION.get());
                return true;
            }
            List<Limb> limbList = limb.getConnectedLimbs();
            for (Limb limb1 : limbList) {
                stats = data.getLimb(limb1);

                stats.setSkinHealth(0);
                stats.setBleedRate(1);
                stats.setPain(200);
                data.adrenaline(Math.max(data.adrenaline(), 125));
            }
            data.dismember(limb);
            player.playSound(ModSounds.AMPUTATION.get());
            return true;
        } else if (damage >= 6 && !(limb == Limb.THORAX || limb == Limb.HEAD)) {
            if (Math.random() < 0.01) {
                handleAmputation(data, limb, stats, 1, 0, player);
            }
        }
        return false;
    }

    public static void handleSonicDamage(PlayerHealthData data, float damage, Player player) {
        data.adrenaline(Math.max(10 * damage, data.adrenaline()));

        RandomSource random = player.getRandom();
        LimbStatistics stats;
        for (Limb limb : Limb.values()) {
            stats = data.getLimb(limb);

            stats.addPain((limb == Limb.HEAD ? 10 : 4) * damage);
            stats.addMuscleHealth(-(2 + 1 * random.nextFloat()) * damage);
        }

        data.brainHealth(data.brainHealth() - .5f * damage);//200 sonic boom damage will insta kill
        data.setHearingLoss(Math.max(.06f * damage, data.getHearingLoss()));
        data.setConsciousness(data.getConsciousness() - 6.9f * damage);
        data.setInternalBleeding(data.getInternalBleeding() + (0.0171f + 0.00855f * random.nextFloat()) * damage);
    }

    private static final float[][] FALL_DAMAGE_STAGES = {
            {0.6f, 0.0f, 0.0f},   // stage 1: feet only
            {0.4f, 0.6f, 0.0f},   // stage 2: feet + legs
            {0.2f, 0.4f, 0.2f},   // stage 3
            {0.1f, 0.2f, 0.4f},   // stage 4
            {0.1f, 0.1f, 0.4f},   // stage 5
            {0.1f, 0.1f, 0.6f},   // stage 6
            {0.1f, 0.1f, 0.8f},   // stage 7
            {0.1f, 0.1f, 1.0f}    // stage 8: always random damage
    };

    public static void handleFallDamage(PlayerHealthData data, float damageValue, Player player) {
        RandomSource random = player.getRandom();
        data.adrenaline(Math.max(data.adrenaline(), damageValue * 2));
        float remainingDamage = damageValue * 1;

        remainingDamage = applyLocationalArmor(Limb.LEFT_FOOT, remainingDamage, player, false, false, false, true);

        for (float[] stage : FALL_DAMAGE_STAGES) {
            float footMult = stage[0];
            float legMult = stage[1];
            float randChance = stage[2];

            // feet
            if (footMult > 0f) {
                applyFallDamage(data, Limb.LEFT_FOOT, remainingDamage * footMult, player);
                applyFallDamage(data, Limb.RIGHT_FOOT, remainingDamage * footMult, player);
            }

            // legs
            if (legMult > 0f) {
                applyFallDamage(data, Limb.UPPER_LEFT_LEG, remainingDamage * footMult, player);
                applyFallDamage(data, Limb.LOWER_LEFT_LEG, remainingDamage * footMult, player);
                applyFallDamage(data, Limb.UPPER_RIGHT_LEG, remainingDamage * footMult, player);
                applyFallDamage(data, Limb.LOWER_RIGHT_LEG, remainingDamage * footMult, player);
            }

            // random damage
            if (randChance > 0f && random.nextFloat() < randChance) {
                handleRandomDamage(data, remainingDamage * 0.5f, player);
            }

            // decay for next pass
            remainingDamage *= 0.7f;
            if (remainingDamage < 1f) return;
        }
    }

    private static void applyFallDamage(PlayerHealthData data, Limb limb, float damage, Player player) {
        LimbStatistics stats = data.getLimb(limb);
        if (stats.isAmputated()) return;

        data.applyMuscleDamage(limb, damage, player);
        stats.addPain(damage);
    }

    public static void handleBluntDamage(PlayerHealthData data, float damageValue, Player player, Limb limb) {
        RandomSource random = player.getRandom();
        data.adrenaline(Math.max(data.adrenaline(), damageValue * 0.5f));
        float remainingDamage = damageValue * 1;
        LimbStatistics stats = data.getLimb(limb);

        if (limb == Limb.HEAD)
            remainingDamage *= 0.7f;
        if (limb == Limb.THORAX)
            remainingDamage *= 0.5f;

        remainingDamage = applyLocationalArmor(limb, remainingDamage, player, false, false, false, true);

        applyConcussion(data, limb, (random.nextFloat() / 2 + 0.5f) * remainingDamage * 6);
        stats.addPain((random.nextFloat() / 2 + 0.5f) * remainingDamage * 8);
        data.applyMuscleDamage(limb, (random.nextFloat() / 2 + 0.5f) * remainingDamage * 0.8f, player);
        if (remainingDamage > 5) {
            stats.addSkinHealth(- (random.nextFloat() / 2 + 0.5f) * remainingDamage);
            data.applyBleedDamage(limb, remainingDamage * 2, player);
        }
        if (remainingDamage > 4) {
            for (Limb limb1 : limb.getConnectedLimbs()) {
                handleBluntDamage(data, remainingDamage / 2f, player, limb1);
            }
        }
    }

    private static void applyConcussion(PlayerHealthData data, Limb limb, float damage) {
        if (limb == Limb.HEAD) {
            data.setConsciousness(data.getConsciousness() - (Math.max(damage * 2, 10)));
        }
    }

    public static void handleMagicDamage(PlayerHealthData data, float damage, ServerPlayer player) {
        RandomSource random = player.getRandom();
        LimbStatistics stats;
        for (Limb limb : Limb.values()) {
            stats = data.getLimb(limb);

            data.adrenaline(Math.max(data.adrenaline(), damage * 1));
            data.applyMuscleDamage(limb, damage * (random.nextFloat() / 4f), player);
            stats.addPain(damage * random.nextFloat());
        }
    }

    private static float applyLocationalArmor(Limb limb, float damage, Player player, boolean is_fire, boolean is_projectile, boolean is_explosion, boolean is_fall) {
        float armPoints = 0, tough = 0, prot = 0, fprot = 0, pprot = 0, expprot = 0, ff = 0;
        switch (limb) {
            case HEAD -> {
                ItemStack item = player.getItemBySlot(EquipmentSlot.HEAD);
                if (item.getItem() instanceof ArmorItem armor) {
                    armPoints = armor.getDefense() * ServerConfig.HELMET_ARMOR_SCALE.get().floatValue();
                    tough = armor.getToughness();
                    prot = item.getEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION);
                    fprot = item.getEnchantmentLevel(Enchantments.FIRE_PROTECTION);
                    pprot = item.getEnchantmentLevel(Enchantments.PROJECTILE_PROTECTION);
                    expprot = item.getEnchantmentLevel(Enchantments.BLAST_PROTECTION);
                }
            }
            case UPPER_LEFT_ARM, LOWER_LEFT_ARM, UPPER_RIGHT_ARM, LOWER_RIGHT_ARM, LEFT_HAND, RIGHT_HAND -> {
                ItemStack item = player.getItemBySlot(EquipmentSlot.CHEST);
                float scalar = 0.5f;
                if (item.is(CasualtiesCubedTags.Item.ARMOR_CHEST_ONLY))
                    scalar = 0;
                else if (item.is(CasualtiesCubedTags.Item.ARMOR_FULL_ARM))
                    scalar = 1;
                if (item.getItem() instanceof ArmorItem armor) {
                    armPoints = armor.getDefense() * scalar * ServerConfig.CHESTPLATE_ARMOR_SCALE.get().floatValue();
                    tough = armor.getToughness();
                    prot = item.getEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION);
                    fprot = item.getEnchantmentLevel(Enchantments.FIRE_PROTECTION);
                    pprot = item.getEnchantmentLevel(Enchantments.PROJECTILE_PROTECTION);
                    expprot = item.getEnchantmentLevel(Enchantments.BLAST_PROTECTION);
                }
            }
            case THORAX -> {
                ItemStack item = player.getItemBySlot(EquipmentSlot.CHEST);
                if (item.getItem() instanceof ArmorItem armor) {
                    armPoints = armor.getDefense() * ServerConfig.CHESTPLATE_ARMOR_SCALE.get().floatValue();
                    tough = armor.getToughness();
                    prot = item.getEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION);
                    fprot = item.getEnchantmentLevel(Enchantments.FIRE_PROTECTION);
                    pprot = item.getEnchantmentLevel(Enchantments.PROJECTILE_PROTECTION);
                    expprot = item.getEnchantmentLevel(Enchantments.BLAST_PROTECTION);
                }
            }
            case UPPER_RIGHT_LEG, LOWER_RIGHT_LEG, UPPER_LEFT_LEG, LOWER_LEFT_LEG -> {
                ItemStack item = player.getItemBySlot(EquipmentSlot.LEGS);
                if (item.getItem() instanceof ArmorItem armor) {
                    armPoints = armor.getDefense() * ServerConfig.LEG_ARMOR_SCALE.get().floatValue();
                    tough = armor.getToughness();
                    prot = item.getEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION);
                    fprot = item.getEnchantmentLevel(Enchantments.FIRE_PROTECTION);
                    pprot = item.getEnchantmentLevel(Enchantments.PROJECTILE_PROTECTION);
                    expprot = item.getEnchantmentLevel(Enchantments.BLAST_PROTECTION);
                }
            }
            case RIGHT_FOOT, LEFT_FOOT -> {
                ItemStack item = player.getItemBySlot(EquipmentSlot.FEET);
                if (item.getItem() instanceof ArmorItem armor) {
                    armPoints = armor.getDefense() * ServerConfig.BOOTS_ARMOR_SCALE.get().floatValue();
                    tough = armor.getToughness();
                    prot = item.getEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION);
                    fprot = item.getEnchantmentLevel(Enchantments.FIRE_PROTECTION);
                    pprot = item.getEnchantmentLevel(Enchantments.PROJECTILE_PROTECTION);
                    expprot = item.getEnchantmentLevel(Enchantments.BLAST_PROTECTION);
                    ff = item.getEnchantmentLevel(Enchantments.FALL_PROTECTION);
                }
            }
            default -> {
                return damage;
            }
        }
        float d, r;
        d = (float) Math.min(0.75f, 0.14f + 0.02f * Math.pow(armPoints - 1, 2));
        r = 1.0f - Math.min(damage / (damage + (2 * tough + 8)), 0.5f);
        float finalReduction = Mth.clamp(d * r, 0, 1);
        float magic = 1;
        if (prot > 0) {
            magic = 1f - prot * 0.05f;
        } else {
            if (is_fire) {
                magic = 1 - fprot * 0.10f;
            } else if (is_projectile) {
                magic = 1 - pprot * 0.10f;
            } else if (is_fall) {
                magic = 1 - ff * 0.14f;
            } else if (is_explosion) {
                magic = 1 - expprot * 0.10f;
            }
        }

        return (damage * (1 - finalReduction)) * magic;
    }

    public static void handleFireDamage(PlayerHealthData data, float damage, Player player) {
        data.adrenaline(Math.max(data.adrenaline(), damage));
        int i = 0;
        Limb randLimb;
        LimbStatistics stats;
        while (damage > 0) {
            i++;
            randLimb = Limb.weigtedRandomLimb();
            stats = data.getLimb(randLimb);

            float damage_pass = (float) (Math.random() * 4);
            if (damage_pass > damage) damage_pass = damage;
            float passDamage = applyLocationalArmor(randLimb, Math.min(2, damage_pass), player, true, false, false, false);
            stats.addPain(data.painFromDamage(passDamage));
            applyConcussion(data, randLimb, damage_pass);

            damageSkinOrMuscle(data, randLimb, stats, passDamage);

            stats.addBurn(passDamage * 2.5f);

            if (Math.random() < i * 0.2) {
                data.getLimb(randLimb).addPain(passDamage * 5);
                damageSkinOrMuscle(data, randLimb, stats, passDamage * 0.75f);
                data.applyBleedDamage(randLimb, passDamage * .5f, player);

                damage -= 1;
            }

            stats.setBleedRate(stats.getBleedRate() * 0.4f);
            damage -= damage_pass;
            hurtArmor(randLimb, player, damage_pass);
        }
    }

    private static void damageSkinOrMuscle(PlayerHealthData data, Limb limb, LimbStatistics stats, float passDamage) {
        if (stats.getSkinHealth() == 0) {
            stats.addMuscleHealth(-passDamage * 5);
        } else {
            stats.addMuscleHealth(-passDamage * 3.5f);
            data.applySkinDamage(limb, passDamage * 5);
        }
    }

    public static void handleExplosionDamage(PlayerHealthData data, float damage, boolean shrapnell, Player player) {
        data.adrenaline(Math.max(data.adrenaline(), damage * 1));
        float passDamage;
        Limb limb;
        LimbStatistics stats;
        while (damage > 2) {
            passDamage = Math.min(damage, 6);
            limb = Limb.weigtedRandomLimb();
            stats = data.getLimb(limb);

            passDamage = applyLocationalArmor(limb, passDamage, player, false, false, true, false);
            applyConcussion(data, limb, passDamage);
            data.applyMuscleDamage(limb, passDamage * 0.2f, player);
            data.applySkinDamage(limb, passDamage * 0.8f);
            stats.addPain(data.painFromDamage(passDamage));
            data.applyBleedDamage(limb, passDamage * 0.7f, player);

            float chance = passDamage / 6 + 0.2f;
            if (Math.random() < chance && shrapnell) {
                stats.addShrapnel((int) (Math.random() * 5));
            }

            hurtArmor(limb, player, damage);

            boolean amputated = handleAmputation(data, limb, stats, passDamage, 15 + 5, player);
            if (amputated) {
                damage /= 4;
            }

            damage -= 2;
        }
    }

    public static void handleProjectileDamage(PlayerHealthData data, HitSector hitSector, float damage, Player player) {
        data.adrenaline(Math.max(data.adrenaline(), damage * 2));
        RandomSource random = player.getRandom();
        List<Limb> limbList = hitSector.limbs;
        Limb randomLimb = limbList.get(random.nextInt(limbList.size()));
        LimbStatistics stats = data.getLimb(randomLimb);
        damage = applyLocationalArmor(randomLimb, damage, player, false, true, false, false);

        applyConcussion(data, randomLimb, damage);
        data.applyMuscleDamage(randomLimb, (float) (damage * (Math.random() / 2f + 0.5f)), player);
        stats.addPain(data.painFromDamage(damage));
        data.applySkinDamage(randomLimb, (float) (damage * (Math.random() / 2f + 0.5f)));
        data.applyBleedDamage(randomLimb, damage * 0.9f, player);
        float chance;

        if (damage <= 2f || damage >= 10f) {
            chance = 0f;
        } else {
            float t = (damage - 2f) / 8f; // normalize to [0..1]
            float curve = (float) (-4 * Math.pow(t - 0.5f, 2) + 1);
            chance = Math.max(0f, curve * 0.75f); // scale to max 0.75
        }
        if (random.nextFloat() < chance) {
            stats.addShrapnel(1);
        }
        hurtArmor(randomLimb, player, damage);
        boolean amputated = handleAmputation(data, randomLimb, stats, damage, 15 + 5 + 10, player);
        if (amputated) {
            damage /= 4;//?
        }
    }

    public static void handleRandomDamage(PlayerHealthData data, float damage, Player player) {
        data.adrenaline(Math.max(data.adrenaline(), damage * 1));
        int i = 0;
        RandomSource random = player.getRandom();
        LimbStatistics stats;
        float damage_pass;
        while (damage > 0) {
            i++;
            Limb randLimb = Limb.weigtedRandomLimb();
            stats = data.getLimb(randLimb);

            damage_pass = random.nextFloat() * 4;
            if (damage_pass > damage) damage_pass = damage;
            float passDamage = applyLocationalArmor(randLimb, Math.min(2, damage_pass), player, true, false, false, false);

            applyConcussion(data, randLimb, passDamage);
            stats.addPain(data.painFromDamage(passDamage * (random.nextFloat() / 2 + 1f)));
            data.applyMuscleDamage(randLimb, passDamage * (random.nextFloat() / 2 + 0.5f), player);
            data.applySkinDamage(randLimb, passDamage * (random.nextFloat() / 2 + 0.5f));

            if (random.nextFloat() < i * 0.2) {
                stats.addPain(data.painFromDamage(passDamage * (random.nextFloat() / 2 + 1.5f)));
                data.applyMuscleDamage(randLimb, passDamage * (random.nextFloat() / 2 + 0.7f), player);
                data.applySkinDamage(randLimb, passDamage * (random.nextFloat() / 2 + 0.7f));
                data.applyBleedDamage(randLimb, passDamage * (random.nextFloat() / 2 + 0.7f), player);
                damage -= damage_pass;
            }

            damage -= damage_pass;
            hurtArmor(randLimb, player, damage_pass);

            boolean amputated = handleAmputation(data, randLimb, stats, passDamage, 15 + 5 + 6, player);
            if (amputated) {
                damage /= 4;
            }
        }
    }

    private static void hurtArmor(Limb limb, Player player, float damage) {
        ItemStack item = null;
        EquipmentSlot eq = null;
        switch (limb) {
            case THORAX, UPPER_LEFT_ARM, LOWER_LEFT_ARM, UPPER_RIGHT_ARM, LOWER_RIGHT_ARM, LEFT_HAND, RIGHT_HAND -> {
                item = player.getItemBySlot(EquipmentSlot.CHEST);
                eq = EquipmentSlot.CHEST;
            }
            case HEAD -> {
                item = player.getItemBySlot(EquipmentSlot.HEAD);
                eq = EquipmentSlot.HEAD;
            }
            case UPPER_RIGHT_LEG, LOWER_RIGHT_LEG, UPPER_LEFT_LEG, LOWER_LEFT_LEG -> {
                item = player.getItemBySlot(EquipmentSlot.LEGS);
                eq = EquipmentSlot.LEGS;
            }
            case RIGHT_FOOT, LEFT_FOOT -> {
                item = player.getItemBySlot(EquipmentSlot.FEET);
                eq = EquipmentSlot.FEET;
            }
        }
        if (item != null) {
            int amount = (int) Math.max(0, damage / 2f);
            if (amount < -1) return;
            EquipmentSlot finalEq = eq;
            item.hurtAndBreak(amount, player, player1 -> player1.broadcastBreakEvent(finalEq));
        }
    }

    @SubscribeEvent
    public static void onHeal(LivingHealEvent event) {
        if (event.getEntity() instanceof Player player) {
            float amount = event.getAmount();
            event.setAmount(0);
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h -> handleMagicHeal(h, amount));
        }
    }

    public static void handleMagicHeal(PlayerHealthData data, float amount) {
        float healAmount = amount * ServerConfig.MAGICAL_HEAL_RATE.get().floatValue();
        LimbStatistics stats;
        for (Limb limb : Limb.values()) {
            stats = data.getLimb(limb);
            if (stats.isAmputated()) continue;
            if (stats.getBleedRate() > 0) stats.setBleedRate(Math.max(0, stats.getBleedRate() - 0.00005f * healAmount));
            stats.setSkinHealth(Math.min(100, stats.getSkinHealth() + 2 * healAmount));
            stats.setMuscleHealth(Math.min(100, stats.getMuscleHealth() + 2 * healAmount));
        }

        float blood = data.getBloodVolume();
        if (blood < 5) {
            data.setBloodVolume(Math.min(5, blood + 0.02f * healAmount));
        } else if (blood > 5) {
            data.setBloodVolume(Math.max(5, blood - (0.02f * healAmount)));
        }

        if (data.getBloodViscosity() > 10) {
            data.setBloodViscosity(data.getBloodViscosity() - 1 * amount);
        }
    }

    public static Vec3 sweepProjectileStep(Projectile proj, Player target) {
        Vec3 prev = new Vec3(proj.xo, proj.yo, proj.zo); // last tick position
        Vec3 motion = proj.getDeltaMovement();
        Vec3 step = motion.scale(0.25); // break into 4 sub-steps per tick

        AABB box = target.getBoundingBox().inflate(0.05);

        Vec3 pos = prev;
        int steps = (int) Math.ceil(1.0 / 0.25); // 4 steps → adjust if needed

        for (int i = 0; i < steps; i++) {
            Vec3 next = pos.add(step);

            Optional<Vec3> hit = box.clip(pos, next);
            if (hit.isPresent()) {
                return hit.get(); // return exact intersection
            }

            pos = next;
        }

        // No hit → fallback to *final sub-step* (closer to true position)
        return pos;
    }


    public static HitSector detectHit(Player player, Vec3 hitpos) {
        AABB box = player.getBoundingBox();

        // relative vertical position: 0 = feet, 1 = top of head
        double relY = (hitpos.y - box.minY) / box.getYsize();

        // shift hit into player-local space (centered on body)
        Vec3 center = new Vec3(
                (box.minX + box.maxX) / 2.0,
                box.minY + player.getBbHeight() / 2.0,
                (box.minZ + box.maxZ) / 2.0
        );
        Vec3 localHit = hitpos.subtract(center);

        // rotate into player-facing space so +Z = forward, +X = right
        float yaw = player.getYRot(); // degrees
        double rad = Math.toRadians(-yaw);
        double localX = localHit.x * Math.cos(rad) - localHit.z * Math.sin(rad);
        double localZ = localHit.x * Math.sin(rad) + localHit.z * Math.cos(rad);

        // width for left/right detection
        double halfWidth = player.getBbWidth() / 2.0;

        HitSector hitPart;
        if (relY > 0.8) {
            hitPart = HitSector.HEAD;
        } else if (relY > 0.3) {
            // torso height band
            if (localX < -0.2 * halfWidth) {
                hitPart = HitSector.LEFT_ARM;
            } else if (localX > 0.2 * halfWidth) {
                hitPart = HitSector.RIGHT_ARM;
            } else {
                hitPart = HitSector.TORSO;
            }
        } else {
            hitPart = HitSector.LEGS;
        }

        return hitPart;
    }

    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof Player target)) return;
        Player actor = event.getEntity();

        if (actor.level().isClientSide) return;

        // Only when sneaking
        if (!actor.isShiftKeyDown()) return;

        target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h -> {
            if (h.getConsciousness() <= 4) {
                // Vector from target → actor
                double dx = actor.getX() - target.getX();
                double dz = actor.getZ() - target.getZ();
                double dist = Math.sqrt(dx * dx + dz * dz);

                if (dist > 0.001) {
                    double strength = 0.25; // tweak to taste

                    double dy = 0.0;

                    // If dragger is at least 0.75 blocks higher than target → add bump
                    if (actor.getY() - target.getY() >= 0.75) {
                        dy = 0.25; // tweak to taste
                    }

                    target.setDeltaMovement(
                            target.getDeltaMovement().add(dx / dist * strength, dy, dz / dist * strength)
                    );
                    target.hurtMarked = true; // force motion sync
                }

                event.setCanceled(true);
            }
        });
    }


    public static boolean isAnyProjectile(DamageSource source) {
        // 1. Vanilla tag check
        if (source.is(DamageTypeTags.IS_PROJECTILE)) return true;

        // 2. Direct entity instanceof check
        Entity direct = source.getDirectEntity();
        return direct instanceof Projectile;
    }

    static final ResourceKey<DamageType> CBCproj = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("createbigcannons", "cannon_projectile"));
    static final ResourceKey<DamageType> CBCprojbig = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("createbigcannons", "big_cannon_projectile"));
    static final ResourceKey<DamageType> CBCmg = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("createbigcannons", "machine_gun_fire"));
    static final ResourceKey<DamageType> CBCmgwat = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("createbigcannons", "machine_gun_fire_in_water"));
    static final ResourceKey<DamageType> CBCtraff = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("createbigcannons", "traffic_cone"));
    static final ResourceKey<DamageType> CBCshrap = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("createbigcannons", "shrapnel"));
    static final ResourceKey<DamageType> CBCgrape = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("createbigcannons", "grapeshot"));
}
