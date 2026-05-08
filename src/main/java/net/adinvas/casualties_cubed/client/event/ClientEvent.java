package net.adinvas.casualties_cubed.client.event;

import com.mojang.blaze3d.platform.InputConstants;
import net.adinvas.casualties_cubed.PlayerHealthProvider;
import net.adinvas.casualties_cubed.CasualtiesCubed;
import net.adinvas.casualties_cubed.client.Keybinds;
import net.adinvas.casualties_cubed.client.SoundManager;
import net.adinvas.casualties_cubed.client.gui.FluidExchangeScreen;
import net.adinvas.casualties_cubed.client.gui.HealthScreen;
import net.adinvas.casualties_cubed.client.overlays.OverlayController;
import net.adinvas.casualties_cubed.event.CommonEvent;
import net.adinvas.casualties_cubed.item.multi_tank.MultiTankFluidItem;
import net.adinvas.casualties_cubed.limbs.Limb;
import net.adinvas.casualties_cubed.limbs.PlayerHealthData;
import net.adinvas.casualties_cubed.network.ModNetwork;
import net.adinvas.casualties_cubed.network.packet.ServerboundGiveUpPacket;
import net.adinvas.casualties_cubed.network.packet.ServerboundLegUsePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.gui.overlay.NamedGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.PlayLevelSoundEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.atomic.AtomicBoolean;

@Mod.EventBusSubscriber(modid = CasualtiesCubed.MOD_ID, value = Dist.CLIENT)
public class ClientEvent {

