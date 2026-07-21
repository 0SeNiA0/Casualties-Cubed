package net.zaharenko424.casualties_cubed.client.moodles;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.limbs.ChipState;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OxygenMoodle extends AbstractMoodleVisual {

    private static final ResourceLocation TEX = CasualtiesCubed.resourceLoc("textures/gui/moodles/oxygen_moodle.png");
    private static final ResourceLocation TEX_CRIT = CasualtiesCubed.resourceLoc("textures/gui/moodles/heartstop_moodle.png");;

    @Override
    public boolean shouldBeDisplayed(ChipState state) {
        return state.isActive();
    }

    @Override
    protected @NotNull MoodleStatus calculateStatus(Player player, PlayerHealthData data) {
        float ox = data.getBloodOxygen();

        if (ox < 5) {
            return MoodleStatus.CRITICAL_NEG;
        } else if (ox < 30) {
            return MoodleStatus.HEAVY_NEG;
        } else if (ox < 60) {
            return MoodleStatus.NORMAL_NEG;
        } else if (ox < 90) {
            return MoodleStatus.LIGHT_NEG;
        }

        return MoodleStatus.NONE;
    }

    @Override
    protected void renderIcon(GuiGraphics ms, float partialTicks, int x, int y) {
        ms.blit(getMoodleStatus() == MoodleStatus.CRITICAL_NEG ? TEX_CRIT : TEX, x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        List<Component> componentList = new ArrayList<>();
        switch (getMoodleStatus()) {
            case LIGHT_NEG -> {
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.oxygen.title1"));
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.oxygen.description1").withStyle(ChatFormatting.GRAY));
            }
            case NORMAL_NEG -> {
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.oxygen.title2").withStyle(ChatFormatting.YELLOW));
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.oxygen.description2").withStyle(ChatFormatting.GRAY));
            }
            case HEAVY_NEG -> {
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.oxygen.title3").withStyle(ChatFormatting.GOLD));
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.oxygen.description3").withStyle(ChatFormatting.GRAY));
            }
            case CRITICAL_NEG -> {
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.oxygen.title4").withStyle(ChatFormatting.RED));
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.oxygen.description4").withStyle(ChatFormatting.GRAY));
            }
        }
        return componentList;
    }
}
