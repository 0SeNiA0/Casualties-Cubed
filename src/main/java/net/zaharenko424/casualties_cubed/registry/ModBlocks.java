package net.zaharenko424.casualties_cubed.registry;

import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.blocks.BrownCapBlock;
import net.zaharenko424.casualties_cubed.blocks.GlowFruitBushBlock;
import net.zaharenko424.casualties_cubed.blocks.ScavBlock;
import net.zaharenko424.casualties_cubed.blocks.medical_mixer.MedicalMixerBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CasualtiesCubed.MOD_ID);

    public static final RegistryObject<GlowFruitBushBlock> GLOW_FRUIT_BUSH = BLOCKS.register("glow_fruit_bush",() -> new GlowFruitBushBlock(BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH)
            .randomTicks()
            .instabreak()
            .noCollission()));

    public static final RegistryObject<BrownCapBlock> BROWN_CAP = BLOCKS.register("brown_cap", ()-> new BrownCapBlock(BlockBehaviour.Properties.copy(Blocks.BROWN_MUSHROOM).randomTicks()));

    public static final RegistryObject<ScavBlock> SCAV_BLOCK = BLOCKS.register("scav_plushie",()-> new ScavBlock(
            BlockBehaviour.Properties.of().instabreak().noOcclusion().noCollission()
    ));

    public static final RegistryObject<MedicalMixerBlock> MEDICAL_MIXER = BLOCKS.register("medical_mixer",()->new MedicalMixerBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
}
