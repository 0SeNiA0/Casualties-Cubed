package net.zaharenko424.casualties_cubed.mixin.command;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.commands.MsgCommand;
import net.minecraft.server.level.ServerPlayer;
import net.zaharenko424.casualties_cubed.event.BrainDamageServerController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(MsgCommand.class)
public abstract class MsgCommandMixin {

    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/commands/MsgCommand;sendMessage(Lnet/minecraft/commands/CommandSourceStack;Ljava/util/Collection;Lnet/minecraft/network/chat/PlayerChatMessage;)V"),
            method = "lambda$register$0", index = 2)
    private static PlayerChatMessage modifyMessage(PlayerChatMessage pMessage, @Local(argsOnly = true) CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        if (player == null) return pMessage;

        String msg = BrainDamageServerController.modifyMessage(player, pMessage.decoratedContent().getString());
        return pMessage.withUnsignedContent(Component.literal(msg));
    }
}
