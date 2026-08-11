package net.zaharenko424.casualties_cubed.client.moodles;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.limbs.ChipState;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ImmunityMoodle extends AbstractMoodleVisual {

    private static final ResourceLocation TEX_HIGH = CasualtiesCubed.resourceLoc("textures/gui/moodles/immunocompetent.png");
    private static final ResourceLocation TEX_LOW = CasualtiesCubed.resourceLoc("textures/gui/moodles/immunocompromised.png");

    private boolean high;

    @Override
    public boolean isSideMoodle() {
        return true;
    }

    @Override
    public boolean shouldBeDisplayed(ChipState state) {
        return state.isActive();
    }

    @Override
    protected @NotNull MoodleStatus calculateStatus(Player player, PlayerHealthData data) {
        if (data.getImmunity() > 150) {
            high = true;
            return MoodleStatus.NORMAL_POS;
        }

        if (data.getImmunity() < 55) {
            high = false;
            return MoodleStatus.NORMAL_NEG;
        }

        return MoodleStatus.NONE;
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
