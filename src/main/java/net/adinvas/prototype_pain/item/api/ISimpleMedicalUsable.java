package net.adinvas.prototype_pain.item.api;

import net.adinvas.prototype_pain.limbs.Limb;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;

public interface ISimpleMedicalUsable {

    ItemStack onMedicalUse(Limb limb, ServerPlayer source, ServerPlayer target, ItemStack stack);

    default SoundEvent getUseSound(){
        return SoundEvents.DYE_USE;
    };
}
