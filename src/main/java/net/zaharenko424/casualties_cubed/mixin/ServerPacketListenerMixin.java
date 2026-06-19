package net.zaharenko424.casualties_cubed.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import net.zaharenko424.casualties_cubed.event.BrainDamageServerController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerPacketListenerMixin {

    @Shadow
    public ServerPlayer player;

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastChatMessage(Lnet/minecraft/network/chat/PlayerChatMessage;Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/network/chat/ChatType$Bound;)V"),
            method = "broadcastChatMessage")
    private void chatMsgToSystem(PlayerList instance, PlayerChatMessage pMessage, ServerPlayer pSender, ChatType.Bound pBoundChatType, Operation<Void> original) {
        String msg = BrainDamageServerController.modifyMessage(player, pMessage.decoratedContent().getString());
        if (msg == null) return;

        if (msg.equals(pMessage.decoratedContent().getString())) {
            original.call(instance, pMessage, pSender, pBoundChatType);
            return;
        }

        original.call(instance, PlayerChatMessage.system(msg), pSender, pBoundChatType);
    }
}
