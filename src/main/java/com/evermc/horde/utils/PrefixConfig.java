package com.evermc.horde.utils;

import com.velocitypowered.api.proxy.Player;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.Map;

public class PrefixConfig extends PluginConfig{

    public PrefixConfig(String fileName) {
        super(fileName);
    }

    /**
     * Process a message and replace all placeholders that match the given player's permissions.
     * <p>
     * The message is processed as follows:
     * <ul>
     *     <li>Each prefix is iterated over (in order of appearance in the configuration file)</li>
     *     <li>The key of the prefix is added to the output array as a placeholder</li>
     *     <li>The value of the prefix is iterated over (in order of appearance in the configuration file)</li>
     *     <li>If the player has the permission of the value, the value is added to the output array as a value</li>
     *     <li>If the player does not have any of the permissions, the value is set to an empty string</li>
     * </ul>
     * <p>
     * The output array is of the form {placeholder1, value1, placeholder2, value2, ...}
     *
     * @param player the player to check permissions for
     * @return the processed message with all placeholders replaced
     */
    public String[] processPermissionPrefix(Player player) {
        String[] output = new String[config.childrenMap().size() * 2]; // Each prefix can have a placeholder and a value
        int i = 0;
        for(Map.Entry<Object, ? extends ConfigurationNode> prefix : config.childrenMap().entrySet()) {
            output[i++] = prefix.getKey().toString();
            for(ConfigurationNode permission : prefix.getValue().childrenList()) {
                if(player.hasPermission(permission.node("permission").getString())) {
                    output[i++] = permission.node("prefix").getString();
                    break; // Only use the first matching permission
                }
            }
            if(i % 2 != 0) { // If no permission matched, set the value to an empty string
                output[i++] = "";
            }
        }
        return output;
    }

}
