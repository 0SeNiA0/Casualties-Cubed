package net.zaharenko424.casualties_cubed.item.usable;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.item.api.ItemWithDescription;
import net.zaharenko424.casualties_cubed.item.api.IAllowInMedicBags;
import net.zaharenko424.casualties_cubed.item.api.INbtDrivenDurability;
import net.zaharenko424.casualties_cubed.item.api.ISimpleMedicalUsable;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.LimbStatistics;
import net.zaharenko424.casualties_cubed.registry.TimedEffectRegistry;

public class MedicalSutureItem extends ItemWithDescription implements ISimpleMedicalUsable, IAllowInMedicBags, INbtDrivenDurability {

    public MedicalSutureItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public void onMedicalUse(ServerPlayer source, ServerPlayer target, Limb limb, ItemStack stack) {
        target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
            LimbStatistics stats = data.getLimb(limb);

            stats.addPain(12.5f);
            stats.addSkinHealAmount(25);
            data.addTimedEffect(TimedEffectRegistry.SUTURE, 0, limb, 10);
        });

        if (!source.isCreative()) subNbtDurability(stack, 51);
        source.level().playSound(null, source.getOnPos(), getUseSound(), SoundSource.PLAYERS);
    }

    @Override
    public Component getName(ItemStack pStack) {
        return appendDurability(pStack, Component.empty().append(super.getName(pStack)));
    }

    @Override
    public SoundEvent getUseSound() {
        return SoundEvents.BONE_MEAL_USE;
    }
}
