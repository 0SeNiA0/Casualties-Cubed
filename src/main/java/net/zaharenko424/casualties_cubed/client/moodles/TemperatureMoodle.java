package net.zaharenko424.casualties_cubed.client.moodles;

import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class TemperatureMoodle extends AbstractMoodleVisual {

    private static final ResourceLocation TEX_HIGH = CasualtiesCubed.resourceLoc("textures/gui/moodles/temphigh.png");
    private static final ResourceLocation TEX_LOW = CasualtiesCubed.resourceLoc("textures/gui/moodles/templow.png");

    boolean low = false;

    @Override
    public MoodleStatus calculateStatus(Player player) {
        float temp = player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(PlayerHealthData::getTemperature).orElse(36.6f);
        if (temp < 36.6) {
            low = true;
        } else {
            low = false;
        }
        if (temp <= 28 || temp >= 41.5) {
            return MoodleStatus.CRITICAL;
        } else if (temp <= 32.5 || temp >= 40.25) {
            return MoodleStatus.HEAVY;
        } else if (temp <= 34 || temp >= 39) {
            return MoodleStatus.NORMAL;
        } else if (temp <= 35.5 || temp >= 38) {
            return MoodleStatus.LIGHT;
        } else {
            return MoodleStatus.NONE;
        }
    }

    @Override
    public void renderIcon(GuiGraphics ms, float partialTicks, int x, int y) {
        ms.blit(low ? TEX_LOW : TEX_HIGH, x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        List<Component> componentList = new ArrayList<>();
        switch (getMoodleStatus()) {
            case LIGHT -> {
                if (low) {
                    componentList.add(Component.translatable("casualties_cubed.gui.moodle.low_temp.title1"));
                    componentList.add(Component.translatable("casualties_cubed.gui.moodle.low_temp.description1").withStyle(ChatFormatting.GRAY));
                } else {
                    componentList.add(Component.translatable("casualties_cubed.gui.moodle.high_temp.title1"));
                    componentList.add(Component.translatable("casualties_cubed.gui.moodle.high_temp.description1").withStyle(ChatFormatting.GRAY));
                }
            }
            case NORMAL -> {
                if (low) {
                    componentList.add(Component.translatable("casualties_cubed.gui.moodle.low_temp.title2").withStyle(ChatFormatting.YELLOW));
                    componentList.add(Component.translatable("casualties_cubed.gui.moodle.low_temp.description2").withStyle(ChatFormatting.GRAY));
                } else {
                    componentList.add(Component.translatable("casualties_cubed.gui.moodle.high_temp.title2").withStyle(ChatFormatting.YELLOW));
                    componentList.add(Component.translatable("casualties_cubed.gui.moodle.high_temp.description2").withStyle(ChatFormatting.GRAY));
                }
            }
            case HEAVY -> {
                if (low) {
                    componentList.add(Component.translatable("casualties_cubed.gui.moodle.low_temp.title3").withStyle(ChatFormatting.GOLD));
                    componentList.add(Component.translatable("casualties_cubed.gui.moodle.low_temp.description3").withStyle(ChatFormatting.GRAY));
                } else {
                    componentList.add(Component.translatable("casualties_cubed.gui.moodle.high_temp.title3").withStyle(ChatFormatting.GOLD));
                    componentList.add(Component.translatable("casualties_cubed.gui.moodle.high_temp.description3").withStyle(ChatFormatting.GRAY));
                }
            }
            case CRITICAL -> {
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
