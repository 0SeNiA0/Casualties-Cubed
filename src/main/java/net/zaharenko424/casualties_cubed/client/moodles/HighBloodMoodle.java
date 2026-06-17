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

public class HighBloodMoodle extends AbstractMoodleVisual {

    private static final ResourceLocation TEX = CasualtiesCubed.resourceLoc("textures/gui/moodles/highblood_moodle.png");

    @Override
    protected @NotNull MoodleStatus calculateStatus(Player player, PlayerHealthData data) {
        float blood = data.getBloodVolume();

        if (blood > 5.625) {
            return MoodleStatus.CRITICAL_NEG;
        } else if (blood > 5.5) {
            return MoodleStatus.HEAVY_NEG;
        } else if (blood > 5.25) {
            return MoodleStatus.NORMAL_NEG;
        } else if (blood > 5) {
            return MoodleStatus.LIGHT_NEG;
        }

        return MoodleStatus.NONE;
    }

    @Override
    protected void renderIcon(GuiGraphics ms, float partialTicks, int x, int y) {
        ms.blit(TEX, x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        List<Component> componentList = new ArrayList<>();
        switch (getMoodleStatus()) {
            case LIGHT_NEG -> {
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.high_blood.title1"));
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.high_blood.description1").withStyle(ChatFormatting.GRAY));
            }
            case NORMAL_NEG -> {
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.high_blood.title2").withStyle(ChatFormatting.YELLOW));
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.high_blood.description2").withStyle(ChatFormatting.GRAY));
            }
            case HEAVY_NEG -> {
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.high_blood.title3").withStyle(ChatFormatting.GOLD));
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.high_blood.description3").withStyle(ChatFormatting.GRAY));
            }
            case CRITICAL_NEG -> {
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.high_blood.title4").withStyle(ChatFormatting.RED));
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.high_blood.description4").withStyle(ChatFormatting.GRAY));
            }
        }
        return componentList;
    }
}
