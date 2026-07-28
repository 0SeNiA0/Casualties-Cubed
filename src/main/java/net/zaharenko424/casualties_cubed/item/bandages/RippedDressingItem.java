package net.zaharenko424.casualties_cubed.item.bandages;

import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.item.api.AbstractBandage;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.LimbStatistics;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class RippedDressingItem extends AbstractBandage {

    public RippedDressingItem() {
        super(new Properties().stacksTo(1), Color.WHITE.getRGB());
    }

    @Override
    public float durabilityScale(float angle) {
        return angle / 8 * 100;
    }

    @Override
    public void useBandageAction(float amount, Player target, @Nullable Limb limb) {
        target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
            LimbStatistics stats = data.getLimb(limb);

            stats.addSkinHealAmount(amount * 0.08f);
            stats.addBandageSlowAmount(amount * 0.18f);
            stats.addPain(-amount * 0.4f);
            stats.addBoneHealTimer(-amount * 0.05f);
            stats.addDislocationTimer(-amount * 0.05f);
        });
    }
}
