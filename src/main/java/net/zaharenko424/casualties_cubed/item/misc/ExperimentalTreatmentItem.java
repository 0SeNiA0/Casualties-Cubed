package net.zaharenko424.casualties_cubed.item.misc;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.zaharenko424.casualties_cubed.item.api.IAllowInMedicBags;
import net.zaharenko424.casualties_cubed.item.api.ISimpleMedicalUsable;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.LimbStatistics;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ExperimentalTreatmentItem extends Item implements IAllowInMedicBags, ISimpleMedicalUsable {

    public ExperimentalTreatmentItem() {
        super(new Properties().stacksTo(1).food(new FoodProperties.Builder().alwaysEat().build()));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (!(pLivingEntity instanceof ServerPlayer player))
            return super.finishUsingItem(pStack, pLevel, pLivingEntity);

        PlayerHealthData data = PlayerHealthData.nonNullOf(player);
        RandomSource random = player.getRandom();
        if (!hasAmputated(data)) {
            if (random.nextFloat() <= .5) {
                randomHarmful(player, data);
            } else {
                randomBeneficial(player, data);
            }

            return super.finishUsingItem(pStack, pLevel, pLivingEntity);
        }

        float roll = random.nextFloat();
        if (roll <= .5) {
            regrowRandom(random, data);
        } else if (roll <= .75) {
            randomHarmful(player, data);
        } else {
            randomBeneficial(player, data);
        }

        return super.finishUsingItem(pStack, pLevel, pLivingEntity);
    }

    static void regrowRandom(RandomSource random, PlayerHealthData data) {
        List<Limb> amputated = new ArrayList<>();
        for (Limb limb : Limb.values()) {
            if (data.isAmputated(limb))
                amputated.add(limb);
        }

        if (!amputated.isEmpty()) {
            Limb toFix = amputated.get(random.nextInt(amputated.size()));
            LimbStatistics stats;
            for (Limb limb : toFix.getConnectedLimbs()) {
                stats = data.getLimb(limb);
                if (stats.isAmputated()) {
                    stats.setAmputated(false);
                    stats.setPain(400);
                }
            }
            stats = data.getLimb(toFix);
            stats.setAmputated(false);
            stats.setPain(400);
        } else {
            if (data.isRightEyeBlind()) {
                data.setRightEyeBlind(false);
            } else if (data.isLeftEyeBlind()) {
                data.setLeftEyeBlind(false);
            } else if (data.isMouthRemoved()) {
                data.setMouthRemoved(false);
            }
        }
    }

    private static final List<Limb> AMPUTATABLE = Util.make(() -> {
        List<Limb> tmp = new ArrayList<>();
        for (Limb limb : Limb.values()) {
            if (limb != Limb.CHEST) tmp.add(limb);
        }
        return List.copyOf(tmp);
    });

    private void randomHarmful(ServerPlayer player, PlayerHealthData data) {
        RandomSource random = player.getRandom();
        float roll = random.nextFloat();
        if (roll <= .02) {
            data.getLimb(Limb.HEAD).setMuscleHealth(0);
            data.setBrainHealth(29);
        } else if (roll <= .1) {
            player.sendSystemMessage(Component.translatable("item.casualties_cubed.experimental_treatment.doom"), true);
            LimbStatistics stats;
            for (Limb limb : Limb.values()) {
                stats = data.getLimb(limb);
                if (stats.getInfection() <= 0) stats.setInfection(1);
            }
        } else if (roll <= .15) {
            for (Limb limb : Limb.values()) {
                data.applySkinDamage(limb, random.nextFloat() * 5);
                data.applyBleedDamage(limb, random.nextFloat() * 5, player);
            }
        } else if (roll <= .25) {
            player.getFoodData().setFoodLevel(0);
            player.getFoodData().setSaturation(0);
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 400, 20));
        } else if (roll <= .35) {
            for (Limb limb : Limb.values()) {
                if (limb == Limb.CHEST) continue;
                data.applyMuscleDamage(limb, random.nextFloat() * 20, player);
            }
        } else if (roll <= .39) {
            data.painkillers.addOpiates(100);
        } else if (roll <= .54) {
            data.setTemperature(43);
        } else if (roll <= .59) {
            data.setTemperature(25);
        } else if (roll <= .64) {
            data.setHearingLoss(1);
            data.setFlashHearingLoss(1);
        } else if (roll <= .79) {
            LimbStatistics stats = data.getLimb(Limb.weigtedRandomLimb());
            stats.setPain(150);
            stats.setSkinHealth(0);
            stats.setMuscleHealth(0);
        } else if (roll <= .84) {
            data.setShock(1);
        } else if (roll <= .94) {
            data.setInternalBleeding(0.2f / 20f / 60f);
        } else if (roll <= .99) {
            data.setBloodVolume(3f);
        } else {
            Limb limb = AMPUTATABLE.get(random.nextInt(AMPUTATABLE.size()));
            if (limb == Limb.HEAD) {
                if (!data.isRightEyeBlind()) {
                    data.setRightEyeBlind(true);
                } else if (!data.isLeftEyeBlind()) {
                    data.setLeftEyeBlind(true);
                } else {
                    data.setMouthRemoved(true);
                }
            } else {
                data.dismember(limb);
            }
        }
    }

    private void randomBeneficial(ServerPlayer player, PlayerHealthData data) {
        float roll = player.getRandom().nextFloat() * 100;
        if (roll < 5) {
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 5));
        } else if (roll < 15) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 5));
        } else if (roll < 25) {
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 200, 5));
        } else if (roll < 35) {
            data.setBloodVolume(5);
            data.setBloodViscosity(0);
            for (Limb limb : Limb.values()) {
                data.getLimb(limb).setBleedRate(0);
            }
            data.setInternalBleeding(0);
        } else if (roll < 45) {
            data.painkillers.reset();
        } else if (roll < 55) {
            LimbStatistics stats;
            for (Limb limb : Limb.values()) {
                stats = data.getLimb(limb);
                stats.setSkinHealth(100);
                stats.setBoneHealTimer(0);
                stats.setDislocationTimer(0);
            }
        } else if (roll < 67) {
            for (Limb limb : Limb.values()) {
                data.getLimb(limb).setMuscleHealth(100);
            }
        } else if (roll < 77) {
            data.setTemperature(36.6f);
        } else if (roll < 87) {
            data.setTriedRollingLastStand(false);
        } else if (roll < 90) {
            for (Limb limb : Limb.values()) {
                data.getLimb(limb).setDisinfectionTimerAtLeast(10 * 60 * 20);
            }
            data.setAntibioticTimer(10 * 60 * 20);
        } else if (roll < 95) {
            data.setHearingLoss(0);
        } else {
            for (Limb limb : Limb.values()) {
                data.getLimb(limb).setInfection(0);
            }
        }
    }

    public boolean hasAmputated(PlayerHealthData data) {
        if (data.isMouthRemoved()) return true;
        if (data.isLeftEyeBlind()) return true;
        if (data.isRightEyeBlind()) return true;

        for (Limb limb : Limb.values()) {
            if (data.isAmputated(limb)) return true;
        }

        return false;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 100;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("item.casualties_cubed.experimental_treatment.description1").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(Component.translatable("item.casualties_cubed.experimental_treatment.description2").withStyle(ChatFormatting.AQUA));
        pTooltipComponents.add(Component.translatable("item.casualties_cubed.experimental_treatment.description3").withStyle(ChatFormatting.DARK_GRAY));
        pTooltipComponents.add(Component.translatable("item.casualties_cubed.experimental_treatment.description4").withStyle(ChatFormatting.GRAY));
        if (Screen.hasShiftDown() && pIsAdvanced.isAdvanced()) {
            pTooltipComponents.add(Component.translatable("item.casualties_cubed.experimental_treatment.extra_note").withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public void onMedicalUse(ServerPlayer source, ServerPlayer target, Limb limb, ItemStack stack) {
        if (limb == Limb.HEAD) {
            finishUsingItem(stack, target.level(), target);
        }
    }
}
