package net.zaharenko424.casualties_cubed.client.moodles;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SepsisMoodle extends AbstractMoodleVisual {

    private static final ResourceLocation TEX = CasualtiesCubed.resourceLoc("textures/gui/moodles/sepsis.png");

    @Override
    protected @NotNull MoodleStatus calculateStatus(Player player, PlayerHealthData data) {
        return MoodleStatus.NONE;
    }

    @Override
    protected void renderIcon(GuiGraphics ms, float partialTicks, int x, int y) {
        ms.blit(TEX, x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        return switch (getMoodleStatus()) {
            case NORMAL_NEG -> List.of(
                    Component.translatable("gui.casualties_cubed.moodle.sepsis.title2").withStyle(ChatFormatting.YELLOW),
                    Component.translatable("gui.casualties_cubed.moodle.sepsis.description2").withStyle(ChatFormatting.GRAY));
            case HEAVY_NEG -> List.of(
                    Component.translatable("gui.casualties_cubed.moodle.sepsis.title3").withStyle(ChatFormatting.GOLD),
                    Component.translatable("gui.casualties_cubed.moodle.sepsis.description3").withStyle(ChatFormatting.GRAY));
            case CRITICAL_NEG -> List.of(
                    Component.translatable("gui.casualties_cubed.moodle.sepsis.title4").withStyle(ChatFormatting.RED),
                    Component.translatable("gui.casualties_cubed.moodle.sepsis.description4").withStyle(ChatFormatting.GRAY));
            default -> super.getTooltip(player);
        };
    }
}
