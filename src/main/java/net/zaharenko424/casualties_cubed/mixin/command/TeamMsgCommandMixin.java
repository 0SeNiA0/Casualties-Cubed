package net.zaharenko424.casualties_cubed.mixin.command;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.commands.TeamMsgCommand;
import net.minecraft.server.level.ServerPlayer;
import net.zaharenko424.casualties_cubed.event.BrainDamageServerController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(TeamMsgCommand.class)
public abstract class TeamMsgCommandMixin {

    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/commands/TeamMsgCommand;sendMessage(Lnet/minecraft/commands/CommandSourceStack;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/scores/PlayerTeam;Ljava/util/List;Lnet/minecraft/network/chat/PlayerChatMessage;)V"),
            method = "lambda$register$1", index = 4)
    private static PlayerChatMessage modifyMessage(PlayerChatMessage pMessage, @Local(argsOnly = true) CommandSourceStack stack) {
        ServerPlayer player = stack.getPlayer();
        if (player == null) return pMessage;

        String msg = BrainDamageServerController.modifyMessage(player, pMessage.decoratedContent().getString());
        return pMessage.withUnsignedContent(Component.literal(msg));
    }
}
