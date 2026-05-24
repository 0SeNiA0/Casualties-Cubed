package net.zaharenko424.casualties_cubed.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.*;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.client.gui.MedicalMixerScreen;
import net.zaharenko424.casualties_cubed.item.multi_tank.MultiTankFluidItem;
import net.zaharenko424.casualties_cubed.registry.ModItems;
import net.zaharenko424.casualties_cubed.recipe.MedicalMixerRecipe;
import net.zaharenko424.casualties_cubed.registry.ModRecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;

@JeiPlugin
public class JeiCompatPlugin implements IModPlugin {

    private static final ResourceLocation ID = CasualtiesCubed.resourceLoc( "jei_recipe");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.useNbtForSubtypes(ModItems.ITEMS.getEntries().stream().map(RegistryObject::get).filter(item -> item instanceof MultiTankFluidItem).toArray(Item[]::new));
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new MedicalMixerCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModItems.MEDICAL_MIXER.get()),MedicalMixerCategory.MEDICAL_MIXER_RECIPE_TYPE);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();

        List<MedicalMixerRecipe> recipes = manager.getAllRecipesFor(ModRecipes.MEDICAL_MIXER_RECIPE.get());
        registration.addRecipes(MedicalMixerCategory.MEDICAL_MIXER_RECIPE_TYPE,recipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(MedicalMixerScreen.class,90,43,36,10,MedicalMixerCategory.MEDICAL_MIXER_RECIPE_TYPE);
    }
}
