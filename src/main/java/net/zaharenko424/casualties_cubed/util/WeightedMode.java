package net.zaharenko424.casualties_cubed.util;

public enum WeightedMode {
    NONE,
    IN,
    OUT,
    BOTH;

    public boolean isIn() {
        return this == IN || this == BOTH;
    }

    public boolean isOut() {
        return this == OUT || this == BOTH;
    }
}
