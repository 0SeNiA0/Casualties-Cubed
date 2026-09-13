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
import net.zaharenko424.casualties_cubed.client.gui.widget.*;
import net.zaharenko424.casualties_cubed.client.moodles.MoodleManager;
import net.zaharenko424.casualties_cubed.config.ClientConfig;
import net.zaharenko424.casualties_cubed.item.api.AbstractBandage;
import net.zaharenko424.casualties_cubed.item.api.IMedicalMinigameUsable;
import net.zaharenko424.casualties_cubed.item.api.ISimpleMedicalUsable;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import net.zaharenko424.casualties_cubed.limbs.SleepQuality;
import net.zaharenko424.casualties_cubed.network.ModNetwork;
import net.zaharenko424.casualties_cubed.network.ServerPacketHandler;
import net.zaharenko424.casualties_cubed.network.packet.ServerboundGuiSyncTogglePacket;
import net.zaharenko424.casualties_cubed.network.packet.ServerboundSleepPacket;
import net.zaharenko424.casualties_cubed.network.packet.ServerboundSwapItemsPacket;
import net.zaharenko424.casualties_cubed.network.packet.ServerboundUseMedItemPacket;
import net.zaharenko424.casualties_cubed.registry.ModSounds;
import net.zaharenko424.casualties_cubed.util.ColorUtil;

import java.util.EnumMap;

import static net.minecraft.util.FastColor.ARGB32.alpha;

public class HealthScreen extends Screen {

    private final EnumMap<Limb, LimbWidget> limbWidgets = new EnumMap<>(Limb.class);

    private final MaybeBagItemWidget right, left;

    private final HealthInfoBoxWidget healthBox;
    private final SpecialUseButton specialUseButton;
    private CPRButton cprButton;
    private final Player target;
    private final Player localPlayer;

    public boolean BGmode = false;

    private final ImageButton switchMode;
    private boolean woundMode = true;
    private final ImageButton switchMainHandButton;
    private final ImageButton sleepButton;
    private final ImageButton workoutButton;

    public HealthScreen(Player target) {
        super(Component.empty());
        this.target = target;
        this.localPlayer = Minecraft.getInstance().player;

        PlayerHealthData data = PlayerHealthData.of(target).orElse(new PlayerHealthData());
        healthBox = new HealthInfoBoxWidget(0, 0, 128, 196, target, data);

        for (Limb limb : Limb.values()) {
            limbWidgets.put(limb, new LimbWidget(limb, target, data, () -> woundMode));
        }

        specialUseButton = new SpecialUseButton(localPlayer, target, data);

        switchMode = new ImageButton(22, 48, new RenderableImage(MODE_WOUND, 22, 48), button -> {
            woundMode = !woundMode;
            button.image().texture(woundMode ? MODE_WOUND : MODE_ARMOR);
            localPlayer.playSound(ModSounds.SMALL_CLICK.get());
        });
        switchMode.holdingTint = -1;

        switchMainHandButton = new ImageButton(24, 24, new RenderableImage(SWITCH_MAIN_HAND, 32, 32), () -> {
            Minecraft.getInstance().options.mainHand().set(localPlayer.getMainArm().getOpposite());
            localPlayer.playSound(ModSounds.CLICK.get());
        });
        switchMainHandButton.tooltip(Component.translatable("tooltip.casualties_cubed.switch_main_hand_button.title"), Component.translatable("tooltip.casualties_cubed.switch_main_hand_button.description"));
        switchMainHandButton.image().offset.set(-4, -4, 0);
        switchMainHandButton.image().texture(localPlayer.getMainArm() == HumanoidArm.RIGHT ? SWITCH_MAIN_HAND : SWITCHED_MAIN_HAND);

        workoutButton = new ImageButton(32, 32, new RenderableImage(WORKOUT, 32, 32), () -> {});
        workoutButton.tooltip(Component.translatable("tooltip.casualties_cubed.workout_button.title"), Component.translatable("tooltip.casualties_cubed.workout_button.description"));

        sleepButton = new ImageButton(32, 32, new RenderableImage(NAP_BUTTON, 32, 32), () -> {
            ModNetwork.CHANNEL.sendToServer(new ServerboundSleepPacket());
            onClose();
        });
        sleepButton.tooltip(Component.translatable("tooltip.casualties_cubed.sleep_button.title", SleepQuality.currentSleepQuality(localPlayer).comp), Component.translatable("tooltip.casualties_cubed.sleep_button.description"));
        sleepButton.active(data.canTakeNap());

        right = new MaybeBagItemWidget(this, HumanoidArm.RIGHT);
        left = new MaybeBagItemWidget(this, HumanoidArm.LEFT);
    }

