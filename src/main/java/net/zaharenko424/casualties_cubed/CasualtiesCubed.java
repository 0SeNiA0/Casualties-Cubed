package net.zaharenko424.casualties_cubed;

import com.mojang.logging.LogUtils;
import net.zaharenko424.casualties_cubed.compat.prototype_physics.PhysicsEvents;
import net.zaharenko424.casualties_cubed.config.ClientConfig;
import net.zaharenko424.casualties_cubed.config.ServerConfig;
import net.zaharenko424.casualties_cubed.registry.ModFluids;
import net.zaharenko424.casualties_cubed.registry.ModLootModifier;
import net.zaharenko424.casualties_cubed.registry.*;
import net.zaharenko424.casualties_cubed.visual.particles.ModParticles;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(CasualtiesCubed.MOD_ID)
public class CasualtiesCubed {

    public static final String MOD_ID = "casualties_cubed";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation resourceLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public CasualtiesCubed(FMLJavaModLoadingContext context) {
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
