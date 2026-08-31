package net.zaharenko424.casualties_cubed.client.gui.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.CasualtiesCubedTags;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.client.MinigameOpener;
import net.zaharenko424.casualties_cubed.client.gui.StatusSprites;
import net.zaharenko424.casualties_cubed.client.gui.widget.*;
import net.zaharenko424.casualties_cubed.client.moodles.MoodleManager;
import net.zaharenko424.casualties_cubed.item.api.AbstractBandage;
import net.zaharenko424.casualties_cubed.item.api.IBag;
import net.zaharenko424.casualties_cubed.item.api.IMedicalMinigameUsable;
import net.zaharenko424.casualties_cubed.item.api.ISimpleMedicalUsable;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import net.zaharenko424.casualties_cubed.limbs.SleepQuality;
import net.zaharenko424.casualties_cubed.network.ModNetwork;
import net.zaharenko424.casualties_cubed.network.ServerPacketHandler;
import net.zaharenko424.casualties_cubed.network.packet.ServerboundGuiSyncTogglePacket;
import net.zaharenko424.casualties_cubed.network.packet.ServerboundSleepPacket;
import net.zaharenko424.casualties_cubed.network.packet.ServerboundUseMedItemPacket;
import net.zaharenko424.casualties_cubed.registry.ModSounds;
import net.zaharenko424.casualties_cubed.util.ColorUtil;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import static net.minecraft.util.FastColor.ARGB32.alpha;

public class HealthScreen extends Screen {

    private final EnumMap<Limb, LimbWidget> limbWidgets = new EnumMap<>(Limb.class);

    private ItemWidget RightItem;
    private final List<ItemWidget> RightItemsubWidgets = new ArrayList<>();
    private ItemWidget LeftItem;
    private final List<ItemWidget> LeftItemsubWidgets = new ArrayList<>();
    private final HealthInfoBoxWidget healthbox;
    private CPRButton cprButton;
    private final Player target;
    private final Player localPlayer;

    private List<CustomButton> buttonList = new ArrayList<>();
    private int listStartX = 5;
    private int listStartY = height / 4 * 3;

    private LimbWidget lastClicked;

    public boolean BGmode = false;

    private final RenderableImage mainRight = new RenderableImage(SWITCH_MAIN_HAND, 32, 32);
    private final RenderableImage mainLeft = new RenderableImage(SWITCHED_MAIN_HAND, 32, 32);
    private final ImageButton switchMainHandButton;
    private final ImageButton sleepButton;
    private final ImageButton workoutButton;

    public HealthScreen(Player target) {
        super(Component.empty());
        this.target = target;
        this.localPlayer = Minecraft.getInstance().player;

        PlayerHealthData data = PlayerHealthData.of(target).orElse(new PlayerHealthData());
        healthbox = new HealthInfoBoxWidget(0, 0, 128, 196, target, data);

        for (Limb limb : Limb.values()) {
            limbWidgets.put(limb, new LimbWidget(limb, data));
        }

        mainRight.offset.set(-4, -4, 0);
        mainLeft.offset.set(-4, -4, 0);
        switchMainHandButton = new ImageButton(24, 24, new RenderableImage(SWITCH_MAIN_HAND, 32, 32), () -> {
            localPlayer.setMainArm(localPlayer.getMainArm().getOpposite());
            localPlayer.playSound(ModSounds.CLICK.get());
        });
        switchMainHandButton.tooltip(Component.translatable("tooltip.casualties_cubed.switch_main_hand_button.title"), Component.translatable("tooltip.casualties_cubed.switch_main_hand_button.description"));
        switchMainHandButton.image(localPlayer.getMainArm() == HumanoidArm.RIGHT ? mainRight : mainLeft);

        workoutButton = new ImageButton(32, 32, new RenderableImage(WORKOUT, 32, 32), () -> {});
        workoutButton.tooltip(Component.translatable("tooltip.casualties_cubed.workout_button.title"), Component.translatable("tooltip.casualties_cubed.workout_button.description"));

        sleepButton = new ImageButton(32, 32, new RenderableImage(NAP_BUTTON, 32, 32), () -> {
            ModNetwork.CHANNEL.sendToServer(new ServerboundSleepPacket());
            onClose();
        });
        sleepButton.tooltip(Component.translatable("tooltip.casualties_cubed.sleep_button.title", SleepQuality.currentSleepQuality(localPlayer).comp), Component.translatable("tooltip.casualties_cubed.sleep_button.description"));
        sleepButton.active(data.canTakeNap());
    }

