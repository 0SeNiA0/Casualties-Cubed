package net.zaharenko424.casualties_cubed.client.gui.minigames;

import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.client.gui.HealthScreen;
import net.zaharenko424.casualties_cubed.client.gui.StatusSprites;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class BandageMinigameScreen extends Screen {
    
    private final Screen parent;
    private final Player target;
    private final ItemStack stack;
    private final int slot;
    private final Limb limb;
    private final InteractionHand hand;

    private BandageObject bandageObject;

    private HandObject handObject;

    private float bleedRate = 0;
    private float maxBleed = 0;

    public BandageMinigameScreen(Screen parent, Player target, ItemStack stack, Limb limb, InteractionHand hand) {
        super(Component.literal("BandageMinigame"));
        this.parent = parent;
        this.target = target;
        this.stack = stack;
        this.limb = limb;
        this.hand = hand;
        slot = -1;
    }

    public BandageMinigameScreen(Screen parent, Player target, ItemStack stack, int slot, Limb limb, InteractionHand hand) {
        super(Component.literal("BandageMinigame"));
        this.parent = parent;
        this.target = target;
        this.stack = stack;
        this.limb = limb;
        this.hand = hand;
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
        return mc.player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(h -> {
            if (h.isAmputated(Limb.LEFT_HAND) && h.isAmputated(Limb.RIGHT_HAND)) return true;
            return false;
        }).orElse(false);
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

        handObject = new HandObject(spriteType, this.width / 2, this.height / 2, this.width, this.height / 3 * 2);
        if (parent instanceof HealthScreen hp) {
            hp.BGmode = true;
        }
        bandageObject = new BandageObject(
                0, 0,
                0, 0, 64, 64,
                CasualtiesCubed.resourceLoc("textures/gui/bandage.png"),
                64, 64,
                1f,
                this.width / 2,
                this.height / 2,
                stack,
                slot,
                hand
        );
        maxBleed = target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(PlayerHealthData::getMAX_BLEED_RATE).orElse(1f);
    }

    private double lastpMouseX = 100, lastpMouseY = 100;

    @Override
    public void tick() {
        Optional<Float> BD = target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(data ->
                data.getLimbBleedRate(limb));
        bleedRate = BD.orElse(0f);
        parent.tick();
        handObject.update(lastpMouseX, lastpMouseY);
        bandageObject.update(target, limb);
        bandageObject.mouseDragged(handObject.x, handObject.y, 0);
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            Optional<Float> cons = player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(PlayerHealthData::getConsciousness);
            Optional<Double> pain = player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(PlayerHealthData::getTotalPain);
            float consscale = (cons.orElse(100f) / 100) * 0.15f;
            float painscale = (float) (pain.orElse(0d) / 100);
            handObject.setShakeScale(painscale);
            handObject.setStiffness(consscale);
        }
        Minecraft.getInstance().player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h -> {
            if (h.getConsciousness() <= 10)
                onClose();
        });
        if (bandageObject.isEndCondition()) {
            onClose();
        }
        super.tick();
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (handObject.spriteType != HandObject.SpriteType.GONE) {
            bandageObject.mouseClicked(handObject.x, handObject.y, pButton);
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
        bandageObject.setDragging(false);
        handObject.mouseReleased();
        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    @Override
    public void onClose() {
        super.onClose();

        if (parent instanceof HealthScreen hp) {
            hp.BGmode = false;
        }

        Minecraft.getInstance().setScreen(parent);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        parent.render(guiGraphics, mouseX, mouseY, partialTicks);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        guiGraphics.fill(0, 0, width, height, 0x88000000);
        Minecraft mc = Minecraft.getInstance();
        guiGraphics.blit(CasualtiesCubed.resourceLoc("textures/gui/bandage_center.png"), this.width / 2 - 40, this.height / 2 - 40, 0, 0, 80, 80, 80, 80, 80);

        guiGraphics.drawCenteredString(mc.font, Component.translatable("casualties_cubed.gui.bandage_instruction1"), this.width / 2, 10, 0xFFFFFF);
        guiGraphics.drawCenteredString(mc.font, Component.translatable("casualties_cubed.gui.bandage_instruction2"), this.width / 2, 20, 0xFFFFFF);
        guiGraphics.drawCenteredString(mc.font, Component.translatable("casualties_cubed.gui.minigame_exit"), this.width / 2, this.height / 6 + 190, 0xFFFFFF);

        bandageObject.render(guiGraphics);
        if (bleedRate > 0) {
            float bleedscale = 0.5f + 1.4f * (bleedRate / maxBleed);
            float sizePx = 20 * bleedscale;
            guiGraphics.blit(StatusSprites.BLEED.tex, (int) (width / 2 - sizePx / 2), (int) (height / 2 - sizePx / 2 + 10), 0, 0, (int) sizePx, (int) sizePx, (int) sizePx, (int) sizePx);
        }
        guiGraphics.renderItem(bandageObject.lastStack, this.width / 10 - 10, this.height / 10 + 5);
        guiGraphics.drawString(mc.font, Component.empty().append(bandageObject.lastStack.getHoverName()), this.width / 10 + 16, this.height / 10 + 5, 0xFFFFFF);

        handObject.render(guiGraphics, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
