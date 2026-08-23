package net.zaharenko424.casualties_cubed.item.multi_tank;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.registries.RegistryObject;
import net.zaharenko424.casualties_cubed.util.ColorUtil;
import net.zaharenko424.casualties_cubed.fluid_system.MultiFluidTankHandler;
import net.zaharenko424.casualties_cubed.fluid_system.MultiTankHelper;
import net.zaharenko424.casualties_cubed.fluid_system.MedicalFluidType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MultiTankFluidItem extends Item {

    private static final int capacity = 1000;

    public MultiTankFluidItem() {
        super(new Properties().stacksTo(1));
    }

    public MultiTankFluidItem(Properties properties) {
        super(properties);
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ICapabilityProvider() {

            private final LazyOptional<IFluidHandlerItem> fluidCap =
                    LazyOptional.of(() -> new MultiFluidTankHandler(stack, getCapacity()));

            @Override
            @SuppressWarnings("unchecked")
            public <T> LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> cap, net.minecraft.core.Direction side) {
                if (cap == ForgeCapabilities.FLUID_HANDLER_ITEM) {
                    return (LazyOptional<T>) fluidCap;
                }
                return LazyOptional.empty();
            }
        };
    }

    public MultiFluidTankHandler getHandler(ItemStack stack) {
        if (stack.isEmpty() || stack.getItem() != this) return null;
        LazyOptional<IFluidHandlerItem> cap = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
        if (!cap.isPresent()) return null;

        IFluidHandlerItem handler = cap.orElse(null);
        if (handler instanceof MultiFluidTankHandler) {
            return (MultiFluidTankHandler) handler;
        }
        return null;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        appendDescription(stack, level, tooltip, flag);
        appendFluidText(stack, tooltip);
    }

    @Override
    public Component getName(ItemStack pStack) {
        float scale = MultiTankHelper.getFilledTotal(pStack) / MultiTankHelper.getCapacity(pStack);
        Component finalcomp = super.getName(pStack);
        finalcomp = Component.empty().append(finalcomp)
                .append(Component.literal(" (").withStyle(ChatFormatting.GRAY))
                .append(Component.translatable("casualties_cubed.tooltip.percent", (int) (scale * 100)).withStyle(Style.EMPTY.withColor(ColorUtil.getRedToGreenColor(scale))))
                .append(Component.literal(")").withStyle(ChatFormatting.GRAY));
        return finalcomp;
    }

    public void appendDescription(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        String key = getDescriptionId(pStack) + ".description";
        if (I18n.exists(key)) {
            pTooltipComponents.add(Component.translatable(key).withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    public ItemStack withDefFluid() {
        return new ItemStack(this);
    }

    public void addFluid(ItemStack stack, RegistryObject<? extends Fluid> fluid, int ml) {
        MultiTankHelper.addFluid(stack, ml, new FluidStack(fluid.get(), ml));
    }

    public ItemStack withFluid(RegistryObject<? extends Fluid> fluid) {
        return withFluid(fluid, getCapacity());
    }

    public ItemStack withFluid(RegistryObject<? extends Fluid> fluid, int ml) {
        ItemStack stack = new ItemStack(this);
        MultiTankHelper.addFluid(stack, ml, new FluidStack(fluid.get(), ml));
        return stack;
    }

    public void appendFluidText(ItemStack stack, List<Component> tooltip) {
        CompoundTag tag = stack.getTagElement("MultiFluidTank");
        boolean hasSpecial = false;
        if (tag == null) {
            return;
        }

        ListTag list = tag.getList("Fluids", Tag.TAG_COMPOUND);
        if (list.isEmpty()) {
            return;
        }

        tooltip.add(Component.translatable("casualties_cubed.tooltip.contents"));
        int color;
        FluidStack fs;
        Component desc;
        for (Tag t : list) {
            fs = FluidStack.loadFluidStackFromNBT((CompoundTag) t);
            color = MedicalFluidType.getColor(fs.getFluid());

            desc = MedicalFluidType.getDescription(fs.getFluid());
            hasSpecial = !desc.getContents().equals(ComponentContents.EMPTY);

            tooltip.add(Component.empty()
                    .append(fs.getDisplayName())
                    .withStyle(Style.EMPTY.withColor(color))
                    .append(Component.literal(" (").withStyle(ChatFormatting.GRAY))
                    .append(Component.translatable("casualties_cubed.tooltip.liquid.amount", fs.getAmount()).withStyle(ChatFormatting.GRAY))
                    .append(Component.literal(")").withStyle(ChatFormatting.GRAY)));
            if (hasSpecial && Screen.hasShiftDown()) {
                tooltip.add(desc.copy().withStyle(Style.EMPTY.withColor(color)));
            }
        }

        if (hasSpecial && !Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("casualties_cubed.multi_tank.hint").withStyle(ChatFormatting.GRAY));
        }
    }
}
