package net.zaharenko424.casualties_cubed.client.moodles;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;

import java.util.List;

public class HypoventilationMoodle extends AbstractMoodle {

    private static final ResourceLocation TEX = CasualtiesCubed.resourceLoc("textures/gui/moodles/hypoventilation.png");
    private static final ResourceLocation RESP_ARREST = CasualtiesCubed.resourceLoc("textures/gui/moodles/cant_breathe.png");

    @Override
    public void update(Player player, PlayerHealthData data) {
        if (!data.isBreathing()) {
            setStatus(MoodleStatus.CRITICAL_NEG, true);
            return;
        }

        float respRate = data.respiratoryRate();
        if (respRate < 50) {
            setStatus(MoodleStatus.NORMAL_NEG);
        } else if (respRate < 90) {
            setStatus(MoodleStatus.LIGHT_NEG);
        } else clearStatus();
    }

    @Override
    protected void renderIcon(GuiGraphics graphics, float partialTicks, int x, int y) {
        graphics.blit(getMoodleStatus() == MoodleStatus.CRITICAL_NEG ? RESP_ARREST : TEX, x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        return switch (getMoodleStatus()) {
            case LIGHT_NEG ->
                    List.of(Component.translatable("gui.casualties_cubed.moodle.hypoventilation.title1"),
                            Component.translatable("gui.casualties_cubed.moodle.hypoventilation.description1"));
            case NORMAL_NEG ->
                    List.of(Component.translatable("gui.casualties_cubed.moodle.hypoventilation.title2"),
                            Component.translatable("gui.casualties_cubed.moodle.hypoventilation.description2"));
            case CRITICAL_NEG ->
                    List.of(Component.translatable("gui.casualties_cubed.moodle.hypoventilation.title3"),
                            Component.translatable("gui.casualties_cubed.moodle.hypoventilation.description3"));
            default -> List.of();
        };
    }
}
