package org.lushplugins.connections;

import org.bukkit.plugin.java.JavaPlugin;

public final class RegrowthConnections extends JavaPlugin {
    private static RegrowthConnections plugin;

    @Override
    public void onLoad() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        // Enable implementation
    }

    @Override
    public void onDisable() {
        // Disable implementation
    }

    public static RegrowthConnections getInstance() {
        return plugin;
    }
}
