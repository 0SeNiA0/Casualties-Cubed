package net.zaharenko424.casualties_cubed.client.moodles;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;

import java.util.List;

public class HungerMoodle extends AbstractMoodle {

    private static final ResourceLocation TEX = CasualtiesCubed.resourceLoc("textures/gui/moodles/hunger.png");

    @Override
    public void update(Player player, PlayerHealthData data) {
        float hunger = data.getCUHunger(player);

        if (hunger <= 15) {
            setStatus(MoodleStatus.CRITICAL_NEG, hunger <= 0);
        } else if (hunger < 35) {
            setStatus(MoodleStatus.HEAVY_NEG);
        } else if (hunger < 50) {
            setStatus(MoodleStatus.NORMAL_NEG);
        } else if (hunger < 75) {
            setStatus(MoodleStatus.LIGHT_NEG);
        } else if (hunger >= 120) {
            setStatus(MoodleStatus.NORMAL_POS);// CU > but currently 120 is max
        } else if (hunger > 100) {
            setStatus(MoodleStatus.LIGHT_POS);
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
                    List.of(Component.translatable("gui.casualties_cubed.moodle.hunger.title1"),
                            Component.translatable("gui.casualties_cubed.moodle.hunger.description1"));
            case NORMAL_NEG ->
                    List.of(Component.translatable("gui.casualties_cubed.moodle.hunger.title2"),
                            Component.translatable("gui.casualties_cubed.moodle.hunger.description2"));
            case HEAVY_NEG ->
                    List.of(Component.translatable("gui.casualties_cubed.moodle.hunger.title3"),
                            Component.translatable("gui.casualties_cubed.moodle.hunger.description3"));
            case CRITICAL_NEG ->
                    List.of(Component.translatable("gui.casualties_cubed.moodle.hunger.title4"),
                            Component.translatable("gui.casualties_cubed.moodle.hunger.description4"));
            case LIGHT_POS ->
                    List.of(Component.translatable("gui.casualties_cubed.moodle.hunger.title5"),
                            Component.translatable("gui.casualties_cubed.moodle.hunger.description5"));
            case NORMAL_POS ->
                    List.of(Component.translatable("gui.casualties_cubed.moodle.hunger.title6"),
                            Component.translatable("gui.casualties_cubed.moodle.hunger.description6"));
            default -> List.of();
        };
    }
}
