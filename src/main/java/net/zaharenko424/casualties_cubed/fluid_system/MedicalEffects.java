package net.zaharenko424.casualties_cubed.fluid_system;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
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
                data.painkillers().addOpiates(0.4f * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.painkillers().addOpiates(0.9f * ml);
            });
        }
    };

    public static final MedicalEffect BIO_CHEM = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addSickness(0.4f * ml);
                data.getLimb(Limb.HEAD).setDisinfectionTimerAtLeast(2 * ml);

                LimbStatistics stats;
                for (Limb limb : Limb.values()) {//TODO timed effect
                    stats = data.getLimb(limb);
                    if (stats.isAmputated()) continue;

                    stats.addPain(0.05f * ml);
                    stats.addMuscleHealth(-0.03f * ml);
                }
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
                data.painkillers().addOpiates(0.2f * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.painkillers().addOpiates(0.4f * ml);
            });
        }
    };

    public static final MedicalEffect PAINKILLERS = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.painkillers().addOpiates(1.4f * ml);
            });
        }
    };

    public static final MedicalEffect HEROIN = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.painkillers().addOpiates(0.6f * ml);
                data.addSickness(0.25f * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.painkillers().addOpiates(1.3f * ml);
                data.addSickness(0.5f * ml);
            });
        }
    };

    public static final MedicalEffect NALOXONE = new MedicalEffect() {

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.painkillers().addAntagonist(0.5f * ml);
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
                data.painkillers().addOpiates(40 * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.painkillers().addOpiates(42 * ml);
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
                final  float sickness = 0.75f;//TODO any liquid when injected reduces viscosity by 0.1 * injectSickness (here 0.75).
                data.setBloodViscosity(data.getBloodViscosity() - 0.1f * sickness * ml);//might be a bug but in CU viscosity penalty doesnt scale with ml
                data.addSickness(0.3f * sickness * ml);
            });
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
                final float sickness = 5;
                data.setBloodViscosity(data.getBloodViscosity() - 0.1f * sickness * ml);
                data.addSickness(0.3f * sickness * ml);
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
                    data.vomiter().vomit();
                }
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                final float sickness = 0.75f;
                data.setBloodViscosity(data.getBloodViscosity() - 0.1f * sickness * ml);
                data.addSickness(0.3f * sickness * ml);
            });
        }

        @Override
        public void applyOnSkin(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                LimbStatistics stats = data.getLimb(limb);
                stats.setDisinfectionTimerAtLeast(30 * ml);
                stats.setPain(stats.getPain() * 0.01f);
            });
        }
    };

    public static final MedicalEffect WOUND_GLUE = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addSickness(0.1f * ml);

                if (player.getRandom().nextFloat() < ml / 80) {
                    data.vomiter().vomit();
                }
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                final float sickness = 0.75f;
                data.setBloodViscosity(data.getBloodViscosity() - 0.1f * sickness * ml);
                data.addSickness(0.3f * sickness * ml);
            });
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
                data.setBrainHealth(data.getBrainHealth() + ml);
                data.getLimb(Limb.HEAD).addPain(5 * ml);
                data.addSickness(1 * ml);

                if (player.getRandom().nextFloat() < ml / 20) {
                    data.vomiter().vomit();
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
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                final float sickness = 0.5f;
                data.setBloodViscosity(data.getBloodViscosity() - 0.1f * sickness * ml);
                data.addSickness(0.3f * sickness * ml);
            });
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

    public static final MedicalEffect PROCOAGULANT = new MedicalEffect() {//FIXME update with timed runnables

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
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                final float sickness = 1.25f;
                data.setBloodViscosity(data.getBloodViscosity() - 0.1f * sickness * ml);
                data.addSickness(0.3f * sickness * ml);
            });
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
