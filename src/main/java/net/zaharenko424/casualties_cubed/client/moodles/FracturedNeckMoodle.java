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

public class FracturedNeckMoodle extends AbstractMoodle {

    private static final ResourceLocation TEX = CasualtiesCubed.resourceLoc("textures/gui/moodles/fractured_neck_moodle.png");

    public FracturedNeckMoodle() {
        super(false, true);
    }

    @Override
    public void update(Player player, PlayerHealthData data) {
        float timer = data.getLimb(Limb.HEAD).getBoneHealTimer();

        if (timer > 32 * 60 + 18) {
            setStatus(MoodleStatus.CRITICAL_NEG);
        } else if(timer > 19 * 60 + 23) {
            setStatus(MoodleStatus.HEAVY_NEG);
        } else if (timer > 6 * 60 + 28) {
            setStatus(MoodleStatus.NORMAL_NEG);
        } else if (timer > 0) {
            setStatus(MoodleStatus.LIGHT_NEG);
        } else clearStatus();
    }

    @Override
    protected void renderIcon(GuiGraphics ms, float partialTicks, int x, int y) {
        ms.blit(TEX, x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        List<Component> componentList = new ArrayList<>();
        componentList.add(Component.translatable("casualties_cubed.gui.moodle.fracture_head.title3").withStyle(ChatFormatting.GOLD));
        componentList.add(Component.translatable("casualties_cubed.gui.moodle.fracture_head.description3").withStyle(ChatFormatting.GRAY));
        return componentList;
    }
}
