package net.zaharenko424.casualties_cubed.fluid_system;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.ForgeMod;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.LimbStatistics;

public class MedicalEffects {

    public static MedicalEffect forFluid(Fluid fluid) {
        if (fluid instanceof MedicalFluid medFluid) return medFluid.getEffect();

        if (fluid.getFluidType() == ForgeMod.WATER_TYPE.get()) return MedicalEffects.WATER;

        if (fluid.getFluidType() == ForgeMod.LAVA_TYPE.get()) return MedicalEffects.LAVA;

        return ExtraMedFluids.getOrDef(fluid).effect();
    }

    public static final MedicalEffect OPIUM = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setPendingOpioids(data.getPendingOpioids() + ml * 0.3f);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setPendingOpioids(data.getPendingOpioids() + ml * 0.5f);
            });
        }
    };

    public static final MedicalEffect MORPHINE = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setPendingOpioids(data.getPendingOpioids() + ml * 0.7f);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setPendingOpioids(data.getPendingOpioids() + ml * 1.1f);
            });
        }
    };

    public static final MedicalEffect HEROIN = new MedicalEffect() {
        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setPendingOpioids(data.getPendingOpioids() + ml * 1.5f);
            });
        }
    };

    public static final MedicalEffect FENTANYL = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.addEffect(new MobEffectInstance(MobEffects.WITHER, 300, 5));
            player.addEffect(new MobEffectInstance(MobEffects.POISON, 300, 5));
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setTemperature(data.getTemperature() - 0.005f * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setPendingOpioids(data.getPendingOpioids() + ml * 50f);
            });
        }
    };

    public static final MedicalEffect WATER = new MedicalEffect() {
        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setTemperature(data.getTemperature() - 0.005f * ml);
            });
        }
    };

    public static final MedicalEffect PAINKILLERS = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setPendingOpioids(data.getPendingOpioids() + (1.4f * ml));
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setPendingOpioids(data.getPendingOpioids() + (0.1f * ml));
            });
        }
    };

    public static final MedicalEffect STREPTOKINASE = new MedicalEffect() {
        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                LimbStatistics stats;
                float bleedScale = 1f + ml * 0.014f;
                for (Limb limb1 : Limb.values()) {
                    stats = data.getLimb(limb1);
                    stats.setBleedRate(stats.getBleedRate() * bleedScale);
                }

                data.setInternalBleeding(data.getInternalBleeding() * (1f + ml * 0.0147f));
                data.setBloodViscosity(data.getBloodViscosity() - 1.5f * ml);
            });
        }
    };

    public static final MedicalEffect PROCOAGULANT = new MedicalEffect() {
        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                LimbStatistics stats;
                float bleedScale = Math.max(0, (1f - ml * 0.016f));
                for (Limb limb1 : Limb.values()) {
                    stats = data.getLimb(limb1);
                    stats.setBleedRate(stats.getBleedRate() * bleedScale);
                }

                data.setInternalBleeding(data.getInternalBleeding() * (1f - ml * 0.017f));
                data.setBloodViscosity(data.getBloodViscosity() + 0.6f * ml);
            });
        }
    };

    public static final MedicalEffect WOUND_GLUE = new MedicalEffect() {
        @Override
        public void applyOnSkin(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                LimbStatistics stats = data.getLimb(limb);
                stats.addSkinHealth(0.5f * ml);
                stats.addMuscleHealth(0.25f * ml);
                stats.addInfection(-0.25f * ml);
                stats.addDisinfectionTimer(300 * ml);
                stats.addPain(-stats.getPain() * 0.9f * 1/20 * ml);
                data.setBloodViscosity(data.getBloodViscosity() + 0.75f * ml);
                //0.1125 sickness
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setBloodViscosity(data.getBloodViscosity() + 0.075f * ml);
                //0.263 sickness
            });
        }
    };

    public static final MedicalEffect NALOXONE = new MedicalEffect() {
        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setOpioids(data.getOpioids() - (4f * ml));
                data.setPendingOpioids(data.getPendingOpioids() - (4f * ml));
            });
        }
    };

    public static final MedicalEffect ALCOHOL = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, (int) (1 * ml), 1));
            player.addEffect(new MobEffectInstance(MobEffects.POISON, (int) (1 * ml), 2));
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setTemperature(data.getTemperature() - 0.005f * ml);
            });
        }

        @Override
        public void applyOnSkin(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                LimbStatistics stats = data.getLimb(limb);
                stats.addPain(0.1f * ml);
                stats.setDisinfectionTimerAtLeast(700 * ml);
            });
        }
    };

    public static final MedicalEffect BRAINGROW = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setBrainHealth(data.getBrainHealth() + ml);
                data.getLimb(Limb.HEAD).addPain(5 * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setBrainHealth(data.getBrainHealth() - ml / 2);
                data.getLimb(Limb.HEAD).addPain(10 * ml);
            });
        }
    };

    public static final MedicalEffect ANTISEPTIC = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, (int) (1 * ml), 2));
            player.addEffect(new MobEffectInstance(MobEffects.POISON, (int) (1 * ml), 3));
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                LimbStatistics stats = data.getLimb(limb);
                stats.addPain(5 * ml);
                stats.setDisinfectionTimerAtLeast(300 * ml);
            });
        }

        @Override
        public void applyOnSkin(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                LimbStatistics stats = data.getLimb(limb);
                stats.addPain(3.5f * ml);
                stats.setDisinfectionTimerAtLeast(1200 * ml);
            });
        }
    };

    public static final MedicalEffect RELIEF_CREAM = new MedicalEffect() {
        @Override
        public void applyOnSkin(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                LimbStatistics stats = data.getLimb(limb);
                stats.setDisinfectionTimerAtLeast(636 * ml);
                stats.setPain(stats.getPain() * 0.01f);
            });
        }
    };

    public static final MedicalEffect SALINE = new MedicalEffect() {
        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setBloodViscosity(data.getBloodViscosity() + 0.01f * ml);
                data.setBloodVolume(data.getBloodVolume() + ml * 0.001f);
            });
        }
    };

    public static final MedicalEffect BLOOD = new MedicalEffect() {
        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setBloodVolume(data.getBloodVolume() + ml * 0.001f);
            });
        }
    };

    public static final MedicalEffect ANTIBIOTICS = new MedicalEffect() {
        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setAntibioticTimer(Math.max(data.getAntibioticTimer(), 4000));
            });
        }
    };

    public static final MedicalEffect ANTISERUM = new MedicalEffect() {
        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setAntibioticTimer(Math.max(data.getAntibioticTimer(), 6000));
                data.getLimb(limb).addDisinfectionTimer(72 * ml);
                data.setBloodVolume(data.getBloodVolume() + 0.001f * ml);
            });
        }
    };

    public static final MedicalEffect CEFTRAIAXONE = new MedicalEffect() {
        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setAntibioticTimer(data.getAntibioticTimer() + 225 * ml);
                data.getLimb(Limb.CHEST).addPain(1.5f * ml);
            });
        }
    };

    public static final MedicalEffect BLEACH = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                LimbStatistics head = data.getLimb(Limb.HEAD), chest = data.getLimb(Limb.CHEST);
                head.addSkinHealth(-0.3f * ml);
                if (head.getPain() < 50) head.addPain(Math.min(1.5f * ml, 50 - head.getPain()));

                chest.addMuscleHealth(-0.675f * ml);
                if (chest.getPain() < 50) chest.addPain(Math.min(1.5f * ml, 50 - chest.getPain()));

                data.setInternalBleeding(data.getInternalBleeding() + 0.01f * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setBloodViscosity(data.getBloodViscosity() + 0.5f * ml);
                //1.75 sickness
            });
        }

        @Override
        public void applyOnSkin(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                LimbStatistics stats = data.getLimb(limb);
                stats.addSkinHealth(-0.2f * ml);
                stats.addMuscleHealth(-0.15f * ml);
                stats.addPain(0.25f * ml);
                stats.addInfection(-0.05f * ml);
                stats.addDisinfectionTimer(80 * ml);
            });
        }
    };

    public static final MedicalEffect LAVA = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                LimbStatistics head = data.getLimb(Limb.HEAD);
                head.addSkinHealth(-15 * ml);
                head.addPain(ml * 2);
                head.addMuscleHealth(-15 * ml);
                data.setTemperature(data.getTemperature() + 0.5f * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                LimbStatistics stats = data.getLimb(limb);
                stats.addSkinHealth(-10 * ml);
                stats.addPain(2 * ml);
                stats.addMuscleHealth(-20 * ml);
                data.setTemperature(data.getTemperature() + 0.5f * ml);
            });
        }

        @Override
        public void applyOnSkin(ServerPlayer player, float ml, Limb limb) {
            player.setSecondsOnFire((int) (0.1 * ml));
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                LimbStatistics stats = data.getLimb(limb);
                stats.addSkinHealth(-20 * ml);
                stats.addPain(2.5f * ml);
                stats.addMuscleHealth(-10 * ml);
                data.setTemperature(data.getTemperature() + 0.5f * ml);
            });
        }
    };

    public static final MedicalEffect CHOCOLATE_MILK = new MedicalEffect() {
        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h -> {
                h.setTemperature(h.getTemperature() - 0.01f * ml);
            });
        }
    };
}
