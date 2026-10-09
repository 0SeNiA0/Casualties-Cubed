package net.zaharenko424.casualties_cubed.client.moodles;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;

import java.util.List;

public class FocusedMoodle extends AbstractMoodle {

    private static final ResourceLocation[] TEX = {
                    CasualtiesCubed.resourceLoc("textures/gui/moodles/impending_doom.png"),
                    CasualtiesCubed.resourceLoc("textures/gui/moodles/horrified.png"),
                    CasualtiesCubed.resourceLoc("textures/gui/moodles/focused.png")
            };

    int state;

    @Override
    public void update(Player player, PlayerHealthData data) {
        if (data.focusedLevel() > 0) {
            state = 2;
            setStatus(MoodleStatus.CRITICAL_NEG, true);
        } else if (data.terrifiedLevel() >= 50) {
            state = 1;
            setStatus(MoodleStatus.CRITICAL_NEG, true);
        } else if (data.terrifiedLevel() > 0) {
            state = 0;
            setStatus(MoodleStatus.NORMAL_NEG);
        } else {
            clearStatus();
        }
    }

    @Override
    protected void renderIcon(GuiGraphics graphics, float partialTicks, int x, int y) {
        graphics.blit(TEX[state], x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        return switch (state) {
            case 0 ->
                    List.of(Component.translatable("gui.casualties_cubed.moodle.focused.title1"),
                            Component.translatable("gui.casualties_cubed.moodle.focused.description1"));
            case 1 ->
                    List.of(Component.translatable("gui.casualties_cubed.moodle.focused.title2"),
                            Component.translatable("gui.casualties_cubed.moodle.focused.description2"));
            default ->
                    List.of(Component.translatable("gui.casualties_cubed.moodle.focused.title3"),
                            Component.translatable("gui.casualties_cubed.moodle.focused.description3"));
        };
    }
}
