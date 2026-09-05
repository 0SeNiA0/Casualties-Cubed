package net.zaharenko424.casualties_cubed.client.moodles;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.config.ServerConfig;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;

import java.util.List;

public class BloodPressureMoodle extends AbstractMoodle {

    private static final ResourceLocation[] RED = {
            CasualtiesCubed.resourceLoc("textures/gui/moodles/hypotension_red.png"),
            CasualtiesCubed.resourceLoc("textures/gui/moodles/hypertension_red.png")
    };
    private static final ResourceLocation[] YELLOW = {
            CasualtiesCubed.resourceLoc("textures/gui/moodles/hypotension_yellow.png"),
            CasualtiesCubed.resourceLoc("textures/gui/moodles/hypertension_yellow.png")
    };

    private boolean low;

    public BloodPressureMoodle() {
        super(false, true);
    }

    @Override
    public void update(Player player, PlayerHealthData data) {
        if (data.isCardiacArrest()) {
            clearStatus();
            return;
        }

        float bloodPressure = data.bloodPressure();
        low = bloodPressure < 110;

        if (bloodPressure < 60 || bloodPressure > 180) {
            setStatus(MoodleStatus.CRITICAL_NEG, true);
        } else if (bloodPressure < 83 || bloodPressure > 162) {
            setStatus(MoodleStatus.HEAVY_NEG, bloodPressure < 70);
        } else if (bloodPressure < 96 || bloodPressure > 145) {
            setStatus(MoodleStatus.NORMAL_NEG);
        } else if (bloodPressure < 110 || bloodPressure > 130) {
            setStatus(MoodleStatus.LIGHT_NEG);
        } else clearStatus();
    }

    @Override
    protected void renderIcon(GuiGraphics ms, float partialTicks, int x, int y) {
        ms.blit((ServerConfig.EXPIE_MODE.get() ? YELLOW : RED)[low ? 0 : 1], x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        return switch (getMoodleStatus()){
            case LIGHT_NEG -> low ? List.of(
                    Component.translatable("gui.casualties_cubed.moodle.hypotension.title1"),
                    Component.translatable("gui.casualties_cubed.moodle.hypotension.description1"))
                                  : List.of(
                    Component.translatable("gui.casualties_cubed.moodle.hypertension.title1"),
                    Component.translatable("gui.casualties_cubed.moodle.hypertension.description1"));
            case NORMAL_NEG -> low ? List.of(
                    Component.translatable("gui.casualties_cubed.moodle.hypotension.title2"),
                    Component.translatable("gui.casualties_cubed.moodle.hypotension.description2"))
                                   : List.of(
                    Component.translatable("gui.casualties_cubed.moodle.hypertension.title2"),
                    Component.translatable("gui.casualties_cubed.moodle.hypertension.description2"));
            case HEAVY_NEG -> low ? List.of(
                    Component.translatable("gui.casualties_cubed.moodle.hypotension.title3").withStyle(ChatFormatting.RED),
                    Component.translatable("gui.casualties_cubed.moodle.hypotension.description3"))
                                  : List.of(
                    Component.translatable("gui.casualties_cubed.moodle.hypertension.title3").withStyle(ChatFormatting.RED),
                    Component.translatable("gui.casualties_cubed.moodle.hypertension.description3"));
            case CRITICAL_NEG -> low ? List.of(
                    Component.translatable("gui.casualties_cubed.moodle.hypotension.title4").withStyle(ChatFormatting.RED),
                    Component.translatable("gui.casualties_cubed.moodle.hypotension.description4"))
                                     : List.of(
                    Component.translatable("gui.casualties_cubed.moodle.hypertension.title4").withStyle(ChatFormatting.RED),
                    Component.translatable("gui.casualties_cubed.moodle.hypertension.description4"));
            default -> List.of();
        };
    }
}
