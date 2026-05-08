package net.adinvas.casualties_cubed.item.usable;

import net.adinvas.casualties_cubed.PlayerHealthProvider;
import net.adinvas.casualties_cubed.blocks.GlowFruitBushBlock;
import net.adinvas.casualties_cubed.item.api.ISimpleMedicalUsable;
import net.adinvas.casualties_cubed.limbs.Limb;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GlowFruitItem extends BlockItem implements ISimpleMedicalUsable {

    public GlowFruitItem(GlowFruitBushBlock block) {
        super(block, new Item.Properties()
                .stacksTo(64)
                .food(new FoodProperties.Builder()
                        .effect(() -> new MobEffectInstance(MobEffects.GLOWING, 200, 0), 1.0F)
                        .effect(() -> new MobEffectInstance(MobEffects.POISON, 100, 3), 1f)
                        .nutrition(1)
                        .saturationMod(1)
                        .build()
                )
        );
    }

    @Override
    public ItemStack onMedicalUse(Limb limb, ServerPlayer source, ServerPlayer target, ItemStack stack) {
        target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h -> {
            h.setLimbSkinHealth(limb, h.getLimbSkinHealth(limb) - 1);
            h.setLimbMuscleHealth(limb, h.getLimbMuscleHealth(limb) - 4);
            h.setLimbDisinfected(limb, Math.max(h.getLimbDisinfected(limb), 4400));
            List<Limb> conected = limb.getConnectedLimbs();
            for (Limb limb1 : conected) {
                h.setLimbMuscleHealth(limb1, h.getLimbMuscleHealth(limb1) - 3);
                h.setLimbDisinfected(limb1, Math.max(h.getLimbDisinfected(limb1), 2200));
            }
            stack.shrink(1);
        });
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("item.casualties_cubed.glow_fruit.description").withStyle(ChatFormatting.GRAY));
    }
}
