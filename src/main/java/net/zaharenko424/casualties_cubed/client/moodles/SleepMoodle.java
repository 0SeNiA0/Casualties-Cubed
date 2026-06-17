package net.zaharenko424.casualties_cubed.client.moodles;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SleepMoodle extends AbstractMoodleVisual {

    private static final ResourceLocation TEX = CasualtiesCubed.resourceLoc("textures/gui/moodles/sleep.png");

    @Override
    protected @NotNull MoodleStatus calculateStatus(Player player, PlayerHealthData data) {
        return player.isSleeping() ? MoodleStatus.LIGHT_POS : MoodleStatus.NONE;
    }

    @Override
    protected void renderIcon(GuiGraphics graphics, float partialTicks, int x, int y) {
        graphics.blit(TEX, x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        return List.of(Component.translatable("gui.casualties_cubed.moodle.sleep.title"),
                Component.translatable("gui.casualties_cubed.moodle.sleep.description"));
    }
}
