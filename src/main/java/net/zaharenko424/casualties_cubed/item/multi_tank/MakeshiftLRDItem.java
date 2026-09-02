package net.zaharenko424.casualties_cubed.item.multi_tank;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.util.Util;
import net.zaharenko424.casualties_cubed.fluid_system.MultiTankHelper;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.LimbStatistics;
import net.zaharenko424.casualties_cubed.registry.ModFluids;

public class MakeshiftLRDItem extends LRDItem {

    public MakeshiftLRDItem() {
        super();
    }

    @Override
    public int getCapacity() {
        return 50;
    }

    @Override
    public void onMedicalUse(ServerPlayer source, ServerPlayer target, Limb limb, ItemStack stack) {
        if (MultiTankHelper.getAmountOfFluid(stack, new FluidStack(ModFluids.LRD_SERUM.get(), 1)) >= 25) {
            if (!source.isCreative()) MultiTankHelper.drainSpecificFluid(stack, 25, new FluidStack(ModFluids.LRD_SERUM.get(), 1));

            target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                LimbStatistics stats = data.getLimb(limb);

                stats.addMuscleHealth(50);
                data.addCaffeinated(60);
                stats.addInfection(-10 * stats.infectionSpeedMult);
                stats.setDisinfectionTimerAtLeast(400);
                data.adrenaline(data.adrenaline() + 60);
                data.venomTotal(Util.moveTowards(12, data.venomTotal(), 0));

                for (Limb limb1 : limb.getConnectedLimbs()) {
                    stats = data.getLimb(limb1);

                    stats.addMuscleHealth(40);
                    stats.addInfection(-5 * stats.infectionSpeedMult);
                    stats.setDisinfectionTimerAtLeast(300);
                }

                if (limb == Limb.THORAX) data.internalBleeding(data.internalBleeding() * 0.75f);
            });

            source.level().playSound(null, source.getOnPos(), getUseSound(), SoundSource.PLAYERS);
        }
    }
}
