package net.zaharenko424.casualties_cubed.hitbox;

import net.zaharenko424.casualties_cubed.limbs.Limb;

import java.util.List;

public enum HitSector {
    HEAD(List.of(Limb.HEAD)),
    TORSO(List.of(Limb.THORAX, Limb.ABDOMEN)),
    LEFT_ARM(List.of(Limb.UPPER_LEFT_ARM, Limb.LOWER_LEFT_ARM, Limb.LEFT_HAND)),
    RIGHT_ARM(List.of(Limb.UPPER_RIGHT_ARM, Limb.LOWER_LEFT_ARM, Limb.RIGHT_HAND)),
    LEGS(Limb.LEG_LIMBS);

    public final List<Limb> limbs;

    HitSector(List<Limb> limbs) {
        this.limbs = limbs;
    }
}
