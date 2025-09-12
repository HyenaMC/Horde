package com.evermc.horde.commands;

import com.evermc.horde.Horde;
import com.evermc.horde.utils.MessagingUtils;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

public class LocalCommand implements RawCommand {

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        if(!source.hasPermission("horde.command.local")) {
            source.sendMessage(Horde.localization.localize("COMMAND_NO_PERMISSION"));
            return;
        }
        String message = invocation.arguments();
        if(!(source instanceof Player player)) {
            source.sendMessage(Horde.localization.localize("COMMAND_MUST_BE_PLAYER"));
            return;
        }
        if(message.isEmpty()) { //Disable default broadcast
            Horde.preferences.setBoolean(false, player.getUniqueId().toString());
            source.sendMessage(Horde.localization.localize("LOCAL_COMMAND_DEFAULT_SET"));
        } else { //Locally send whatever after the root command
            String[] placeholders = MessagingUtils.prepareChatPlaceholders(player);
            Component broadcastTemplate = Horde.localization.localize("LOCAL_CHAT_TEMPLATE", placeholders);
            Component typedMessage = MessagingUtils.deserializeChatMessage(message);
            Component output = broadcastTemplate.append(typedMessage);
            MessagingUtils.sendLocallyAsConfigured(player.getUniqueId(),
                    player.getCurrentServer().orElseThrow().getServer(),  output);
        }
    }

}