    static int GiveUpTime = 40;
    static int WaitTimer = 0;

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        OverlayController.renderShaderOverlay(event);
    }

    @SubscribeEvent
    public static void onScreenClick(ScreenEvent.MouseButtonPressed.Pre event) {
        Screen screen = event.getScreen();
        if (!(screen instanceof AbstractContainerScreen<?> containerScreen) || event.getButton() != InputConstants.MOUSE_BUTTON_RIGHT) return;

        containerScreen.mouseMoved(event.getMouseX(), event.getMouseY());
        Slot hovered = containerScreen.getSlotUnderMouse();
        if (hovered == null) return;

        ItemStack toStack = hovered.getItem();
        ItemStack fromStack = containerScreen.getMenu().getCarried();
        if (fromStack.isEmpty() || !(fromStack.getItem() instanceof MultiTankFluidItem fromTank)
                || toStack.isEmpty() || !(toStack.getItem() instanceof MultiTankFluidItem toTank)) return;

        if (toTank.getHandler(toStack).getTank().getFreeSpace() == 0
                || fromTank.getHandler(fromStack).getTank().getTotalFluid() == 0) return;

        Minecraft.getInstance().pushGuiLayer(new FluidExchangeScreen(containerScreen, hovered.getItem(), fromStack, hovered.index));

        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.ClientTickEvent event){
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        ProfilerFiller profiler = mc.getProfiler();
        SoundManager.tick();
        if (WaitTimer > 0) {
            WaitTimer--;
        }
        profiler.push("casualties_cubed:client_misc");

        AtomicBoolean uncontious = new AtomicBoolean(false);
        player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
            if (data.getConsciousness() <= 10) {
                uncontious.set(true);
            }
        });

        if (Keybinds.OPEN_PAIN_GUI.isDown() && !uncontious.get()) {
            Keybinds.OPEN_PAIN_GUI.consumeClick();
            if (WaitTimer <= 0) {
                Player target = CommonEvent.getLookedAtPlayer(player, 2);
                boolean self = target == null || player.isShiftKeyDown();


                if (self) {
                    Minecraft.getInstance().setScreen(new HealthScreen(player.getUUID()));
                } else {
                    Minecraft.getInstance().setScreen(new HealthScreen(target.getUUID()));
                }

            } else {
                Keybinds.OPEN_PAIN_GUI.setDown(false);
            }
        }

        player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
            if (data.getConsciousness() <= 10) {
                if (Keybinds.GIVE_UP.isDown()) {
                    GiveUpTime--;
                } else {
                    GiveUpTime = 40;
                }
            }
        });

        if (GiveUpTime <= 0) {
            ModNetwork.CHANNEL.sendToServer(new ServerboundGiveUpPacket());
            GiveUpTime = 40;
        }

        profiler.pop();

        player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h -> {
            float contiousness = (100 - h.getConsciousness()) / 100;

            if (contiousness <= 10) {
                mc.player.setYRot(mc.player.yRotO); // reset yaw
                mc.player.setXRot(mc.player.xRotO); // reset pitch
            }
        });
    }

    @SubscribeEvent
    public static void onScreenKey(ScreenEvent.KeyPressed.Post event){
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        if (event.getScreen() instanceof HealthScreen){
            if (Keybinds.OPEN_PAIN_GUI.matches(event.getKeyCode(), event.getScanCode())) {
                event.getScreen().onClose();
                mc.setScreen(null);
                Keybinds.OPEN_PAIN_GUI.setDown(false);
                Keybinds.OPEN_PAIN_GUI.consumeClick();
                WaitTimer = 5;
            }
        }
    }

    @SubscribeEvent
    public static void onInputUpdate(MovementInputUpdateEvent event) {
        event.getEntity().getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
            if (data.getConsciousness() < 10) {
                event.getInput().down = false;
                event.getInput().forwardImpulse = 0;
                event.getInput().jumping = false;
                event.getInput().up = false;
                event.getInput().left = false;
                event.getInput().leftImpulse= 0;
                event.getInput().right =false;
                event.getInput().shiftKeyDown = false;
            }

            if (event.getInput().leftImpulse != 0 || event.getInput().forwardImpulse != 0 || event.getInput().jumping){
                ModNetwork.CHANNEL.sendToServer(new ServerboundLegUsePacket());
            }
        });
    }
    private static final ResourceLocation pain_tex = CasualtiesCubed.resourceLoc("textures/gui/icons/pain.png");

    @SubscribeEvent
    public static void onOpenInventory(ScreenEvent.Opening event) {
        if (!(event.getScreen() instanceof InventoryScreen)) return;

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
            if (data.getConsciousness() <= 10) {
                event.setCanceled(true); // block opening inventory
            }
        });
    }

    @SubscribeEvent
    public static void onContainerDraw(ScreenEvent.Render event) {//TODO make separate menu for fluid transfer. alternatively keep prev menu "open" while in fluid transfer screen
        if (!(event.getScreen() instanceof AbstractContainerScreen<?> screen))return;
        Slot hovered = screen.getSlotUnderMouse();

        ItemStack carried = screen.getMenu().getCarried();
        if (hovered != null && carried.getItem() instanceof MultiTankFluidItem from && hovered.getItem().getItem() instanceof MultiTankFluidItem to){
            GuiGraphics guiGraphics = event.getGuiGraphics();
            int mouseX = event.getMouseX();
            int mouseY = event.getMouseY();
            Component text = Component.translatable("casualties_cubed.gui.fluid_screen",Component.keybind("key.casualties_cubed.fluid_screen"));
            guiGraphics.renderTooltip(Minecraft.getInstance().font, text,mouseX,mouseY);
        }
    }

    public static float lastStab = 0;
    public static float stab_alpha = 0;

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Pre event) {
        NamedGuiOverlay overlay = event.getOverlay();
        if (overlay != VanillaGuiOverlay.PLAYER_HEALTH.type()
                && overlay != VanillaGuiOverlay.ARMOR_LEVEL.type()
                && overlay != VanillaGuiOverlay.AIR_LEVEL.type()) return;

        if (overlay != VanillaGuiOverlay.AIR_LEVEL.type()) {
            event.setCanceled(true);
            return;
        }

        // cancel rendering of the vanilla health bar
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        float Oxygen = player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA)
                .map(PlayerHealthData::getOxygen)
                .orElse(0f);

        float stab = player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA)
                .map(PlayerHealthData::getStability)
                .orElse(100f);

        lastStab = Mth.lerp(0.1f, lastStab, stab);

        double Pain = player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA)
                        .map(PlayerHealthData::getTotalPain)
                                .orElse(0d);

        GuiGraphics gui = event.getGuiGraphics();

        int x = mc.getWindow().getGuiScaledWidth() / 2 - 91; // same place as hearts
        int y = mc.getWindow().getGuiScaledHeight() - 39;

        int size = (int) ((lastStab / 100) * 180);
        if (stab >= 100){
            stab_alpha = 0;
        }else {
            stab_alpha = Mth.lerp(event.getPartialTick(), stab_alpha,1);
        }

        int alpha = (int) (stab_alpha *255);

        int argb = (alpha << 24) | (0x00FFFF & 0xFFFFFF);

        gui.drawString(mc.font,"O₂ " + (int)Oxygen + "%", x, y,0xFFFFFF);
        gui.blit(pain_tex,x + 50,y - 2,0,0,10,10,10,10);
        gui.drawString(mc.font, (int) Pain + "%",x + 60,y,0xFFFFFF);
        gui.fill(x + (90 - size / 2),y - 20,x + 90 + (size / 2),y - 22, argb);
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        // Cancel rendering
        Minecraft mc =  Minecraft.getInstance();
        mc.player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
            HumanoidArm arm = mc.player.getMainArm();
            Limb limb = arm == HumanoidArm.LEFT ? Limb.LEFT_ARM : Limb.RIGHT_ARM;

            if (data.isAmputated(limb)) event.setCanceled(true);
        });
    }

    @SubscribeEvent
    public static void onPlaySound(PlayLevelSoundEvent event){
        Minecraft mc =  Minecraft.getInstance();
        if (mc.player == null) return;

        float soundPenalty = mc.player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(PlayerHealthData::getHearingLoss).orElse(0f);
        if (soundPenalty <= 0) return;

        event.setNewVolume(event.getOriginalVolume() * (1 - soundPenalty));

        if (event.getNewVolume() <= 0){
            event.setCanceled(true);
        }
    }
}