package net.zaharenko424.casualties_cubed.item.multi_tank;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.zaharenko424.casualties_cubed.config.ServerConfig;
import net.zaharenko424.casualties_cubed.fluid_system.MultiFluidTankHandler;
import net.zaharenko424.casualties_cubed.item.api.FluidTint;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import net.zaharenko424.casualties_cubed.registry.ModFluids;

public class BloodBagItem extends AutoInjectorItem implements FluidTint {

    @Override
    public int getCapacity() {
        return 750;
    }

    @Override
    public int getInjectAmount() {
        return 375;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        MultiFluidTankHandler handler = getHandler(stack);
        int canInsert = handler.getTank().getFreeSpace();
        if (canInsert == 0) return InteractionResultHolder.pass(stack);

        if (!pLevel.isClientSide) {
            PlayerHealthData data = PlayerHealthData.of(pPlayer).orElse(null);
            canInsert = data.bloodVolume() * 1000 < canInsert ? (int) Math.floor(data.bloodVolume() * 1000) : canInsert;
            data.bloodVolume(data.bloodVolume() - canInsert / 1000f);
            addFluid(stack, ServerConfig.EXPIE_MODE.get() ? ModFluids.YELLOW_BLOOD : ModFluids.RED_BLOOD, canInsert);
        }

        return InteractionResultHolder.success(stack);
    }

    @Override
    public int defTint() {
        return 2143865032;
    }

    @Override
    public ItemStack withDefFluid() {
        return withFluid(ServerConfig.EXPIE_MODE.get() ? ModFluids.YELLOW_BLOOD : ModFluids.RED_BLOOD);
    }
}
