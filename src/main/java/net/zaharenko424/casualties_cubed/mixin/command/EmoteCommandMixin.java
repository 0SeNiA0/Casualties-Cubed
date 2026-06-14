package net.zaharenko424.casualties_cubed.mixin.command;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.commands.EmoteCommands;
import net.minecraft.server.level.ServerPlayer;
import net.zaharenko424.casualties_cubed.event.BrainDamageServerController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EmoteCommands.class)
public abstract class EmoteCommandMixin {

    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastChatMessage(Lnet/minecraft/network/chat/PlayerChatMessage;Lnet/minecraft/commands/CommandSourceStack;Lnet/minecraft/network/chat/ChatType$Bound;)V"),
            method = "lambda$register$0", index = 0)
    private static PlayerChatMessage modifyMessage(PlayerChatMessage pMessage, @Local CommandSourceStack stack) {
        ServerPlayer player = stack.getPlayer();
        if (player == null) return pMessage;

        String msg = BrainDamageServerController.modifyMessage(player, pMessage.decoratedContent().getString());
        return pMessage.withUnsignedContent(Component.literal(msg));
    }
}
