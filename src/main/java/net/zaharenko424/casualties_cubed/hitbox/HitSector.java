package net.zaharenko424.casualties_cubed.hitbox;

import net.zaharenko424.casualties_cubed.limbs.Limb;

import java.util.List;

public enum HitSector {
    HEAD(List.of(Limb.HEAD)),
    TORSO(List.of(Limb.CHEST)),
    LEFT_ARM(List.of(Limb.LEFT_ARM, Limb.LEFT_HAND)),
    RIGHT_ARM(List.of(Limb.RIGHT_ARM, Limb.RIGHT_HAND)),
    LEGS(List.of(Limb.RIGHT_LEG, Limb.RIGHT_FOOT, Limb.LEFT_LEG, Limb.LEFT_FOOT));

    public final List<Limb> limbs;

    HitSector(List<Limb> limbs) {
        this.limbs = limbs;
    }
}
