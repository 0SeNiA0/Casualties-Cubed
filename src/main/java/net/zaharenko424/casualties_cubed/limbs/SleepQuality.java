package net.zaharenko424.casualties_cubed.limbs;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.zaharenko424.casualties_cubed.CasualtiesCubedTags;

public enum SleepQuality {
    BAD(Component.translatable("gui.casualties_cubed.sleep_quality.bad").withStyle(ChatFormatting.RED), 0.7f),
    MEDIOCRE(Component.translatable("gui.casualties_cubed.sleep_quality.mediocre").withStyle(ChatFormatting.GOLD), 0.85f),
    OKAY(Component.translatable("gui.casualties_cubed.sleep_quality.okay").withStyle(ChatFormatting.GRAY), 1),
    GOOD(Component.translatable("gui.casualties_cubed.sleep_quality.good").withStyle(ChatFormatting.GREEN), 1.25f);

    public final Component comp;
    public final float regen;

    SleepQuality(Component comp, float regen) {
        this.comp = comp;
        this.regen = regen;
    }

    public static SleepQuality currentSleepQuality(Player player) {
        BlockState state = player.getSleepingPos().map(pos -> player.level().getBlockState(pos)).orElse(null);
        if (state != null && state.is(BlockTags.BEDS)) {
            return SleepQuality.GOOD;
        }

        state = player.getBlockStateOn();
        if (state.getCollisionShape(player.level(), player.getOnPos()).isEmpty()) state = player.level().getBlockState(player.getOnPos().below());

        if (state.is(CasualtiesCubedTags.Block.SLEEP_GOOD)) return GOOD;
        if (state.is(CasualtiesCubedTags.Block.SLEEP_OKAY)) return OKAY;

        return state.is(CasualtiesCubedTags.Block.SLEEP_MEDIOCRE) ? MEDIOCRE : BAD;
    }
}
