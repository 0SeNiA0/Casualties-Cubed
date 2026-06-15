package net.zaharenko424.casualties_cubed.limbs;

import net.adinvas.prototype_physics.RagdollPart;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.network.PacketDistributor;
import net.zaharenko424.casualties_cubed.CasualtiesCubedTags;
import net.zaharenko424.casualties_cubed.ModDamageTypes;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.compat.TempCompat;
import net.zaharenko424.casualties_cubed.compat.prototype_physics.PhysicsUtil;
import net.zaharenko424.casualties_cubed.compat.serene_seasons.SereneSeasonsUtil;
import net.zaharenko424.casualties_cubed.config.ServerConfig;
import net.zaharenko424.casualties_cubed.hitbox.HitSector;
import net.zaharenko424.casualties_cubed.network.MedicalAction;
import net.zaharenko424.casualties_cubed.network.ModNetwork;
import net.zaharenko424.casualties_cubed.network.packet.ClientboundTriggerLastStandPacket;
import net.zaharenko424.casualties_cubed.registry.ModGameRules;
import net.zaharenko424.casualties_cubed.registry.ModItems;
import net.zaharenko424.casualties_cubed.registry.ModSounds;

import java.util.*;

public class PlayerHealthData {

    private static final String MOVE_SPEED_MODIFIER = "custom_move_speed";
    private static final UUID MOVE_SPEED_MODIFIER_UUID = UUID.nameUUIDFromBytes(MOVE_SPEED_MODIFIER.getBytes());
    private static final String ATTACK_DAMAGE_MODIFIER = "custom_attack_damage";
    private static final UUID ATTACK_DAMAGE_MODIFIER_UUID = UUID.nameUUIDFromBytes(ATTACK_DAMAGE_MODIFIER.getBytes());
    private static final String ATTACK_SPEED_MODIFIER = "custom_attack_speed";
    private static final UUID ATTACK_SPEED_MODIFIER_UUID = UUID.nameUUIDFromBytes(ATTACK_SPEED_MODIFIER.getBytes());

    private final Map<Limb, LimbStatistics> limbStats = new EnumMap<>(Limb.class);
    private final List<DelayedChangeEntry> changeEntries = new ArrayList<>();

    private float blood = 5f;
    private double totalPain = 0f;
    private float consciousness = 100f;
    private float consciousnessCap = 100f;
    private float hemothorax = 0f;
    private float hemothoraxPain = 0f;
    private float internalBleeding = 0f;
    private float Oxygen = 100f;
    private float OxygenCap = 100;
    private float Opioids = 0;
    private float PendingOpioids = 0;
    private float BPM = 70;
    private boolean isBreathing = true;
    private boolean respiratoryArrest = false;
    private float bloodViscosity = 0;
    private float adrenaline = 0;
    private int lifeSupportTimer = 0;

    private float immunity = 100;
    private float antibioticTimer = 0;
    private float drugAddition = 0;
    private float brainHealth = 100;
    private float Shock = 0;
    private float dirtiness = 0;
    private float temperature = 36.6f;
    private float hearingLoss = 0;
    private float flashHearingLoss = 0;

    private boolean leftEyeBlind = false;
    private boolean RightEyeBlind = false;
    private boolean isMouthRemoved = false;

    private boolean LastStand = false;
    private boolean isRagdolled = false;
    private boolean canBreakOutOffRagdoll = false;
    private float Stability = 100;

    private float painscale = 1;
    /*
    TODO: - Make:
        -MAKE Amputations
     */

    //private boolean syncNeeded;

    //passtrough values
    private int hungerLevel = 20;
    private boolean isUnderwater = false;

