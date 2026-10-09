package net.zaharenko424.casualties_cubed.item.reusable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.item.api.ItemWithDescription;
import net.zaharenko424.casualties_cubed.item.api.IAllowInMedicBags;
import net.zaharenko424.casualties_cubed.item.api.INbtDrivenDurability;
import net.zaharenko424.casualties_cubed.item.api.ISimpleMedicalUsable;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.util.Util;

public class IcePackItem extends ItemWithDescription implements ISimpleMedicalUsable, IAllowInMedicBags, INbtDrivenDurability {

    private static final int RECHARGE_TIME = 111 * 20;//0.009/s -> 111s for full

    public IcePackItem() {
        super(new Item.Properties().stacksTo(1));
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
        Level level = source.level();
        float charge = rechargePercentage(level, stack);
        if (charge < 0.5) return;

        target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
            data.temperature(data.temperature() - 1);
            data.getLimb(limb).setChilled();
        });

        source.level().playSound(null, source.getOnPos(), getUseSound(), SoundSource.PLAYERS);
        stack.getOrCreateTag().putLong("LastUse", level.getGameTime() - Math.round(RECHARGE_TIME * (charge - 0.5f)));//+- extra tick but good enough
    }

    @Override
    public Component getName(ItemStack pStack) {
        return INbtDrivenDurability.appendDurability(rechargePercentage(Util.level(), pStack), Component.empty().append(super.getName(pStack)));
    }
}
