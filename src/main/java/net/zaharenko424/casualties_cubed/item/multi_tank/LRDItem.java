package net.zaharenko424.casualties_cubed.item.multi_tank;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.fluid_system.MultiTankHelper;
import net.zaharenko424.casualties_cubed.item.api.IAllowInMedicBags;
import net.zaharenko424.casualties_cubed.item.api.ISimpleMedicalUsable;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.LimbStatistics;
import net.zaharenko424.casualties_cubed.registry.ModFluids;

public class LRDItem extends MultiTankFluidItem implements ISimpleMedicalUsable, IAllowInMedicBags {

    public LRDItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public int getCapacity() {
        return 75;
    }

    @Override
    public void onMedicalUse(ServerPlayer source, ServerPlayer target, Limb limb, ItemStack stack) {
        if (MultiTankHelper.getAmountOfFluid(stack, new FluidStack(ModFluids.LRD_SERUM.get(), 1)) >= 25) {
            if (!source.isCreative()) MultiTankHelper.drainSpecificFluid(stack, 25, new FluidStack(ModFluids.LRD_SERUM.get(), 1));

            target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                LimbStatistics stats = data.getLimb(limb);

                stats.addMuscleHealth(50);
                stats.addInfection(-10);
                stats.setDisinfectionTimerAtLeast(12000);
                stats.setBleedRate(stats.getBleedRate() * 0.7f);

                data.setInternalBleeding(data.getInternalBleeding() * 0.45f);
                data.setPendingOpioids(data.getPendingOpioids() + 20);

                for (Limb limb1 : limb.getConnectedLimbs()) {
                    stats = data.getLimb(limb1);

                    stats.addMuscleHealth(40);
                    stats.addInfection(-5);
                    stats.setDisinfectionTimerAtLeast(6000);
                    stats.setBleedRate(stats.getBleedRate() * 0.75f);
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
