package net.zaharenko424.casualties_cubed.item.special.bag;

import net.zaharenko424.casualties_cubed.item.api.IBag;
import net.zaharenko424.casualties_cubed.menu.MediumMedibagMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MediumMedibagItem extends Item implements IBag {

    public MediumMedibagItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        pPlayer.level().playSound(null, pPlayer.blockPosition(), SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 1, 1);
        if (!pLevel.isClientSide()) {
            NetworkHooks.openScreen((ServerPlayer) pPlayer,
                    new SimpleMenuProvider(
                            (id, inv, ply) -> new MediumMedibagMenu(id, inv, itemStack),
                            Component.translatable("item.casualties_cubed.medium_medibag")
                    ),
                    buf -> buf.writeBoolean(pUsedHand == InteractionHand.MAIN_HAND)
            );
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("item.casualties_cubed.medium_medibag.description").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    @Override
    public int size() {
        return 8;
    }
}
