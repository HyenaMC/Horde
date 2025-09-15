package com.evermc.horde;

import com.evermc.horde.commands.BroadcastCommand;
import com.evermc.horde.commands.HordeCommand;
import com.evermc.horde.commands.LocalCommand;
import com.evermc.horde.listeners.PlayerChatEventListener;
import com.evermc.horde.utils.DataConfig;
import com.evermc.horde.utils.Localization;
import com.evermc.horde.utils.PluginConfig;
import com.evermc.horde.utils.PrefixConfig;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(id = "horde", name = "Horde", version = "1.0.1",
        authors = {"kpAjun@HyenaMC"},
        url = "https://minecraft.teamhyena.org",
        description = "A light-weight chat plugin with minimal configurations.")
public class Horde {

    public static Logger logger;
    public static ProxyServer server;
    public static Path dataDirectory;
    public static CommandManager commandManager;
    public static EventManager eventManager;

    @Inject
    public Horde(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory,
                 CommandManager commandManager, EventManager eventManager) {
        Horde.server = server;
        Horde.logger = logger;
        Horde.dataDirectory = dataDirectory;
        Horde.commandManager = commandManager;
        Horde.eventManager = eventManager;
    }

    public static PluginConfig config;
    public static Localization localization;
    public static DataConfig preferences;
    public static PrefixConfig prefixes;
    public static String version;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {

        config = new PluginConfig("config.yml");
        localization = new Localization("localization.yml");
        preferences = new DataConfig("data/preferences.yml");
        prefixes = new PrefixConfig("prefix.yml");
        logger.info("[1/3] Configuration pre-fetch complete.");

        CommandMeta hordeMeta = commandManager.metaBuilder("horde").plugin(this).build();
        CommandMeta broadcastMeta = commandManager.metaBuilder("broadcast")
                .aliases("b", "bc").plugin(this).build();
        CommandMeta localMeta = commandManager.metaBuilder("local")
                .aliases("l", "lc").plugin(this).build();
        commandManager.register(hordeMeta, new HordeCommand());
        commandManager.register(broadcastMeta, new BroadcastCommand());
        commandManager.register(localMeta, new LocalCommand());
        logger.info("[2/3] Commands registration complete.");

        eventManager.register(this, new PlayerChatEventListener());
        logger.info("[3/3] Listeners initialization complete.");

        version = this.getClass().getAnnotation(Plugin.class).version();
        logger.info("Horde v{} is enabled.", version);

    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {

        for(DataConfig cfg : DataConfig.activeConfigs) {
            cfg.close();
        }
        logger.info("Horde v{} is shut down.", version);

    }

    /**
     * Reloads the configuration files.
     */
    public static void reload() {
        config.load();
        localization.load();
        prefixes.load();
    }

}
