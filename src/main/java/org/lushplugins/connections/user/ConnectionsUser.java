package org.lushplugins.connections.user;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ConnectionsUser {
    private final UUID uuid;
    private final String username;
    private final List<UUID> incomingRequests;
    private final List<UUID> outgoingRequests;
    private final Map<UUID, Connection> connections;

    public ConnectionsUser(
        @NotNull UUID uuid,
        @Nullable String username,
        @NotNull List<UUID> incomingRequests,
        @NotNull List<UUID> outgoingRequests,
        @NotNull Map<UUID, Connection> connections
    ) {
        this.uuid = uuid;
        this.username = username;
        this.incomingRequests = incomingRequests;
        this.outgoingRequests = outgoingRequests;
        this.connections = connections;
    }

    public ConnectionsUser(@NotNull UUID uuid, @Nullable String username) {
        this(uuid, username, new ArrayList<>(), new ArrayList<>(), new HashMap<>());
    }

    public ConnectionsUser(@NotNull UUID uuid) {
        this(uuid, null);
    }

    public @NotNull UUID getUniqueId() {
        return this.uuid;
    }

    public @Nullable String getUsername() {
        return this.username;
    }

    public @NotNull List<UUID> getIncomingRequests() {
        return this.incomingRequests;
    }

    public @NotNull List<UUID> getOutgoingRequests() {
        return outgoingRequests;
    }

    public boolean hasConnectionWith(UUID uuid) {
        return this.connections.containsKey(uuid);
    }

    public @Nullable Connection getConnectionWith(UUID uuid) {
        return this.connections.get(uuid);
    }

    public Map<UUID, Connection> getConnectionsMap() {
        return this.connections;
    }

    public record Connection(String connectionType) {}
}
