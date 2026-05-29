package net.zaharenko424.casualties_cubed.item.reusable;

import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.client.MinigameOpener;
import net.zaharenko424.casualties_cubed.item.api.IAllowInMedicBags;
import net.zaharenko424.casualties_cubed.item.api.IMedicalMinigameUsable;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TweezersItem extends Item implements IMedicalMinigameUsable, IAllowInMedicBags {

    public TweezersItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("item.casualties_cubed.tweezers.description").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void openMinigameScreen(Player target, ItemStack stack, @Nullable Limb limb, InteractionHand hand) {
        target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
            if (data.getLimb(limb).getShrapnel() == 0) return;

            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                MinigameOpener.OpenShrapnelMinigame(target, limb);
            });
        });
    }

    @Override
    public void openMinigameBagScreen(Player target, ItemStack stack, ItemStack bagStack, int lost, @Nullable Limb limb, InteractionHand hand) {
        this.openMinigameScreen(target, stack, limb, hand);
    }
}
