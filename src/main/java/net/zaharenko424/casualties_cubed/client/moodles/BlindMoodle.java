package net.zaharenko424.casualties_cubed.client.moodles;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;

import java.util.ArrayList;
import java.util.List;

public class BlindMoodle extends AbstractMoodle {

    private static final ResourceLocation TEX = CasualtiesCubed.resourceLoc("textures/gui/moodles/eye_gone_moodle.png");

    public BlindMoodle() {
        super(true, false);
    }

    @Override
    public void update(Player player, PlayerHealthData data) {
        boolean r = data.isRightEyeBlind();
        boolean l = data.isLeftEyeBlind();

        if (r & l) {
            setStatus(MoodleStatus.HEAVY_NEG);
        } else if (r || l) {
            setStatus(MoodleStatus.NORMAL_NEG);
        } else clearStatus();
    }

    @Override
    protected void renderIcon(GuiGraphics ms, float partialTicks, int x, int y) {
        ms.blit(TEX, x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        List<Component> componentList = new ArrayList<>();
        switch (getMoodleStatus()) {
            case NORMAL_NEG -> {
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.eye_gone.title1"));
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.eye_gone.description1").withStyle(ChatFormatting.GRAY));
            }
            case HEAVY_NEG -> {
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.eye_gone.title2").withStyle(ChatFormatting.YELLOW));
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.eye_gone.description2").withStyle(ChatFormatting.GRAY));
            }
        }
        return componentList;
    }
}
