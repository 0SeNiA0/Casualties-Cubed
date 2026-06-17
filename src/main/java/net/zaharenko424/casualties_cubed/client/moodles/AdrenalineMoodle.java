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

import java.util.ArrayList;
import java.util.List;

public class AdrenalineMoodle extends AbstractMoodleVisual {

    private static final ResourceLocation TEX = CasualtiesCubed.resourceLoc("textures/gui/moodles/adrenaline_moodle.png");

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
        float blood2 = data.getAdrenaline();
        if (blood2 > 65) {
            return MoodleStatus.NORMAL_NEG;
        } else if (blood2 > 20) {
            return MoodleStatus.LIGHT_NEG;
        } else {
            return MoodleStatus.NONE;
        }
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
