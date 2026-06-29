package net.zaharenko424.casualties_cubed.client.moodles;

import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.limbs.ChipState;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public abstract class AbstractMoodleVisual {

    private static final ResourceLocation RING_TEX = CasualtiesCubed.resourceLoc("textures/gui/moodles/moodle_ring.png");
    private static final ResourceLocation SQUARE_TEX = CasualtiesCubed.resourceLoc("textures/gui/moodles/moodle_square.png");

    private MoodleStatus moodleStatus;
    private MoodleStatus lastStatus;
    private int sinceChange = 0;

    private long lastJumpTime = -1;
    private static final long animationDuration = 300; // ms
    private static final float jumpHeight = -7;        // pixels up

    /// Side moodles only appear in health panel
    public boolean isSideMoodle() {
        return false;
    }

    public boolean shouldBeDisplayed(ChipState state) {
        return true;
    }

    public void triggerJump() {
        lastJumpTime = System.currentTimeMillis();
    }

    public List<Component> getTooltip(Player player) {
        return Collections.emptyList();
    }

    public boolean isMouseOver(int mouseX, int mouseY, int x, int y) {
        return mouseX >= x && mouseX < x + 16 &&
                mouseY >= y && mouseY < y + 16;
    }

    public void render(GuiGraphics ms, float partialTicks, int x, int y) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        float time = (mc.level.getGameTime() + partialTicks) / 20f;

        if (lastStatus != getMoodleStatus()) {
            if (sinceChange++ > 5) {
                sinceChange = 0;
                lastStatus = getMoodleStatus();
                triggerJump();
            }
        }

        float animatedOffset = 0;
        if (lastJumpTime > 0) {
            long elapsed = System.currentTimeMillis() - lastJumpTime;
            if (elapsed < animationDuration) {
                float t = (float) elapsed / animationDuration;
                animatedOffset = jumpHeight * (1.0f - t * t); // easing
            } else {
                lastJumpTime = -1; // finished
            }
        }

        int finaly = y + (int) animatedOffset;

        if (moodleStatus == MoodleStatus.CRITICAL_NEG) {
            int color = getCriticalColor();
            int endcolor = getCriticalEndColor();
            float pulse = Mth.sin(time * Mth.PI);

            ms.fillGradient(x + 1, (int) (finaly - 20 + (10 * pulse)), x + 15, finaly + 3, endcolor, color);
        }

        renderBackground(ms, partialTicks, x, finaly);
        renderIcon(ms, partialTicks, x, finaly);
    }

    public boolean shouldRender() {
        return moodleStatus != MoodleStatus.NONE;
    }

    public int getCriticalColor() {
        return 0x66991d1d;
    }

    public int getCriticalEndColor() {
        return 0x00991d1d;
    }

    public void update(Player player, PlayerHealthData data) {
        moodleStatus = calculateStatus(player, data);
    }

    protected abstract @NotNull MoodleStatus calculateStatus(Player player, PlayerHealthData data);

    public MoodleStatus getMoodleStatus() {
        return moodleStatus;
    }

    public void renderBackground(GuiGraphics guiGraphics, float partialTicks, int x, int y) {
        if (moodleStatus == null) return;

        guiGraphics.blit(moodleStatus.tex, x, y, 0, 0, 16, 16, 16, 16);
        guiGraphics.blit(moodleStatus.positive ? SQUARE_TEX : RING_TEX, x - 2, y - 2, 20, 20, 0, 0, 40, 40, 40, 40);
    }

    protected abstract void renderIcon(GuiGraphics graphics, float partialTicks, int x, int y);
}
