package net.adinvas.casualties_cubed.registry;

import net.minecraft.world.level.GameRules;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ModGameRules {

    public static GameRules.Key<GameRules.BooleanValue> INVENTORY_STEAL;
    public static GameRules.Key<GameRules.IntegerValue> LAST_STAND_CHANCE;
    public static GameRules.Key<GameRules.IntegerValue> BLIDNESS_VIEW;
    public static GameRules.Key<GameRules.BooleanValue> AMPUTATION_RESTRICTION;

    public static void registerGamerules(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            INVENTORY_STEAL = GameRules.register(
                    "doInventoryStealing", // gamerule name
                    GameRules.Category.PLAYER,
                    GameRules.BooleanValue.create(false) // default value
            );
            LAST_STAND_CHANCE = GameRules.register(
                    "lastStandChancePercentage",
                    GameRules.Category.PLAYER,
                    GameRules.IntegerValue.create(10)
            );
            BLIDNESS_VIEW = GameRules.register(
                    "blindnessViewDistance",
                    GameRules.Category.PLAYER,
                    GameRules.IntegerValue.create(48)
            );
            AMPUTATION_RESTRICTION = GameRules.register(
                    "restrictAmputationMinigame",
                    GameRules.Category.PLAYER,
                    GameRules.BooleanValue.create(true)
            );
        });
    }
}
