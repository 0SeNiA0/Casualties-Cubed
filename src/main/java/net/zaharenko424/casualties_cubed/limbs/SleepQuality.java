package net.zaharenko424.casualties_cubed.limbs;

public enum SleepQuality {
    BAD(0.7f),
    MEDIOCRE(0.85f),
    OKAY(1),
    GOOD(1.25f);

    public final float regen;

    SleepQuality(float regen) {
        this.regen = regen;
    }
}
