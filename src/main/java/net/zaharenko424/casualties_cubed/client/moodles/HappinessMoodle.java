package net.zaharenko424.casualties_cubed.client.moodles;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;

import java.util.List;

public class HappinessMoodle extends AbstractMoodle {

    private static final ResourceLocation[] TEX = {
            CasualtiesCubed.resourceLoc("textures/gui/moodles/happiness_low_1.png"),
            CasualtiesCubed.resourceLoc("textures/gui/moodles/happiness_low_2.png"),
            CasualtiesCubed.resourceLoc("textures/gui/moodles/happiness_low_3.png"),
            CasualtiesCubed.resourceLoc("textures/gui/moodles/happiness_low_4.png"),
            CasualtiesCubed.resourceLoc("textures/gui/moodles/happiness_high.png")
    };

    private ResourceLocation sprite = TEX[4];

    @Override
    public void update(Player player, PlayerHealthData data) {
        float happiness = data.totalHappiness();

        if (happiness < -75) {
            setStatus(MoodleStatus.CRITICAL_NEG);
            sprite = TEX[3];
        } else if (happiness < -50) {
            setStatus(MoodleStatus.HEAVY_NEG);
            sprite = TEX[2];
        } else if (happiness < -30) {
            setStatus(MoodleStatus.NORMAL_NEG);
            sprite = TEX[1];
        } else if (happiness < -10) {
            setStatus(MoodleStatus.LIGHT_NEG);
            sprite = TEX[0];
        } else if (happiness > 75) {
            setStatus(MoodleStatus.CRITICAL_POS);
            sprite = TEX[4];
        } else if (happiness > 50) {
            setStatus(MoodleStatus.HIGH_POS);
            sprite = TEX[4];
        } else if (happiness > 30) {
            setStatus(MoodleStatus.NORMAL_POS);
            sprite = TEX[4];
        } else if (happiness > 10) {
            setStatus(MoodleStatus.LIGHT_POS);
            sprite = TEX[4];
        } else clearStatus();
    }

    @Override
    protected void renderIcon(GuiGraphics graphics, float partialTicks, int x, int y) {
        graphics.blit(sprite, x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        return switch (getMoodleStatus()) {
            case LIGHT_NEG ->
                    List.of(Component.translatable("gui.casualties_cubed.moodle.happiness_low.title1"),
                            Component.translatable("gui.casualties_cubed.moodle.happiness_low.description1"));
            case NORMAL_NEG ->
                    List.of(Component.translatable("gui.casualties_cubed.moodle.happiness_low.title2"),
                            Component.translatable("gui.casualties_cubed.moodle.happiness_low.description2"));
            case HEAVY_NEG ->
                    List.of(Component.translatable("gui.casualties_cubed.moodle.happiness_low.title3"),
                            Component.translatable("gui.casualties_cubed.moodle.happiness_low.description3"));
            case CRITICAL_NEG ->
                    List.of(Component.translatable("gui.casualties_cubed.moodle.happiness_low.title4"),
                            Component.translatable("gui.casualties_cubed.moodle.happiness_low.description4"));
            case LIGHT_POS ->
                    List.of(Component.translatable("gui.casualties_cubed.moodle.happiness_high.title1"),
                            Component.translatable("gui.casualties_cubed.moodle.happiness_high.description1"));
            case NORMAL_POS ->
                    List.of(Component.translatable("gui.casualties_cubed.moodle.happiness_high.title2"),
                            Component.translatable("gui.casualties_cubed.moodle.happiness_high.description2"));
            case HIGH_POS ->
                    List.of(Component.translatable("gui.casualties_cubed.moodle.happiness_high.title3"),
                            Component.translatable("gui.casualties_cubed.moodle.happiness_high.description3"));
            case CRITICAL_POS ->
                    List.of(Component.translatable("gui.casualties_cubed.moodle.happiness_high.title4"),
                            Component.translatable("gui.casualties_cubed.moodle.happiness_high.description4"));
        };
    }
}
