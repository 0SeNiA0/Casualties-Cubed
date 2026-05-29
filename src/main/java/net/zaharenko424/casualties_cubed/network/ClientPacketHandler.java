package net.zaharenko424.casualties_cubed.network;

import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.blocks.medical_mixer.MedicalMixerBlockEntity;
import net.zaharenko424.casualties_cubed.menu.MedicalMixerMenu;
import net.zaharenko424.casualties_cubed.network.packet.ClientboundAmputateRestrictionSyncPacket;
import net.zaharenko424.casualties_cubed.network.packet.ClientboundFluidSyncPacket;
import net.zaharenko424.casualties_cubed.network.packet.ClientboundSyncHealthPacket;
import net.zaharenko424.casualties_cubed.network.packet.ClientboundTriggerLastStandPacket;
import net.zaharenko424.casualties_cubed.registry.ModSounds;
import net.zaharenko424.casualties_cubed.visual.ClientGamerules;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static net.zaharenko424.casualties_cubed.network.ServerPacketHandler.TOO_FAR;

public class ClientPacketHandler {

    public static void handleAmputateRestriction(ClientboundAmputateRestrictionSyncPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> ClientGamerules.AmputateRestriction = packet.value());
        ctx.get().setPacketHandled(true);
    }

    public static void handleLastStand(ClientboundTriggerLastStandPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> Minecraft.getInstance().player.playSound(ModSounds.LAST_STAND.get(),1f,1f));
        ctx.get().setPacketHandled(true);
    }

    public static void handleSyncHealth(ClientboundSyncHealthPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;
            if (player == null || mc.level == null) return;

            Entity entity = player.level().getEntity(packet.targetId());
            if (!(entity instanceof Player target) || player.distanceToSqr(entity) > TOO_FAR) return;

            target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(cap ->
                    cap.deserializeNBT(packet.data()));
        });
        ctx.get().setPacketHandled(true);
    }

    public static void handleFluidSync(ClientboundFluidSyncPacket packet, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            BlockPos pos = packet.pos();

            if (!(minecraft.level.getBlockEntity(pos) instanceof MedicalMixerBlockEntity blockEntity)) return;

            int tankId = packet.tankId();
            FluidStack fluid = packet.fluid();
            blockEntity.setFluidInTank(fluid, tankId);

            if (minecraft.player.containerMenu instanceof MedicalMixerMenu mixerMenu &&
                    mixerMenu.getBlockEntity().getBlockPos().equals(pos)){
                mixerMenu.setFluidInTank(fluid, tankId);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
