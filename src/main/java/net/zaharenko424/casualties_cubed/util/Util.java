package net.zaharenko424.casualties_cubed.util;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.joml.Vector2f;

import java.text.DecimalFormat;
import java.util.UUID;

public class Util {

    public static final long INIT_TIME_MS = System.currentTimeMillis();

    public static final float TICK_TO_SEC = 1 / 20f;
    public static final float TICK_TO_MIN = TICK_TO_SEC / 60;
    public static final float ML_TO_L = 1 / 1000f;
    public static final float CU_BLOOD_POINT_AS_L = 0.025f;

    public static final DecimalFormat ONE_OPTIONAL = new DecimalFormat("0.#");

    public static final Vector2f REUSABLE_2F = new Vector2f();

    public static float CUBloodPointsToL(float cuPoints) {
        return cuPoints * CU_BLOOD_POINT_AS_L;
    }

    public static float moveTowards(float moveAmount, float start, float end) {
        if (Math.abs(end - start) <= moveAmount) return end;

        return start + Math.signum(end - start) * moveAmount;
    }

    public static float min(float... values) {
        if (values.length == 0) return 0;

        float min = values[0];
        for (int i = 1; i < values.length; i++) {
            if (values[i] < min) min = values[i];
        }

        return min;
    }

    public static float remap(float value, float from1, float to1, float from2, float to2) {
        return (value - from1) / (to1 - from1) * (to2 - from2) + from2;
    }

    public static Vector2f clampLength(Vector2f vec, float length) {
        float lengthSqr = vec.x * vec.x + vec.y + vec.y;
        if (lengthSqr <= length * length) return vec;

        float invLength = org.joml.Math.invsqrt(lengthSqr) * length;
        return vec.mul(invLength);
    }

    public static float repeat(float time, float length) {
        return Mth.clamp(time - Mth.floor(time / length) * length, 0f, length);
    }

    public static float pingPong(float time, float length) {
        time = repeat(time, length * 2f);
        return length - Math.abs(time - length);
    }

    public static Level level() {
        if (FMLEnvironment.dist.isDedicatedServer()) {
            return ServerLifecycleHooks.getCurrentServer().overworld();
        }

        return DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> Minecraft.getInstance().level);
    }

    public static void maybeApplyModifier(Player player, Attribute attribute, String name, UUID modifierId, double amount, AttributeModifier.Operation operation) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance == null) return;

        instance.removeModifier(modifierId);

        // Skip adding modifier if multiplier = 0 (no change)
        if (amount == 0.0) return;

        instance.addTransientModifier(new AttributeModifier(modifierId, name, amount, operation));
    }

    public static void maybeRemoveModifier(Player player, Attribute attribute, UUID modifierId) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance != null) instance.removeModifier(modifierId);
    }

    public static String formatDuration(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        if (totalSeconds < 60) {
            return seconds + "s";
        } else if (totalSeconds < 3600) {
            return minutes + "m " + seconds + "s";
        } else {
            return hours + "h " + minutes + "m " + seconds + "s";
        }
    }

    public static boolean tagsEqual(CompoundTag a, CompoundTag b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }
}
