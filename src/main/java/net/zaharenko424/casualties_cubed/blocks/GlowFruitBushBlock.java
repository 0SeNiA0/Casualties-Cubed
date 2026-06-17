package net.zaharenko424.casualties_cubed.blocks;

import net.minecraft.world.level.block.*;
import net.zaharenko424.casualties_cubed.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GlowFruitBushBlock extends BushBlock implements BonemealableBlock {

    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    private static final VoxelShape SHAPE = Shapes.box(2/16f, 0, 2/16f, 14/16f, 14/16f, 14/16f);

    public GlowFruitBushBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(Blocks.DIRT) || state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.STONE)||state.is(Blocks.DEEPSLATE)
                ||state.is(Blocks.ANDESITE)
                ||state.is(Blocks.DIORITE)
                ||state.is(Blocks.TUFF)
                ||state.is(Blocks.GRANITE)
                ||state.is(Blocks.COBBLESTONE)
                ||state.is(Blocks.COBBLED_DEEPSLATE);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getValue(AGE) == 3 ? 10 : 0; // glow when mature
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource rand) {
        int age = state.getValue(AGE);
        int light = state.getLightBlock(world,pos);
        int Chances = (int) Math.max(4,light/1.5f);
        if (age < 3 && rand.nextInt(Chances) == 0) {
            world.setBlock(pos, state.setValue(AGE, age + 1), 2);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state, boolean isClient) {
        return state.getValue(AGE) < 3;
    }

    @Override
    public boolean isBonemealSuccess(Level world, RandomSource rand, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource rand, BlockPos pos, BlockState state) {
        int age = state.getValue(AGE);
        world.setBlock(pos, state.setValue(AGE, Math.min(3, age + 1)), 2);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {
        int age = state.getValue(AGE);
        if (age == 3) {
            if (!world.isClientSide) {
                popResource(world, pos, new ItemStack(ModItems.GLOW_FRUIT.get()));
                world.setBlock(pos, state.setValue(AGE, 1), 2);
            }
            return InteractionResult.sidedSuccess(world.isClientSide);
        }

        return InteractionResult.PASS;
    }
}
