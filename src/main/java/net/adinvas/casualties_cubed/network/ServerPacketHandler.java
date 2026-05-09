package net.adinvas.casualties_cubed.network;

import net.adinvas.casualties_cubed.CasualtiesCubed;
import net.adinvas.casualties_cubed.PlayerHealthProvider;
import net.adinvas.casualties_cubed.fluid_system.MedicalFluid;
import net.adinvas.casualties_cubed.fluid_system.MultiTankHelper;
import net.adinvas.casualties_cubed.item.api.IBag;
import net.adinvas.casualties_cubed.item.api.IBandage;
import net.adinvas.casualties_cubed.item.api.ISimpleMedicalUsable;
import net.adinvas.casualties_cubed.item.multi_tank.MultiTankFluidItem;
import net.adinvas.casualties_cubed.limbs.Limb;
import net.adinvas.casualties_cubed.limbs.PlayerHealthData;
import net.adinvas.casualties_cubed.network.packet.*;
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

import java.util.List;
import java.util.function.Supplier;

//TODO add med session? (keep track of players interacting with other players. potential refuse med interaction keybind)
//TODO ensure that sender actually has the item that they say they are using
public class ServerPacketHandler {

    public static final float TOO_FAR = 3 * 3;

    public static void handleAdjustShrapnel(ServerboundAdjustShrapnelPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;

            Entity entity = sender.level().getEntity(packet.targetId());
            if (!(entity instanceof Player target) || sender.distanceToSqr(entity) > TOO_FAR) return;

            target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(targetData ->
                    targetData.setLimbShrapnel(packet.limb(), packet.amount()));
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
                Limb limb = packet.limb();
                RandomSource random = sender.getRandom();
                targetData.setLimbPain(limb, targetData.getLimbPain(limb) + (random.nextFloat() + 1) * 6);
                targetData.setLimbMuscleHealth(limb,targetData.getLimbMuscleHealth(limb) - (random.nextFloat() + 0.5f) * 2.5f);
                targetData.setLimbSkinHealth(limb,targetData.getLimbSkinHealth(limb) - (random.nextFloat() + 0.5f) * 3.5f);
                targetData.applyBleedDamage(limb,(random.nextFloat() + 0.5f) / 5,null);
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

            PlayerHealthData data = sender.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).orElse(null);
            if (data.isAmputated(Limb.RIGHT_ARM) && data.isAmputated(Limb.LEFT_ARM)) return;// Cant interact without arms

