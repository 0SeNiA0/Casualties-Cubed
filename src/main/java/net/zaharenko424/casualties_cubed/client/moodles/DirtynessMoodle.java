package net.zaharenko424.casualties_cubed.client.moodles;

import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class DirtynessMoodle extends AbstractMoodleVisual {

    private static final ResourceLocation TEX = CasualtiesCubed.resourceLoc("textures/gui/moodles/dirty.png");

    @Override
    public MoodleStatus calculateStatus(Player player) {
        float dirt = player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(PlayerHealthData::getDirtiness).orElse(0f);
        if (dirt > 80) {
            return MoodleStatus.NORMAL;
        } else if (dirt > 50) {
            return MoodleStatus.LIGHT;
        } else {
            return MoodleStatus.NONE;
        }
    }

    @Override
    public void renderIcon(GuiGraphics ms, float partialTicks, int x, int y) {
        ms.blit(TEX, x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        List<Component> componentList = new ArrayList<>();
        switch (getMoodleStatus()) {
            case LIGHT -> {
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.dirty.title1"));
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.dirty.description1").withStyle(ChatFormatting.GRAY));
            }
            case NORMAL -> {
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.dirty.title2").withStyle(ChatFormatting.YELLOW));
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.dirty.description2").withStyle(ChatFormatting.GRAY));
            }
        }
        return componentList;
    }
}
