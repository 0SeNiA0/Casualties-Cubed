package net.zaharenko424.casualties_cubed.client.moodles;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;

import java.util.ArrayList;
import java.util.List;

public class LungFailureMoodle extends AbstractMoodle {

    private static final ResourceLocation TEX = CasualtiesCubed.resourceLoc("textures/gui/moodles/lungfaliure_moodle.png");

    @Override
    public void update(Player player, PlayerHealthData data) {
        if (data.getLimb(Limb.THORAX).getMuscleHealth() < 4) {
            setStatus(MoodleStatus.CRITICAL_NEG, true);
        } else clearStatus();
    }

    @Override
    protected void renderIcon(GuiGraphics ms, float partialTicks, int x, int y) {
        ms.blit(TEX, x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        List<Component> componentList = new ArrayList<>();
        componentList.add(Component.translatable("casualties_cubed.gui.moodle.lung_faliure.title4").withStyle(ChatFormatting.GOLD));
        componentList.add(Component.translatable("casualties_cubed.gui.moodle.lung_faliure.description4").withStyle(ChatFormatting.GRAY));
        return componentList;
    }
}
