package net.zaharenko424.casualties_cubed.item.multi_tank;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.zaharenko424.casualties_cubed.fluid_system.MedicalFluidType;
import net.zaharenko424.casualties_cubed.fluid_system.MultiTankHelper;
import net.zaharenko424.casualties_cubed.registry.ModSounds;

import java.util.List;

public class PillContainerItem extends MultiTankFluidItem {

    public PillContainerItem() {
        super(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().alwaysEat().build()));
    }

    @Override
    public int getCapacity() {
        return 100;
    }

    public int getUseAmount() {
        return 10;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (!(pLivingEntity instanceof ServerPlayer player)) return pStack;

        int max = (int) Math.min(MultiTankHelper.getFilledTotal(pStack), getUseAmount());
        List<FluidStack> drained = MultiTankHelper.drain(pStack, max, player.isCreative());
        for (FluidStack fs : drained) {
            MedicalFluidType.ingest(player, fs.getAmount(), fs.getFluid());
        }

        return pStack;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.EAT;
    }

    @Override
    public SoundEvent getEatingSound() {
        return ModSounds.PILLS.get();
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 15;
    }
}