    @Override
    protected void init() {
        super.init();
        int start_x = (this.width / 2) - 16;
        int start_y = (this.height / 4) - 25;
        listStartX = 1;
        listStartY = 196 + 2;
        limbWidgets.get(Limb.HEAD).setPosition(start_x, start_y);
        limbWidgets.get(Limb.THORAX).setPosition(start_x, start_y + 32);
        limbWidgets.get(Limb.ABDOMEN).setPosition(start_x, start_y + 64);
        limbWidgets.get(Limb.UPPER_LEFT_ARM).setPosition(start_x + 32, start_y + 32);
        limbWidgets.get(Limb.LOWER_LEFT_ARM).setPosition(start_x + 32 + 24, start_y + 32);
        limbWidgets.get(Limb.UPPER_RIGHT_ARM).setPosition(start_x - 24, start_y + 32);
        limbWidgets.get(Limb.LOWER_RIGHT_ARM).setPosition(start_x - 48, start_y + 32);
        limbWidgets.get(Limb.LEFT_HAND).setPosition(start_x + 32 + 48, start_y + 32);
        limbWidgets.get(Limb.RIGHT_HAND).setPosition(start_x - 48 - 16, start_y + 32);
        limbWidgets.get(Limb.UPPER_LEFT_LEG).setPosition(start_x + 16, start_y + 32 + 64);
        limbWidgets.get(Limb.LOWER_LEFT_LEG).setPosition(start_x + 16, start_y + 32 + 64 + 24);
        limbWidgets.get(Limb.UPPER_RIGHT_LEG).setPosition(start_x, start_y + 32 + 64);
        limbWidgets.get(Limb.LOWER_RIGHT_LEG).setPosition(start_x, start_y + 32 + 64 + 24);
        limbWidgets.get(Limb.LEFT_FOOT).setPosition(start_x + 16, start_y + 32 + 64 + 48);
        limbWidgets.get(Limb.RIGHT_FOOT).setPosition(start_x, start_y + 32 + 64 + 48);

        limbWidgets.values().forEach(widget -> {
            widget.populate_sprites();
            addRenderableWidget(widget);
        });

        cprButton = new CPRButton(this.width - 36, this.height - 36, this, target);

        Player player = Minecraft.getInstance().player;
        if (player != null) {
            for (InteractionHand hand : InteractionHand.values()) {
                ItemStack stack = player.getItemInHand(hand);
                HumanoidArm arm = Limb.getArmFromHand(hand, player);

                if (arm == HumanoidArm.RIGHT) {
                    // Draw right-hand item on the right side of HUD
                    RightItem = new ItemWidget(start_x - 48 - 16 - 8, start_y + 8, stack);
                } else {
                    // Draw left-hand item on the left side of HUD
                    LeftItem = new ItemWidget(start_x + 32 + 48 + 8, start_y + 8, stack);
                }

                maybeSetupBagItems(start_x, start_y, arm == HumanoidArm.RIGHT);
            }
        }
        addRenderableWidget(LeftItem);
        addRenderableWidget(RightItem);
        addRenderableWidget(healthbox);
        addRenderableWidget(cprButton);
        cprButton.visible = target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(h -> h.getConsciousness() < 10).orElse(false);

        ModNetwork.CHANNEL.sendToServer(new ServerboundGuiSyncTogglePacket(target.getId(), true));

        updateScreen();

        int y = this.height - MoodleManager.MOODLE_SIZE - 1;
        addWidget(MoodleManager.HEALTH_PANEL_BUTTON);

        switchMainHandButton.offset.set(width - 12, y - 32 - 2 - 32 - 2 - 12, 0);
        addRenderableWidget(switchMainHandButton);

        sleepButton.offset.set(width - 16, y - 16, 0);
        addRenderableWidget(sleepButton);

        workoutButton.offset.set(width - 16, y - 32 - 2 - 16, 0);
        addRenderableWidget(workoutButton);

        healthbox.init(this);
    }

