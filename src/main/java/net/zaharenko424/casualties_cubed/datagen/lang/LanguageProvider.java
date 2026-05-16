package net.zaharenko424.casualties_cubed.datagen.lang;

import net.minecraft.client.KeyMapping;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.RegistryObject;
import net.zaharenko424.casualties_cubed.fluid_system.MedicalFluid;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Locale;
import java.util.function.Supplier;
import java.util.stream.Collectors;

//Helper methods from a_changed(mostly*)
public abstract class LanguageProvider extends net.minecraftforge.common.data.LanguageProvider {

    protected final String modid;

    public LanguageProvider(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
        this.modid = modid;
    }

    protected void addMoodle(String key, String value) {
        addGuiO("moodle." + key, value);
    }

    protected void addAdvancement(String key, String title, String description) {
        key = "advancements." + key;//TODO add modid eventually...
        //key = "advancements." + modid + "." + key;
        add(key + ".title", title);
        add(key + ".descr", description);//TODO rename eventually.
        //add(key + ".description", description);
    }

    protected void addAttribute(RegistryObject<Attribute> attribute, String value) {
        add(toLanguageKey(attribute.getId(), "attribute"), value);
    }

    protected void addBlockFromId(RegistryObject<? extends Block> block) {
        addBlock(block, Arrays.stream(block.getId().getPath().split("_"))
                .map(word -> word.substring(0, 1).toUpperCase(Locale.ROOT) + word.substring(1))
                .collect(Collectors.joining(" ")));
    }

    public void addFluidType(Supplier<? extends FluidType> key, String name) {
        add(key.get(), name);
    }

    public void add(FluidType key, String name) {
        add(key.getDescriptionId(), name);
    }

    protected void addMedicalFluidWDesc(RegistryObject<? extends MedicalFluid> fluid, String name, String desc) {
        add(fluid.getId().toLanguageKey("medical_fluid"), name);
        add(fluid.getId().toLanguageKey("medical_fluid", "description"), desc);
    }

    protected void addMedicalFluidFromIdWDesc(RegistryObject<? extends MedicalFluid> fluid, String desc) {
        add(fluid.getId().toLanguageKey("medical_fluid"), Arrays.stream(fluid.getId().getPath().split("_"))
                .map(word -> word.substring(0, 1).toUpperCase(Locale.ROOT) + word.substring(1))
                .collect(Collectors.joining(" ")));
        add(fluid.getId().toLanguageKey("medical_fluid", "description"), desc);
    }

    protected void addFluidTypeFromId(RegistryObject<? extends FluidType> fluidType) {
        addFluidType(fluidType, Arrays.stream(fluidType.getId().getPath().split("_"))
                .map(word -> word.substring(0, 1).toUpperCase(Locale.ROOT) + word.substring(1))
                .collect(Collectors.joining(" ")));
    }

    protected void addEntityFromId(RegistryObject<? extends EntityType<?>> entity) {
        addEntityType(entity, Arrays.stream(entity.getId().getPath().split("_"))
                .map(word -> word.substring(0, 1).toUpperCase(Locale.ROOT) + word.substring(1))
                .collect(Collectors.joining(" ")));
    }

    protected void addCommand(String key, String value) {
        add("commands." + modid + "." + key, value);
    }

    protected void addContainer(String key, String value) {
        add("container." + modid + "." + key, value);
    }

    protected void addDeathMessage(String damageSource, String generic, @Nullable String item, @Nullable String player) {
        String key = "death.attack." + damageSource;
        add(key, generic);
        if (item != null) add(key + ".item", item);
        if (player != null) add(key + ".player", player);
    }

    protected void addGamerule(GameRules.Key<?> rule, String value, @Nullable String description) {
        String key = "gamerule." + rule.getId();
        add(key, value);
        if (description != null) add(key + ".description", description);
    }

    protected void addKey(KeyMapping key, String value) {
        add(key.getName(), value);
    }

    protected void addKey(KeyMapping key, String path, String value) {
        add(key.getName() + "." + path, value);
    }

    protected void addItemWDesc(RegistryObject<? extends Item> item, String name, String desc) {
        addItem(item, name);
        add(item.get().getDescriptionId() + ".description", desc);
    }

    protected void addItemFromId(RegistryObject<? extends Item> item) {
        addItem(item, Arrays.stream(item.getId().getPath().split("_"))
                .map(word -> word.substring(0, 1).toUpperCase(Locale.ROOT) + word.substring(1))
                .collect(Collectors.joining(" ")));
    }

    protected void addItemFromIdWDesc(RegistryObject<? extends Item> item, String desc) {
        addItem(item, Arrays.stream(item.getId().getPath().split("_"))
                .map(word -> word.substring(0, 1).toUpperCase(Locale.ROOT) + word.substring(1))
                .collect(Collectors.joining(" ")));
        add(item.get().getDescriptionId() + ".description", desc);
    }

    protected void addMessage(String key, String value) {
        add("message." + modid + "." + key, value);
    }

    protected void addMisc(String key, String value) {
        add("misc." + modid + "." + key, value);
    }

    protected void addScreen(String key, String value) {
        add("screen." + modid + "." + key, value);
    }

    protected void addSound(RegistryObject<SoundEvent> sound, String value) {
        add(toLanguageKey(sound.getId(), "subtitles"), value);
    }

    protected void addTooltipO(String key, String value) {
        add(modid + ".tooltip." + key, value);
    }

    protected void addTooltip(String key, String value) {
        add("tooltip." + modid + "." + key, value);
    }

    protected void addText(String key, String value) {
        add("text." + modid + "." + key, value);
    }


    protected void addBestiaryText(String key, String value) {
        add("text." + modid + "." + "bestiary." + key, value);
    }

    protected void addBestiaryDesc(String key, String value) {
        add("text." + modid + "." + "bestiary." + key + ".desc", value);
    }

    protected void addBestiaryTitle(String key, String value) {
        add("text." + modid + "." + "bestiary." + key + ".title", value);
    }

    protected void addAbility(String key, String value) {
        add("ability." + modid + "." + key, value);
    }

    protected void addEntityDialogues(String key, String value) {
        add("entity_dialogues." + modid + "." + key, value);
    }

    protected void addGuiO(String key, String value) {
        add(modid + ".gui." + key, value);
    }

    protected void addGui(String key, String value) {
        add("gui." + modid + "." + key, value);
    }

    protected void addJeiDescriptions(String key, String value) {
        add("jei_descriptions." + modid + "." + key, value);
    }

    protected void addEffect(RegistryObject<? extends MobEffect> effect, String value, @Nullable String description) {
        String key = effect.get().getDescriptionId();
        add(key, value);
        if (description != null) add(key + ".description", description);
    }

    protected void addEnchantment(RegistryObject<? extends Enchantment> enchantment, String value, @Nullable String description, @Nullable String jeiDescription) {
        String key = enchantment.get().getDescriptionId();
        add(key, value);
        if (description != null) add(key + ".desc", description);
        if (jeiDescription != null) add(key + ".jei_desc", jeiDescription);
    }

    protected void addStat(RegistryObject<ResourceLocation> stat, String value) {
        add(stat.get().toLanguageKey("stat"), value);
    }

    protected String toLanguageKey(ResourceLocation loc, String type) {
        return type + "." + loc.getNamespace() + "." + loc.getPath();
    }
}
