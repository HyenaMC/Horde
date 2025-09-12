package com.evermc.horde.utils;

import com.evermc.horde.Horde;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.Map;

public class Localization extends PluginConfig {

    public Localization(String fileName) {
        super(fileName);
    }

    /**
     * Gets a string from the localization config given a path.
     * @param path the path to the string
     * @return the string at the given path
     */
    public String getString(String path) {
        return this.config.node(path).getString("<Missing localization:" + path + ">");
    }

    /**
     * Parses a string and replaces placeholders with values.
     * @param message the string to parse
     * @param placeholders the placeholder pairs to replace (key, value, key, value, ...)
     * @return the string with all placeholders replaced
     *
     * @throws IllegalArgumentException if placeholders is odd-length
     */
    public String parsePlaceholders(String message, String... placeholders) {
        if (placeholders.length % 2 != 0) {
            throw new IllegalArgumentException("Placeholders must be in pairs of key and value.");
        }
        for (int i = 0; i < placeholders.length; i += 2) {
            String key = placeholders[i];
            String value = placeholders[i + 1];
            message = message.replace("{" + key + "}", value);
        }
        return message;
    }

    /**
     * Localizes a string with placeholders.
     * @param path the path to the localization string
     * @param placeholders the placeholder pairs to replace (key, value, key, value, ...)
     * @return the localized string with all placeholders replaced
     *
     * @throws IllegalArgumentException if placeholders is odd-length
     */
    public String localizeString(String path, String... placeholders) {
        return parsePlaceholders(getString(path), placeholders);
    }

    /**
     * Localizes a string with placeholders and parses it with MiniMessage.
     * @param path the path to the localization string
     * @param placeholders the placeholder pairs to replace (key, value, key, value, ...)
     * @return the localized string with all placeholders replaced and parsed with MiniMessage
     *
     * @throws IllegalArgumentException if placeholders is odd-length
     */
    public Component localizeMiniMessage(String path, String... placeholders) {
        return MiniMessage.miniMessage().deserialize(localizeString(path, placeholders));
    }

    /**
     * Localizes a string with placeholders and parses it with legacy formatting codes (§)
     * @param path the path to the localization string
     * @param placeholders the placeholder pairs to replace (key, value, key, value, ...)
     * @return the localized string with all placeholders replaced and parsed with the legacy format
     *
     * @throws IllegalArgumentException if placeholders is odd-length
     */
    public Component localizeLegacy(String path, String... placeholders) {
        return LegacyComponentSerializer.legacySection().deserialize(localizeString(path, placeholders));
    }

    /**
     * Localizes a string with placeholders and parses it with the currently configured
     * localization format.
     * @param path the path to the localization string
     * @param placeholders the placeholder pairs to replace (key, value, key, value, ...)
     * @return the localized string with all placeholders replaced and parsed with the current
     *     localization format
     *
     * @throws IllegalArgumentException if placeholders is odd-length
     */
    public Component localize(String path, String... placeholders) {
        if(Horde.config.getConfig().node("enable-legacy-localization-formatting").getBoolean()) {
            return localizeLegacy(path, placeholders);
        } else {
            return localizeMiniMessage(path, placeholders);
        }
    }

    /**
     * Gets a map of values from the localization config given a path.
     * Generally used for name mappings and similar applications.
     * @param path the path to the map
     * @return the map at the given path
     */
    public Map<Object,? extends ConfigurationNode> getMap(String path) {
        return config.node(path).childrenMap();
    }

}
