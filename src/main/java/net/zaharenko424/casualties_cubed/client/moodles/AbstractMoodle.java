package net.zaharenko424.casualties_cubed.client.moodles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;

import java.util.Collections;
import java.util.List;

public abstract class AbstractMoodle {

    private static final long animationDuration = 300; // ms
    private static final float jumpHeight = -7;        // pixels up

    protected final boolean sideMoodle;
    protected final boolean chipRequired;

    private MoodleStatus moodleStatus;
    private boolean critical;

    private long lastJumpTime = -1;

    protected AbstractMoodle() {
        this(false, false);
    }

    protected AbstractMoodle(boolean sideMoodle, boolean chipRequired) {
        this.sideMoodle = sideMoodle;
        this.chipRequired = chipRequired;
    }

    /// Side moodles only appear in health panel
    public final boolean isSideMoodle() {
        return sideMoodle;
    }

    public boolean shouldBeDisplayed(PlayerHealthData data) {
        return !chipRequired || data.chip().isActive();
    }

    public boolean shouldRender() {
        return moodleStatus != null;
    }

    protected void triggerJump() {
        lastJumpTime = System.currentTimeMillis();
    }

    public int getCriticalColor() {
        return 0x66991d1d;
    }

    public int getCriticalEndColor() {
        return 0x00991d1d;
    }

    public abstract void update(Player player, PlayerHealthData data);

    protected void setStatus(MoodleStatus status) {
        setStatus(status, false);
    }

    protected void setStatus(MoodleStatus status, boolean critical) {
        if (moodleStatus == status && this.critical == critical) return;

        moodleStatus = status;
        this.critical = critical;
        triggerJump();
    }

    protected void clearStatus() {
        moodleStatus = null;
    }

    public MoodleStatus getMoodleStatus() {
        return moodleStatus;
    }

    public boolean isMouseOver(int mouseX, int mouseY, int x, int y) {// use square dist to center? only if non-positive
        return mouseX >= x && mouseX < x + 16 &&
                mouseY >= y && mouseY < y + 16;
    }

    public void render(GuiGraphics graphics, float partialTicks, int x, int y) {
        if (moodleStatus == null) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        float time = (mc.level.getGameTime() + partialTicks) / 20f;

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

        if (critical) {
            int color = getCriticalColor();
            int endcolor = getCriticalEndColor();
            float pulse = Mth.sin(time * Mth.PI);

            graphics.fillGradient(x - 2, (int) (finaly - 20 + (10 * pulse)), x + 18, finaly + 3, endcolor, color);
        }

        graphics.blit(moodleStatus.tex, x - 2, finaly - 2, 20, 20, 0, 0, 40, 40, 40, 40);

        renderIcon(graphics, partialTicks, x, finaly);
    }

    protected abstract void renderIcon(GuiGraphics graphics, float partialTicks, int x, int y);

    public List<Component> getTooltip(Player player) {
        return Collections.emptyList();
    }
}
