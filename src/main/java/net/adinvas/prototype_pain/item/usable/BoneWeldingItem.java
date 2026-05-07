package net.adinvas.prototype_pain.item.usable;

import net.adinvas.prototype_pain.PlayerHealthProvider;
import net.adinvas.prototype_pain.item.api.IAllowInMedicBags;
import net.adinvas.prototype_pain.item.api.INbtDrivenDurability;
import net.adinvas.prototype_pain.item.api.ISimpleMedicalUsable;
import net.adinvas.prototype_pain.limbs.Limb;
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

public class BoneWeldingItem extends Item implements ISimpleMedicalUsable, IAllowInMedicBags, INbtDrivenDurability {

    public BoneWeldingItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public ItemStack onMedicalUse(Limb limb, ServerPlayer source, ServerPlayer target, ItemStack stack) {
        target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h -> {
            h.setLimbSkinHealth(limb, h.getLimbSkinHealth(limb) - 25);
            h.setLimbMuscleHealth(limb, h.getLimbMuscleHealth(limb) - 26);
            h.setLimbFracture(limb, h.getLimbFracture(limb) * 0.15f);
            h.setLimbBleedRate(limb, h.getLimbBleedRate(limb) + ((0.09f) / 20f / 60f));
            h.setLimbPain(limb, h.getLimbPain(limb) + 30);
            h.setBloodViscosity(h.getBloodViscosity() + 2);
        });
        ItemStack newitemstack = stack;
        subNbtDurability(stack, 50);
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
        pTooltipComponents.add(Component.translatable("item.prototype_pain.bone_welding.description").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public SoundEvent getUseSound() {
        return SoundEvents.BONE_MEAL_USE;
    }
}
