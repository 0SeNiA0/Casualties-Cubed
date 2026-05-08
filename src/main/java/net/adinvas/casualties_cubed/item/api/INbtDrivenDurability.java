package net.adinvas.casualties_cubed.item.api;

import net.adinvas.casualties_cubed.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;

public interface INbtDrivenDurability {

    default float getMaxNbtDurability(ItemStack stack) {
        return 100f;
    }

    default float getNbtDurability(ItemStack stack) {//Assume no tag == full durability
        if (!stack.hasTag()) return getMaxNbtDurability(stack);

        CompoundTag tag = stack.getTag();
        assert tag != null;
        if (!tag.contains("Durability", Tag.TAG_FLOAT)) return getMaxNbtDurability(stack);

        return tag.getFloat("Durability");
    }

    default void subNbtDurability(ItemStack stack, float value) {
        CompoundTag tag = stack.getOrCreateTag();

        float prev = tag.contains("Durability", Tag.TAG_FLOAT) ? tag.getFloat("Durability") : getMaxNbtDurability(stack);
        float new_ = prev - value;

        if (new_ <= 0) {
            stack.shrink(1);
            tag.remove("Durability");
            return;
        }

        tag.putFloat("Durability", new_);
    }

    default void setNbtDurability(ItemStack stack, float value) {
        CompoundTag tag = stack.getOrCreateTag();

        if (value >= getMaxNbtDurability(stack)) {
            tag.remove("Durability");
            return;
        }

        tag.putFloat("Durability", value);
    }

    default Component appendDurability(ItemStack stack, MutableComponent component) {
        float delta = getNbtDurability(stack) / getMaxNbtDurability(stack);
        return component
                .append(Component.literal(" (").withStyle(ChatFormatting.GRAY))
                .append(Component.literal((int) (delta * 100) + "%").withStyle(Style.EMPTY.withColor(Util.getRedToGreenColor(delta))))
                .append(Component.literal(")").withStyle(ChatFormatting.GRAY));
    }
}
