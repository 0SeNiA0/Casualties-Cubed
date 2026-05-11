package net.zaharenko424.casualties_cubed.item.api;

import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface IBandage extends INbtDrivenDurability {

    default void use(Player target, Limb limb, ItemStack bandage, float amount) {
        amount = Math.min(getNbtDurability(bandage), amount);//Make sure not to use more than there is left
        useBandageAction(amount, target, limb);
        subNbtDurability(bandage, amount);
    }

    void useBandageAction(float scalableAmount, Player target, @Nullable Limb limb);
}
