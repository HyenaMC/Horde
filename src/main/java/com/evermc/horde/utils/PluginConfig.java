package com.evermc.horde.utils;

import com.evermc.horde.Horde;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class PluginConfig {

    protected final Path filePath;
    protected YamlConfigurationLoader loader;
    protected ConfigurationNode config;

    public PluginConfig(String fileName) {
        this.filePath = Horde.dataDirectory.resolve(fileName);
        load();
    }

    /**
     * Loads the configuration from the given file path. If the file does not exist, copies the default
     * configuration from the jar to the given location.
     *
     * @throws RuntimeException if there is an IO error while loading the configuration
     */
    public void load() {
        try {
            Files.createDirectories(filePath.getParent());
        } catch (IOException e) {
            Horde.logger.error("Could not create parent folders for: {}", filePath);
            throw new RuntimeException(e);
        }
        if(Files.notExists(filePath)) {
            try (InputStream in = getClass().getResourceAsStream("/" + filePath.getFileName().toString())) {
                if (in != null) {
                    Files.createDirectories(this.filePath.getParent());
                    try (OutputStream out = Files.newOutputStream(filePath)) {
                        in.transferTo(out);
                    }
                } else {
                    Files.createFile(this.filePath);
                }
            } catch (IOException e) {
                Horde.logger.error("Failed to create missing config file: {}!", filePath.getFileName().toString());
                throw new RuntimeException(e);
            }
        }

        try {
            loader = YamlConfigurationLoader.builder().path(filePath).build();
            config = loader.load();
        } catch (IOException e) {
            Horde.logger.error("Failed to load config file: {}!", filePath.getFileName().toString());
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the underlying configuration node for this config.
     * @return the configuration node
     */
    public ConfigurationNode getConfig() {
        return config;
    }

}
