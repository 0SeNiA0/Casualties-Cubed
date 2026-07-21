package net.zaharenko424.casualties_cubed.util;

public enum WrapMode {
    ONCE,
    LOOP,
    ///Not implemented
    PING_PONG,
    DEFAULT,
    CLAMP_FOREVER,
    CLAMP;

    public boolean isClamp() {
        return this == ONCE || this == DEFAULT || this == CLAMP_FOREVER || this == CLAMP;
    }
}
