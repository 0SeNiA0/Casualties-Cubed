package net.zaharenko424.casualties_cubed.item.multi_tank;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.zaharenko424.casualties_cubed.fluid_system.MedicalEffects;
import net.zaharenko424.casualties_cubed.fluid_system.MultiTankHelper;
import net.zaharenko424.casualties_cubed.item.api.ISimpleMedicalUsable;
import net.zaharenko424.casualties_cubed.limbs.Limb;

import java.util.List;

public class AutoInjectorItem extends MultiTankFluidItem implements ISimpleMedicalUsable {

    @Override
    public int getCapacity() {
        return 100;
    }

    public int getInjectAmount() {
        return 34;
    }

    @Override
    public void onMedicalUse(ServerPlayer source, ServerPlayer target, Limb limb, ItemStack stack) {
        int max = (int) Math.min(MultiTankHelper.getFilledTotal(stack), getInjectAmount());
        List<FluidStack> drained = MultiTankHelper.drain(stack, max, source.isCreative());
        for (FluidStack fs : drained) {
            MedicalEffects.forFluid(fs.getFluid()).applyInjected(target, fs.getAmount(), limb);
        }
    }
}
