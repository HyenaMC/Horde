package com.evermc.horde.utils;

import com.evermc.horde.Horde;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Vector;

public class DataConfig extends PluginConfig {

    public static Vector<DataConfig> activeConfigs = new Vector<>();

    public DataConfig(String fileName) {
        super(fileName);
        activeConfigs.add(this);
    }

    /**
     * Closes this config and saves any changes to disk.
     * <p>
     * If the config has not been modified (i.e., the "dirty" flag is false),
     * this method does nothing.
     */
    public void close() {
        try {
            loader.save(config);
        } catch (ConfigurateException e) {
            Horde.logger.error("Failed to save config file: {}!", filePath.getFileName().toString());
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets a boolean value at the given node path.
     * <p>
     * This method is intended for internal usage only and
     * assumes successful serialization. It will throw a RuntimeException
     * if an error occurs to help debugging.
     *
     * @param value the boolean value to set
     * @param path the node path to set
     * @throws RuntimeException if there is an error serializing the value
     */
    public void setBoolean(boolean value, Object... path) {
        try {
            this.config.node(path).set(Boolean.class, value);
        } catch (SerializationException e) {
            Horde.logger.error("Failed to serialize boolean value at node: {}!", (Object) path);
            throw new RuntimeException(e);
        }
    }

}

