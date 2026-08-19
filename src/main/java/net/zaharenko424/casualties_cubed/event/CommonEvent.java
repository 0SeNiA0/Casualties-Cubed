package net.zaharenko424.casualties_cubed.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
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
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.MissingMappingsEvent;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.compat.FoodAndDrinkCompat;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.LimbStatistics;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import net.zaharenko424.casualties_cubed.network.ModNetwork;
import net.zaharenko424.casualties_cubed.network.packet.ClientboundAmputateRestrictionSyncPacket;
import net.zaharenko424.casualties_cubed.network.packet.ClientboundBlindnessViewSyncPacket;
import net.zaharenko424.casualties_cubed.registry.ModBlocks;
import net.zaharenko424.casualties_cubed.registry.ModFluids;
import net.zaharenko424.casualties_cubed.registry.ModGameRules;
import net.zaharenko424.casualties_cubed.registry.ModItems;

import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = CasualtiesCubed.MOD_ID)
public class CommonEvent {

    @SubscribeEvent
    public static void remap(MissingMappingsEvent event) {
        List<MissingMappingsEvent.Mapping<Block>> blocks = event.getMappings(Registries.BLOCK, CasualtiesCubed.MOD_ID);
        for (MissingMappingsEvent.Mapping<Block> mapping : blocks) {
            switch (mapping.getKey().getPath()) {
                case "scav_plushie" -> mapping.remap(ModBlocks.EXPIE_PLUSHY.get());
            }
        }

        List<MissingMappingsEvent.Mapping<Item>> items = event.getMappings(Registries.ITEM, CasualtiesCubed.MOD_ID);
        for (MissingMappingsEvent.Mapping<Item> mapping : items) {
            switch (mapping.getKey().getPath()) {
                case "band_aids" -> mapping.remap(ModItems.ADHESIVE_BANDAGE.get());
                case "alcohol" -> mapping.remap(ModItems.ALCOHOL_BOTTLE.get());
                case "antiserum" -> mapping.remap(ModItems.ANTISERUM_INJECTOR.get());
                case "antibiotics" -> mapping.remap(ModItems.ANTIBIOTICS_PILLS.get());
                case "antiseptic" -> mapping.remap(ModItems.ANTISEPTIC_SPRAY.get());
                case "blood_clotting" -> mapping.remap(ModItems.PROCOAGULANT_INJECTOR.get());
                case "blood_thinner" -> mapping.remap(ModItems.STREPTOKINASE_INJECTOR.get());
                case "brain_grow" -> mapping.remap(ModItems.BRAIN_GROW_PILLS.get());
                case "ceftriaxone" -> mapping.remap(ModItems.CEFTRIAXONE_VIAL.get());
                case "painkillers" -> mapping.remap(ModItems.PAINKILLERS_PILLS.get());
                case "saline", "saline_bag" -> mapping.remap(ModItems.IV_BAG.get());
                case "relief_cream" -> mapping.remap(ModItems.RELIEF_CREAM_BOTTLE.get());
                case "heroin_vial" -> mapping.remap(ModItems.HEROIN_SYRINGE.get());
                case "alganate_dressing" -> mapping.remap(ModItems.ALGINATE_DRESSING.get());
                case "reaction_vial" -> mapping.remap(ModItems.MEDICINE_VIAL.get());
                case "scav_plushie" -> mapping.remap(ModItems.EXPIE_PLUSHY.get());

                case "bottle" -> mapping.remap(ModItems.WATER_BOTTLE.get());
            }
        }

        List<MissingMappingsEvent.Mapping<FluidType>> fluidsTypes = event.getMappings(ForgeRegistries.FLUID_TYPES.get().getRegistryKey(), CasualtiesCubed.MOD_ID);
        for (MissingMappingsEvent.Mapping<FluidType> mapping : fluidsTypes) {
            switch (mapping.getKey().getPath()) {
                case "reaction_liquid" -> mapping.remap(ModFluids.BIO_CHEM_TYPE.get());
            }
        }

        List<MissingMappingsEvent.Mapping<Fluid>> fluids = event.getMappings(Registries.FLUID, CasualtiesCubed.MOD_ID);
        for (MissingMappingsEvent.Mapping<Fluid> mapping : fluids) {
            switch (mapping.getKey().getPath()) {
                case "reaction_liquid" -> mapping.remap(ModFluids.BIO_CHEM.get());
            }
        }
    }

