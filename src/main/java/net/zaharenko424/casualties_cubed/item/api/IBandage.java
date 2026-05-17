package net.zaharenko424.casualties_cubed.item.api;

import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface IBandage extends INbtDrivenDurability {

    default void use(Player source, Player target, Limb limb, float amount, ItemStack bandage) {
        amount = Math.min(getNbtDurability(bandage), amount);//Make sure not to use more than there is left
        useBandageAction(amount, target, limb);
        if (!source.isCreative()) subNbtDurability(bandage, amount);
    }

    void useBandageAction(float scalableAmount, Player target, @Nullable Limb limb);
}
