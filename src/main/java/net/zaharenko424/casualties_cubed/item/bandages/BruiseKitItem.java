package net.zaharenko424.casualties_cubed.item.bandages;

import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.item.api.AbstractBandage;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.LimbStatistics;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class BruiseKitItem extends AbstractBandage {

    public BruiseKitItem() {
        super(new Properties().stacksTo(1), Color.GREEN.getRGB());
    }

    @Override
    public float durabilityScale(float angle) {
        return angle / 10 * 100;
    }

    @Override
    protected void useBandageAction(float amount, Player target, @Nullable Limb limb) {
        target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data->{
            LimbStatistics stats = data.getLimb(limb);

            stats.addSkinHealAmount(amount * 1);

            //Timed muscle regen
            stats.addMuscleHealth(1 * amount * 1.4f);//duration = amount * 1.4

            stats.addPain(-amount * 0.8f);
            stats.addDislocationTimer(-amount * 0.8f);
        });
    }
}
