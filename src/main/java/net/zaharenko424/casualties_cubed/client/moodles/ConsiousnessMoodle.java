package net.zaharenko424.casualties_cubed.client.moodles;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ConsiousnessMoodle extends AbstractMoodleVisual {

    private static final ResourceLocation TEX = CasualtiesCubed.resourceLoc("textures/gui/moodles/consious_moodle.png");
    private static final ResourceLocation UNC_TEX = CasualtiesCubed.resourceLoc("textures/gui/moodles/unconsious_moodle.png");

    public boolean fullyUNC = false;

    @Override
    protected @NotNull MoodleStatus calculateStatus(Player player, PlayerHealthData data) {
        float consciousness = data.getConsciousness();

        if (consciousness < 10) {
            fullyUNC = true;
            return MoodleStatus.CRITICAL;
        } else if (consciousness < 30) {
            fullyUNC = false;
            return MoodleStatus.CRITICAL;
        } else if (consciousness < 55) {
            fullyUNC = false;
            return MoodleStatus.HEAVY;
        } else if (consciousness < 75) {
            fullyUNC = false;
            return MoodleStatus.NORMAL;
        } else if (consciousness < 90) {
            fullyUNC = false;
            return MoodleStatus.LIGHT;
        } else {
            fullyUNC = false;
            return MoodleStatus.NONE;
        }
    }

    @Override
    protected void renderIcon(GuiGraphics ms, float partialTicks, int x, int y) {
        ResourceLocation tex = fullyUNC ? UNC_TEX : TEX;
        ms.blit(tex, x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        List<Component> componentList = new ArrayList<>();
        switch (getMoodleStatus()) {
            case LIGHT -> {
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.consiousness.title1"));
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.consiousness.description1").withStyle(ChatFormatting.GRAY));
            }
            case NORMAL -> {
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.consiousness.title2").withStyle(ChatFormatting.YELLOW));
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.consiousness.description2").withStyle(ChatFormatting.GRAY));
            }
            case HEAVY -> {
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.consiousness.title3").withStyle(ChatFormatting.GOLD));
                componentList.add(Component.translatable("casualties_cubed.gui.moodle.consiousness.description3").withStyle(ChatFormatting.GRAY));
            }
            case CRITICAL -> {
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
