package net.zaharenko424.casualties_cubed.client.moodles;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.limbs.ChipState;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DislocationMoodle extends AbstractMoodleVisual {

    private static final ResourceLocation TEX = CasualtiesCubed.resourceLoc("textures/gui/moodles/dislocation_moodle.png");
    private static final List<Limb> checkList = List.of(Limb.LEFT_ARM, Limb.RIGHT_FOOT, Limb.RIGHT_LEG, Limb.RIGHT_ARM,
            Limb.RIGHT_HAND, Limb.LEFT_FOOT, Limb.LEFT_LEG, Limb.LEFT_HAND);

    @Override
    public boolean shouldBeDisplayed(ChipState state) {
        return state.isActive();
    }

    @Override
    protected @NotNull MoodleStatus calculateStatus(Player player, PlayerHealthData data) {
        for (Limb limb : checkList) {
            if (data.getLimb(limb).getDislocation() > 0) return MoodleStatus.HEAVY_NEG;
        }

        return MoodleStatus.NONE;
    }

    @Override
    protected void renderIcon(GuiGraphics ms, float partialTicks, int x, int y) {
        ms.blit(TEX, x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        List<Component> componentList = new ArrayList<>();
        componentList.add(Component.translatable("casualties_cubed.gui.moodle.dislocation.title3").withStyle(ChatFormatting.GOLD));
        componentList.add(Component.translatable("casualties_cubed.gui.moodle.dislocation.description3").withStyle(ChatFormatting.GRAY));
        return componentList;
    }
}
