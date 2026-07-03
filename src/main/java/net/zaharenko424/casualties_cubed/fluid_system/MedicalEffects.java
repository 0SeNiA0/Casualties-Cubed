package net.zaharenko424.casualties_cubed.fluid_system;

import net.minecraft.server.level.ServerPlayer;
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


    public static final MedicalEffect MORPHINE = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setPendingOpioids(data.getPendingOpioids() + 0.4f * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setPendingOpioids(data.getPendingOpioids() + 0.9f * ml);
            });
        }
    };

    public static final MedicalEffect OPIUM = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setPendingOpioids(data.getPendingOpioids() + 0.2f * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setPendingOpioids(data.getPendingOpioids() + 0.4f * ml);
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

    public static final MedicalEffect HEROIN = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setPendingOpioids(data.getPendingOpioids() + 0.6f * ml);
                data.addSickness(0.25f * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setPendingOpioids(data.getPendingOpioids() + 1.3f * ml);
                data.addSickness(0.5f * ml);
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

    public static final MedicalEffect CEFTRAIAXONE = new MedicalEffect() {

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setAntibioticTimer(data.getAntibioticTimer() + 225 * ml);
                data.getLimb(Limb.CHEST).addPain(1.5f * ml);
            });
        }
    };

    public static final MedicalEffect FENTANYL = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setPendingOpioids(data.getPendingOpioids() + 40 * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setPendingOpioids(data.getPendingOpioids() + 42 * ml);
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

    public static final MedicalEffect ALCOHOL = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setTemperature(data.getTemperature() - 0.0025f * ml);
                data.addSickness(0.07f * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setBloodViscosity(data.getBloodViscosity() + 0.075f * ml);
                data.addSickness(0.263f * ml);
            });
        }

        @Override
        public void applyOnSkin(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                LimbStatistics stats = data.getLimb(limb);
                stats.addPain(0.1f * ml);
                stats.setDisinfectionTimerAtLeast(70 * ml);
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

                data.setInternalBleeding(data.getInternalBleedingCapped() + 0.01f * ml);
                data.addSickness(0.9f * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setBloodViscosity(data.getBloodViscosity() + 0.5f * ml);
                data.addSickness(1.75f * ml);
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

    public static final MedicalEffect RELIEF_CREAM = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addSickness(0.1f * ml);
                //chance to vomit (wat chance tho...)
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addSickness(0.263f * ml);
                data.setBloodViscosity(data.getBloodViscosity() + 0.075f * ml);
            });
        }

        @Override
        public void applyOnSkin(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                LimbStatistics stats = data.getLimb(limb);
                stats.setDisinfectionTimerAtLeast(636 * ml);
                stats.setPain(stats.getPain() * 0.01f);
            });
        }
    };

    public static final MedicalEffect WOUND_GLUE = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addSickness(0.1f * ml);
                //chance to vomit
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addSickness(0.263f * ml);
                data.setBloodViscosity(data.getBloodViscosity() + 0.075f * ml);
            });
        }

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
                data.addSickness(0.1125f * ml);
            });
        }
    };

    public static final MedicalEffect BRAINGROW = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setBrainHealth(data.getBrainHealth() + ml);
                data.getLimb(Limb.HEAD).addPain(5 * ml);
                data.addSickness(1 * ml);
                //chance to vomit
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setBrainHealth(data.getBrainHealth() - ml / 2);
                data.getLimb(Limb.HEAD).addPain(10 * ml);
                data.addSickness(0.35f * ml);
                data.setBloodViscosity(data.getBloodViscosity() + 0.1f * ml);
            });
        }
    };

    public static final MedicalEffect ANTIBIOTICS = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setAntibioticTimer(Math.max(data.getAntibioticTimer(), 4000));
                data.setSepsis(data.getSepsis() - 0.25f * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addSickness(1.75f * ml);
                data.setBloodViscosity(data.getBloodViscosity() + 0.5f * ml);
            });
        }
    };

    public static final MedicalEffect ANTIVENOM = new MedicalEffect() {

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addVenom(-0.8f * ml);
            });
        }

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addSickness(0.24f * ml);
            });
        }
    };

    public static final MedicalEffect ANTISERUM = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setAntibioticTimer(Math.max(data.getAntibioticTimer(), 6000));
                data.setBloodVolume(data.getBloodVolume() + 0.001f * ml);
                data.addSickness(0.3f * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setSepsis(data.getSepsis() - 0.2f * ml);
                data.setBloodVolume(data.getBloodVolume() + 0.001f * ml);
                data.setAntibioticTimer(Math.max(data.getAntibioticTimer(), 6000));
                data.getLimb(limb).addDisinfectionTimer(72 * ml);
            });
        }
    };

    public static final MedicalEffect PROCOAGULANT = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                LimbStatistics stats;
                float bleedScale = Math.max(0, (1f - ml * 0.01f));
                for (Limb limb1 : Limb.values()) {
                    stats = data.getLimb(limb1);
                    stats.setBleedRate(stats.getBleedRate() * bleedScale);
                }

                data.setInternalBleeding(data.getInternalBleedingCapped() * (1f - ml * 0.01f));
                data.setBloodViscosity(data.getBloodViscosity() + 0.6f * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                LimbStatistics stats;
                float bleedScale = Math.max(0, (1f - ml * 0.016f));
                for (Limb limb1 : Limb.values()) {
                    stats = data.getLimb(limb1);
                    stats.setBleedRate(stats.getBleedRate() * bleedScale);
                }

                data.setInternalBleeding(data.getInternalBleedingCapped() * (1f - ml * 0.017f));
                data.setBloodViscosity(data.getBloodViscosity() + 0.6f * ml);
            });
        }
    };

    public static final MedicalEffect STREPTOKINASE = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                LimbStatistics stats;
                float bleedScale = 1f + ml * 0.014f;
                for (Limb limb1 : Limb.values()) {
                    stats = data.getLimb(limb1);
                    stats.setBleedRate(stats.getBleedRate() * bleedScale);
                }

                data.setInternalBleeding(data.getInternalBleedingCapped() * (1f + ml * 0.0147f));
                data.setBloodViscosity(data.getBloodViscosity() - 0.75f * ml);
                data.addSickness(10 / 33.34f * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                LimbStatistics stats;
                float bleedScale = 1f + ml * 0.014f;
                for (Limb limb1 : Limb.values()) {
                    stats = data.getLimb(limb1);
                    stats.setBleedRate(stats.getBleedRate() * bleedScale);
                }

                data.setInternalBleeding(data.getInternalBleedingCapped() * (1f + ml * 0.0147f));
                data.setBloodViscosity(data.getBloodViscosity() - 1.5f * ml);
                data.addSickness(5 / 33.34f * ml);
            });
        }
    };

    public static final MedicalEffect SALINE = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addSickness(0.042f * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setBloodViscosity(data.getBloodViscosity() - 0.002f * ml);
                data.setBloodVolume(data.getBloodVolume() + ml * 0.001f);
            });
        }
    };

    public static final MedicalEffect BLOOD = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addSickness(0.08f * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setBloodVolume(data.getBloodVolume() + ml * 0.001f);
            });
        }
    };

    public static final MedicalEffect ANTISEPTIC = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addSickness(0.15f * ml);
                LimbStatistics stats = data.getLimb(Limb.HEAD);
                stats.addPain(1 * ml);
                stats.setDisinfectionTimerAtLeast(480 * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addSickness(0.438f * ml);
                data.setBloodViscosity(data.getBloodViscosity() + 0.125f * ml);
            });
        }

        @Override
        public void applyOnSkin(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                LimbStatistics stats = data.getLimb(limb);
                stats.addPain(1 * ml);
                stats.setDisinfectionTimerAtLeast(480 * ml);//4 * 60 * 20 / 10
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
}