    @SubscribeEvent
    public static void onAttachCap(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).isPresent()) {
                event.addCapability(CasualtiesCubed.resourceLoc("properties"), new PlayerHealthProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) return;

        event.getOriginal().reviveCaps();
        event.getOriginal().getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(oldStore -> {
            event.getEntity().getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(newStore -> {
                newStore.deserializeFromDisk(oldStore.serializeToDisk());
            });
        });
        event.getOriginal().invalidateCaps();
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
    public static void onRegisterCap(RegisterCapabilitiesEvent event) {
        event.register(PlayerHealthData.class);
    }

    @SubscribeEvent
    public static void onChangeGameMode(PlayerEvent.PlayerChangeGameModeEvent event) {
        Player player = event.getEntity();
        if (player.level().isClientSide || event.getNewGameMode() != GameType.SPECTATOR) return;

        PlayerHealthData data = PlayerHealthData.of(event.getEntity()).orElse(null);
        if (data != null) data.clearAttributePenalties(player);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side != LogicalSide.SERVER || event.phase != TickEvent.Phase.START) return;
        if (event.player instanceof ServerPlayer player) {
            if (player.isSpectator()) return;

            ServerLevel level = player.serverLevel();
            ProfilerFiller profiler = level.getProfiler();

            profiler.push("casualties_cubed:player_health_system");
            event.player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                data.update(player);
                boolean usingArm = player.isUsingItem();
                if (usingArm) {
                    InteractionHand hand = player.getUsedItemHand();
                    data.onArmUse(hand, player);
                }
            });
            profiler.pop();
        }
    }

    @SubscribeEvent
    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player)) return;
        ItemStack stack = event.getItem();
        FoodAndDrinkCompat.FoodEntry data = FoodAndDrinkCompat.get(stack.getItem());
        if (data != null)
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h -> {
                h.setTemperature(h.getTemperature() + data.temperature);
                //TODO thirst
                //TODO sickness

            });
    }

    private static int blindnessRangePrev = 48;
    private static boolean amputationRestrictionPrev = true;

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.START && event.level instanceof ServerLevel serverLevel) {
            int blindnessRange = serverLevel.getGameRules().getInt(ModGameRules.BLIDNESS_VIEW);
            boolean amputationRestriction = serverLevel.getGameRules().getBoolean(ModGameRules.AMPUTATION_RESTRICTION);

            if (blindnessRange != blindnessRangePrev) {
                ModNetwork.CHANNEL.send(PacketDistributor.ALL.noArg(), new ClientboundBlindnessViewSyncPacket(blindnessRange));
                blindnessRangePrev = blindnessRange;
            }
            if (amputationRestriction != amputationRestrictionPrev) {
                ModNetwork.CHANNEL.send(PacketDistributor.ALL.noArg(), new ClientboundAmputateRestrictionSyncPacket(amputationRestriction));
                amputationRestrictionPrev = amputationRestriction;
            }
        }
    }

    @SubscribeEvent
    public static void onItemUse(LivingEntityUseItemEvent.Stop event) {
        if (!(event.getEntity() instanceof Player player)) return;

        player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
            Item item = event.getItem().getItem();
            LimbStatistics head = data.getLimb(Limb.HEAD), chest = data.getLimb(Limb.THORAX);

            if (head.getBoneHealTimer() > 0) {
                head.addPain(3);
            }

            if (chest.getDislocationTimer() > 0 || chest.getBoneHealTimer() > 0) {
                chest.addPain(4);
            }

            if (item.isEdible()) {
                if (head.getDislocationTimer() > 0) {
                    head.addDislocationTimer(25);
                }
            }
        });
    }

    @SubscribeEvent
    public static void onAttack(LivingAttackEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
            LimbStatistics head = data.getLimb(Limb.HEAD), chest = data.getLimb(Limb.THORAX);

            if (head.getBoneHealTimer() > 0) head.addPain(3);

            if (chest.getBoneHealTimer() > 0 || chest.getDislocationTimer() > 0) {
                chest.addPain(4);
            }//TODO also add pain to used arm if direct attack?
        });
    }

    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
            LimbStatistics head = data.getLimb(Limb.HEAD), chest = data.getLimb(Limb.THORAX);

            if (head.getBoneHealTimer() > 0) head.addPain(10);

            if (chest.getDislocationTimer() > 0) {
                chest.addPain(10);
            }

            LimbStatistics stats;
            for (Limb limb : Limb.LEG_LIMBS) {
                stats = data.getLimb(limb);
                if (stats.getDislocationTimer() > 0 || stats.getBoneHealTimer() > 0) stats.addPain(10);
            }
        });
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
     *
     * @param level     The world
     * @param player    The player
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