    @Override
    protected void init() {
        super.init();
        int start_x = (this.width / 2) - 16;
        int start_y = (this.height / 4) - 25;
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

        right.offset.set(start_x - 48 - 16 - 8, start_y + 8);
        right.init();
        addRenderableWidget(right);
        left.offset.set(start_x + 32 + 48 + 8, start_y + 8);
        left.init();
        addRenderableWidget(left);

        addRenderableWidget(healthBox);
        addRenderableWidget(specialUseButton);
        addRenderableWidget(cprButton);
        cprButton.visible = target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(h -> h.consciousness() < 10).orElse(false);

        ModNetwork.CHANNEL.sendToServer(new ServerboundGuiSyncTogglePacket(target.getId(), true));

        updateLimbs();

        int y = this.height - MoodleManager.MOODLE_SIZE - 1;
        addWidget(MoodleManager.HEALTH_PANEL_BUTTON);

        switchMode.offset.set(6 + 11 + 134, 6 + 24 + 203, 0);
        addRenderableWidget(switchMode);

        switchMainHandButton.offset.set(width - 12, y - 32 - 2 - 32 - 2 - 12, 0);
        addRenderableWidget(switchMainHandButton);

        sleepButton.offset.set(width - 16, y - 16, 0);
        addRenderableWidget(sleepButton);

        workoutButton.offset.set(width - 16, y - 32 - 2 - 16, 0);
        addRenderableWidget(workoutButton);

        healthBox.init(this);
    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(graphics);
        switchMode.image().tint = ClientConfig.UI_GLOW_COLOR.get();
        switchMode.hoveringTint = ColorUtil.avg(switchMode.image().tint, -1);

        super.render(graphics, pMouseX, pMouseY, pPartialTick);

        limbWidgets.values().forEach(widget -> widget.renderSprites(graphics));

        MoodleManager.render(graphics, pPartialTick, width, height, false, pMouseX, pMouseY);

        if (!specialUseButton.visible() || !specialUseButton.isMouseOver(pMouseX, pMouseY)) {
            LimbWidget h = getHoveringWidget(pMouseX, pMouseY);

            if (h != null) {
                if (!BGmode) {
                    healthBox.selectLimb(h.limb());
                    specialUseButton.selectLimb(h);
                }
            }
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
        cprButton.visible = target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(h -> h.consciousness() < 10).orElse(false) && (target != Minecraft.getInstance().player);

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
        updateLimbs();

        right.tick();
        left.tick();

        if (!BGmode) {
            Minecraft.getInstance().player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                if (!data.isConscious()) onClose();
            });
        }

        PlayerHealthData data = PlayerHealthData.of(localPlayer).orElse(null);
        if (data == null) {
            onClose();
            return;
        }

        switchMainHandButton.image().texture(localPlayer.getMainArm() == HumanoidArm.RIGHT ? SWITCH_MAIN_HAND : SWITCHED_MAIN_HAND);
        sleepButton.tooltip(Component.translatable("tooltip.casualties_cubed.sleep_button.title", SleepQuality.currentSleepQuality(localPlayer).comp), Component.translatable("tooltip.casualties_cubed.sleep_button.description"));
        sleepButton.active(data.canTakeNap());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public void updateLimbs() {
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
        boolean anyConsumed = false;
        for (GuiEventListener listener : children()) {
            anyConsumed |= listener.mouseReleased(pMouseX, pMouseY, pButton);
        }
        return anyConsumed;
    }

    private LimbWidget getHoveringWidget(double pMouseX, double pMouseY) {
        for (LimbWidget widget : limbWidgets.values()) {
            if (widget.isMouseOver(pMouseX, pMouseY)) return widget;
        }
        return null;
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (specialUseButton.visible() && specialUseButton.isMouseOver(pMouseX, pMouseY)) {
            return specialUseButton.mouseClicked(pMouseX, pMouseY, pButton);
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    public void addItem(HealthScreenItemWidget widget) {
        if(children().contains(widget)) removeWidget(widget);
        addRenderableWidget(widget);
    }

    public void removeItem(HealthScreenItemWidget widget) {
        removeWidget(widget);
    }

    public void useItem(HealthScreenItemWidget widget, double x, double y) {
        LimbWidget limbWidget = getHoveringWidget(x ,y);
        if (limbWidget != null && !limbWidget.isAmputated()) {
            ItemStack stack = widget.stack();
            Limb limb = limbWidget.limb();
            InteractionHand hand = localPlayer.getMainArm() == widget.arm() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
            if (stack.getItem() instanceof AbstractBandage) {
                MinigameOpener.OpenBandageMinigame(target, stack, widget.slot(), limb, hand);
            } else if (stack.getItem() instanceof IMedicalMinigameUsable helper) {
                helper.openMinigameBagScreen(target, stack, widget.bagStack(), widget.slot(), limb, hand);
            }

            if (stack.is(CasualtiesCubedTags.Item.CAUTERIZE) || stack.getItem() instanceof ISimpleMedicalUsable) {
                ModNetwork.CHANNEL.sendToServer(new ServerboundUseMedItemPacket(target.getId(), limb, hand, (byte) widget.slot()));
            }

            return;
        }

        if ((getChildAt(x, y).orElse(null) instanceof HealthScreenItemWidget itemWidget) && widget != itemWidget) {
            ModNetwork.CHANNEL.sendToServer(
                    new ServerboundSwapItemsPacket(widget.arm(), (byte) widget.slot(), itemWidget.arm(), (byte) itemWidget.slot())
            );
        }
    }

    private static final ResourceLocation MODE_WOUND = CasualtiesCubed.texLoc("gui/mode_wound");
    private static final ResourceLocation MODE_ARMOR = CasualtiesCubed.texLoc("gui/mode_armor");
    private static final ResourceLocation SWITCH_MAIN_HAND = CasualtiesCubed.texLoc("gui/switch_main_hand");
    private static final ResourceLocation SWITCHED_MAIN_HAND = CasualtiesCubed.texLoc("gui/switched_main_hand");
    private static final ResourceLocation WORKOUT = CasualtiesCubed.texLoc("gui/workout");
    private static final ResourceLocation NAP_BUTTON = CasualtiesCubed.texLoc("gui/nap");
}
