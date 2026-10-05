package net.zaharenko424.casualties_cubed.config;

import it.unimi.dsi.fastutil.floats.Float2ObjectFunction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public enum TempUnit {
    C(c -> Component.translatable("gui.casualties_cubed.temp_unit.c", String.format("%.1f", c))),
    F(c -> Component.translatable("gui.casualties_cubed.temp_unit.f", String.format("%.1f", (c * 9/5) + 32))),
    K(c -> Component.translatable("gui.casualties_cubed.temp_unit.k", String.format("%.1f", c + 273.15)));

    public final Float2ObjectFunction<MutableComponent> compFunc;

    TempUnit(Float2ObjectFunction<MutableComponent> compFunc) {
        this.compFunc = compFunc;
    }
}
