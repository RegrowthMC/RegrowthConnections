package org.lushplugins.connections.user;

import org.lushplugins.connections.RegrowthConnections;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class UserCache extends org.lushplugins.connections.utils.UserCache<ConnectionsUser> {

    @Override
    protected CompletableFuture<ConnectionsUser> load(UUID uuid) {
        return RegrowthConnections.getInstance().getStorageManager().loadConnectionsUser(uuid);
    }
}
