package net.zaharenko424.casualties_cubed.fluid_system;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.ForgeRegistries;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.limbs.Limb;

import java.util.function.Consumer;

public class MedicalFluidType extends FluidType {

    private final MedicalEffect effect;
    private final int color;

    public MedicalFluidType(MedicalEffect effect, int color) {
        this(Properties.create(), effect, color);
    }

    public MedicalFluidType(Properties properties, MedicalEffect effect, int color) {
        super(properties);
        this.effect = effect;
        this.color = color;
    }

    private static MedicalEffect effect(Fluid fluid) {
        return fluid.getFluidType() instanceof MedicalFluidType type ? type.effect : ExtraMedFluids.getOrDef(fluid).effect();
    }

    public static void ingest(ServerPlayer target, float ml, Fluid fluid) {
        effect(fluid).applyIngested(target, ml);
    }

    public static void inject(ServerPlayer target, float ml, Limb limb, Fluid fluid) {
        MedicalEffect effect = effect(fluid);

        float injectionSickness = effect.injectionSickness();
        if (injectionSickness > 0) {
            target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.addSickness(0.3f * injectionSickness * ml);
                data.setBloodViscosity(data.getBloodViscosity() - 0.1f * injectionSickness);//might be a bug but in CU viscosity penalty doesnt scale with ml
            });
        }

        effect.applyInjected(target, ml, limb);
    }

    public static void apply(ServerPlayer target, float ml, Limb limb, Fluid fluid) {
        effect(fluid).applyOnSkin(target, ml, limb);
    }

    public MedicalEffect effect() {
        return effect;
    }

    public int getColor() {
        return color;
    }

    public static int getColor(Fluid fluid) {
        if (fluid.getFluidType() instanceof MedicalFluidType medFluid) return medFluid.getColor();

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
                return MedicalFluidType.getColor(stack.getFluid()) | 0xFF000000;
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
