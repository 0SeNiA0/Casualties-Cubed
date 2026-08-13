package net.zaharenko424.casualties_cubed.client.moodles;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;

import java.util.List;

public class ImpairedSpeechMoodle extends AbstractMoodle {

    private static final ResourceLocation TEX = CasualtiesCubed.resourceLoc("textures/gui/moodles/impaired_speech.png");

    public ImpairedSpeechMoodle() {
        super(true, false);
    }

    @Override
    public void update(Player player, PlayerHealthData data) {
        if (data.getLimb(Limb.HEAD).getDislocationTimer() > 0 || data.isMouthRemoved()) {
            setStatus(MoodleStatus.LIGHT_NEG);
        } else clearStatus();
    }

    @Override
    protected void renderIcon(GuiGraphics graphics, float partialTicks, int x, int y) {
        graphics.blit(TEX, x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        return List.of(Component.translatable("gui.casualties_cubed.moodle.impaired_speech.title"),
                       Component.translatable("gui.casualties_cubed.moodle.impaired_speech.description"));
    }
}
