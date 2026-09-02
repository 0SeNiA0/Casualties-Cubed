package net.zaharenko424.casualties_cubed.client.gui.minigames;

import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.client.gui.screen.HealthScreen;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.joml.Vector2d;

import java.util.Optional;

public class CPRMinigameScreen extends Screen implements Minigame {
    
    private final Screen parent;
    private final Player target;

    private CPRTimer cprTimer;
    private HandObject leftHandObject;
    private HandObject rightHandObject;

    private int CorrX= 0;
    private int CorrY=0;
    public CPRMinigameScreen(Screen parent, Player target) {
        super(Component.translatable("casualties_cubed.gui.minigame.cpr"));
        this.parent = parent;
        this.target = target;
    }

    public boolean isLAmputated(){
        Minecraft mc = Minecraft.getInstance();
        if (mc.player==null) return false;
        return mc.player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(h->{
            if (h.isAmputated(Limb.LEFT_HAND))
                return true;
            return false;
        }).orElse(false);
    }
    public boolean isRAmputated(){
        Minecraft mc = Minecraft.getInstance();
        if (mc.player==null) return false;
        return mc.player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(h->{
            if (h.isAmputated(Limb.RIGHT_HAND))
                return true;
            return false;
        }).orElse(false);
    }

    @Override
    protected void init() {
        super.init();
        HandObject.SpriteType spriteType;
        if (isLAmputated()) {
            spriteType= HandObject.SpriteType.GONE;
        }else {
            spriteType= HandObject.SpriteType.NORMAL;
        }
        leftHandObject = new HandObject(spriteType,this.width/2,this.height/2,this.width,(int) ((float) this.height /3*1.5f));
        if (isRAmputated()) {
            spriteType= HandObject.SpriteType.GONE;
        }else {
            spriteType= HandObject.SpriteType.NORMAL;
        }
        rightHandObject = new HandObject(spriteType,this.width/2,this.height/2,this.width,(int) ((float) this.height /3*2.5f));
        if (parent instanceof HealthScreen hp){
            hp.BGmode = true;
        }
        rightHandObject.mouseClicked();
        cprTimer = new CPRTimer(this.width/5,this.height/2);
    }

    private double lastpMouseX=100,lastpMouseY=100;
    @Override
    public void tick() {
        parent.tick();
        leftHandObject.update(lastpMouseX,lastpMouseY);
        rightHandObject.update(lastpMouseX,lastpMouseY);
        Player player = Minecraft.getInstance().player;
        if (player!=null){
            Optional<Float> cons=  player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(PlayerHealthData::consciousness);
            Optional<Float> pain = player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(PlayerHealthData::averagePain);
            float consscale = (cons.orElse(100f)/100)*0.15f;
            float painscale = pain.orElse(0f)/100;
            leftHandObject.setShakeScale(painscale);
            leftHandObject.setStiffness(consscale);
            rightHandObject.setShakeScale(painscale);
            rightHandObject.setStiffness(consscale);
        }
        Minecraft.getInstance().player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data->{
            if (!data.isConscious()) onClose();
        });
        super.tick();
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        float distance_from_correct = (float) new Vector2d(pMouseX,pMouseY).distance(new Vector2d(CorrX,CorrY));
        if (distance_from_correct<50){
            cprTimer.mouseClicked(distance_from_correct,target);
        }
        leftHandObject.mouseClicked();
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
        leftHandObject.mouseReleased();
        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }




    @Override
    public void onClose() {
        super.onClose();
        if (parent instanceof HealthScreen hp){
            hp.BGmode = false;
        }
        Minecraft.getInstance().setScreen(parent);
    }



    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        parent.render(guiGraphics, mouseX, mouseY, partialTicks);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        guiGraphics.fill(0,0,width,height,0x88000000);
        Minecraft mc = Minecraft.getInstance();


        guiGraphics.drawCenteredString(mc.font,Component.translatable("casualties_cubed.gui.cpr_instruction1"),this.width/2,10,0xFFFFFF);
        guiGraphics.drawCenteredString(mc.font,Component.translatable("casualties_cubed.gui.cpr_instruction2"),this.width/2,20,0xFFFFFF);
        guiGraphics.drawCenteredString(mc.font,Component.translatable("casualties_cubed.gui.minigame_exit"),this.width/2-40,this.height / 6+190,0xFFFFFF);

        cprTimer.render(guiGraphics,partialTicks);
        guiGraphics.blit(CasualtiesCubed.resourceLoc("textures/gui/cpr_body.png"),
                this.width/2-50,this.height/4-20,0,0,256,256,256,256
        );
        int correctx = this.width/2-50 + 128;
        int correcty = this.height/4-20+128;
        CorrX =correctx;
        CorrY = correcty;
        leftHandObject.render(guiGraphics,partialTicks);
        rightHandObject.render(guiGraphics,partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
