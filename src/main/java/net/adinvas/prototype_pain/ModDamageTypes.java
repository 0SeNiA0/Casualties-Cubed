package net.adinvas.prototype_pain;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;

public class ModDamageTypes {

    public static final ResourceKey<DamageType> GIVE_UP = ResourceKey.create(Registries.DAMAGE_TYPE,
            PrototypePain.resourceLoc("give_up"));
    public static final ResourceKey<DamageType> BLEED = ResourceKey.create(Registries.DAMAGE_TYPE,
            PrototypePain.resourceLoc("bleed"));
    public static final ResourceKey<DamageType> OPIOIDS = ResourceKey.create(Registries.DAMAGE_TYPE,
            PrototypePain.resourceLoc("opioids"));
    public static final ResourceKey<DamageType> HEAVY_BLEED = ResourceKey.create(Registries.DAMAGE_TYPE,
            PrototypePain.resourceLoc("heavy_bleed"));
    public static final ResourceKey<DamageType> INTERNAL_BLEED = ResourceKey.create(Registries.DAMAGE_TYPE,
            PrototypePain.resourceLoc("internal_bleed"));
    public static final ResourceKey<DamageType> OXYGEN = ResourceKey.create(Registries.DAMAGE_TYPE,
            PrototypePain.resourceLoc("oxygen"));

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
