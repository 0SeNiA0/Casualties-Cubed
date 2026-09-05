package net.zaharenko424.casualties_cubed.client.moodles;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.config.ServerConfig;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;

import java.util.ArrayList;
import java.util.List;

public class InternalBleedingMoodle extends AbstractMoodle {

    private static final ResourceLocation RED = CasualtiesCubed.resourceLoc("textures/gui/moodles/internal_bleeding_red.png");
    private static final ResourceLocation YELLOW = CasualtiesCubed.resourceLoc("textures/gui/moodles/internal_bleeding_yellow.png");

    public InternalBleedingMoodle() {
        super(false, true);
    }

    @Override
    public void update(Player player, PlayerHealthData data) {
        float bleed = data.internalBleeding();

        if (bleed > 0.6498) {
            setStatus(MoodleStatus.CRITICAL_NEG, true);
        } else if (bleed > 0.43605) {
            setStatus(MoodleStatus.HEAVY_NEG, true);
        } else if (bleed > 0.2223) {
            setStatus(MoodleStatus.NORMAL_NEG, bleed > 0.4275);
        } else if (bleed > 0.04275) {
            setStatus(MoodleStatus.LIGHT_NEG);
        } else clearStatus();
    }

    @Override
    protected void renderIcon(GuiGraphics ms, float partialTicks, int x, int y) {
        ms.blit(ServerConfig.EXPIE_MODE.get() ? YELLOW : RED, x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        List<Component> componentList = new ArrayList<>();
        componentList.add(Component.translatable("casualties_cubed.gui.moodle.internal_bleeding.title3").withStyle(ChatFormatting.GOLD));
        componentList.add(Component.translatable("casualties_cubed.gui.moodle.internal_bleeding.description3").withStyle(ChatFormatting.GRAY));
        return componentList;
    }
}
