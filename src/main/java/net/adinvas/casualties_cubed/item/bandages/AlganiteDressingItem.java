package net.adinvas.casualties_cubed.item.bandages;

import net.adinvas.casualties_cubed.PlayerHealthProvider;
import net.adinvas.casualties_cubed.item.api.IAllowInMedicBags;
import net.adinvas.casualties_cubed.item.api.IBandage;
import net.adinvas.casualties_cubed.limbs.Limb;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AlganiteDressingItem extends Item implements IBandage, IAllowInMedicBags {

    public AlganiteDressingItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("item.casualties_cubed.alganate_dressing.description").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void useBandageAction(float scalableAmount, Player target, @Nullable Limb limb) {
        target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h -> {
            h.addDelayedChange(((0.01f * scalableAmount) / 20f) / 60f, 300, limb);
            float painRed = Math.max(0f, 1f - 0.01f * scalableAmount);
            h.setLimbPain(limb, h.getLimbPain(limb) * painRed);
            h.setLimbSkinHealth(limb, h.getLimbSkinHealth(limb) + 1f * scalableAmount);
        });
    }

    @Override
    public Component getName(ItemStack pStack) {
        return appendDurability(pStack, Component.empty().append(super.getName(pStack)));
    }
}
