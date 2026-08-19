package net.zaharenko424.casualties_cubed.item.usable;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.zaharenko424.casualties_cubed.registry.ModSounds;
import net.zaharenko424.casualties_cubed.util.Util;
import net.zaharenko424.casualties_cubed.item.api.IAllowInMedicBags;
import net.zaharenko424.casualties_cubed.item.api.INbtDrivenDurability;
import net.zaharenko424.casualties_cubed.item.api.ISimpleMedicalUsable;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ChestDrainItem extends Item implements ISimpleMedicalUsable, IAllowInMedicBags {

    private static final int RECHARGE_TIME = 250 * 20;//24/m 0.4/s -> 250s for full

    public ChestDrainItem() {
        super(new Properties().stacksTo(1));
    }

    public boolean isReady(Level level, ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains("LastUse", CompoundTag.TAG_LONG)) return true;

        long lastUse = tag.getLong("LastUse"), current = level.getGameTime();
        if (lastUse > current) return true;

        return lastUse + RECHARGE_TIME < current;
    }

    public float rechargePercentage(Level level, ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains("LastUse", CompoundTag.TAG_LONG)) return 1;

        long lastUse = tag.getLong("LastUse"), current = level.getGameTime();
        if (lastUse > current || current > lastUse + RECHARGE_TIME) return 1;

        return Mth.clamp((current - lastUse), 0, RECHARGE_TIME) / RECHARGE_TIME;
    }

    @Override
    public void onMedicalUse(ServerPlayer source, ServerPlayer target, Limb limb, ItemStack stack) {
        if (limb != Limb.THORAX || !isReady(source.level(), stack)) return;

        PlayerHealthData.of(target).ifPresent(data -> {
            data.getLimb(Limb.THORAX).addBleedRate(0.036f);
            data.setHemothorax(data.getHemothorax() - 35);
        });

        source.level().playSound(null, source.getOnPos(), getUseSound(), SoundSource.PLAYERS);
        stack.getOrCreateTag().putLong("LastUse", source.level().getGameTime());
    }

    @Override
    public Component getName(ItemStack pStack) {
        return INbtDrivenDurability.appendDurability(rechargePercentage(Util.level(), pStack), Component.empty().append(super.getName(pStack)));
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("item.casualties_cubed.chest_drain.description").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public SoundEvent getUseSound() {
        return ModSounds.DRAIN_USE.get();
    }
}
