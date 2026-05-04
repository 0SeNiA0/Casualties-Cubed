package net.adinvas.prototype_pain.client.ticksounds;

import net.adinvas.prototype_pain.registry.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class HeartBeatSound {

    private final Player player;
    private double tickDelay;
    private float bpm;
    private float targetBPM;
    private float volume;
    private float pitch;

    public HeartBeatSound(Player player, float BPM) {
        this.player = player;
       this.bpm = BPM;
       this.targetBPM = BPM;
        this.volume = 1.0f;
        this.pitch = 1.0f;

    }



    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public void setBPM(float newBPM) {
        this.targetBPM = newBPM;
    }

    public void tick() {
        if (tickDelay > 0) {
            tickDelay--;
        }

        if (tickDelay <= 0) {
            // play one heartbeat thump
            Minecraft.getInstance().getSoundManager().play(
                    SimpleSoundInstance.forUI(ModSounds.HEART_BEAT.get(), pitch, volume)
            );

            // update bpm smoothly
            bpm = (float) Mth.lerp(0.5, bpm, targetBPM);
            pitch = 1f +(((bpm-70)/100)*0.5f);
            // reset delay based on the *new* bpm
            tickDelay = Math.max(1, (int) (1200 / bpm)); // 20 ticks/sec * 60 sec/min
        }
    }


}
