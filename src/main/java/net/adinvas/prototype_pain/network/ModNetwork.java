package net.adinvas.prototype_pain.network;

import net.adinvas.prototype_pain.PrototypePain;
import net.adinvas.prototype_pain.network.packet.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModNetwork {
    
    private static final String PROTOCOL_VERSION = "1";
    private static int id = 0;
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            PrototypePain.resourceLoc("prototype_pain_main"), // channel name (unique per mod)
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    @SuppressWarnings("Convert2MethodRef")
    public static void registerPackets() {
        registerClientbound(ClientboundAmputateRestrictionSyncPacket.class, ClientboundAmputateRestrictionSyncPacket::encode,
                ClientboundAmputateRestrictionSyncPacket::new, (packet, ctx) -> ClientPacketHandler.handleAmputateRestriction(packet, ctx));

        registerClientbound(ClientboundBlindnessViewSyncPacket.class, ClientboundBlindnessViewSyncPacket::encode,
                ClientboundBlindnessViewSyncPacket::decode, ClientboundBlindnessViewSyncPacket::handle);

        registerClientbound(ClientboundFluidSyncPacket.class, ClientboundFluidSyncPacket::encode,
                ClientboundFluidSyncPacket::new, (packet, ctx) -> ClientPacketHandler.handleFluidSync(packet, ctx));

        registerClientbound(ClientboundSyncHealthPacket.class, ClientboundSyncHealthPacket::encode,
                ClientboundSyncHealthPacket::new, (packet, ctx) -> ClientPacketHandler.handleSyncHealth(packet, ctx));

        registerClientbound(ClientboundTriggerLastStandPacket.class, ClientboundTriggerLastStandPacket::encode,
                ClientboundTriggerLastStandPacket::new, (packet, ctx) -> ClientPacketHandler.handleLastStand(packet, ctx));



        registerServerbound(ServerboundAdjustShrapnelPacket.class, ServerboundAdjustShrapnelPacket::encode,
                ServerboundAdjustShrapnelPacket::new, ServerPacketHandler::handleAdjustShrapnel);

        registerServerbound(ServerboundCauterizeActionPacket.class, ServerboundCauterizeActionPacket::encode,
                ServerboundCauterizeActionPacket::new, ServerPacketHandler::handleCauterize);

        registerServerbound(ServerboundCPRPacket.class, ServerboundCPRPacket::encode, ServerboundCPRPacket::new,
                ServerPacketHandler::handleCPR);

        registerServerbound(ServerboundDislocationTryPacket.class, ServerboundDislocationTryPacket::encode,
                ServerboundDislocationTryPacket::new, ServerPacketHandler::handleDislocationFix);

        registerServerbound(ServerboundExchangeItemInBagPacket.class, ServerboundExchangeItemInBagPacket::encode,
                ServerboundExchangeItemInBagPacket::new, ServerPacketHandler::handleExchangeItemInBag);

        registerServerbound(ServerboundExchangeItemInHandPacket.class, ServerboundExchangeItemInHandPacket::encode,
                ServerboundExchangeItemInHandPacket::new,ServerPacketHandler::handleExchangeItemInHand);

        registerServerbound(ServerboundFluidTransferPacket.class, ServerboundFluidTransferPacket::encode,
                ServerboundFluidTransferPacket::new, ServerPacketHandler::handleFluidTransfer);

        registerServerbound(ServerboundGiveUpPacket.class, ServerboundGiveUpPacket::encode,
                ServerboundGiveUpPacket::new, ServerPacketHandler::handleGiveUp);

        registerServerbound(ServerboundGuiSyncTogglePacket.class, ServerboundGuiSyncTogglePacket::encode,
                ServerboundGuiSyncTogglePacket::new, ServerPacketHandler::handleGuiSyncToggle);

        registerServerbound(ServerboundLegUsePacket.class, ServerboundLegUsePacket::encode,
                ServerboundLegUsePacket::new, ServerPacketHandler::handleLegUse);

        registerServerbound(ServerboundMedicalActionPacket.class, ServerboundMedicalActionPacket::encode,
                ServerboundMedicalActionPacket::new, ServerPacketHandler::handleMedicalAction);

        registerServerbound(ServerboundShrapnelFailPacket.class, ServerboundShrapnelFailPacket::encode,
                ServerboundShrapnelFailPacket::new, ServerPacketHandler::handleShrapnelFail);

        registerServerbound(ServerboundSyringeFailPacket.class, ServerboundSyringeFailPacket::encode,
                ServerboundSyringeFailPacket::new, ServerPacketHandler::handleSyringeFail);

        registerServerbound(ServerboundTalkPacket.class, ServerboundTalkPacket::encode,
                ServerboundTalkPacket::new, ServerPacketHandler::handleTalk);

        registerServerbound(ServerboundUseBandagePacket.class, ServerboundUseBandagePacket::encode,
                ServerboundUseBandagePacket::new, ServerPacketHandler::handleUseBandage);

        registerServerbound(ServerboundUseMedItemPacket.class, ServerboundUseMedItemPacket::encode,
                ServerboundUseMedItemPacket::new, ServerPacketHandler::handleUseMedItem);

        registerServerbound(ServerboundUseSyringePacket.class, ServerboundUseSyringePacket::encode,
                ServerboundUseSyringePacket::new, ServerPacketHandler::handleUseSyringe);
    }

    private static <MSG> void registerClientbound(Class<MSG> clazz, BiConsumer<MSG, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> handler) {
        CHANNEL.registerMessage(id++, clazz, encoder, decoder, handler, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    private static <MSG> void registerServerbound(Class<MSG> clazz, BiConsumer<MSG, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> handler) {
        CHANNEL.registerMessage(id++, clazz, encoder, decoder, handler, Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }
}
