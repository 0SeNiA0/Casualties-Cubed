package net.zaharenko424.casualties_cubed.client.moodles;

import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.limbs.ChipState;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FractureMoodle extends AbstractMoodleVisual {

    private static final ResourceLocation TEX = CasualtiesCubed.resourceLoc("textures/gui/moodles/fracture_moodle.png");
    private static final List<Limb> checkList = List.of(Limb.LEFT_ARM, Limb.RIGHT_FOOT, Limb.RIGHT_LEG, Limb.RIGHT_ARM,
            Limb.RIGHT_HAND, Limb.LEFT_FOOT, Limb.LEFT_LEG, Limb.LEFT_HAND);

    @Override
    public boolean shouldBeDisplayed(ChipState state) {
        return state.isActive();
    }

    @Override
    public MoodleStatus calculateStatus(Player player) {
        Optional<Boolean> fractured = player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(data -> {
            for (Limb limb : checkList) {
                if (data.getLimb(limb).getFracture() > 0) {
                    return true;
                }
            }
            return false;
        });

        if (fractured.orElse(false)) {
            return MoodleStatus.HEAVY;
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
        componentList.add(Component.translatable("casualties_cubed.gui.moodle.fracture.title3").withStyle(ChatFormatting.GOLD));
        componentList.add(Component.translatable("casualties_cubed.gui.moodle.fracture.description3").withStyle(ChatFormatting.GRAY));
        return componentList;
    }
}
