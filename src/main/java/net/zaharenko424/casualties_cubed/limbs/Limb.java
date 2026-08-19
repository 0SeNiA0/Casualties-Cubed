package net.zaharenko424.casualties_cubed.limbs;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Random;

public enum Limb {
    HEAD(Component.translatable("casualties_cubed.limb.head")),
    THORAX(Component.translatable("casualties_cubed.limb.thorax")),
    ABDOMEN(Component.translatable("casualties_cubed.limb.abdomen")),
    UPPER_RIGHT_ARM(Component.translatable("casualties_cubed.limb.upper_right_arm")),
    LOWER_RIGHT_ARM(Component.translatable("casualties_cubed.limb.lower_right_arm")),
    UPPER_LEFT_ARM(Component.translatable("casualties_cubed.limb.upper_left_arm")),
    LOWER_LEFT_ARM(Component.translatable("casualties_cubed.limb.lower_left_arm")),
    RIGHT_HAND(Component.translatable("casualties_cubed.limb.right_hand")),
    LEFT_HAND(Component.translatable("casualties_cubed.limb.left_hand")),
    UPPER_RIGHT_LEG(Component.translatable("casualties_cubed.limb.upper_right_leg")),
    LOWER_RIGHT_LEG(Component.translatable("casualties_cubed.limb.lower_right_leg")),
    UPPER_LEFT_LEG(Component.translatable("casualties_cubed.limb.upper_left_leg")),
    LOWER_LEFT_LEG(Component.translatable("casualties_cubed.limb.lower_left_leg")),
    LEFT_FOOT(Component.translatable("casualties_cubed.limb.left_foot")),
    RIGHT_FOOT(Component.translatable("casualties_cubed.limb.right_foot"));

    public final Component comp;

    Limb(Component comp) {
        this.comp = comp;
    }

    public static final List<Limb> ARM_LIMBS_RIGHT = List.of(
            UPPER_RIGHT_ARM, LOWER_RIGHT_ARM, RIGHT_HAND
    );

    public static final List<Limb> ARM_LIMBS_LEFT = List.of(
            UPPER_LEFT_ARM, LOWER_LEFT_ARM, LEFT_HAND
    );

    public static final List<Limb> LEG_LIMBS_RIGHT = List.of(
            UPPER_RIGHT_LEG, LOWER_RIGHT_LEG, RIGHT_FOOT
    );

    public static final List<Limb> LEG_LIMBS_LEFT = List.of(
            UPPER_LEFT_LEG, LOWER_LEFT_LEG, LEFT_FOOT
    );

    public static final List<Limb> LEG_LIMBS = List.of(
            UPPER_RIGHT_LEG, LOWER_RIGHT_LEG, RIGHT_FOOT,
            UPPER_LEFT_LEG, LOWER_LEFT_LEG, LEFT_FOOT
    );

    public static Limb getFromHand(InteractionHand hand, Player player) {
        HumanoidArm arm = player.getMainArm();
        if (hand == InteractionHand.MAIN_HAND) {
            return arm == HumanoidArm.RIGHT ? UPPER_RIGHT_ARM : UPPER_LEFT_ARM;
        }

        return arm == HumanoidArm.LEFT ? UPPER_LEFT_ARM : UPPER_RIGHT_ARM;
    }

    public Limb getConnectedTo() {
        return switch (this) {
            case HEAD, UPPER_RIGHT_ARM, UPPER_LEFT_ARM, ABDOMEN -> THORAX;
            case UPPER_RIGHT_LEG, UPPER_LEFT_LEG -> ABDOMEN;
            case LOWER_RIGHT_ARM -> UPPER_RIGHT_ARM;
            case LOWER_LEFT_ARM -> UPPER_LEFT_ARM;
            case RIGHT_HAND -> LOWER_RIGHT_ARM;
            case LEFT_HAND -> LOWER_LEFT_ARM;
            case LOWER_RIGHT_LEG -> UPPER_RIGHT_LEG;
            case LOWER_LEFT_LEG -> UPPER_LEFT_LEG;
            case RIGHT_FOOT -> LOWER_RIGHT_LEG;
            case LEFT_FOOT -> LOWER_LEFT_LEG;
            case THORAX -> HEAD;//Chest and head loop but that shouldn't be an issue
        };
    }

