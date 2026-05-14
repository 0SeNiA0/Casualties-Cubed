package net.zaharenko424.casualties_cubed.hitbox;

import net.zaharenko424.casualties_cubed.limbs.Limb;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum HitSector {
    HEAD,
    TORSO,
    LEFT_ARM,
    RIGHT_ARM,
    LEGS;

    public List<Limb> getLimbsPerSector(){
        return switch (this){
            case RIGHT_ARM -> List.of(Limb.RIGHT_ARM, Limb.RIGHT_HAND);
            case HEAD -> List.of(Limb.HEAD);
            case TORSO -> List.of(Limb.CHEST);
            case LEFT_ARM -> List.of(Limb.LEFT_ARM, Limb.LEFT_HAND);
            case LEGS -> List.of(Limb.RIGHT_LEG, Limb.RIGHT_FOOT, Limb.LEFT_LEG, Limb.LEFT_FOOT);
        };
    }

    public static HitSector getCBCChances(){
        List<HitSector> limbList = new ArrayList<>();
        Random random = new Random();
        limbList.add(HitSector.HEAD);
        limbList.add(HitSector.TORSO);
        limbList.add(HitSector.TORSO);
        limbList.add(HitSector.TORSO);
        limbList.add(HitSector.LEGS);
        limbList.add(HitSector.LEGS);
        limbList.add(HitSector.LEFT_ARM);
        limbList.add(HitSector.RIGHT_ARM);
        return limbList.get(random.nextInt(limbList.size()));
    }
}
