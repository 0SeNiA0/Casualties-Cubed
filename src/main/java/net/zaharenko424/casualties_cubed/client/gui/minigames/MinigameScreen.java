package net.zaharenko424.casualties_cubed.client.gui.minigames;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.client.gui.screen.HealthScreen;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import net.zaharenko424.casualties_cubed.util.Util;
import org.joml.Vector2f;

public abstract class MinigameScreen extends Screen implements Minigame {

    private final Screen parent;

    protected final Player target;

    protected final Vector2f handPosO = new Vector2f();
    protected final Vector2f handPos = new Vector2f();
    protected final Vector2f handVelocity = new Vector2f();

    protected SpriteType spriteType;
    protected boolean isClicking;

    protected MinigameScreen(Component title, Screen parent, Player target) {
        super(title);
        this.parent = parent;
        this.target = target;

        //GLFW.glfwSetInputMode(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
    }

    @Override
    protected void init() {
        if (parent instanceof HealthScreen hp) {
            hp.BGmode = true;
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        parent.render(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.fill(0, 0, width, height, 0x88000000);
    }

    @Override
    public void tick() {
        parent.tick();

        float consciousness = PlayerHealthData.of(target).map(PlayerHealthData::getConsciousness).orElse(0f);
        float f = 75;
        float ff = 0.25f;
        float fff = 4;//+ skill
        float ffff = 1.5f;
        ff *= consciousness * 0.01f;
        fff *= consciousness * 0.01f;

        MouseHandler handler = Minecraft.getInstance().mouseHandler;
        Window window = Minecraft.getInstance().getWindow();
        Vector2f vec = new Vector2f((float) handler.xpos() * window.getGuiScaledWidth() / window.getScreenWidth() - width / 2f,
                                    (float) handler.ypos() * window.getGuiScaledHeight() / window.getScreenHeight() - height / 2f)
                .sub(handPos).mul(ff);
        Util.clampLength(vec, f * 0.5f);

        handVelocity.lerp(vec, fff * Util.TICK_TO_SEC);
        handVelocity.lerp(Util.REUSABLE_2F.set(0), ffff * Util.TICK_TO_SEC);
        handPosO.set(handPos);
        handPos.add(handVelocity.mul(120 * Util.TICK_TO_SEC, vec));
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        isClicking = true;
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        isClicking = false;
        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    @Override
    public void onClose() {
        super.onClose();

        if (parent instanceof HealthScreen hp) {
            hp.BGmode = false;
        }

        Minecraft.getInstance().setScreen(parent);
        //GLFW.glfwSetInputMode(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
    }

    protected void renderHand(GuiGraphics graphics, float partialTick) {
        PoseStack pose = graphics.pose();
        pose.pushPose();

        Vector2f vec = handPosO.lerp(handPos, partialTick, Util.REUSABLE_2F);
        pose.translate(vec.x + width / 2f, vec.y + height / 2f, 0);
        vec.sub(width, height / 3f * 2);
        pose.mulPose(Axis.ZP.rotation((float) Mth.atan2(vec.y, vec.x)));

        switch (spriteType){
            case NORMAL -> {
                if (isClicking){
                    graphics.blit(
                            CasualtiesCubed.resourceLoc( "textures/gui/limbs/arm_click.png"),
                            -160, -32, 0, 0, 192, 64, 192, 64
                    );
                }else {
                    graphics.blit(
                            CasualtiesCubed.resourceLoc( "textures/gui/limbs/arm.png"),
                            -160, -32, 0, 0, 192, 64, 192, 64
                    );
                }
            }
            case TWEEZERS -> {
                if (isClicking){
                    graphics.blit(
                            CasualtiesCubed.resourceLoc( "textures/gui/limbs/arm_click_tweezers.png"),
                            -188, -53, 0, 0, 192, 64, 192, 64
                    );
                }else {
                    graphics.blit(
                            CasualtiesCubed.resourceLoc( "textures/gui/limbs/arm_tweezers.png"),
                            -188, -53, 0, 0, 192, 64, 192, 64
                    );
                }
            }
            case GONE ->
                    graphics.blit(CasualtiesCubed.resourceLoc( "textures/gui/limbs/arm_broken.png"),
                    -160, -32, 0, 0, 192, 64, 192, 64
            );
            case SAW ->
                    graphics.blit(CasualtiesCubed.resourceLoc( "textures/gui/limbs/arm_saw.png"),
                    -160, -32, 0, 0, 192, 64, 192, 64
            );
        }

        pose.popPose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    protected enum SpriteType {
        NORMAL,
        TWEEZERS,
        GONE,
        SAW
    }
}
