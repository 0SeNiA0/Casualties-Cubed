package net.zaharenko424.casualties_cubed;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;

public class ModDamageTypes {

    public static final ResourceKey<DamageType> GIVE_UP = key("give_up");
    public static final ResourceKey<DamageType> BLEED = key("bleed");
    public static final ResourceKey<DamageType> OPIOIDS = key("opioids");
    public static final ResourceKey<DamageType> HEAVY_BLEED = key("heavy_bleed");
    public static final ResourceKey<DamageType> INTERNAL_BLEED = key("internal_bleed");
    public static final ResourceKey<DamageType> OXYGEN = key("oxygen");

    private static ResourceKey<DamageType> key(String path) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, CasualtiesCubed.resourceLoc(path));
    }

    public static DamageSource giveUp(ServerLevel level) {
        return new DamageSource(level.registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(GIVE_UP));
    }
    public static DamageSource bleed(ServerLevel level) {
        return new DamageSource(level.registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(BLEED));
    }
    public static DamageSource heavy_bleed(ServerLevel level) {
        return new DamageSource(level.registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(HEAVY_BLEED));
    }
    public static DamageSource opioids(ServerLevel level) {
        return new DamageSource(level.registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(OPIOIDS));
    }
    public static DamageSource internal(ServerLevel level) {
        return new DamageSource(level.registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(INTERNAL_BLEED));
    }
    public static DamageSource oxygen(ServerLevel level) {
        return new DamageSource(level.registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(OXYGEN));
    }
}
