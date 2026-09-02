package net.zaharenko424.casualties_cubed.item.usable;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.item.api.IAllowInMedicBags;
import net.zaharenko424.casualties_cubed.item.api.INbtDrivenDurability;
import net.zaharenko424.casualties_cubed.item.api.ISimpleMedicalUsable;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IcePackItem extends Item implements ISimpleMedicalUsable, IAllowInMedicBags, INbtDrivenDurability {

    public IcePackItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public void onMedicalUse(ServerPlayer source, ServerPlayer target, Limb limb, ItemStack stack) {
        target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
            data.temperature(data.temperature() - 1);
            data.getLimb(limb).setChilled();
        });

        if (!source.isCreative()) subNbtDurability(stack, 50);
        source.level().playSound(null, source.getOnPos(), getUseSound(), SoundSource.PLAYERS);
    }

    @Override
    public Component getName(ItemStack pStack) {
        return appendDurability(pStack, Component.empty().append(super.getName(pStack)));
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("item.casualties_cubed.ice_pack.description").withStyle(ChatFormatting.GRAY));
    }
}
