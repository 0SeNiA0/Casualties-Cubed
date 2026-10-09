package net.zaharenko424.casualties_cubed.item.usable;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.item.api.ItemWithDescription;
import net.zaharenko424.casualties_cubed.item.api.IAllowInMedicBags;
import net.zaharenko424.casualties_cubed.item.api.INbtDrivenDurability;
import net.zaharenko424.casualties_cubed.item.api.ISimpleMedicalUsable;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.LimbStatistics;

public class HeatPackItem extends ItemWithDescription implements ISimpleMedicalUsable, IAllowInMedicBags, INbtDrivenDurability {

    public HeatPackItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public void onMedicalUse(ServerPlayer source, ServerPlayer target, Limb limb, ItemStack stack) {
        target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
            data.temperature(data.temperature() + 1f);
            LimbStatistics stats = data.getLimb(limb);

            stats.addMuscleHealth(20);
        });

        if (!source.isCreative()) subNbtDurability(stack, 50);
        source.level().playSound(null, source.getOnPos(), getUseSound(), SoundSource.PLAYERS);
    }

    @Override
    public Component getName(ItemStack pStack) {
        return appendDurability(pStack, Component.empty().append(super.getName(pStack)));
    }
}
