package net.zaharenko424.casualties_cubed.client.moodles;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;

import java.util.List;

public class ThirstMoodle extends AbstractMoodle {

    private static final ResourceLocation TEX_LOW = CasualtiesCubed.resourceLoc("textures/gui/moodles/thirst_low.png");
    private static final ResourceLocation TEX_HIGH = CasualtiesCubed.resourceLoc("textures/gui/moodles/thirst_high.png");

    private boolean low = false;

    @Override
    public void update(Player player, PlayerHealthData data) {
        float thirst = data.thirst();
        low = thirst < 100;

        if (thirst <= 20) {
            setStatus(MoodleStatus.CRITICAL_NEG, thirst <= 0);
        } else if (thirst < 35) {
            setStatus(MoodleStatus.HEAVY_NEG);
        } else if (thirst < 55) {
            setStatus(MoodleStatus.NORMAL_NEG);
        } else if (thirst < 75) {
            setStatus(MoodleStatus.LIGHT_NEG);
        } else if (thirst > 175) {
            setStatus(MoodleStatus.CRITICAL_NEG, true);
        } else if (thirst > 125) {
            setStatus(MoodleStatus.NORMAL_NEG);
        } else if (thirst > 100) {
            setStatus(MoodleStatus.LIGHT_NEG);
        } else clearStatus();
    }

    @Override
    protected void renderIcon(GuiGraphics graphics, float partialTicks, int x, int y) {
        graphics.blit(low ? TEX_LOW : TEX_HIGH, x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        return switch (getMoodleStatus()) {
            case LIGHT_NEG ->
                    low ? List.of(Component.translatable("gui.casualties_cubed.moodle.thirst_low.title1"),
                            Component.translatable("gui.casualties_cubed.moodle.thirst_low.description1"))
                    :     List.of(Component.translatable("gui.casualties_cubed.moodle.thirst_high.title1"),
                            Component.translatable("gui.casualties_cubed.moodle.thirst_high.description1"));
            case NORMAL_NEG ->
                    low ? List.of(Component.translatable("gui.casualties_cubed.moodle.thirst_low.title2"),
                            Component.translatable("gui.casualties_cubed.moodle.thirst_low.description2"))
                    :     List.of(Component.translatable("gui.casualties_cubed.moodle.thirst_high.title2"),
                            Component.translatable("gui.casualties_cubed.moodle.thirst_high.description2"));
            case HEAVY_NEG ->
                    List.of(Component.translatable("gui.casualties_cubed.moodle.thirst_low.title3"),
                            Component.translatable("gui.casualties_cubed.moodle.thirst_low.description3"));
            case CRITICAL_NEG ->
                    low ? List.of(Component.translatable("gui.casualties_cubed.moodle.thirst_low.title4"),
                            Component.translatable("gui.casualties_cubed.moodle.thirst_low.description4"))
                    :     List.of(Component.translatable("gui.casualties_cubed.moodle.thirst_high.title3"),
                            Component.translatable("gui.casualties_cubed.moodle.thirst_high.description3"));
            default -> List.of();
        };
    }
}
