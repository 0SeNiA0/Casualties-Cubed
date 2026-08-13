package net.zaharenko424.casualties_cubed.client.moodles;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;

import java.util.List;

public class EnergizedMoodle extends AbstractMoodle {

    private static final ResourceLocation TEX = CasualtiesCubed.resourceLoc("textures/gui/moodles/energized.png");

    public EnergizedMoodle() {
        super(true, true);
    }

    @Override
    public void update(Player player, PlayerHealthData data) {
        if (data.caffeinated() > 0) {
            setStatus(MoodleStatus.NORMAL_POS);
        } else clearStatus();
    }

    @Override
    protected void renderIcon(GuiGraphics ms, float partialTicks, int x, int y) {
        ms.blit(TEX, x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        return List.of(
                Component.translatable("gui.casualties_cubed.moodle.energized.title"),
                Component.translatable("gui.casualties_cubed.moodle.energized.description", PlayerHealthData.of(player).map(PlayerHealthData::caffeinated).orElse(0f))
        );
    }
}
