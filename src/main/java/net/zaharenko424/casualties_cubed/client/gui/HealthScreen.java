package net.zaharenko424.casualties_cubed.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.zaharenko424.casualties_cubed.CasualtiesCubedTags;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.client.MinigameOpener;
import net.zaharenko424.casualties_cubed.client.gui.widget.*;
import net.zaharenko424.casualties_cubed.client.moodles.AbstractMoodleVisual;
import net.zaharenko424.casualties_cubed.client.moodles.MoodleController;
import net.zaharenko424.casualties_cubed.client.ticksounds.HeartBeatSound;
import net.zaharenko424.casualties_cubed.item.api.IBag;
import net.zaharenko424.casualties_cubed.item.api.AbstractBandage;
import net.zaharenko424.casualties_cubed.item.api.IMedicalMinigameUsable;
import net.zaharenko424.casualties_cubed.item.api.ISimpleMedicalUsable;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.LimbStatistics;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import net.zaharenko424.casualties_cubed.network.ModNetwork;
import net.zaharenko424.casualties_cubed.network.ServerPacketHandler;
import net.zaharenko424.casualties_cubed.network.packet.ServerboundGuiSyncTogglePacket;
import net.zaharenko424.casualties_cubed.network.packet.ServerboundUseMedItemPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HealthScreen extends Screen {

    private LimbWidget L_Hand;
    private LimbWidget R_Hand;
    private LimbWidget L_Arm;
    private LimbWidget R_Arm;
    private LimbWidget L_Foot;
    private LimbWidget R_Foot;
    private LimbWidget L_Leg;
    private LimbWidget R_Leg;
    private LimbWidget Chest;
    private LimbWidget Head;
    private ItemWidget RightItem;
    private final List<ItemWidget> RightItemsubWidgets = new ArrayList<>();
    private ItemWidget LeftItem;
    private final List<ItemWidget> LeftItemsubWidgets = new ArrayList<>();
    private HealthInfoBoxWidget healthbox;
    private CPRButton cprButton;
    private final Player target;
    private final Player localPlayer;

    private List<CustomButton> buttonList = new ArrayList<>();
    private int listStartX = 5;
    private int listStartY = height / 4 * 3;

    private LimbWidget lastClicked;
    private LimbWidget lastHovered;

    private HeartBeatSound heartBeatSound;

    public boolean BGmode = false;


    public HealthScreen(Player target) {
        super(Component.empty());
        this.target = target;
        this.localPlayer = Minecraft.getInstance().player;
    }

    @Override
    protected void init() {
        super.init();
        int start_x = (this.width / 2) + 50;
        int start_y = (this.height / 4) - 25;
        listStartX = 1;
        listStartY = 196 + 2;
        Head = new LimbWidget(start_x, start_y, 32, 32, Component.empty(), Limb.HEAD);
        Chest = new LimbWidget(start_x, start_y + 32, 32, 64, Component.empty(), Limb.CHEST);
        L_Arm = new LimbWidget(start_x + 32, start_y + 32, 48, 16, Component.empty(), Limb.LEFT_ARM);
        R_Arm = new LimbWidget(start_x - 48, start_y + 32, 48, 16, Component.empty(), Limb.RIGHT_ARM);
        L_Hand = new LimbWidget(start_x + 32 + 48, start_y + 32, 16, 16, Component.empty(), Limb.LEFT_HAND);
        R_Hand = new LimbWidget(start_x - 48 - 16, start_y + 32, 16, 16, Component.empty(), Limb.RIGHT_HAND);
        L_Leg = new LimbWidget(start_x + 16, start_y + 32 + 64, 16, 48, Component.empty(), Limb.LEFT_LEG);
        R_Leg = new LimbWidget(start_x, start_y + 32 + 64, 16, 48, Component.empty(), Limb.RIGHT_LEG);
        L_Foot = new LimbWidget(start_x + 16, start_y + 32 + 64 + 48, 16, 16, Component.empty(), Limb.LEFT_FOOT);
        R_Foot = new LimbWidget(start_x, start_y + 32 + 64 + 48, 16, 16, Component.empty(), Limb.RIGHT_FOOT);
        healthbox = new HealthInfoBoxWidget(0, 0, 128, 196, Component.empty());
        cprButton = new CPRButton(this.width - 36, this.height - 36, this, target);

        Player player = Minecraft.getInstance().player;
        if (player != null) {
            for (InteractionHand hand : InteractionHand.values()) {
                ItemStack stack = player.getItemInHand(hand);

                // Which arm is this hand using? (LEFT or RIGHT)
                HumanoidArm arm = Limb.getArmFromHand(hand, player);

                if (arm == HumanoidArm.RIGHT) {
                    // Draw right-hand item on the right side of HUD
                    RightItem = new ItemWidget(
                            start_x - 48 - 16 - 8,
                            start_y + 8,
                            stack
                    );
                } else {
                    // Draw left-hand item on the left side of HUD
                    LeftItem = new ItemWidget(
                            start_x + 32 + 48 + 8,
                            start_y + 8,
                            stack
                    );
                }
            }
        }
        addRenderableWidget(Head);
        addRenderableWidget(Chest);
        addRenderableWidget(L_Arm);
        addRenderableWidget(L_Leg);
        addRenderableWidget(L_Foot);
        addRenderableWidget(L_Hand);
        addRenderableWidget(R_Arm);
        addRenderableWidget(R_Foot);
        addRenderableWidget(R_Hand);
        addRenderableWidget(R_Leg);
        addRenderableWidget(LeftItem);
        addRenderableWidget(RightItem);
        addRenderableWidget(healthbox);
        addRenderableWidget(cprButton);
        Head.populate_sprites();
        Chest.populate_sprites();
        L_Arm.populate_sprites();
        L_Leg.populate_sprites();
        L_Foot.populate_sprites();
        L_Hand.populate_sprites();
        R_Arm.populate_sprites();
        R_Foot.populate_sprites();
        R_Hand.populate_sprites();
        R_Leg.populate_sprites();
        if (target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(h -> h.getConsciousness() < 10).orElse(false)) {
            cprButton.visible = true;
        } else {
            cprButton.visible = false;
        }

        ModNetwork.CHANNEL.sendToServer(new ServerboundGuiSyncTogglePacket(target.getId(), true));
        lastHovered = Head;
        healthbox.setName(Component.literal(target.getScoreboardName()));
        if (player != null) {
            target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h -> {
                heartBeatSound = new HeartBeatSound(player, h.getHeartRate());
            });
        }
        if (RightItem.getStack().getItem() instanceof IBag iBag) {
            List<ItemStack> itemStacks = iBag.getItems(RightItem.getStack());
            RightItemsubWidgets.clear();

            int slotWidth = 16;
            int rows = 2;
            int total = itemStacks.size();
            int columns = (int) Math.ceil(total / (double) rows);

            // Centered above the main slot
            int centerX = start_x - 48 - 16 - 8;
            int centerY = start_y + 8;

            int totalWidth = (columns - 1) * slotWidth;
            int startX = centerX - totalWidth / 2;
            int startY = centerY - (rows * slotWidth) - 4; // small vertical gap

            for (int i = 0; i < total; i++) {
                int col = i % columns;  // horizontal index
                int row = i / columns;  // vertical index (0 = top, 1 = bottom)

                int x = startX + col * slotWidth;
                int y = startY + row * slotWidth;

                ItemWidget widget = new ItemWidget(x, y, itemStacks.get(i));
                RightItemsubWidgets.add(widget);
                addRenderableWidget(widget);
            }
        }
        if (LeftItem.getStack().getItem() instanceof IBag iBag) {
            List<ItemStack> itemStacks = iBag.getItems(LeftItem.getStack());
            LeftItemsubWidgets.clear();

            int slotWidth = 16;
            int rows = 2;
            int total = itemStacks.size();
            int columns = (int) Math.ceil(total / (double) rows);

            // Centered above the main slot
            int centerX = start_x + 32 + 48 + 8;// +9 to roughly center by half slot
            int centerY = start_y + 8;

            int totalWidth = (columns - 1) * slotWidth;
            int startX = centerX - totalWidth / 2;
            int startY = centerY - (slotWidth * rows) - 4; // small vertical gap (4px)

            for (int i = 0; i < total; i++) {
                int col = i % columns;  // horizontal index
                int row = i / columns;  // vertical index (0 = top, 1 = bottom)

                int x = startX + col * slotWidth;
                int y = startY + row * slotWidth;

                ItemWidget widget = new ItemWidget(x, y, itemStacks.get(i));
                LeftItemsubWidgets.add(widget);
                addRenderableWidget(widget);
            }
            // Starting position: centered horizontally above the main slot
            // The main slot is at (start_x - 48 - 16 - 8, start_y + 8)

            // Total width of all columns

        }
        updateScreen();
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        R_Arm.renderSprites(pGuiGraphics);
        Chest.renderSprites(pGuiGraphics);
        Head.renderSprites(pGuiGraphics);
        L_Arm.renderSprites(pGuiGraphics);
        R_Hand.renderSprites(pGuiGraphics);
        L_Hand.renderSprites(pGuiGraphics);
        R_Leg.renderSprites(pGuiGraphics);
        L_Leg.renderSprites(pGuiGraphics);
        R_Foot.renderSprites(pGuiGraphics);
        L_Foot.renderSprites(pGuiGraphics);
        healthbox.setBGMode(BGmode);
        LeftItem.setBGMode(BGmode);
        RightItem.setBGMode(BGmode);

        for (ItemWidget itemWidget : LeftItemsubWidgets) {
            itemWidget.setBGMode(BGmode);
        }

        for (ItemWidget itemWidget : RightItemsubWidgets) {
            itemWidget.setBGMode(BGmode);
        }

        // render moodles for self (ignoring hotbar constraints!)
        List<AbstractMoodleVisual> visible = MoodleController.updateAndGetToRender(localPlayer, true);

        int x = 4; // center moodles
        int y = this.height - MoodleController.MOODLE_SIZE - MoodleController.PADDING; // fixed height above bottom

        AbstractMoodleVisual hovered = null;
        for (AbstractMoodleVisual moodle : visible) {
            moodle.render(pGuiGraphics, pPartialTick, x, y);

            if (moodle.isMouseOver(pMouseX, pMouseY, x, y)) {
                hovered = moodle;
            }

            x += MoodleController.MOODLE_SIZE + MoodleController.PADDING;
        }

        if (hovered != null) {
            pGuiGraphics.renderTooltip(minecraft.font, hovered.getTooltip(localPlayer), Optional.empty(), pMouseX, pMouseY);
        }

        LimbWidget h = getHoveringWidget(pMouseX, pMouseY);

        if (h != null) {
            if (!BGmode)
                lastHovered = h;
        }

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(width / 2f, height * 0.666f, 0);
        drawECG(pGuiGraphics);
        pGuiGraphics.pose().popPose();
    }

    float timeToUpdate;
    long lastFrameMS = System.currentTimeMillis();
    int writeX;
    int lastY;
    float writeHeight;
    int[][] pixelGrid = new int[120][25];

    private void drawECG(GuiGraphics graphics) {
        PlayerHealthData data = PlayerHealthData.of(Minecraft.getInstance().player).orElse(null);
        if (data == null) return;

        int width = 120;
        int height = 25;

        long currentMS = System.currentTimeMillis();
        timeToUpdate += (currentMS - lastFrameMS) / 1000f;
        lastFrameMS = currentMS;
        if (timeToUpdate > 0.1f) {
            timeToUpdate = 0.1f;
        }

        while (timeToUpdate > 0.028f) {
            writeX++;
            if (writeX >= width) {
                writeX = 0;
            }

            writeHeight = data.getECGHeight(timeToUpdate - 0.028f);

            int num = Math.round((writeHeight + 1f) * 0.5f * (float)(height - 1));
            num = height - num;
            int num2 = Math.min(lastY, num);
            int num3 = Math.max(lastY, num);
            for (int i = num2; i <= num3; i++) {
                pixelGrid[writeX][i] = -1;
                if (i + 1 < height) {
                    pixelGrid[writeX][i + 1] = color(-1, Math.max(50, alpha(pixelGrid[writeX][i + 1])));
                }

                if (i - 1 >= 0) {
                    pixelGrid[writeX][i - 1] = color(-1, Math.max(50, alpha(pixelGrid[writeX][i - 1])));
                }

                if (writeX + 1 < width) {
                    pixelGrid[writeX + 1][i] = color(-1, Math.max(50, alpha(pixelGrid[writeX + 1][i])));
                }

                if (writeX - 1 >= 0) {
                    pixelGrid[writeX - 1][i] = color(-1, Math.max(50, alpha(pixelGrid[writeX - 1][i])));
                }
            }

            for (int j = 0; j < width; j++) {
                for (int k = 0; k < height; k++) {
                    int color = pixelGrid[j][k];
                    pixelGrid[j][k] = color(color, (byte) (alpha(color) * 0.985f));
                }
            }

            lastY = num;
            timeToUpdate -= 0.028f;
        }

        graphics.drawManaged(() -> {
            for (int j = 0; j < width; j++) {
                for (int k = 0; k < height; k++) {
                    int color = pixelGrid[j][k];
                    if (color == 0 || alpha(color) == 0) continue;

                    graphics.fill(j, k, j + 1, k + 1, color);
                }
            }
        });
    }

    private static int alpha(int packed) {
        return FastColor.ARGB32.alpha(packed);
    }

    private static int color(int packed, int alpha) {
        return alpha << 24 | (packed & 0x00FFFFFF);
    }

    @Override
    public void tick() {
        super.tick();
        cprButton.visible = target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(h -> h.getConsciousness() < 10).orElse(false) && (target != Minecraft.getInstance().player);
        if (heartBeatSound != null && Minecraft.getInstance().player != null) {
            target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h -> {
                float bpm = h.getHeartRate();
                heartBeatSound.setBPM(bpm);
            });
            heartBeatSound.tick();
        }

        if (!target.isAlive()) {
            onClose(); // target gone
            return;
        }

        Player viewer = Minecraft.getInstance().player;
        double distSq = viewer.distanceToSqr(target);

        if (distSq > ServerPacketHandler.TOO_FAR) {
            Minecraft.getInstance().screen.onClose();
            return;
        }
        updateScreen();
        UpdateSubStacks();
        if (!BGmode)
            UpdateButtons(lastClicked);
        target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(health -> {
            LimbStatistics hovered = health.getLimb(lastHovered.getLimb());
            healthbox.setSkin(hovered.getSkinHealth());
            healthbox.setMuscle(hovered.getMuscleHealth());
            healthbox.setLimbname(lastHovered.getLimb());
            healthbox.setPain2(hovered.getPain());
            healthbox.setBleed2(hovered.getBleedRate());
            healthbox.setPain((float) health.getAveragePain());
            healthbox.setContiousness(health.getConsciousness());
            healthbox.setBlood(health.getBloodVolume());
            healthbox.setBleed(health.totalBleedSpeed());
            healthbox.setInfection(hovered.getInfection());
            healthbox.setOpiates(health.painkillers.currentOpiateReception());
            healthbox.setOxygen(health.getBloodOxygen());
            healthbox.setDislocated(hovered.getDislocationTimer());
            healthbox.setFracture(hovered.getBoneHealTimer());
            healthbox.setBrain(health.getBrainHealth());
            healthbox.setTemp(health.getTemperature());
            healthbox.setImmunity(health.getImmunity());
        });
        if (!BGmode) {
            Minecraft.getInstance().player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h -> {
                if (h.getConsciousness() <= 4)
                    onClose();
            });
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public void UpdateSubStacks() {
        if (RightItem.getStack().getItem() instanceof IBag iBag) {
            List<ItemStack> itemStacks = iBag.getItems(RightItem.getStack());
            int total = Math.min(itemStacks.size(), RightItemsubWidgets.size());
            for (int i = 0; i < total; i++) {
                RightItemsubWidgets.get(i).setStack(itemStacks.get(i));
            }
        }
        if (LeftItem.getStack().getItem() instanceof IBag iBag) {
            List<ItemStack> itemStacks = iBag.getItems(LeftItem.getStack());
            int total = Math.min(itemStacks.size(), LeftItemsubWidgets.size());
            for (int i = 0; i < total; i++) {
                LeftItemsubWidgets.get(i).setStack(itemStacks.get(i));
            }
        }
        if (BGmode) {
            LeftItem.visible = false;
            RightItem.visible = false;
            for (ItemWidget widget : LeftItemsubWidgets) {
                widget.visible = false;
            }
            for (ItemWidget widget : RightItemsubWidgets) {
                widget.visible = false;
            }
        } else {
            LeftItem.visible = true;
            RightItem.visible = true;
            for (ItemWidget widget : LeftItemsubWidgets) {
                widget.visible = true;
            }
            for (ItemWidget widget : RightItemsubWidgets) {
                widget.visible = true;
            }
        }
    }

    public void updateScreen() {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            for (InteractionHand hand : InteractionHand.values()) {
                ItemStack stack = player.getItemInHand(hand);

                HumanoidArm arm = Limb.getArmFromHand(hand, player);

                if (arm == HumanoidArm.RIGHT) {
                    RightItem.setStack(stack);
                } else {
                    LeftItem.setStack(stack);
                }
            }
        }
        target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(health -> {
            LimbStatistics stats;
            for (Limb limb : Limb.values()) {
                stats = health.getLimb(limb);
                LimbWidget widget = switch (limb) {
                    case HEAD -> Head;
                    case CHEST -> Chest;
                    case LEFT_ARM -> L_Arm;
                    case RIGHT_ARM -> R_Arm;
                    case LEFT_HAND -> L_Hand;
                    case RIGHT_HAND -> R_Hand;
                    case LEFT_LEG -> L_Leg;
                    case RIGHT_LEG -> R_Leg;
                    case LEFT_FOOT -> L_Foot;
                    case RIGHT_FOOT -> R_Foot;
                };
                if (stats.isAmputated()) {
                    widget.visible = false;
                    widget.setAmputated(true);
                    continue;
                }

                widget.setAmputated(false);
                if (limb == Limb.HEAD) {
                    widget.setLeftEyeGone(health.isLeftEyeBlind());
                    widget.setMouthGone(health.isMouthRemoved());
                    widget.setRightEyeGone(health.isRightEyeBlind());
                }
                // collect values once
                float bleed = stats.getBleedRate();
                boolean isBleeding = bleed > 0 && !stats.isTourniquet() && !health.isUnderTourniquet(limb);
                float pain = stats.getPain();
                float skin = stats.getSkinHealth();
                float muscle = stats.getMuscleHealth();

                boolean infection = stats.getInfection() > 25;
                boolean dislocated = stats.getDislocationTimer() > 0;
                boolean splint = stats.hasSplint();
                boolean shrapnel = stats.getShrapnel() > 0;
                boolean fractured = stats.getBoneHealTimer() > 0;
                boolean desinfection = stats.getDisinfectionTime() > 0;
                boolean tourniquet = stats.isTourniquet();

                // ---- apply to the widget ----
                widget.setShake(pain / 100f);
                widget.setBorder_red(1 - (skin / 100f));
                widget.setBase_red(1 - (muscle / 100f));

                if (isBleeding) {
                    float scale = Math.max(0.9f, (bleed / health.getMAX_BLEED_RATE()) * 2.5f);
                    widget.setScaleOf(StatusSprites.BLEED, scale);
                }
                widget.setSubSpriteVisible(StatusSprites.BLEED, isBleeding);
                widget.setSubSpriteVisible(StatusSprites.DISINFECTION, desinfection);
                widget.setSubSpriteVisible(StatusSprites.FRACTURE, fractured);
                widget.setSubSpriteVisible(StatusSprites.INFECTION, infection);
                widget.setSubSpriteVisible(StatusSprites.SHRAPNEL, shrapnel);
                widget.setSubSpriteVisible(StatusSprites.SPLINT, splint);
                widget.setSubSpriteVisible(StatusSprites.DISLOCATION, dislocated);
                widget.setSubSpriteVisible(StatusSprites.TOURNIQUET, tourniquet);
            }
        });
    }

    @Override
    public void onClose() {
        super.onClose();
        if (heartBeatSound != null) {
            heartBeatSound = null;
        }
        ModNetwork.CHANNEL.sendToServer(new ServerboundGuiSyncTogglePacket(target.getId(), false));
    }

    @Override
    public void renderBackground(GuiGraphics gui) {
        super.renderBackground(gui);
        gui.fill(0, 0, this.width, this.height, 0x000000FF);
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        LimbWidget widget = getHoveringWidget(pMouseX, pMouseY);
        if (widget != null && !widget.isAmputated()) {
            if (RightItem.isDragging()) {
                useMedItem(widget, HumanoidArm.RIGHT);
            } else if (LeftItem.isDragging()) {
                useMedItem(widget, HumanoidArm.LEFT);
            }

            useMedItemFromBag(widget, RightItemsubWidgets, HumanoidArm.RIGHT);
            useMedItemFromBag(widget, LeftItemsubWidgets, HumanoidArm.LEFT);
        }

        RightItem.onRelease(pMouseX, pMouseY);
        LeftItem.onRelease(pMouseX, pMouseY);
        for (ItemWidget itemWidget : RightItemsubWidgets) {
            itemWidget.onRelease(pMouseX, pMouseY);
        }
        for (ItemWidget itemWidget : LeftItemsubWidgets) {
            itemWidget.onRelease(pMouseX, pMouseY);
        }
        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    private void useMedItem(LimbWidget widget, HumanoidArm arm) {
        InteractionHand hand = getHand(arm, minecraft.player);
        Limb limb = widget.getLimb();
        ItemStack itemstack = minecraft.player.getItemInHand(hand);

        if (itemstack.getItem() instanceof AbstractBandage) {
            MinigameOpener.OpenBandageMinigame(target, itemstack, limb, hand);
        } else if (itemstack.getItem() instanceof IMedicalMinigameUsable helper) {
            helper.openMinigameScreen(target, itemstack, limb, hand);
        }

        if (!itemstack.is(CasualtiesCubedTags.Item.CAUTERIZE) && !(itemstack.getItem() instanceof ISimpleMedicalUsable)) {
            return;
        }

        ModNetwork.CHANNEL.sendToServer(new ServerboundUseMedItemPacket(target.getId(), limb, hand));
    }

    private void useMedItemFromBag(LimbWidget widget, List<ItemWidget> widgets, HumanoidArm arm) {
        InteractionHand hand = getHand(arm, minecraft.player);
        Limb limb;
        ItemStack itemstack, bagstack = minecraft.player.getItemInHand(hand);
        for (int slot = 0; slot < widgets.size(); slot++) {
            if (!widgets.get(slot).isDragging()) continue;

            limb = widget.getLimb();
            itemstack = widgets.get(slot).getStack();

            if (itemstack.getItem() instanceof AbstractBandage) {
                MinigameOpener.OpenBandageMinigame(target, itemstack, slot, limb, hand);
                return;
            } else if (itemstack.getItem() instanceof IMedicalMinigameUsable helper) {
                helper.openMinigameBagScreen(target, itemstack, bagstack, slot, limb, hand);
                return;
            }

            if (itemstack.is(CasualtiesCubedTags.Item.CAUTERIZE) || itemstack.getItem() instanceof ISimpleMedicalUsable) {
                ModNetwork.CHANNEL.sendToServer(new ServerboundUseMedItemPacket(target.getId(), limb, hand, (byte) slot));
                return;
            }
        }
    }

    private InteractionHand getHand(HumanoidArm arm, Player player) {
        HumanoidArm mainArm = player.getMainArm();

        // If we're asking for the player's dominant arm → MAIN_HAND
        if (arm == mainArm) {
            return InteractionHand.MAIN_HAND;
        } else {
            // Otherwise it's the opposite → OFF_HAND
            return InteractionHand.OFF_HAND;
        }
    }

    private LimbWidget getHoveringWidget(double pMouseX, double pMouseY) {
        for (GuiEventListener child : this.children()) {
            if (child instanceof LimbWidget limbwidget) {
                if (limbwidget.isMouseOver(pMouseX, pMouseY)) return limbwidget;
            }
        }
        return null;
    }

    private CustomButton getHoveringWidgetCustomButton(double pMouseX, double pMouseY) {
        for (GuiEventListener child : this.children()) {
            if (child instanceof CustomButton limbwidget) {
                if (limbwidget.isMouseOver(pMouseX, pMouseY)) return limbwidget;
            }
        }
        return null;
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        LimbWidget widget = getHoveringWidget(pMouseX, pMouseY);
        if (widget != null) {
            if (!widget.isAmputated()) {
                UpdateButtons(widget);
            }
        }

        CustomButton button = getHoveringWidgetCustomButton(pMouseX, pMouseY);
        if (button != null) {
            if (!BGmode)
                UpdateButtons(lastClicked);
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    public void UpdateButtons(LimbWidget widget) {
        List<StatusSprites> statusList = new ArrayList<>();
        if (widget != null) {
            for (CustomButton button : buttonList) {
                removeWidget(button);
            }
            buttonList = new ArrayList<>();
            if (widget.isSpritePresent(StatusSprites.SHRAPNEL)) statusList.add(StatusSprites.SHRAPNEL);
            if (widget.isSpritePresent(StatusSprites.DISLOCATION)) statusList.add(StatusSprites.DISLOCATION);
            if (widget.isSpritePresent(StatusSprites.TOURNIQUET)) statusList.add(StatusSprites.TOURNIQUET);
            if (widget.isSpritePresent(StatusSprites.SPLINT)) statusList.add(StatusSprites.SPLINT);
            int i = 0;
            for (StatusSprites sprite : statusList) {
                buttonList.add(new CustomButton(listStartX, listStartY + (16 * i), sprite, widget.getLimb(), target));
                addRenderableWidget(buttonList.get(i));
                i++;
            }
            lastClicked = widget;
        }
    }
}
