package net.adinvas.casualties_cubed.blocks;

import net.adinvas.casualties_cubed.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BrownCapBlock extends BushBlock {

    protected static final VoxelShape SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 6.0, 11.0);

    public BrownCapBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter pLevel, BlockPos pPos) {
        return state.is(Blocks.DIRT) || state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.STONE)||state.is(Blocks.DEEPSLATE)
                ||state.is(Blocks.ANDESITE)
                ||state.is(Blocks.DIORITE)
                ||state.is(Blocks.TUFF)
                ||state.is(Blocks.GRANITE)
                ||state.is(Blocks.COBBLESTONE)
                ||state.is(Blocks.COBBLED_DEEPSLATE);
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        int artificialLight = pLevel.getBrightness(LightLayer.BLOCK,pPos);

        if (artificialLight > 10 && pRandom.nextInt(4) == 0){
            Direction dir = Direction.Plane.HORIZONTAL.getRandomDirection(pRandom);
            BlockPos targetPos = pPos.relative(dir);
            BlockState targetState = pLevel.getBlockState(targetPos);
            if (targetState.isAir()&&!pLevel.getBlockState(targetPos.relative(Direction.DOWN)).isAir()) {
                pLevel.setBlock(targetPos, ModBlocks.BROWN_CAP.get().defaultBlockState(), Block.UPDATE_ALL);
            }
        }
    }
}
