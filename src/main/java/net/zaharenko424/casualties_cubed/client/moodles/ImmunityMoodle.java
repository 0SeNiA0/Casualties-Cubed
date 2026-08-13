package net.zaharenko424.casualties_cubed.client.moodles;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;

import java.util.List;

public class ImmunityMoodle extends AbstractMoodle {

    private static final ResourceLocation TEX_HIGH = CasualtiesCubed.resourceLoc("textures/gui/moodles/immunocompetent.png");
    private static final ResourceLocation TEX_LOW = CasualtiesCubed.resourceLoc("textures/gui/moodles/immunocompromised.png");

    private boolean high;

    public ImmunityMoodle() {
        super(true, true);
    }

    @Override
    public void update(Player player, PlayerHealthData data) {
        float immunity = data.getImmunity();

        if (immunity > 150) {
            high = true;
            setStatus(MoodleStatus.NORMAL_POS);
        } else if (immunity < 55) {
            high = false;
            setStatus(MoodleStatus.NORMAL_NEG);
        } else clearStatus();
    }

    @Override
    protected void renderIcon(GuiGraphics graphics, float partialTicks, int x, int y) {
        graphics.blit(high ? TEX_HIGH : TEX_LOW, x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        return high ? List.of(
                Component.translatable("gui.casualties_cubed.moodle.immunocompetent.title"),
                Component.translatable("gui.casualties_cubed.moodle.immunocompetent.description").withStyle(ChatFormatting.GRAY))
                : List.of(
                Component.translatable("gui.casualties_cubed.moodle.immunocompromised.title"),
                Component.translatable("gui.casualties_cubed.moodle.immunocompromised.description"));
    }
}
