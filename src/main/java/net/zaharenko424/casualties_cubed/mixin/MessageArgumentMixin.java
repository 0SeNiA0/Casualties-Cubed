package net.zaharenko424.casualties_cubed.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;
import net.zaharenko424.casualties_cubed.event.BrainDamageServerController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Consumer;

@Mixin(MessageArgument.class)
public abstract class MessageArgumentMixin {

    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/commands/arguments/MessageArgument;resolveSignedMessage(Ljava/util/function/Consumer;Lnet/minecraft/commands/CommandSourceStack;Lnet/minecraft/network/chat/PlayerChatMessage;)V"),
            method = "resolveChatMessage")
    private static Consumer<PlayerChatMessage> modifyMessage(Consumer<PlayerChatMessage> pCallback, @Local(argsOnly = true) CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        if (player == null) return pCallback;

        return msg -> {
            String str = BrainDamageServerController.modifyMessage(player, msg.decoratedContent().getString());
            if (str == null) return;

            pCallback.accept(PlayerChatMessage.system(str));
        };
    }

    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/commands/arguments/MessageArgument;resolveDisguisedMessage(Ljava/util/function/Consumer;Lnet/minecraft/commands/CommandSourceStack;Lnet/minecraft/network/chat/PlayerChatMessage;)V"),
            method = "resolveChatMessage")
    private static Consumer<PlayerChatMessage> modifyMessage1(Consumer<PlayerChatMessage> pCallback, @Local(argsOnly = true) CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        if (player == null) return pCallback;

        return msg -> {
            String str = BrainDamageServerController.modifyMessage(player, msg.decoratedContent().getString());
            if (str == null) return;

            pCallback.accept(PlayerChatMessage.system(str));
        };
    }
}
