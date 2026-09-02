package net.zaharenko424.casualties_cubed.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;
import net.zaharenko424.casualties_cubed.CasualtiesCubedTags;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.fluid_system.MedicalFluidType;
import net.zaharenko424.casualties_cubed.fluid_system.MultiFluidTankHandler;
import net.zaharenko424.casualties_cubed.fluid_system.MultiTankHelper;
import net.zaharenko424.casualties_cubed.item.api.AbstractBandage;
import net.zaharenko424.casualties_cubed.item.api.IBag;
import net.zaharenko424.casualties_cubed.item.api.ISimpleMedicalUsable;
import net.zaharenko424.casualties_cubed.item.multi_tank.MultiTankFluidItem;
import net.zaharenko424.casualties_cubed.item.multi_tank.SyringeItem;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.LimbStatistics;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import net.zaharenko424.casualties_cubed.network.packet.*;

import java.util.List;
import java.util.function.Supplier;

//TODO add med session? (keep track of players interacting with other players. potential refuse med interaction keybind)
//TODO ensure that sender actually has the item that they say they are using
public class ServerPacketHandler {

    public static final float TOO_FAR = 3 * 3;

    public static void handleRagdoll(ServerboundRagdollPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;

            PlayerHealthData.of(sender).ifPresent(data -> data.forceRagdoll(sender, packet.ragdoll()));
        });
        ctx.get().setPacketHandled(true);
    }

    public static void handleSleep(ServerboundSleepPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;

            PlayerHealthData.of(sender).ifPresent(data -> data.sleep(sender));
        });
        ctx.get().setPacketHandled(true);
    }

    public static void handleAdjustShrapnel(ServerboundAdjustShrapnelPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;

            Entity entity = sender.level().getEntity(packet.targetId());
            if (!(entity instanceof Player target) || sender.distanceToSqr(entity) > TOO_FAR) return;

            target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(targetData ->
                    targetData.getLimb(packet.limb()).setShrapnel(packet.amount()));
        });
        ctx.get().setPacketHandled(true);
    }

    public static void handleShrapnelFail(ServerboundShrapnelFailPacket packet, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;

            Entity entity = sender.level().getEntity(packet.targetId());
            if (!(entity instanceof Player target) || sender.distanceToSqr(entity) > TOO_FAR) return;

            target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(targetData -> {
                LimbStatistics stats = targetData.getLimb(packet.limb());
                RandomSource random = sender.getRandom();

                stats.addPain((random.nextFloat() + 1) * 6);
                stats.addMuscleHealth(- (random.nextFloat() + 0.5f) * 2.5f);
                stats.addSkinHealth(- (random.nextFloat() + 0.5f) * 3.5f);

                targetData.applyBleedDamage(packet.limb(),(random.nextFloat() + 0.5f) / 5,null);
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

            PlayerHealthData data = sender.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).orElse(null);
            if (data.isAmputated(Limb.UPPER_RIGHT_ARM) && data.isAmputated(Limb.UPPER_LEFT_ARM)) return;// Cant interact without arms

            target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(targetData -> {
                LimbStatistics stats = targetData.getLimb(packet.limb());
                if (stats.getDislocationTimer() == 0) return;

                stats.setDislocationTimer(packet.dislocationValue());
                stats.addPain((sender.getRandom().nextFloat() * 20) + 20);
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

            PlayerHealthData data = sender.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).orElse(null);
            if (data.isAmputated(Limb.getFromHand(packet.usedHand(), sender))) return;// Cant use amputated limb

            PlayerHealthData targetData = sender == target ? data : target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).orElse(null);
            if (targetData.isAmputated(packet.limb())) return;// Cant treat missing limb

            ItemStack stack = sender.getItemInHand(packet.usedHand());
            Item item = stack.getItem();
            if (item instanceof IBag bag) {
                int bagSlot = packet.bagSlot();
                if (bagSlot == -1 || bag.size() <= bagSlot) return;// Bag was not expected / not usable OR too small

                stack = bag.getItem(stack, bagSlot);
            }

            if (!(stack.getItem() instanceof MultiTankFluidItem fluidItem)) return;// Not syringe?

            MultiFluidTankHandler handler = fluidItem.getHandler(stack);

            List<FluidStack> fluids = handler.getTank().getFluids();
            int count = Math.min(packet.amounts().length, fluids.size());
            for (int i = 0; i < count; i++) {
                if (Float.isNaN(packet.amounts()[i])) continue;

                MedicalFluidType.inject(target, packet.amounts()[i], packet.limb(), fluids.get(i).getFluid());
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

            PlayerHealthData data = sender.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).orElse(null);
            if (data.isAmputated(Limb.getFromHand(packet.usedHand(), sender))) return;// Cant use syringe without a hand

            if (!(sender.getItemInHand(packet.usedHand()).getItem() instanceof SyringeItem)) return;

            target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(targetData -> {
                RandomSource random = sender.getRandom();
                LimbStatistics stats = targetData.getLimb(packet.limb());

                stats.addPain(((random.nextFloat() + 0.5f) * 20));
                stats.addShrapnel(1);
            });
        });
        ctx.get().setPacketHandled(true);
    }

    public static void handleUseBandage(ServerboundUseBandagePacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender == null) return;

            Entity entity = sender.level().getEntity(packet.targetId());
            if (!(entity instanceof ServerPlayer target) || sender.distanceToSqr(entity) > ServerPacketHandler.TOO_FAR) return;

            PlayerHealthData data = sender.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).orElse(null);
            if (data.isAmputated(Limb.getFromHand(packet.usedHand(), sender))) return;// Cant use amputated limb

            PlayerHealthData targetData = sender == target ? data : target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).orElse(null);
            if (targetData.isAmputated(packet.limb())) return;// Cant treat missing limb

            ItemStack stack = sender.getItemInHand(packet.usedHand());
            Item item = stack.getItem();
            if (item instanceof IBag bag) {
                int bagSlot = packet.bagSlot();
                if (bagSlot == -1 || bag.size() <= bagSlot) return;// Bag was not expected / not usable OR too small

                ItemStack stackInBag = bag.getItem(stack, bagSlot);
                item = stackInBag.getItem();

                if (!(item instanceof AbstractBandage bandage)) return;

                bandage.use(sender, target, packet.limb(), stackInBag);
                bag.setItem(stack, bagSlot, stackInBag);
                return;
            }

            if (item instanceof AbstractBandage bandage) {
                bandage.use(sender, target, packet.limb(), stack);
            }
        });
        context.setPacketHandled(true);
    }

    public static void handleUseMedItem(ServerboundUseMedItemPacket packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender == null) return;

            Entity entity = sender.level().getEntity(packet.targetId());
            if (!(entity instanceof ServerPlayer target) || sender.distanceToSqr(entity) > ServerPacketHandler.TOO_FAR) return;

            PlayerHealthData data = sender.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).orElse(null);
            if (data.isAmputated(Limb.getFromHand(packet.usedHand(), sender))) return;// Cant use amputated limb

            PlayerHealthData targetData = sender == target ? data : target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).orElse(null);
            if (targetData.isAmputated(packet.limb())) return;// Cant treat missing limb

            ItemStack stack = sender.getItemInHand(packet.usedHand());
            Item item = stack.getItem();
            if (item instanceof IBag bag) {
                int bagSlot = packet.bagSlot();
                if (bagSlot == -1 || bag.size() <= bagSlot) return;// Bag was not expected / not usable OR too small

                ItemStack stackInBag = bag.getItem(stack, bagSlot);
                item = stackInBag.getItem();

                if (stack.is(CasualtiesCubedTags.Item.CAUTERIZE)) {
                    cauterize(sender, target, packet.limb());
                    return;
                }

                if (!(item instanceof ISimpleMedicalUsable usable)) return;

                usable.onMedicalUse(sender, target, packet.limb(), stackInBag);
                bag.setItem(stack, bagSlot, stackInBag);
                return;
            }

            if (stack.is(CasualtiesCubedTags.Item.CAUTERIZE)) {
                cauterize(sender, target, packet.limb());
                return;
            }

            if (item instanceof ISimpleMedicalUsable usable) {
                usable.onMedicalUse(sender, target, packet.limb(), stack);
            }
        });
        context.setPacketHandled(true);
    }

    private static void cauterize(ServerPlayer sender, ServerPlayer target, Limb limb) {
        target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(targetData -> {
            LimbStatistics stats = targetData.getLimb(limb);
            RandomSource random = sender.getRandom();

            stats.addPain((random.nextFloat() + 1) * 80);
            stats.setBleedRate(stats.getBleedRate() * 0.4f);
            stats.addMuscleHealth(- (random.nextFloat() + 1) * 15);
            stats.addSkinHealth(- (random.nextFloat() + 1) * 25);
            stats.addBurn(5 + random.nextFloat() * 5);
        });
    }

    public static void handleMedicalAction(ServerboundMedicalActionPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;

            Entity entity = sender.level().getEntity(packet.targetId());
            if (!(entity instanceof Player target) || sender.distanceToSqr(entity) > TOO_FAR) return;

            PlayerHealthData data = sender.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).orElse(null);
            if (data.isAmputated(Limb.UPPER_RIGHT_ARM) && data.isAmputated(Limb.UPPER_LEFT_ARM)) return;// Cant interact without arms

            target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(targetData ->
                    targetData.medicalAction(packet.action(), packet.limb(), sender));
        });
        ctx.get().setPacketHandled(true);
    }

    public static void handleCPR(ServerboundCPRPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;

            Entity entity = sender.level().getEntity(packet.targetId());
            if (!(entity instanceof Player target) || sender.distanceToSqr(entity) > TOO_FAR) return;

            PlayerHealthData data = sender.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).orElse(null);
            if (data.isAmputated(Limb.UPPER_RIGHT_ARM) && data.isAmputated(Limb.UPPER_LEFT_ARM)) return;// Cant interact without arms

            target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(targetData -> {
                RandomSource random = sender.getRandom();
                LimbStatistics chest = targetData.getLimb(Limb.THORAX);

                switch (packet.success()) {
                    case LOW -> {
                        if (chest.getMuscleHealth() <= 5)
                            targetData.bloodOxygen(Math.max(targetData.bloodOxygen(), ((random.nextFloat()) / 2 + 0.5f) * 3));
                        else
                            targetData.bloodOxygen(Math.max(targetData.bloodOxygen(), ((random.nextFloat()) / 2 + 0.5f) * 6));

                        chest.addPain((random.nextFloat() + 0.5f) * 30);
                        if (random.nextInt(8) == 0) {
                            chest.addBoneHealTimer(10);
                        }
                        if (random.nextInt(2) == 0) {
                            chest.addMuscleHealth(- (random.nextFloat() + 0.5f) * 8);
                        }
                    }
                    case MEDIUM -> {
                        if (chest.getMuscleHealth() <= 5)
                            targetData.bloodOxygen(Math.max(targetData.bloodOxygen(), ((random.nextFloat()) / 2 + 0.5f) * 4));
                        else
                            targetData.bloodOxygen(Math.max(targetData.bloodOxygen(), ((random.nextFloat()) / 2 + 0.5f) * 8));

                        chest.addPain((random.nextFloat() + 0.5f) * 20);
                        if (random.nextInt(6) == 0) {
                            chest.addBoneHealTimer(10);
                        }
                        if (random.nextInt(4) == 0) {
                            chest.addMuscleHealth(- (random.nextFloat() + 0.5f) * 5);
                        }
                    }
                    case HIGH -> {
                        if (chest.getMuscleHealth() <= 5)
                            targetData.bloodOxygen(Math.max(targetData.bloodOxygen(), ((random.nextFloat()) / 2 + 0.5f) * 6));
                        else
                            targetData.bloodOxygen(Math.max(targetData.bloodOxygen(), ((random.nextFloat()) / 2 + 0.5f) * 12));

                        chest.addPain((random.nextFloat() + 0.5f) * 10);
                        if (random.nextInt(4) == 0) {
                            chest.addBoneHealTimer(10);
                        }
                        if (random.nextInt(8) == 0) {
                            chest.addMuscleHealth(- (random.nextFloat() + 0.5f) * 1);
                        }
                    }
                }
                targetData.consciousness(targetData.consciousness() - 5);
            });
        });
        ctx.get().setPacketHandled(true);
    }

    public static void handleGiveUp(ServerboundGiveUpPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;

            sender.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA)
                    .ifPresent(data -> {
                        if (data.consciousness() <= 10) data.kill(sender, true);
                    });
        });
        ctx.get().setPacketHandled(true);
    }

    public static void handleGuiSyncToggle(ServerboundGuiSyncTogglePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;

            Entity entity = sender.level().getEntity(packet.targetId());
            if (!(entity instanceof ServerPlayer target) || sender.distanceToSqr(entity) > TOO_FAR) return;

            if (packet.enable()) {
                SyncTracker.add(target, sender);
            } else {
                SyncTracker.removeViewer(sender);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static void handleTalk(ServerboundTalkPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                LimbStatistics stats = data.getLimb(Limb.HEAD);
                if (stats.getDislocationTimer() > 0) stats.addPain(0.3f);
            });
        });
        ctx.get().setPacketHandled(true);
    }

    public static void handleLegUse(ServerboundLegUsePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null || player.isPassenger() || player.isFallFlying() || player.getAbilities().flying) return;

            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA)
                    .ifPresent(PlayerHealthData::onLegUse);
        });
        ctx.get().setPacketHandled(true);
    }

    public static void handleTransferFluid(ServerboundTransferFluidPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender == null) return;

            AbstractContainerMenu menu = sender.containerMenu;
            if (menu == null) return;

            ItemStack fromStack = menu.getCarried();
            Slot toSlot = menu.getSlot(packet.toSlot());
            ItemStack toStack = toSlot.getItem();
            if (fromStack.isEmpty() || !(fromStack.getItem() instanceof MultiTankFluidItem fromTank)
                    || toStack.isEmpty() || !(toStack.getItem() instanceof MultiTankFluidItem toTank)) return;

            float toTransfer = Math.min(packet.amount(), Math.min(fromTank.getHandler(fromStack).getTank().getTotalFluid(), toTank.getHandler(toStack).getTank().getFreeSpace()));

            if (toTransfer < 1) return;

            List<FluidStack> drained = MultiTankHelper.drain(fromStack, toTransfer, false);
            for (FluidStack stack : drained) {
                MultiTankHelper.addFluid(toStack, stack.getAmount(), stack);
            }

            menu.broadcastChanges();
        });
        context.setPacketHandled(true);
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

    public static void handleExchangeItemInHand(ServerboundExchangeItemInHandPacket packet, Supplier<NetworkEvent.Context> ctx){
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
