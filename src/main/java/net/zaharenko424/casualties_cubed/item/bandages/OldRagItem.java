package net.zaharenko424.casualties_cubed.item.bandages;

import net.minecraft.util.FastColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.item.api.AbstractBandage;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.LimbStatistics;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import org.jetbrains.annotations.Nullable;

public class OldRagItem extends AbstractBandage {

    public OldRagItem() {
        super(new Properties().stacksTo(1), FastColor.ARGB32.color(255, 143, 126, 139));
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
            stats.addBandageSlowAmount(amount * 0.1f);
            stats.addPain(-amount * 0.25f);
            stats.addBoneHealTimer(-amount * 0.05f);
            stats.addDislocationTimer(-amount * 0.05f);
        });
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pPlayer.isInWaterOrBubble()) return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));

        if (pLevel.isClientSide) return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));

        PlayerHealthData.of(pPlayer).ifPresent(data -> data.setWetness(data.getWetness() * 0.5f));

        return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
    }
}
