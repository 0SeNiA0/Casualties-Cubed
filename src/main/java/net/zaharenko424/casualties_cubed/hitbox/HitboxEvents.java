package net.zaharenko424.casualties_cubed.hitbox;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
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
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;

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

        if (src.is(DamageTypeTags.IS_DROWNING) || src.is(DamageTypes.IN_WALL)) {//Handled in PlayerHealthData
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
            data.handleBluntDamage(damageamount, player, Limb.HEAD);
            data.setBrainHealth(data.getBrainHealth() - damageamount);
            event.setAmount(0);
            return;
        }

        if (src.is(DamageTypes.SONIC_BOOM)) {
            data.handleSonicDamage(damageamount, player);
            event.setAmount(0);
            return;
        }

        if (src.is(DamageTypes.FREEZE)) {
            data.setTemperature(data.getTemperature() - damageamount * 1.8f);
            event.setAmount(0);
            return;
        }

        if (src.is(DamageTypes.FALL)) {
            data.handleFallDamage(damageamount, player);
            event.setAmount(0);
            return;
        }

        if (src.is(CBCmg) || src.is(CBCmgwat)) {
            data.handleProjectileDamage(HitSector.getCBCChances(), damageamount, player);
            event.setAmount(0);
            return;
        }

        if (src.is(CBCproj) || src.is(CBCprojbig) || src.is(CBCtraff)) {
            if (damageamount < 5) {
                float finalDamage = damageamount * 15;
                data.handleProjectileDamage(HitSector.getCBCChances(), finalDamage, player);
                data.handleProjectileDamage(HitSector.getCBCChances(), finalDamage, player);
                data.handleProjectileDamage(HitSector.getCBCChances(), finalDamage, player);
                data.handleProjectileDamage(HitSector.getCBCChances(), finalDamage, player);
                data.handleProjectileDamage(HitSector.getCBCChances(), finalDamage, player);
                data.handleProjectileDamage(HitSector.getCBCChances(), finalDamage, player);
                data.handleProjectileDamage(HitSector.getCBCChances(), finalDamage, player);
                data.handleProjectileDamage(HitSector.getCBCChances(), finalDamage, player);
            } else {
                data.handleExplosionDamage(damageamount, true, player);
            }

            event.setAmount(0);
            return;
        }

        if (src.is(DamageTypeTags.IS_EXPLOSION)) {
            data.handleExplosionDamage(damageamount, event.getSource().is(CasualtiesCubedTags.DamageType.SHRAPNELL), player);
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
                data.handleProjectileDamage(hit, damageamount, player);
                event.setAmount(0);
                return;
            }
        } else if (src.is(CasualtiesCubedTags.DamageType.MAGIC) || event.getSource().is(DamageTypes.MAGIC)) {
            data.handleMagicDamage(damageamount, (ServerPlayer) player);
            event.setAmount(0);
            return;
        } else if (src.is(DamageTypeTags.IS_FIRE)) {
            data.handleFireDamage(damageamount, player);
            event.setAmount(0);
            return;
        } else if (src.getEntity() instanceof Player shooter) {
            double range = 400.0;
            EntityHitResult hitPos = rayTraceLivingEntity(shooter, range);
            if (hitPos == null) {
                return;
            }
            // Your custom hit sector logic
            HitSector hit = detectHit(player, hitPos.getLocation());
            // This damage value is AFTER vanilla reductions (armor, resistance, etc.)
            float finalDamage = event.getAmount();
            // Call into your capability with final damage
            data.handleProjectileDamage(hit, finalDamage, player);
            event.setAmount(0);
            return;
        } else if (src.is(DamageTypeTags.BYPASSES_ARMOR)) {
            data.handleRandomDamage(damageamount, player);
            event.setAmount(0);
            return;
        }
        //fuck you warium

        data.handleRandomDamage(damageamount, player);
        event.setAmount(0);
    }


    //@SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void oLivingDamage(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        float absorb = player.getAbsorptionAmount();
        float damageamount = event.getAmount();

        if (player.hasEffect(MobEffects.DAMAGE_RESISTANCE)) {
            int amp = player.getEffect(MobEffects.DAMAGE_RESISTANCE).getAmplifier();
            float reduction = 0.2f * (amp + 1); // 20% per level
            damageamount *= (1.0f - reduction);
        }
        if (absorb > 0) {
            float reduction = Math.min(absorb, damageamount * 0.75f);
            player.setAbsorptionAmount(absorb - reduction);
            damageamount -= reduction;
        }
        damageamount = Math.max(damageamount, 0);

        /*
        player.sendSystemMessage(Component.literal(
                "Source: " + event.getSource().toString()
                        + " | amt: " + damageamount
                        + " | bypassArmor: " + event.getSource().is(DamageTypeTags.BYPASSES_ARMOR)
                        + " | bypassShield: " + event.getSource().is(DamageTypeTags.BYPASSES_SHIELD)
                        + " | bypassEnchant: " + event.getSource().is(DamageTypeTags.BYPASSES_ENCHANTMENTS)
                        + " | ignoredTag: " + event.getSource().is(ModDamageTypeTags.IGNORE)
                        + " | absorb: " + absorb
        ));

         */

    }


    @SubscribeEvent
    public static void onHeal(LivingHealEvent event) {
        if (event.getEntity() instanceof Player player) {
            float amount = event.getAmount();
            event.setAmount(0);
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h -> {
                h.handleMagicHeal(amount);
            });
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
        if (direct instanceof Projectile) return true;

        return false;
    }

    static final ResourceKey<DamageType> CBCproj = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("createbigcannons", "cannon_projectile"));
    static final ResourceKey<DamageType> CBCprojbig = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("createbigcannons", "big_cannon_projectile"));
    static final ResourceKey<DamageType> CBCmg = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("createbigcannons", "machine_gun_fire"));
    static final ResourceKey<DamageType> CBCmgwat = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("createbigcannons", "machine_gun_fire_in_water"));
    static final ResourceKey<DamageType> CBCtraff = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("createbigcannons", "traffic_cone"));
    static final ResourceKey<DamageType> CBCshrap = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("createbigcannons", "shrapnel"));
    static final ResourceKey<DamageType> CBCgrape = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("createbigcannons", "grapeshot"));


    public static EntityHitResult rayTraceLivingEntity(LivingEntity shooter, double range) {
        Level world = shooter.level();
        Vec3 start = shooter.getEyePosition(1.0F);
        Vec3 direction = shooter.getViewVector(1.0F).normalize();
        Vec3 end = start.add(direction.scale(range));

        // Raytrace for blocks first
        ClipContext blockContext = new ClipContext(
                start,
                end,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                shooter
        );
        BlockHitResult blockHit = world.clip(blockContext);

        Vec3 finalEnd = end;
        if (blockHit.getType() == HitResult.Type.BLOCK) {
            finalEnd = blockHit.getLocation();
        }

        // Build an AABB along the ray
        AABB scanBox = shooter.getBoundingBox().expandTowards(direction.scale(range)).inflate(1.0);
        List<LivingEntity> candidates = world.getEntitiesOfClass(
                LivingEntity.class,
                scanBox,
                e -> e != shooter && e.isAlive()
        );

        LivingEntity nearest = null;
        double nearestDistSq = Double.MAX_VALUE;
        Vec3 hitPos = null;

        for (LivingEntity target : candidates) {
            AABB box = target.getBoundingBox().inflate(target.getPickRadius());
            Optional<Vec3> optHit = box.clip(start, finalEnd);
            if (optHit.isPresent()) {
                double distSq = start.distanceToSqr(optHit.get());
                if (distSq < nearestDistSq) {
                    nearestDistSq = distSq;
                    nearest = target;
                    hitPos = optHit.get();
                }
            }
        }

        if (nearest != null && hitPos != null) {
            return new EntityHitResult(nearest, hitPos);
        }

        return null; // No entity hit
    }

}
