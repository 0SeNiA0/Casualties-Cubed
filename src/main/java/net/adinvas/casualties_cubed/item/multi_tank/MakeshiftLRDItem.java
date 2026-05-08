package net.adinvas.casualties_cubed.item.multi_tank;

import net.adinvas.casualties_cubed.registry.ModMedicalFluids;
import net.adinvas.casualties_cubed.PlayerHealthProvider;
import net.adinvas.casualties_cubed.fluid_system.MultiTankHelper;
import net.adinvas.casualties_cubed.item.api.IAllowInMedicBags;
import net.adinvas.casualties_cubed.item.api.ISimpleMedicalUsable;
import net.adinvas.casualties_cubed.limbs.Limb;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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
    public ItemStack onMedicalUse(Limb limb, ServerPlayer source, ServerPlayer target, ItemStack stack) {
        if (MultiTankHelper.getAmountOfFluid(stack, ModMedicalFluids.LRD_SERUM.get().getAsStack(1)) >= 25) {
            MultiTankHelper.drainSpecificFluid(stack, 25, ModMedicalFluids.LRD_SERUM.get().getAsStack(1));
            target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h -> {
                h.setLimbMuscleHealth(limb, h.getLimbMuscleHealth(limb) + 50f);
                h.setLimbInfection(limb, h.getLimbInfection(limb) - 10);
                h.setLimbDisinfected(limb, Math.max(h.getLimbDisinfected(limb), 12000));
                h.setLimbBleedRate(limb, h.getLimbBleedRate(limb) * 0.7f);
                h.setInternalBleeding(h.getInternalBleeding() * 0.45f);
                h.setPendingOpioids(h.getPendingOpioids() + 20);

                for (Limb limb1 : limb.getConnectedLimbs()) {
                    h.setLimbMuscleHealth(limb1, h.getLimbMuscleHealth(limb1) + 40f);
                    h.setLimbInfection(limb1, h.getLimbInfection(limb1) - 5);
                    h.setLimbDisinfected(limb1, Math.max(h.getLimbDisinfected(limb1), 6000));
                    h.setLimbBleedRate(limb1, h.getLimbBleedRate(limb1) * 0.75f);
                }
            });
        }
        return stack;
    }

    @Override
    public SoundEvent getUseSound() {
        return SoundEvents.BONE_MEAL_USE;
    }
}
