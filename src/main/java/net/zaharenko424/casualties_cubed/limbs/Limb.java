package net.zaharenko424.casualties_cubed.limbs;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Random;

public enum Limb {
    HEAD(Component.translatable("casualties_cubed.limb.head")),
    CHEST(Component.translatable("casualties_cubed.limb.chest")),
    LEFT_ARM(Component.translatable("casualties_cubed.limb.left_arm")),
    RIGHT_ARM(Component.translatable("casualties_cubed.limb.right_arm")),
    RIGHT_HAND(Component.translatable("casualties_cubed.limb.right_hand")),
    LEFT_HAND(Component.translatable("casualties_cubed.limb.left_hand")),
    LEFT_LEG(Component.translatable("casualties_cubed.limb.left_leg")),
    RIGHT_LEG(Component.translatable("casualties_cubed.limb.right_leg")),
    LEFT_FOOT(Component.translatable("casualties_cubed.limb.left_foot")),
    RIGHT_FOOT(Component.translatable("casualties_cubed.limb.right_foot"));

    public final Component comp;

    Limb(Component comp) {
        this.comp = comp;
    }

    public static Limb getFromHand(InteractionHand hand, Player player) {
        HumanoidArm arm = player.getMainArm();
        if (hand == InteractionHand.MAIN_HAND) {
            return arm == HumanoidArm.RIGHT ? RIGHT_ARM : LEFT_ARM;
        }

        return arm == HumanoidArm.LEFT ? LEFT_ARM : RIGHT_ARM;
    }

    public Limb randomFromConectedLimb(){
        List<Limb> limbs = this.getConnectedLimbs();
        Random rand = new Random();
        return limbs.get(rand.nextInt(limbs.size()));
    }

    public Limb getConnectedTo() {
        return switch (this) {
            case HEAD, RIGHT_ARM, LEFT_ARM, RIGHT_LEG, LEFT_LEG -> CHEST;
            case RIGHT_HAND -> RIGHT_ARM;
            case LEFT_HAND -> LEFT_ARM;
            case RIGHT_FOOT -> RIGHT_LEG;
            case LEFT_FOOT -> LEFT_LEG;
            case CHEST -> HEAD;//Chest and head loop but that shouldn't be an issue
        };
    }

    public List<Limb> getLowerAndSelf() {
        return switch (this) {
            case CHEST -> List.of();
            case LEFT_ARM -> List.of(LEFT_ARM, LEFT_HAND);
            case RIGHT_ARM -> List.of(RIGHT_ARM, RIGHT_HAND);
            case LEFT_HAND -> List.of(LEFT_HAND);
            case RIGHT_HAND -> List.of(RIGHT_HAND);
            case LEFT_LEG -> List.of(LEFT_LEG, LEFT_FOOT);
            case RIGHT_LEG -> List.of(RIGHT_LEG, RIGHT_FOOT);
            case LEFT_FOOT -> List.of(LEFT_FOOT);
            case RIGHT_FOOT -> List.of(RIGHT_FOOT);
            case HEAD -> List.of(HEAD);
        };
    }

    public List<Limb> getConnectedLimbs(){
        return switch (this){
            case CHEST -> List.of(HEAD, RIGHT_ARM, LEFT_ARM, RIGHT_LEG, LEFT_LEG);
            case LEFT_ARM -> List.of(CHEST, LEFT_HAND);
            case LEFT_HAND -> List.of(Limb.LEFT_ARM);
            case RIGHT_ARM -> List.of(CHEST, RIGHT_HAND);
            case RIGHT_HAND -> List.of(Limb.RIGHT_ARM);
            case LEFT_LEG -> List.of(CHEST, LEFT_FOOT);
            case RIGHT_LEG -> List.of(CHEST, RIGHT_FOOT);
            case LEFT_FOOT -> List.of(Limb.LEFT_LEG);
            case RIGHT_FOOT -> List.of(Limb.RIGHT_LEG);
            case HEAD -> List.of(Limb.CHEST);
        };
    }

    static Limb randomLimb(){
        Limb[] values = Limb.values();
        Random rand = new Random();
        return values[rand.nextInt(values.length)];
    }

    public static Limb weigtedRandomLimb(){
        Limb[] limb_list = {
                HEAD, //~4%
                CHEST,//~8%
                CHEST,
                RIGHT_ARM,//~12%
                RIGHT_ARM,
                RIGHT_ARM,
                LEFT_ARM,//~12%
                LEFT_ARM,
                LEFT_ARM,
                RIGHT_LEG,//~12%
                RIGHT_LEG,
                RIGHT_LEG,
                LEFT_LEG,//~12%
                LEFT_LEG,
                LEFT_LEG,
                RIGHT_HAND,//~8%
                RIGHT_HAND,
                LEFT_HAND,//~8%
                LEFT_HAND,
                RIGHT_FOOT,//~8%
                RIGHT_FOOT,
                LEFT_FOOT,//~8%
                LEFT_FOOT,
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
            case RIGHT_HAND, RIGHT_ARM -> List.of(Limb.LEFT_HAND);
            case LEFT_ARM, LEFT_HAND -> List.of(Limb.RIGHT_HAND);
            default -> List.of(RIGHT_HAND, LEFT_HAND);
        };
    }
}

