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

    @Override
    public boolean shouldBeDisplayed(ChipState state) {
        return state.isActive();
    }

    @Override
    protected @NotNull MoodleStatus calculateStatus(Player player, PlayerHealthData data) {
        float ox = data.getBloodOxygen();

        if (ox < 45) return MoodleStatus.CRITICAL_NEG;
        if (ox < 60) return MoodleStatus.HEAVY_NEG;
        if (ox < 75) return MoodleStatus.NORMAL_NEG;
        return ox < 90 ? MoodleStatus.LIGHT_NEG : MoodleStatus.NONE;
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
