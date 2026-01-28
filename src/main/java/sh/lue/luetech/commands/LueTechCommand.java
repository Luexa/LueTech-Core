package sh.lue.luetech.commands;

import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.UuidArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import sh.lue.luetech.LueTech;

import java.math.BigInteger;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@EventBusSubscriber(modid = LueTech.MOD_ID)
public class LueTechCommand {
    private static final Component NEWLINE = Component.literal("\n");
    private static final Component DOUBLE_NEWLINE = Component.literal("\n\n");
    private static final Component NETWORK_ENTRY = Component.literal("Network: ")
            .withStyle(ChatFormatting.AQUA);
    private static final Component PLAYER_UUID_ENTRY = Component.literal("Owner: ")
            .withStyle(ChatFormatting.AQUA);
    private static final Component TEAM_UUID_ENTRY = Component.literal("Team: ")
            .withStyle(ChatFormatting.AQUA);
    private static final Component STORED_POWER_ENTRY = Component.literal("Stored Power: ")
            .withStyle(ChatFormatting.AQUA);
    private static final Component MAX_POWER_ENTRY = Component.literal("Max Power: ")
            .withStyle(ChatFormatting.AQUA);
    private static final Component ACTIVE_ENTRY = Component.literal("Active: ")
            .withStyle(ChatFormatting.AQUA);

    @SubscribeEvent
    static void onRegisterCommands(RegisterCommandsEvent event) {
        var dispatcher = event.getDispatcher();
        dispatcher.register(
                Commands.literal("luetech")
                        .then(Commands.literal("beacon")
                                .requires(src -> src.hasPermission(2))
                                .then(Commands.literal("list")
                                        .executes(LueTechCommand::listBeaconNetworks))
                                .then(beaconSetSubcommand()))
                        .then(Commands.literal("unique_machine")
                                .requires(src -> src.hasPermission(2))
                                .then(Commands.literal("dump")
                                        .executes(LueTechCommand::dumpUniqueMachines))));
    }

    private static ArgumentBuilder<CommandSourceStack, ?> beaconSetSubcommand() {
        return Commands.literal("set")
                .then(Commands.argument("network", UuidArgument.uuid())
                        .suggests(LueTechCommand::suggestNetworks)
                        .then(Commands.literal("max_power")
                                .then(Commands.argument("value", BigIntegerArgumentType
                                        .bigIntegerMin(BigInteger.ZERO))
                                        .executes(LueTechCommand::setBeaconMaxPower)))
                        .then(Commands.literal("stored_power")
                                .then(Commands.argument("value", BigIntegerArgumentType
                                        .bigIntegerMin(BigInteger.ZERO))
                                        .executes(LueTechCommand::setBeaconStoredPower))));
    }

    private static CompletableFuture<Suggestions> suggestNetworks(CommandContext<CommandSourceStack> ctx,
                                                                  SuggestionsBuilder builder) {
        var input = builder.getRemainingLowerCase();
        LueTech.savedData.dominanceBeacon.allNetworks()
                .stream()
                .map(network -> network.getUUID().toString())
                .filter(s -> s.startsWith(input))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }

    private static int listBeaconNetworks(CommandContext<CommandSourceStack> ctx) {
        var source = ctx.getSource();
        if (LueTech.savedData == null) {
            source.sendFailure(Component.literal("Saved Data is not available."));
            return 0;
        }
        var networks = LueTech.savedData.dominanceBeacon.allNetworks();
        if (networks.isEmpty()) {
            source.sendSuccess(
                    () -> Component.literal("No networks found in Saved Data.")
                            .withStyle(ChatFormatting.AQUA),
                    false);
            return 1;
        }
        Supplier<Component> supplyListing = () -> {
            MutableComponent listing = Component.literal("");
            boolean firstNetwork = true;
            for (var network : networks) {
                var uuid = network.getUUID();
                var playerUUID = network.getPlayerUUID();
                var teamUUID = network.getTeamUUID();
                var storedPower = network.getStoredPower();
                var maxPower = network.getMaxPower();
                var active = network.getActive();
                if (!firstNetwork) {
                    listing.append(DOUBLE_NEWLINE);
                }
                firstNetwork = false;
                listing
                        .append(NETWORK_ENTRY)
                        .append(Component.literal(uuid.toString()).withStyle(ChatFormatting.GOLD))
                        .append(NEWLINE)
                        .append(PLAYER_UUID_ENTRY)
                        .append(Component.literal(playerUUID.toString()).withStyle(ChatFormatting.GOLD))
                        .append(NEWLINE)
                        .append(TEAM_UUID_ENTRY)
                        .append(Component.literal(teamUUID == null ? "N/A" : teamUUID.toString())
                                .withStyle(ChatFormatting.GOLD))
                        .append(NEWLINE)
                        .append(STORED_POWER_ENTRY)
                        .append(Component.literal(FormattingUtil.formatNumbers(storedPower))
                                .withStyle(ChatFormatting.GOLD))
                        .append(NEWLINE)
                        .append(MAX_POWER_ENTRY)
                        .append(Component.literal(FormattingUtil.formatNumbers(maxPower))
                                .withStyle(ChatFormatting.GOLD))
                        .append(NEWLINE)
                        .append(ACTIVE_ENTRY)
                        .append(Component.literal(active ? "true" : "false").withStyle(ChatFormatting.GOLD));
            }
            return listing;
        };
        source.sendSuccess(supplyListing, true);
        return 1;
    }

