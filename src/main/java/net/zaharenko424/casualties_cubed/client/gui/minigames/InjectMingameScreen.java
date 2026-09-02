package net.zaharenko424.casualties_cubed.client.gui.minigames;

import com.mojang.blaze3d.systems.RenderSystem;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.client.gui.screen.HealthScreen;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import net.zaharenko424.casualties_cubed.network.packet.ServerboundExchangeItemInBagPacket;
import net.zaharenko424.casualties_cubed.network.packet.ServerboundExchangeItemInHandPacket;
import net.zaharenko424.casualties_cubed.network.ModNetwork;
import net.zaharenko424.casualties_cubed.network.packet.ServerboundSyringeFailPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class InjectMingameScreen extends Screen implements Minigame {

    private final Screen parent;
    private final Player target;
    private final ItemStack syringeStack;
    private final Limb limb;
    private double lastpMouseX = this.width / 2.0, lastpMouseY = this.height / 2.0;
    private final ItemStack bagstack;
    private final int slot;

    private SyringeObject syringeObject;
    private final InteractionHand hand;

    private HandObject handObject;

    public InjectMingameScreen(Screen parent, Player target, ItemStack syringeStack, Limb limb, InteractionHand hand) {
        super(Component.translatable("casualties_cubed.gui.minigame.inject"));
        this.parent = parent;
        this.target = target;
        this.syringeStack = syringeStack;
        this.limb = limb;
        this.hand = hand;
        this.bagstack = null;
        slot = -1;
    }

    public InjectMingameScreen(Screen parent, Player target, ItemStack syringeStack, ItemStack bagstack, int slot, Limb limb, InteractionHand hand) {
        super(Component.translatable("casualties_cubed.gui.minigame.inject"));
        this.parent = parent;
        this.target = target;
        this.syringeStack = syringeStack;
        this.limb = limb;
        this.hand = hand;
        this.bagstack = bagstack;
        this.slot = slot;
    }

    public boolean isAmputated() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return false;
        return mc.player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(h -> {
            for (Limb l : limb.availableHandsForAction()) {
                if (!h.isAmputated(l)) {
                    return false;
                }
            }
            return true;
        }).orElse(false);
    }

    public boolean isBothAmputated() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return false;

        return mc.player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(data ->
                data.isAmputated(Limb.LEFT_HAND) && data.isAmputated(Limb.RIGHT_HAND)).orElse(false);
    }

    @Override
    protected void init() {
        super.init();
        HandObject.SpriteType spriteType;
        if (isAmputated()) {
            spriteType = HandObject.SpriteType.GONE;
        } else {
            spriteType = HandObject.SpriteType.NORMAL;
        }
        if (target != minecraft.player) {
            if (isBothAmputated()) {
                spriteType = HandObject.SpriteType.GONE;
            } else {
                spriteType = HandObject.SpriteType.NORMAL;
            }
        }
        handObject = new HandObject(spriteType, this.width / 2.0, this.height / 2.0, this.width, this.height / 3 * 2);
        if (parent instanceof HealthScreen hp) {
            hp.BGmode = true;
        }
        syringeObject = new SyringeObject(this.width / 2, 0, 1f, this.height / 6, hand, (byte) slot);
        syringeObject.setFullness(syringeStack);
        syringeObject.setColor(syringeStack);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        parent.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        pGuiGraphics.fill(0, 0, width, height, 0x88000000);
        pGuiGraphics.fill(0, height / 6 + 158, width, height / 6 + 161, 0xFFFFFFFF);

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

        // Enable scissor
        RenderSystem.enableScissor(scissorX, scissorY, scissorW, scissorH);
        syringeObject.render(pGuiGraphics);
        RenderSystem.disableScissor();

        pGuiGraphics.drawCenteredString(mc.font, Component.translatable("casualties_cubed.gui.syringe_instruction"), this.width / 2, 10, 0xFFFFFF);
        pGuiGraphics.drawCenteredString(mc.font, Component.translatable("casualties_cubed.gui.minigame_exit"), this.width / 2, clipY + 30, 0xFFFFFF);
        pGuiGraphics.renderItem(syringeStack, this.width / 10 - 10, this.height / 10 + 5);
        pGuiGraphics.drawString(mc.font, Component.empty().append(syringeStack.getHoverName()), this.width / 10 + 16, this.height / 10 + 5, 0xFFFFFF);

        handObject.render(pGuiGraphics, pPartialTick);
    }

    @Override
    public void tick() {
        parent.tick();
        handObject.update(lastpMouseX, lastpMouseY);
        syringeObject.mouseDragged(handObject.x, handObject.y, 0);
        syringeObject.update(syringeStack, target, limb);
        if (syringeObject.isSnapped()) {
            handleFail();
        }
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            Optional<Float> cons = player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(PlayerHealthData::consciousness);
            Optional<Float> pain = player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(PlayerHealthData::averagePain);
            float consscale = (cons.orElse(100f) / 100) * 0.15f;
            float painscale = pain.orElse(0f) / 100;
            handObject.setShakeScale(painscale);
            handObject.setStiffness(consscale);
        }
        Minecraft.getInstance().player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
            if (!data.isConscious()) onClose();
        });
        super.tick();
    }

    public void handleFail() {
        ModNetwork.CHANNEL.sendToServer(new ServerboundSyringeFailPacket(target.getId(), limb, hand));
        onClose();
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (handObject.spriteType != HandObject.SpriteType.GONE) {
            syringeObject.mouseClicked(handObject.x, handObject.y, pButton);

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
        syringeObject.setDragging(false);
        handObject.mouseReleased();
        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    @Override
    public void onClose() {
        super.onClose();
        if (bagstack != null) {
            ModNetwork.CHANNEL.sendToServer(new ServerboundExchangeItemInBagPacket(bagstack, slot, syringeStack, hand == InteractionHand.OFF_HAND));
        } else {
            ModNetwork.CHANNEL.sendToServer(new ServerboundExchangeItemInHandPacket(syringeStack, hand == InteractionHand.OFF_HAND));
        }
        syringeObject.stop();
        if (syringeObject.getTickSound() != null) {
            minecraft.getSoundManager().stop(syringeObject.getTickSound());
        }
        if (syringeObject.isSnapped()) {
            Minecraft.getInstance().player.playSound(SoundEvents.GLASS_BREAK, 1f, 1f);
        }
        if (parent instanceof HealthScreen hp) {
            hp.BGmode = false;
        }
        Minecraft.getInstance().setScreen(parent);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
