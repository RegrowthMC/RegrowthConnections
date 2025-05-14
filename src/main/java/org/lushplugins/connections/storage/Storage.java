package org.lushplugins.connections.storage;

import org.bukkit.configuration.ConfigurationSection;
import org.lushplugins.connections.user.ConnectionsUser;

import java.util.Collection;
import java.util.UUID;

public interface Storage {

    default void enable(ConfigurationSection config) {}

    default void disable() {}

    ConnectionsUser loadConnectionsUser(UUID uuid);

    void saveConnectionsUser(ConnectionsUser user);

    Collection<String> findSimilarUsernames(String input);
}
