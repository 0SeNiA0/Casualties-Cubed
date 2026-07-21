package net.zaharenko424.casualties_cubed.client.gui.minigames;

import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.client.gui.HealthScreen;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import net.zaharenko424.casualties_cubed.network.ModNetwork;
import net.zaharenko424.casualties_cubed.network.packet.ServerboundDislocationTryPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.joml.Vector2d;

import java.util.Optional;

public class DislocationMinigameScreen extends Screen {

    private static final ResourceLocation BONE_TEX = CasualtiesCubed.resourceLoc("textures/gui/limbs/bone2.png");

    private final Screen parent;
    private final Player target;
    private final Limb limb;

    private HandObject handObject;

    private BoneObject boneObject;

    public DislocationMinigameScreen(Screen parent, Player target, Limb limb) {
        super(Component.translatable("casualties_cubed.gui.minigame.dislocation"));
        this.parent = parent;
        this.target = target;
        this.limb = limb;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        parent.render(guiGraphics, mouseX, mouseY, partialTicks);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        guiGraphics.fill(0, 0, width, height, 0x88000000);
        Minecraft mc = Minecraft.getInstance();

        guiGraphics.drawCenteredString(mc.font, Component.translatable("casualties_cubed.gui.dislocation_instruction1"), this.width / 2, 10, 0xFFFFFF);
        //guiGraphics.drawCenteredString(mc.font,Component.translatable("prototype_pain.gui.dislocation_instruction2"),this.width/2,20,0xFFFFFF);

        guiGraphics.drawCenteredString(mc.font, (int) boneObject.getFakeDislocation() + "%", this.width / 2, this.height / 6 + 175, 0xCC0000);
        guiGraphics.drawCenteredString(mc.font, Component.translatable("casualties_cubed.gui.minigame_exit"), this.width / 2, this.height / 6 + 190, 0xFFFFFF);

        guiGraphics.blit(BONE_TEX, this.width / 2 - 160, this.height / 2 - 40, 0, 0, 160, 80, 160, 80);
        guiGraphics.setColor(0.5f, 0.5f, 0.5f, 0.1f);
        guiGraphics.blit(BONE_TEX, this.width / 2, this.height / 2 - 40, 160, 0, -160, 80, 160, 80);
        guiGraphics.setColor(1, 1, 1, 1f);
        boneObject.render(guiGraphics);

        handObject.render(guiGraphics, partialTicks);
    }

    private double lastpMouseX = 100, lastpMouseY = 100;

    public int endtick = -40;

    @Override
    public void tick() {
        super.tick();
        parent.tick();
        handObject.update(lastpMouseX, lastpMouseY);
        boneObject.update();
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            Optional<Float> cons = player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(PlayerHealthData::getConsciousness);
            Optional<Double> pain = player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(PlayerHealthData::getAveragePain);
            float consscale = (cons.orElse(100f) / 100) * 0.15f;
            float painscale = (float) (pain.orElse(0d) / 100);
            handObject.setShakeScale(painscale);
            handObject.setStiffness(consscale);
            if (handObject.isIs_clicked() && boneObject.isInside(lastpMouseX, lastpMouseY) && pain.orElse(0d) < 75) {
                Vector2d vel = new Vector2d(handObject.getVx(), handObject.getVy());
                boneObject.onHit(vel, target, limb);
            }
        }
        Minecraft.getInstance().player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h -> {
            if (h.getConsciousness() <= 10)
                onClose();
        });

        if (boneObject.isEndCondition()) {
            endtick++;
            if (endtick >= 0) {
                onClose();
            }
        }
    }

    @Override
    public void onClose() {
        super.onClose();
        if (boneObject.isEndCondition()) {
            ModNetwork.CHANNEL.sendToServer(new ServerboundDislocationTryPacket(target.getId(), limb, 0));
        }
        if (parent instanceof HealthScreen hp) {
            hp.BGmode = false;
        }
        Minecraft.getInstance().setScreen(parent);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        handObject.mouseClicked();
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
        handObject.mouseReleased();
        return super.mouseReleased(pMouseX, pMouseY, pButton);
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

        handObject = new HandObject(spriteType, this.width / 2, this.height / 2, this.width, this.height / 3 * 2);
        if (parent instanceof HealthScreen hp) {
            hp.BGmode = true;
        }

        float dislocation = target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(data -> data.getLimb(limb).getDislocationTimer()).orElse(0f);
        boneObject = new BoneObject(this.width / 2, this.height / 2 - 40, dislocation);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
