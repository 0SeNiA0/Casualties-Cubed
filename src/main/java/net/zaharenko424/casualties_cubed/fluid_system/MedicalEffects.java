package net.zaharenko424.casualties_cubed.fluid_system;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.LimbStatistics;
import net.zaharenko424.casualties_cubed.registry.TimedEffectRegistry;

public class MedicalEffects {

    public static final MedicalEffect CLEAN_WATER = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setTemperature(data.getTemperature() - 0.005f * ml);
            });
        }
    };

    public static final MedicalEffect LRD_SERUM = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addSickness(0.05f * ml);
            });
        }
    };

    public static final MedicalEffect MORPHINE = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.painkillers.addOpiates(0.4f * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.painkillers.addOpiates(0.9f * ml);
            });
        }
    };

    public static final MedicalEffect BIO_CHEM = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addSickness(0.4f * ml);
                data.getLimb(Limb.HEAD).setDisinfectionTimerAtLeast(2 * ml);

                data.addTimedEffect(TimedEffectRegistry.BIO_CHEM, ml, null, 10);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addSickness(0.1f * ml);

                LimbStatistics stats = data.getLimb(limb);
                stats.addDisinfectionTimer(0.3f * ml);
                stats.addPain(0.04f * ml);
                stats.addMuscleHealth(-0.01f * ml);
            });
        }
    };

    public static final MedicalEffect OPIUM = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.painkillers.addOpiates(0.2f * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.painkillers.addOpiates(0.4f * ml);
            });
        }
    };

    public static final MedicalEffect PAINKILLERS = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.painkillers.addOpiates(1.4f * ml);
            });
        }
    };

    public static final MedicalEffect HEROIN = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.painkillers.addOpiates(0.6f * ml);
                data.addSickness(0.25f * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.painkillers.addOpiates(1.3f * ml);
                data.addSickness(0.5f * ml);
            });
        }
    };

    public static final MedicalEffect NALOXONE = new MedicalEffect() {

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.painkillers.addAntagonist(0.5f * ml);
            });
        }
    };

    public static final MedicalEffect NALTREXONE = new MedicalEffect() {
        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addTimedEffect(TimedEffectRegistry.NALTREXONE, ml, null, 1.25f * ml);

                if (player.getRandom().nextFloat() < 0.0075 * ml) {
                    data.vomiter.vomit();
                }
                //-happiness
                data.painkillers.addAntagonist(0.75f * ml);
                data.painkillers.addTolerance(-0.75f * ml);
            });
        }
    };

    public static final MedicalEffect CEFTRAIAXONE = new MedicalEffect() {

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setAntibioticTimer(data.getAntibioticTimer() + 11.25f * ml);
                data.getLimb(limb).addPain(0.8f * ml);
            });
        }
    };

    public static final MedicalEffect FENTANYL = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.painkillers.addOpiates(40 * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.painkillers.addOpiates(42 * ml);
            });
        }
    };

    public static final MedicalEffect CHLOROFORM = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addTimedEffect(TimedEffectRegistry.CHLOROFORM, ml, null, 1.8f * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            applyIngested(player, ml);
        }
    };

    public static final MedicalEffect HIGH_GRADE_STIMULANT = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addTimedEffect(TimedEffectRegistry.HIGH_GRADE_STIMULANT, ml, null, 2 * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addTimedEffect(TimedEffectRegistry.HIGH_GRADE_STIMULANT, ml, null, 2.4f * ml);
            });
        }
    };

    public static final MedicalEffect MID_GRADE_STIMULANT = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                //-happiness
                data.addSickness(0.1f * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addTimedEffect(TimedEffectRegistry.MID_GRADE_STIMULANT, ml, null, 3.6f * ml);
            });
        }
    };

    public static final MedicalEffect LOW_GRADE_STIMULANT = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addTimedEffect(TimedEffectRegistry.HIGH_GRADE_STIMULANT, ml, null, 2.5f * ml);
                //-happiness
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addTimedEffect(TimedEffectRegistry.LOW_GRADE_STIMULANT, ml, null, 3.25f * ml);
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

    public static final MedicalEffect MOLD = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addSickness(0.3f * ml);
                //-happiness, +food

                if (ml > 25) {
                    data.vomiter.vomit();
                }
            });
        }

        @Override
        public float injectionSickness() {
            return 3;
        }
    };

    public static final MedicalEffect MERCURY = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addSickness(0.25f * ml);
                data.setBloodViscosity(data.getBloodViscosity() - 0.2f * ml);
                //-happiness
                data.addTimedEffect(TimedEffectRegistry.MERCURY, ml, null, 200);
            });
        }

        @Override
        public float injectionSickness() {
            return 10;
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
        public float injectionSickness() {
            return 0.75f;
        }

        @Override
        public void applyOnSkin(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                LimbStatistics stats = data.getLimb(limb);
                stats.addPain(0.1f * ml);
                stats.setDisinfectionTimerAtLeast(3.5f * ml);
            });
        }
    };

    public static final MedicalEffect BLEACH = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                //-happiness
                data.addTimedEffect(TimedEffectRegistry.BLEACH, ml, null, 3 * ml);
            });
        }

        @Override
        public float injectionSickness() {
            return 5;
        }

        @Override
        public void applyOnSkin(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                LimbStatistics stats = data.getLimb(limb);
                stats.addSkinHealth(-0.2f * ml);
                stats.addMuscleHealth(-0.15f * ml);
                stats.addPain(0.25f * ml);
                stats.addInfection(-0.05f * ml);
                stats.setDisinfectionTimerAtLeast(4 * ml);
            });
        }
    };

    public static final MedicalEffect RELIEF_CREAM = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addSickness(0.1f * ml);

                if (player.getRandom().nextFloat() < ml / 80) {
                    data.vomiter.vomit();
                }
            });
        }

        @Override
        public float injectionSickness() {
            return 0.75f;
        }

        @Override
        public void applyOnSkin(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                LimbStatistics stats = data.getLimb(limb);
                stats.addSkinHealAmount(0.3f * ml);
                stats.setDisinfectionTimerAtLeast(30 * ml);

                data.addTimedEffect(TimedEffectRegistry.RELIEF_CREAM, ml, limb, 1.5f * ml);
            });
        }
    };

    public static final MedicalEffect WOUND_GLUE = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addSickness(0.1f * ml);

                if (player.getRandom().nextFloat() < ml / 80) {
                    data.vomiter.vomit();
                }
            });
        }

        @Override
        public float injectionSickness() {
            return 0.75f;
        }

        @Override
        public void applyOnSkin(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                LimbStatistics stats = data.getLimb(limb);
                stats.addSkinHealth(0.5f * ml);
                stats.addMuscleHealth(0.25f * ml);
                stats.addInfection(-0.25f * ml);
                stats.setDisinfectionTimerAtLeast(15 * ml);
                stats.setPain(stats.getPain() * 0.9f * 1/20 * ml);
                data.setBloodViscosity(data.getBloodViscosity() + 0.75f * ml);
                data.addSickness(0.1125f * ml);
            });
        }
    };

    public static final MedicalEffect BRAINGROW = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addTimedEffect(TimedEffectRegistry.BRAINGROW, ml, null, 100);
                //-happiness
                data.addSickness(1 * ml);

                if (data.brainGrowSickness() > 0 || ml > 40) {
                    data.setShock(0.5f * ml);
                    //ragdoll, mindwipe
                }
                data.brainGrowSickness(60 * ml);

                if (player.getRandom().nextFloat() < ml / 20) {
                    data.vomiter.vomit();
                }
            });
        }
    };

    public static final MedicalEffect ANTIBIOTICS = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setAntibioticTimer(data.getAntibioticTimer() + 25 * ml);
                data.setSepsis(data.getSepsis() - 0.25f * ml);
            });
        }

        @Override
        public float injectionSickness() {
            return 0.5f;
        }
    };

    public static final MedicalEffect ANTIVENOM = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addSickness(0.24f * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addVenom(-0.8f * ml);
            });
        }
    };

    public static final MedicalEffect ANTISERUM = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setAntibioticTimer(data.getAntibioticTimer() + 1 * ml);
                data.addSickness(0.3f * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setSepsis(data.getSepsis() - 0.2f * ml);
                data.setBloodVolume(data.getBloodVolume() + 0.001f * ml);
                data.setAntibioticTimer(data.getAntibioticTimer() + 6 * ml);

                float disinfect = Mth.clamp(ml * 0.02f, 0, 1);
                data.getLimb(limb).setDisinfectionTimerAtLeast(180 * disinfect);

                LimbStatistics stats;
                for (Limb connected : limb.getConnectedLimbs()) {
                    stats = data.getLimb(connected);
                    if (stats.isAmputated()) continue;

                    stats.setDisinfectionTimerAtLeast(150 * disinfect);
                }
            });
        }
    };

    public static final MedicalEffect PROCOAGULANT = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addTimedEffect(TimedEffectRegistry.PROCOAGULANT, ml, null, 12 / 33.34f * ml);
                //-happiness
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addTimedEffect(TimedEffectRegistry.PROCOAGULANT, ml, null, 20 / 33.34f * ml);
            });
        }
    };

    public static final MedicalEffect EPINEPHRINE = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addTimedEffect(TimedEffectRegistry.EPINEPHRINE, ml, null, ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addTimedEffect(TimedEffectRegistry.EPINEPHRINE, ml, null, 6 * ml);
            });
        }
    };

    public static final MedicalEffect OXYLINE = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addTimedEffect(TimedEffectRegistry.OXYLINE_DRINK, ml, null, 1/3f * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addTimedEffect(TimedEffectRegistry.OXYLINE, ml, null, 2 * ml);
            });
        }
    };

    public static final MedicalEffect SODIUM_NITROPRUSSIDE = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addSickness(0.05f * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addBloodPressureChangeFromMedicine(6 * ml);
            });
        }
    };

    public static final MedicalEffect VASOPRESSIN = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addBloodPressureChangeFromMedicine(-0.5f * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addBloodPressureChangeFromMedicine(-6 * ml);
            });
        }
    };

    public static final MedicalEffect AMIODARONE = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addSickness(0.1f * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addTimedEffect(TimedEffectRegistry.AMIODARONE, ml, null, 3 * ml);
            });
        }
    };

    public static final MedicalEffect STREPTOKINASE = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setBloodViscosity(data.getBloodViscosity() - 0.75f * ml);
                data.addSickness(0.3f * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setBloodViscosity(data.getBloodViscosity() - 1.5f * ml);
                data.addSickness(0.15f * ml);
            });
        }
    };

    public static final MedicalEffect SALINE = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addSickness(32 / 750f * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setBloodViscosity(data.getBloodViscosity() - 50 / 750f * ml);
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
                data.getLimb(Limb.HEAD).setDisinfectionTimerAtLeast(240 * Mth.clamp(ml / 100, 0, 1));
            });
        }

        @Override
        public float injectionSickness() {
            return 1.25f;
        }

        @Override
        public void applyOnSkin(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                LimbStatistics stats = data.getLimb(limb);
                stats.addPain(10 * Mth.clamp(ml / 10, 0, 1));
                stats.setDisinfectionTimerAtLeast(Math.min(24 * ml, 240));
            });
        }
    };

    public static final MedicalEffect GROUNDWATER = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setTemperature(data.getTemperature() - 0.005f * ml);

                if (player.getRandom().nextFloat() > 0.5f) {
                    data.addSickness(player.getRandom().nextFloat() * 8 + 7);
                }
            });
        }

        @Override
        public float injectionSickness() {
            return 0.15f;
        }
    };

    public static final MedicalEffect SOAP = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addSickness(0.2f * ml);
                //-happiness, + water
            });
        }

        @Override
        public float injectionSickness() {
            return 1.6f;
        }

        @Override
        public void applyOnSkin(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.getLimb(limb).setDisinfectionTimerAtLeast(30 * Mth.clamp(ml / 100, 0, 1));
                data.setDirtiness(data.getDirtiness() - 0.25f * ml);
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
                head.addBurn(10 * ml);
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
                stats.addBurn(10 * ml);
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
                stats.addBurn(5 * ml);
                data.setTemperature(data.getTemperature() + 0.5f * ml);
            });
        }
    };
}
