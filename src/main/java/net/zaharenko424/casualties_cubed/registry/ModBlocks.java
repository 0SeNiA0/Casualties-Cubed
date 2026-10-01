package net.zaharenko424.casualties_cubed.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.blocks.*;
import net.zaharenko424.casualties_cubed.blocks.medical_mixer.MedicalMixerBlock;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CasualtiesCubed.MOD_ID);

    public static final RegistryObject<GlowFruitBushBlock> GLOW_FRUIT_BUSH = BLOCKS.register("glow_fruit_bush", () -> new GlowFruitBushBlock(BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH)
            .randomTicks()
            .instabreak()
            .noCollission()));

    public static final RegistryObject<BrownCapBlock> BROWN_CAP = BLOCKS.register("brown_cap", () -> new BrownCapBlock(BlockBehaviour.Properties.copy(Blocks.BROWN_MUSHROOM).randomTicks()));

    public static final RegistryObject<ExpiePlushyBlock> EXPIE_PLUSHY = BLOCKS.register("expie_plushy", () -> new ExpiePlushyBlock(
            BlockBehaviour.Properties.of().instabreak().noOcclusion().noCollission()
    ));

    public static final RegistryObject<Block> SCRAP_PILE = BLOCKS.register("scrap_pile", () -> new CUBlock(BlockBehaviour.Properties.copy(Blocks.GRAVEL).sound(new ForgeSoundType(0.5f, 1, ModSounds.SCRAP_PILE_HIT, ModSounds.SCRAP_PILE_STEP, ModSounds.SCRAP_PILE_HIT, ModSounds.SCRAP_PILE_HIT, ModSounds.SCRAP_PILE_STEP))));
    public static final RegistryObject<Block> TRASH_PILE = BLOCKS.register("trash_pile", () -> new CUBlock(BlockBehaviour.Properties.copy(Blocks.GRAVEL).sound(new ForgeSoundType(0.5f, 1, ModSounds.TRASH_PILE_HIT, ModSounds.SCRAP_PILE_STEP, ModSounds.TRASH_PILE_HIT, ModSounds.TRASH_PILE_HIT, ModSounds.SCRAP_PILE_STEP))));
    private static final ForgeSoundType STEEL = new ForgeSoundType(0.5f, 1, ModSounds.STEEL_HIT, ModSounds.STEEL_STEP, ModSounds.STEEL_HIT, ModSounds.STEEL_HIT, ModSounds.STEEL_STEP);
    public static final RegistryObject<Block> STEEL_TILE = BLOCKS.register("steel_tile", () -> new CUBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).strength(5.0F, 6.0F).sound(STEEL)));
    public static final RegistryObject<Block> RUBBER = BLOCKS.register("rubber", () -> new CUBlock(BlockBehaviour.Properties.copy(Blocks.GRAVEL).sound(new ForgeSoundType(0.5f, 1, ModSounds.RUBBER_HIT, ModSounds.RUBBER_STEP, ModSounds.RUBBER_STEP, ModSounds.RUBBER_HIT, ModSounds.RUBBER_STEP))));
    public static final RegistryObject<Block> PLASTIC = BLOCKS.register("plastic", () -> new CUBlock(BlockBehaviour.Properties.copy(Blocks.GRAVEL).sound(new ForgeSoundType(0.5f, 1, ModSounds.RUBBER_HIT, ModSounds.PLASTIC_STEP, ModSounds.PLASTIC_STEP, ModSounds.RUBBER_HIT, ModSounds.PLASTIC_STEP))));
    public static final RegistryObject<Block> HEAT_RESISTANT_ALLOY = BLOCKS.register("heat_resistant_alloy", () -> new CUBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).strength(15.0F, 18.0F).sound(STEEL)));
    private static final ForgeSoundType ROCK = new ForgeSoundType(0.5f, 1, ModSounds.ROCK_HIT, ModSounds.CONCRETE_STEP, ModSounds.ROCK_HIT, ModSounds.ROCK_HIT, ModSounds.CONCRETE_STEP);
    public static final RegistryObject<Block> MARBLE = BLOCKS.register("marble", () -> new CUBlock(BlockBehaviour.Properties.copy(Blocks.STONE).sound(ROCK)));
    public static final RegistryObject<Block> LIMESTONE = BLOCKS.register("limestone", () -> new CUBlock(BlockBehaviour.Properties.copy(Blocks.STONE).sound(ROCK)));
    //public static final RegistryObject<Block> SCAFFOLDING = BLOCKS.register("scaffolding", () -> new Block(BlockBehaviour.Properties.copy(Blocks.SCAFFOLDING)));
    public static final RegistryObject<ToxirockBlock> TOXIROCK = BLOCKS.register("toxirock", () -> new ToxirockBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE).sound(ROCK)));
    public static final RegistryObject<Block> COPPER = BLOCKS.register("copper", () -> new CUBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 3.0F).sound(new ForgeSoundType(0.5f, 1, ModSounds.CRYSTAL_HIT, ModSounds.ROCK_STEP, ModSounds.CRYSTAL_HIT, ModSounds.CRYSTAL_HIT, ModSounds.ROCK_STEP))));
    public static final RegistryObject<Block> ILMENITE = BLOCKS.register("ilmenite", () -> new CUBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(6.0F, 3.0F).sound(new ForgeSoundType(0.5f, 1, ModSounds.ROCK_HIT, ModSounds.ROCK_STEP, ModSounds.ROCK_HIT, ModSounds.ROCK_HIT, ModSounds.ROCK_STEP))));

    public static final RegistryObject<MedicalMixerBlock> MEDICAL_MIXER = BLOCKS.register("medical_mixer", () -> new MedicalMixerBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
}
