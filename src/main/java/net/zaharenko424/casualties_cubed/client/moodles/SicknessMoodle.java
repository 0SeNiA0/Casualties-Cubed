package net.zaharenko424.casualties_cubed.client.moodles;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;

import java.util.List;

public class SicknessMoodle extends AbstractMoodle {

    private static final ResourceLocation TEX = CasualtiesCubed.resourceLoc("textures/gui/moodles/sickness.png");

    @Override
    public void update(Player player, PlayerHealthData data) {
        float sickness = data.getSickness();

        if (sickness > 75) {
            setStatus(MoodleStatus.CRITICAL_NEG, sickness > 95);
        } else if (sickness > 50) {
            setStatus(MoodleStatus.HEAVY_NEG);
        } else if (sickness > 30) {
            setStatus(MoodleStatus.NORMAL_NEG);
        } else if (sickness > 10) {
            setStatus(MoodleStatus.LIGHT_NEG);
        } else clearStatus();
    }

    @Override
    protected void renderIcon(GuiGraphics ms, float partialTicks, int x, int y) {
        ms.blit(TEX, x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        return switch (getMoodleStatus()) {
            case LIGHT_NEG -> List.of(
                    Component.translatable("gui.casualties_cubed.moodle.sickness.title1"),
                    Component.translatable("gui.casualties_cubed.moodle.sickness.description1").withStyle(ChatFormatting.GRAY));
            case NORMAL_NEG -> List.of(
                    Component.translatable("gui.casualties_cubed.moodle.sickness.title2").withStyle(ChatFormatting.YELLOW),
                    Component.translatable("gui.casualties_cubed.moodle.sickness.description2").withStyle(ChatFormatting.GRAY));
            case HEAVY_NEG -> List.of(
                    Component.translatable("gui.casualties_cubed.moodle.sickness.title3").withStyle(ChatFormatting.GOLD),
                    Component.translatable("gui.casualties_cubed.moodle.sickness.description3").withStyle(ChatFormatting.GRAY));
            case CRITICAL_NEG -> List.of(
                    Component.translatable("gui.casualties_cubed.moodle.sickness.title4").withStyle(ChatFormatting.RED),
                    Component.translatable("gui.casualties_cubed.moodle.sickness.description4").withStyle(ChatFormatting.GRAY));
            default -> super.getTooltip(player);
        };
    }
}
