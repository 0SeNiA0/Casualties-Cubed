package net.zaharenko424.casualties_cubed.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.zaharenko424.casualties_cubed.item.api.INbtDrivenDurability;
import net.zaharenko424.casualties_cubed.item.api.ItemWithDescription;

public class ScrapMetal extends ItemWithDescription implements INbtDrivenDurability {

    public ScrapMetal(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack pStack, ItemStack pOther, Slot pSlot, ClickAction pAction, Player pPlayer, SlotAccess pAccess) {
        float durability = getNbtDurability(pStack);
        float maxDurability = getMaxNbtDurability(pStack);
        if (durability >= maxDurability || !pOther.is(this)) return false;

        float toTransfer = Math.min(maxDurability - durability, getNbtDurability(pOther));

        setNbtDurability(pStack, durability + toTransfer);
        subNbtDurability(pOther, toTransfer);
        return true;
    }

    @Override
    public Component getName(ItemStack pStack) {
        return appendDurability(pStack, Component.empty().append(super.getName(pStack)));
    }
}
