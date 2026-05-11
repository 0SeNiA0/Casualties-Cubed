package net.zaharenko424.casualties_cubed.client.moodles;

import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HighBloodMoodle extends AbstractMoodleVisual {
    
    @Override
    public MoodleStatus calculateStatus(Player player) {
        Optional<Float> blood = player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(PlayerHealthData::getBloodVolume);
        float blood2 = blood.orElse(5f);
        if (blood2>5.625){
            return MoodleStatus.CRITICAL;
        } else if (blood2>5.5) {
            return MoodleStatus.HEAVY;
        } else if (blood2>5.25) {
            return MoodleStatus.NORMAL;
        } else if (blood2>5) {
            return MoodleStatus.LIGHT;
        }else {
            return MoodleStatus.NONE;
        }
    }

    @Override
    public void renderIcon(GuiGraphics ms, float partialTicks, int x, int y) {
        ResourceLocation tex = CasualtiesCubed.resourceLoc("textures/gui/moodles/highblood_moodle.png");
        ms.blit(tex, x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        List<Component> componentList = new ArrayList<>();
        switch (getMoodleStatus()){
            case LIGHT -> {
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.high_blood.title1"));
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.high_blood.description1").withStyle(ChatFormatting.GRAY));
            }
            case NORMAL -> {
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.high_blood.title2").withStyle(ChatFormatting.YELLOW));
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.high_blood.description2").withStyle(ChatFormatting.GRAY));
            }
            case HEAVY -> {
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.high_blood.title3").withStyle(ChatFormatting.GOLD));
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.high_blood.description3").withStyle(ChatFormatting.GRAY));
            }
            case CRITICAL -> {
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.high_blood.title4").withStyle(ChatFormatting.RED));
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.high_blood.description4").withStyle(ChatFormatting.GRAY));
            }
        }
        return componentList;
    }
}
