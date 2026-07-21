package net.zaharenko424.casualties_cubed.item.bandages;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.item.api.IAllowInMedicBags;
import net.zaharenko424.casualties_cubed.item.api.IBandage;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.LimbStatistics;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class OldRagItem extends Item implements IBandage, IAllowInMedicBags {

    public OldRagItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public void useBandageAction(float scalableAmount, Player target, @Nullable Limb limb) {
        target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
            data.addDelayedChange(((0.003f * scalableAmount) / 20f) / 60f, 200, limb);
            LimbStatistics stats = data.getLimb(limb);

            float painRed = Math.max(0f, 1f - 0.02f * scalableAmount);
            stats.setPain(stats.getPain() * painRed);
            stats.addSkinHealth(0.12f * scalableAmount);

            float fractRed = Math.max(0f, 1f - 0.0002f * scalableAmount);
            stats.setBoneHealTimer(stats.getBoneHealTimer() * fractRed);
            stats.setDislocationTimer(stats.getDislocationTimer() * fractRed);
        });
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pPlayer.isInWaterOrBubble()) return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));

        if (pLevel.isClientSide) return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));

        PlayerHealthData.of(pPlayer).ifPresent(data -> data.setWetness(data.getWetness() * 0.5f));

        return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
    }

    @Override
    public Component getName(ItemStack pStack) {
        return appendDurability(pStack, Component.empty().append(super.getName(pStack)));
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("item.casualties_cubed.old_rag.description").withStyle(ChatFormatting.GRAY));
    }
}
