package net.zaharenko424.casualties_cubed.client.moodles;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;

import java.util.List;

public class IrradiatedMoodle extends AbstractMoodle {

    private static final ResourceLocation TEX = CasualtiesCubed.resourceLoc("textures/gui/moodles/irradiated.png");

    public IrradiatedMoodle() {
        super(false, true);
    }

    @Override
    public void update(Player player, PlayerHealthData data) {
        float radiationSickness = data.radiationSickness() * 0.3f;

        if (radiationSickness > 24) {
            setStatus(MoodleStatus.CRITICAL_NEG, true);
        } else if (radiationSickness > 15) {
            setStatus(MoodleStatus.HEAVY_NEG, true);
        } else if (radiationSickness > 9) {
            setStatus(MoodleStatus.NORMAL_NEG);
        } else if (radiationSickness > 3) {
            setStatus(MoodleStatus.LIGHT_NEG);
        } else clearStatus();
    }

    @Override
    protected void renderIcon(GuiGraphics graphics, float partialTicks, int x, int y) {
        graphics.blit(TEX, x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Player player) {
        return switch (getMoodleStatus()) {
            case LIGHT_NEG ->
                    List.of(Component.translatable("gui.casualties_cubed.moodle.irradiated.title1"),
                            Component.translatable("gui.casualties_cubed.moodle.irradiated.description1"));
            case NORMAL_NEG ->
                    List.of(Component.translatable("gui.casualties_cubed.moodle.irradiated.title2"),
                            Component.translatable("gui.casualties_cubed.moodle.irradiated.description2"));
            case HEAVY_NEG ->
                    List.of(Component.translatable("gui.casualties_cubed.moodle.irradiated.title3"),
                            Component.translatable("gui.casualties_cubed.moodle.irradiated.description3"));
            case CRITICAL_NEG ->
                    List.of(Component.translatable("gui.casualties_cubed.moodle.irradiated.title4"),
                            Component.translatable("gui.casualties_cubed.moodle.irradiated.description4"));
            default -> List.of();
        };
    }
}
