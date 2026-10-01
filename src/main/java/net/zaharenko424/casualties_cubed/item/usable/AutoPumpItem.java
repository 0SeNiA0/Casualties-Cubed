package net.zaharenko424.casualties_cubed.item.usable;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.zaharenko424.casualties_cubed.item.api.IAllowInMedicBags;
import net.zaharenko424.casualties_cubed.item.api.INbtDrivenDurability;
import net.zaharenko424.casualties_cubed.item.api.ItemWithDescription;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import net.zaharenko424.casualties_cubed.registry.ModSounds;
import net.zaharenko424.casualties_cubed.util.Util;

public class AutoPumpItem extends ItemWithDescription implements IAllowInMedicBags, INbtDrivenDurability, Equipable {

    public AutoPumpItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public boolean destroyOnZeroDurability() {
        return false;
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if (level.isClientSide || getNbtDurability(stack) <= 0) return;

        float damage = Util.TICK_TO_SEC / 1200;
        PlayerHealthData data = PlayerHealthData.of(player).orElse(null);
        if (data != null && data.bloodPressure() < 85) {
            damage += 0.002f;
            data.addBloodPressure(44);
            player.level().playSound(null, player.getOnPos(), ModSounds.AUTO_PUMP.get(), SoundSource.PLAYERS);
        }

        subNbtDurability(stack, damage);
    }

    @Override
    public Component getName(ItemStack pStack) {
        return appendDurability(pStack, Component.empty().append(super.getName(pStack)));
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.CHEST;
    }
}
