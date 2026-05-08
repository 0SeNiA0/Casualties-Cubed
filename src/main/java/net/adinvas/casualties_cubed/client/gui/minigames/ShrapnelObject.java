package net.adinvas.casualties_cubed.client.gui.minigames;

import net.adinvas.casualties_cubed.CasualtiesCubed;
import net.adinvas.casualties_cubed.limbs.Limb;
import net.adinvas.casualties_cubed.network.ModNetwork;
import net.adinvas.casualties_cubed.network.packet.ServerboundShrapnelFailPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class ShrapnelObject extends GrabObject {
    
    int dx= 0;
    private final int minstickY;
    private final int maxY;
    private final Player target;
    private final Limb limb;

    public ShrapnelObject(int x, int y, int minstickY, int maxY, Player target, Limb limb) {
        super(x, y, 7, 5, 19, 129,
                CasualtiesCubed.resourceLoc("textures/gui/limbs/shrapnel.png")
                , 32, 160, 1);
        dx= x;
        this.minstickY = minstickY;
        this.maxY = maxY;
        this.target = target;
        this.limb = limb;
    }


    public void update(float yVel,boolean ignorevel){
        Minecraft mc = Minecraft.getInstance();
        if (yVel>1.6&&dragging&&!ignorevel&& isSticked()){
            if (mc.player == target){
                dragging =false;
            }
            fail();
        }
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int button) {
        if (dragging && button == 0) {
            if (this.y<minstickY){
                this.x = (int) mouseX - dragOffsetX;
                this.dx = (int) mouseX - dragOffsetX;
            }else {
                this.dx = (int) mouseX - dragOffsetX;
            }
            this.y = (int) mouseY - dragOffsetY;
            if (this.y>maxY){
                this.y = maxY;
            }
            if(Math.abs(this.x-this.dx)>10&&isSticked()){
                dragging= false;
                fail();
            }
        }
    }

    public boolean isSticked(){
        return this.y>minstickY;
    }

    public void fail(){
        ModNetwork.CHANNEL.sendToServer(new ServerboundShrapnelFailPacket(target.getId(), limb));
    }
}
