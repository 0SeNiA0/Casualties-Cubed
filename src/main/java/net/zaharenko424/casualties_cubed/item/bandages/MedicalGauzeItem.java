package net.zaharenko424.casualties_cubed.item.bandages;

import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.item.api.AbstractBandage;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.LimbStatistics;
import org.jetbrains.annotations.Nullable;

public class MedicalGauzeItem extends AbstractBandage {

    public MedicalGauzeItem() {
        super(new Properties().stacksTo(1), FastColor.ARGB32.color(255, 158, 167, 194));
    }

    @Override
    public float durabilityScale(float angle) {
        return angle / 15 * 100;
    }

    @Override
    public void useBandageAction(float amount, Player target, @Nullable Limb limb) {
        target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
            LimbStatistics stats = data.getLimb(limb);

            stats.addSkinHealAmount(amount * 0.2f);
            stats.addBandageSlowAmount(amount * 0.5f);
            stats.addPain(-amount * 3);
            data.setPendingOpioids(data.getPendingOpioids() + amount * 0.28f);
        });
    }
}
