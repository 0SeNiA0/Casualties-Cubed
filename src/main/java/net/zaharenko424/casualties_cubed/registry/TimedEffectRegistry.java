package net.zaharenko424.casualties_cubed.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.LimbStatistics;
import net.zaharenko424.casualties_cubed.limbs.TimedEffectFunction;
import net.zaharenko424.casualties_cubed.util.Util;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class TimedEffectRegistry {

    public static final ResourceLocation LOC = CasualtiesCubed.resourceLoc("timed_effect");

    public static final DeferredRegister<TimedEffectFunction> TIMED_EFFECTS = DeferredRegister.create(LOC, CasualtiesCubed.MOD_ID);

    private static Supplier<IForgeRegistry<TimedEffectFunction>> supplier;
    public static IForgeRegistry<TimedEffectFunction> registry() {
        if (supplier == null) throw new IllegalStateException("TimedEffectFunction registry access before registry is created");
        return supplier.get();
    }

    @SubscribeEvent
    public static void onNewRegistry(NewRegistryEvent event) {
        supplier = event.create(RegistryBuilder.<TimedEffectFunction>of(LOC).disableSync());
    }

    public static final RegistryObject<TimedEffectFunction> BIO_CHEM = TIMED_EFFECTS.register("bio_chem", () ->
            (player, data, effect) -> {
                float ml = effect.ml();
                LimbStatistics stats = data.getLimb(Limb.HEAD);
                stats.addPain(0.05f * ml);
                stats.addMuscleHealth(-0.025f * ml);

                stats = data.getLimb(Limb.CHEST);
                stats.addPain(0.05f * ml);
                stats.addMuscleHealth(-0.03f * ml);
            }
    );

    public static final RegistryObject<TimedEffectFunction> NALTREXONE = TIMED_EFFECTS.register("naltrexone", () ->
            (player, data, effect) -> data.addSickness(-1));

    public static final RegistryObject<TimedEffectFunction> CHLOROFORM = TIMED_EFFECTS.register("chloroform", () ->
            (player, data, effect) -> data.setConsciousness(Util.moveTowards(8, data.getConsciousness(), 0)));

    public static final RegistryObject<TimedEffectFunction> MERCURY = TIMED_EFFECTS.register("mercury", () ->
            (player, data, effect) -> data.setBrainHealth(data.getBrainHealth() - 0.0005f * effect.ml()));

    public static final RegistryObject<TimedEffectFunction> BLEACH = TIMED_EFFECTS.register("bleach", () ->
            (player, data, effect) -> {
                data.getLimb(Limb.HEAD).addSkinHealth(-0.1f);

                LimbStatistics stats = data.getLimb(Limb.CHEST);
                stats.addMuscleHealth(-0.225f);
                if (stats.getPain() < 50) {
                    stats.addPain(1.5f);
                }

                data.addSickness(0.3f);
            }
    );

    public static final RegistryObject<TimedEffectFunction> RELIEF_CREAM = TIMED_EFFECTS.register("relief_cream", () ->
            (player, data, effect) -> {
                LimbStatistics stats = data.getLimb(effect.limb());
                stats.setPain(Mth.lerp(0.15f, stats.getPain(), stats.getPain() * 0.1f));
            }
    );

    public static final RegistryObject<TimedEffectFunction> BRAINGROW = TIMED_EFFECTS.register("braingrow", () ->
            (player, data, effect) -> {
                data.setBrainHealth(data.getBrainHealth() + 0.005f * effect.ml());
                data.addStrokeAmount(-1.5f);
            }
    );

    public static final RegistryObject<TimedEffectFunction> PROCOAGULANT = TIMED_EFFECTS.register("procoagulant", () ->
            (player, data, effect) -> {
                data.setInternalBleeding(data.getInternalBleeding() * 0.95f);
                data.setBloodViscosity(data.getBloodViscosity() + 1.75f);
                data.addStrokeAmount(-10);

                LimbStatistics stats;
                for (Limb l : Limb.values()) {
                    stats = data.getLimb(l);
                    if (!stats.isAmputated()) stats.setBleedRate(stats.getBleedRate() * 0.96f);
                }
            }
    );

    public static final RegistryObject<TimedEffectFunction> EPINEPHRINE = TIMED_EFFECTS.register("epinephrine", () ->
            (player, data, effect) -> {
                data.setAdrenaline(100);

                if (player.isAlive() && data.isCardiacArrest() && player.getRandom().nextFloat() < 0.05f) {
                    data.setHeartRate(200);
                    data.setFibrillationProgress(50);
                }

                if (effect.duration() > 240) {
                    data.tryStartFibrillation(true);
                }
            }
    );

    public static final RegistryObject<TimedEffectFunction> OXYLINE_DRINK = TIMED_EFFECTS.register("oxyline_drink", () ->
            (player, data, effect) -> {
                LimbStatistics stats = data.getLimb(Limb.CHEST);
                stats.addPain(8);
                stats.addMuscleHealth(-2.5f);

                data.setShock(data.getShock() + 28);
            }
    );

    public static final RegistryObject<TimedEffectFunction> OXYLINE = TIMED_EFFECTS.register("oxyline", () ->
            (player, data, effect) -> {
                data.addRespiratoryRate(2.5f);
                data.setBloodOxygen(data.getBloodOxygen() + 1.666f);
                //+2.5 stamina
                data.setFibrillationProgress(data.fibrillationProgress() - 1.2f);
                data.setBloodVolume(data.getBloodVolume() + 0.0025f);
            }
    );

    public static final RegistryObject<TimedEffectFunction> AMIODARONE = TIMED_EFFECTS.register("amiodarone", () ->
            (player, data, effect) -> {
                if (data.fibrillationProgress() > 0) {
                    data.setFibrillationProgress(Util.moveTowards(2, data.fibrillationProgress(), 0));
                }

                data.getLimb(Limb.HEAD).addMuscleHealth(-0.25f);
                data.getLimb(Limb.CHEST).addMuscleHealth(-0.25f);
                //second part of chest also -0.25
            }
    );

    public static final RegistryObject<TimedEffectFunction> HIGH_GRADE_STIMULANT = TIMED_EFFECTS.register("high_grade_stimulant", () ->
            (player, data, effect) -> {
                //+stamina
                data.setConsciousness(data.getConsciousness() + 3.5f);
                data.setAdrenaline(data.getAdrenaline() + 25);

                RandomSource random = player.getRandom();
                //shakeIntensity
                if (data.stimulantMultiplier() < 0.32f) {
                    data.addStimulantMultiplier(0.05f);
                }

                if (effect.duration() > 320) {
                    //more shakeIntensity
                    //ragdoll
                    if (random.nextFloat() < 0.05f) {
                        data.setConsciousness(data.getConsciousness() - 50);
                    }
                    if (random.nextFloat() < 0.05f) {
                        data.addTemperature(-1);
                    }
                    //control reverse
                    if (data.stimulantMultiplier()  > -0.5f) {
                        data.addStimulantMultiplier(-0.1f);
                    }
                    data.overdoseIndex(3);
                }

                if (effect.highestDuration() > 80 && effect.duration() <= 1) {
                    //-energy
                    if (effect.highestDuration() > 320) {
                        //-energy
                        data.vomiter.vomit();
                    }
                }
            }
    );

    public static final RegistryObject<TimedEffectFunction> MID_GRADE_STIMULANT = TIMED_EFFECTS.register("mid_grade_stimulant", () ->
            (player, data, effect) -> {
                //+stamina
                data.setConsciousness(data.getConsciousness() + 2);
                //+energy
                data.addSickness(0.1f);
                data.setInternalBleeding(data.getInternalBleeding() + 0.000528f);
                data.setAdrenaline(data.getAdrenaline() + 7);

                RandomSource random = player.getRandom();
                //shakeIntensity
                if (data.stimulantMultiplier() < 0.25f) {
                    data.addStimulantMultiplier(0.035f);
                }

                if (effect.duration() > 220) {
                    //more shakeIntensity
                    //-stamina
                    //ragdoll
                    data.setInternalBleeding(data.getInternalBleeding() + 0.00132f);
                    data.setBrainHealth(data.getBrainHealth() - 0.05f);

                    LimbStatistics stats = data.getLimb(Limb.CHEST);
                    if (stats.getPain() < 60) {
                        stats.addPain(4);
                    }
                    data.overdoseIndex(3);
                }

                if (effect.highestDuration() > 59) {
                    //-stamina
                    if (effect.duration() <= 1) {
                        //-energy
                        data.vomiter.vomit();
                    }
                }
            }
    );

    public static final RegistryObject<TimedEffectFunction> LOW_GRADE_STIMULANT = TIMED_EFFECTS.register("low_grade_stimulant", () ->
            (player, data, effect) -> {
                //+stamina
                data.setConsciousness(data.getConsciousness() + 1);
                //+energy
                data.addSickness(0.18f);
                data.setAdrenaline(data.getAdrenaline() + 20);
                data.addTemperature(0.03f);

                RandomSource random = player.getRandom();
                //shakeIntensity
                if (data.stimulantMultiplier() < 0.175f) {
                    data.addStimulantMultiplier(0.035f);
                }

                if (effect.duration() > 160) {
                    //more shakeIntensity
                    //-stamina
                    //-energy
                    if (random.nextFloat() < 0.075f) {
                        data.setBloodOxygen(data.getBloodOxygen() - 3);
                    }
                    //ragdoll
                    if (random.nextFloat() < 0.06f) {
                        data.setConsciousness(0);
                    }
                    if (random.nextFloat() < 0.035f) {
                        data.vomiter.vomit();
                    }
                    if (random.nextFloat() < 0.02f) {
                        data.setAdrenaline(0);
                    }
                    //control reverse

                    data.addTemperature(0.04f);
                    data.setBrainHealth(data.getBrainHealth() - 0.08f);

                    LimbStatistics stats = data.getLimb(Limb.CHEST);
                    if (stats.getPain() < 60) {
                        stats.addPain(4);
                    }

                    stats = data.getLimb(Limb.HEAD);
                    if (stats.getPain() < 60) {
                        stats.addPain(4);
                    }
                    data.overdoseIndex(3);
                }

                if (effect.highestDuration() > 50) {
                    if (effect.duration() < 25 && data.getConsciousness() > effect.duration() * 4) {
                        data.setConsciousness(effect.duration() * 4);
                    }

                    if (effect.duration() <= 1) {
                        //-energy
                        data.setConsciousness(0);
                        data.vomiter.vomit();
                    }
                }
            }
    );
}
