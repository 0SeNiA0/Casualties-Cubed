package net.zaharenko424.casualties_cubed.item.multi_tank;

import net.zaharenko424.casualties_cubed.registry.ModMedicalFluids;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.fluid_system.MultiTankHelper;
import net.zaharenko424.casualties_cubed.item.api.IAllowInMedicBags;
import net.zaharenko424.casualties_cubed.item.api.ISimpleMedicalUsable;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;

public class MakeshiftLRDItem extends MultiTankFluidItem implements ISimpleMedicalUsable, IAllowInMedicBags {

    public MakeshiftLRDItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public int getCapacity() {
        return 50;
    }

    @Override
    public void onMedicalUse(ServerPlayer source, ServerPlayer target, Limb limb, ItemStack stack) {
        if (MultiTankHelper.getAmountOfFluid(stack, ModMedicalFluids.LRD_SERUM.get().getAsStack(1)) >= 25) {
            if (!source.isCreative()) MultiTankHelper.drainSpecificFluid(stack, 25, ModMedicalFluids.LRD_SERUM.get().getAsStack(1));
            target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.setLimbMuscleHealth(limb, data.getLimbMuscleHealth(limb) + 50f);
                data.setLimbInfection(limb, data.getLimbInfection(limb) - 10);
                data.setLimbDisinfected(limb, Math.max(data.getLimbDisinfected(limb), 12000));
                data.setLimbBleedRate(limb, data.getLimbBleedRate(limb) * 0.7f);
                data.setInternalBleeding(data.getInternalBleeding() * 0.45f);
                data.setPendingOpioids(data.getPendingOpioids() + 20);

                for (Limb limb1 : limb.getConnectedLimbs()) {
                    data.setLimbMuscleHealth(limb1, data.getLimbMuscleHealth(limb1) + 40f);
                    data.setLimbInfection(limb1, data.getLimbInfection(limb1) - 5);
                    data.setLimbDisinfected(limb1, Math.max(data.getLimbDisinfected(limb1), 6000));
                    data.setLimbBleedRate(limb1, data.getLimbBleedRate(limb1) * 0.75f);
                }
            });

            source.level().playSound(null, source.getOnPos(), getUseSound(), SoundSource.PLAYERS);
        }
    }

    @Override
    public SoundEvent getUseSound() {
        return SoundEvents.BONE_MEAL_USE;
    }
}
