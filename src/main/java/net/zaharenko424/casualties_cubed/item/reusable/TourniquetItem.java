package net.zaharenko424.casualties_cubed.item.reusable;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.item.api.ItemWithDescription;
import net.zaharenko424.casualties_cubed.item.api.IAllowInMedicBags;
import net.zaharenko424.casualties_cubed.item.api.ISimpleMedicalUsable;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.LimbStatistics;

public class TourniquetItem extends ItemWithDescription implements ISimpleMedicalUsable, IAllowInMedicBags {

    public TourniquetItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public void onMedicalUse(ServerPlayer source, ServerPlayer target, Limb limb, ItemStack stack) {
        if (limb == Limb.THORAX || limb == Limb.ABDOMEN) return;
        target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
            LimbStatistics stats = data.getLimb(limb);
            if (stats.isTourniquet()) return;

            stats.setTourniquet(true);

            if (!source.isCreative()) stack.shrink(1);
            source.level().playSound(null, source.getOnPos(), getUseSound(), SoundSource.PLAYERS);
        });
    }
}
