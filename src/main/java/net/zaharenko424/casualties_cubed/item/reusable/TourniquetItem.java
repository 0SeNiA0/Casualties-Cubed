package net.zaharenko424.casualties_cubed.item.reusable;

import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.item.api.IAllowInMedicBags;
import net.zaharenko424.casualties_cubed.item.api.ISimpleMedicalUsable;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TourniquetItem extends Item implements ISimpleMedicalUsable, IAllowInMedicBags {

    public TourniquetItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public void onMedicalUse(ServerPlayer source, ServerPlayer target, Limb limb, ItemStack stack) {
        if (limb == Limb.CHEST) return;
        target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
            if (!data.getTourniquet(limb)) {
                data.setTourniquet(limb, true);

                if (!source.isCreative()) stack.shrink(1);
                source.level().playSound(null, source.getOnPos(), getUseSound(), SoundSource.PLAYERS);
            }
        });
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("item.casualties_cubed.tourniquet.description").withStyle(ChatFormatting.GRAY));
    }
}
