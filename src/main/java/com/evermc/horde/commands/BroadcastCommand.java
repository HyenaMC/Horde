package com.evermc.horde.commands;

import com.evermc.horde.Horde;
import com.evermc.horde.utils.MessagingUtils;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

public class BroadcastCommand implements RawCommand {

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        if(!source.hasPermission("horde.command.broadcast")) {
            source.sendMessage(Horde.localization.localize("COMMAND_NO_PERMISSION"));
            return;
        }
        String message = invocation.arguments();
        if(!(source instanceof Player player)) {
            source.sendMessage(Horde.localization.localize("COMMAND_MUST_BE_PLAYER"));
            return;
        }
        if(message.isEmpty()) { //Set default broadcast
            Horde.preferences.setBoolean(true, player.getUniqueId().toString());
            source.sendMessage(Horde.localization.localize("BROADCAST_COMMAND_DEFAULT_SET"));
        } else { //Broadcast whatever after the root command
            String[] placeholders = MessagingUtils.prepareChatPlaceholders(player);
            Component broadcastTemplate = Horde.localization.localize("GLOBAL_CHAT_TEMPLATE", placeholders);
            Component typedMessage = MessagingUtils.deserializeChatMessage(message);
            Component output = broadcastTemplate.append(typedMessage);
            MessagingUtils.broadcastAsConfigured(player.getUniqueId(), output);
        }
    }

}
