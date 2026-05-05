package net.adinvas.prototype_pain.network;

import net.adinvas.prototype_pain.PlayerHealthProvider;
import net.adinvas.prototype_pain.PrototypePain;
import net.adinvas.prototype_pain.fluid_system.MedicalFluid;
import net.adinvas.prototype_pain.fluid_system.MultiTankHelper;
import net.adinvas.prototype_pain.item.api.IBag;
import net.adinvas.prototype_pain.item.api.IMedicalMinigameUsable;
import net.adinvas.prototype_pain.item.multi_tank.MultiTankFluidItem;
import net.adinvas.prototype_pain.limbs.Limb;
import net.adinvas.prototype_pain.limbs.PlayerHealthData;
import net.adinvas.prototype_pain.network.packet.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

//TODO add med session? (keep track of players interacting with other players. potential refuse med interaction keybind)
//TODO ensure that sender actually has the item that they say they are using
public class ServerPacketHandler {

    static final float TOO_FAR = 8 * 8;

    public static void handleAdjustShrapnel(ServerboundAdjustShrapnelPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;

            Entity entity = sender.level().getEntity(packet.targetId());
            if (!(entity instanceof Player target) || sender.distanceToSqr(entity) > TOO_FAR) return;

            target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data ->
                    data.setLimbShrapnell(packet.limb(), packet.amount()));
        });
        ctx.get().setPacketHandled(true);
    }

    public static void handleShrapnelFail(ServerboundShrapnelFailPacket packet, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;

            Entity entity = sender.level().getEntity(packet.targetId());
            if (!(entity instanceof Player target) || sender.distanceToSqr(entity) > TOO_FAR) return;

            target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data->{
                Limb limb = packet.limb();
                if (data.hasLimbShrapnell(limb) == 0) return;

                RandomSource random = sender.getRandom();
                data.setLimbPain(limb, data.getLimbPain(limb) + (random.nextFloat() + 1) * 6);
                data.setLimbMuscleHealth(limb,data.getLimbMuscleHealth(limb) - (random.nextFloat() + 0.5f) * 2.5f);
                data.setLimbSkinHealth(limb,data.getLimbSkinHealth(limb) - (random.nextFloat() + 0.5f) * 3.5f);
                data.applyBleedDamage(limb,(random.nextFloat() + 0.5f) / 5,null);
            });
        });
        ctx.get().setPacketHandled(true);
    }

    public static void handleCauterize(ServerboundCauterizeActionPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;

            Entity entity = sender.level().getEntity(packet.targetId());
            if (!(entity instanceof Player target) || sender.distanceToSqr(entity) > TOO_FAR) return;

            target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                RandomSource random = sender.getRandom();
                Limb limb = packet.limb();
                data.setLimbPain(limb,data.getLimbPain(limb) + (random.nextFloat() + 1) * 80);
                data.setLimbBleedRate(limb,data.getLimbBleedRate(limb) * 0.4f);
                data.setLimbMuscleHealth(limb, data.getLimbMuscleHealth(limb) - (random.nextFloat() + 1) * 15);
                data.setLimbSkinHealth(limb, data.getLimbSkinHealth(limb) - (random.nextFloat() + 1) * 25);
            });
        });
        ctx.get().setPacketHandled(true);
    }

    public static void handleDislocationFix(ServerboundDislocationTryPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;

            Entity entity = sender.level().getEntity(packet.targetId());
            if (!(entity instanceof Player target) || sender.distanceToSqr(entity) > TOO_FAR) return;

            target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                Limb limb = packet.limb();
                data.setLimbDislocation(limb, packet.dislocationValue());
                data.setLimbPain(limb, data.getLimbPain(limb) + (sender.getRandom().nextFloat() * 20) + 20);
            });
        });
        ctx.get().setPacketHandled(true);
    }

    ///FIXME {@link ServerboundUseSyringePacket}
    public static void handleUseSyringe(ServerboundUseSyringePacket packet, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;

            Entity entity = sender.level().getEntity(packet.targetId());
            if (!(entity instanceof ServerPlayer target) || sender.distanceToSqr(entity) > TOO_FAR) return;

            for (int i=0;i<packet.ids().length;i++){
                MedicalFluid fluid = MedicalFluid.getFromId(packet.ids()[i]);
                float amount = packet.amounts()[i];
                if (fluid!=null&& !Float.isNaN(amount)){
                    fluid.getMedicalEffect().applyInjected(target, amount, packet.limb());
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static void handleSyringeFail(ServerboundSyringeFailPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;

            Entity entity = sender.level().getEntity(packet.targetId());
            if (!(entity instanceof Player target) || sender.distanceToSqr(entity) > TOO_FAR) return;

            target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                RandomSource random = sender.getRandom();
                Limb limb = packet.limb();
                data.setLimbPain(limb, data.getLimbPain(limb) + ((random.nextFloat() + 0.5f) * 20));
                data.setLimbShrapnell(limb,data.hasLimbShrapnell(limb) + 1);
            });
        });
        ctx.get().setPacketHandled(true);
    }

    public static void handleUseBandage(ServerboundUseBandagePacket packet, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(()->{
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;

            Entity entity = sender.level().getEntity(packet.targetId());
            if (!(entity instanceof Player target) || sender.distanceToSqr(entity) > TOO_FAR) return;

            ItemStack stack = packet.bandage();
            if (stack.getItem() instanceof IMedicalMinigameUsable medicalMinigameUsable) {
                medicalMinigameUsable.useMinigameAction(packet.durability(), target, packet.limb());
            }
        });
    }

    public static void handleUseMedItem(ServerboundUseMedItemPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;

            Entity entity = sender.level().getEntity(packet.targetId());
            if (!(entity instanceof ServerPlayer target) || sender.distanceToSqr(entity) > TOO_FAR) return;

            target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                InteractionHand hand = packet.offhand() ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
                ItemStack used = data.tryUseItem(packet.limb(), sender.getItemInHand(hand), sender, target);
                sender.setItemInHand(hand, used);
            });
        });
        ctx.get().setPacketHandled(true);
    }

    public static void handleUseBagMedItem(ServerboundUseBagMedItemPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;

            Entity entity = sender.level().getEntity(packet.targetId());
            if (!(entity instanceof ServerPlayer target) || sender.distanceToSqr(entity) > TOO_FAR) return;

            // Verify the bag is in the player’s hands before allowing this

            target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(health -> {
                // We don’t have a specific hand (it’s inside a bag),
                // but we can still treat it as “internal use”:
                ItemStack itemstack = packet.item().copy();

                // Perform the same logic as tryUseItem, but no hand reference
                ItemStack used = health.tryUseItem(packet.limb(), itemstack, sender, target);
                if (packet.bag().getItem() instanceof IBag iBag) {
                    List<ItemStack> items = iBag.getItems(packet.bag());

                    // Find the used item and remove/damage it
                    items.set(packet.slot(), used);

                    // Save the updated inventory back to the bag
                    iBag.setItems(packet.bag(), items);

                    // Update the bag in the player hand
                    sender.setItemInHand(packet.offhand() ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND, packet.bag());
                }
            });
        });
        ctx.get().setPacketHandled(true);
    }

    public static void handleMedicalAction(ServerboundMedicalActionPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;

            Entity entity = sender.level().getEntity(packet.targetId());
            if (!(entity instanceof Player target) || sender.distanceToSqr(entity) > TOO_FAR) return;

            target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h ->
                    h.medicalAction(packet.action(), packet.limb(), sender));
        });
        ctx.get().setPacketHandled(true);
    }

    public static void handleCPR(ServerboundCPRPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;

            Entity entity = sender.level().getEntity(packet.targetId());
            if (!(entity instanceof Player target) || sender.distanceToSqr(entity) > TOO_FAR) return;

            target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                RandomSource random = sender.getRandom();

                switch (packet.success()) {
                    case LOW -> {
                        if (data.getLimbMuscleHealth(Limb.CHEST) <= 5)
                            data.setOxygen(Math.max(data.getOxygen(), ((random.nextFloat()) / 2 + 0.5f) * 3));
                        else
                            data.setOxygen(Math.max(data.getOxygen(), ((random.nextFloat()) / 2 + 0.5f) * 6));

                        data.setLimbPain(Limb.CHEST, data.getLimbPain(Limb.CHEST) + (random.nextFloat() + 0.5f) * 30);
                        if (random.nextInt(8) == 0) {
                            data.setLimbFracture(Limb.CHEST, data.getLimbFracture(Limb.CHEST) + 10);
                        }
                        if (random.nextInt(2) == 0) {
                            data.setLimbMuscleHealth(Limb.CHEST, data.getLimbMuscleHealth(Limb.CHEST) - (random.nextFloat() + 0.5f) * 8);
                        }
                    }
                    case MEDIUM -> {
                        if (data.getLimbMuscleHealth(Limb.CHEST) <= 5)
                            data.setOxygen(Math.max(data.getOxygen(), ((random.nextFloat()) / 2 + 0.5f) * 4));
                        else
                            data.setOxygen(Math.max(data.getOxygen(), ((random.nextFloat()) / 2 + 0.5f) * 8));
                        data.setLimbPain(Limb.CHEST, data.getLimbPain(Limb.CHEST) + (random.nextFloat() + 0.5f) * 20);
                        if (random.nextInt(6) == 0) {
                            data.setLimbFracture(Limb.CHEST, data.getLimbFracture(Limb.CHEST) + 10);
                        }
                        if (random.nextInt(4) == 0) {
                            data.setLimbMuscleHealth(Limb.CHEST, data.getLimbMuscleHealth(Limb.CHEST) - (random.nextFloat() + 0.5f) * 5);
                        }
                    }
                    case HIGH -> {
                        if (data.getLimbMuscleHealth(Limb.CHEST) <= 5)
                            data.setOxygen(Math.max(data.getOxygen(), ((random.nextFloat()) / 2 + 0.5f) * 6));
                        else
                            data.setOxygen(Math.max(data.getOxygen(), ((random.nextFloat()) / 2 + 0.5f) * 12));
                        data.setLimbPain(Limb.CHEST, data.getLimbPain(Limb.CHEST) + (random.nextFloat() + 0.5f) * 10);
                        if (random.nextInt(4) == 0) {
                            data.setLimbFracture(Limb.CHEST, data.getLimbFracture(Limb.CHEST) + 10);
                        }
                        if (random.nextInt(8) == 0) {
                            data.setLimbMuscleHealth(Limb.CHEST, data.getLimbMuscleHealth(Limb.CHEST) - (random.nextFloat() + 0.5f) * 1);
                        }
                    }
                }
                data.setContiousness(data.getContiousness() - 5);
            });
        });
        ctx.get().setPacketHandled(true);
    }

    public static void handleGiveUp(ServerboundGiveUpPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;

            sender.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA)
                    .ifPresent(data -> data.killPlayer(sender, true));
        });
        ctx.get().setPacketHandled(true);
    }

