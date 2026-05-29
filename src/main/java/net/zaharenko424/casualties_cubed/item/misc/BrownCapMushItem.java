package net.zaharenko424.casualties_cubed.item.misc;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BrownCapMushItem extends Item {

    public BrownCapMushItem() {
        super(new Properties()
                .food(new FoodProperties.Builder()
                        .nutrition(8)
                        .saturationMod(8)
                        .alwaysEat()
                        .build()));
    }

    @Override
    public SoundEvent getEatingSound() {
        return SoundEvents.GENERIC_EAT;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (!(pLivingEntity instanceof ServerPlayer player)) return super.finishUsingItem(pStack, pLevel, pLivingEntity);

        PlayerHealthData data = PlayerHealthData.nonNullOf(player);
        FoodData food = player.getFoodData();
        RandomSource random = player.getRandom();
        for (int i = 0; i < 9; i++) {
            randomEffect(player, random, food, data);
        }

        return super.finishUsingItem(pStack, pLevel, pLivingEntity);
    }

    static void randomEffect(ServerPlayer player, RandomSource random, FoodData food, PlayerHealthData data) {
        float maineffect = random.nextFloat();
        if (maineffect <= .2) {
            food.setFoodLevel(player.getFoodData().getFoodLevel() + 3);
            food.setSaturation(player.getFoodData().getSaturationLevel() + 3);
        } else if (maineffect <= .35) {
            food.setFoodLevel(player.getFoodData().getFoodLevel() + 5);
            food.setSaturation(player.getFoodData().getSaturationLevel() + 5);
        } else if (maineffect <= .5) {
            food.setFoodLevel(player.getFoodData().getFoodLevel() + 3);
            food.setSaturation(player.getFoodData().getSaturationLevel() + 3);

            for (Limb limb : Limb.values()) {
                data.getLimb(limb).addInfection(-20);
            }
        } else if (maineffect <= .6) {
            food.setFoodLevel(player.getFoodData().getFoodLevel() - 3);
            food.setSaturation(player.getFoodData().getSaturationLevel() - 3);
        } else if (maineffect <= .75) {
            food.setFoodLevel(player.getFoodData().getFoodLevel() + 1);
            food.setSaturation(player.getFoodData().getSaturationLevel() + 1);
            data.setPendingOpioids(data.getPendingOpioids() + 35);
        } else if (maineffect <= .975) {
            food.setFoodLevel(player.getFoodData().getFoodLevel() + 5);
            food.setSaturation(player.getFoodData().getSaturationLevel() + 5);
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0));
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 3000, 2));
        } else if (maineffect <= .995) {
            food.setFoodLevel(player.getFoodData().getFoodLevel() + 1);
            food.setSaturation(player.getFoodData().getSaturationLevel() + 1);
            data.getLimb(Limb.CHEST).setMuscleHealth(0);
        } else {
            ExperimentalTreatmentItem.regrowRandom(random, data);
        }

        maineffect = random.nextFloat();

        if (maineffect <= .1) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h -> {
                h.setConsciousness(0);
            });
        } else if (maineffect <= .15) {
            player.addEffect(new MobEffectInstance(MobEffects.WITHER, 200, 4));
        } else if (maineffect <= .25) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 4));
        } else if (maineffect <= .31) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h -> {
                h.setTemperature(h.getTemperature() - 4.5f);
            });
        } else if (maineffect <= .39) {
            player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() + 10);
            player.getFoodData().setSaturation(player.getFoodData().getSaturationLevel() + 10);
        } else if (maineffect <= .49) {
            player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() - 20);
            player.getFoodData().setSaturation(player.getFoodData().getSaturationLevel() - 20);
        } else if (maineffect <= .57) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h -> {
                h.setTemperature(h.getTemperature() + 4.5f);
            });
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 64;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("item.casualties_cubed.brown_cap_mush.description").withStyle(ChatFormatting.GRAY));
    }
}
