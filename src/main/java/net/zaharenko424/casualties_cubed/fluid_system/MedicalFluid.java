package net.zaharenko424.casualties_cubed.fluid_system;


import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.IForgeRegistry;
import net.zaharenko424.casualties_cubed.registry.ModFluids;
import net.zaharenko424.casualties_cubed.registry.ModMedicalRegistry;

public class MedicalFluid {

    private final MedicalEffect medicalEffect;
    private final int color;

    public MedicalFluid(MedicalEffect medicalEffect, int color) {
        this.medicalEffect = medicalEffect;
        this.color = color;
    }

    public MedicalEffect getMedicalEffect() {
        return medicalEffect;
    }

    public ResourceLocation getRegistryId() {
        IForgeRegistry<MedicalFluid> reg = ModMedicalRegistry.REGISTRY.get();
        return reg.getKey(this);
    }

    public Component getDisplayName() {
        ResourceLocation id = getRegistryId();
        return Component.translatable("medical_fluid." + id.getNamespace() + "." + id.getPath());
    }

    public Component getDescription() {
        ResourceLocation id = getRegistryId();
        return Component.translatable("medical_fluid." + id.getNamespace()
                + "." + id.getPath()
                + ".description");
    }

    public String getNameId(){
        ResourceLocation id = getRegistryId();
        return "medical_fluid." + id.getNamespace()
                + "." + id.getPath();
    }

    public static MedicalFluid getFromId(String fullId) {
        if (fullId == null) return null;

        ResourceLocation id = ResourceLocation.parse(fullId);

        IForgeRegistry<MedicalFluid> reg = ModMedicalRegistry.REGISTRY.get();
        return reg.getValue(id);
    }

    public int getColor() {
        return color;
    }

    public boolean showInJEI(){
        return true;
    }
    public boolean showInTooltip(ItemStack stack){
        return true;
    }

    public boolean is(TagKey<MedicalFluid> tagKey){
        return ModMedicalRegistry.REGISTRY.get().tags().getTag(tagKey).contains(this);
    }

    public FluidStack getAsStack(int amount){
        FluidStack stack = new FluidStack(ModFluids.SRC_MEDICAL.get().getSource(),amount);
        stack.getOrCreateTag().putString("MedicalId",this.getRegistryId().toString());
        return stack;
    }
}
