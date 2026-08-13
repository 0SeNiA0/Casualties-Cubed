package net.zaharenko424.casualties_cubed.client.moodles;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;

import java.util.List;

public class StrokeMoodle extends AbstractMoodle {

    private static final ResourceLocation TEX = CasualtiesCubed.resourceLoc("textures/gui/moodles/stroke.png");

    public StrokeMoodle() {
        super(false, true);
    }

    @Override
    public void update(Player player, PlayerHealthData data) {
        if (data.strokeAmount() > 70) {
            setStatus(MoodleStatus.CRITICAL_NEG, true);
        } else clearStatus();
    }

    @Override
    protected void renderIcon(GuiGraphics graphics, float partialTicks, int x, int y) {
        graphics.blit(TEX, x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        return List.of(Component.translatable("gui.casualties_cubed.moodle.stroke.title"),
                       Component.translatable("gui.casualties_cubed.moodle.stroke.description"));
    }
}
