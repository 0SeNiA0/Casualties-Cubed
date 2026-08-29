package net.zaharenko424.casualties_cubed.client.moodles;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;

import java.util.List;

public class StaminaMoodle extends AbstractMoodle {

    private static final ResourceLocation TEX = CasualtiesCubed.resourceLoc("textures/gui/moodles/stamina.png");

    @Override
    public void update(Player player, PlayerHealthData data) {
        float stamina = data.stamina();

        if (stamina < 15) {
            setStatus(MoodleStatus.CRITICAL_NEG, true);
        } else if (stamina < 35) {
            setStatus(MoodleStatus.HEAVY_NEG);
        } else if (stamina < 50) {
            setStatus(MoodleStatus.NORMAL_NEG);
        } else if (stamina < 70) {
            setStatus(MoodleStatus.LIGHT_NEG);
        } else clearStatus();
    }

    @Override
    protected void renderIcon(GuiGraphics graphics, float partialTicks, int x, int y) {
        graphics.blit(TEX, x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        float stamina = PlayerHealthData.of(player).map(PlayerHealthData::stamina).orElse(0f);
        return switch (getMoodleStatus()) {
            case LIGHT_NEG ->
                    List.of(Component.translatable("gui.casualties_cubed.moodle.stamina.title1"),
                            Component.translatable("gui.casualties_cubed.moodle.stamina.description1", stamina));
            case NORMAL_NEG ->
                    List.of(Component.translatable("gui.casualties_cubed.moodle.stamina.title2"),
                            Component.translatable("gui.casualties_cubed.moodle.stamina.description2", stamina));
            case HEAVY_NEG ->
                    List.of(Component.translatable("gui.casualties_cubed.moodle.stamina.title3"),
                            Component.translatable("gui.casualties_cubed.moodle.stamina.description3", stamina));
            case CRITICAL_NEG ->
                    List.of(Component.translatable("gui.casualties_cubed.moodle.stamina.title4"),
                            Component.translatable("gui.casualties_cubed.moodle.stamina.description4", stamina));
            default -> List.of();
        };
    }
}