    public static Optional<PlayerHealthData> of(Player player) {
        return player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).resolve();
    }

    public static PlayerHealthData nonNullOf(Player player) {
        return player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).orElseThrow(() -> new IllegalStateException("No health capability found for player " + player + " (This should never happen)"));
    }

    //1.8L/min = 0.03L/s

    public PlayerHealthData() {
        for (Limb limb : Limb.values()) {
            limbStats.put(limb, new LimbStatistics(this));
        }
    }

    public float getAntibioticTimer() {
        return antibioticTimer;
    }

    public void setAntibioticTimer(float antibioticTimer) {
        this.antibioticTimer = antibioticTimer;
    }

    public float getStability() {
        return Stability;
    }

    public void setStability(float stability) {
        Stability = stability;
    }

    public float getDrugAddition() {
        return drugAddition;
    }

    public void setDrugAddition(float drugAddition) {
        this.drugAddition = drugAddition;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public boolean isLeftEyeBlind() {
        return leftEyeBlind;
    }

    public void setLeftEyeBlind(boolean leftEyeBlind) {
        this.leftEyeBlind = leftEyeBlind;
    }

    public boolean isRightEyeBlind() {
        return RightEyeBlind;
    }

    public void setRightEyeBlind(boolean rightEyeBlind) {
        RightEyeBlind = rightEyeBlind;
    }

    public boolean isMouthRemoved() {
        return isMouthRemoved;
    }

    public void setMouthRemoved(boolean mouthRemoved) {
        isMouthRemoved = mouthRemoved;
    }

    public void setFlashHearingLoss(float flashHearingLoss) {
        this.flashHearingLoss = flashHearingLoss;
    }

    public float getFlashHearingLoss() {
        return flashHearingLoss;
    }

    public void setLastStand(boolean lastStand) {
        LastStand = lastStand;
    }

    public float getDirtiness() {
        return dirtiness;
    }

    public void setDirtiness(float dirtiness) {
        this.dirtiness = dirtiness;
    }

    public float getAdrenaline() {
        return adrenaline;
    }

    public void setAdrenaline(float adrenaline) {
        this.adrenaline = adrenaline;
    }

    public float getBrainHealth() {
        return brainHealth;
    }

    public void setBrainHealth(float brainHealth) {
        this.brainHealth = Mth.clamp(brainHealth, 0, 100);
    }

    public float getImmunity() {
        return immunity;
    }

    public void setImmunity(float immunity) {
        this.immunity = immunity;
    }

    public int getLifeSupportTimer() {
        return lifeSupportTimer;
    }

    public void setLifeSupportTimer(int lifeSupportTimer) {
        this.lifeSupportTimer = lifeSupportTimer;
    }

    public float getShock() {
        return Shock;
    }

    public void setShock(float shock) {
        Shock = shock;
    }

    public float getHearingLoss() {
        return hearingLoss;
    }

    public void setHearingLoss(float hearingLoss) {
        this.hearingLoss = Mth.clamp(hearingLoss, 0, 1);
    }

    public float getInternalBleeding() {
        return internalBleeding;
    }

    public void setInternalBleeding(float internalBleeding) {
        this.internalBleeding = internalBleeding;
    }

    public float getHemothorax() {
        return hemothorax;
    }

    public void setHemothorax(float hemothorax) {
        this.hemothorax = hemothorax;
    }

    public float getBloodVolume() {
        return blood;
    }

    public void setBloodVolume(float liters) {
        blood = liters;
    }

    public float getConsciousness() {
        return consciousness;
    }

    public void setConsciousness(float value) {
        consciousness = Mth.clamp(value, 0, consciousnessCap);
    }

    public void setConsciousnessCap(float value) {
        consciousnessCap = Mth.clamp(value, 0, 100);
    }

    public float getPendingOpioids() {
        return PendingOpioids;
    }

    public void setPendingOpioids(float value) {
        PendingOpioids = value;
    }

    public float getOpioids() {
        return Opioids;
    }

    public void setOpioids(float va) {
        Opioids = va;
    }

    public float getNetOpioids() {
        return Opioids - drugAddition;
    }

    public float getOxygen() {
        return Oxygen;
    }

    public void setOxygen(float value) {
        Oxygen = value;
    }

    public void setOxygenCap(float value) {
        OxygenCap = value;
    }

    public float getBPM() {
        return BPM;
    }

    public boolean isRespiratoryArrest() {
        return respiratoryArrest;
    }

    public void setBloodViscosity(float bloodViscosity) {
        this.bloodViscosity = bloodViscosity;
    }

    public float getBloodViscosity() {
        return bloodViscosity;
    }

    public float getConsciousnessCap() {
        return consciousnessCap;
    }

    public float getOxygenCap() {
        return OxygenCap;
    }

    public boolean isBreathing() {
        return isBreathing;
    }

    public void setBreathing(boolean value) {
        isBreathing = value;
    }

    public double getTotalPain() {
        return totalPain;
    }

    public void setIsUnderwater(boolean val) {
        isUnderwater = val;
    }

    public void setHungerLevel(int hunger) {
        this.hungerLevel = hunger;
    }

    public float getNutritionFactor() {
        // Scale from 0.0 to 1.0 based on hunger (20 max)
        return hungerLevel / 20f;
    }


    public ChipState getChip() {
        return ChipState.ACTIVE;
    }


    public float getMAX_BLEED_RATE() {
        return (float) (ServerConfig.MAX_BLEED_RATE.get() / 20f);
    }

    public double getOPIATE_PAIN_REDUCTION() {
        return ServerConfig.OPIATE_PAIN_REDUCTION.get();
    }

    public LimbStatistics getLimb(Limb limb) {
        return limbStats.computeIfAbsent(limb, l -> new LimbStatistics(this));
    }

    public boolean isAmputated(Limb limb) {
        return getLimb(limb).isAmputated();
    }

    public float getCombinedBleed() {
        float bleed_all = 0f;
        LimbStatistics stats;
        for (Limb limb : limbStats.keySet()) {
            stats = limbStats.get(limb);
            if (!stats.isTourniquet() && !isOppositeToChestUnderTourniquet(limb)) {
                bleed_all += stats.getBleedRate();
            }
        }
        return bleed_all + internalBleeding;
    }

    public boolean isOppositeToChestUnderTourniquet(Limb limb) {
        Limb toCheck = switch (limb) {
            case RIGHT_FOOT -> Limb.RIGHT_LEG;
            case LEFT_FOOT -> Limb.LEFT_LEG;
            case LEFT_HAND -> Limb.LEFT_ARM;
            case RIGHT_HAND -> Limb.RIGHT_ARM;
            default -> null;
        };

        return toCheck != null && getLimb(toCheck).isTourniquet();
    }

    public void recalculateConsciousness() {
        // Base consciousness target from oxygen
        double target = Oxygen;
        boolean hard_cut = false;
        // Hard knockout conditions
        if (Shock > 0.66) {
            target = 0;
            hard_cut = true;
        }
        // Pain reduces target consciousness
        else if (totalPain > 50) {
            target += (50 - totalPain);
        }

        // Clamp to max cap
        target = Math.min(consciousnessCap, target);

        // Calculate difference
        double diff = target - consciousness;

        // Apply regeneration rate (getContiousnessRegen = %/s)
        float regenPerSecond = ServerConfig.CONSCIOUSNESS_REGEN.get().floatValue(); // e.g. 0.5 means 0.5% per second
        float regenPerTick = regenPerSecond / 20f;     // assuming 20 ticks per second

        // Smoothly move toward target
        if (Math.abs(diff) > regenPerTick && !hard_cut && diff > 0) {
            consciousness += Math.signum(diff) * regenPerTick;
        } else {
            consciousness = (float) target; // close enough, snap to target
        }

        // Clamp final value
        consciousness = Mth.clamp(consciousness, 0f, consciousnessCap);
    }

    public void dismember(Limb limb) {
        switch (limb) {
            case LEFT_ARM -> getLimb(Limb.LEFT_HAND).setAmputated(true);
            case RIGHT_LEG -> getLimb(Limb.RIGHT_FOOT).setAmputated(true);
            case LEFT_LEG -> getLimb(Limb.LEFT_FOOT).setAmputated(true);
            case RIGHT_ARM -> getLimb(Limb.RIGHT_HAND).setAmputated(true);
        }
        getLimb(limb).setAmputated(true);
    }

    private boolean handleAmputation(Limb limb, LimbStatistics stats, float damage, float base_damage_treshhold, Player player) {
        if (!ServerConfig.PERMANENT_DAMAGE.get()) return false;

        boolean skipOthers = false;
        float damage_treshold = base_damage_treshhold;
        if ((limb == Limb.HEAD && isMouthRemoved && leftEyeBlind && RightEyeBlind) || limb == Limb.CHEST) {
            damage_treshold *= 2;
            skipOthers = true;
        } else if (limb == Limb.HEAD && (!leftEyeBlind || !RightEyeBlind)) {
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
                if (chance == 1 && !RightEyeBlind) {
                    RightEyeBlind = true;
                } else if ((chance == 2 || chance == 1) && !leftEyeBlind) {
                    leftEyeBlind = true;
                } else if (!isMouthRemoved) {
                    isMouthRemoved = true;
                }
                player.playSound(ModSounds.AMPUTATION.get());
                return true;
            }
            List<Limb> limbList = limb.getConnectedLimbs();
            for (Limb limb1 : limbList) {
                stats = getLimb(limb1);

                stats.setSkinHealth(0);
                stats.setBleedRate(1);
                stats.setPain(200);
                setAdrenaline(Math.max(getAdrenaline(), 125));
            }
            dismember(limb);
            player.playSound(ModSounds.AMPUTATION.get());
            return true;
        } else if (damage >= 6 && !(limb == Limb.CHEST || limb == Limb.HEAD)) {
            if (Math.random() < 0.01) {
                handleAmputation(limb, stats, 1, 0, player);
            }
        }
        return false;
    }

    public void recalcTotalPain() {
        totalPain = limbStats.values().stream().mapToDouble(LimbStatistics::getFinalPain).max().orElse(0f);
        totalPain = Math.max(totalPain, hemothoraxPain);
        totalPain = Math.max(0, totalPain - adrenaline);
    }

    public double getMaxInfection() {
        double infection = limbStats.values().stream().mapToDouble(LimbStatistics::getInfection).max().orElse(0d);
        return Math.max(infection, 0);
    }

    public float painFromDamage(float damage) {
        return damage * ServerConfig.PAIN_PER_DAMAGE.get().floatValue();
    }

    public void applyPain(Limb limb, float value) {
        applyPain(getLimb(limb), value);
    }

    private void applyPain(LimbStatistics limb, float value) {
        limb.addPain(value * painscale);
    }

    public void applySkinDamage(Limb limb, float damage) {
        applySkinDamage(getLimb(limb), damage);
    }

    private void applySkinDamage(LimbStatistics limb, float damage) {
        limb.addSkinHealth(-damage * ServerConfig.DAMAGE_SCALE.get().floatValue());
        limb.setSkinHeal(false);
        limb.setMuscleHeal(false);
    }

    public void applyMuscleDamage(Limb limb, float damage, Player player) {
        applyMuscleDamage(limb, getLimb(limb), damage, player);
    }

    private void applyMuscleDamage(Limb limb, LimbStatistics stats, float damage, Player player) {
        if (stats.getMuscleHealth() < 100 && damage > 2) {
            float bone_damage_chance = (float) ((100 - stats.getMuscleHealth()) / 100 * ServerConfig.FRAC_DISL_FROM_MUSCLE_DAMAGE_CHANCE.get());
            if (Math.random() > 0.5) {
                if (Math.random() < bone_damage_chance || damage > 15) {
                    stats.setFracture(Math.max(stats.getFracture(), 30 + (damage / 10) * 70));
                    if (player.level().isClientSide())
                        player.playSound(ModSounds.BROKEN_BONE.get());
                }
            } else {
                if (Math.random() < bone_damage_chance || damage > 15) {
                    stats.setDislocation(Math.max(stats.getDislocation(), 30 + (damage / 10) * 70));
                    if (player.level().isClientSide())
                        player.playSound(ModSounds.BROKEN_BONE.get());
                }
            }

        }
        if (limb == Limb.CHEST && Math.random() > 0.5) {
            internalBleeding += (damage / 15) * (getMAX_BLEED_RATE() / 3);
        }
        stats.addMuscleHealth(-damage * ServerConfig.DAMAGE_SCALE.get().floatValue());
        stats.setSkinHeal(false);
        stats.setMuscleHeal(false);
        if (Math.random() > 0.9 && limb == Limb.HEAD) {
            brainHealth -= (float) (Math.random() * 5);
        }
    }

    public void applyBleedDamage(Limb limb, float damage, Player player) {
        applyBleedDamage(getLimb(limb), damage, player);
        //spawnParticleFromDamage((ServerPlayer) player,damage);
    }

    private void applyBleedDamage(LimbStatistics limb, float damage, Player player) {
        limb.addBleedRate((damage / 15) * getMAX_BLEED_RATE());
    }

    private void applyDirectBleedRate(Limb limb, float value) {
        getLimb(limb).setBleedRate(Mth.clamp(limbStats.get(limb).getBleedRate() - value, 0, 100));
    }

    private void applyConcussion(Limb limb, float damage) {
        if (limb == Limb.HEAD) {
            setConsciousness(consciousness - (Math.max(damage * 2, 10)));
        }
    }

    int tick = 0;
    int breathTick = 0;

    int lastStandAnim = -1;
    boolean JustTriggeredLastStand = false;
    float tempspeedup = 0f;

    public void tickUpdate(ServerPlayer player) {
        if (player.isCreative()) {
            applyPenalties(player);
            return;
        }

        isRagdolled = false;

        canBreakOutOffRagdoll = false;

        if (JustTriggeredLastStand || (lastStandAnim > 0 && !LastStand)) {
            if (JustTriggeredLastStand) {
                lastStandAnim = 130;
                JustTriggeredLastStand = false;
                return;
            }
            if (lastStandAnim > 1) {
                lastStandAnim--;
                return;
            }
            LastStand = true;
            lastStandAnim = 0;
            float newBrain = (float) (75f + (Math.random() * 15));
            player.getFoodData().setFoodLevel(20);
            player.getFoodData().setSaturation(20);
            //thirst if i add it
            //sickness if i add it
            blood = Math.max(blood, 3.5f);
            if (player.isUnderWater()) Oxygen = 100;
            else Oxygen = 20;
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 500, 1));
            internalBleeding *= 0.2f;
            hemothorax *= 0.2f;
            temperature = 36.6f;
            antibioticTimer += 1.5f * 60 * 20;

            LimbStatistics stats;
            for (Limb limb : Limb.values()) {
                stats = getLimb(limb);
                stats.addMuscleHealth(30);
                stats.setInfection(stats.getInfection() * 0.2f);
                stats.setBleedRate(stats.getBleedRate() * 0.2f);
            }

            setOpioids(0);
            setPendingOpioids(0);
            drugAddition = 0;
            brainHealth = newBrain;
            consciousness = 20;
            return;
        }
        if (!LastStand) {
            if (brainHealth < 15 && consciousness <= 10 && (temperature >= 42 || Oxygen <= 4)) {
                float chance = player.level().getGameRules().getInt(ModGameRules.LAST_STAND_CHANCE) / 100f;
                if (Math.random() < chance) {
                    JustTriggeredLastStand = true;
                    ModNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new ClientboundTriggerLastStandPacket());
                }
            }
        }

        if (ServerConfig.DO_TEMP_CHANGE.get()) {
            updateTemperature(player);
        } else {
            temperature = 36.6f;
        }

        calculateImmunity();
        updateDirtyness(player);
        if (flashHearingLoss > 0) {
            flashHearingLoss -= 0.1f / 20f;
        }
        flashHearingLoss = Mth.clamp(flashHearingLoss, 0, 1);
        if (hearingLoss > 0) {
            hearingLoss -= 0.001f / 20f;
        }
        hearingLoss = Mth.clamp(hearingLoss, 0, 1);
        if (adrenaline > 0) {
            adrenaline -= 4f / 20f;
        }
        adrenaline = Math.max(0, adrenaline);
        //Opioid Pending
        if (PendingOpioids > 0) {
            float change = Math.min(2f + tempspeedup, PendingOpioids) / 20f;
            PendingOpioids -= change;
            Opioids += change;
            tempspeedup = (float) Math.min(tempspeedup + 0.05, 3f);
        } else {
            tempspeedup = 0;
        }
        PendingOpioids = Math.max(PendingOpioids, 0);
        isUnderwater = player.isUnderWater();
        hungerLevel = player.getFoodData().getFoodLevel();

        if (breathTick++ > 20) {
            breathTick = 0;
            player.setAirSupply(player.getMaxAirSupply());
        }
        if (player.hasEffect(MobEffects.REGENERATION) && tick++ > 20) {

            tick = 0;
            MobEffectInstance inst = player.getEffect(MobEffects.REGENERATION);
            if (inst != null) {
                handleMagicHeal(inst.getAmplifier() + 1);
            }
        }


        // Death timer check
        if (brainHealth < 0.1 || limbStats.get(Limb.CHEST).isAmputated() || limbStats.get(Limb.HEAD).isAmputated()) {
            if (player.isAlive()) {
                killPlayer(player, false);
                return;
            }
        }

        // Update each limb
        for (Limb limb : limbStats.keySet()) {
            getLimb(limb).tick(limb);
        }

        if (ServerConfig.LIMB_REGROWTH.get() && player.hasEffect(MobEffects.REGENERATION)) {
            int amplifier = player.getEffect(MobEffects.REGENERATION).getAmplifier();
            if (amplifier >= ServerConfig.LIMB_REGROWTH_MIN_REGEN.get()) {
                List<Limb> amputated = new ArrayList<>();
                for (Limb limb : limbStats.keySet()) {
                    if (limbStats.get(limb).isAmputated() && !limbStats.get(limb.getConnectedTo()).isAmputated()) amputated.add(limb);
                }

                float regenAmount = (float) amplifier / amputated.size();
                amputated.forEach(limb -> limbStats.get(limb).progressRegrowth(regenAmount));
            }
        }

        recalcTotalPain();
        if (totalPain > 75) {
            Shock += 0.0015f;
        } else {
            Shock -= 0.003f;
        }
        Shock = Mth.clamp(Shock, 0, 1);

        // Bleeding — internal
        if (internalBleeding > 0) {
            internalBleeding = (float) Math.max(0, internalBleeding - (ServerConfig.WOUND_ANTIBLEED_RATE.get() / 20 / 60));
            internalBleeding = Mth.clamp(internalBleeding, 0, getMAX_BLEED_RATE() / 4);
        }

        // Hemothorax
        hemothorax += internalBleeding * 40;
        if (hemothorax > 0) {
            hemothoraxPain = (float) ((4.0 / 15.0) * hemothorax);
            hemothorax -= (ServerConfig.HEMOTHORAX_HEAL_RATE.get().floatValue() / 20f);
        }

        // Blood calculation
        float totalBleedPerTick = getCombinedBleed();
        blood -= totalBleedPerTick;
        consciousnessCap = 100;

        if (blood > 5.25) consciousnessCap = 80;

        if (consciousnessCap > brainHealth) consciousnessCap = brainHealth;
        double headpenalty = Math.min((limbStats.get(Limb.HEAD).getMuscleHealth() - 50) * 2, 0);

        if (limbStats.get(Limb.HEAD).getMuscleHealth() < 15) {
            limbStats.get(Limb.HEAD).setMuscleHeal(true);
        }
        if (consciousnessCap > 100 + headpenalty) consciousnessCap = (float) (100 + headpenalty);

        float bloodRegenRate = (ServerConfig.BLOOD_REGEN_RATE.get().floatValue() / 20f);
        if (blood > 5) {
            blood = Math.max(5, blood - (bloodRegenRate * getNutritionFactor()));
        } else if (blood < 5) {
            blood = Math.min(5, blood + (bloodRegenRate * getNutritionFactor()));
        }

        if (getNetOpioids() > 0) {
            float negativecons = (float) (getNetOpioids() * ServerConfig.CONS_PENALTY_PER_OPIOID.get());
            consciousnessCap = Math.min(consciousnessCap, 100 - negativecons);
        } else if (getNetOpioids() < -40) {
            brainHealth -= 0.01f / 20f;
        }

        if (brainHealth < 30) consciousnessCap = 0;

        if (Opioids > 0) {
            drugAddition += 0.05f / 20f;
        } else {
            drugAddition += -0.05f / 20f;
        }
        drugAddition = Math.max(0, drugAddition);
        if (drugAddition > 42) {
            brainHealth -= 0.05f / 20f;
        }

        // Respiratory arrest condition
        respiratoryArrest = isFreezing || getNetOpioids() > 100 || blood >= 5.7 || getLimb(Limb.CHEST).getMuscleHealth() < 5 || getLimb(Limb.HEAD).isTourniquet();

        // Oxygen cap — based on blood volume and hemothorax
        OxygenCap = 100;
        bloodViscosity = Math.max(0, bloodViscosity - (ServerConfig.BLOOD_VISCOSITY_REGEN.get().floatValue() / 20f));
        if (blood < 4.375) {
            OxygenCap += (160f / 3) * blood - (700f / 3);
        }
        OxygenCap += (-2.0f / 3.0f) * hemothorax;
        OxygenCap -= bloodViscosity;
        OxygenCap = Math.max(0, OxygenCap);

        // Breathing & oxygen change
        isBreathing = (!isUnderwater || player.getEffect(MobEffects.WATER_BREATHING) != null) && !respiratoryArrest && !player.isInWall();

        if (Oxygen > OxygenCap || (!isBreathing && !isUnderwater) || (getAirLossRate(player) > 0 && isUnderwater) || respiratoryArrest) {
            Oxygen = Math.max(0, Oxygen - (ServerConfig.OXYGEN_DRAIN.get().floatValue() / 20f));
        }
        if (isBreathing && (Oxygen <= OxygenCap)) {
            Oxygen = Math.min(100, Oxygen + (ServerConfig.OXYGEN_REPLENISH.get().floatValue() / 20f));
        }

        // Opioid decay
        Opioids = Math.max(0, Opioids - 0.02f);

        // Consciousness
        float oldCont = this.consciousness;
        recalculateConsciousness();

        // Death timer adjustments
        if (Oxygen <= 4) {
            brainHealth -= (ServerConfig.BRAIN_DRAIN.get().floatValue() / 20);
        }
        calculateBrain();
        calculateBPM();
        applyPenalties(player);
        changeEntries.removeIf(entry -> {
            entry.reduceTicks(1); // decrement by 1 tick
            applyDirectBleedRate(entry.getLimb(), entry.getAmount_per_tick());
            return entry.getTicks() <= 0; // remove if done
        });


        if (oldCont <= 4 && this.consciousness > 4) {
            if (player.level().isClientSide) return; // only server-side

            for (ServerPlayer other : player.getServer().getPlayerList().getPlayers()) {
                if (other.containerMenu == player.inventoryMenu) {
                    other.closeContainer(); // kicks them out of target's inventory
                }
            }
        }
        if (lifeSupportTimer > 0) {
            lifeSupportTimer--;
            Oxygen = Math.max(Oxygen, 50);
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 10, 0));
            adrenaline = Math.max(70, adrenaline);
        }
        if (leftEyeBlind && RightEyeBlind) {
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 100, 1, true, false));
        }

        boolean isUnc = getConsciousness() <= 10;
        if (isUnc) {
            player.zza = 0;
            player.xxa = 0;
            player.yRotO = 0; // Stop looking around
            player.xRotO = 0;

        }

        if (PhysicsUtil.isPhysicsActivated(player) && PhysicsUtil.isPhysicsLoaded()) {
            Stability = calculateStability(player);
            Vec3 vel = PhysicsUtil.getVel(RagdollPart.TORSO, player);
            isRagdolled = isUnc || Stability <= 10;
            canBreakOutOffRagdoll = Stability > 50 && vel.length() < 0.5f;

            if (isRagdolled) {
                PhysicsUtil.setPhysics(true, player, 20, 0);
                player.fallDistance = 0;
            } else {
                if (canBreakOutOffRagdoll) {
                    PhysicsUtil.setPhysics(false, player, 0, 0);
                }
            }
        } else {
            Stability = 100;
        }

        blood = Math.max(0, blood);
    }

    public void calculateBrain() {
        if (Oxygen > 5) {
            if (ServerConfig.PERMANENT_DAMAGE.get()) {
                brainHealth = Mth.clamp(brainHealth + (ServerConfig.BRAIN_HEALTH_REGEN.get().floatValue() / 20f / 60f), 0, 100);
            } else {
                brainHealth = Mth.clamp(brainHealth + (5 / 20f), 0, 100);
            }
        }
    }

    public boolean isFreezing = false;

    public void applyPenalties(ServerPlayer player) {
        double baseMoveSpeed = 0.1;
        double baseAttackDamage = 1.0;
        double baseAttackSpeed = 4.0;
// --- Calculate limb-based multipliers ---
        LimbStatistics rightLeg = getLimb(Limb.RIGHT_LEG),
                        leftLeg = getLimb(Limb.LEFT_LEG);
        double moveReduction;
        if (rightLeg.isAmputated() && leftLeg.isAmputated()) {
            moveReduction = 0.2;
        } else {
            moveReduction =
                    ((100 - rightLeg.getMuscleHealth()) / 100.0) * 0.14 +
                    ((100 - leftLeg.getMuscleHealth()) / 100.0) * 0.14 +
                    ((100 - getLimb(Limb.RIGHT_FOOT).getMuscleHealth()) / 100.0) * 0.14 +
                    ((100 - getLimb(Limb.LEFT_FOOT).getMuscleHealth()) / 100.0) * 0.14;
        }

        HumanoidArm handpart = Limb.getArmFromHand(InteractionHand.MAIN_HAND, player);
        LimbStatistics rightArm = getLimb(Limb.RIGHT_ARM),
                        rightHand = getLimb(Limb.RIGHT_HAND),
                        leftArm = getLimb(Limb.LEFT_ARM),
                        leftHand = getLimb(Limb.LEFT_HAND);
        double attackMultiplier;
        if (handpart == HumanoidArm.RIGHT) {
            attackMultiplier = ((100 - rightArm.getMuscleHealth()) / 100.0) * 0.25 +
                    ((100 - leftArm.getMuscleHealth()) / 100.0) * 0.10 +
                    ((100 - rightHand.getMuscleHealth()) / 100.0) * 0.25 +
                    ((100 - leftHand.getMuscleHealth()) / 100.0) * 0.10;
            if (leftHand.isAmputated()) attackMultiplier -= 0.1;
            if (leftArm.isAmputated()) attackMultiplier -= 0.1;
        } else {
            attackMultiplier = ((100 - rightArm.getMuscleHealth()) / 100.0) * 0.1 +
                    ((100 - leftArm.getMuscleHealth()) / 100.0) * 0.25 +
                    ((100 - rightHand.getMuscleHealth()) / 100.0) * 0.10 +
                    ((100 - leftHand.getMuscleHealth()) / 100.0) * 0.25;
            if (rightHand.isAmputated()) attackMultiplier -= 0.1;
            if (rightArm.isAmputated()) attackMultiplier -= 0.1;
        }

        double moveMultiplier = 1.0 - moveReduction;
        isFreezing = false;
        painscale = 1f;

// --- Temperature effects ---
        if (temperature > 42) {
            for (Limb limb : Limb.values()) {
                applyPain(limb, 0.1f / 20f);
            }
            brainHealth -= 0.5f / 20f;
            moveMultiplier -= 0.1f;
        } else if (temperature > 41) {
            moveMultiplier -= 0.05f;
        } else if (temperature < 27) {
            moveMultiplier -= 0.2f;
            consciousness -= ((ServerConfig.CONSCIOUSNESS_REGEN.get().floatValue() + 0.25f) / 20f);
            if (consciousness < 10) {
                isFreezing = true;
            }
            painscale = 1.5f;
        } else if (temperature < 32) {
            painscale = 1.3f;
            moveMultiplier -= 0.1f;
        } else if (temperature < 33) {
            painscale = 1.1f;
            moveMultiplier -= 0.07f;
        } else if (temperature < 35) {
            moveMultiplier -= 0.05f;
        }
// --- Apply modifiers safely ---
        applyAttributeModifier(player, Attributes.MOVEMENT_SPEED, MOVE_SPEED_MODIFIER, MOVE_SPEED_MODIFIER_UUID,
                (baseMoveSpeed * Math.max(0.0, moveMultiplier)) - baseMoveSpeed,
                AttributeModifier.Operation.ADDITION);

        applyAttributeModifier(player, Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE_MODIFIER, ATTACK_DAMAGE_MODIFIER_UUID,
                (baseAttackDamage * (1 - attackMultiplier)) - baseAttackDamage,
                AttributeModifier.Operation.ADDITION);

        applyAttributeModifier(player, Attributes.ATTACK_SPEED, ATTACK_SPEED_MODIFIER, ATTACK_SPEED_MODIFIER_UUID,
                (baseAttackSpeed * (1 - attackMultiplier)) - baseAttackSpeed,
                AttributeModifier.Operation.ADDITION);

        LimbStatistics stats;
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.isEmpty()) continue;

            HumanoidArm arm = Limb.getArmFromHand(hand, player);
            Limb limb = (arm == HumanoidArm.LEFT) ? Limb.LEFT_HAND : Limb.RIGHT_HAND;
            stats = getLimb(limb);
            boolean broken = stats.getMuscleHealth() < 10
                    || stats.getFracture() > 0
                    || stats.getDislocation() > 0;

            if (broken) {
                player.setItemInHand(hand, ItemStack.EMPTY);

                int handSlot = player.getInventory().selected; // hotbar index of hand
                if (player.getInventory().getItem(handSlot).isEmpty()) {
                    // don't add to inventory, just drop
                    player.drop(stack, false);
                } else {
                    // safe to add to other inventory slots
                    ItemStack leftover = player.getInventory().add(stack) ? ItemStack.EMPTY : stack;
                    if (!leftover.isEmpty()) player.drop(leftover, false);
                }
            }
        }
    }

    private static void applyAttributeModifier(Player player, Attribute attribute, String name, UUID uuid, double amount, AttributeModifier.Operation operation) {
        var instance = player.getAttribute(attribute);
        if (instance == null) return;

        instance.removeModifier(uuid);

        // Skip adding modifier if multiplier = 0 (no change)
        if (amount == 0.0) return;

        instance.addTransientModifier(new AttributeModifier(uuid, name, amount, operation));
    }

    private static void removeAttributeModifier(Player player, Attribute attribute, UUID uuid) {
        var instance = player.getAttribute(attribute);
        if (instance == null) return;

        instance.removeModifier(uuid);
    }

    public void clearAttributePenalties(Player player) {
        removeAttributeModifier(player, Attributes.MOVEMENT_SPEED, MOVE_SPEED_MODIFIER_UUID);
        removeAttributeModifier(player, Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE_MODIFIER_UUID);
        removeAttributeModifier(player, Attributes.ATTACK_SPEED, ATTACK_SPEED_MODIFIER_UUID);
    }

    public CompoundTag serializeNBT(CompoundTag nbt, boolean full) {
        // Player-wide values
        nbt.putFloat("Blood", blood);
        nbt.putDouble("TotalPain", totalPain);
        nbt.putFloat("Consciousness", consciousness);
        nbt.putFloat("ConsciousnessCap", consciousnessCap);
        nbt.putFloat("Hemothorax", hemothorax);
        nbt.putFloat("HemothoraxPain", hemothoraxPain);
        nbt.putFloat("InternalBleeding", internalBleeding);
        nbt.putFloat("Oxygen", Oxygen);
        nbt.putFloat("OxygenCap", OxygenCap);
        nbt.putFloat("Opioids", Opioids);
        nbt.putFloat("BPM", BPM);
        nbt.putBoolean("IsBreathing", isBreathing);
        nbt.putFloat("BloodViscosity", bloodViscosity);
        nbt.putFloat("BrainHealth", brainHealth);
        nbt.putFloat("Immunity", immunity);
        nbt.putFloat("Drug_addition", drugAddition);
        nbt.putFloat("Shock", Shock);
        nbt.putFloat("Dirty", dirtiness);
        nbt.putFloat("Temp", temperature);
        nbt.putFloat("Adrenaline", adrenaline);
        nbt.putInt("LifeSupport", lifeSupportTimer);
        nbt.putBoolean("LeftEyeBlind", leftEyeBlind);
        nbt.putBoolean("RightEyeBlind", RightEyeBlind);
        nbt.putBoolean("MouthMissing", isMouthRemoved);
        nbt.putFloat("HearingLoss", hearingLoss);
        nbt.putFloat("FlashHearing", flashHearingLoss);
        nbt.putBoolean("LastStand", LastStand);
        nbt.putFloat("Stability", Stability);

        ListTag changeList = new ListTag();
        for (DelayedChangeEntry entry : changeEntries) {
            changeList.add(entry.toNBT());
        }
        nbt.put("ChangeList", changeList);

        // Serialize limb data as a list
        ListTag limbList = new ListTag();
        CompoundTag limbTag;
        for (Map.Entry<Limb, LimbStatistics> entry : limbStats.entrySet()) {
            if (!full && !entry.getValue().sync()) continue;

            limbTag = new CompoundTag();
            limbTag.putString("LimbName", entry.getKey().name());
            entry.getValue().save(limbTag);
            limbList.add(limbTag);
        }
        nbt.put("LimbStats", limbList);

        return nbt;
    }

    public void deserializeNBT(CompoundTag nbt) {
        if (nbt.contains("Reduced")) {//Short-circuit reduced data
            deserializeReducedNBT(nbt);
            return;
        }

        if (nbt.contains("Blood"))
            blood = nbt.getFloat("Blood");
        if (nbt.contains("TotalPain"))
            totalPain = nbt.getDouble("TotalPain");
        if (nbt.contains("Consciousness"))
            consciousness = nbt.getFloat("Consciousness");
        if (nbt.contains("ConsciousnessCap"))
            consciousnessCap = nbt.getFloat("ConsciousnessCap");
        if (nbt.contains("Hemothorax"))
            hemothorax = nbt.getFloat("Hemothorax");
        if (nbt.contains("HemothoraxPain"))
            hemothoraxPain = nbt.getFloat("HemothoraxPain");
        if (nbt.contains("InternalBleeding"))
            internalBleeding = nbt.getFloat("InternalBleeding");
        if (nbt.contains("Oxygen"))
            Oxygen = nbt.getFloat("Oxygen");
        if (nbt.contains("OxygenCap"))
            OxygenCap = nbt.getFloat("OxygenCap");
        if (nbt.contains("Opioids"))
            Opioids = nbt.getFloat("Opioids");
        if (nbt.contains("BPM"))
            BPM = nbt.getFloat("BPM");
        if (nbt.contains("IsBreathing"))
            isBreathing = nbt.getBoolean("IsBreathing");
        if (nbt.contains("BloodViscosity"))
            bloodViscosity = nbt.getFloat("BloodViscosity");
        if (nbt.contains("BrainHealth"))
            brainHealth = nbt.getFloat("BrainHealth");

        if (nbt.contains("Immunity"))
            immunity = nbt.getFloat("Immunity");
        if (nbt.contains("Drug_addition"))
            drugAddition = nbt.getFloat("Drug_addition");
        if (nbt.contains("Shock"))
            Shock = nbt.getFloat("Shock");
        if (nbt.contains("Dirty"))
            dirtiness = nbt.getFloat("Dirty");
        if (nbt.contains("Temp"))
            temperature = nbt.getFloat("Temp");
        if (nbt.contains("Adrenaline"))
            adrenaline = nbt.getFloat("Adrenaline");
        if (nbt.contains("LifeSupport"))
            lifeSupportTimer = nbt.getInt("LifeSupport");
        if (nbt.contains("LeftEyeBlind"))
            leftEyeBlind = nbt.getBoolean("LeftEyeBlind");
        if (nbt.contains("RightEyeBlind"))
            RightEyeBlind = nbt.getBoolean("RightEyeBlind");
        if (nbt.contains("MouthMissing"))
            isMouthRemoved = nbt.getBoolean("MouthMissing");
        if (nbt.contains("HearingLoss"))
            hearingLoss = nbt.getFloat("HearingLoss");
        if (nbt.contains("FlashHearing"))
            flashHearingLoss = nbt.getFloat("FlashHearing");
        if (nbt.contains("LastStand"))
            LastStand = nbt.getBoolean("LastStand");
        if (nbt.contains("Stability"))
            Stability = nbt.getFloat("Stability");

        changeEntries.clear();
        ListTag changeList = nbt.getList("ChangeList", 10);
        for (int i = 0; i < changeList.size(); i++) {
            CompoundTag changeTag = changeList.getCompound(i);
            changeEntries.add(DelayedChangeEntry.fromNBT(changeTag));
        }

        ListTag limbList = nbt.getList("LimbStats", Tag.TAG_COMPOUND);
        CompoundTag limbTag;
        for (int i = 0; i < limbList.size(); i++) {
            limbTag = limbList.getCompound(i);

            // ✅ Get existing stats or create if missing
            limbStats.computeIfAbsent(Limb.valueOf(limbTag.getString("LimbName")),
                    k -> new LimbStatistics(this)).load(limbTag);
        }
    }

    public CompoundTag serializeReducedNbt(boolean full) {
        ListTag limbList = new ListTag();
        for (Map.Entry<Limb, LimbStatistics> entry : limbStats.entrySet()) {
            if (!full && !entry.getValue().softSync()) continue;

            CompoundTag limbTag = new CompoundTag();
            limbTag.putString("LimbName", entry.getKey().name());
            LimbStatistics stats = entry.getValue();
            limbTag.putBoolean("Amputated", stats.isAmputated());
            limbList.add(limbTag);
        }

        if (limbList.isEmpty()) return null;

        CompoundTag tag = new CompoundTag();
        tag.putBoolean("Reduced", true);
        tag.put("LimbStats", limbList);
        return tag;
    }

    public void deserializeReducedNBT(CompoundTag nbt) {
        ListTag list = nbt.getList("LimbStats", Tag.TAG_COMPOUND);
        CompoundTag limbTag;
        for (Tag tag : list) {
            limbTag = (CompoundTag) tag;
            limbStats.computeIfAbsent(Limb.valueOf(limbTag.getString("LimbName")),
                    k -> new LimbStatistics(this)).setAmputated(limbTag.getBoolean("Amputated"));
        }
    }

    public void copyFrom(PlayerHealthData other) {
        this.blood = other.blood;
        this.totalPain = other.totalPain;
        this.consciousness = other.consciousness;
        this.consciousnessCap = other.consciousnessCap;
        this.hemothorax = other.hemothorax;
        this.hemothoraxPain = other.hemothoraxPain;
        this.internalBleeding = other.internalBleeding;
        this.Oxygen = other.Oxygen;
        this.OxygenCap = other.OxygenCap;
        this.Opioids = other.Opioids;
        this.BPM = other.BPM;
        this.isBreathing = other.isBreathing;
        this.bloodViscosity = other.bloodViscosity;
        this.brainHealth = other.brainHealth;
        this.immunity = other.immunity;
        this.antibioticTimer = other.antibioticTimer;
        this.drugAddition = other.drugAddition;
        this.Shock = other.Shock;
        this.dirtiness = other.dirtiness;
        this.temperature = other.temperature;
        this.adrenaline = other.adrenaline;
        this.lifeSupportTimer = other.lifeSupportTimer;
        this.isMouthRemoved = other.isMouthRemoved;
        this.leftEyeBlind = other.leftEyeBlind;
        this.RightEyeBlind = other.RightEyeBlind;
        this.hearingLoss = other.hearingLoss;
        this.flashHearingLoss = other.flashHearingLoss;
        this.LastStand = other.LastStand;
        this.Stability = other.Stability;

        this.changeEntries.clear();
        for (DelayedChangeEntry entry : other.changeEntries) {
            this.changeEntries.add(new DelayedChangeEntry(entry.getAmount_per_tick(), entry.getTicks(), entry.getLimb()));
        }

        for (Map.Entry<Limb, LimbStatistics> entry : other.limbStats.entrySet()) {
            Limb limb = entry.getKey();
            LimbStatistics originalStats = entry.getValue();
            limbStats.computeIfAbsent(limb, k -> new LimbStatistics(this)).copyFrom(originalStats);
        }
    }

    public void resetToDefaults() {
        // clear & repopulate limb stats with fresh defaults
        limbStats.clear();
        for (Limb limb : Limb.values()) {
            limbStats.put(limb, new LimbStatistics(this));
        }

        // clear delayed changes
        changeEntries.clear();

        // reset player-wide primitives to initial defaults (match the field initializers)
        blood = 5f;
        totalPain = 0f;
        consciousness = 100f;
        consciousnessCap = 100f;
        hemothorax = 0f;
        hemothoraxPain = 0f;
        internalBleeding = 0f;
        Oxygen = 100f;
        OxygenCap = 100f;
        Opioids = 0f;
        BPM = 70;
        isBreathing = true;
        respiratoryArrest = false;
        bloodViscosity = 0f;
        brainHealth = 100;
        drugAddition = 0;
        temperature = 36.6f;
        Shock = 0;
        dirtiness = 0;
        antibioticTimer = 0;
        immunity = 100;
        adrenaline = 0;
        lifeSupportTimer = 0;
        isMouthRemoved = false;
        leftEyeBlind = false;
        RightEyeBlind = false;
        hearingLoss = 0;
        flashHearingLoss = 0;
        LastStand = false;
        isRagdolled = false;
        Stability = 100;

        // passthrough / player-state
        hungerLevel = 20;
        isUnderwater = false;

        // derived values
        recalcTotalPain();
        recalculateConsciousness();
    }

    public void resetToDefaults(ServerPlayer player) {
        resetToDefaults();
        clearAttributePenalties(player);
    }

    public void medicalAction(MedicalAction action, Limb limb, Player source) {
        LimbStatistics stats = getLimb(limb);
        if (stats.isAmputated()) return;

        switch (action) {
            case REMOVE_SPLINT -> {
                if (!stats.hasSplint()) return;

                stats.setHasSplint(false);
                source.getInventory().add(new ItemStack(ModItems.SPLINT.get()));
            }
            case REMOVE_TOURNIQUET -> {
                if (!stats.isTourniquet()) return;

                stats.setTourniquet(false);
                ItemHandlerHelper.giveItemToPlayer(source, new ItemStack(ModItems.TOURNIQUET.get()));
            }
        }
    }

    public void handleSonicDamage(float damage, Player player) {
        setAdrenaline(Math.max(10 * damage, adrenaline));

        RandomSource random = player.getRandom();
        LimbStatistics stats;
        for (Limb limb : Limb.values()) {
            stats = getLimb(limb);

            stats.addPain((limb == Limb.HEAD ? 10 : 4) * damage);
            stats.addMuscleHealth(-(2 + 1 * random.nextFloat()) * damage);
        }

        setBrainHealth(brainHealth - .5f * damage);//200 sonic boom damage will insta kill
        setHearingLoss(Math.max(.06f * damage, hearingLoss));
        setConsciousness(consciousness - 6.9f * damage);
        setInternalBleeding(internalBleeding + (0.0171f + 0.00855f * random.nextFloat()) * damage);
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

    public void handleFallDamage(float damageValue, Player player) {
        RandomSource random = player.getRandom();
        setAdrenaline(Math.max(getAdrenaline(), damageValue * 2));
        float remainingDamage = damageValue * 1;
        LimbStatistics rightLeg = getLimb(Limb.RIGHT_LEG), rightFoot = getLimb(Limb.RIGHT_FOOT),
                leftLeg = getLimb(Limb.LEFT_LEG), leftFoot = getLimb(Limb.LEFT_FOOT);

        remainingDamage = applyLocationalArmor(Limb.LEFT_FOOT, remainingDamage, player, false, false, false, true);
        float combined_legs = (leftFoot.getMuscleHealth() + rightLeg.getMuscleHealth() + rightFoot.getMuscleHealth() +leftLeg.getMuscleHealth()) / 4;
        float ragdolltreshold = 5 + (combined_legs / 100 * 10);

        if (remainingDamage >= ragdolltreshold) Stability = 0;

        for (float[] stage : FALL_DAMAGE_STAGES) {
            float footMult = stage[0];
            float legMult = stage[1];
            float randChance = stage[2];

            // feet
            if (footMult > 0f) {
                applyMuscleDamage(Limb.LEFT_FOOT, leftFoot, remainingDamage * footMult, player);
                applyPain(leftFoot, painFromDamage(remainingDamage * footMult));
                applyMuscleDamage(Limb.RIGHT_FOOT, rightFoot, remainingDamage * footMult, player);
                applyPain(rightFoot, painFromDamage(remainingDamage * footMult));
            }

            // legs
            if (legMult > 0f) {
                applyMuscleDamage(Limb.LEFT_LEG, leftLeg, remainingDamage * legMult, player);
                applyPain(leftLeg, painFromDamage(remainingDamage * legMult));
                applyMuscleDamage(Limb.RIGHT_LEG, rightLeg, remainingDamage * legMult, player);
                applyPain(rightLeg, painFromDamage(remainingDamage * legMult));
            }

            // random damage
            if (randChance > 0f && random.nextFloat() < randChance) {
                handleRandomDamage(remainingDamage * 0.5f, player);
            }

            // decay for next pass
            remainingDamage *= 0.7f;
            if (remainingDamage < 1f) return;
        }
    }

    public void handleBluntDamage(float damageValue, Player player, Limb limb) {
        RandomSource random = player.getRandom();
        setAdrenaline(Math.max(getAdrenaline(), damageValue * 0.5f));
        float remainingDamage = damageValue * 1;
        LimbStatistics stats = getLimb(limb);

        if (limb == Limb.HEAD)
            remainingDamage *= 0.7f;
        if (limb == Limb.CHEST)
            remainingDamage *= 0.5f;

        remainingDamage = applyLocationalArmor(limb, remainingDamage, player, false, false, false, true);

        applyConcussion(limb, (random.nextFloat() / 2 + 0.5f) * remainingDamage * 6);
        stats.addPain((random.nextFloat() / 2 + 0.5f) * remainingDamage * 8);
        applyMuscleDamage(limb, stats, (random.nextFloat() / 2 + 0.5f) * remainingDamage * 0.8f, player);
        if (remainingDamage > 5) {
            stats.addSkinHealth(- (random.nextFloat() / 2 + 0.5f) * remainingDamage);
            applyBleedDamage(stats, remainingDamage * 2, player);
        }
        if (remainingDamage > 4) {
            for (Limb limb1 : limb.getConnectedLimbs()) {
                handleBluntDamage(remainingDamage / 2f, player, limb1);
            }
        }
    }

    public void handleMagicDamage(float damage, ServerPlayer player) {
        LimbStatistics stats;
        for (Limb limb : limbStats.keySet()) {
            stats = getLimb(limb);

            setAdrenaline(Math.max(getAdrenaline(), damage * 1));
            applyMuscleDamage(limb, stats, (float) (damage * (Math.random() / 4f)), player);
            applyPain(stats, (float) (damage * (Math.random())));
        }
    }

    private float applyLocationalArmor(Limb limb, float damage, Player player, boolean is_fire, boolean is_projectile, boolean is_explosion, boolean is_fall) {
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
            case LEFT_ARM, RIGHT_ARM, LEFT_HAND, RIGHT_HAND -> {
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
            case CHEST -> {
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
            case RIGHT_LEG, LEFT_LEG -> {
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

    public void handleFireDamage(float damage, Player player) {
        setAdrenaline(Math.max(getAdrenaline(), damage));
        int i = 0;
        Limb randLimb;
        LimbStatistics stats;
        while (damage > 0) {
            i++;
            randLimb = Limb.weigtedRandomLimb();
            stats = getLimb(randLimb);

            float damage_pass = (float) (Math.random() * 4);
            if (damage_pass > damage) damage_pass = damage;
            float passDamage = applyLocationalArmor(randLimb, Math.min(2, damage_pass), player, true, false, false, false);
            applyPain(randLimb, passDamage * 5);
            applyConcussion(randLimb, damage_pass);

            damageSkinOrMuscle(randLimb, stats, passDamage);

            stats.addBurn(passDamage * 2.5f);

            if (Math.random() < i * 0.2) {
                applyPain(randLimb, passDamage * 5);
                damageSkinOrMuscle(randLimb, stats, passDamage * 0.75f);
                applyBleedDamage(randLimb, passDamage * .5f, player);

                damage -= 1;
            }

            stats.setBleedRate(stats.getBleedRate() * 0.4f);
            damage -= damage_pass;
            hurtArmor(randLimb, player, damage_pass);
        }
    }

    private void damageSkinOrMuscle(Limb limb, LimbStatistics stats, float passDamage) {
        if (stats.getSkinHealth() == 0) {
            stats.addMuscleHealth(-passDamage * 5);
        } else {
            stats.addMuscleHealth(-passDamage * 3.5f);
            applySkinDamage(limb, passDamage * 5);
        }
    }

    public void handleExplosionDamage(float damage, boolean shrapnell, Player player) {
        setAdrenaline(Math.max(getAdrenaline(), damage * 1));
        float passDamage;
        Limb limb;
        LimbStatistics stats;
        while (damage > 2) {
            passDamage = Math.min(damage, 6);
            limb = Limb.weigtedRandomLimb();
            stats = getLimb(limb);

            passDamage = applyLocationalArmor(limb, passDamage, player, false, false, true, false);
            applyConcussion(limb, passDamage);
            applyMuscleDamage(limb, stats, passDamage * 0.2f, player);
            applySkinDamage(stats, passDamage * 0.8f);
            applyPain(stats, painFromDamage(passDamage));
            applyBleedDamage(stats, passDamage * 0.7f, player);

            float chance = passDamage / 6 + 0.2f;
            if (Math.random() < chance && shrapnell) {
                stats.addShrapnel((int) (Math.random() * 5));
            }

            hurtArmor(limb, player, damage);

            boolean amputated = handleAmputation(limb, stats, passDamage, 15 + 5, player);
            if (amputated) {
                damage /= 4;
            }

            damage -= 2;
        }
    }

    public void calculateBPM() {
        int newBPM = 70;
        int painAdd = (int) (Math.pow((totalPain / 100), 1.4) * 100);

        newBPM = newBPM + painAdd;

        if (blood < 3) {
            newBPM = (int) (newBPM * (blood / 3));
        }
        if (getNetOpioids() > 50) {
            newBPM = (int) (newBPM * 0.80);
        }
        this.BPM = Mth.clamp(newBPM, 0, 170);
    }

    public void handleProjectileDamage(HitSector hitSector, float damage, Player player) {
        setAdrenaline(Math.max(getAdrenaline(), damage * 2));
        RandomSource random = player.getRandom();
        List<Limb> limbList = hitSector.getLimbsPerSector();
        Limb randomLimb = limbList.get(random.nextInt(limbList.size()));
        LimbStatistics stats = getLimb(randomLimb);
        damage = applyLocationalArmor(randomLimb, damage, player, false, true, false, false);

        applyConcussion(randomLimb, damage);
        applyMuscleDamage(randomLimb, stats, (float) (damage * (Math.random() / 2f + 0.5f)), player);
        applyPain(stats, painFromDamage(damage));
        applySkinDamage(stats, (float) (damage * (Math.random() / 2f + 0.5f)));
        applyBleedDamage(stats, damage * 0.9f, player);
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
        boolean amputated = handleAmputation(randomLimb, stats, damage, 15 + 5 + 10, player);
        if (amputated) {
            damage /= 4;//?
        }
    }

    public void handleRandomDamage(float damage, Player player) {
        setAdrenaline(Math.max(getAdrenaline(), damage * 1));
        int i = 0;
        LimbStatistics stats;
        float damage_pass;
        while (damage > 0) {
            i++;
            Limb randLimb = Limb.weigtedRandomLimb();
            stats = getLimb(randLimb);

            damage_pass = (float) (Math.random() * 4);
            if (damage_pass > damage) damage_pass = damage;
            float passDamage = applyLocationalArmor(randLimb, Math.min(2, damage_pass), player, true, false, false, false);

            applyConcussion(randLimb, passDamage);
            applyPain(stats, painFromDamage((float) (passDamage * (Math.random() / 2 + 1f))));
            applyMuscleDamage(randLimb, stats, (float) (passDamage * (Math.random() / 2 + 0.5f)), player);
            applySkinDamage(stats, (float) (passDamage * (Math.random() / 2 + 0.5f)));

            if (Math.random() < i * 0.2) {
                applyPain(stats, painFromDamage((float) (passDamage * (Math.random() / 2 + 1.5f))));
                applyMuscleDamage(randLimb, stats, (float) (passDamage * (Math.random() / 2 + 0.7f)), player);
                applySkinDamage(stats, (float) (passDamage * (Math.random() / 2 + 0.7f)));
                applyBleedDamage(stats, (float) (passDamage * (Math.random() / 2 + 0.7f)), player);
                damage -= damage_pass;
            }

            damage -= damage_pass;
            hurtArmor(randLimb, player, damage_pass);

            boolean amputated = handleAmputation(randLimb, stats, passDamage, 15 + 5 + 6, player);
            if (amputated) {
                damage /= 4;
            }
        }
    }


    public void addDelayedChange(float totalBleedAmount, int timeInTicks, Limb limb) {
        float bleed = totalBleedAmount / timeInTicks;
        changeEntries.add(new DelayedChangeEntry(bleed, timeInTicks, limb));
    }

    public void onArmUse(InteractionHand hand, Player player) {
        HumanoidArm arm = Limb.getArmFromHand(hand, player);

        if (arm == HumanoidArm.LEFT) {
            LimbStatistics leftArm = getLimb(Limb.LEFT_ARM), leftHand = getLimb(Limb.LEFT_HAND);
            if ((leftHand.getFracture() > 0 || leftHand.getDislocation() > 0))
                applyPain(Limb.LEFT_HAND, 0.5f);
            if ((leftArm.getFracture() > 0 || leftArm.getDislocation() > 0))
                applyPain(Limb.LEFT_ARM, 0.5f);
        } else {
            LimbStatistics rightArm = getLimb(Limb.RIGHT_ARM), rightHand = getLimb(Limb.RIGHT_HAND);
            if ((rightHand.getFracture() > 0 || rightHand.getDislocation() > 0))
                applyPain(Limb.RIGHT_HAND, 0.5f);
            if ((rightArm.getFracture() > 0 || rightArm.getDislocation() > 0))
                applyPain(Limb.RIGHT_ARM, 0.5f);
        }
    }

    private static final List<Limb> LEG_PARTS = List.of(Limb.RIGHT_LEG, Limb.RIGHT_FOOT, Limb.LEFT_LEG, Limb.LEFT_FOOT);

    public void onLegUse() {
        final float pain_per_tick = 0.5f;
        LimbStatistics stats;
        for (Limb limb : LEG_PARTS) {
            stats = getLimb(limb);
            if (stats.getFracture() > 0 || stats.getDislocation() > 0) applyPain(stats, pain_per_tick);
        }
    }

    public void killPlayer(ServerPlayer player, boolean gaveUp) {
        boolean bleedout = blood < 3.5f;
        boolean internalBleed = hemothorax > 50;
        boolean overdose = getNetOpioids() > 100;
        boolean bleedoutHeavy = getCombinedBleed() > 2f / 20f / 60f;

        DamageSource src = ModDamageTypes.oxygen(player.serverLevel());
        if (gaveUp) {
            src = ModDamageTypes.giveUp(player.serverLevel());

        }
        if (bleedoutHeavy) {
            src = ModDamageTypes.heavy_bleed(player.serverLevel());

        }
        if (internalBleed) {
            src = ModDamageTypes.internal(player.serverLevel());

        }
        if (bleedout) {
            src = ModDamageTypes.bleed(player.serverLevel());

        }
        if (overdose) {
            src = ModDamageTypes.opioids(player.serverLevel());

        }

        LimbStatistics stats;
        for (Limb limb : limbStats.keySet()) {
            stats = limbStats.get(limb);
            if (stats.isTourniquet()) {
                ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.TOURNIQUET.get()));
            }
            if (stats.hasSplint()) {
                ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.SPLINT.get()));
            }
        }

        player.hurt(src, Float.MAX_VALUE);
    }


    public void handleMagicHeal(float amount) {
        float healAmount = amount * ServerConfig.MAGICAL_HEAL_RATE.get().floatValue();
        for (Limb limb : limbStats.keySet()) {
            if (limbStats.get(limb).getBleedRate() > 0)
                limbStats.get(limb).setBleedRate(Math.max(0, limbStats.get(limb).getBleedRate() - 0.00005f * healAmount));
            limbStats.get(limb).setSkinHealth(Math.min(100, limbStats.get(limb).getSkinHealth() + 2 * healAmount));
            limbStats.get(limb).setMuscleHealth(Math.min(100, limbStats.get(limb).getMuscleHealth() + 2 * healAmount));
        }
        if (blood < 5) {
            blood = Math.min(5, blood + 0.02f * healAmount);
        } else if (blood > 5) {
            blood = Math.max(5, blood - (0.02f * healAmount));
        }
        if (bloodViscosity > 10) {
            bloodViscosity -= 1 * amount;
        }
    }

    public void hurtArmor(Limb limb, Player player, float damage) {
        ItemStack item = null;
        EquipmentSlot eq = null;
        switch (limb) {
            case CHEST, LEFT_ARM, RIGHT_ARM, LEFT_HAND, RIGHT_HAND -> {
                item = player.getItemBySlot(EquipmentSlot.CHEST);
                eq = EquipmentSlot.CHEST;
            }
            case HEAD -> {
                item = player.getItemBySlot(EquipmentSlot.HEAD);
                eq = EquipmentSlot.HEAD;
            }
            case RIGHT_LEG, LEFT_LEG -> {
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

    int temperatureTick = 0;
    float envTemp = 36.6f;

    public void updateTemperature(Player player) {
        if (temperatureTick++ > 20) {
            temperatureTick = 0;
            envTemp = getAmbientTemperature(player);
        }
        float armorInsulation;
        armorInsulation = ThermalArmorHandler.getArmorInsulation(player);

        float INSULATION_PER_POINT = 0.05f;
        float MAX_INSULATION_SCALE = 0.85f;
        float MIN_RATE = 1e-5f;

        float rawInsulationEffect = armorInsulation * INSULATION_PER_POINT;
        float clampedInsulationEffect = Mth.clamp(rawInsulationEffect, 0f, MAX_INSULATION_SCALE);

        float baseRate = 0.005f / 20f;
        float adjustmentRate = baseRate * (1f - clampedInsulationEffect);
        adjustmentRate = Math.max(adjustmentRate, MIN_RATE);

        float targetTemp = Mth.clamp(envTemp, 18f, 44f);
        float optimalTemp = 36.6f;

        float delta = targetTemp - temperature;

        float distanceFromOpt = Math.abs(temperature - optimalTemp);

        float curveFactor = Mth.clamp(1f - (distanceFromOpt / 10f) * 0.8f, 0.1f, 1.5f);

        boolean movingTowardOpt = Math.signum(delta) != Math.signum(temperature - optimalTemp);

        float directionFactor = movingTowardOpt ? 4.0f : 1.0f;

        float finalRate = adjustmentRate * curveFactor * directionFactor;

        temperature += delta * finalRate;
    }

    public Float getAmbientTemperature(Player player) {
        float outData = getBiomeTemperature(player);

        int seaLevel = player.level().getSeaLevel();
        double heightDiff = player.getY() - seaLevel;

        float heightModifier = (float) Mth.clamp(-heightDiff * 0.025f, -4f, 4f);
        outData += heightModifier;

        BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos();
        float tblockheatBonus = 0;
        for (int dx = -3; dx <= 3; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                for (int dz = -3; dz <= 3; dz++) {
                    checkPos.set(player.blockPosition().offset(dx, dy, dz));
                    BlockState state = player.level().getBlockState(checkPos);

                    float base;

                    base = TempCompat.get(state.getBlock()) != null ? TempCompat.get(state.getBlock()) : 0;
                    if (state.hasProperty(BlockStateProperties.LIT) && state.getValue(BlockStateProperties.LIT)) {
                        base += 1f;
                    }

                    if (base != 0f) {
                        double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
                        float falloff = (float) Math.max(0.0, 1.0 - dist / 4.0); // full at 0m, none beyond 4m
                        tblockheatBonus += base * falloff;
                    }
                }
            }
        }
        tblockheatBonus = Mth.clamp(tblockheatBonus, -30, 30);

        if (player.isSprinting()) outData += 1.5f;
        else if (player.isSwimming()) outData -= 1f;
        if (player.getFoodData().getFoodLevel() < 6) outData -= 0.5f;

        float generalheatbonus = 0f;

        if (player.isInWater()) generalheatbonus -= 3f; // cold water
        if (player.isOnFire()) generalheatbonus += 10f;

        boolean skyVisible = player.level().canSeeSkyFromBelowWater(player.blockPosition());
        if (!skyVisible) {
            tblockheatBonus *= 0.5f; // less effect indoors
        }

        if (player.isInWaterOrRain()) generalheatbonus -= 2f;
        if (player.level().isThundering()) generalheatbonus -= 3f;
        if (player.level().isDay() && player.level().canSeeSky(player.blockPosition())) generalheatbonus += 1f;

        outData += (tblockheatBonus + generalheatbonus);
        return outData;
    }

    public float getBiomeTemperature(Player player) {
        Level world = player.level();
        BlockPos pos = player.blockPosition();

        Biome biome = world.getBiome(pos).value();
        float sunScale = 0;
        float sunAngle = (float) Math.toDegrees(world.getSunAngle(0));

        if (sunAngle > 90 && sunAngle < 270) {
            if (sunAngle > 180) {
                sunAngle -= 180;
            }
            sunScale = Mth.clamp(Math.abs((sunAngle - 90) / 30), 0, 1);
        }
        TempCompat.BiomeTemperatureEntry temperatureEntry = TempCompat.getForPosition(world, pos);
        Float outData = null;
        if (temperatureEntry != null) {
            outData = SereneSeasonsUtil.getSeasonScale(world, temperatureEntry);
            outData += (temperatureEntry.nightChange * sunScale);
        }

        if (outData == null) {
            outData = 25f + ((biome.getBaseTemperature() + 0.5f) / 2.5f) * 16f;
        }
        return outData;
    }

    public void calculateImmunity() {
        float temp_bonus = -36.6f + temperature;
        float blood_bonus = blood > 5 ? (-5 + blood) * 5 : 0;
        float dirtiness_bonus = -Math.max(0, dirtiness - 50);
        float antibiotics_bonus = antibioticTimer > 0 ? 60 : 0;
        float hunder_bonus = hungerLevel - 10;
        antibioticTimer = Math.max(antibioticTimer - 1, 0);
        immunity = 100 + temp_bonus + blood_bonus + dirtiness_bonus + antibiotics_bonus + hunder_bonus;
    }

    public void updateDirtyness(Player player) {
        float passiveIncrease = 0.000666f;
        if (player.isSprinting()) passiveIncrease *= 3;
        boolean swamp =
                player.level().getBiome(player.blockPosition()).is(Biomes.SWAMP) ||
                        player.level().getBiome(player.blockPosition()).is(Biomes.MANGROVE_SWAMP) ||
                        player.level().getBiome(player.blockPosition()).is(Biomes.JUNGLE);


        // Dirt accumulates faster if player is hurt or bleeding
        if (getCombinedBleed() > 0 || swamp) passiveIncrease += 0.02f;
        BlockPos blockPosBelow = player.blockPosition().below();
        BlockState blockStateBelow = player.level().getBlockState(blockPosBelow);
        if (blockStateBelow.is(Blocks.MUD) || blockStateBelow.is(BlockTags.SAND)) passiveIncrease += 0.05f;
        if (player.isOnFire()) passiveIncrease += 0.01f;  // smoke/ash

        // Rain and clean water slowly reduce dirt
        float passiveDecrease = 0f;
        if (player.isUnderWater()) {
            if (!swamp) {
                passiveDecrease += 1f;
            } else {
                passiveIncrease += 0.5f;
            }
        } else if (player.level().isRainingAt(player.blockPosition())) passiveDecrease += 0.05f;

        dirtiness += (passiveIncrease - passiveDecrease) / 20;

        // Clamp between 0 and 100
        dirtiness = Mth.clamp(dirtiness, 0f, 100f);
    }

    //Visuals

    private int lastAir = -1;
    private long lastTick = -1;
    private float lastRate = 0f;

    // Call every tick for the player
    public float getAirLossRate(Player player) {
        int currentAir = player.getAirSupply();
        long tick = player.level().getGameTime();

        // On first tick, just initialize
        if (lastAir == -1) {
            lastAir = currentAir;
            lastTick = tick;
            return 0f;
        }

        // Compute difference
        long deltaTicks = tick - lastTick;
        if (deltaTicks <= 0) return lastRate; // Avoid division by 0

        int diff = lastAir - currentAir;
        float rate = diff / (float) deltaTicks;

        // Update tracking
        lastAir = currentAir;
        lastTick = tick;

        // Filter small changes (air regen, mod sync issues, etc.)
        if (rate < 0) rate = 0f;

        lastRate = rate;
        return rate;
    }

    public float calculateStability(ServerPlayer player) {
        float DefaultChange = 2.5f;
        if (consciousness < 50) {
            DefaultChange -= consciousness / 20f;
        }
        Vec3 velocity = PhysicsUtil.getVel(RagdollPart.TORSO, player);
        if (velocity.length() > 0f) {
            DefaultChange -= (float) ((velocity.length()) * 2);
        }
        if (Math.abs(player.getDeltaMovement().y) > 20 / 20f && player.fallDistance > 5) {
            DefaultChange -= 5;
        }
        if (player.getPose() == Pose.CROUCHING) {
            DefaultChange += 1;
        }
        if (player.getPose() == Pose.SWIMMING) {
            DefaultChange += 2;
        }
        Vec3 flow = player.level().getFluidState(player.blockPosition()).getFlow(player.level(), player.blockPosition());
        if (flow.length() > 0) {
            DefaultChange -= (float) (flow.length() * 3);
            if (player.hasPose(Pose.CROUCHING)) {
                DefaultChange -= 0.5f;
            }
        }
        if (consciousness < 10) {
            if (Stability != 0) {
                PhysicsUtil.applyRandomRot(player);
            }
            Stability = 0;
        } else {
            Stability += DefaultChange;
        }
        Stability = Mth.clamp(Stability, 0, 100);
        return Stability;
    }

    public String baseToString() {
        return "PlayerHealthData{" +
                "blood=" + blood +
                ", totalPain=" + totalPain +
                ", contiousness=" + consciousness +
                ", contiousnessCap=" + consciousnessCap +
                ", hemothorax=" + hemothorax +
                ", hemothoraxpain=" + hemothoraxPain +
                ", internalBleeding=" + internalBleeding +
                ", oxygen=" + Oxygen +
                ", oxygenCap=" + OxygenCap +
                ", opioids=" + Opioids +
                ", bpm=" + BPM +
                ", isBreathing=" + isBreathing +
                ", respiratoryArrest=" + respiratoryArrest +
                ", bloodViscosity=" + bloodViscosity +
                ", immunity=" + immunity +
                ", drug_addiction=" + drugAddition +
                ", brainHealth=" + brainHealth +
                ", Shock=" + Shock +
                ", dirtiness=" + dirtiness +
                ", temperature=" + temperature +
                ", hearingLoss=" + hearingLoss +
                ", adrenalone=" + adrenaline +
                '}';
    }
}
