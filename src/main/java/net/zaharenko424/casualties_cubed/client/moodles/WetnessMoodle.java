package net.zaharenko424.casualties_cubed.client.moodles;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;

import java.util.List;

public class WetnessMoodle extends AbstractMoodle {

    private static final ResourceLocation TEX = CasualtiesCubed.resourceLoc("textures/gui/moodles/wetness.png");

    @Override
    public void update(Player player, PlayerHealthData data) {
        float wetness = data.getWetness();

        if (wetness > 70) {
            setStatus(MoodleStatus.CRITICAL_NEG);
        } else if (wetness > 45) {
            setStatus(MoodleStatus.HEAVY_NEG);
        } else if (wetness > 20) {
            setStatus(MoodleStatus.NORMAL_NEG);
        } else if (wetness > 5) {
            setStatus(MoodleStatus.LIGHT_NEG);
        } else clearStatus();
    }

    @Override
    protected void renderIcon(GuiGraphics graphics, float partialTicks, int x, int y) {
        graphics.blit(TEX, x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        return switch (getMoodleStatus()) {
            case LIGHT_NEG -> List.of(
                    Component.translatable("gui.casualties_cubed.moodle.wetness.title1"),
                    Component.translatable("gui.casualties_cubed.moodle.wetness.description1").withStyle(ChatFormatting.GRAY));
            case NORMAL_NEG -> List.of(
                    Component.translatable("gui.casualties_cubed.moodle.wetness.title2").withStyle(ChatFormatting.YELLOW),
                    Component.translatable("gui.casualties_cubed.moodle.wetness.description2").withStyle(ChatFormatting.GRAY));
            case HEAVY_NEG -> List.of(
                    Component.translatable("gui.casualties_cubed.moodle.wetness.title3").withStyle(ChatFormatting.GOLD),
                    Component.translatable("gui.casualties_cubed.moodle.wetness.description3").withStyle(ChatFormatting.GRAY));
            case CRITICAL_NEG -> List.of(
                    Component.translatable("gui.casualties_cubed.moodle.wetness.title4").withStyle(ChatFormatting.RED),
                    Component.translatable("gui.casualties_cubed.moodle.wetness.description4").withStyle(ChatFormatting.GRAY));
            default -> super.getTooltip(player);
        };
    }
}
