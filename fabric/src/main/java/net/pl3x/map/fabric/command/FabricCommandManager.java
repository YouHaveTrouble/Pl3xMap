package net.pl3x.map.fabric.command;

import cloud.commandframework.Command;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.fabric.FabricServerCommandManager;
import io.leangen.geantyref.TypeToken;
import net.minecraft.commands.arguments.DimensionArgument;
import net.pl3x.map.core.command.CommandHandler;
import net.pl3x.map.core.command.Sender;
import net.pl3x.map.core.command.argument.parser.WorldParser;
import org.checkerframework.checker.nullness.qual.NonNull;

public class FabricCommandManager implements CommandHandler {
    private final FabricServerCommandManager<@NonNull Sender> manager;
    private final Command.Builder<@NonNull Sender> root;

    public FabricCommandManager() {
        this.manager = new FabricServerCommandManager<>(CommandExecutionCoordinator.simpleCoordinator(), FabricSender::create, Sender::getSender);

        var brigadier = getManager().brigadierManager();
        brigadier.setNativeNumberSuggestions(false);
        brigadier.registerMapping(new TypeToken<WorldParser<Sender>>() {
        }, builder -> builder.toConstant(DimensionArgument.dimension()).cloudSuggestions());

        setupExceptionHandlers();

        this.root = buildRoot();
        getManager().command(getRoot());
        registerSubcommands();
    }

    @Override
    public @NonNull FabricServerCommandManager<@NonNull Sender> getManager() {
        return this.manager;
    }

    @Override
    public Command.@NonNull Builder<@NonNull Sender> getRoot() {
        return this.root;
    }
}