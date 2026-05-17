package net.zaharenko424.casualties_cubed.item.multi_tank;

import net.zaharenko424.casualties_cubed.Util;
import net.zaharenko424.casualties_cubed.fluid_system.MedicalFluid;
import net.zaharenko424.casualties_cubed.fluid_system.MultiTankHelper;
import net.zaharenko424.casualties_cubed.item.api.ISimpleMedicalUsable;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class SprayBottleItem extends MultiTankFluidItem implements ISimpleMedicalUsable {

    public int getOnSkinAmount() {
        return 10;
    }

    @Override
    public int getCapacity() {
        return 200;
    }

    @Override
    public void onMedicalUse(ServerPlayer source, ServerPlayer target, Limb limb, ItemStack stack) {
        int max = (int) Math.min(MultiTankHelper.getFilledTotal(stack), getOnSkinAmount());
        List<FluidStack> drained = MultiTankHelper.drain(stack, max, source.isCreative());
        MedicalFluid MF;
        for (FluidStack fs : drained) {
            MF = Util.getFallback(fs.getFluid());
            if (fs.hasTag()) {
                if (fs.getTag().contains("MedicalId")) {
                    MF = MedicalFluid.getFromId(fs.getTag().getString("MedicalId"));
                }
            }
            MF.getMedicalEffect().applyOnSkin(target, fs.getAmount(), limb);
        }
    }
}
