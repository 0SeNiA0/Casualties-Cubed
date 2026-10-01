package net.zaharenko424.casualties_cubed.item.special.bag;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.zaharenko424.casualties_cubed.item.api.ItemWithDescription;
import net.zaharenko424.casualties_cubed.item.api.IBag;
import net.zaharenko424.casualties_cubed.menu.LargeMedibagMenu;

public class LargeMedibagItem extends ItemWithDescription implements IBag {

    public LargeMedibagItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        pPlayer.level().playSound(null, pPlayer.blockPosition(), SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 1, 1);
        if (!pLevel.isClientSide()) {
            NetworkHooks.openScreen((ServerPlayer) pPlayer,
                    new SimpleMenuProvider(
                            (id, inv, ply) -> new LargeMedibagMenu(id, inv, itemStack),
                            Component.translatable("item.casualties_cubed.large_medibag")
                    ),
                    buf -> buf.writeBoolean(pUsedHand == InteractionHand.MAIN_HAND)
            );
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    @Override
    public int size() {
        return 12;
    }
}
