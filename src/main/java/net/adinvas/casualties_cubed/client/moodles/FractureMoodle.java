package net.adinvas.casualties_cubed.client.moodles;

import net.adinvas.casualties_cubed.PlayerHealthProvider;
import net.adinvas.casualties_cubed.CasualtiesCubed;
import net.adinvas.casualties_cubed.limbs.Limb;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FractureMoodle extends AbstractMoodleVisual {
    
    public static List<Limb> checkList = new ArrayList<>();
    
    static {
        checkList.add(Limb.LEFT_ARM);
        checkList.add(Limb.RIGHT_FOOT);
        checkList.add(Limb.RIGHT_LEG);
        checkList.add(Limb.RIGHT_ARM);
        checkList.add(Limb.RIGHT_HAND);
        checkList.add(Limb.LEFT_FOOT);
        checkList.add(Limb.LEFT_LEG);
        checkList.add(Limb.LEFT_HAND);
    }
    
    @Override
    public MoodleStatus calculateStatus(Player player) {
        Optional<Boolean> fractured = player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(h->{
            for (Limb limb: checkList){
                if (h.getLimbFracture(limb)>0){
                    return true;
                }
            }
            return false;
        });

        if (fractured.orElse(false)){
            return MoodleStatus.HEAVY;
        }else {
            return MoodleStatus.NONE;
        }
    }

    @Override
    public void renderIcon(GuiGraphics ms, float partialTicks, int x, int y) {
        ResourceLocation tex = CasualtiesCubed.resourceLoc("textures/gui/moodles/fracture_moodle.png");
        ms.blit(tex, x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        List<Component> componentList = new ArrayList<>();
        componentList.add(Component.translatable("casualties_cubed.gui.moodle.fracture.title3").withStyle(ChatFormatting.GOLD));
        componentList.add(Component.translatable("casualties_cubed.gui.moodle.fracture.description3").withStyle(ChatFormatting.GRAY));
        return componentList;
    }
}