    public List<Limb> getLowerAndSelf() {
        return switch (this) {
            case THORAX -> List.of(THORAX);
            case ABDOMEN -> List.of(ABDOMEN);
            case UPPER_RIGHT_ARM -> List.of(UPPER_RIGHT_ARM, LOWER_RIGHT_ARM, RIGHT_HAND);
            case LOWER_RIGHT_ARM -> List.of(LOWER_RIGHT_ARM, RIGHT_HAND);
            case RIGHT_HAND -> List.of(RIGHT_HAND);
            case UPPER_LEFT_ARM -> List.of(UPPER_LEFT_ARM, LOWER_LEFT_ARM, LEFT_HAND);
            case LOWER_LEFT_ARM -> List.of(LOWER_LEFT_ARM, LEFT_HAND);
            case LEFT_HAND -> List.of(LEFT_HAND);
            case UPPER_RIGHT_LEG -> List.of(UPPER_RIGHT_LEG, LOWER_RIGHT_LEG, RIGHT_FOOT);
            case LOWER_RIGHT_LEG -> List.of(LOWER_RIGHT_LEG, RIGHT_FOOT);
            case RIGHT_FOOT -> List.of(RIGHT_FOOT);
            case UPPER_LEFT_LEG -> List.of(UPPER_LEFT_LEG, LOWER_LEFT_LEG, LEFT_FOOT);
            case LOWER_LEFT_LEG -> List.of(LOWER_LEFT_LEG, LEFT_FOOT);
            case LEFT_FOOT -> List.of(LEFT_FOOT);
            case HEAD -> List.of(HEAD);
        };
    }

    public List<Limb> getConnectedLimbs(){
        return switch (this){
            case THORAX -> List.of(HEAD, UPPER_RIGHT_ARM, UPPER_LEFT_ARM, ABDOMEN);
            case ABDOMEN -> List.of(THORAX, UPPER_RIGHT_LEG, UPPER_LEFT_LEG);
            case UPPER_RIGHT_ARM -> List.of(THORAX, LOWER_RIGHT_ARM);
            case LOWER_RIGHT_ARM -> List.of(UPPER_RIGHT_ARM, RIGHT_HAND);
            case RIGHT_HAND -> List.of(LOWER_RIGHT_ARM);
            case UPPER_LEFT_ARM -> List.of(THORAX, LOWER_LEFT_ARM);
            case LOWER_LEFT_ARM -> List.of(UPPER_LEFT_ARM, LEFT_HAND);
            case LEFT_HAND -> List.of(LOWER_LEFT_ARM);
            case UPPER_RIGHT_LEG -> List.of(ABDOMEN, LOWER_RIGHT_LEG);
            case LOWER_RIGHT_LEG -> List.of(UPPER_RIGHT_LEG, RIGHT_FOOT);
            case RIGHT_FOOT -> List.of(Limb.LOWER_RIGHT_LEG);
            case UPPER_LEFT_LEG -> List.of(ABDOMEN, LOWER_LEFT_LEG);
            case LOWER_LEFT_LEG -> List.of(UPPER_LEFT_LEG, LEFT_FOOT);
            case LEFT_FOOT -> List.of(Limb.LOWER_LEFT_LEG);
            case HEAD -> List.of(Limb.THORAX);
        };
    }

    static Limb randomLimb(){
        Limb[] values = Limb.values();
        Random rand = new Random();
        return values[rand.nextInt(values.length)];
    }

    public static Limb weigtedRandomLimb(){
        Limb[] limb_list = {
                HEAD,
                HEAD,
                THORAX,
                THORAX,
                THORAX,
                ABDOMEN,
                ABDOMEN,
                ABDOMEN,
                UPPER_RIGHT_ARM,
                UPPER_RIGHT_ARM,
                LOWER_RIGHT_ARM,
                LOWER_RIGHT_ARM,
                UPPER_LEFT_ARM,
                UPPER_LEFT_ARM,
                LOWER_LEFT_ARM,
                LOWER_LEFT_ARM,
                UPPER_RIGHT_LEG,
                UPPER_RIGHT_LEG,
                LOWER_RIGHT_LEG,
                LOWER_RIGHT_LEG,
                UPPER_LEFT_LEG,
                UPPER_LEFT_LEG,
                LOWER_LEFT_LEG,
                LOWER_LEFT_LEG,
                RIGHT_HAND,
                RIGHT_HAND,
                LEFT_HAND,
                LEFT_HAND,
                RIGHT_FOOT,
                RIGHT_FOOT,
                LEFT_FOOT,
                LEFT_FOOT
        };
        Random rand = new Random();
        return limb_list[rand.nextInt(limb_list.length)];
    }

    public static HumanoidArm getArmFromHand(InteractionHand hand, Player player){
        HumanoidArm mainArm = player.getMainArm();
        if (hand == InteractionHand.MAIN_HAND) {
            // MAIN_HAND always uses the player's dominant arm
            return mainArm;
        } else {
            // OFF_HAND is always the opposite of the main arm
            return (mainArm == HumanoidArm.RIGHT) ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
        }
    }

    public List<Limb> availableHandsForAction(){
        return switch (this) {
            case RIGHT_HAND, LOWER_RIGHT_ARM, UPPER_RIGHT_ARM -> List.of(Limb.LEFT_HAND);
            case LOWER_LEFT_ARM, UPPER_LEFT_ARM, LEFT_HAND -> List.of(Limb.RIGHT_HAND);
            default -> List.of(RIGHT_HAND, LEFT_HAND);
        };
    }
}

