package net.zaharenko424.casualties_cubed.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.fluid_system.MedicalFluid;
import net.zaharenko424.casualties_cubed.registry.ModFluids;
import net.zaharenko424.casualties_cubed.fluid_system.MultiTankHelper;
import net.zaharenko424.casualties_cubed.item.multi_tank.MultiTankFluidItem;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import net.zaharenko424.casualties_cubed.registry.ModMedicalFluids;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collection;
import java.util.Optional;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModCommands {

    private static final SuggestionProvider<CommandSourceStack> LIMBS = (context, builder) -> {
        for (Limb e : Limb.values()) {
            builder.suggest(e.name().toLowerCase()); // lowercase is more user-friendly
        }
        return builder.buildFuture();
    };

    private static final SuggestionProvider<CommandSourceStack> MED_FLUIDS = (context, builder) -> {
        for (RegistryObject<MedicalFluid> medicalFluid: ModMedicalFluids.MEDICAL_FLUIDS.getEntries()){
            builder.suggest(medicalFluid.getId().toString());
        }
        return builder.buildFuture();
    };

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(
                Commands.literal("casualties_cubed")
                .requires(source -> source.hasPermission(0))
                .then(Commands.literal("heal")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("targets", EntityArgument.players())
                                .executes(ctx -> {
                                    Collection<ServerPlayer> targets = EntityArgument.getPlayers(ctx, "targets");

                                    for (ServerPlayer player : targets) {
                                        player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(PlayerHealthData::resetToDefaults);
                                    }

                                    ctx.getSource().sendSuccess(() ->
                                            Component.literal("Healed " + targets.size() + " player(s)."), true);

                                    return targets.size();
                                })
                        )
                )

                .then(Commands.literal("checklimb")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("target", EntityArgument.player())
                                .then(Commands.argument("limb", StringArgumentType.word())
                                        .suggests(LIMBS)
                                        .executes(ctx -> {
                                            String raw = StringArgumentType.getString(ctx, "limb");
                                            Limb limb = Limb.valueOf(raw.toUpperCase());

                                            ServerPlayer target = EntityArgument.getPlayer(ctx, "target");

                                            target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h -> {
                                                ctx.getSource().sendSuccess(
                                                        () -> h.getLimbDataText(limb),
                                                        false
                                                );
                                            });

                                            return 1;
                                        })
                                )
                        )
                )

                .then(Commands.literal("checkbody")
                        .requires(source ->source.hasPermission(2))
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes(ctx -> {
                                    ServerPlayer target = EntityArgument.getPlayer(ctx,"target");
                                    Optional<String> text = target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(PlayerHealthData::baseToString);
                                    ctx.getSource().sendSuccess(() ->
                                            Component.literal(String.valueOf(text)),false);
                                    return 1;
                                })
                        )
                )

                .then(Commands.literal("setlimb")
                        .requires(source ->source.hasPermission(2))
                        .then(Commands.argument("target",EntityArgument.player())
                                .then(Commands.argument("limb",StringArgumentType.word())
                                        .suggests(LIMBS)
                                        .then(Commands.argument("field",StringArgumentType.word())
                                                .suggests((ctx,builder) -> {
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
                                                            String raw = StringArgumentType.getString(ctx,"limb");
                                                            Limb limb = Limb.valueOf(raw.toUpperCase());
                                                            raw = StringArgumentType.getString(ctx,"field");
                                                            float value = FloatArgumentType.getFloat(ctx,"value");
                                                            ServerPlayer target = EntityArgument.getPlayer(ctx,"target");

                                                            String finalRaw = raw;
                                                            target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h->{
                                                                switch (finalRaw){
                                                                    case "skinhealth" -> h.setLimbSkinHealth(limb,value);
                                                                    case "musclehealth" -> h.setLimbMuscleHealth(limb,value);
                                                                    case "pain" -> h.setLimbPain(limb,value);
                                                                    case "infection" -> h.setLimbInfection(limb,value);
                                                                    case "fracturetimer" -> h.setLimbFracture(limb,value);
                                                                    case "dislocatedtimer" -> h.setLimbDislocation(limb,value);
                                                                    case "desinfectiontimer" -> h.setLimbDisinfected(limb,value);
                                                                    case "bleedrate" -> h.setLimbBleedRate(limb,value);
                                                                    default -> ctx.getSource().sendFailure(Component.literal("Unknown field: " + finalRaw));
                                                                }
                                                            });
                                                            ctx.getSource().sendSuccess(() ->
                                                                            Component.literal("Applied value " + value + " to " + limb +" | "+finalRaw+ " for " + target.getName().getString()),
                                                                    false);
                                                            return 1;
                                                        })
                                                )
                                        )
                                )
                        )
                )

                .then(Commands.literal("setbody")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("target", EntityArgument.player())
                                .then(Commands.argument("field", StringArgumentType.word())
                                        .suggests((ctx, builder) -> {
                                            // suggest all available fields
                                            builder.suggest("blood");
                                            builder.suggest("contiousness");
                                            builder.suggest("contiousnessCap");
                                            builder.suggest("hemothorax");
                                            builder.suggest("internalBleeding");
                                            builder.suggest("oxygen");
                                            builder.suggest("oxygenCap");
                                            builder.suggest("opioids");
                                            builder.suggest("bloodViscosity");
                                            builder.suggest("brainhealth");
                                            builder.suggest("drug_addiction");
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
                                                            case "contiousnesscap" -> h.setConsciousnessCap(value);
                                                            case "hemothorax" -> h.setHemothorax(value);
                                                            case "internalbleeding" -> h.setInternalBleeding(value);
                                                            case "oxygen" -> h.setOxygen(value);
                                                            case "oxygencap" -> h.setOxygenCap(value);
                                                            case "opioids" -> h.setPendingOpioids(value);
                                                            case "bloodviscosity" -> h.setBloodViscosity(value);
                                                            case "brainhealth" -> h.setBrainHealth(value);
                                                            case "drug_addiction" -> h.setDrug_addition(value);
                                                            case "dirtyness" -> h.setDirtyness(value);
                                                            case "painshock" -> h.setShock(value);
                                                            case "temperature" -> h.setTemperature(value);
                                                            case "lefteyeblind" -> h.setLeftEyeBlind(value > 0);
                                                            case "righteyeblind" -> h.setRightEyeBlind(value > 0);
                                                            case "mouthremoved" -> h.setMouthRemoved(value > 0);
                                                            default -> ctx.getSource().sendFailure(Component.literal("Unknown field: " + field));
                                                        }
                                                    });

                                                    ctx.getSource().sendSuccess(() ->
                                                                    Component.literal("Applied value " + value + " to " + field + " for " + target.getName().getString()),
                                                            false);
                                                    return 1;
                                                })
                                        )
                                )
                        )
                )

                .then(Commands.literal("amputate")
                        .requires(source ->source.hasPermission(2))
                        .then(Commands.argument("target", EntityArgument.player())
                                .then(Commands.argument("limb", StringArgumentType.word())
                                        .suggests(LIMBS)
                                        .executes(ctx -> {
                                            String raw = StringArgumentType.getString(ctx, "limb");
                                            Limb limb = Limb.valueOf(raw.toUpperCase());

                                            ServerPlayer target = EntityArgument.getPlayer(ctx, "target");

                                            target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h -> {
                                                h.dismember(limb);
                                                ctx.getSource().sendSuccess(() -> Component.literal("amputated "+raw),true);
                                            });

                                            return 1;
                                        })
                                )
                        )
                )

                .then(Commands.literal("fillfluid")
                        .requires(source->source.hasPermission(2))
                        .then(Commands.argument("fluid", ResourceLocationArgument.id())
                                .suggests(MED_FLUIDS)
                                .then(Commands.argument("amount", IntegerArgumentType.integer())
                                        .executes(ctx -> {
                                            ResourceLocation id = ResourceLocationArgument.getId(ctx,"fluid");
                                            ServerPlayer serverplayer = ctx.getSource().getPlayer();
                                            if (serverplayer == null){
                                                ctx.getSource().sendFailure(Component.literal("Can only Be run player"));
                                                return 1;
                                            }

                                            MedicalFluid mFluid = MedicalFluid.getFromId(id.toString());
                                            if (mFluid == null){
                                                ctx.getSource().sendFailure(Component.literal("invalid fluid"));
                                                return 1;
                                            }

                                            ItemStack itemStack = serverplayer.getItemInHand(InteractionHand.MAIN_HAND);
                                            if (itemStack.isEmpty() || !(itemStack.getItem() instanceof MultiTankFluidItem)){
                                                ctx.getSource().sendFailure(Component.literal("No compatible item found in main hand"));
                                                return 1;
                                            }

                                            int amount = IntegerArgumentType.getInteger(ctx,"amount");
                                            MultiTankHelper.addMedicalFluid(itemStack,amount,id.toString(),new FluidStack(ModFluids.SRC_MEDICAL.get().getSource(),1));
                                            ctx.getSource().sendSuccess(()->
                                                    Component.literal("Added " + amount + "mb of" + id + " to item"),true);
                                            return 1;
                                        })
                                )
                        )
                )
        );
    }
}