            target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(targetData -> {
                RandomSource random = sender.getRandom();
                Limb limb = packet.limb();
                targetData.setLimbPain(limb,targetData.getLimbPain(limb) + (random.nextFloat() + 1) * 80);
                targetData.setLimbBleedRate(limb,targetData.getLimbBleedRate(limb) * 0.4f);
                targetData.setLimbMuscleHealth(limb, targetData.getLimbMuscleHealth(limb) - (random.nextFloat() + 1) * 15);
                targetData.setLimbSkinHealth(limb, targetData.getLimbSkinHealth(limb) - (random.nextFloat() + 1) * 25);
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
            if (data.isAmputated(Limb.RIGHT_ARM) && data.isAmputated(Limb.LEFT_ARM)) return;// Cant interact without arms

            target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(targetData -> {
                Limb limb = packet.limb();
                if (targetData.getLimbDislocated(limb) == 0) return;

                targetData.setLimbDislocation(limb, packet.dislocationValue());
                targetData.setLimbPain(limb, targetData.getLimbPain(limb) + (sender.getRandom().nextFloat() * 20) + 20);
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

            //TODO ensure hand not missing

            for (int i = 0; i < packet.ids().length; i++) {
                MedicalFluid fluid = MedicalFluid.getFromId(packet.ids()[i]);
                float amount = packet.amounts()[i];
                if (fluid != null && !Float.isNaN(amount)) {
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

            PlayerHealthData data = sender.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).orElse(null);
            if (data.isAmputated(Limb.RIGHT_HAND) && data.isAmputated(Limb.LEFT_HAND)) return;// Cant use syringe without a hand

            target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(targetData -> {
                RandomSource random = sender.getRandom();
                Limb limb = packet.limb();
                targetData.setLimbPain(limb, targetData.getLimbPain(limb) + ((random.nextFloat() + 0.5f) * 20));
                targetData.setLimbShrapnel(limb,targetData.hasLimbShrapnel(limb) + 1);
            });
        });
        ctx.get().setPacketHandled(true);
    }

    public static void handleUseBandage(ServerboundUseBandagePacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if (packet.durability() == 0) return;// Noop

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

                if (!(item instanceof IBandage bandage)) return;

                bandage.use(target, packet.limb(), stackInBag, packet.durability());
                bag.setItem(stack, bagSlot, stackInBag);
                return;
            }

            if (item instanceof IBandage bandage) {
                bandage.use(target, packet.limb(), stack, packet.durability());
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

                if (!(item instanceof ISimpleMedicalUsable usable)) return;

                usable.onMedicalUse(packet.limb(), sender, target, stackInBag);
                bag.setItem(stack, bagSlot, stackInBag);
                return;
            }

            if (item instanceof ISimpleMedicalUsable usable) {
                usable.onMedicalUse(packet.limb(), sender, target, stack);
            }
        });
        context.setPacketHandled(true);
    }

    public static void handleMedicalAction(ServerboundMedicalActionPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;

            Entity entity = sender.level().getEntity(packet.targetId());
            if (!(entity instanceof Player target) || sender.distanceToSqr(entity) > TOO_FAR) return;

            PlayerHealthData data = sender.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).orElse(null);
            if (data.isAmputated(Limb.RIGHT_ARM) && data.isAmputated(Limb.LEFT_ARM)) return;// Cant interact without arms

            target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(targetData -> {
                if (targetData.isAmputated(packet.limb())) return;// Cant interact with amputated limb

                targetData.medicalAction(packet.action(), packet.limb(), sender);
            });
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
            if (data.isAmputated(Limb.RIGHT_ARM) && data.isAmputated(Limb.LEFT_ARM)) return;// Cant interact without arms

            target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(targetData -> {
                RandomSource random = sender.getRandom();

                switch (packet.success()) {
                    case LOW -> {
                        if (targetData.getLimbMuscleHealth(Limb.CHEST) <= 5)
                            targetData.setOxygen(Math.max(targetData.getOxygen(), ((random.nextFloat()) / 2 + 0.5f) * 3));
                        else
                            targetData.setOxygen(Math.max(targetData.getOxygen(), ((random.nextFloat()) / 2 + 0.5f) * 6));

                        targetData.setLimbPain(Limb.CHEST, targetData.getLimbPain(Limb.CHEST) + (random.nextFloat() + 0.5f) * 30);
                        if (random.nextInt(8) == 0) {
                            targetData.setLimbFracture(Limb.CHEST, targetData.getLimbFracture(Limb.CHEST) + 10);
                        }
                        if (random.nextInt(2) == 0) {
                            targetData.setLimbMuscleHealth(Limb.CHEST, targetData.getLimbMuscleHealth(Limb.CHEST) - (random.nextFloat() + 0.5f) * 8);
                        }
                    }
                    case MEDIUM -> {
                        if (targetData.getLimbMuscleHealth(Limb.CHEST) <= 5)
                            targetData.setOxygen(Math.max(targetData.getOxygen(), ((random.nextFloat()) / 2 + 0.5f) * 4));
                        else
                            targetData.setOxygen(Math.max(targetData.getOxygen(), ((random.nextFloat()) / 2 + 0.5f) * 8));
                        targetData.setLimbPain(Limb.CHEST, targetData.getLimbPain(Limb.CHEST) + (random.nextFloat() + 0.5f) * 20);
                        if (random.nextInt(6) == 0) {
                            targetData.setLimbFracture(Limb.CHEST, targetData.getLimbFracture(Limb.CHEST) + 10);
                        }
                        if (random.nextInt(4) == 0) {
                            targetData.setLimbMuscleHealth(Limb.CHEST, targetData.getLimbMuscleHealth(Limb.CHEST) - (random.nextFloat() + 0.5f) * 5);
                        }
                    }
                    case HIGH -> {
                        if (targetData.getLimbMuscleHealth(Limb.CHEST) <= 5)
                            targetData.setOxygen(Math.max(targetData.getOxygen(), ((random.nextFloat()) / 2 + 0.5f) * 6));
                        else
                            targetData.setOxygen(Math.max(targetData.getOxygen(), ((random.nextFloat()) / 2 + 0.5f) * 12));
                        targetData.setLimbPain(Limb.CHEST, targetData.getLimbPain(Limb.CHEST) + (random.nextFloat() + 0.5f) * 10);
                        if (random.nextInt(4) == 0) {
                            targetData.setLimbFracture(Limb.CHEST, targetData.getLimbFracture(Limb.CHEST) + 10);
                        }
                        if (random.nextInt(8) == 0) {
                            targetData.setLimbMuscleHealth(Limb.CHEST, targetData.getLimbMuscleHealth(Limb.CHEST) - (random.nextFloat() + 0.5f) * 1);
                        }
                    }
                }
                targetData.setConsciousness(targetData.getConsciousness() - 5);
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
                        if (data.getConsciousness() <= 10) data.killPlayer(sender, true);
                    });
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

            List<FluidStack> drained = MultiTankHelper.drain(fromStack, toTransfer);
            for (FluidStack stack : drained) {
                CasualtiesCubed.LOGGER.info("fluid {}, amount {}", stack.getFluid(), stack.getAmount());
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
