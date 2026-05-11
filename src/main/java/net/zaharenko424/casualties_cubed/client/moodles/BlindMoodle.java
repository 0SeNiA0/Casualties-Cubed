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

public class BlindMoodle extends AbstractMoodleVisual {
    
    @Override
    public MoodleStatus calculateStatus(Player player) {
        boolean r = player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(PlayerHealthData::isRightEyeBlind).orElse(false);
        boolean l = player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(PlayerHealthData::isLeftEyeBlind).orElse(false);
        if (r&l){
            return MoodleStatus.HEAVY;
        }else if (r||l){
            return MoodleStatus.NORMAL;
        }else{
            return MoodleStatus.NONE;
        }
    }

    @Override
    public void renderIcon(GuiGraphics ms, float partialTicks, int x, int y) {
        ResourceLocation tex = CasualtiesCubed.resourceLoc("textures/gui/moodles/eye_gone_moodle.png");
        ms.blit(tex, x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        List<Component> componentList = new ArrayList<>();
        switch (getMoodleStatus()){
            case NORMAL -> {
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.eye_gone.title1"));
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.eye_gone.description1").withStyle(ChatFormatting.GRAY));
            }
            case HEAVY -> {
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.eye_gone.title2").withStyle(ChatFormatting.YELLOW));
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.eye_gone.description2").withStyle(ChatFormatting.GRAY));
            }
        }
        return componentList;
    }
}
