package net.zaharenko424.casualties_cubed.item.bandages;

import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.item.api.AbstractBandage;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.LimbStatistics;
import org.jetbrains.annotations.Nullable;

public class SterilizedDressingItem extends AbstractBandage {

    public SterilizedDressingItem() {
        super(new Item.Properties().stacksTo(1), FastColor.ARGB32.color(255, 128, 128, 128));
    }

    @Override
    public float durabilityScale(float angle) {
        return angle / 12 * 100;
    }

    @Override
    public void useBandageAction(float amount, Player target, @Nullable Limb limb) {
        target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
            LimbStatistics stats = data.getLimb(limb);

            stats.addSkinHealAmount(amount * 0.3f);
            stats.addBandageSlowAmount(amount * 0.45f);
            stats.addPain(-amount * 0.6f);
            stats.addBoneHealTimer(-amount * 0.2f);
            stats.addDislocationTimer(-amount * 0.2f);
            stats.addDisinfectionTimer(amount * 9);
        });
    }
}
