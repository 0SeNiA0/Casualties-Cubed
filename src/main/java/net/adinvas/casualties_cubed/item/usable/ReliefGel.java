package net.adinvas.casualties_cubed.item.usable;

import net.adinvas.casualties_cubed.PlayerHealthProvider;
import net.adinvas.casualties_cubed.item.api.IAllowInMedicBags;
import net.adinvas.casualties_cubed.item.api.INbtDrivenDurability;
import net.adinvas.casualties_cubed.item.api.ISimpleMedicalUsable;
import net.adinvas.casualties_cubed.limbs.Limb;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ReliefGel extends Item implements ISimpleMedicalUsable, IAllowInMedicBags, INbtDrivenDurability {

    public ReliefGel() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public ItemStack onMedicalUse(Limb limb, ServerPlayer source, ServerPlayer target, ItemStack stack) {
        target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
            data.setPendingOpioids(data.getPendingOpioids() + 1);
            data.setLimbPain(limb, data.getLimbPain(limb) - 5);
            data.setLimbDesinfected(limb, 300);
            data.setLimbMuscleHeal(limb, true);
            data.setLimbMuscleHealth(limb, data.getLimbMuscleHealth(limb) + 10);
        });
        ItemStack newitemstack = stack;
        subNbtDurability(stack, 20);
        if (getNbtDurability(stack) <= 0) {
            newitemstack = ItemStack.EMPTY;
        }
        return newitemstack;
    }

    @Override
    public Component getName(ItemStack pStack) {
        return appendDurability(pStack, Component.empty().append(super.getName(pStack)));
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("item.casualties_cubed.aid_gel.description").withStyle(ChatFormatting.GRAY));
    }
}
