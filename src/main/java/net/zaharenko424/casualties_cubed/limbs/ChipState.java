package net.zaharenko424.casualties_cubed.limbs;

public enum ChipState {
    ACTIVE,
    MALFUNCTION,
    UNCHIPPED;

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean isInactive() {
        return !isActive();
    }
}
