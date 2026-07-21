package net.zaharenko424.casualties_cubed.client.gui.minigames;

import com.mojang.blaze3d.systems.RenderSystem;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.client.gui.HealthScreen;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import net.zaharenko424.casualties_cubed.network.ModNetwork;
import net.zaharenko424.casualties_cubed.network.packet.ServerboundAdjustShrapnelPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShrapnelMinigameScreen extends Screen {

    private final Screen parent;
    private final Player target;
    private final Limb limb;
    private double lastpMouseX = this.width / 2f, lastpMouseY = this.height / 2f;
    private final boolean ignorevel;
    private int RememberShrapnel;

    private final List<ShrapnelObject> shrapnelObjects = new ArrayList<>();
    private final List<Integer> xlists = new ArrayList<>();

    private HandObject handObject;

    public ShrapnelMinigameScreen(Screen parent, Player target, Limb limb, boolean ignorevel) {
        super(Component.translatable("casualties_cubed.gui.minigame.shrapnel"));
        this.parent = parent;
        this.target = target;
        this.limb = limb;
        this.ignorevel = ignorevel;
    }

    public boolean isAmputated() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return false;
        if (target != mc.player) {
            return mc.player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(data ->
                    data.isAmputated(Limb.LEFT_HAND) && data.isAmputated(Limb.RIGHT_HAND)).orElse(false);
        } else {
            return mc.player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(data -> {
                for (Limb l : limb.availableHandsForAction()) {
                    if (!data.isAmputated(l)) {
                        return false;
                    }
                }
                return true;
            }).orElse(false);
        }
    }

    @Override
    protected void init() {
        super.init();
        HandObject.SpriteType spriteType;
        if (ignorevel) {
            spriteType = HandObject.SpriteType.TWEEZERS;
        } else if (isAmputated()) {
            spriteType = HandObject.SpriteType.GONE;
        } else {
            spriteType = HandObject.SpriteType.NORMAL;
        }
        handObject = new HandObject(spriteType, this.width / 2f, this.height / 2f, this.width, this.height / 3 * 2);
        if (parent instanceof HealthScreen hp) {
            hp.BGmode = true;
        }
        int ShrapnelAmount = target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(data -> data.getLimb(limb).getShrapnel()).orElse(0);
        RememberShrapnel = ShrapnelAmount;
        int x = this.width / 2 - 16;
        shrapnelObjects.clear();
        xlists.clear();
        for (int i = 0; i < ShrapnelAmount; i++) {
            int passX = x;
            if (i % 2 == 0) {
                passX -= 48 * i;
            } else {
                passX += 48 * i;
            }
            xlists.add(passX);
            shrapnelObjects.add(
                    new ShrapnelObject(passX, (int) (height / 6f + 145 + ((Math.random() * 2 - 0.5f) * 5)), this.height / 6, this.height / 6 + 150, target, limb)
            );
        }
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        parent.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        pGuiGraphics.fill(0, 0, width, height, 0x88000000);
        pGuiGraphics.fill(0, height / 6 + 158, width, height / 6 + 162, 0xFFFFFFFF);

        Minecraft mc = Minecraft.getInstance();
        int screenHeight = mc.getWindow().getScreenHeight();
        int screenWidth = mc.getWindow().getScreenWidth();

        double guiScaleX = (double) screenWidth / (double) this.width;
        double guiScaleY = (double) screenHeight / (double) this.height;

        int clipY = this.height / 6 + 161;

        int scissorX = 0;
        int scissorY = (int) (screenHeight - (clipY * guiScaleY));
        int scissorW = (int) (this.width * guiScaleX);
        int scissorH = (int) ((clipY) * guiScaleY);

        for (Integer i : xlists) {
            pGuiGraphics.blit(CasualtiesCubed.resourceLoc("textures/gui/limbs/blood_decal.png"), i, height / 6 + 157, 0, 0, 32, 5, 32, 5);
        }

        // Enable scissor
        RenderSystem.enableScissor(scissorX, scissorY, scissorW, scissorH);

        for (ShrapnelObject shrapnelObject : shrapnelObjects) {
            shrapnelObject.render(pGuiGraphics);
        }

        RenderSystem.disableScissor();

        pGuiGraphics.drawCenteredString(mc.font, Component.translatable("casualties_cubed.gui.shrapnel_instruction"), this.width / 2, 10, 0xFFFFFF);
        pGuiGraphics.drawCenteredString(mc.font, Component.translatable("casualties_cubed.gui.minigame_exit"), this.width / 2, clipY + 30, 0xFFFFFF);

        handObject.render(pGuiGraphics, pPartialTick);
    }


    private boolean IgnoreResult = false;

    @Override
    public void tick() {
        parent.tick();
        handObject.update(lastpMouseX, lastpMouseY);
        float yVel = (float) Math.abs(handObject.getVy());
        for (ShrapnelObject shrapnelObject : shrapnelObjects) {
            shrapnelObject.mouseDragged(handObject.x, handObject.y, 0);
            shrapnelObject.update(yVel, ignorevel);
        }

        Player player = Minecraft.getInstance().player;
        if (player != null) {
            Optional<Float> cons = player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(PlayerHealthData::getConsciousness);
            Optional<Double> pain = player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(PlayerHealthData::getAveragePain);
            float consscale = (cons.orElse(100f) / 100) * 0.15f;
            float painscale = (float) (pain.orElse(0d) / 100) * 0.55f;
            handObject.setShakeScale(painscale);
            handObject.setStiffness(consscale);
        }
        Minecraft.getInstance().player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
            if (data.getConsciousness() <= 10)
                onClose();
        });

        target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
            if (data.getLimb(limb).getShrapnel() != RememberShrapnel) {
                IgnoreResult = true;
                onClose();
            }
        });
        super.tick();
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (handObject.spriteType != HandObject.SpriteType.GONE) {
            for (ShrapnelObject shrapnelObject : shrapnelObjects) {
                shrapnelObject.mouseClicked(handObject.x, handObject.y, 0);
            }

            handObject.mouseClicked();
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }


    @Override
    public void mouseMoved(double pMouseX, double pMouseY) {
        lastpMouseY = pMouseY;
        lastpMouseX = pMouseX;
        super.mouseMoved(pMouseX, pMouseY);
    }


    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        for (ShrapnelObject shrapnelObject : shrapnelObjects) {
            shrapnelObject.setDragging(false);
        }
        handObject.mouseReleased();
        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    @Override
    public void onClose() {
        super.onClose();
        if (parent instanceof HealthScreen hp) {
            hp.BGmode = false;
        }
        if (!IgnoreResult) {
            int shrapnellLeft = (int) shrapnelObjects.stream().filter(ShrapnelObject::isSticked).count();
            ModNetwork.CHANNEL.sendToServer(new ServerboundAdjustShrapnelPacket(target.getId(), limb, shrapnellLeft));
        }
        Minecraft.getInstance().setScreen(parent);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
