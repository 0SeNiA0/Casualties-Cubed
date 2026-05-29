package net.zaharenko424.casualties_cubed.item.misc;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.zaharenko424.casualties_cubed.blocks.BrownCapBlock;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BrownCapItem extends BlockItem {

    public BrownCapItem(BrownCapBlock block) {
        super(block, new Properties()
                .food(new FoodProperties.Builder()
                        .nutrition(1)
                        .saturationMod(1)
                        .alwaysEat()
                        .build()));
    }

    @Override
    public SoundEvent getEatingSound() {
        return SoundEvents.GENERIC_EAT;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (pLivingEntity instanceof ServerPlayer player)
            BrownCapMushItem.randomEffect(player, player.getRandom(), player.getFoodData(), PlayerHealthData.nonNullOf(player));

        return super.finishUsingItem(pStack, pLevel, pLivingEntity);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 16;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("item.casualties_cubed.brown_cap.description").withStyle(ChatFormatting.GRAY));
    }
}
