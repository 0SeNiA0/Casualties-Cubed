package net.zaharenko424.casualties_cubed.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.server.command.EnumArgument;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.fluid_system.MultiTankHelper;
import net.zaharenko424.casualties_cubed.item.multi_tank.MultiTankFluidItem;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.LimbStatistics;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import net.zaharenko424.casualties_cubed.limbs.Stat;
import net.zaharenko424.casualties_cubed.registry.ModFluids;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModCommands {

    private static final SuggestionProvider<CommandSourceStack> LIMBS = (context, builder) -> {
        for (Limb e : Limb.values()) {
            builder.suggest(e.name().toLowerCase()); // lowercase is more user-friendly
        }

        return builder.buildFuture();
    };

    private static final Function<CommandContext<CommandSourceStack>, Limb> LIMB_ARG = ctx ->
            Limb.valueOf(StringArgumentType.getString(ctx, "limb").toUpperCase());

    private static final SuggestionProvider<CommandSourceStack> MED_FLUIDS = (context, builder) -> {
        for (RegistryObject<Fluid> medicalFluid : ModFluids.FLUIDS.getEntries()) {
            builder.suggest(medicalFluid.getId().toString());
        }
        return builder.buildFuture();
    };

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        LiteralCommandNode<CommandSourceStack> node = dispatcher.register(
                Commands.literal("casualties_cubed")
                        .requires(source -> source.hasPermission(Commands.LEVEL_GAMEMASTERS))

                        .then(Commands.literal("addExp")
                                .then(Commands.argument("stat", EnumArgument.enumArgument(Stat.class))
                                        .then(Commands.argument("exp", FloatArgumentType.floatArg(0))
                                                .executes(ctx -> ctx.getSource().isPlayer() ? addExp(ctx, List.of(ctx.getSource().getPlayerOrException())) : 0)
                                                .then(Commands.argument("targets", EntityArgument.players())
                                                        .executes(ctx -> addExp(ctx, EntityArgument.getPlayers(ctx, "targets")))
                                                )
                                        )
                                )
                        )

                        .then(Commands.literal("coagulate")
                                .executes(ctx -> ctx.getSource().isPlayer() ? coagulate(ctx, List.of(ctx.getSource().getPlayerOrException())) : 0)
                                .then(Commands.argument("targets", EntityArgument.players())
                                        .executes(ctx -> coagulate(ctx, EntityArgument.getPlayers(ctx, "targets")))
                                )
                        )

                        .then(Commands.literal("heal")
                                .executes(ctx -> ctx.getSource().isPlayer() ? heal(ctx, List.of(ctx.getSource().getPlayerOrException())) : 0)
                                .then(Commands.argument("targets", EntityArgument.players())
                                        .executes(ctx -> heal(ctx, EntityArgument.getPlayers(ctx, "targets")))
                                )
                        )

                        .then(Commands.literal("checklimb")
                                .then(Commands.argument("limb", StringArgumentType.word())
                                        .suggests(LIMBS)
                                        .executes(ctx -> ctx.getSource().isPlayer() ? checkLimb(ctx, ctx.getSource().getPlayerOrException(), LIMB_ARG.apply(ctx)) : 0)
                                        .then(Commands.argument("target", EntityArgument.player())
                                                .executes(ctx -> checkLimb(ctx, EntityArgument.getPlayer(ctx, "target"), LIMB_ARG.apply(ctx)))
                                        )
                                )
                        )

                        .then(Commands.literal("checkbody")
                                .executes(ctx -> ctx.getSource().isPlayer() ? checkBody(ctx, ctx.getSource().getPlayerOrException()) : 0)
                                .then(Commands.argument("target", EntityArgument.player())
                                        .executes(ctx -> checkBody(ctx, EntityArgument.getPlayer(ctx, "target")))
                                )
                        )

                        .then(Commands.literal("setlimb")
                                .then(Commands.argument("target", EntityArgument.player())
                                        .then(Commands.argument("limb", StringArgumentType.word())
                                                .suggests(LIMBS)
                                                .then(Commands.argument("field", StringArgumentType.word())
                                                        .suggests((ctx, builder) -> {
                                                            builder.suggest("skinhealth");
                                                            builder.suggest("musclehealth");
                                                            builder.suggest("pain");
                                                            builder.suggest("infection");
                                                            builder.suggest("fracturetimer");
                                                            builder.suggest("dislocatedtimer");
                                                            builder.suggest("bleedrate");
                                                            builder.suggest("desinfectiontimer");
                                                            return builder.buildFuture();
                                                        })
                                                        .then(Commands.argument("value", FloatArgumentType.floatArg())
                                                                .executes(ctx -> {
                                                                    Limb limb = LIMB_ARG.apply(ctx);
                                                                    String raw = StringArgumentType.getString(ctx, "field");
                                                                    float value = FloatArgumentType.getFloat(ctx, "value");
                                                                    ServerPlayer target = EntityArgument.getPlayer(ctx, "target");

                                                                    target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                                                                        LimbStatistics stats = data.getLimb(limb);
                                                                        switch (raw) {
                                                                            case "skinhealth" ->
                                                                                    stats.setSkinHealth(value);
                                                                            case "musclehealth" ->
                                                                                    stats.setMuscleHealth(value);
                                                                            case "pain" ->
                                                                                    stats.setPain(value);
                                                                            case "infection" ->
                                                                                    stats.setInfection(value);
                                                                            case "fracturetimer" ->
                                                                                    stats.setBoneHealTimer(value);
                                                                            case "dislocatedtimer" ->
                                                                                    stats.setDislocationTimer(value);
                                                                            case "desinfectiontimer" ->
                                                                                    stats.setDisinfectionTime(value);
                                                                            case "bleedrate" ->
                                                                                    stats.setBleedRate(value);
                                                                            default ->
                                                                                    ctx.getSource().sendFailure(Component.translatable("commands.casualties_cubed.error.unknown_field", raw));
                                                                        }
                                                                    });
                                                                    ctx.getSource().sendSuccess(() ->
                                                                                    Component.translatable("commands.casualties_cubed.setlimb.success", value, limb, raw, target.getName()),
                                                                            false);
                                                                    return 1;
                                                                })
                                                        )
                                                )
                                        )
                                )
                        )

                        .then(Commands.literal("setbody")
                                .then(Commands.argument("target", EntityArgument.player())
                                        .then(Commands.argument("field", StringArgumentType.word())
                                                .suggests((ctx, builder) -> {
                                                    // suggest all available fields
                                                    builder.suggest("blood");
                                                    builder.suggest("contiousness");
                                                    builder.suggest("hemothorax");
                                                    builder.suggest("internalBleeding");
                                                    builder.suggest("oxygen");
                                                    builder.suggest("bloodViscosity");
                                                    builder.suggest("brainhealth");
                                                    builder.suggest("dirtyness");
                                                    builder.suggest("painshock");
                                                    builder.suggest("temperature");
                                                    builder.suggest("lefteyeblind");
                                                    builder.suggest("righteyeblind");
                                                    builder.suggest("mouthremoved");
                                                    return builder.buildFuture();
                                                })
                                                .then(Commands.argument("value", FloatArgumentType.floatArg())
                                                        .executes(ctx -> {
                                                            String field = StringArgumentType.getString(ctx, "field").toLowerCase();
                                                            float value = FloatArgumentType.getFloat(ctx, "value");
                                                            ServerPlayer target = EntityArgument.getPlayer(ctx, "target");

                                                            // Access the player's capability or component that stores these stats
                                                            target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h -> {
                                                                switch (field) {
                                                                    case "blood" -> h.setBloodVolume(value);
                                                                    case "contiousness" -> h.setConsciousness(value);
                                                                    case "hemothorax" -> h.setHemothorax(value);
                                                                    case "internalbleeding" ->
                                                                            h.setInternalBleeding(value);
                                                                    case "oxygen" -> h.setBloodOxygen(value);
                                                                    case "bloodviscosity" -> h.setBloodViscosity(value);
                                                                    case "brainhealth" -> h.brainHealth(value);
                                                                    case "dirtyness" -> h.setDirtiness(value);
                                                                    case "painshock" -> h.setShock(value);
                                                                    case "temperature" -> h.setTemperature(value);
                                                                    case "lefteyeblind" -> h.setLeftEyeBlind(value > 0);
                                                                    case "righteyeblind" ->
                                                                            h.setRightEyeBlind(value > 0);
                                                                    case "mouthremoved" -> h.disfigured(value > 0);
                                                                    default ->
                                                                            ctx.getSource().sendFailure(Component.translatable("commands.casualties_cubed.error.unknown_field", field));
                                                                }
                                                            });

                                                            ctx.getSource().sendSuccess(() ->
                                                                            Component.translatable("commands.casualties_cubed.setbody.success", value, field, target.getName()),
                                                                    false);
                                                            return 1;
                                                        })
                                                )
                                        )
                                )
                        )

                        .then(Commands.literal("amputate")
                                .then(Commands.argument("limb", StringArgumentType.word())
                                        .suggests(LIMBS)
                                        .executes(ctx -> ctx.getSource().isPlayer() ? amputate(ctx, ctx.getSource().getPlayerOrException(), LIMB_ARG.apply(ctx)) : 0)
                                        .then(Commands.argument("target", EntityArgument.player())
                                                .executes(ctx -> amputate(ctx, EntityArgument.getPlayer(ctx, "target"), LIMB_ARG.apply(ctx)))
                                        )
                                )
                        )

                        .then(Commands.literal("fillfluid")
                                .then(Commands.argument("fluid", ResourceLocationArgument.id())
                                        .suggests(MED_FLUIDS)
                                        .then(Commands.argument("amount", IntegerArgumentType.integer())
                                                .executes(ctx -> {
                                                    ResourceLocation id = ResourceLocationArgument.getId(ctx, "fluid");
                                                    ServerPlayer serverplayer = ctx.getSource().getPlayer();
                                                    if (serverplayer == null) {
                                                        ctx.getSource().sendFailure(Component.translatable("commands.casualties_cubed.fillfluid.error.player_only"));
                                                        return 1;
                                                    }

                                                    Optional<Fluid> fluid = BuiltInRegistries.FLUID.getOptional(id);
                                                    if (fluid.isEmpty()) {
                                                        ctx.getSource().sendFailure(Component.translatable("commands.casualties_cubed.fillfluid.error.invalid_fluid"));
                                                        return 1;
                                                    }

                                                    ItemStack itemStack = serverplayer.getItemInHand(InteractionHand.MAIN_HAND);
                                                    if (itemStack.isEmpty() || !(itemStack.getItem() instanceof MultiTankFluidItem)) {
                                                        ctx.getSource().sendFailure(Component.translatable("commands.casualties_cubed.fillfluid.error.no_item"));
                                                        return 1;
                                                    }

                                                    int amount = IntegerArgumentType.getInteger(ctx, "amount");
                                                    MultiTankHelper.addFluid(itemStack, amount, new FluidStack(fluid.get(), amount));
                                                    ctx.getSource().sendSuccess(() ->
                                                            Component.translatable("commands.casualties_cubed.fillfluid.success", amount, id), true);
                                                    return 1;
                                                })
                                        )
                                )
                        )
        );

        dispatcher.register(Commands.literal("ccu")
                .requires(ctx -> ctx.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .redirect(node));
    }

    private static int addExp(CommandContext<CommandSourceStack> ctx, Collection<ServerPlayer> targets) {
        Stat stat = ctx.getArgument("stat", Stat.class);
        float exp = FloatArgumentType.getFloat(ctx, "exp");

        for (ServerPlayer player : targets) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data ->
                    data.skills.addExp(player, stat, exp));
        }

        ctx.getSource().sendSuccess(() ->
                Component.translatable("commands.casualties_cubed.add_exp.success", exp, stat, targets.size()), true);

        return targets.size();
    }

    private static int coagulate(CommandContext<CommandSourceStack> ctx, Collection<ServerPlayer> targets) {
        for (ServerPlayer player : targets) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                for (Limb limb : Limb.values()) {
                    data.getLimb(limb).setBleedRate(0);
                }
                data.setInternalBleeding(0);
            });
        }

        ctx.getSource().sendSuccess(() ->
                Component.translatable("commands.casualties_cubed.coagulate.success", targets.size()), true);

        return targets.size();
    }

    private static int heal(CommandContext<CommandSourceStack> ctx, Collection<ServerPlayer> targets) {
        for (ServerPlayer player : targets) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data ->
                    data.heal(player));
        }

        ctx.getSource().sendSuccess(() ->
                Component.translatable("commands.casualties_cubed.heal.success", targets.size()), true);

        return targets.size();
    }

    private static int checkLimb(CommandContext<CommandSourceStack> ctx, ServerPlayer target, Limb limb) {
        target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data ->
                ctx.getSource().sendSuccess(() -> Component.literal(data.getLimb(limb).toString()), false)
        );

        return 1;
    }

    private static int checkBody(CommandContext<CommandSourceStack> ctx, ServerPlayer target) {
        Optional<String> text = target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(PlayerHealthData::baseToString);
        ctx.getSource().sendSuccess(() ->
                Component.literal(String.valueOf(text)), false);
        return 1;
    }

    private static int amputate(CommandContext<CommandSourceStack> ctx, ServerPlayer target, Limb limb) {
        target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h -> {
            h.dismember(limb);
            ctx.getSource().sendSuccess(() -> Component.translatable("commands.casualties_cubed.amputate.success", limb.comp), true);
        });

        return 1;
    }
}
