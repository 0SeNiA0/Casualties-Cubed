package net.zaharenko424.casualties_cubed.client.gui.minigames;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.client.gui.StatusSprites;
import net.zaharenko424.casualties_cubed.client.gui.widget.RenderableImage;
import net.zaharenko424.casualties_cubed.item.api.AbstractBandage;
import net.zaharenko424.casualties_cubed.item.api.IBag;
import net.zaharenko424.casualties_cubed.item.api.INbtDrivenDurability;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.LimbStatistics;
import net.zaharenko424.casualties_cubed.network.ModNetwork;
import net.zaharenko424.casualties_cubed.network.packet.ServerboundUseBandagePacket;
import net.zaharenko424.casualties_cubed.registry.ModItems;
import net.zaharenko424.casualties_cubed.registry.ModSounds;
import net.zaharenko424.casualties_cubed.util.Util;
import org.joml.Vector2f;

public class BandageMinigameScreen extends MinigameScreen {

    private static final ResourceLocation BANDAGE_TEX = CasualtiesCubed.resourceLoc("textures/gui/bandage.png");
    private static final ResourceLocation CENTER_TEX = CasualtiesCubed.resourceLoc("textures/gui/bandage_center.png");
    private static final float radius = 70;

    private ItemStack stack;
    private final float maxDurability;
    private float lastDurability;
    private ItemStack lastStack;
    private final AbstractBandage bandage;

    private final int bagSlot;
    private final Limb limb;
    private final InteractionHand usedHand;

    private float bandageAngle = 0;
    private float lastActionAngle = 0;
    private boolean didWrap;
    private boolean dragging;
    private float sizeMultiplier = 0.9f;//wrap size

    private float bleedRate = 0;

    private final RenderableImage bandageRoll = new RenderableImage(BANDAGE_TEX, 64, 64);

    public BandageMinigameScreen(Screen parent, Player target, ItemStack stack, Limb limb, InteractionHand hand) {
        this(parent, target, stack, -1, limb, hand);
    }

    public BandageMinigameScreen(Screen parent, Player target, ItemStack stack, int slot, Limb limb, InteractionHand hand) {
        super(Component.translatable("casualties_cubed.gui.minigame.bandage"), parent, target);

        this.stack = stack;
        lastStack = stack;
        bandage = (AbstractBandage) stack.getItem();
        maxDurability = bandage.getMaxNbtDurability(stack);
        lastDurability = bandage.getNbtDurability(stack);

        this.limb = limb;
        this.usedHand = hand;
        this.bagSlot = slot;

        float f = 1 - (1 - lastDurability / 100) * (1 - lastDurability / 100);
        angleToVec(bandageAngle - 90, Util.REUSABLE_2F);
        Util.REUSABLE_2F.mul(Mth.lerp(f, radius / 2 + 4, radius)).add(width / 2f, height / 2f);

        bandageRoll.offset.set(Util.REUSABLE_2F, 0);
        bandageRoll.scale.set(1 - (1 - lastDurability / 100) * (1 - lastDurability / 100));
        bandageRoll.tint = bandage.getColor();
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
        return mc.player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(h ->
                h.isAmputated(Limb.LEFT_HAND) && h.isAmputated(Limb.RIGHT_HAND)).orElse(false);
    }

    @Override
    protected void init() {
        super.init();

        if (isAmputated()) {
            spriteType = SpriteType.GONE;
        } else {
            spriteType = SpriteType.NORMAL;
        }
        if (target != minecraft.player) {
            if (isBothAmputated()) {
                spriteType = SpriteType.GONE;
            } else {
                spriteType = SpriteType.NORMAL;
            }
        }
    }