    private void maybeSetupBagItems(int start_x, int start_y, boolean right) {
        ItemStack stack = (right ? RightItem : LeftItem).getStack();
        if (!(stack.getItem() instanceof IBag iBag)) return;

        List<ItemStack> itemStacks = iBag.getItems(stack);
        List<ItemWidget> widgets = right ? RightItemsubWidgets : LeftItemsubWidgets;
        widgets.clear();

        int slotWidth = 16;
        int rows = 2;
        int total = itemStacks.size();
        int columns = (int) Math.ceil(total / (double) rows);

        // Centered above the main slot
        int centerX = start_x + (right ? -72 : 88);// +9 to roughly center by half slot
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
            widgets.add(widget);
            addRenderableWidget(widget);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(graphics);
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
        limbWidgets.values().forEach(widget -> widget.renderSprites(graphics));
        LeftItem.setBGMode(BGmode);
        RightItem.setBGMode(BGmode);

        for (ItemWidget itemWidget : LeftItemsubWidgets) {
            itemWidget.setBGMode(BGmode);
        }

        for (ItemWidget itemWidget : RightItemsubWidgets) {
            itemWidget.setBGMode(BGmode);
        }

        MoodleManager.render(graphics, pPartialTick, width, height, false, pMouseX, pMouseY);

        LimbWidget h = getHoveringWidget(pMouseX, pMouseY);

        if (h != null) {
            if (!BGmode) healthbox.setSelectedLimb(h.getLimb());
        }

        graphics.pose().pushPose();
        graphics.pose().translate(6 + 7, 6 + 164, 0);
        drawECG(graphics);
        graphics.pose().popPose();
    }

    @Override
    public void renderBackground(GuiGraphics graphics) {
        graphics.fill(0, 0, width, height, 1610612736);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.ScreenEvent.BackgroundRendered(this, graphics));
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
                    pixelGrid[writeX][i + 1] = ColorUtil.colorWithAlpha(-1, Math.max(50, alpha(pixelGrid[writeX][i + 1])));
                }

                if (i - 1 >= 0) {
                    pixelGrid[writeX][i - 1] = ColorUtil.colorWithAlpha(-1, Math.max(50, alpha(pixelGrid[writeX][i - 1])));
                }

                if (writeX + 1 < width) {
                    pixelGrid[writeX + 1][i] = ColorUtil.colorWithAlpha(-1, Math.max(50, alpha(pixelGrid[writeX + 1][i])));
                }

                if (writeX - 1 >= 0) {
                    pixelGrid[writeX - 1][i] = ColorUtil.colorWithAlpha(-1, Math.max(50, alpha(pixelGrid[writeX - 1][i])));
                }
            }

            for (int j = 0; j < width; j++) {
                for (int k = 0; k < height; k++) {
                    int color = pixelGrid[j][k];
                    pixelGrid[j][k] = ColorUtil.colorWithAlpha(color, (byte) (alpha(color) * 0.985f));
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

    @Override
    public void tick() {
        super.tick();
        cprButton.visible = target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(h -> h.getConsciousness() < 10).orElse(false) && (target != Minecraft.getInstance().player);

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

        if (!BGmode) {
            UpdateButtons(lastClicked);
            Minecraft.getInstance().player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                if (!data.isConscious()) onClose();
            });
        }

        PlayerHealthData data = PlayerHealthData.of(localPlayer).orElse(null);
        if (data == null) {
            onClose();
            return;
        }

        switchMainHandButton.image(localPlayer.getMainArm() == HumanoidArm.RIGHT ? mainRight : mainLeft);
        sleepButton.tooltip(Component.translatable("tooltip.casualties_cubed.sleep_button.title", SleepQuality.currentSleepQuality(localPlayer).comp), Component.translatable("tooltip.casualties_cubed.sleep_button.description"));
        sleepButton.active(data.canTakeNap());
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

        limbWidgets.values().forEach(LimbWidget::update);
    }

    @Override
    public void onClose() {
        super.onClose();

        ModNetwork.CHANNEL.sendToServer(new ServerboundGuiSyncTogglePacket(target.getId(), false));
        localPlayer.playSound(ModSounds.HEALTH_SCREEN_CLOSE.get());
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

        boolean anyConsumed = false;
        for (GuiEventListener listener : children()) {
            anyConsumed |= listener.mouseReleased(pMouseX, pMouseY, pButton);
        }
        return anyConsumed;
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
        return arm == player.getMainArm() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }

    private LimbWidget getHoveringWidget(double pMouseX, double pMouseY) {
        for (LimbWidget widget : limbWidgets.values()) {
            if (widget.isMouseOver(pMouseX, pMouseY)) return widget;
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

    private static final ResourceLocation SWITCH_MAIN_HAND = CasualtiesCubed.texLoc("gui/switch_main_hand");
    private static final ResourceLocation SWITCHED_MAIN_HAND = CasualtiesCubed.texLoc("gui/switched_main_hand");
    private static final ResourceLocation WORKOUT = CasualtiesCubed.texLoc("gui/workout");
    private static final ResourceLocation NAP_BUTTON = CasualtiesCubed.texLoc("gui/nap");
}
