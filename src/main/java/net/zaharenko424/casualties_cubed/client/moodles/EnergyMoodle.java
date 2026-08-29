package net.zaharenko424.casualties_cubed.client.moodles;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;

import java.util.List;

public class EnergyMoodle extends AbstractMoodle {

    private static final ResourceLocation TEX = CasualtiesCubed.resourceLoc("textures/gui/moodles/energy.png");

    @Override
    public void update(Player player, PlayerHealthData data) {
        float energy = data.energy();

        if (energy < 7) {
            setStatus(MoodleStatus.CRITICAL_NEG, true);
        } else if (energy < 15) {
            setStatus(MoodleStatus.HEAVY_NEG);
        } else if (energy < 25) {
            setStatus(MoodleStatus.NORMAL_NEG);
        } else if (energy < 35) {
            setStatus(MoodleStatus.LIGHT_NEG);
        } else clearStatus();
    }

    @Override
    protected void renderIcon(GuiGraphics graphics, float partialTicks, int x, int y) {
        graphics.blit(TEX, x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        return switch (getMoodleStatus()) {
            case LIGHT_NEG ->
                    List.of(Component.translatable("gui.casualties_cubed.moodle.energy.title1"),
                            Component.translatable("gui.casualties_cubed.moodle.energy.description1"));
            case NORMAL_NEG ->
                    List.of(Component.translatable("gui.casualties_cubed.moodle.energy.title2"),
                            Component.translatable("gui.casualties_cubed.moodle.energy.description2"));
            case HEAVY_NEG ->
                    List.of(Component.translatable("gui.casualties_cubed.moodle.energy.title3"),
                            Component.translatable("gui.casualties_cubed.moodle.energy.description3"));
            case CRITICAL_NEG ->
                    List.of(Component.translatable("gui.casualties_cubed.moodle.energy.title4"),
                            Component.translatable("gui.casualties_cubed.moodle.energy.description4"));
            default -> List.of();
        };
    }
}
