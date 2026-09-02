package net.zaharenko424.casualties_cubed.client;

import net.zaharenko424.casualties_cubed.registry.ModSounds;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.client.ticksounds.PainDrone;
import net.zaharenko424.casualties_cubed.client.ticksounds.TinnitusSound;
import net.zaharenko424.casualties_cubed.config.ClientConfig;
import net.minecraft.client.Minecraft;

public class SoundManager {

    public static PainDrone painDrone;
    public static TinnitusSound tinnitusSound;

    public static void tick() {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null) return;
        if (!ClientConfig.EXPERIMENTAL_SOUNDS.get())return;

        boolean shouldPlay = mc.player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA)
                .map(h -> h.averagePain() > 50)
                .orElse(false);

        if (shouldPlay && (painDrone == null || painDrone.isStopped())) {
            painDrone = new PainDrone(ModSounds.PAINDRONE.get());
            mc.getSoundManager().play(painDrone);
        }

        shouldPlay = mc.player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA)
                .map(h -> h.flashHearingLoss() > 0)
                .orElse(false);
        if (shouldPlay && (tinnitusSound == null || tinnitusSound.isStopped())) {
            tinnitusSound = new TinnitusSound(ModSounds.RINGING.get());
            mc.getSoundManager().play(tinnitusSound);
        }

        if (painDrone != null) {
            painDrone.update(mc.player);
            if (painDrone.isStopped()) {
                painDrone = null;
            }
        }

        if (tinnitusSound !=null){
            tinnitusSound.update(mc.player);
            if (tinnitusSound.isStopped()){
                tinnitusSound = null;
            }
        }
    }
}
