package net.zaharenko424.casualties_cubed.fluid_system;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;

public class MedicalFluidType extends FluidType {

    private final int color;

    public MedicalFluidType(Properties properties, int color) {
        super(properties);
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public static int getColor(Fluid fluid) {
        if (fluid.getFluidType() instanceof MedicalFluidType medFluid) return medFluid.getColor();

        if (fluid.getFluidType() == ForgeMod.WATER_TYPE.get()) return 0x5276d1;

        return ExtraMedFluids.getOrDef(fluid).color();
    }

    @Override
    public String getDescriptionId(FluidStack stack) {
        return BuiltInRegistries.FLUID.getKey(stack.getFluid()).toLanguageKey("medical_fluid");
    }

    public Component getDescription() {
        ResourceLocation id = ForgeRegistries.FLUID_TYPES.get().getKey(this);
        return Component.translatable("medical_fluid." + id.getNamespace()
                + "." + id.getPath()
                + ".description");
    }

    public static Component getDescription(Fluid fluid) {
        if (fluid.getFluidType() instanceof MedicalFluidType medFluid) return medFluid.getDescription();

        return ExtraMedFluids.getOrDef(fluid).description();
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {

            @Override
            public int getTintColor(FluidStack stack) {
                if (stack.getFluid().getFluidType() instanceof MedicalFluidType fluid) {
                    return fluid.color | 0xFF000000;
                }

                // fallback color
                return 0xFFFFFFFF;
            }

            @Override
            public ResourceLocation getStillTexture() {
                return ResourceLocation.withDefaultNamespace("block/water_still");
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return ResourceLocation.withDefaultNamespace("block/water_flow");
            }
        });
    }
}