    @Override
    public void tick() {
        stack = Minecraft.getInstance().player.getItemInHand(usedHand);// Grab updated bandage stack or fail trying

        if (stack.getItem() instanceof IBag bag) {
            if (bagSlot == -1 || bagSlot >= bag.size()) {
                onClose();
                return;
            }

            stack = bag.getItem(stack, bagSlot);
        }

        if (stack.getItem() != bandage) {
            onClose();
            return;
        }

        if (!ItemStack.isSameItemSameTags(lastStack, stack)) {
            lastStack = stack;
            lastDurability = bandage.getNbtDurability(stack);
        }

        if (lastDurability < 0) {
            onClose();
            return;
        }

        super.tick();
        handVelocity.lerp(new Vector2f(), 34 * 1/*bandage speed mult*/ * Util.TICK_TO_SEC);

        bleedRate = target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(data ->
                data.getLimb(limb).getBleedRate()).orElse(0f);

        dragBandage();
        update();

        Minecraft.getInstance().player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
            if (!data.isConscious()) onClose();
        });
    }

    float angleFromVec(Vector2f vec) {
        float num = (float) (Math.atan2(vec.x, vec.y) * Mth.RAD_TO_DEG);
        if (num < 0f) {
            num += 360f;
        }
        return num;
    }

    void angleToVec(float angle, Vector2f vec) {
        angle *= Mth.DEG_TO_RAD;
        vec.set(Mth.cos(angle), Mth.sin(angle));
    }

    void dragBandage() {
        if (dragging) {
            handPosO.lerp(handPos, Minecraft.getInstance().getDeltaFrameTime(), Util.REUSABLE_2F).mul(1, -1).normalize();
            float newAngle = angleFromVec(Util.REUSABLE_2F);
            float newBandageAngle = newAngle - bandageAngle;
            float newLastActionAngle = newAngle - lastActionAngle;
            if (newBandageAngle < 20) {
                if (bandageAngle > 350 && (newAngle < 90 || newAngle == 360)) {
                    bandageAngle -= 360;
                    lastActionAngle -= 360;
                    didWrap = true;
                    Minecraft.getInstance().player.playSound(ModSounds.BANDAGE_USE.get());
                    doBandageAction();
                } else if (newLastActionAngle > 20) {
                    doBandageAction();
                }

                bandageAngle = newAngle;
            }
        }

        if (handPos.lengthSquared() < 36 * 36) {
            handVelocity.add(handPos.normalize(Util.REUSABLE_2F).mul(Util.TICK_TO_SEC * 25));
            handPos.normalize(36);
            handPosO.set(handPos);
        }
    }

    void update() {
        if (isClicking) {
            angleToVec(bandageAngle - 90, Util.REUSABLE_2F);
            dragging = Util.REUSABLE_2F.dot(handPos.normalize(new Vector2f())) > 0.9f;
        } else {
            dragging = false;
        }

        float rollScale = 1 - (1 - lastDurability / 100) * (1 - lastDurability / 100);

        angleToVec(bandageAngle - 90, Util.REUSABLE_2F);
        Util.REUSABLE_2F.mul(Mth.lerp(rollScale, radius / 2 + 4, radius)).add(width / 2f, height / 2f);

        bandageRoll.offset.set(Util.REUSABLE_2F, 0);
        bandageRoll.rotation.identity().rotateXYZ(0, 0, bandageAngle * Mth.DEG_TO_RAD * 3);
        bandageRoll.scale.set(rollScale);
    }

    void doBandageAction() {
        ModNetwork.CHANNEL.sendToServer(new ServerboundUseBandagePacket(target.getId(), limb, usedHand, (byte) bagSlot));
        lastActionAngle += 20;
        if (didWrap) {
            sizeMultiplier += 0.0015f;
        }
        lastDurability -= ModItems.DRESSING.get().durabilityScale(AbstractBandage.ANGLE_PER_PACKET);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);

        Minecraft mc = Minecraft.getInstance();
        graphics.blit(CENTER_TEX, this.width / 2 - 40, this.height / 2 - 40, 0, 0, 80, 80, 80, 80, 80);

        graphics.drawCenteredString(mc.font, Component.translatable("casualties_cubed.gui.bandage_instruction1"), this.width / 2, 10, 0xFFFFFF);
        graphics.drawCenteredString(mc.font, Component.translatable("casualties_cubed.gui.bandage_instruction2"), this.width / 2, 20, 0xFFFFFF);
        graphics.drawCenteredString(mc.font, Component.translatable("casualties_cubed.gui.minigame_exit"), this.width / 2, this.height / 6 + 190, 0xFFFFFF);

        bandageRoll.render(graphics, mouseX, mouseY, partialTick);

        PoseStack stack = graphics.pose();
        stack.pushPose();
        stack.translate(bandageRoll.offset.x, bandageRoll.offset.y, bandageRoll.offset.z);
        stack.mulPose(bandageRoll.rotation);
        stack.scale(bandageRoll.scale.x, bandageRoll.scale.y, bandageRoll.scale.z);

        graphics.fill(-1, -1, 1, 1, -1);
        stack.popPose();

        if (bleedRate > 0) {
            float bleedscale = 0.5f + 1.4f * (bleedRate / LimbStatistics.MAX_BLEED_RATE);
            float sizePx = 20 * bleedscale;
            graphics.blit(StatusSprites.BLEED.tex, (int) (width / 2f - sizePx / 2), (int) (height / 2f - sizePx / 2 + 10), 0, 0, (int) sizePx, (int) sizePx, (int) sizePx, (int) sizePx);
        }
        graphics.renderItem(lastStack, this.width / 10 - 10, this.height / 10 + 5);

        Component comp = INbtDrivenDurability.appendDurability(lastDurability / maxDurability, Component.translatable(lastStack.getDescriptionId()));
        graphics.drawString(mc.font, comp, this.width / 10 + 16, this.height / 10 + 5, 0xFFFFFF);

        renderHand(graphics, partialTick);
    }
}
