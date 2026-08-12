package net.zaharenko424.casualties_cubed.client.moodles;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.limbs.ChipState;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ArrhythmiaMoodle extends AbstractMoodleVisual {

    private static final ResourceLocation TEX = CasualtiesCubed.resourceLoc("textures/gui/moodles/arrhythmia.png");

    @Override
    public boolean shouldBeDisplayed(ChipState state) {
        return state.isActive();
    }

    @Override
    protected @NotNull MoodleStatus calculateStatus(Player player, PlayerHealthData data) {
        float fibrillation = data.getFibrillationProgress();
        if (fibrillation > 75) return MoodleStatus.CRITICAL_NEG;
        if (fibrillation > 50) return MoodleStatus.HEAVY_NEG;

        return fibrillation > 15 ? MoodleStatus.NORMAL_NEG : MoodleStatus.NONE;
    }

    @Override
    protected void renderIcon(GuiGraphics graphics, float partialTicks, int x, int y) {
        graphics.blit(TEX, x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        return switch (getMoodleStatus()) {
            case NORMAL_NEG ->
                    List.of(Component.translatable("gui.casualties_cubed.moodle.arrhythmia.title1"),
                            Component.translatable("gui.casualties_cubed.moodle.arrhythmia.description1"));
            case HEAVY_NEG ->
                    List.of(Component.translatable("gui.casualties_cubed.moodle.arrhythmia.title2"),
                            Component.translatable("gui.casualties_cubed.moodle.arrhythmia.description2"));
            case CRITICAL_NEG ->
                    List.of(Component.translatable("gui.casualties_cubed.moodle.arrhythmia.title3"),
                            Component.translatable("gui.casualties_cubed.moodle.arrhythmia.description3"));
            default -> List.of();
        };
    }
}
