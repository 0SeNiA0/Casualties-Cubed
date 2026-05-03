package net.adinvas.prototype_pain.client.moodles;

import net.adinvas.prototype_pain.PlayerHealthProvider;
import net.adinvas.prototype_pain.PrototypePain;
import net.adinvas.prototype_pain.limbs.Limb;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class AmputatedMoodle extends AbstractMoodleVisual{
    @Override
    public MoodleStatus calculateStatus(Player player) {
        boolean amputated = player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(h->{
            for (Limb limb:Limb.values()){
                if (h.isAmputated(limb))
                    return true;
            }
            return false;
        }).orElse(false);
        if (amputated) {
            return MoodleStatus.HEAVY;
        }else {
            return MoodleStatus.NONE;
        }
    }

    @Override
    public void renderIcon(GuiGraphics ms, float partialTicks, int x, int y) {
        ResourceLocation tex = new ResourceLocation(PrototypePain.MOD_ID,"textures/gui/moodles/amputated_moodle.png");
        ms.blit(tex, x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        List<Component> componentList = new ArrayList<>();
        componentList.add(Component.translatable("prototype_pain.gui.moodle.amputated.title1"));
        componentList.add(Component.translatable("prototype_pain.gui.moodle.amputated.description1").withStyle(ChatFormatting.GRAY));
        return componentList;
    }
}
