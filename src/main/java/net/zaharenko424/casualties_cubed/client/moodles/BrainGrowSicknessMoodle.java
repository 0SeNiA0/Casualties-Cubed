package net.zaharenko424.casualties_cubed.client.moodles;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;

import java.util.List;

public class BrainGrowSicknessMoodle extends AbstractMoodle {

    private static final ResourceLocation TEX = CasualtiesCubed.resourceLoc("textures/gui/moodles/braingrow_sickness.png");

    public BrainGrowSicknessMoodle() {
        super(false, true);
    }

    @Override
    public void update(Player player, PlayerHealthData data) {
        if (data.brainGrowSickness() > 0) {
            setStatus(MoodleStatus.LIGHT_NEG);
        } else clearStatus();
    }

    @Override
    protected void renderIcon(GuiGraphics graphics, float partialTicks, int x, int y) {
        graphics.blit(TEX, x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        return List.of(Component.translatable("gui.casualties_cubed.moodle.braingrow_sickness.title"),
                       Component.translatable("gui.casualties_cubed.moodle.braingrow_sickness.description", Math.round(PlayerHealthData.of(player).map(PlayerHealthData::brainGrowSickness).orElse(0f))));
    }
}
