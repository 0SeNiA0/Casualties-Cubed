package net.zaharenko424.casualties_cubed.item.api;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AbstractBandage extends Item implements INbtDrivenDurability, IAllowInMedicBags {

    public static final float ANGLE_PER_PACKET = 0.055555556f;

    protected final int color;

    public AbstractBandage(Properties pProperties, int color) {
        super(pProperties);
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    ///One full wrap will total to ~1 angle
    public float durabilityScale(float angle) {
        return angle * 100; //C:U uses 1 as full condition.. mb also use 1 instead of 100
    }

    public void use(Player source, Player target, Limb limb, ItemStack bandage) {
        float amount = durabilityScale(ANGLE_PER_PACKET);
        amount = Math.min(getNbtDurability(bandage), amount);//Make sure not to use more than there is left
        useBandageAction(amount, target, limb);
        if (!source.isCreative()) subNbtDurability(bandage, amount);
    }

    protected abstract void useBandageAction(float amount, Player target, @Nullable Limb limb);

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable(getDescriptionId() +".description").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public Component getName(ItemStack pStack) {
        return appendDurability(pStack, Component.empty().append(super.getName(pStack)));
    }
}
