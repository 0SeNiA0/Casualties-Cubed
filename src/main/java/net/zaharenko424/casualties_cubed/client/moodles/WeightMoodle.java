package net.zaharenko424.casualties_cubed.client.moodles;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;

import java.util.List;

public class WeightMoodle extends AbstractMoodle {

    private static final ResourceLocation TEX_LOW = CasualtiesCubed.resourceLoc("textures/gui/moodles/weight_low.png");
    private static final ResourceLocation TEX_HIGH = CasualtiesCubed.resourceLoc("textures/gui/moodles/weight_high.png");

    private boolean low = false;

    public WeightMoodle() {
        super(true, true);
    }

    @Override
    public boolean shouldBeDisplayed(PlayerHealthData data) {
        return super.shouldBeDisplayed(data) || data.weightOffset() < -40 || data.weightOffset() > 40;
    }

    @Override
    public void update(Player player, PlayerHealthData data) {
        float weightOffset = data.weightOffset();
        low = weightOffset < 0;

        if (weightOffset <= -50 || weightOffset >= 50) {
            setStatus(MoodleStatus.CRITICAL_NEG);
        } else if (weightOffset < -40 || weightOffset > 40) {
            setStatus(MoodleStatus.HEAVY_NEG);
        } else if (weightOffset < -30 || weightOffset > 30) {
            setStatus(MoodleStatus.NORMAL_NEG);
        } else if (weightOffset < -15 || weightOffset > 15) {
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
                    low ? List.of(Component.translatable("gui.casualties_cubed.moodle.weight_low.title1"),
                                  Component.translatable("gui.casualties_cubed.moodle.weight_low.description1"))
                    :     List.of(Component.translatable("gui.casualties_cubed.moodle.weight_high.title1"),
                                  Component.translatable("gui.casualties_cubed.moodle.weight_high.description1"));
            case NORMAL_NEG ->
                    low ? List.of(Component.translatable("gui.casualties_cubed.moodle.weight_low.title2"),
                                  Component.translatable("gui.casualties_cubed.moodle.weight_low.description2"))
                    :     List.of(Component.translatable("gui.casualties_cubed.moodle.weight_high.title2"),
                                  Component.translatable("gui.casualties_cubed.moodle.weight_high.description2"));
            case HEAVY_NEG ->
                    low ? List.of(Component.translatable("gui.casualties_cubed.moodle.weight_low.title3"),
                                  Component.translatable("gui.casualties_cubed.moodle.weight_low.description3"))
                    :     List.of(Component.translatable("gui.casualties_cubed.moodle.weight_high.title3"),
                                  Component.translatable("gui.casualties_cubed.moodle.weight_high.description3"));
            case CRITICAL_NEG ->
                    low ? List.of(Component.translatable("gui.casualties_cubed.moodle.weight_low.title4"),
                                  Component.translatable("gui.casualties_cubed.moodle.weight_low.description4"))
                    :     List.of(Component.translatable("gui.casualties_cubed.moodle.weight_high.title4"),
                                  Component.translatable("gui.casualties_cubed.moodle.weight_high.description4"));
            default -> List.of();
        };
    }
}
