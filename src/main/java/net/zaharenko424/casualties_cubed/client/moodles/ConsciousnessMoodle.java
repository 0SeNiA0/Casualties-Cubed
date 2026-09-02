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

public class ConsciousnessMoodle extends AbstractMoodle {

    private static final ResourceLocation TEX = CasualtiesCubed.resourceLoc("textures/gui/moodles/consious_moodle.png");
    private static final ResourceLocation UNC_TEX = CasualtiesCubed.resourceLoc("textures/gui/moodles/unconsious_moodle.png");

    public boolean fullyUNC = false;

    @Override
    public void update(Player player, PlayerHealthData data) {
        float consciousness = data.consciousness();

        if (consciousness < 20) {
            fullyUNC = true;
            setStatus(MoodleStatus.CRITICAL_NEG);
        } else if (consciousness < 30) {
            fullyUNC = false;
            setStatus(MoodleStatus.CRITICAL_NEG);
        } else if (consciousness < 55) {
            setStatus(MoodleStatus.HEAVY_NEG);
        } else if (consciousness < 72) {
            setStatus(MoodleStatus.NORMAL_NEG);
        } else if (consciousness < 90) {
            setStatus(MoodleStatus.LIGHT_NEG);
        } else {
            clearStatus();
        }
    }

    @Override
    protected void renderIcon(GuiGraphics ms, float partialTicks, int x, int y) {
        ResourceLocation tex = getMoodleStatus() == MoodleStatus.CRITICAL_NEG && fullyUNC ? UNC_TEX : TEX;
        ms.blit(tex, x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        List<Component> componentList = new ArrayList<>();
        switch (getMoodleStatus()) {
            case LIGHT_NEG -> {
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.consiousness.title1"));
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.consiousness.description1").withStyle(ChatFormatting.GRAY));
            }
            case NORMAL_NEG -> {
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.consiousness.title2").withStyle(ChatFormatting.YELLOW));
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.consiousness.description2").withStyle(ChatFormatting.GRAY));
            }
            case HEAVY_NEG -> {
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.consiousness.title3").withStyle(ChatFormatting.GOLD));
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.consiousness.description3").withStyle(ChatFormatting.GRAY));
            }
            case CRITICAL_NEG -> {
                if (fullyUNC) {
                    componentList.add(Component.translatable("casualties_cubed.gui.moodle.consiousness.title5").withStyle(ChatFormatting.RED));
                    componentList.add(Component.translatable("casualties_cubed.gui.moodle.consiousness.description5").withStyle(ChatFormatting.GRAY));
                } else {
                    componentList.add(Component.translatable("casualties_cubed.gui.moodle.consiousness.title4").withStyle(ChatFormatting.RED));
                    componentList.add(Component.translatable("casualties_cubed.gui.moodle.consiousness.description4").withStyle(ChatFormatting.GRAY));
                }
            }
        }
        return componentList;
    }
}
