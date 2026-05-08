package net.adinvas.casualties_cubed.datagen;

import net.adinvas.casualties_cubed.CasualtiesCubed;
import net.adinvas.casualties_cubed.blocks.GlowFruitBushBlock;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import static net.adinvas.casualties_cubed.registry.ModBlocks.*;

public class BlockStateProvider extends net.minecraftforge.client.model.generators.BlockStateProvider {

    public BlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, CasualtiesCubed.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockWithItem(BROWN_CAP.get(), models().cross(BROWN_CAP.getId().toString(), blockTexture(BROWN_CAP.get())).renderType("cutout"));
        simpleBlockWithItem(MEDICAL_MIXER.get(), models().getExistingFile(MEDICAL_MIXER.getId()));
        horizontalBlockWithItem(SCAV_BLOCK);
        glowFruit();
    }

    private static ResourceLocation blockLoc(ResourceLocation loc) {
        return ResourceLocation.fromNamespaceAndPath(loc.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + loc.getPath());
    }

    private void glowFruit() {
        getVariantBuilder(GLOW_FRUIT_BUSH.get()).forAllStates(state ->
                ConfiguredModel.builder().modelFile(models()
                        .cross(GLOW_FRUIT_BUSH.getId().toString(), CasualtiesCubed.resourceLoc(ModelProvider.BLOCK_FOLDER + "/glow_plant/glow_plant" + state.getValue(GlowFruitBushBlock.AGE))).renderType("cutout")).build());
    }

    private void horizontalBlockWithItem(RegistryObject<? extends HorizontalDirectionalBlock> block) {
        ResourceLocation loc = blockLoc(block.getId());
        Block bl = block.get();
        ModelFile file = models().getExistingFile(loc);

        getVariantBuilder(bl).forAllStates(state ->
                        ConfiguredModel.builder().modelFile(file)
                                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                                .build()
                );
        simpleBlockItem(bl, file);
    }
}
