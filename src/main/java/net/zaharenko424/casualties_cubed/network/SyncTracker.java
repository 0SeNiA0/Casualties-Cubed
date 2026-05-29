package net.zaharenko424.casualties_cubed.network;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import net.zaharenko424.casualties_cubed.network.packet.ClientboundSyncHealthPacket;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@ParametersAreNonnullByDefault
@Mod.EventBusSubscriber
public class SyncTracker {

    private static final Map<ServerPlayer, ServerPlayer> viewersToHolders = new HashMap<>();
    private static final Multimap<ServerPlayer, ServerPlayer> holdersToViewers = HashMultimap.create();
    private static final Multimap<ServerPlayer, ServerPlayer> pendingFullSync = HashMultimap.create();

    public static void add(ServerPlayer holder, ServerPlayer viewer) {
        ServerPlayer prevHolder = viewersToHolders.put(viewer, holder);
        if (prevHolder == holder) return;

        if (prevHolder != null) holdersToViewers.remove(prevHolder, viewer);

        holdersToViewers.put(holder, viewer);
        if (holder != viewer) pendingFullSync.put(holder, viewer);
    }

    public static void remove(ServerPlayer player) {
        if (holdersToViewers.containsKey(player)) removeHolder(player);
        if (viewersToHolders.containsKey(player)) removeViewer(player);
    }

    public static void removeHolder(ServerPlayer holder) {
        Collection<ServerPlayer> removed = holdersToViewers.removeAll(holder);
        if (removed.isEmpty()) return;

        for (ServerPlayer viewer : removed) {
            viewersToHolders.remove(viewer);
            pendingFullSync.remove(holder, viewer);
        }
    }

    public static void removeViewer(ServerPlayer viewer) {
        ServerPlayer holder = viewersToHolders.remove(viewer);
        if (holder == null) return;

        holdersToViewers.remove(holder, viewer);
        pendingFullSync.remove(holder, viewer);
    }

    public static boolean isViewing(ServerPlayer viewer, ServerPlayer holder) {
        return holder == viewersToHolders.get(viewer);
    }

    private static void sync(MinecraftServer server) {
        PlayerHealthData data;
        Packet<?> partial, full;
        CompoundTag tag;
        for (ServerPlayer holder : server.getPlayerList().getPlayers()) {
            data = PlayerHealthData.nonNullOf(holder);

            partial = ModNetwork.CHANNEL.toVanillaPacket(new ClientboundSyncHealthPacket(holder.getId(), data.serializeNBT(new CompoundTag(), false)), NetworkDirection.PLAY_TO_CLIENT);
            holder.connection.send(partial);

            tag = data.serializeReducedNbt( false);
            if (tag != null) ModNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> holder), new ClientboundSyncHealthPacket(holder.getId(), tag));//TODO AT/mixin to access tracking and only send to non viewers?

            full = null;
            for (ServerPlayer viewer : SyncTracker.holdersToViewers.get(holder)) {
                if (viewer == holder) continue; //self sync handled already

                if (pendingFullSync.remove(holder, viewer)) {
                    if (full == null) full = ModNetwork.CHANNEL.toVanillaPacket(new ClientboundSyncHealthPacket(holder.getId(), data.serializeNBT(new CompoundTag(), true)), NetworkDirection.PLAY_TO_CLIENT);
                    viewer.connection.send(full);
                    continue;
                }

                viewer.connection.send(partial);
            }
        }
    }

    @SubscribeEvent
    public static void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        PlayerHealthData data = PlayerHealthData.nonNullOf(player);
        ModNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new ClientboundSyncHealthPacket(player.getId(), data.serializeReducedNbt(true)));
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (!(event.getTarget() instanceof ServerPlayer player)) return;

        PlayerHealthData data = PlayerHealthData.nonNullOf(player);
        ModNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()), new ClientboundSyncHealthPacket(player.getId(), data.serializeReducedNbt(true)));
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        ProfilerFiller profiler = server.getProfiler();
        profiler.push(CasualtiesCubed.MOD_ID + ":sync_tracker");
        sync(server);
        profiler.pop();
    }

    @SubscribeEvent
    public static void onLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        remove(player);
    }
}