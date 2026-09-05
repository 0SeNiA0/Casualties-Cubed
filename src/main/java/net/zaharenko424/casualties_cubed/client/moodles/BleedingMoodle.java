package net.zaharenko424.casualties_cubed.client.moodles;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.config.ServerConfig;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;

import java.util.ArrayList;
import java.util.List;

public class BleedingMoodle extends AbstractMoodle {

    private static final ResourceLocation[] RED = {
            CasualtiesCubed.resourceLoc("textures/gui/moodles/bleeding1_red.png"),
            CasualtiesCubed.resourceLoc("textures/gui/moodles/bleeding2_red.png"),
            CasualtiesCubed.resourceLoc("textures/gui/moodles/bleeding3_red.png"),
            CasualtiesCubed.resourceLoc("textures/gui/moodles/bleeding4_red.png")
    };
    private static final ResourceLocation[] YELLOW = {
            CasualtiesCubed.resourceLoc("textures/gui/moodles/bleeding1_yellow.png"),
            CasualtiesCubed.resourceLoc("textures/gui/moodles/bleeding2_yellow.png"),
            CasualtiesCubed.resourceLoc("textures/gui/moodles/bleeding3_yellow.png"),
            CasualtiesCubed.resourceLoc("textures/gui/moodles/bleeding4_yellow.png")
    };

    @Override
    public void update(Player player, PlayerHealthData data) {
        float bleed = data.totalBleedSpeed();

        if (bleed > 0.6f / 20 / 60) {
            setStatus(MoodleStatus.CRITICAL_NEG, true);
        } else if (bleed > 0.3f / 20 / 60) {
            setStatus(MoodleStatus.HEAVY_NEG, true);
        } else if (bleed > 0.15f / 20 / 60) {
            setStatus(MoodleStatus.NORMAL_NEG);
        } else if (bleed > 0.05f / 20 / 60) {
            setStatus(MoodleStatus.LIGHT_NEG);
        } else clearStatus();
    }

    @Override
    protected void renderIcon(GuiGraphics ms, float partialTicks, int x, int y) {
        ms.blit((ServerConfig.EXPIE_MODE.get() ? YELLOW : RED)[switch (getMoodleStatus()) {
            case LIGHT_NEG -> 0;
            case NORMAL_NEG -> 1;
            case HEAVY_NEG -> 2;
            default -> 3;
        }], x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        List<Component> componentList = new ArrayList<>();
        switch (getMoodleStatus()){
            case LIGHT_NEG -> {
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.bleeding.title1"));
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.bleeding.description1").withStyle(ChatFormatting.GRAY));
            }
            case NORMAL_NEG -> {
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.bleeding.title2").withStyle(ChatFormatting.YELLOW));
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.bleeding.description2").withStyle(ChatFormatting.GRAY));
            }
            case HEAVY_NEG -> {
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.bleeding.title3").withStyle(ChatFormatting.GOLD));
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.bleeding.description3").withStyle(ChatFormatting.GRAY));
            }
            case CRITICAL_NEG -> {
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.bleeding.title4").withStyle(ChatFormatting.RED));
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.bleeding.description4").withStyle(ChatFormatting.GRAY));
            }
        }
        return componentList;
    }
}
