package org.lushplugins.connections.storage.type;

import org.bukkit.configuration.ConfigurationSection;
import org.lushplugins.connections.RegrowthConnections;
import org.lushplugins.connections.storage.Storage;
import org.lushplugins.connections.user.ConnectionsUser;
import org.lushplugins.lushlib.libraries.jackson.core.JsonProcessingException;
import org.lushplugins.lushlib.libraries.jackson.core.type.TypeReference;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public abstract class AbstractSQLStorage implements Storage {
    protected static final String USER_TABLE = "connections_users";

    private DataSource dataSource;

    @Override
    public void enable(ConfigurationSection config) {
        this.dataSource = setupDataSource(config);
        testDataSourceConnection();
    }

    @Override
    public ConnectionsUser loadConnectionsUser(UUID uuid) {
        try (Connection conn = conn();
             PreparedStatement stmt = conn.prepareStatement(String.format("""
                 SELECT *
                 FROM %s
                 WHERE uuid = ?;
                 """, USER_TABLE))
        ) {
            stmt.setString(1, uuid.toString());

            ResultSet results = stmt.executeQuery();
            if (results.next()) {
                List<UUID> incomingRequests;
                List<UUID> outgoingRequests;
                Map<UUID, ConnectionsUser.Connection> connections;
                try {
                    incomingRequests = RegrowthConnections.JACKSON_MAPPER.readValue(results.getString("incoming_requests"), new TypeReference<>() {});
                    outgoingRequests = RegrowthConnections.JACKSON_MAPPER.readValue(results.getString("outgoing_requests"), new TypeReference<>() {});
                    connections = RegrowthConnections.JACKSON_MAPPER.readValue(results.getString("connections"), new TypeReference<>() {});
                } catch (JsonProcessingException e) {
                    RegrowthConnections.getInstance().getLogger().log(Level.SEVERE, "Failed to load user's connections data: ", e);
                    return null;
                }

                return new ConnectionsUser(
                    uuid,
                    results.getString("username"),
                    incomingRequests,
                    outgoingRequests,
                    connections
                );
            } else {
                return new ConnectionsUser(uuid);
            }
        } catch (SQLException e) {
            RegrowthConnections.getInstance().getLogger().log(Level.SEVERE, "Failed to load user's connections data: ", e);
        }

        return null;
    }

    @Override
    public void saveConnectionsUser(ConnectionsUser user) {
        try (Connection conn = conn();
             PreparedStatement stmt = conn.prepareStatement(this.getSaveConnectionsUserStatement())
        ) {
            String incomingRequests;
            String outgoingRequests;
            String connections;
            try {
                incomingRequests = RegrowthConnections.JACKSON_MAPPER.writeValueAsString(user.getIncomingRequests());
                outgoingRequests = RegrowthConnections.JACKSON_MAPPER.writeValueAsString(user.getOutgoingRequests());
                connections = RegrowthConnections.JACKSON_MAPPER.writeValueAsString(user.getConnectionsMap());
            } catch (JsonProcessingException e) {
                RegrowthConnections.getInstance().getLogger().log(Level.SEVERE, "Failed to save parse user's magic data.: ", e);
                return;
            }

            stmt.setString(1, user.getUniqueId().toString());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, incomingRequests);
            stmt.setString(4, outgoingRequests);
            stmt.setString(5, connections);

            stmt.execute();
        } catch (SQLException e) {
            RegrowthConnections.getInstance().getLogger().log(Level.SEVERE, "Failed to save user's magic data: ", e);
        }
    }

    @Override
    public Collection<String> findSimilarUsernames(String input) {
        try (Connection conn = conn();
             PreparedStatement stmt = conn.prepareStatement(String.format("""
                 SELECT username
                 FROM %s
                 WHERE username LIKE CONCAT(?, '%%')
                 LIMIT 50;
                 """, USER_TABLE))
        ) {
            stmt.setString(1, input);

            List<String> usernames = new ArrayList<>();
            ResultSet results = stmt.executeQuery();
            while (results.next()) {
                usernames.add(results.getString("username"));
            }

            return usernames;
        } catch (SQLException e) {
            RegrowthConnections.getInstance().getLogger().log(Level.SEVERE, "Failed to load user's connections data: ", e);
        }

        return null;
    }

    protected abstract String getSaveConnectionsUserStatement();

    protected Connection conn() {
        try {
            return getDataSource().getConnection();
        } catch (SQLException e) {
            RegrowthConnections.getInstance().log(Level.SEVERE, "An error occurred whilst getting a connection: ", e);
            return null;
        }
    }

    protected DataSource getDataSource() {
        return dataSource;
    }

    protected abstract DataSource setupDataSource(ConfigurationSection config);

    protected void runSqlFile(String filePath) {
        String setup;
        try (InputStream in = AbstractSQLStorage.class.getClassLoader().getResourceAsStream(filePath)) {
            setup = new BufferedReader(new InputStreamReader(in)).lines().collect(Collectors.joining(""));
        } catch (IOException e) {
            RegrowthConnections.getInstance().getLogger().log(Level.SEVERE, "Could not read db setup file.", e);
            e.printStackTrace();
            return;
        }

        String[] statements = setup.split("\\|");
        for (String statement : statements) {
            try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(statement)) {
                stmt.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    protected void testDataSourceConnection() {
        try (Connection conn = conn()) {
            if (!conn.isValid(1000)) {
                throw new SQLException("Could not establish database connection.");
            }
        } catch (SQLException e) {
            RegrowthConnections.getInstance().log(Level.SEVERE, "An error occurred whilst testing the data source ", e);
        }
    }
}
