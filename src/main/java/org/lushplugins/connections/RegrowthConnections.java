package org.lushplugins.connections;

import org.lushplugins.connections.command.FriendCommand;
import org.lushplugins.connections.config.Message;
import org.lushplugins.connections.storage.StorageManager;
import org.lushplugins.connections.user.ConnectionsUser;
import org.lushplugins.connections.user.UserCache;
import org.lushplugins.connections.utils.lamp.parameter.ConnectionsUserContextParameter;
import org.lushplugins.connections.utils.lamp.response.MessageResponseHandler;
import org.lushplugins.connections.utils.lamp.response.StringMessageResponseHandler;
import org.lushplugins.lushlib.libraries.jackson.databind.ObjectMapper;
import org.lushplugins.lushlib.plugin.SpigotPlugin;
import org.lushplugins.lushlib.serializer.JacksonHelper;
import revxrsal.commands.Lamp;
import revxrsal.commands.bukkit.BukkitLamp;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;

public final class RegrowthConnections extends SpigotPlugin {
    public static final ObjectMapper JACKSON_MAPPER = JacksonHelper.addCustomSerializers(new ObjectMapper());
    private static RegrowthConnections plugin;

    private UserCache userCache;
    private StorageManager storageManager;

    @Override
    public void onLoad() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        this.userCache = new UserCache();
        this.storageManager = new StorageManager();

        registerListener(new org.lushplugins.connections.utils.UserCache.Listener<>(this.userCache));

        Lamp<BukkitCommandActor> lamp = BukkitLamp.builder(this)
            .parameterTypes(parameters -> {
                parameters.addContextParameterFactory(new ConnectionsUserContextParameter());
            })
            .suggestionProviders(providers -> {
                providers.addProvider(ConnectionsUser.class, new ConnectionsUserContextParameter.SuggestionProvider());
            })
            .responseHandler(Message.class, new MessageResponseHandler())
            .responseHandler(String.class, new StringMessageResponseHandler())
            .build();

        lamp.register(new FriendCommand());
    }

    @Override
    public void onDisable() {
        // Disable implementation
    }

    public UserCache getUserManager() {
        return userCache;
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    public static RegrowthConnections getInstance() {
        return plugin;
    }
}
