package net.zaharenko424.casualties_cubed.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CasualtiesCubed.MOD_ID);

    public static final RegistryObject<SoundEvent> HEALTH_SCREEN_OPEN = register("health_screen_open");
    public static final RegistryObject<SoundEvent> HEALTH_SCREEN_CLOSE = register("health_screen_close");
    public static final RegistryObject<SoundEvent> HEART_THUMP = register("heart_thump");
    public static final RegistryObject<SoundEvent> HEART_THUMP_HEAVY = register("heart_thump_heavy");
    public static final RegistryObject<SoundEvent> HEART_THUMP_HEAVY_MONITOR = register("heart_thump_heavy_monitor");
    public static final RegistryObject<SoundEvent> SPRAY = register("spray");
    public static final RegistryObject<SoundEvent> SPLINT = register("splint");
    public static final RegistryObject<SoundEvent> AUTO_PUMP = register("auto_pump");
    public static final RegistryObject<SoundEvent> BONE_WELD = register("bone_weld");
    public static final RegistryObject<SoundEvent> DRAIN_USE = register("drain_use");
    public static final RegistryObject<SoundEvent> LEVEL_UP = register("level_up");

    public static final RegistryObject<SoundEvent> CLICK = register("click");
    public static final RegistryObject<SoundEvent> SMALL_CLICK = register("small_click");

    public static final RegistryObject<SoundEvent> VOMIT_WARNING = register("vomit_warning");
    public static final RegistryObject<SoundEvent> BLOOD_VOMIT_WARNING = register("blood_vomit_warning");
    public static final RegistryObject<SoundEvent> VOMIT = register("vomit");
    public static final RegistryObject<SoundEvent> FLATLINE = register("flatline");

    public static final RegistryObject<SoundEvent> SCRAP_PILE_HIT = register("scrap_pile_hit");
    public static final RegistryObject<SoundEvent> SCRAP_PILE_STEP = register("scrap_pile_step");
    public static final RegistryObject<SoundEvent> TRASH_PILE_HIT = register("trash_pile_hit");
    public static final RegistryObject<SoundEvent> STEEL_HIT = register("steel_hit");
    public static final RegistryObject<SoundEvent> STEEL_STEP = register("steel_step");
    public static final RegistryObject<SoundEvent> RUBBER_HIT = register("rubber_hit");
    public static final RegistryObject<SoundEvent> RUBBER_STEP = register("rubber_step");
    public static final RegistryObject<SoundEvent> PLASTIC_STEP = register("plastic_step");
    public static final RegistryObject<SoundEvent> ROCK_HIT = register("rock_hit");
    public static final RegistryObject<SoundEvent> ROCK_STEP = register("rock_step");
    public static final RegistryObject<SoundEvent> CRYSTAL_HIT = register("crystal_hit");
    public static final RegistryObject<SoundEvent> CONCRETE_STEP = register("concrete_step");

    public static final RegistryObject<SoundEvent> BANDAGE_USE = register("bandage");
    public static final RegistryObject<SoundEvent> SYRINGE_USE = register("syringe");
    public static final RegistryObject<SoundEvent> SYRINGE_LOOP = register("syringe_loop");
    public static final RegistryObject<SoundEvent> RINGING = register("ring");
    public static final RegistryObject<SoundEvent> PILLS = register("pills");
    public static final RegistryObject<SoundEvent> PAINDRONE = register("pain_drone");
    public static final RegistryObject<SoundEvent> BROKEN_BONE = register("bone_break");
    public static final RegistryObject<SoundEvent> AMPUTATION = register("amputation");
    public static final RegistryObject<SoundEvent> LAST_STAND = register("laststand");

    private static RegistryObject<SoundEvent> register(String name) {
        ResourceLocation id = CasualtiesCubed.resourceLoc(name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus bus) {
        SOUND_EVENTS.register(bus);
    }
}