    private static int setBeaconMaxPower(CommandContext<CommandSourceStack> ctx) {
        UUID networkUUID = ctx.getArgument("network", UUID.class);
        BigInteger newValue = ctx.getArgument("value", BigInteger.class);
        var source = ctx.getSource();

        var network = LueTech.savedData.dominanceBeacon.getNetwork(networkUUID);
        if (network == null) {
            source.sendFailure(Component.literal("No network found with specified UUID")
                    .withStyle(ChatFormatting.RED));
            return 0;
        }

        network.setMaxPower(newValue);
        source.sendSuccess(() -> Component.literal("Network max power successfully updated"), true);
        return 1;
    }

    private static int setBeaconStoredPower(CommandContext<CommandSourceStack> ctx) {
        UUID networkUUID = ctx.getArgument("network", UUID.class);
        BigInteger newValue = ctx.getArgument("value", BigInteger.class);
        var source = ctx.getSource();

        var network = LueTech.savedData.dominanceBeacon.getNetwork(networkUUID);
        if (network == null) {
            source.sendFailure(Component.literal("No network found with specified UUID")
                    .withStyle(ChatFormatting.RED));
            return 0;
        }

        network.setStoredPower(newValue);
        source.sendSuccess(() -> Component.literal("Network stored power successfully updated"), true);
        return 1;
    }

    private static int dumpUniqueMachines(CommandContext<CommandSourceStack> ctx) {
        var source = ctx.getSource();
        MutableComponent dump = Component.literal("").withStyle(ChatFormatting.AQUA);
        boolean firstPlayerEntry = true;
        for (var playerEntry : LueTech.savedData.getPlayerActivatedMachines().entrySet()) {
            var playerUUID = playerEntry.getKey();
            var ownedMachines = playerEntry.getValue();
            if (ownedMachines.isEmpty()) continue;
            if (!firstPlayerEntry) {
                dump.append(DOUBLE_NEWLINE);
            }
            firstPlayerEntry = false;
            dump.append(Component.literal("[Player "));
            dump.append(Component.literal(playerUUID.toString()).withStyle(ChatFormatting.GOLD));
            dump.append("]");
            for (var machineEntry : ownedMachines.entrySet()) {
                var multiblockType = machineEntry.getKey();
                var machineUUID = machineEntry.getValue();
                dump.append("\n  ");
                dump.append(Component.literal(multiblockType.toString()).withStyle(ChatFormatting.RED));
                dump.append(" ");
                dump.append(Component.literal(machineUUID.toString()).withStyle(ChatFormatting.YELLOW));
            }
        }
        for (var teamEntry : LueTech.savedData.getTeamPlayerDelegates().entrySet()) {
            var teamUUID = teamEntry.getKey();
            var teamDelegates = teamEntry.getValue();
            if (teamDelegates.isEmpty()) continue;
            if (!firstPlayerEntry) {
                dump.append(DOUBLE_NEWLINE);
            }
            firstPlayerEntry = false;
            dump.append(Component.literal("[Team "));
            dump.append(Component.literal(teamUUID.toString()).withStyle(ChatFormatting.GOLD));
            dump.append("]");
            for (var delegateEntry : teamDelegates.entrySet()) {
                var multiblockType = delegateEntry.getKey();
                var playerUUID = delegateEntry.getValue();
                dump.append("\n  ");
                dump.append(Component.literal(multiblockType.toString()).withStyle(ChatFormatting.RED));
                dump.append(" ");
                dump.append(Component.literal(playerUUID.toString()).withStyle(ChatFormatting.YELLOW));
            }
        }
        if (firstPlayerEntry) {
            source.sendSuccess(() -> Component.literal("No unique machines in Saved Data")
                    .withStyle(ChatFormatting.AQUA), false);
        } else {
            source.sendSuccess(() -> dump, true);
        }
        return 1;
    }
}
