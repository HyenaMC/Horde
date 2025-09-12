package com.evermc.horde.commands;

import com.evermc.horde.Horde;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HordeCommand implements SimpleCommand {

    @Override
    public void execute(final Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if(args.length == 0 || args[0].equals("help")) {
            if(source instanceof Player player) {
                String uuid = player.getUniqueId().toString();
                boolean defaultMode = Horde.config.getConfig().node("default-global-chat").getBoolean();
                source.sendMessage(Horde.localization.localize("HORDE_COMMAND_HELP",
                        "horde_version", Horde.version,
                        "default_broadcast", String.valueOf(Horde.preferences.getConfig().node(uuid).getBoolean(defaultMode)).toUpperCase()));
            } else {
                source.sendMessage(Horde.localization.localize("HORDE_COMMAND_HELP_CONSOLE",
                        "horde_version", Horde.version));
            }
        } else if(args[0].equals("reload")) {
            if(source.hasPermission("horde.command.reload")) {
                Horde.reload();
                source.sendMessage(Horde.localization.localize("HORDE_COMMAND_RELOAD_SUCCESS"));
            } else {
                source.sendMessage(Horde.localization.localize("COMMAND_NO_PERMISSION"));
            }
        } else {
            source.sendMessage(Horde.localization.localize("COMMAND_UNKNOWN_SUBCOMMAND",
                    "command", invocation.alias()));
        }
    }

    /**
     * Suggests subcommands for the horde command.
     * <p>
     * If the user has not provided any arguments, this will suggest "help" and "reload" as subcommands.
     *
     * @param invocation the command invocation
     * @return a list of suggestions
     */
    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        List<String> suggestions = new ArrayList<>();
        if(invocation.arguments().length == 0) {
            suggestions.add("help");
            suggestions.add("reload");
        }
        return CompletableFuture.completedFuture(suggestions);
    }

}
