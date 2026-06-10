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

public class BleedMoodle extends AbstractMoodleVisual {

    private static final ResourceLocation TEX = CasualtiesCubed.resourceLoc("textures/gui/moodles/blood_moodle.png");

    @Override
    protected @NotNull MoodleStatus calculateStatus(Player player, PlayerHealthData data) {
        float bleed = data.getCombinedBleed();

        if (bleed > 0.6f / 20 / 60) {
            return MoodleStatus.CRITICAL;
        } else if (bleed > 0.3f / 20 / 60) {
            return MoodleStatus.HEAVY;
        } else if (bleed > 0.15f / 20 / 60) {
            return MoodleStatus.NORMAL;
        } else if (bleed > 0.05f / 20 / 60) {
            return MoodleStatus.LIGHT;
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
        switch (getMoodleStatus()){
            case LIGHT -> {
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.bleeding.title1"));
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.bleeding.description1").withStyle(ChatFormatting.GRAY));
            }
            case NORMAL -> {
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.bleeding.title2").withStyle(ChatFormatting.YELLOW));
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.bleeding.description2").withStyle(ChatFormatting.GRAY));
            }
            case HEAVY -> {
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.bleeding.title3").withStyle(ChatFormatting.GOLD));
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.bleeding.description3").withStyle(ChatFormatting.GRAY));
            }
            case CRITICAL -> {
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.bleeding.title4").withStyle(ChatFormatting.RED));
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.bleeding.description4").withStyle(ChatFormatting.GRAY));
            }
        }
        return componentList;
    }

}
