package net.zaharenko424.casualties_cubed.limbs;

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
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemHandlerHelper;
import net.zaharenko424.casualties_cubed.CasualtiesCubedTags;
import net.zaharenko424.casualties_cubed.ModDamageTypes;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.compat.TempCompat;
import net.zaharenko424.casualties_cubed.compat.serene_seasons.SereneSeasonsUtil;
import net.zaharenko424.casualties_cubed.config.ServerConfig;
import net.zaharenko424.casualties_cubed.hitbox.HitSector;
import net.zaharenko424.casualties_cubed.network.MedicalAction;
import net.zaharenko424.casualties_cubed.registry.ModItems;
import net.zaharenko424.casualties_cubed.registry.ModSounds;
import net.zaharenko424.casualties_cubed.util.AnimationCurve;
import net.zaharenko424.casualties_cubed.util.Keyframe;
import net.zaharenko424.casualties_cubed.util.Util;
import net.zaharenko424.casualties_cubed.util.WeightedMode;

import java.util.*;

public class PlayerHealthData {

    private static final String MOVE_SPEED_MODIFIER = "custom_move_speed";
    private static final UUID MOVE_SPEED_MODIFIER_UUID = UUID.nameUUIDFromBytes(MOVE_SPEED_MODIFIER.getBytes());
    private static final String ATTACK_DAMAGE_MODIFIER = "custom_attack_damage";
    private static final UUID ATTACK_DAMAGE_MODIFIER_UUID = UUID.nameUUIDFromBytes(ATTACK_DAMAGE_MODIFIER.getBytes());
    private static final String ATTACK_SPEED_MODIFIER = "custom_attack_speed";
    private static final UUID ATTACK_SPEED_MODIFIER_UUID = UUID.nameUUIDFromBytes(ATTACK_SPEED_MODIFIER.getBytes());

    private static final AnimationCurve hungerLimbHeal = new AnimationCurve(List.of(
            new Keyframe(-50, -0.0001931116f, 0, 0.007145616f, WeightedMode.NONE, 0, 0),
            new Keyframe(20, 0.5f, 0.007145616f, 0.01249994f, WeightedMode.NONE, 0, 0.3333333f),
            new Keyframe(60, 0.9999974f, 0.01249994f, 6.4075E-08f, WeightedMode.NONE, 0.3333333f, 0.3333333f),
            new Keyframe(100, 1, 6.4075E-08f, 0.02495816f, WeightedMode.NONE, 0.3333333f, 0),
            new Keyframe(110, 1.249582f, 0.02495816f, 0.000102973f, WeightedMode.NONE, 0, 0)
    ));
    private static final AnimationCurve temperatureMovementCurve = new AnimationCurve(List.of(
            new Keyframe(20, 0.6000061f, 0, 0.0189991f, WeightedMode.NONE, 0, 0),
            new Keyframe(30, 0.7899971f, 0.0189991f, 0.07000097f, WeightedMode.NONE, 0.3333333f, 0),
            new Keyframe(33, 1, 0.07000097f, 0, WeightedMode.NONE, 0, 0),
            new Keyframe(40.7f, 1, 0, -0.116373f, WeightedMode.NONE, 0, 0),
            new Keyframe(45, 0.499596f, -0.116373f, -0.03110576f, WeightedMode.NONE, 0, 0),
            new Keyframe(52.23217f, 0.2746337f, -0.03110576f, 0, WeightedMode.NONE, 0, 0)
    ));
    private static final AnimationCurve immunityInfectionSpeed = new AnimationCurve(List.of(
            new Keyframe(0, 2.5f, -0.01900765f, -0.01999984f, WeightedMode.NONE, 0, 0.3333333f),
            new Keyframe(50, 1.500008f, -0.01999984f, -0.01333398f, WeightedMode.NONE, 0.3333333f, 0.3333333f),
            new Keyframe(80, 1.099988f, -0.01333398f, -0.004999423f, WeightedMode.NONE, 0.3333333f, 0.3333333f),
            new Keyframe(100, 1, -0.004999423f, -0.005000472f, WeightedMode.NONE, 0.3333333f, 0),
            new Keyframe(120, 0.8999906f, -0.005000472f, -0.01029073f, WeightedMode.NONE, 0, 0),
            new Keyframe(140, 0.694176f, -0.01029073f, -0.009894184f, WeightedMode.NONE, 0.3333333f, 0.3333333f),
            new Keyframe(195, 0.1499959f, -0.009894184f, -0.1899985f, WeightedMode.NONE, 0, 0),
            new Keyframe(200, -0.7999964f, -0.1899985f, 0, WeightedMode.NONE, 0, 0)
    ));

    private final Map<Limb, LimbStatistics> limbStats = new EnumMap<>(Limb.class);

    ///C:U -50 - 200? range, 1 = 0.025L, total body blood = 2.5 + blood * 0.025
    private float blood = 5f;
    private float averagePain = 0;
    private float consciousness = 100f;
    private float totalBleedSpeed = 0;
    private float hemothorax = 0f;
    private float hemothoraxPain = 0f;
    ///per minute -> per tick = / 1200 //! C:U uses 0 - 100 range with 1 = 0.0088/m (Body.HandleBody())
    private float internalBleeding = 0f;
    private float bloodOxygen = 100f;
    private float OxygenCap = 100;
    private float Opioids = 0;
    private float PendingOpioids = 0;
    private float heartRate = 70;
    private boolean isBreathing = true;
    private boolean respiratoryArrest = false;
    private float bloodViscosity = 0;
    private float adrenaline = 0, currentAdrenaline = 0;
    private int lifeSupportTimer = 0;

    private float immunity = 100;
    private float antibioticTimer = 0;//seconds
    private final Painkillers painkillers = new Painkillers(this);
    private float brainHealth = 100;
    private float Shock = 0;
    private float dirtiness = 0;
    private float temperature = 36.6f;
    private float hearingLoss = 0;
    private float flashHearingLoss = 0;
    private float sepsis = 0;
    private float sickness = 0;
    private final Vomiter vomiter = new Vomiter(this);
    float temporarySlowdown;
    private float venomTotal, venomCurrent;
    private float wetness;

    private boolean leftEyeBlind = false;
    private boolean RightEyeBlind = false;
    private boolean isMouthRemoved = false;

    private boolean isRagdolled = false;
    private float Stability = 100;

    private float stimulantMultiplier;
    private float hungerLimbHealCurrent;
    private boolean breathing;
    private float respiratoryRate;
    private float brainGrowSickness;
    private float caffeinated;
    private float painShock;
    private float bloodPressure = 120;
    private float fibrillationProgress;
    private float strokeAmount;
    private float bloodPressureChangeFromMedicine;
    private float heartRatePressureOffset;
    private float bloodVesselSize = 1;
    private float heartProg;
    private boolean didThump;
    private float randomFibrillationVariation;
    private boolean triedRollingLastStand = false;
    private int lastStandTime = -1;
    private boolean successfullyRolledLastStand;
    private boolean fibrillationForced;
    private boolean hasPulmonaryEmbolism;
    private float temperatureMovementMult;
    private float bleedClottingSpeed;
    private float bleedingSpeedMultiplier;
    private float currentImmunityMult;

