package net.zaharenko424.casualties_cubed.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
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
            (player, data, ml, limb, duration) -> {
                LimbStatistics stats = data.getLimb(Limb.HEAD);
                stats.addPain(0.05f * ml);
                stats.addMuscleHealth(-0.025f * ml);

                stats = data.getLimb(Limb.CHEST);
                stats.addPain(0.05f * ml);
                stats.addMuscleHealth(-0.03f * ml);
            }
    );

    public static final RegistryObject<TimedEffectFunction> NALTREXONE = TIMED_EFFECTS.register("naltrexone", () ->
            (player, data, ml, limb, duration) -> data.addSickness(-1));

    public static final RegistryObject<TimedEffectFunction> CHLOROFORM = TIMED_EFFECTS.register("chloroform", () ->
            (player, data, ml, limb, duration) -> data.setConsciousness(Util.moveTowards(8, data.getConsciousness(), 0)));

    public static final RegistryObject<TimedEffectFunction> MERCURY = TIMED_EFFECTS.register("mercury", () ->
            (player, data, ml, limb, duration) -> data.setBrainHealth(data.getBrainHealth() - 0.0005f * ml));

    public static final RegistryObject<TimedEffectFunction> BLEACH = TIMED_EFFECTS.register("bleach", () ->
            (player, data, ml, limb, duration) -> {
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
            (player, data, ml, limb, duration) -> {
                LimbStatistics stats = data.getLimb(limb);
                stats.setPain(Mth.lerp(0.15f, stats.getPain(), stats.getPain() * 0.1f));
            }
    );

    public static final RegistryObject<TimedEffectFunction> BRAINGROW = TIMED_EFFECTS.register("braingrow", () ->
            (player, data, ml, limb, duration) -> {
                data.setBrainHealth(data.getBrainHealth() + 0.005f * ml);
                data.addStrokeAmount(-1.5f);
            });

    public static final RegistryObject<TimedEffectFunction> PROCOAGULANT = TIMED_EFFECTS.register("procoagulant", () ->
            (player, data, ml, limb, duration) -> {
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
            (player, data, ml, limb, duration) -> {
                data.setAdrenaline(100);

                if (player.isAlive() && data.isCardiacArrest() && player.getRandom().nextFloat() < 0.05f) {
                    data.setHeartRate(200);
                    data.setFibrillationProgress(50);
                }

                if (duration > 240) {
                    data.tryStartFibrillation(true);
                }
            }
    );

    public static final RegistryObject<TimedEffectFunction> OXYLINE_DRINK = TIMED_EFFECTS.register("oxyline_drink", () ->
            (player, data, ml, limb, duration) -> {
                LimbStatistics stats = data.getLimb(Limb.CHEST);
                stats.addPain(8);
                stats.addMuscleHealth(-2.5f);

                data.setShock(data.getShock() + 28);
            }
    );

    public static final RegistryObject<TimedEffectFunction> OXYLINE = TIMED_EFFECTS.register("oxyline", () ->
            (player, data, ml, limb, duration) -> {
                data.addRespiratoryRate(2.5f);
                data.setBloodOxygen(data.getBloodOxygen() + 1.666f);
                //+2.5 stamina
                data.setFibrillationProgress(data.getFibrillationProgress() - 1.2f);
                data.setBloodVolume(data.getBloodVolume() + 0.0025f);
            }
    );

    public static final RegistryObject<TimedEffectFunction> AMIODARONE = TIMED_EFFECTS.register("amiodarone", () ->
            (player, data, ml, limb, duration) -> {
                if (data.getFibrillationProgress() > 0) {
                    data.setFibrillationProgress(Util.moveTowards(2, data.getFibrillationProgress(), 0));
                }

                data.getLimb(Limb.HEAD).addMuscleHealth(-0.25f);
                data.getLimb(Limb.CHEST).addMuscleHealth(-0.25f);
                //second part of chest also -0.25
            }
    );
}
