package net.adinvas.casualties_cubed.event;

import net.adinvas.casualties_cubed.PlayerHealthProvider;
import net.adinvas.casualties_cubed.CasualtiesCubed;
import net.adinvas.casualties_cubed.compat.FoodAndDrinkCompat;
import net.adinvas.casualties_cubed.limbs.Limb;
import net.adinvas.casualties_cubed.limbs.PlayerHealthData;
import net.adinvas.casualties_cubed.network.ModNetwork;
import net.adinvas.casualties_cubed.network.SyncTracker;
import net.adinvas.casualties_cubed.network.packet.ClientboundAmputateRestrictionSyncPacket;
import net.adinvas.casualties_cubed.network.packet.ClientboundBlindnessViewSyncPacket;
import net.adinvas.casualties_cubed.registry.ModGameRules;
import net.adinvas.casualties_cubed.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = CasualtiesCubed.MOD_ID)
public class CommonEvent {

    @SubscribeEvent
    public static void onAttachCap(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).isPresent()){
                event.addCapability(CasualtiesCubed.resourceLoc("properties"),new PlayerHealthProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event){
        if (event.isWasDeath()){
            event.getOriginal().getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(oldStore ->{
                event.getOriginal().getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(newStore ->{
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }

    @SubscribeEvent
    public static void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            int val = player.serverLevel()
                    .getGameRules()
                    .getInt(ModGameRules.BLIDNESS_VIEW);

            boolean valb = player.serverLevel()
                    .getGameRules()
                    .getBoolean(ModGameRules.AMPUTATION_RESTRICTION);


            ModNetwork.CHANNEL.send(
                    PacketDistributor.PLAYER.with(() -> player),
                    new ClientboundBlindnessViewSyncPacket(val)
            );
            ModNetwork.CHANNEL.send(
                    PacketDistributor.PLAYER.with(() -> player),
                    new ClientboundAmputateRestrictionSyncPacket(valb)
            );
        }
    }

    @SubscribeEvent
    public static void onRegisterCap(RegisterCapabilitiesEvent event){
        event.register(PlayerHealthData.class);
    }



    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event){
        if(event.side == LogicalSide.SERVER){
            if (event.phase!= TickEvent.Phase.START)return;
            if (event.player instanceof ServerPlayer player) {
                if (player.gameMode.isCreative())return;
                ServerLevel level = player.serverLevel();
                ProfilerFiller profiler = level.getProfiler();

                profiler.push("casualties_cubed:player_health_system");
                event.player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(playerHealthData -> {
                    playerHealthData.tickUpdate(player);
                    boolean usingArm = player.isUsingItem();
                    if (usingArm){
                        InteractionHand hand = player.getUsedItemHand();
                        playerHealthData.onArmUse(hand,player);
                    }
                });
                profiler.pop();
            }
        }
        /*
            if (event.player instanceof Player) {
                if (Keybinds.OPEN_PAIN_GUI.isDown()) {
                    Keybinds.OPEN_PAIN_GUI.consumeClick();

                    Player target = getLookedAtPlayer(event.player, 2);
                    boolean self = target==null||event.player.isShiftKeyDown();
                    if (event.side == LogicalSide.CLIENT) {
                        if (self){
                            Minecraft.getInstance().setScreen(new HealthScreen(event.player));
                        }else {
                            Minecraft.getInstance().setScreen(new HealthScreen(target));
                        }
                    }
                }
            }
            //Doesnt work IG.
         */

    }

    @SubscribeEvent
    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player)) return;
        ItemStack stack = event.getItem();
        FoodAndDrinkCompat.FoodEntry data = FoodAndDrinkCompat.get(stack.getItem());
        if (data != null)
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h->{
                h.setTemperature(h.getTemperature()+ data.temperature);
                //TODO thirst
                //TODO sickness

            });
    }


    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        ProfilerFiller profiler = server.getProfiler();
        profiler.push("casualties_cubed:sync_tracker");
        if (event.phase == TickEvent.Phase.END) {
            SyncTracker.tick(server);
            SyncTracker.tickEveryone(server);
            SyncTracker.tickEveryoneReducedBroadcast(server);
        }
        profiler.pop();
    }

    private static int blindnessRangePrev = 48;
    private static boolean amputationRestrictionPrev = true;

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.START && event.level instanceof ServerLevel serverLevel) {
            int blindnessRange = serverLevel.getGameRules().getInt(ModGameRules.BLIDNESS_VIEW);
            boolean amputationRestriction = serverLevel.getGameRules().getBoolean(ModGameRules.AMPUTATION_RESTRICTION);

            if (blindnessRange!=blindnessRangePrev){
                ModNetwork.CHANNEL.send(PacketDistributor.ALL.noArg(),new ClientboundBlindnessViewSyncPacket(blindnessRange));
                blindnessRangePrev = blindnessRange;
            }
            if (amputationRestriction!=amputationRestrictionPrev){
                ModNetwork.CHANNEL.send(PacketDistributor.ALL.noArg(),new ClientboundAmputateRestrictionSyncPacket(amputationRestriction));
                amputationRestrictionPrev = amputationRestriction;
            }
        }
    }

    @SubscribeEvent
    public static void onItemUse(LivingEntityUseItemEvent.Stop event){
        if (event.getEntity() instanceof Player player) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h->{
                Item item = event.getItem().getItem();
                if (h.getLimbFracture(Limb.HEAD)>0){
                    h.setLimbPain(Limb.HEAD, h.getLimbPain(Limb.HEAD)+3);
                }
                if (h.getLimbDislocated(Limb.CHEST)>0||h.getLimbFracture(Limb.CHEST)>0){
                    h.setLimbPain(Limb.CHEST, h.getLimbPain(Limb.CHEST)+4);
                }
                if (item.isEdible()){
                    if (h.getLimbDislocated(Limb.HEAD)>0){
                        h.setLimbPain(Limb.HEAD, h.getLimbPain(Limb.HEAD)+25);
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public static void onAttack(LivingAttackEvent event){
        if (event.getEntity() instanceof Player player) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h->{
                if (h.getLimbFracture(Limb.HEAD)>0){
                    h.setLimbPain(Limb.HEAD, h.getLimbPain(Limb.HEAD)+3);
                }
                if (h.getLimbDislocated(Limb.CHEST)>0||h.getLimbFracture(Limb.CHEST)>0){
                    h.setLimbPain(Limb.CHEST, h.getLimbPain(Limb.CHEST)+4);
                }
            });
        }
    }

    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event){
        if (event.getEntity() instanceof Player player) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h->{
                if (h.getLimbFracture(Limb.HEAD)>0){
                    h.setLimbPain(Limb.HEAD, h.getLimbPain(Limb.HEAD)+10);
                }
                if (h.getLimbDislocated(Limb.CHEST)>0){
                    h.setLimbPain(Limb.CHEST, h.getLimbPain(Limb.HEAD)+10);
                }
                List<Limb> templist= new ArrayList<>();
                templist.add(Limb.LEFT_LEG);
                templist.add(Limb.RIGHT_LEG);
                templist.add(Limb.LEFT_FOOT);
                templist.add(Limb.RIGHT_FOOT);
                for (Limb limb :templist){
                    if (h.getLimbDislocated(limb)>0||h.getLimbFracture(limb)>0){
                        h.setLimbPain(limb, h.getLimbPain(limb)+10);
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public static void onJoin(EntityJoinLevelEvent event){
        if(event.getEntity() instanceof ServerPlayer player){
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h->{
                h.isReducedDirty = true;
            });
            SyncTracker.onJoin(player,ServerLifecycleHooks.getCurrentServer());
        }
    }

    public static Player getLookedAtPlayer(Player viewer, double maxDistance) {
        Vec3 eyePos = viewer.getEyePosition(1.0F);
        Vec3 lookVec = viewer.getLookAngle();
        Vec3 reachVec = eyePos.add(lookVec.scale(maxDistance));

        AABB searchBox = viewer.getBoundingBox()
                .expandTowards(lookVec.scale(maxDistance))
                .inflate(1.0D); // widen a bit so it's easier to hit

        // find closest player along the ray
        Player nearest = null;
        double nearestDist = maxDistance;

        for (Player target : viewer.level().getEntitiesOfClass(Player.class, searchBox)) {
            if (target == viewer) continue; // skip self

            AABB hitBox = target.getBoundingBox().inflate(0.3); // tolerance
            Optional<Vec3> hit = hitBox.clip(eyePos, reachVec);

            if (hit.isPresent()) {
                double dist = eyePos.distanceTo(hit.get());
                if (dist < nearestDist) {
                    nearestDist = dist;
                    nearest = target;
                }
            }
        }

        return nearest;
    }

    private static final float HEARING_DISTANCE = 20;

    @SubscribeEvent
    public static void onExplosionDetonate(ExplosionEvent.Detonate event) {
        Level level = event.getLevel();
        if (level.isClientSide()) return;// 1. This logic must run on the server

        Vec3 explosionPos = event.getExplosion().getPosition();

        // 2. Create a bounding box 32 blocks in every direction from the explosion
        // This is a fast, efficient first-pass check.
        AABB checkBounds = new AABB(new BlockPos((int) explosionPos.x, (int) explosionPos.y, (int) explosionPos.z)).inflate(HEARING_DISTANCE);

        // 3. Get all players within that box
        double dist;
        float distScale;
        for (Player player : level.getEntitiesOfClass(Player.class, checkBounds)) {

            // 4. Check the precise spherical distance
            dist = player.position().distanceTo(explosionPos);
            if (dist > HEARING_DISTANCE) {
                continue; // Player was in the corner of the AABB but > 32 blocks away
            }

            distScale = (float) Math.pow(1 - dist / HEARING_DISTANCE, 2f);
            if (!hasLineOfSight(level, player, explosionPos)) {
                distScale /= 2;
            } else distScale += .1f;

            ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
            if (!helmet.isEmpty()) {
                if (helmet.is(ModItems.SimpleEarProtection.get())) {
                    distScale *= .75f;
                } else if (helmet.is(Tags.Items.ARMORS_HELMETS) && helmet.getItem() instanceof ArmorItem armor
                        && armor.getDefense() > 1) distScale *= .75f;
            }

            float finalDistanceScale = Mth.clamp(distScale, 0, 1);

            if (distScale > 0.1) {
                player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                    data.setConsciousness(data.getConsciousness() - (100 * finalDistanceScale));
                    data.setHearingLoss((float) (data.getHearingLoss() + Math.max(0.05, finalDistanceScale / 2f)));
                    data.setFlashHearingLoss(data.getFlashHearingLoss() + Math.min(0.25f, finalDistanceScale * 4));
                });
            }
        }
    }

    /**
     * Checks if a player has a direct line of sight to a target position.
     * @param level The world
     * @param player The player
     * @param targetPos The position of the explosion
     * @return true if there is a clear line of sight, false otherwise
     */
    private static boolean hasLineOfSight(Level level, Player player, Vec3 targetPos) {
        // Start the raycast from the player's eyes
        Vec3 eyePos = player.getEyePosition();

        ClipContext clipContext = new ClipContext(
                eyePos,                 // Start of the ray
                targetPos,              // End of the ray
                ClipContext.Block.COLLIDER, // Checks against blocks with collision (e.g., stone, wood)
                ClipContext.Fluid.NONE,   // Ignores fluids
                player                  // The entity to ignore (the player themselves)
        );

        // If the raycast 'missed', it means it didn't hit a block.
        // Therefore, the player has a clear line of sight.
        return level.clip(clipContext).getType() == HitResult.Type.MISS;
    }
}
