package net.zaharenko424.casualties_cubed.limbs;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.registries.RegistryObject;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.registry.TimedEffectRegistry;
import org.jetbrains.annotations.Nullable;

public class TimedEffect {

    private final TimedEffectFunction effect;
    private final float ml;
    private final Limb limb;

    private float duration;

    public TimedEffect(RegistryObject<TimedEffectFunction> effect, float ml, Limb limb, float duration) {
        this.effect = effect.get();
        this.ml = ml;
        this.limb = limb;
        this.duration = duration;
    }

    protected TimedEffect(CompoundTag tag) {
        String str = tag.getString("effect");
        effect = TimedEffectRegistry.registry().getValue(ResourceLocation.parse(str));
        assert effect != null;

        ml = tag.getFloat("ml");
        limb = tag.contains("limb", Tag.TAG_STRING) ? Limb.valueOf(tag.getString("limb")) : null;
        duration = tag.getFloat("duration");
    }

    public static @Nullable TimedEffect fromTag(CompoundTag tag) {
        try {
            return new TimedEffect(tag);
        } catch (IllegalStateException e) {
            CasualtiesCubed.LOGGER.warn("Exception while loading timed effect", e);
            return null;
        }
    }

    public boolean is(RegistryObject<TimedEffectFunction> effect) {
        return effect.get() == this.effect;
    }

    public Limb limb() {
        return limb;
    }

    public boolean isInvalid() {
        return duration <= 0;
    }

    public void addTime(float time) {
        duration += time;
    }

    public void update(ServerPlayer player, PlayerHealthData data) {
        duration--;
        effect.update(player, data, ml, limb, duration);
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putString("effect", TimedEffectRegistry.registry().getKey(effect).toString());
        tag.putFloat("ml", ml);
        if (limb != null) tag.putString("limb", limb.toString());
        tag.putFloat("duration", duration);
        return tag;
    }
}
