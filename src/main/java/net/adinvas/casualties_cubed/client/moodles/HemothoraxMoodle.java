package net.adinvas.casualties_cubed.client.moodles;

import net.adinvas.casualties_cubed.PlayerHealthProvider;
import net.adinvas.casualties_cubed.CasualtiesCubed;
import net.adinvas.casualties_cubed.limbs.PlayerHealthData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HemothoraxMoodle extends AbstractMoodleVisual{

    @Override
    public MoodleStatus calculateStatus(Player player) {
        Optional<Float> hemo = player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(PlayerHealthData::getHemothorax);
        if (hemo.orElse(0f)>70){
            return MoodleStatus.CRITICAL;
        }else if (hemo.orElse(0f)>40){
            return MoodleStatus.HEAVY;
        }else {
            return MoodleStatus.NONE;
        }
    }

    @Override
    public void renderIcon(GuiGraphics ms, float partialTicks, int x, int y) {
        ResourceLocation tex = CasualtiesCubed.resourceLoc("textures/gui/moodles/hemothorax_moodle.png");
        ms.blit(tex, x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        List<Component> componentList = new ArrayList<>();
        componentList.add(Component.translatable("casualties_cubed.gui.moodle.hemothorax.title3").withStyle(ChatFormatting.GOLD));
        componentList.add(Component.translatable("casualties_cubed.gui.moodle.hemothorax.description3").withStyle(ChatFormatting.GRAY));
        return componentList;
    }
}
