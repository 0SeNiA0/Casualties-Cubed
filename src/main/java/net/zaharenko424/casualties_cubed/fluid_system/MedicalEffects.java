package net.zaharenko424.casualties_cubed.fluid_system;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.config.ServerConfig;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.LimbStatistics;
import net.zaharenko424.casualties_cubed.registry.TimedEffectRegistry;

public class MedicalEffects {

    public static final MedicalEffect CLEAN_WATER = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.drink(0.09f * ml);
                data.temperature(data.temperature() - 0.0025f * ml);
            });
        }
    };

    public static final MedicalEffect CARBONATED_WATER = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.drink(0.09f * ml);
                data.addHappiness(0.008f);
                data.temperature(data.temperature() - 0.0025f * ml);
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
                    data.vomiter.vomit(player);
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
                data.antibioticTimer(data.antibioticTimer() + 11.25f * ml);
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

    public static final MedicalEffect KETCHUP = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.drink(0.06f * ml);
                data.eat(player, 0.08f * ml, 0.01f * ml);
                data.addSickness(0.05f * ml);
                data.addHappiness(-0.025f * ml);
            });
        }
    };

    public static final MedicalEffect MILK = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.drink(0.09f * ml);
                data.eat(player, 0.03f * ml, 0.004f * ml);
                data.temperature(data.temperature() - 0.0025f * ml);
                data.addHappiness(0.005f * ml);
            });
        }

        @Override
        public float injectionSickness() {
            return 0.4f;
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

    public static final MedicalEffect APPLE_JUICE = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.drink(0.09f * ml);
                data.addWeightOffset(0.001f * ml);
                data.temperature(data.temperature() - 0.003f * ml);
                data.addHappiness(0.01f * ml);
            });
        }

        @Override
        public float injectionSickness() {
            return 0.4f;
        }
    };

    public static final MedicalEffect ORANGE_JUICE = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.drink(0.09f * ml);
                data.addWeightOffset(0.0007f * ml);
                data.temperature(data.temperature() - 0.001f * ml);

                if (ServerConfig.EXPIE_MODE.get()) {
                    data.addHappiness(-0.015f * ml);
                    data.addSickness(-0.025f * ml);
                } else data.addHappiness(0.015f * ml);
            });
        }

        @Override
        public float injectionSickness() {
            return 0.4f;
        }
    };

    public static final MedicalEffect LEMONADE = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.drink(0.09f * ml);
                data.addWeightOffset(0.002f * ml);
                data.temperature(data.temperature() - 0.003f * ml);
                data.addHappiness(-0.012f * ml);
            });
        }

        @Override
        public float injectionSickness() {
            return 0.4f;
        }
    };

    public static final MedicalEffect ICE_TEA = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.drink(0.09f * ml);
                data.addWeightOffset(0.004f * ml);
                data.temperature(data.temperature() - 0.003f * ml);
                data.addHappiness(0.014f * ml);

                if (ServerConfig.EXPIE_MODE.get()) {
                    data.addSickness(0.035f * ml);
                }
            });
        }

        @Override
        public float injectionSickness() {
            return 0.4f;
        }
    };

    public static final MedicalEffect SOUP = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.drink(0.09f * ml);
                data.eat(player, 0.1f * ml, 0.005f * ml);
                data.addHappiness(0.01f * ml);
            });
        }

        @Override
        public float injectionSickness() {
            return 0.6f;
        }
    };

    public static final MedicalEffect CHOCOLATE_MILK = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.drink(0.09f * ml);
                data.eat(player, 0.025f * ml, 0.003f * ml);
                data.addHappiness(0.01f * ml);
                data.temperature(data.temperature() - 0.0025f * ml);

                if (ServerConfig.EXPIE_MODE.get()) {
                    data.addSickness(0.12f * ml);
                }
            });
        }

        @Override
        public float injectionSickness() {
            return ServerConfig.EXPIE_MODE.get() ? 1 : 0.4f;
        }
    };

    public static final MedicalEffect CEREAL = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.drink(0.075f * ml);
                data.eat(player, 0.06f * ml, 0.004f * ml);
                data.temperature(data.temperature() - 0.0025f * ml);
                data.addHappiness(0.015f * ml);
            });
        }

        @Override
        public float injectionSickness() {
            return 0.9f;
        }
    };

    public static final MedicalEffect COFFEE = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.drink(0.12f * ml);
                data.addStamina(0.25f * ml);
                data.addEnergy(0.15f * ml);

                data.addSickness((ServerConfig.EXPIE_MODE.get() ? 0.15f : 0.025f) * ml);

                data.addHappiness(0.025f * ml);
                data.addWeightOffset(0.001f * ml);
                data.addCaffeinated(3.5f * ml);
            });
        }

        @Override
        public float injectionSickness() {
            return 0.7f;
        }
    };

    public static final MedicalEffect ENERGY_DRINK = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.drink(0.12f * ml);
                data.addStamina(0.25f * ml);
                data.addEnergy(0.2f * ml);

                data.addSickness((ServerConfig.EXPIE_MODE.get() ? 0.2f : 0.05f) * ml);

                data.addHappiness(0.025f * ml);
                data.addWeightOffset(0.004f * ml);
                data.addCaffeinated(4 * ml);
            });
        }

        @Override
        public float injectionSickness() {
            return 0.7f;
        }
    };

    public static final MedicalEffect SPORTS_DRINK = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.drink(0.13f * ml);
                data.addStamina(0.25f * ml);
                data.addEnergy(0.1f * ml);

                data.addSickness((ServerConfig.EXPIE_MODE.get() ? 0.02f : 0.01f) * ml);

                data.addHappiness(0.005f * ml);
                data.addWeightOffset(0.01f * ml);
            });
        }

        @Override
        public float injectionSickness() {
            return 0.4f;
        }
    };

    public static final MedicalEffect OLIVE_OIL = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.drink(0.03f * ml);
                data.eat(player, 0.08f * ml, 0.02f * ml);
                data.dirtiness(data.dirtiness() + 0.05f * ml);
                data.addSickness(0.12f * ml);
                data.addHappiness(-0.01f * ml);
            });
        }

        @Override
        public float injectionSickness() {
            return 1;
        }
    };

    public static final MedicalEffect HOT_SAUCE = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.drink(0.04f * ml);
                data.eat(player, 0.05f * ml, 0.02f * ml);
                data.addSickness(0.03f * ml);
                data.getLimb(Limb.HEAD).addPain(0.12f * ml);

                data.addTimedEffect(TimedEffectRegistry.HOT_SAUCE, ml, null, 25);
            });
        }

        @Override
        public float injectionSickness() {
            return 1;
        }
    };

    public static final MedicalEffect ICE_CREAM = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.drink(0.06f * ml);
                data.eat(player, 0.06f * ml, 0.012f * ml);

                if (ServerConfig.EXPIE_MODE.get()) data.addSickness(0.015f * ml);

                data.addHappiness(0.015f * ml);

                data.addTimedEffect(TimedEffectRegistry.ICE_CREAM, ml, null, 15);
            });
        }

        @Override
        public float injectionSickness() {
            return ServerConfig.EXPIE_MODE.get() ? 1 : 0.4f;
        }
    };

    public static final MedicalEffect YOGURT = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.drink(0.04f * ml);
                data.eat(player, 0.09f * ml, 0.004f * ml);

                if (ServerConfig.EXPIE_MODE.get()) data.addSickness(0.01f * ml);

                data.addHappiness(0.002f * ml);
                data.temperature(data.temperature() - 0.0015f * ml);
            });
        }

        @Override
        public float injectionSickness() {
            return ServerConfig.EXPIE_MODE.get() ? 1 : 0.4f;
        }
    };

    public static final MedicalEffect MOLD = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.eat(player, 0.03f * ml, 0.004f * ml);
                data.addSickness(0.3f * ml);
                data.addHappiness(-0.04f * ml);

                if (ml > 25) {
                    data.vomiter.vomit(player);
                }
            });
        }

        @Override
        public float injectionSickness() {
            return 3;
        }
    };

    public static final MedicalEffect POWDERED_MILK = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                float f = ml / 30;
                data.drink(-8 * f);
                data.eat(player, 8 * f, 0.4f * f);
                data.addSickness(4 * f);
                data.addHappiness(-0.5f * f);
            });
        }

        @Override
        public float injectionSickness() {
            return 1;
        }
    };

    public static final MedicalEffect RAD_WATER = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.drink(0.09f * ml);
                data.temperature(data.temperature() - 0.002f * ml);

                data.addTimedEffect(TimedEffectRegistry.RAD_WATER, ml, null, 45);
            });
        }

        @Override
        public float injectionSickness() {
            return 0.2f;
        }
    };

    public static final MedicalEffect MERCURY = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addSickness(0.25f * ml);
                data.bloodViscosity(data.bloodViscosity() - 0.2f * ml);
                data.addHappiness(-0.04f * ml);
                data.addTimedEffect(TimedEffectRegistry.MERCURY, ml, null, 200);
            });
        }

        @Override
        public float injectionSickness() {
            return 10;
        }
    };

    public static final MedicalEffect SODA = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.drink(0.09f * ml);
                data.temperature(data.temperature() - 0.003f * ml);
                data.addStamina(0.04f * ml);
                data.addEnergy(0.04f * ml);

                data.addSickness((ServerConfig.EXPIE_MODE.get() ? 0.05f : 0.01f) * ml);

                data.addHappiness(0.015f * ml);
                data.addWeightOffset(0.004f * ml);
                data.addCaffeinated(0.8f * ml);
            });
        }

        @Override
        public float injectionSickness() {
            return 0.6f;
        }
    };

    public static final MedicalEffect ALCOHOL = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.drink(0.075f * ml);
                data.temperature(data.temperature() - 0.0025f * ml);
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
                data.drink(0.07f * ml);
                data.addHappiness(-0.05f * ml);
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
                stats.addPain(0.25f * ml);
                stats.addMuscleHealth(-0.15f * ml);
                stats.addSkinHealAmount(-0.2f * ml);
                stats.addInfection(-0.05f * ml);
                stats.setDisinfectionTimerAtLeast(400 * Mth.clamp(ml * 0.01f, 0, 1));
            });
        }
    };

    public static final MedicalEffect RELIEF_CREAM = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addSickness(0.1f * ml);

                if (player.getRandom().nextFloat() < ml / 80) {
                    data.vomiter.vomit(player);
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
                    data.vomiter.vomit(player);
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
                stats.addSkinHealAmount(0.5f * ml);
                stats.addMuscleHealth(0.25f * ml);
                stats.addInfection(-0.25f * ml);
                stats.setDisinfectionTimerAtLeast(300 * Mth.clamp(ml / 20, 0, 1));
                stats.setPain(stats.getPain() * 0.9f * Mth.clamp(ml / 20, 0, 1));
                data.bloodViscosity(data.bloodViscosity() + 15 * Mth.clamp(ml / 20, 0, 1));
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
                    data.shock(0.5f * ml);
                    //ragdoll, mindwipe
                }
                data.brainGrowSickness(60 * ml);

                if (player.getRandom().nextFloat() < ml / 20) {
                    data.vomiter.vomit(player);
                }
            });
        }
    };

    public static final MedicalEffect ANTIBIOTICS = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.antibioticTimer(data.antibioticTimer() + 25 * ml);
                data.sepsis(data.sepsis() - 0.25f * ml);
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
                data.addVenomTotal(-0.8f * ml);
            });
        }
    };

    public static final MedicalEffect ANTISERUM = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.antibioticTimer(data.antibioticTimer() + 1 * ml);
                data.addSickness(0.3f * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.sepsis(data.sepsis() - 0.2f * ml);
                data.bloodVolume(data.bloodVolume() + 0.001f * ml);
                data.antibioticTimer(data.antibioticTimer() + 6 * ml);

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
                data.bloodViscosity(data.bloodViscosity() - 0.75f * ml);
                data.addSickness(0.3f * ml);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.bloodViscosity(data.bloodViscosity() - 1.5f * ml);
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
                data.bloodViscosity(data.bloodViscosity() - 50 / 750f * ml);
                data.bloodVolume(data.bloodVolume() + ml * 0.001f);
            });
        }
    };

    public static final MedicalEffect YELLOW_BLOOD = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            drinkBlood(player, ml, true);
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            injectBlood(player, ml, limb, true);
        }
    };

    public static final MedicalEffect BLOOD = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            drinkBlood(player, ml, false);
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            injectBlood(player, ml, limb, false);
        }
    };

    private static void drinkBlood(ServerPlayer player, float ml, boolean yellow) {
        player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
            float mlScaled = ml / 750;
            data.drink(20 * mlScaled);
            data.addSickness((ServerConfig.EXPIE_MODE.get() == yellow ? 60 : 70) * mlScaled);
            data.addHappiness(-7 * mlScaled);
        });
    }

    private static void injectBlood(ServerPlayer player, float ml, Limb limb, boolean yellow) {
        player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
            data.bloodVolume(data.bloodVolume() + ml * 0.001f);
            if (ServerConfig.EXPIE_MODE.get() != yellow) {
                float mlScaled = ml / 750;
                data.addSickness(50 * mlScaled);
                data.sepsis(data.sepsis() + 40 * mlScaled);
                data.getLimb(limb).addMuscleHealth(-30 * mlScaled);
            }
        });
    }

    public static final MedicalEffect ALIEN_BLOOD = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                float mlScaled = ml / 750;
                data.drink(20 * mlScaled);
                data.addSickness(60 * mlScaled);
                data.addHappiness(-7 * mlScaled);
            });
        }

        @Override
        public void applyInjected(ServerPlayer player, float ml, Limb limb) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.bloodVolume(data.bloodVolume() + 0.000875f * ml);
                float mlScaled = ml / 750;
                data.sepsis(data.sepsis() + 10 * mlScaled);
                data.addSickness(20 * mlScaled);
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
                data.drink(0.08f * ml);
                data.temperature(data.temperature() - 0.0025f * ml);

                if (player.getRandom().nextFloat() > 0.5f) {
                    data.addSickness(player.getRandom().nextFloat() * 8 + 7);
                    data.addHappiness(-0.5f);
                }
            });
        }

        @Override
        public float injectionSickness() {
            return 0.15f;
        }
    };

    public static final MedicalEffect LUMALGAE = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.drink(0.065f * ml);
                data.eat(player, 0.02f * ml, 0.001f * ml);
                data.temperature(data.temperature() - 0.0025f * ml);
                data.addHappiness(-1);

                if (player.getRandom().nextFloat() > 0.35f) {
                    data.addSickness((player.getRandom().nextFloat() * 0.025f + 0.06f) * ml);
                }

                if (player.getRandom().nextFloat() < 0.1f) {
                    data.vomiter.vomit(player);
                }
            });
        }

        @Override
        public float injectionSickness() {
            return 1.6f;
        }
    };

    public static final MedicalEffect OIL = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.drink(0.01f * ml);
                data.eat(player, 0.02f * ml, 0.03f * ml);
                data.addHappiness(-3);
                data.addSickness(0.3f * ml);
            });
        }

        @Override
        public float injectionSickness() {
            return 5;
        }
    };

    public static final MedicalEffect SAP = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.drink(0.04f * ml);
                data.temperature(data.temperature() - 0.0025f * ml);
                data.addHappiness(0.5f);
                data.addSickness((8 + player.getRandom().nextFloat()) * 0.001f * ml);
            });
        }

        @Override
        public float injectionSickness() {
            return 2;
        }
    };

    public static final MedicalEffect DIRTY_WATER = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.drink(0.06f * ml);
                data.temperature(data.temperature() - 0.0025f * ml);

                if (player.getRandom().nextFloat() > 0.5f) {
                    data.addSickness((player.getRandom().nextFloat() * 0.02f + 0.06f) * ml);
                    data.addHappiness(-1f);
                }
            });
        }

        @Override
        public float injectionSickness() {
            return 2;
        }
    };

    public static final MedicalEffect FAT = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.drink(0.04f * ml);
                data.eat(player, 0.05f * ml, 0.008f * ml);
                data.addHappiness(-0.005f * ml);
                data.addSickness(0.04f * ml);
            });
        }

        @Override
        public float injectionSickness() {
            return 2;
        }
    };

    public static final MedicalEffect SOAP = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.drink(0.04f * ml);
                data.addSickness(0.2f * ml);
                data.addHappiness(-0.02f * ml);
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
                data.dirtiness(data.dirtiness() - 0.25f * ml);
            });
        }
    };

    public static final MedicalEffect PRODUCE_JUICE = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.drink(0.085f * ml);
                data.temperature(data.temperature() - 0.003f * ml);
                data.addHappiness(0.00025f);
                data.eat(player, 0.02f * ml, 0.002f * ml);
            });
        }

        @Override
        public float injectionSickness() {
            return 0.9f;
        }
    };

    public static final MedicalEffect REFINED_JUICE = new MedicalEffect() {

        @Override
        public void applyIngested(ServerPlayer player, float ml) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.drink(0.095f * ml);
                data.temperature(data.temperature() - 0.002f * ml);
                data.addHappiness(0.002f);
                data.eat(player, 0.025f * ml, 0.002f * ml);
            });
        }

        @Override
        public float injectionSickness() {
            return 0.9f;
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
                data.temperature(data.temperature() + 0.5f * ml);
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
                data.temperature(data.temperature() + 0.5f * ml);
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
                data.temperature(data.temperature() + 0.5f * ml);
            });
        }
    };
}
