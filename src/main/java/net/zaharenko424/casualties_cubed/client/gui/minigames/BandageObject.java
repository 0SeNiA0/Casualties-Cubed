package net.zaharenko424.casualties_cubed.client.gui.minigames;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.zaharenko424.casualties_cubed.client.gui.RenderableImage;
import net.zaharenko424.casualties_cubed.item.api.AbstractBandage;
import net.zaharenko424.casualties_cubed.item.api.IBag;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.network.ModNetwork;
import net.zaharenko424.casualties_cubed.network.packet.ServerboundUseBandagePacket;
import net.zaharenko424.casualties_cubed.registry.ModItems;
import net.zaharenko424.casualties_cubed.registry.ModSounds;
import org.joml.Vector2f;

public class BandageObject extends GrabObject {

    private final int centerX;
    private final int centerY;
    private final float radius = 70;

    float sizeMultiplier = 0.9f;//wrap size

    private ItemStack itemStack;

    private boolean EndCondition = false;

    float maxDurability;
    float lastDurability;
    ItemStack lastStack;
    final AbstractBandage bandage;

    int bagSlot;
    InteractionHand usedHand;

    Vector2f vec = new Vector2f();
    float bandageAngle = 0;
    float lastActionAngle = 0;
    boolean didWrap;

    final RenderableImage bandageRoll = new RenderableImage(tex, 64, 64);

    public boolean isEndCondition() {
        return EndCondition;
    }

    public BandageObject(int x, int y, int hitX, int hitY, int hitWidth, int hitHeight, ResourceLocation tex, int texWidth, int texHeight, float scale, int centerX, int centerY, ItemStack stack, int bagSlot, InteractionHand hand, Player target, Limb limb) {
        super(x, y, hitX, hitY, hitWidth, hitHeight, tex, texWidth, texHeight, scale);
        this.centerX = centerX;
        this.centerY = centerY;
        this.itemStack = stack;

        bandage = (AbstractBandage) stack.getItem();
        maxDurability = bandage.getMaxNbtDurability(stack);
        lastDurability = bandage.getNbtDurability(stack);
        lastStack = stack;

        float f = 1 - (1 - lastDurability / 100) * (1 - lastDurability / 100);

        angleToVec(bandageAngle - 90, vec);
        vec.mul(Mth.lerp(f, radius / 2 + 4, radius)).add(centerX, centerY);

        this.x = (int) vec.x;
        this.y = (int) vec.y;

        this.bagSlot = bagSlot;
        this.usedHand = hand;

        this.target = target;
        this.limb = limb;

        bandageRoll.offset.set(vec, 0);
        bandageRoll.scale.set(1 - (1 - lastDurability / 100) * (1 - lastDurability / 100));

        bandageRoll.tint = bandage.getColor();
    }

    private final Player target;
    private final Limb limb;

    public void update(){//TODO move following to upper level?
        itemStack = Minecraft.getInstance().player.getItemInHand(usedHand);// Grab updated bandage stack or fail trying

        if (itemStack.getItem() instanceof IBag bag) {
            if (bagSlot == -1 || bagSlot >= bag.size()) {
                EndCondition = true;
                return;
            }

            itemStack = bag.getItem(itemStack, bagSlot);
        }

        if (itemStack.getItem() != bandage) {
            EndCondition = true;
            return;
        }

        if (!ItemStack.isSameItemSameTags(lastStack, itemStack)) {
            lastStack = itemStack;
            lastDurability = bandage.getNbtDurability(itemStack);
        }

        if (lastDurability < 0) EndCondition = true;
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

    @Override
    public void mouseDragged(double mouseX, double mouseY, int button) {
        if (!dragging || button != 0) return;

        vec.set(mouseX - centerX, centerY - mouseY).normalize();
        float newAngle = angleFromVec(vec);
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

        float rollScale = 1 - (1 - lastDurability / 100) * (1 - lastDurability / 100);

        angleToVec(bandageAngle - 90, vec);
        vec.mul(Mth.lerp(rollScale, radius / 2 + 4, radius)).add(centerX, centerY);

        this.x = (int) vec.x;
        this.y = (int) vec.y;

        bandageRoll.offset.set(vec, 0);
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
    public boolean isInside(double mouseX, double mouseY) {
        return Vector2f.distanceSquared((float) mouseX, (float) mouseY, bandageRoll.offset.x, bandageRoll.offset.y) <= Math.pow(bandageRoll.scale.x * 32, 2);
    }

    @Override
    public void render(GuiGraphics guiGraphics) {
        bandageRoll.render(guiGraphics, 0, 0, 0);
    }
}
