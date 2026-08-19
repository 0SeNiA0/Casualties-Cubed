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

public class AdrenalineMoodle extends AbstractMoodle {

    private static final ResourceLocation TEX = CasualtiesCubed.resourceLoc("textures/gui/moodles/adrenaline_moodle.png");

    public AdrenalineMoodle() {
        super(true, true);
    }

    @Override
    public void update(Player player, PlayerHealthData data) {
        float blood2 = data.currentAdrenaline();
        if (blood2 > 65) {
            setStatus(MoodleStatus.NORMAL_NEG);
        } else if (blood2 > 20) {
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
        componentList.add(Component.translatable("casualties_cubed.gui.moodle.adrenaline.title1"));
        componentList.add(Component.translatable("casualties_cubed.gui.moodle.adrenaline.description1").withStyle(ChatFormatting.GRAY));
        return componentList;
    }
}
