package net.zaharenko424.casualties_cubed.config;

import net.minecraft.util.FastColor;
import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue EXPERIMENTAL_VISUALS;
    public static final ForgeConfigSpec.BooleanValue EXPERIMENTAL_SOUNDS;

    public static final ForgeConfigSpec.IntValue UI_GLOW_COLOR;

    static {
        ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

        BUILDER.push("Prototype Pain Client Config");

        EXPERIMENTAL_VISUALS = BUILDER
                .comment("Experimental { ;) } Options")
                .define("additionalVisuals",false);

        EXPERIMENTAL_SOUNDS =BUILDER
                .define("additionalSounds",false);

        UI_GLOW_COLOR = BUILDER
                .comment("Glow color for health screen.")
                .defineInRange("uiGlowColor", FastColor.ARGB32.color(255, 47, 224, 129), Integer.MIN_VALUE, Integer.MAX_VALUE);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
