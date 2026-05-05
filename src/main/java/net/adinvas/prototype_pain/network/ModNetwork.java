package net.adinvas.prototype_pain.network;

import net.adinvas.prototype_pain.PrototypePain;
import net.adinvas.prototype_pain.network.packet.*;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class ModNetwork {
    
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            PrototypePain.resourceLoc("prototype_pain_main"), // channel name (unique per mod)
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    @SuppressWarnings("Convert2MethodRef")
    public static void register() {
        // Register your packets here
        int id = 0;
        CHANNEL.registerMessage(id++, ClientboundSyncHealthPacket.class, ClientboundSyncHealthPacket::write, ClientboundSyncHealthPacket::new, (packet, ctx) -> ClientPacketHandler.handleSyncHealth(packet, ctx));
        CHANNEL.registerMessage(id++, ServerboundGuiSyncTogglePacket.class, ServerboundGuiSyncTogglePacket::write, ServerboundGuiSyncTogglePacket::new, ServerPacketHandler::handleGuiSyncToggle);
        CHANNEL.registerMessage(id++, ServerboundUseMedItemPacket.class, ServerboundUseMedItemPacket::write, ServerboundUseMedItemPacket::new, ServerPacketHandler::handleUseMedItem);
        CHANNEL.registerMessage(id++, ServerboundMedicalActionPacket.class, ServerboundMedicalActionPacket::write, ServerboundMedicalActionPacket::new, ServerPacketHandler::handleMedicalAction);
        CHANNEL.registerMessage(id++, ServerboundLegUsePacket.class, ServerboundLegUsePacket::toBytes, ServerboundLegUsePacket::new, ServerPacketHandler::handleLegUse);
        CHANNEL.registerMessage(id++, ServerboundGiveUpPacket.class, ServerboundGiveUpPacket::write, ServerboundGiveUpPacket::new, ServerPacketHandler::handleGiveUp);
        CHANNEL.registerMessage(id++, ServerboundFluidTransferPacket.class, ServerboundFluidTransferPacket::write, ServerboundFluidTransferPacket::new, ServerPacketHandler::handleFluidTransfer);
        CHANNEL.registerMessage(id++, ServerboundSyringeFailPacket.class, ServerboundSyringeFailPacket::write, ServerboundSyringeFailPacket::new, ServerPacketHandler::handleSyringeFail);
        CHANNEL.registerMessage(id++, ServerboundUseSyringePacket.class, ServerboundUseSyringePacket::write, ServerboundUseSyringePacket::new, ServerPacketHandler::handleUseSyringe);
        CHANNEL.registerMessage(id++, ExchangeItemInHandPacket.class,ExchangeItemInHandPacket::write,ExchangeItemInHandPacket::new,ServerPacketHandler::handleExchangeItemInHand);
        CHANNEL.registerMessage(id++, ServerboundUseBandagePacket.class, ServerboundUseBandagePacket::write, ServerboundUseBandagePacket::new, ServerPacketHandler::handleUseBandage);
        CHANNEL.registerMessage(id++, ServerboundDislocationTryPacket.class, ServerboundDislocationTryPacket::write, ServerboundDislocationTryPacket::new, ServerPacketHandler::handleDislocationFix);
        CHANNEL.registerMessage(id++, ServerboundShrapnelFailPacket.class, ServerboundShrapnelFailPacket::write, ServerboundShrapnelFailPacket::new, ServerPacketHandler::handleShrapnelFail);
        CHANNEL.registerMessage(id++, ServerboundAdjustShrapnelPacket.class, ServerboundAdjustShrapnelPacket::write, ServerboundAdjustShrapnelPacket::new, ServerPacketHandler::handleAdjustShrapnel);
        CHANNEL.registerMessage(id++, ServerboundUseBagMedItemPacket.class, ServerboundUseBagMedItemPacket::write, ServerboundUseBagMedItemPacket::new,ServerPacketHandler::handleUseBagMedItem);
        CHANNEL.registerMessage(id++, ServerboundExchangeItemInBagPacket.class, ServerboundExchangeItemInBagPacket::write, ServerboundExchangeItemInBagPacket::new,ServerPacketHandler::handleExchangeItemInBag);
        CHANNEL.registerMessage(id++, ServerboundCauterizeActionPacket.class, ServerboundCauterizeActionPacket::write, ServerboundCauterizeActionPacket::new, ServerPacketHandler::handleCauterize);
        CHANNEL.registerMessage(id++, ServerboundTalkPacket.class, ServerboundTalkPacket::toBytes, ServerboundTalkPacket::new, ServerPacketHandler::handleTalk);
        CHANNEL.registerMessage(id++, ServerboundCPRPacket.class, ServerboundCPRPacket::write, ServerboundCPRPacket::new, ServerPacketHandler::handleCPR);
        CHANNEL.registerMessage(id++, ClientboundTriggerLastStandPacket.class, ClientboundTriggerLastStandPacket::write, ClientboundTriggerLastStandPacket::new, (packet, ctx) -> ClientPacketHandler.handleLastStand(packet, ctx));
        CHANNEL.registerMessage(id++, ClientboundBlindnessViewSyncPacket.class, ClientboundBlindnessViewSyncPacket::encode, ClientboundBlindnessViewSyncPacket::decode, ClientboundBlindnessViewSyncPacket::handle);
        CHANNEL.registerMessage(id++, ClientboundFluidSyncPacket.class, ClientboundFluidSyncPacket::write, ClientboundFluidSyncPacket::new, (packet, ctx) -> ClientPacketHandler.handleFluidSync(packet, ctx), Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(id++, ClientboundAmputateRestrictionSyncPacket.class, ClientboundAmputateRestrictionSyncPacket::encode, ClientboundAmputateRestrictionSyncPacket::new, (packet, ctx) -> ClientPacketHandler.handleAmputateRestriction(packet, ctx), Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }
}
