package net.adinvas.casualties_cubed.item.usable;

import net.adinvas.casualties_cubed.PlayerHealthProvider;
import net.adinvas.casualties_cubed.item.api.IAllowInMedicBags;
import net.adinvas.casualties_cubed.item.api.INbtDrivenDurability;
import net.adinvas.casualties_cubed.item.api.ISimpleMedicalUsable;
import net.adinvas.casualties_cubed.limbs.Limb;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class BandAidItem extends Item implements ISimpleMedicalUsable, IAllowInMedicBags, INbtDrivenDurability {

    public BandAidItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public ItemStack onMedicalUse(Limb limb, ServerPlayer source, ServerPlayer target, ItemStack stack) {
        AtomicReference<ItemStack> newitem = new AtomicReference<>(stack);
        target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h -> {
            h.setLimbSkinHealth(limb, h.getLimbSkinHealth(limb) + 3);
            h.addDelayedChange(((0.1f) / 20f) / 60f, 100, limb);
            h.setLimbPain(limb, h.getLimbPain(limb) * 0.90f);
            ItemStack newitemstack = stack;
            subNbtDurability(stack, 20);
            if (getNbtDurability(stack) <= 0) {
                newitemstack = ItemStack.EMPTY;
            }
            newitem.set(newitemstack);
        });
        return newitem.get();
    }

    @Override
    public Component getName(ItemStack pStack) {
        return appendDurability(pStack, Component.empty().append(super.getName(pStack)));
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("item.casualties_cubed.band_aid.description").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public SoundEvent getUseSound() {
        return SoundEvents.BONE_MEAL_USE;
    }
}
