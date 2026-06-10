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

public class InfectionMoodle extends AbstractMoodleVisual {

    private static final ResourceLocation TEX = CasualtiesCubed.resourceLoc("textures/gui/moodles/infection_moodle.png");

    @Override
    public boolean shouldBeDisplayed(ChipState state) {
        return state.isActive() || getMoodleStatus() != MoodleStatus.LIGHT;
    }

    @Override
    protected @NotNull MoodleStatus calculateStatus(Player player, PlayerHealthData data) {
        double maxInfection = data.getMaxInfection();

        if (maxInfection > 80) {
            return MoodleStatus.CRITICAL;
        } else if (maxInfection > 60) {
            return MoodleStatus.HEAVY;
        } else if (maxInfection > 40) {
            return MoodleStatus.NORMAL;
        } else if (maxInfection > 25) {
            return MoodleStatus.LIGHT;
        } else {
            return MoodleStatus.NONE;
        }
    }

    @Override
    protected void renderIcon(GuiGraphics ms, float partialTicks, int x, int y) {
        ms.blit(TEX, x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        List<Component> componentList = new ArrayList<>();
        switch (getMoodleStatus()) {
            case LIGHT -> {
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.infection.title1"));
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.infection.description1").withStyle(ChatFormatting.GRAY));
            }
            case NORMAL -> {
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.infection.title2").withStyle(ChatFormatting.YELLOW));
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.infection.description2").withStyle(ChatFormatting.GRAY));
            }
            case HEAVY -> {
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.infection.title3").withStyle(ChatFormatting.GOLD));
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.infection.description3").withStyle(ChatFormatting.GRAY));
            }
            case CRITICAL -> {
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.infection.title4").withStyle(ChatFormatting.RED));
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.infection.description4").withStyle(ChatFormatting.GRAY));
            }
        }
        return componentList;
    }
}
