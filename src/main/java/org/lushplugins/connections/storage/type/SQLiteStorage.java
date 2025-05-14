package org.lushplugins.connections.storage.type;

import org.bukkit.configuration.ConfigurationSection;
import org.lushplugins.connections.RegrowthConnections;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

public class SQLiteStorage extends AbstractSQLStorage {
    private static final String DATABASE_PATH = new File(RegrowthConnections.getInstance().getDataFolder(), "data.db").getAbsolutePath();

    @Override
    public void enable(ConfigurationSection config) {
        super.enable(config);
        runSqlFile("storage/sqlite_setup.sql");
    }

    @Override
    protected String getSaveConnectionsUserStatement() {
        return String.format("""
            INSERT INTO %s (uuid, username, incoming_requests, outgoing_requests, connections)
            VALUES (?, ?, ?, ?, ?)
            ON CONFLICT (uuid)
            DO UPDATE SET
                uuid = EXCLUDED.uuid,
                username = EXCLUDED.username,
                incoming_requests = EXCLUDED.incoming_requests,
                outgoing_requests = EXCLUDED.outgoing_requests,
                connections = EXCLUDED.connections;
            """, USER_TABLE);
    }

    @Override
    protected Connection conn() {
        try {
            return DriverManager.getConnection("jdbc:sqlite:" + DATABASE_PATH);
        } catch (SQLException e) {
            RegrowthConnections.getInstance().log(Level.SEVERE, "An error occurred whilst getting a connection: ", e);
            return null;
        }
    }

    @Override
    protected DataSource setupDataSource(ConfigurationSection config) {
        return null;
    }
}
