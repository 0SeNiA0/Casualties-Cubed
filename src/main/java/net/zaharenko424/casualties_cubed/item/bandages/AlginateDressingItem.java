package net.zaharenko424.casualties_cubed.item.bandages;

import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.item.api.AbstractBandage;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.LimbStatistics;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class AlginateDressingItem extends AbstractBandage {

    public AlginateDressingItem() {
        super(new Properties().stacksTo(1), Color.WHITE.getRGB());
    }

    @Override
    public float durabilityScale(float angle) {
        return angle / 15 * 100;
    }

    @Override
    protected void useBandageAction(float amount, Player target, @Nullable Limb limb) {
        target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h -> {
            LimbStatistics stats = h.getLimb(limb);

            stats.addSkinHealAmount(amount * 1.25f);
            stats.addBandageSlowAmount(amount * 0.725f);
            stats.addPain(-amount * 0.8f);
            stats.addDisinfectionTimer(amount * 8);
        });
    }
}
