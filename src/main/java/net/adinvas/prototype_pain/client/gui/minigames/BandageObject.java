package net.adinvas.prototype_pain.client.gui.minigames;

import com.mojang.math.Axis;
import net.adinvas.prototype_pain.item.api.IBag;
import net.adinvas.prototype_pain.item.api.IBandage;
import net.adinvas.prototype_pain.item.bandages.PlasticDressingItem;
import net.adinvas.prototype_pain.item.bandages.SterilizedDressingItem;
import net.adinvas.prototype_pain.limbs.Limb;
import net.adinvas.prototype_pain.network.ModNetwork;
import net.adinvas.prototype_pain.network.packet.ServerboundUseBandagePacket;
import net.adinvas.prototype_pain.registry.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class BandageObject extends GrabObject {

    private final int centerX;
    private final int centerY;
    private final float radius = 70;

    private float angle = 0;
    private float rotation = 0f;    // sprite roll
    private float scaleFactor = 1f; // shrink as it rolls

    private ItemStack itemStack;

    private boolean EndCondition = false;

    private float maxDurability;
    private float lastDurability;
    private ItemStack lastStack;
    private float durabilitySincePacket = 0;

    int bagSlot;
    InteractionHand usedHand;

    public boolean isEndCondition() {
        return EndCondition;
    }

    public BandageObject(int x, int y, int hitX, int hitY, int hitWidth, int hitHeight, ResourceLocation tex, int texWidth, int texHeight, float scale, int centerX, int centerY, ItemStack stack, int bagSlot, InteractionHand hand) {
        super(x, y, hitX, hitY, hitWidth, hitHeight, tex, texWidth, texHeight, scale);
        this.centerX = centerX;
        this.centerY = centerY;
        this.itemStack = stack;

        IBandage bandage = (IBandage) stack.getItem();
        maxDurability = bandage.getMaxNbtDurability(stack);
        lastDurability = bandage.getNbtDurability(stack);
        lastStack = stack;
        calculateScale();

        float scaleoffsetX = (float) (Math.cos(angle) * (-texWidth / 2f * (1 - scaleFactor)));
        float scaleoffsetY = (float) (Math.sin(angle) * (-texHeight / 2f * (1 - scaleFactor)));

        this.x = (int) (centerX + scaleoffsetX + Math.cos(angle) * radius - texWidth * scale / 2f);
        this.y = (int) (centerY + scaleoffsetY + Math.sin(angle) * radius - texHeight * scale / 2f);

        this.bagSlot = bagSlot;
        this.usedHand = hand;
    }

    private int tickCounter = 0;

    public void update(Player target, Limb limb){//TODO move following to upper level?
        itemStack = Minecraft.getInstance().player.getItemInHand(usedHand);// Grab updated bandage stack or fail trying

        if (itemStack.getItem() instanceof IBag bag) {
            if (bagSlot == -1 || bagSlot >= bag.size()) {
                EndCondition = true;
                return;
            }

            itemStack = bag.getItem(itemStack, bagSlot);
        }

        if (!(itemStack.getItem() instanceof IBandage bandage)) {
            EndCondition = true;
            return;
        }

        if (!ItemStack.isSameItemSameTags(lastStack, itemStack)) {
            lastStack = itemStack;
            maxDurability = bandage.getMaxNbtDurability(itemStack);// Update only if new stack != old stack
            lastDurability = bandage.getNbtDurability(itemStack);
        }

        if (lastDurability <= durabilitySincePacket) {
            ModNetwork.CHANNEL.sendToServer(new ServerboundUseBandagePacket(target.getId(), limb, usedHand, (byte) bagSlot, lastDurability));
            EndCondition = true;
            return;
        }

        if (tickCounter++ > 5 && !EndCondition) {
            tickCounter = 0;
            ModNetwork.CHANNEL.sendToServer(new ServerboundUseBandagePacket(target.getId(), limb, usedHand, (byte) bagSlot, durabilitySincePacket));
            lastDurability = lastDurability - durabilitySincePacket;
            durabilitySincePacket = 0;
        }
    }

    protected void calculateScale() {
        scaleFactor = 1.0f - 0.7f * (1 - (lastDurability - durabilitySincePacket) / maxDurability);
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int button) {
        if (!dragging || button != 0) return;

        // Compute mouse angle relative to circle center
        double dx = mouseX - centerX;
        double dy = mouseY - centerY;
        float newAngle = (float) Math.atan2(dy, dx);

        // Compute angular difference
        float diff = newAngle - angle;

        // Normalize to -π..π range
        while (diff < -Mth.PI) diff += Mth.TWO_PI;
        while (diff > Mth.PI) diff -= Mth.TWO_PI;

        // Allow only clockwise motion (negative diff = CCW)
        if (diff > 0) {
            float durabilityneg = ((Mth.RAD_TO_DEG * diff) / 360) * 10;
            durabilitySincePacket += durabilityneg;
            angle += diff;
            rotation += diff * 6f; // spin effect multiplier
        }

        // Wrap around full circle
        if (angle > Mth.TWO_PI) {
            Minecraft.getInstance().player.playSound(ModSounds.BANDAGE_USE.get());
            angle -= Mth.TWO_PI;
        }

        calculateScale();

        // Update position along circle
        float scaleoffsetX = Mth.cos(angle) * (-texWidth / 2f * (1 - scaleFactor));
        float scaleoffsetY = Mth.sin(angle) * (-texHeight / 2f * (1 - scaleFactor));

        this.x = (int) (centerX + scaleoffsetX + Mth.cos(angle) * radius - texWidth * scale / 2f);
        this.y = (int) (centerY + scaleoffsetY + Mth.sin(angle) * radius - texHeight * scale / 2f);
    }

    @Override
    public void render(GuiGraphics guiGraphics) {
        var pose = guiGraphics.pose();
        pose.pushPose();

        calculateScale();

        // Move to center of sprite for rotation
        pose.translate(x + texWidth * scale / 2f, y + texHeight * scale / 2f, 0);
        pose.mulPose(Axis.ZP.rotation(rotation / 2));
        pose.scale(scale * scaleFactor, scale * scaleFactor, 1f);
        pose.translate(-texWidth / 2f, -texHeight / 2f, 0);

        if (itemStack.getItem() instanceof PlasticDressingItem) {
            guiGraphics.setColor(0.5f, 0.5f, 1f, 1);
        } else if (itemStack.getItem() instanceof SterilizedDressingItem) {
            guiGraphics.setColor(0.6f, 0.6f, 0.6f, 1);
        }

        guiGraphics.blit(tex, 0, 0, 0, 0, texWidth, texHeight, texWidth, texHeight);
        guiGraphics.setColor(1f, 1f, 1f, 1);
        pose.popPose();
    }
}
