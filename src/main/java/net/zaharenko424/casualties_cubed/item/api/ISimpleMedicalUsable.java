package net.zaharenko424.casualties_cubed.item.api;

import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;

public interface ISimpleMedicalUsable {

    void onMedicalUse(Limb limb, ServerPlayer source, ServerPlayer target, ItemStack stack);

    default SoundEvent getUseSound(){
        return SoundEvents.DYE_USE;
    };
}