    private Vec3 lastPos = Vec3.ZERO;//last pos used to calculate deltaMovement on server
    private final float thirst = 100;//to be implemented
    private final float energy = 100;

    /*
    TODO: - Make:
        -MAKE Amputations
     */

    //private boolean syncNeeded;

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

    public float getTemperature() {
        return temperature;
    }

    public void addTemperature(float c) {
        setTemperature(temperature + c);
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

    public void setTriedRollingLastStand(boolean triedRollingLastStand) {
        this.triedRollingLastStand = triedRollingLastStand;
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

    public float getPainShock() {
        return painShock;
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

    public float getInternalBleedingCapped() {
        return Mth.clamp(internalBleeding, 0, 25 * 0.0088f);
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
        consciousness = Mth.clamp(value, 0, 100);
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

    public float getBloodOxygen() {
        return bloodOxygen;
    }

    public void setBloodOxygen(float value) {
        bloodOxygen = value;
    }

    public float getHeartRate() {
        return heartRate;
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

    public double getAveragePain() {
        return averagePain;
    }

    ///Maps vanilla food points to CU -50 - 120 range
    public float getCUHunger(ServerPlayer player) {//def hunger is 100 so 20 is extra -> use saturation? or new system
        return Mth.map(player.getFoodData().getFoodLevel(), 0, 20, -50, 120);
    }

    public float getSepsis() {
        return sepsis;
    }

    public void setSepsis(float value) {
        this.sepsis = Mth.clamp(value, 0, 100);
    }

    public float getSickness() {
        return sickness;
    }

    public void addSickness(float sickness) {
        setSickness(this.sickness + sickness);
    }

    public void setSickness(float sickness) {
        this.sickness = Mth.clamp(sickness, 0, 100);
    }

    public float getVenom() {
        return venomCurrent;
    }

    public float getVenomTotal() {
        return venomTotal;
    }

    public void addVenom(float value) {
        venomTotal = Math.max(0, venomTotal + value);
    }

    public void setVenom(float value) {
        venomTotal = value;
    }

    public float getWetness() {
        return wetness;
    }

    public void addWetness(float wetness) {
        setWetness(this.wetness + wetness);
    }

    public void setWetness(float wetness) {
        this.wetness = Mth.clamp(wetness, 0, 100);
    }

    public ChipState getChip() {
        return ChipState.ACTIVE;
    }

    public boolean isConscious() {
        return consciousness > 30;
    }

    public float getBloodPercentage() {
        return blood / 5;
    }

    public float getMAX_BLEED_RATE() {
        return (float) (ServerConfig.MAX_BLEED_RATE.get() * Util.TICK_TO_SEC);
    }

    public LimbStatistics getLimb(Limb limb) {
        return limbStats.computeIfAbsent(limb, l -> new LimbStatistics(this));
    }

    public boolean isAmputated(Limb limb) {
        return getLimb(limb).isAmputated();
    }

    public float totalBleedSpeed() {
        return totalBleedSpeed;
    }

    public float currentImmunityMult() {
        return currentImmunityMult;
    }

    public float hungerLimbHealCurrent() {
        return hungerLimbHealCurrent;
    }

    public void respiratoryRate(float value) {
        respiratoryRate = value;
    }

    public float bloodPressure() {
        return bloodPressure;
    }

    public Painkillers painkillers() {
        return painkillers;
    }

    public Vomiter vomiter() {
        return vomiter;
    }

    ///Checks whether the limb is below a limb with tourniquet
    public boolean isUnderTourniquet(Limb limb) {
        if (limb == Limb.CHEST) return false;

        limb = limb.getConnectedTo();

        while (limb != Limb.CHEST) {
            if (getLimb(limb).isTourniquet()) return true;
            limb = limb.getConnectedTo();
        }

        return false;
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
        limb.addPain(value);
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
                    stats.setBoneHealTimer(Math.max(stats.getBoneHealTimer(), 30 + (damage / 10) * 70));
                    if (player.level().isClientSide())
                        player.playSound(ModSounds.BROKEN_BONE.get());
                }
            } else {
                if (Math.random() < bone_damage_chance || damage > 15) {
                    stats.setDislocationTimer(Math.max(stats.getDislocationTimer(), 30 + (damage / 10) * 70));
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

    private void applyConcussion(Limb limb, float damage) {
        if (limb == Limb.HEAD) {
            setConsciousness(consciousness - (Math.max(damage * 2, 10)));
        }
    }

    public void update(ServerPlayer player) {// order of update from C:U to make sure that values are calculated correctly
        //--Painkillers component = base.GetComponent<Painkillers>();
        //--if (this.reversedControls)
        //--{
        //--    this.moveDir = -this.moveDir;
        //--}
        painkillers.update(player);

        handleVariableUpdates(player);
        handleBody(player); //(+ handle the limbs)
        handleBodyTemperature(player);
        handleWaterShaking(player);
        //HandleRadiationSickness();
        handlePeriodicChecks(player);
        //--HandleGroundedState();
        //--HandlePhysics();
        //--HandleVisuals(component);
        //--

        player.setAirSupply(player.getMaxAirSupply());//reset vanilla air
        vomiter.update(player);
        maybeRegrowLimbs(player);
        applyPenalties(player);
    }

    public float bleedClottingSpeed() {
        return bleedClottingSpeed;
    }

    public float bleedingSpeedMultiplier() {
        return bleedingSpeedMultiplier;
    }

    private void handleVariableUpdates(ServerPlayer player) {
        stimulantMultiplier = Util.moveTowards(Util.TICK_TO_SEC * 0.02f, stimulantMultiplier, 0);
        hungerLimbHealCurrent = hungerLimbHeal.evaluate(Mth.map(player.getFoodData().getFoodLevel(), 0, 20, -50, 120));

        LimbStatistics head = getLimb(Limb.HEAD);
        if (isMouthRemoved && head.getMuscleHealth() > 50) {
            head.setMuscleHealth(Util.moveTowards(Util.TICK_TO_SEC, head.getMuscleHealth(), 50));
        }

        temporarySlowdown = Util.moveTowards(Util.TICK_TO_SEC * 0.1f, temporarySlowdown, 0);
    }

    private void handleBody(ServerPlayer player) {
        handleCirculation(player);

        brainGrowSickness = Math.max(brainGrowSickness - Util.TICK_TO_SEC, 0f);
        updateDirtyness(player);
        breathing = player.isAlive() && !player.isInWall() && respiratoryRate > 10 && !player.getEyeInFluidType().canDrownIn(player);//TODO add other breathing factors, somehow factor in stuff from other mods //ForgeHooks.onLivingBreathe();
        caffeinated = Math.max(caffeinated - Util.TICK_TO_SEC, 0f);

        //ragdoll if unconscious, legSpeedMult <= 0, shock > 10
        //isRagdolled = true;

        if (averagePain > 99) {
            Shock = 100;
            //ragdoll();
        }

        hearingLoss = Mth.clamp(hearingLoss - 0.05f * Util.TICK_TO_SEC, 0, 100);

        // Bleeding — internal
        if (internalBleeding > 0) {
            internalBleeding = (float) Math.max(0, internalBleeding - ServerConfig.WOUND_ANTIBLEED_RATE.get() * Util.TICK_TO_MIN);
            internalBleeding = Mth.clamp(internalBleeding, 0, getMAX_BLEED_RATE() / 4);
        }

        // Hemothorax
        if (internalBleeding > 5 * 0.0088f) {// int bleed to cu points = 1/0.0088 * int bleed; cu points to int bleed = 1 * 0.0088
            hemothorax += getInternalBleedingCapped() * Util.TICK_TO_SEC;
        } else if (hemothorax > 0) {
            hemothorax -= Math.min(hemothorax, ServerConfig.HEMOTHORAX_HEAL_RATE.get().floatValue() * Util.TICK_TO_SEC);
        }

        LimbStatistics stats = getLimb(Limb.CHEST);
        if (stats.getPain() < hemothorax * 0.25f) {
            stats.addPain(3.5f * Util.TICK_TO_SEC);
        }

        blood -= getInternalBleedingCapped() * (1 / 0.0088f * 0.0057f) * Util.TICK_TO_SEC;

        averagePain = 0;
        totalBleedSpeed = 0;// CU calculates total here but limbs actually subtract blood
        float totalInfection = 0;
        for (Limb limb : Limb.values()) {
            stats = getLimb(limb);
            if (stats.isAmputated()) continue;

            stats.update(player, limb);

            if (!stats.isTourniquet() && !isUnderTourniquet(limb)) totalBleedSpeed += stats.getBleedRate();//bleed rate is per tick but totalBleedSpeed is calculated per second
            averagePain = Math.max(stats.getPain() - currentAdrenaline * 0.5f, averagePain);
            totalInfection += stats.getInfection();

            if (getCUHunger(player) <= 0) {
                stats.addMuscleHealth(-0.15f * Util.TICK_TO_SEC);
            }
        }

        totalBleedSpeed += getInternalBleedingCapped() * (1 / 0.0088f * 0.0057f) * Util.TICK_TO_SEC;
        totalBleedSpeed -= bloodRegenSpeed(player) * Util.TICK_TO_SEC;
        if (totalBleedSpeed < 0) totalBleedSpeed = 0;

        //trauma

        if (totalInfection > 100) {
            setSepsis(sepsis + 0.00028f * totalInfection * Util.TICK_TO_SEC * ServerConfig.INFECTION_RATE.get().floatValue());
        } else setSepsis(sepsis - 0.07f * Util.TICK_TO_SEC);

        venomTotal = Util.moveTowards(Util.TICK_TO_SEC / 10.5f, venomTotal, 0);
        venomCurrent = Util.moveTowards(Util.TICK_TO_SEC, venomCurrent, venomTotal);
        if (venomCurrent > 0) {
            float venomOxygenCap = 100 - venomCurrent * 0.5f;
            if (bloodOxygen > venomOxygenCap) {
                bloodOxygen = Util.moveTowards(Util.TICK_TO_SEC * 0.7f, bloodOxygen, venomOxygenCap);
            }

            if (bloodViscosity < venomCurrent) {
                bloodViscosity = Util.moveTowards(Util.TICK_TO_SEC / 4.5f, bloodViscosity, venomCurrent);
            }

            setBloodVolume(blood - venomCurrent * Util.TICK_TO_SEC / 500 * 0.025f);
        }

        if (averagePain > 75) {
            painShock += Util.TICK_TO_SEC / 30;
        } else painShock -= Util.TICK_TO_SEC / 30;
        painShock = Mth.clamp(painShock, 0, 1);

        if (painShock > 0.66f) consciousness = 0;

        float newConsciousness = Util.min(
                100,
                getLimb(Limb.HEAD).getMuscleHealth() * 2.1f,
                bloodOxygen * 1.2f,
                Util.remap(bloodPressure, 60, 110, 30, 100),
                Util.remap(bloodPressure, 140, 200, 100, 30),
                150 - sickness,
                player.isSleeping() ? 10 : 100,//might be an issue
                brainHealth,
                //energy
                150 - averagePain,
                //radiation
                (bloodPressure < 60f) ? 0f : 100f
        );// + more stuff
        if (consciousness > newConsciousness) {
            setConsciousness(Util.moveTowards(12 * Util.TICK_TO_SEC, consciousness, newConsciousness));
        } else {
            setConsciousness(Util.moveTowards(3 * Util.TICK_TO_SEC, consciousness, newConsciousness));
        }

        setBrainHealth(brainHealth + (brainHealth > 0 ? 0.003f : 0) * Util.TICK_TO_SEC * ServerConfig.HEALING_RATE.get().floatValue());

        addSickness(-0.06f * Util.TICK_TO_SEC * ServerConfig.METABOLISM_RATE.get().floatValue());
        if (sickness >= 95) {
            getLimb(Limb.CHEST).addInfection(1);
        }

        if (!isConscious()) {
            if (player.isAlive()) {//update healthData when dead as well then?
                //energy, happiness when sleeping

                if (!triedRollingLastStand && brainHealth <= 15) {
                    tryLastStand(player);
                }
            }
        } else {
            //energy, happiness
        }

        if (player.isAlive()) {
            player.getFoodData().addExhaustion(1 / 23f * Util.TICK_TO_SEC * 4);
            //thirst, stamina, etc

            if (!(!player.onGround() && player.isFallFlying()) && (Math.abs(lastPos.x - player.getX()) > 0.1 || Math.abs(lastPos.z - player.getZ()) > 0.1)) {
                temperature += 0.04f * Util.TICK_TO_SEC;
            }
            lastPos = player.position();

            if (strokeAmount > 0) {
                blood -= 0.1f * Util.CU_BLOOD_POINT_AS_L * Util.TICK_TO_SEC;
                brainHealth -= 0.025f * Util.TICK_TO_SEC;

                if (strokeAmount > 90) {
                    tryStartFibrillation(true);
                }
            }
            strokeAmount = Mth.clamp(strokeAmount, 0, 100);
        }

        //weightOffset, happiness
        addWetness(-(wetness > 75 ? 0.35f : 0.2f) * Util.TICK_TO_SEC);
        setBrainHealth(brainHealth);
        Shock = Mth.clamp(Shock - (isConscious() ? 10 : 0) * Util.TICK_TO_SEC, 0, 100);
        adrenaline = Mth.clamp(adrenaline - (lastStandTime > 0 ? -5 : 1.7f) * Util.TICK_TO_SEC, 0, 100);
        currentAdrenaline = Util.moveTowards(5 * Util.TICK_TO_SEC, currentAdrenaline, adrenaline);


        //Wetness
        if (player.isInWaterRainOrBubble()) {
            addWetness((player.isInWaterOrBubble() ? 15 : 2) * Util.TICK_TO_SEC);

            if (dirtiness > 10) {
                dirtReduceTime += 3 * Util.TICK_TO_SEC;
                if (dirtReduceTime > 1) {
                    dirtReduceTime = 0;
                    dirtiness -= 10;
                }
            }
        }
    }

    float dirtReduceTime;

    private void tryLastStand(ServerPlayer player) {
        triedRollingLastStand = true;

        ItemStack totem = null;
        if (ServerConfig.TOTEM_OF_UNDYING_LAST_STAND.get()) {
            for(InteractionHand interactionhand : InteractionHand.values()) {
                ItemStack itemstack1 = player.getItemInHand(interactionhand);
                if (itemstack1.is(Items.TOTEM_OF_UNDYING)) {
                    totem = itemstack1.copy();
                    itemstack1.shrink(1);
                    break;
                }
            }
        }

        if (totem == null && player.getRandom().nextFloat() > 0.5f) return;//hardcoded 50% chance for now. mb use a config option until happiness is added

        brainHealth = player.getRandom().nextFloat() * 15 + 75;
        FoodData food = player.getFoodData();
        food.setFoodLevel(Mth.floor(Mth.lerp(0.5f, food.getFoodLevel(), 18)));
        //thirst
        sickness = Mth.lerp(sickness, 0, 0.3f);
        blood = Math.max(blood, 3.75f);
        heartRate = 120;
        fibrillationProgress = 0;
        bloodPressure = 135;
        bloodVesselSize = 1;
        bloodOxygen = 100;
        bloodViscosity = 0;
        strokeAmount = 0;

        heartRatePressureOffset = 0;
        respiratoryRate = 100;
        sepsis *= 0.4f;
        lastStandTime = 300;
        //happiness
        venomCurrent = 0;
        venomTotal = 0;
        //energy
        antibioticTimer = 120;
        caffeinated = 200;
        hemothorax *= 0.5f;
        temperature = 37;
        //radiation
        internalBleeding *= 0.05f;

        LimbStatistics stats;
        for (Limb limb : Limb.values()) {
            stats = getLimb(limb);
            if (stats.isAmputated()) continue;

            stats.setMuscleHealth(Mth.lerp(0.3f, stats.getMuscleHealth(), 100));
            stats.setInfection(stats.getInfection() * 0.05f);
            stats.setBleedRate(stats.getBleedRate() * 0.05f);
        }

        painkillers.reset();

        successfullyRolledLastStand = true;

        if (ServerConfig.INFINITE_LAST_STAND.get()) {
            triedRollingLastStand = false;
        }
    }

    private void handleCirculation(ServerPlayer player) {
        float tempDiffFromNormal = temperature - 36.6f;

        bloodOxygen += (respiratoryRate * 0.01f * (breathing ? 1 : 0) - 0.5f) * Util.TICK_TO_SEC;

        float bloodPercentage = getBloodPercentage();
        if (bloodPercentage < 0.6f && bloodOxygen > bloodPercentage / 0.6f * 100) {
            bloodOxygen = Util.moveTowards(Util.TICK_TO_SEC * 0.75f, bloodOxygen, bloodPercentage / 0.6f * 100);
        }

        //TODO for oxygen to go down stamina has to go down

        float viscosityOxygenCap = 100 - Math.abs(Util.moveTowards(40, bloodViscosity, 0)) * 0.4f;
        if (bloodOxygen > viscosityOxygenCap) {
            bloodOxygen = Util.moveTowards(Util.TICK_TO_SEC * 0.75f, bloodOxygen, viscosityOxygenCap);
        }

        if (bloodOxygen > 100 - hemothorax * 0.3f) {
            bloodOxygen = Util.moveTowards(Util.TICK_TO_SEC * 0.8f, bloodOxygen, 100 - hemothorax * 0.3f);
        }
        bloodOxygen = Mth.clamp(bloodOxygen, 0, 100);

        bloodViscosity = Util.moveTowards(Util.TICK_TO_SEC * 0.05f, bloodPercentage, 0);
        bloodViscosity = Mth.clamp(bloodViscosity, -100, 100);

        blood = Util.moveTowards(bloodRegenSpeed(player) * Util.TICK_TO_SEC, blood, 5);
        blood = Mth.clamp(blood, 0, 7.5f);

        float newRespRate = 100;
        if (bloodPressure > 145) {
            newRespRate -= bloodPressure - 145;
        } else if (bloodPressure < 20) {
            newRespRate = 0;
        }

        //opiate effects
        if (painkillers.currentOpiateReception() != 0) {
            newRespRate -= painkillers.currentOpiateReception();
        }

        newRespRate -= fibrillationProgress * 0.3f;
        newRespRate -= hemothorax * 0.5f;
        newRespRate += tempDiffFromNormal * 3;
        //stamina resp rate increase
        newRespRate += adrenaline * 0.2f;
        newRespRate += averagePain * 0.25f;
        newRespRate -= Math.max(0, (strokeAmount - 50) * 2);

        if (temperature < 28 && !isConscious()) {
            newRespRate -= 50;
        }

        if (getLimb(Limb.CHEST).getBoneHealTimer() > 0) {
            newRespRate -= 0.6f;
        }

        respiratoryRate = Util.moveTowards(Util.TICK_TO_SEC * (respiratoryRate > 10 ? 8 : 1), respiratoryRate, newRespRate);
        respiratoryRate = Mth.clamp(respiratoryRate, 0, 100);//move to setter?

        if (fibrillationProgress > 0) {
            if (isFibrillationRising()) {
                fibrillationProgress += Util.TICK_TO_SEC * ServerConfig.FIB_RATE.get().floatValue();

                if (heartRate > 280) {
                    fibrillationProgress += Util.TICK_TO_SEC * 3 * ServerConfig.FIB_RATE.get().floatValue();
                }
            } else {
                fibrillationProgress -= Util.TICK_TO_SEC * 0.75f;
                if (fibrillationProgress < 0) fibrillationProgress = 0;
            }
        } else fibrillationForced = false;

        if (bloodOxygen < 50 || bloodPressure < 78 || heartRate > 200 || Math.max(0, bloodViscosity) > 95 || temperature < 28) {
            tryStartFibrillation(false);
        }

        if (fibrillationProgress >= 100) {
            fibrillationForced = false;
            heartRate = 0;
        }
        fibrillationProgress = Mth.clamp(fibrillationProgress, 0, 100);

        float newPressure = 120;
        newPressure -= (100 - (blood - 2.5f) / 0.025f) / 4;
        //stamina
        newPressure += currentAdrenaline * 0.2f;
        newPressure -= sepsis * 0.4f;
        newPressure += tempDiffFromNormal * 2;
        //weight offset ?
        //thirst

        float cuHunger = getCUHunger(player);
        if (cuHunger < 40) {
            newPressure += (cuHunger - 40) * 0.25f;
        }

        if (bloodPressureChangeFromMedicine > 0) {
            newPressure *= 0.75f;
        } else if (bloodPressureChangeFromMedicine < 0) {
            newPressure *= 1.25f;
        }

        if (painkillers.currentOpiateReception() != 0) {
            newPressure -= painkillers.currentOpiateReception() * 0.4f;
        }

        if (!isInCardiacArrest()) {
            float newHeartRate = 70;
            newHeartRate += averagePain;
            //stamina
            newHeartRate += currentAdrenaline * 0.55f;
            newHeartRate -= Math.max(0, bloodViscosity) * 0.3f;
            newHeartRate += tempDiffFromNormal * 0.5f;

            if (painkillers.currentOpiateReception() != 0) {
                newHeartRate -= painkillers.currentOpiateReception() / 5;
            }

            if (bloodPressure < newPressure - 5) {
                heartRatePressureOffset += Util.TICK_TO_SEC * 1.5f;
            }

            if (bloodPressure > newPressure + 5) {
                heartRatePressureOffset -= Util.TICK_TO_SEC * 1.5f;
            }

            newHeartRate += heartRatePressureOffset;
            newHeartRate += fibrillationProgress;

            if (fibrillationProgress > 75) {
                newHeartRate += (fibrillationProgress - 75f) * 4f;
            }

            if (fibrillationProgress > 95) {
                newHeartRate += (this.fibrillationProgress - 95f) * 30f;
            }

            heartRate = Mth.lerp(Util.TICK_TO_SEC * 0.15f, heartRate, newHeartRate);
        } else heartRate = 0;

        if (bloodPressure > newPressure + 10) {
            bloodVesselSize += Util.TICK_TO_SEC * 0.0036f;
        } else if (bloodPressure < newPressure - 10) {
            bloodVesselSize -= Util.TICK_TO_SEC * 0.0036f;
        } else {
            bloodVesselSize = Util.moveTowards(Util.TICK_TO_SEC * 0.0036f, bloodVesselSize, 1);
        }

        if (/*stamina < 50 ||*/ currentAdrenaline > 30) {
            bloodVesselSize -= Util.TICK_TO_SEC * 0.005f;
        }
        bloodVesselSize = Mth.clamp(bloodVesselSize, 0.85f, 1.15f);

        float f6 = Mth.clamp(heartRate, 0f, 215f) - 70f;
        if (f6 > 0) {
            f6 /= 200;
        } else f6 /= 70;

        float f7 = 1 + (bloodPercentage - 1f) * 1.1f;
        float f8 = 1 - fibrillationProgress / 260f;
        float f9 = 1 + bloodViscosity / 200f;
        float f10 = 1 - sepsis * 0.00525f;
        float f11 = 1;
        if (painkillers.currentOpiateReception() != 0) {
            f11 = Mth.clamp(1 - painkillers.currentOpiateReception() / 400f, 0.75f, 1.25f);
        }
        float f12 = 1 - tempDiffFromNormal / 40f;
        //weightOffset
        float newBloodPressure = 120 * (1 + f6) * f7 * f8 * f9 * /*thirstBloodPressure*/ f10 * f11 * f12 * 1 / bloodVesselSize;

        if (bloodPressureChangeFromMedicine > 0) {
            newBloodPressure *= 0.75f;
        } else if (bloodPressureChangeFromMedicine < 0) {
            newBloodPressure *= 1.25f;
        }

        bloodPressure = Mth.lerp(Util.TICK_TO_SEC * 0.25f, bloodPressure, newBloodPressure);
        bloodPressure = Mth.clamp(bloodPressure, 0, 250);

        if (bloodPressure > 145) {
            final float energy = 100;//TODO add energy?
            float pressurePain = Math.min((bloodPressure - 120f) * 0.5f, 50f) * Mth.clamp(energy / 33f, 0, 1);
            LimbStatistics head = getLimb(Limb.HEAD);
            if (head.getPain() < pressurePain) {
                head.setPain(Util.moveTowards(Util.TICK_TO_SEC * 2.5f, head.getPain(), pressurePain));
            }
        }

        if (isBrainDying()) {
            if (bloodOxygen < 80) brainHealth -= Util.TICK_TO_SEC * (80f - bloodOxygen) / 600f;

            brainHealth -= Util.TICK_TO_SEC * 1.5f;
        }

        heartProg += Util.TICK_TO_SEC * heartRate / 60;//CU uses unscaled time but 1.20 has no time scaling
        if (heartProg > 1) {//TODO technically this does nothing outside ecg so could move this entirely to client/health screen
            if (heartProg > 1.2f) {
                heartProg = 1.2f;
            }

            heartProg -= 1;
            didThump = false;

            if (fibrillationProgress > 40) {
                randomFibrillationVariation = 1 + (player.getRandom().nextFloat() - 0.5f) * (fibrillationProgress - 40f) / 150f;
            } else randomFibrillationVariation = 1;
        }

        if (heartProg > 0.3f && !didThump) {
            didThump = true;
            //heart thump sound in health screen -> prob just do this client side
        }

        //pressure readout: round(bloodPressure) / round(bloodPressure * 0.66)
        //respiratory rate readout: respiratoryRate * 0.25/m
    }

    private static final AnimationCurve heartCurveNormal = new AnimationCurve(List.of(
            new Keyframe(-0.09999999f, 0, 0, 1.23537f, WeightedMode.NONE, 0, 0.3333333f),
            new Keyframe(-0.00590552f, 0.1162415f, -0.01582689f, -0.3781773f, WeightedMode.NONE, 0.9854773f, 1),
            new Keyframe(0.1640255f, -0.2316138f, -0.05739408f, 28.8334f, WeightedMode.NONE, 0.7828803f, 0.05674533f),
            new Keyframe(0.2853596f, 0.9987809f, -0.3684868f, -0.1440666f, WeightedMode.NONE, 0.6909047f, 1),
            new Keyframe(0.3720251f, -0.6998956f, 0.3191285f, -0.2657984f, WeightedMode.NONE, 1, 1),
            new Keyframe(0.441258f, -0.2914941f, 7.325673f, 2.528934f, WeightedMode.NONE, 0.3333333f, 0.7717564f),
            new Keyframe(0.5196843f, -0.2318335f, 0.7150099f, 1.103651f, WeightedMode.NONE, 0.3333333f, 1),
            new Keyframe(0.5627187f, 0.08824407f, 13.44368f, 7.385239f, WeightedMode.NONE, 0.3333333f, 0.5650983f),
            new Keyframe(0.6198916f, 0.2699015f, 0.9851937f, 0.3543337f, WeightedMode.NONE, 1, 0.734637f),
            new Keyframe(0.7045727f, 0.1488903f, -2.369716f, -1.884808f, WeightedMode.NONE, 0.6926749f, 0.7784983f),
            new Keyframe(0.8602069f, -0.04067692f, -0.1900704f, -1.555309E-05f, WeightedMode.NONE, 1, 0.3333333f),
            new Keyframe(1, 0, 0.2909794f, 0, WeightedMode.NONE, 0.3333333f, 0)
    ));
    private static final AnimationCurve heartCurveArrythmia = new AnimationCurve(List.of(
            new Keyframe(0, 0, 2.845296f, 2.845296f, WeightedMode.NONE, 0, 0.3333333f),
            new Keyframe(0.1179802f, 0.3356887f, 4.582402f, 4.582402f, WeightedMode.NONE, 0.3333333f, 0.3746302f),
            new Keyframe(0.2495227f, 0.6348413f, -0.4898691f, -0.4898691f, WeightedMode.NONE, 0.3333333f, 0.3333333f),
            new Keyframe(0.3875494f, 0.185712f, -11.07063f, -11.07063f, WeightedMode.NONE, 0.3333333f, 0.1364052f),
            new Keyframe(0.4904054f, -0.5488325f, -1.067827f, -1.067827f, WeightedMode.NONE, 0.3333333f, 0.3412433f),
            new Keyframe(0.7050565f, -0.6214908f, 0.5978603f, 0.5978603f, WeightedMode.NONE, 0.3333333f, 0.370597f),
            new Keyframe(1, 0, 2.107152f, 2.107152f, WeightedMode.NONE, 0.3333333f, 0)
    ));

    public float getECGHeight(float offset) {
        offset *= heartRate / 60;
        float height = Mth.lerp(fibrillationProgress / 90f,
                heartCurveNormal.evaluate(heartProg - offset),
                heartCurveArrythmia.evaluate(heartProg - offset)) * randomFibrillationVariation;

        if (fibrillationProgress > 75) {
            height *= 1 - (fibrillationProgress - 75) / 25;
        }

        return height;
    }

    private float bloodRegenSpeed(ServerPlayer player) {
        return Util.CUBloodPointsToL(0.035f * Math.max(getCUHunger(player) * 0.01f, 0) * ServerConfig.HEALING_RATE.get().floatValue());
    }

    private boolean isFibrillationRising() {
        return bloodOxygen < 60 || bloodPressure < 88 || heartRate > 200 || fibrillationForced || bloodViscosity > 80
                || temperature < 28.5f;
    }

    private boolean isInCardiacArrest() {
        return heartRate < 20;
    }

    private boolean isBrainDying() {
        return bloodPressure < 10 || consciousness < 5;
    }

    public void tryStartFibrillation(boolean forced) {
        if (fibrillationProgress <= 0) {
            fibrillationProgress = 0.1f;
        }

        if (forced) fibrillationForced = true;
    }

    private void handlePeriodicChecks(ServerPlayer player) {
        RandomSource random = player.getRandom();
        // 60s radiation
        if (player.tickCount % 30 * 20 == 0) {// 30s
            //fake attacked - send a hurt sound to that player only?
            //reverse controls
            if (random.nextFloat() > brainHealth * 0.01f && random.nextFloat() < 0.06f) {
                vomiter.vomit();
            }
            if (random.nextFloat() > brainHealth * 0.01f) {
                if (random.nextFloat() < 0.75) {
                    ItemStack stack = player.getMainHandItem();
                    player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                    player.drop(stack, true);
                }
                if (random.nextFloat() < 0.75) {
                    ItemStack stack = player.getOffhandItem();
                    player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
                    player.drop(stack, true);
                }
                // no mouth slot
            }
            // brain damage ragdoll
            // brain flash overlay
        }

        if (player.tickCount % 20 == 0) {// 1s
            //weightMovementMult
            temperatureMovementMult = temperatureMovementCurve.evaluate(temperature);
            //clothingTemperature = 0
            bleedClottingSpeed = 0.025f * Mth.map(bloodViscosity, -100, 0, 0, 1) * Mth.clamp(1 - venomCurrent / 20, 0, 1);
            bleedingSpeedMultiplier = (0.01f + Mth.map(bloodViscosity, -100, 0, 0.01f, 0)) * ServerConfig.BLEED_RATE.get().floatValue();
            lastStandTime--;

            if (bloodViscosity > 90 && random.nextFloat() < 0.0166) {
                hasPulmonaryEmbolism = true;
            }

            if (bloodViscosity < 50 && hasPulmonaryEmbolism) {
                hasPulmonaryEmbolism = false;
            }

            if (hasPulmonaryEmbolism) {
                getLimb(Limb.CHEST).addMuscleHealth(-0.6f);
            }

            if (bloodPressure > 180 && random.nextFloat() < 0.02) {
                strokeAmount = Math.max(strokeAmount, 0.1f);
            }

            if (strokeAmount > 0) {
                strokeAmount += 0.1333f * (ServerConfig.STROKES.get() ? 1 : -1);
            }

            //thirst too high -> fibrillation
            //calc clothing temp

            bloodPressureChangeFromMedicine = Util.moveTowards(1, bloodPressureChangeFromMedicine, 0);
            //happiness
        }

        if (player.tickCount % 10 == 0) {// 0.5s
            //more stuffs
            // encumbrance, stimulants

            antibioticTimer -= 0.5f;
            if (antibioticTimer < 0) antibioticTimer = 0;

            float hungerImm = (getCUHunger(player) - 70) * 0.75f;
            float thirstImm = (thirst - 60) * 0.3f;
            float energyImm = (energy - 60) * 0.2f;
            float temperatureImm = (temperature - 37) * 8;
            float bloodImm = (blood - 5) * 0.2f;
            float dirtinessImm = Math.max(0, dirtiness - 50);
            immunity = 100 + hungerImm + thirstImm + energyImm + temperatureImm + bloodImm - dirtinessImm - sickness * 0.8f;// - radiation * 0.5f
            if (antibioticTimer > 0) {
                immunity += 70;
            }
            immunity = Mth.clamp(immunity, 0, 200);

            currentImmunityMult = immunityInfectionSpeed.evaluate(immunity);
        }
    }

    private void maybeRegrowLimbs(ServerPlayer player) {
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
    }

    public void applyPenalties(ServerPlayer player) {
        double baseMoveSpeed = 0.1;
        double baseAttackDamage = 1.0;
        double baseAttackSpeed = 4.0;
// --- Calculate limb-based multipliers ---
        LimbStatistics rightLeg = getLimb(Limb.RIGHT_LEG),
                        leftLeg = getLimb(Limb.LEFT_LEG);
        float moveReduction;
        if (rightLeg.isAmputated() && leftLeg.isAmputated()) {
            moveReduction = 0.2f;
        } else {
            moveReduction =
                    ((100 - rightLeg.getMuscleHealth()) / 100) * 0.14f +
                    ((100 - leftLeg.getMuscleHealth()) / 100) * 0.14f +
                    ((100 - getLimb(Limb.RIGHT_FOOT).getMuscleHealth()) / 100) * 0.14f +
                    ((100 - getLimb(Limb.LEFT_FOOT).getMuscleHealth()) / 100) * 0.14f;
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

        float moveMultiplier = Util.min(
                1 - moveReduction,
                temperatureMovementMult,
                1 - temporarySlowdown
        );

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
                    || stats.getBoneHealTimer() > 0
                    || stats.getDislocationTimer() > 0;

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
        nbt.putDouble("AveragePain", averagePain);
        nbt.putFloat("Consciousness", consciousness);
        nbt.putFloat("Hemothorax", hemothorax);
        nbt.putFloat("HemothoraxPain", hemothoraxPain);
        nbt.putFloat("InternalBleeding", internalBleeding);
        nbt.putFloat("Oxygen", bloodOxygen);
        nbt.putFloat("OxygenCap", OxygenCap);
        nbt.putFloat("BPM", heartRate);
        nbt.putBoolean("IsBreathing", isBreathing);
        nbt.putFloat("BloodViscosity", bloodViscosity);
        nbt.putFloat("BrainHealth", brainHealth);
        nbt.putFloat("Immunity", immunity);
        nbt.put("painkillers", painkillers.save());
        nbt.putFloat("Shock", Shock);
        nbt.putFloat("Dirty", dirtiness);
        nbt.putFloat("Temp", temperature);
        nbt.putFloat("Adrenaline", adrenaline);
        nbt.putFloat("CurrentAdrenaline", currentAdrenaline);
        nbt.putInt("LifeSupport", lifeSupportTimer);
        nbt.putBoolean("LeftEyeBlind", leftEyeBlind);
        nbt.putBoolean("RightEyeBlind", RightEyeBlind);
        nbt.putBoolean("MouthMissing", isMouthRemoved);
        nbt.putFloat("HearingLoss", hearingLoss);
        nbt.putFloat("FlashHearing", flashHearingLoss);
        nbt.putFloat("Sepsis", sepsis);
        nbt.putFloat("Sickness", sickness);
        nbt.put("Vomiter", vomiter.save());
        nbt.putFloat("TemporarySlowdown", temporarySlowdown);
        nbt.putFloat("VenomTotal", venomTotal);
        nbt.putFloat("VenomCurrent", venomCurrent);
        nbt.putFloat("Wetness", wetness);
        nbt.putFloat("Stability", Stability);

        nbt.putFloat("totalBleedSpeed", totalBleedSpeed);
        nbt.putFloat("stimulantMultiplier", stimulantMultiplier);
        nbt.putFloat("hungerLimbHealCurrent", hungerLimbHealCurrent);
        nbt.putBoolean("breathing", breathing);
        nbt.putFloat("respiratoryRate", respiratoryRate);
        nbt.putFloat("brainGrowSickness", brainGrowSickness);
        nbt.putFloat("caffeinated", caffeinated);
        nbt.putFloat("painShock", painShock);
        nbt.putFloat("bloodPressure", bloodPressure);
        nbt.putFloat("bloodPressureChangeFromMedicine", bloodPressureChangeFromMedicine);
        nbt.putFloat("heartRatePressureOffset", heartRatePressureOffset);
        nbt.putFloat("bloodVesselSize", bloodVesselSize);
        nbt.putBoolean("fibrillationForced", fibrillationForced);
        nbt.putFloat("fibrillationProgress", fibrillationProgress);
        nbt.putFloat("randomFibrillationVariation", randomFibrillationVariation);
        nbt.putFloat("strokeAmount", strokeAmount);
        nbt.putFloat("heartProg", heartProg);
        nbt.putBoolean("didThump", didThump);
        nbt.putBoolean("triedRollingLastStand", triedRollingLastStand);
        nbt.putInt("lastStandTime", lastStandTime);
        nbt.putBoolean("successfullyRolledLastStand", successfullyRolledLastStand);
        nbt.putBoolean("hasPulmonaryEmbolism", hasPulmonaryEmbolism);
        nbt.putFloat("temperatureMovementMult", temperatureMovementMult);
        nbt.putFloat("bleedClottingSpeed", bleedClottingSpeed);
        nbt.putFloat("bleedingSpeedMultiplier", bleedingSpeedMultiplier);
        nbt.putFloat("currentImmunityMult", currentImmunityMult);

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

        blood = nbt.getFloat("Blood");
        averagePain = nbt.getFloat("AveragePain");
        consciousness = nbt.getFloat("Consciousness");
        hemothorax = nbt.getFloat("Hemothorax");
        hemothoraxPain = nbt.getFloat("HemothoraxPain");
        internalBleeding = nbt.getFloat("InternalBleeding");
        bloodOxygen = nbt.getFloat("Oxygen");
        OxygenCap = nbt.getFloat("OxygenCap");
        heartRate = nbt.getFloat("BPM");
        isBreathing = nbt.getBoolean("IsBreathing");
        bloodViscosity = nbt.getFloat("BloodViscosity");
        brainHealth = nbt.getFloat("BrainHealth");

        totalBleedSpeed = nbt.getFloat("totalBleedSpeed");
        immunity = nbt.getFloat("Immunity");
        painkillers.load(nbt.getCompound("painkillers"));
        Shock = nbt.getFloat("Shock");
        dirtiness = nbt.getFloat("Dirty");
        temperature = nbt.getFloat("Temp");
        adrenaline = nbt.getFloat("Adrenaline");
        currentAdrenaline = nbt.getFloat("CurrentAdrenaline");
        lifeSupportTimer = nbt.getInt("LifeSupport");
        leftEyeBlind = nbt.getBoolean("LeftEyeBlind");
        RightEyeBlind = nbt.getBoolean("RightEyeBlind");
        isMouthRemoved = nbt.getBoolean("MouthMissing");
        hearingLoss = nbt.getFloat("HearingLoss");
        flashHearingLoss = nbt.getFloat("FlashHearing");
        sepsis = nbt.getFloat("Sepsis");
        sickness = nbt.getFloat("Sickness");
        vomiter.load(nbt.getCompound("Vomiter"));
        temporarySlowdown = nbt.getFloat("TemporarySlowdown");
        venomTotal = nbt.getFloat("VenomTotal");
        venomCurrent = nbt.getFloat("VenomCurrent");
        wetness = nbt.getFloat("Wetness");
        Stability = nbt.getFloat("Stability");

        stimulantMultiplier = nbt.getFloat("stimulantMultiplier");
        hungerLimbHealCurrent = nbt.getFloat("hungerLimbHealCurrent");
        breathing = nbt.getBoolean("breathing");
        respiratoryRate = nbt.getFloat("respiratoryRate");
        brainGrowSickness = nbt.getFloat("brainGrowSickness");
        caffeinated = nbt.getFloat("caffeinated");
        painShock = nbt.getFloat("painShock");
        bloodPressure = nbt.getFloat("bloodPressure");
        bloodPressureChangeFromMedicine = nbt.getFloat("bloodPressureChangeFromMedicine");
        heartRatePressureOffset = nbt.getFloat("heartRatePressureOffset");
        bloodVesselSize = nbt.getFloat("bloodVesselSize");
        fibrillationForced = nbt.getBoolean("fibrillationForced");
        fibrillationProgress = nbt.getFloat("fibrillationProgress");
        randomFibrillationVariation = nbt.getFloat("randomFibrillationVariation");
        strokeAmount = nbt.getFloat("strokeAmount");
        heartProg = nbt.getFloat("heartProg");
        didThump = nbt.getBoolean("didThump");
        triedRollingLastStand = nbt.getBoolean("triedRollingLastStand");
        lastStandTime = nbt.getInt("lastStandTime");
        successfullyRolledLastStand = nbt.getBoolean("successfullyRolledLastStand");
        hasPulmonaryEmbolism = nbt.getBoolean("hasPulmonaryEmbolism");
        temperatureMovementMult = nbt.getFloat("temperatureMovementMult");
        bleedClottingSpeed = nbt.getFloat("bleedClottingSpeed");
        bleedingSpeedMultiplier = nbt.getFloat("bleedingSpeedMultiplier");
        currentImmunityMult = nbt.getFloat("currentImmunityMult");

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

    public void heal(ServerPlayer player) {
        // clear & repopulate limb stats with fresh defaults
        limbStats.clear();
        for (Limb limb : Limb.values()) {
            limbStats.put(limb, new LimbStatistics(this));
        }

        // reset player-wide primitives to initial defaults (match the field initializers)
        blood = 5f;
        averagePain = 0f;
        consciousness = 100f;
        hemothorax = 0f;
        hemothoraxPain = 0f;
        internalBleeding = 0f;
        bloodOxygen = 100f;
        OxygenCap = 100f;
        heartRate = 70;
        isBreathing = true;
        respiratoryArrest = false;
        bloodViscosity = 0f;
        brainHealth = 100;
        painkillers.reset();
        temperature = 36.6f;
        Shock = 0;
        dirtiness = 0;
        antibioticTimer = 0;
        immunity = 100;
        adrenaline = 0;
        currentAdrenaline = 0;
        lifeSupportTimer = 0;
        isMouthRemoved = false;
        leftEyeBlind = false;
        RightEyeBlind = false;
        hearingLoss = 0;
        flashHearingLoss = 0;
        sepsis = 0;
        sickness = 0;
        vomiter.reset();
        temporarySlowdown = 0;
        venomTotal = venomCurrent = 0;
        wetness = 0;
        triedRollingLastStand = false;
        isRagdolled = false;
        Stability = 100;

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

    public void handleProjectileDamage(HitSector hitSector, float damage, Player player) {
        setAdrenaline(Math.max(getAdrenaline(), damage * 2));
        RandomSource random = player.getRandom();
        List<Limb> limbList = hitSector.limbs;
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


    public void onArmUse(InteractionHand hand, Player player) {
        HumanoidArm arm = Limb.getArmFromHand(hand, player);

        if (arm == HumanoidArm.LEFT) {
            LimbStatistics leftArm = getLimb(Limb.LEFT_ARM), leftHand = getLimb(Limb.LEFT_HAND);
            if ((leftHand.getBoneHealTimer() > 0 || leftHand.getDislocationTimer() > 0))
                applyPain(Limb.LEFT_HAND, 0.5f);
            if ((leftArm.getBoneHealTimer() > 0 || leftArm.getDislocationTimer() > 0))
                applyPain(Limb.LEFT_ARM, 0.5f);
        } else {
            LimbStatistics rightArm = getLimb(Limb.RIGHT_ARM), rightHand = getLimb(Limb.RIGHT_HAND);
            if ((rightHand.getBoneHealTimer() > 0 || rightHand.getDislocationTimer() > 0))
                applyPain(Limb.RIGHT_HAND, 0.5f);
            if ((rightArm.getBoneHealTimer() > 0 || rightArm.getDislocationTimer() > 0))
                applyPain(Limb.RIGHT_ARM, 0.5f);
        }
    }

    private static final List<Limb> LEG_PARTS = List.of(Limb.RIGHT_LEG, Limb.RIGHT_FOOT, Limb.LEFT_LEG, Limb.LEFT_FOOT);

    public void onLegUse() {
        final float pain_per_tick = 0.5f;
        LimbStatistics stats;
        for (Limb limb : LEG_PARTS) {
            stats = getLimb(limb);
            if (stats.getBoneHealTimer() > 0 || stats.getDislocationTimer() > 0) applyPain(stats, pain_per_tick);
        }
    }

    public void killPlayer(ServerPlayer player, boolean gaveUp) {
        boolean bleedout = blood < 3.5f;
        boolean internalBleed = hemothorax > 50;
        boolean overdose = painkillers.currentOpiateReception() > 100;
        boolean bleedoutHeavy = totalBleedSpeed() > 2f / 20f / 60f;

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

    private void handleBodyTemperature(ServerPlayer player) {
        if (player.tickCount % 20 == 0) {
            float envTemp = getAmbientTemperature(player);

            float armorInsulation;
            armorInsulation = ThermalArmorHandler.getArmorInsulation(player);

            float INSULATION_PER_POINT = 0.05f;
            float MAX_INSULATION_SCALE = 0.85f;

            float rawInsulationEffect = armorInsulation * INSULATION_PER_POINT;
            float clampedInsulationEffect = Mth.clamp(rawInsulationEffect, 0f, MAX_INSULATION_SCALE);

            temperature = Mth.lerp(0.003f / Math.max(/*0.5f*/0.9f, clampedInsulationEffect), temperature, 24);

            float mul = 1 - Mth.clamp(0.3f - energy * 0.01f, 0, 1);
            if (painkillers.currentOpiateReception() > 0) {
                mul -= painkillers.currentOpiateReception() * 0.005f;
            }
            temperature += 0.04f * mul;//TODO test temp, might need to lower ambient

            if (temperature > 37.5f) {
                float maxWetness = (temperature - 37.5f) * 20;
                if (wetness < maxWetness) addWetness(1);
            }

            temperature -= 0.001f * wetness;

            if (temperature < 36.5) {
                temperature += Math.max(getCUHunger(player) * 0.01f, 0.3f) * 0.003f;//there is also hunger + energy check
            }

            if (temperature < 32) {
                temperature += 0.01f;
            }
        }

        if (temperature > 42) {
            brainHealth -= 0.5f * Util.TICK_TO_SEC;
        }

        if (temperature > 41) {
            blood -= Util.CUBloodPointsToL(0.15f) * Util.TICK_TO_SEC;
        }
    }

    private float wetShakeTime;
    private float wetShakeProgress;

    private void handleWaterShaking(ServerPlayer player) {
        if (wetShakeProgress > 0) {
            addWetness(-2.5f);
            wetShakeProgress++;
            if (wetShakeProgress >= 20) wetShakeProgress = 0;
        } else if (consciousness > 20 && wetness > 30 && temperature < 36.6 && !player.isInWaterOrBubble()) {
            wetShakeTime++;
            if (wetShakeTime > 12 * 20) {
                wetShakeTime = 0;
                addWetness(-2.5f);//its actually supposed to be done over 1 second but whatever
                wetShakeProgress = 1;
            }
        } else wetShakeTime = 0;
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
                        base += 10f;
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

        outData += (tblockheatBonus);
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

    public void updateDirtyness(Player player) {
        float passiveIncrease = 0.000666f;
        if (player.isSprinting()) passiveIncrease *= 3;
        boolean swamp =
                player.level().getBiome(player.blockPosition()).is(Biomes.SWAMP) ||
                        player.level().getBiome(player.blockPosition()).is(Biomes.MANGROVE_SWAMP) ||
                        player.level().getBiome(player.blockPosition()).is(Biomes.JUNGLE);


        // Dirt accumulates faster if player is hurt or bleeding
        if (totalBleedSpeed() > 0 || swamp) passiveIncrease += 0.02f;
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

    public String baseToString() {
        return "PlayerHealthData{" +
                "blood=" + blood +
                ", averagePain=" + averagePain +
                ", consciousness=" + consciousness +
                ", hemothorax=" + hemothorax +
                ", hemothoraxpain=" + hemothoraxPain +
                ", internalBleeding=" + internalBleeding +
                ", oxygen=" + bloodOxygen +
                ", oxygenCap=" + OxygenCap +
                ", bpm=" + heartRate +
                ", isBreathing=" + isBreathing +
                ", respiratoryArrest=" + respiratoryArrest +
                ", bloodViscosity=" + bloodViscosity +
                ", immunity=" + immunity +
                ", brainHealth=" + brainHealth +
                ", Shock=" + Shock +
                ", dirtiness=" + dirtiness +
                ", temperature=" + temperature +
                ", hearingLoss=" + hearingLoss +
                ", adrenaline=" + adrenaline +
                ", currentAdrenaline=" + currentAdrenaline +
                '}';
    }
}
