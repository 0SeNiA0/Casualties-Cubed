package net.zaharenko424.casualties_cubed.client.moodles;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TemperatureMoodle extends AbstractMoodleVisual {

    private static final ResourceLocation TEX_HIGH = CasualtiesCubed.resourceLoc("textures/gui/moodles/temphigh.png");
    private static final ResourceLocation TEX_LOW = CasualtiesCubed.resourceLoc("textures/gui/moodles/templow.png");

    boolean low = false;

    @Override
    protected @NotNull MoodleStatus calculateStatus(Player player, PlayerHealthData data) {
        float temp = data.getTemperature();
        low = temp < 36.6;

        if (temp <= 28 || temp >= 41.5) {
            return MoodleStatus.CRITICAL_NEG;
        } else if (temp <= 32.5 || temp >= 40.25) {
            return MoodleStatus.HEAVY_NEG;
        } else if (temp <= 34 || temp >= 39) {
            return MoodleStatus.NORMAL_NEG;
        } else if (temp <= 35.5 || temp >= 38) {
            return MoodleStatus.LIGHT_NEG;
        }

        return MoodleStatus.NONE;
    }

    @Override
    protected void renderIcon(GuiGraphics ms, float partialTicks, int x, int y) {
        ms.blit(low ? TEX_LOW : TEX_HIGH, x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        List<Component> componentList = new ArrayList<>();
        switch (getMoodleStatus()) {
            case LIGHT_NEG -> {
                if (low) {
                    componentList.add(Component.translatable("casualties_cubed.gui.moodle.low_temp.title1"));
                    componentList.add(Component.translatable("casualties_cubed.gui.moodle.low_temp.description1").withStyle(ChatFormatting.GRAY));
                } else {
                    componentList.add(Component.translatable("casualties_cubed.gui.moodle.high_temp.title1"));
                    componentList.add(Component.translatable("casualties_cubed.gui.moodle.high_temp.description1").withStyle(ChatFormatting.GRAY));
                }
            }
            case NORMAL_NEG -> {
                if (low) {
                    componentList.add(Component.translatable("casualties_cubed.gui.moodle.low_temp.title2").withStyle(ChatFormatting.YELLOW));
                    componentList.add(Component.translatable("casualties_cubed.gui.moodle.low_temp.description2").withStyle(ChatFormatting.GRAY));
                } else {
                    componentList.add(Component.translatable("casualties_cubed.gui.moodle.high_temp.title2").withStyle(ChatFormatting.YELLOW));
                    componentList.add(Component.translatable("casualties_cubed.gui.moodle.high_temp.description2").withStyle(ChatFormatting.GRAY));
                }
            }
            case HEAVY_NEG -> {
                if (low) {
                    componentList.add(Component.translatable("casualties_cubed.gui.moodle.low_temp.title3").withStyle(ChatFormatting.GOLD));
                    componentList.add(Component.translatable("casualties_cubed.gui.moodle.low_temp.description3").withStyle(ChatFormatting.GRAY));
                } else {
                    componentList.add(Component.translatable("casualties_cubed.gui.moodle.high_temp.title3").withStyle(ChatFormatting.GOLD));
                    componentList.add(Component.translatable("casualties_cubed.gui.moodle.high_temp.description3").withStyle(ChatFormatting.GRAY));
                }
            }
            case CRITICAL_NEG -> {
                if (low) {
                    componentList.add(Component.translatable("casualties_cubed.gui.moodle.low_temp.title4").withStyle(ChatFormatting.RED));
                    componentList.add(Component.translatable("casualties_cubed.gui.moodle.low_temp.description4").withStyle(ChatFormatting.GRAY));
                } else {
                    componentList.add(Component.translatable("casualties_cubed.gui.moodle.high_temp.title4").withStyle(ChatFormatting.RED));
                    componentList.add(Component.translatable("casualties_cubed.gui.moodle.high_temp.description4").withStyle(ChatFormatting.GRAY));
                }
            }
        }
        return componentList;
    }
}
