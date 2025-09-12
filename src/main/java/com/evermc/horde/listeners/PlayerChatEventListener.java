package com.evermc.horde.listeners;

import com.evermc.horde.Horde;
import com.evermc.horde.utils.MessagingUtils;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;

@SuppressWarnings("deprecation") // Velocity marked all setResult() as deprecated to warn about a misuse
public class PlayerChatEventListener {

    @Subscribe
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        event.setResult(PlayerChatEvent.ChatResult.message("")); // DENY will kick new clients
        String typedMessage = event.getMessage();
        if(Horde.preferences.getConfig().node(player.getUniqueId().toString()).
                getBoolean(Horde.config.getConfig().node("default-global-chat").getBoolean()) &&
                    player.hasPermission("horde.command.broadcast")) { // Player has global chat enabled
            String[] placeholders = MessagingUtils.prepareChatPlaceholders(player);
            var broadcastTemplate = Horde.localization.localize("GLOBAL_CHAT_TEMPLATE", placeholders);
            var messageComponent = MessagingUtils.deserializeChatMessage(typedMessage);
            var output = broadcastTemplate.append(messageComponent);
            MessagingUtils.broadcastAsConfigured(player.getUniqueId(), output);
        } else if (!Horde.preferences.getConfig().node(player.getUniqueId().toString()).
                getBoolean(Horde.config.getConfig().node("default-global-chat").getBoolean()) &&
                    player.hasPermission("horde.command.local")) { // Player has local chat enabled
            String[] placeholders = MessagingUtils.prepareChatPlaceholders(player);
            var broadcastTemplate = Horde.localization.localize("LOCAL_CHAT_TEMPLATE", placeholders);
            var messageComponent = MessagingUtils.deserializeChatMessage(typedMessage);
            var output = broadcastTemplate.append(messageComponent);
            MessagingUtils.sendLocallyAsConfigured(player.getUniqueId(),
                    player.getCurrentServer().orElseThrow().getServer(), output);
        } else { // Player has no permission to chat
            player.sendMessage(Horde.localization.localize("CHAT_NO_PERMISSION"));
        }
    }

}