//TODO use as mentioned above session tracker?
    public static void handleGuiSyncToggle(ServerboundGuiSyncTogglePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;

            Entity entity = sender.level().getEntity(packet.targetId());
            if (!(entity instanceof Player target) || sender.distanceToSqr(entity) > TOO_FAR) return;

            if (packet.enable()) {
                SyncTracker.add(sender.getUUID(), target.getUUID());
            } else {
                SyncTracker.remove(sender);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static void handleTalk(ServerboundTalkPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                if (data.getLimbDislocated(Limb.HEAD) > 0) {
                    data.setLimbPain(Limb.HEAD, data.getLimbPain(Limb.HEAD) + 0.3f);
                }
            });
        });
        ctx.get().setPacketHandled(true);
    }

    public static void handleLegUse(ServerboundLegUsePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA)
                    .ifPresent(PlayerHealthData::onLegUse);
        });
        ctx.get().setPacketHandled(true);
    }

//TODO fix fluid transfer dupe
    public static void handleFluidTransfer(ServerboundFluidTransferPacket packet, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(()->{
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;

            ItemStack src = packet.source();
            ItemStack trg = packet.target();
            if (src.getItem() instanceof MultiTankFluidItem from && trg.getItem() instanceof MultiTankFluidItem to){
                List<FluidStack> drained = MultiTankHelper.drain(src, packet.amount());
                for (FluidStack stack : drained){
                    PrototypePain.LOGGER.info("fluid {}, amount {}",stack.getFluid(),stack.getAmount());
                    MultiTankHelper.addFluid(trg,stack.getAmount(),stack);
                }

                sender.containerMenu.setCarried(ItemStack.EMPTY);
                sender.containerMenu.broadcastChanges();
                sender.containerMenu.setCarried(src);
                sender.getInventory().setItem(packet.helperSlot(), trg);
                sender.containerMenu.broadcastChanges();
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static void handleExchangeItemInBag(ServerboundExchangeItemInBagPacket packet, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;

            ItemStack trg = packet.target();
            if (packet.bag().getItem() instanceof IBag iBag) {
                List<ItemStack> items = iBag.getItems(packet.bag());

                // Find the used item and remove/damage it
                items.set(packet.slot(),trg);

                // Save the updated inventory back to the bag
                iBag.setItems(packet.bag(), items);

                // Update the bag in the player hand
                sender.setItemInHand(packet.offhand() ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND, packet.bag());
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static void handleExchangeItemInHand(ExchangeItemInHandPacket packet, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;

            ItemStack trg = packet.target();
            if (trg != null) {
                if (packet.offhand()) {
                    sender.setItemInHand(InteractionHand.OFF_HAND, trg);
                }else {
                    sender.setItemInHand(InteractionHand.MAIN_HAND, trg);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
