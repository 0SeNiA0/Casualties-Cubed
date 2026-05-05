package net.adinvas.prototype_pain;

import com.mojang.logging.LogUtils;
import net.adinvas.prototype_pain.compat.prototype_physics.PhysicsEvents;
import net.adinvas.prototype_pain.config.ClientConfig;
import net.adinvas.prototype_pain.config.ServerConfig;
import net.adinvas.prototype_pain.fluid_system.ModFluids;
import net.adinvas.prototype_pain.loot.ModLootModifier;
import net.adinvas.prototype_pain.registry.*;
import net.adinvas.prototype_pain.visual.particles.ModParticles;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(PrototypePain.MOD_ID)
public class PrototypePain {

    public static final String MOD_ID = "prototype_pain";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation resourceLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public PrototypePain(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        ModFluids.register(modEventBus);
        ModMedicalFluids.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModMenus.MENUS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModLootModifier.register(modEventBus);
        ModCreativeTab.CREATIVE_TABS.register(modEventBus);
        ModSounds.register(modEventBus);
        ModParticles.register(modEventBus);

        context.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
        context.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);

        if (ModList.get().isLoaded("prototype_physics")) {
            MinecraftForge.EVENT_BUS.register(new PhysicsEvents());
            LOGGER.info("PrototypePhysics detected — registered compatibility listeners");
        }
    }

    //TODO Diffrent Crafting Stations
    //TODO something for onyx

}
