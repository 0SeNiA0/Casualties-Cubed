package net.zaharenko424.casualties_cubed.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import net.zaharenko424.casualties_cubed.util.Util;

public class ToxirockBlock extends CUBlock {

    public ToxirockBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        if (!(pEntity instanceof ServerPlayer player)) return;

        PlayerHealthData data = PlayerHealthData.of(player).orElse(null);
        if (data == null) return;

        data.addRadiationSickness(2.5f * Util.TICK_TO_SEC);
    }
}
