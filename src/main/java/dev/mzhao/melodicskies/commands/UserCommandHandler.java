package dev.mzhao.melodicskies.commands;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.var;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserCommandHandler extends CommandBase {
    public static final UserCommandHandler instance = new UserCommandHandler();

    Object2ObjectMap<String, Consumer<String[]>> subcommands = new Object2ObjectOpenHashMap<>();

    public void registerSubcommand(String subcommand, Consumer<String[]> lambda) {
        if (subcommands.put(subcommand, lambda) != null) {
            throw new IllegalArgumentException("Duplicate subcommands not allowed. This indicates a bug.");
        }
    }

    @Override
    public String getCommandName() {
        return "melodicskies";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        var subcommand = subcommands.get(args[0]);
        if (subcommand == null)
            throw new IllegalArgumentException("Subcommand " + args[0] + " not found.");
        subcommand.accept(args);
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("ms", "melodic", "skies");
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1)
            return getListOfStringsMatchingLastWord(args, subcommands.keySet());
        return Collections.emptyList();
    }
}
