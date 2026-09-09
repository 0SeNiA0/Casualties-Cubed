package net.zaharenko424.casualties_cubed.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.phys.Vec3;
import net.zaharenko424.casualties_cubed.ServerPlayerDeltaAccess;
import net.zaharenko424.casualties_cubed.event.BrainDamageServerController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerPacketListenerMixin implements ServerPlayerDeltaAccess {

    @Shadow
    public ServerPlayer player;
    @Shadow
    private double firstGoodX;
    @Shadow
    private double firstGoodY;
    @Shadow
    private double firstGoodZ;

    @Unique
    private Vec3 ccu$deltaMovement = Vec3.ZERO;

    @Override
    public Vec3 ccu$deltaMovement() {
        return ccu$deltaMovement;
    }

    @Inject(at = @At("HEAD"), method = "tick")
    private void captureDelta(CallbackInfo ci) {
        ccu$deltaMovement = new Vec3(firstGoodX, firstGoodY, firstGoodZ).subtract(player.position());
    }

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
