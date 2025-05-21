package org.lushplugins.connections.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.connections.RegrowthConnections;
import org.lushplugins.connections.locale.Locale;
import org.lushplugins.connections.menu.button.ConnectionTypeButton;
import org.lushplugins.connections.menu.button.ConnectionButton;
import org.lushplugins.lushlib.gui.button.Button;
import org.lushplugins.lushlib.gui.button.SimpleItemButton;
import org.lushplugins.lushlib.gui.button.type.NextPageButton;
import org.lushplugins.lushlib.gui.button.type.PreviousPageButton;
import org.lushplugins.lushlib.gui.inventory.GuiBlueprint;
import org.lushplugins.lushlib.gui.inventory.GuiLayer;
import org.lushplugins.lushlib.utils.DisplayItemStack;
import org.lushplugins.lushlib.utils.YamlUtils;
import org.lushplugins.lushlib.utils.converter.YamlConverter;

public class ConfigManager {
    private GuiBlueprint connectionsMenu;
    private GuiBlueprint connectionTypesMenu;
    private Locale locale;

    public ConfigManager() {
        RegrowthConnections.getInstance().saveDefaultConfig();
    }

    public void reloadConfig() {
        RegrowthConnections plugin = RegrowthConnections.getInstance();
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        ConfigurationSection connectionsMenuSection = config.getConfigurationSection("connections-menu");
        if (connectionsMenuSection != null) {
            GuiLayer layer = new GuiLayer(connectionsMenuSection.getStringList("format"));

            for (ConfigurationSection buttonSection : YamlUtils.getConfigurationSections(connectionsMenuSection, "buttons")) {
                Button button;
                String type = buttonSection.getString("type");
                DisplayItemStack item = YamlConverter.getDisplayItem(buttonSection);
                switch (type) {
                    case "connection" -> button = new ConnectionButton(item);
                    case "previous_page" -> button = new PreviousPageButton(item);
                    case "next_page" -> button = new NextPageButton(item);
                    case null, default -> {
                        switch (buttonSection.getName()) {
                            case "<" -> button = new PreviousPageButton(item);
                            case ">" -> button = new NextPageButton(item);
                            default -> button = new SimpleItemButton(item);
                        }
                    }
                }

                layer.setButton(buttonSection.getName().charAt(0), button);
            }

            this.connectionsMenu = new GuiBlueprint(
                connectionsMenuSection.getString("title"),
                layer
            );
        } else {
            this.connectionsMenu = null;
        }

        ConfigurationSection connectionTypesMenuSection = config.getConfigurationSection("connection-types-menu");
        if (connectionTypesMenuSection != null) {
            GuiLayer layer = new GuiLayer(connectionTypesMenuSection.getStringList("format"));

            for (ConfigurationSection buttonSection : YamlUtils.getConfigurationSections(connectionTypesMenuSection, "buttons")) {
                Button button;
                String type = buttonSection.getString("type");
                switch (type) {
                    case "connection_type" -> {
                        String categoryName = buttonSection.getString("connection-type");
                        DisplayItemStack item = YamlConverter.getDisplayItem(buttonSection);
                        button = new ConnectionTypeButton(categoryName, item);
                    }
                    case null, default -> {
                        DisplayItemStack item = YamlConverter.getDisplayItem(buttonSection);
                        button = new SimpleItemButton(item, (ignored) -> {});
                    }
                }

                layer.setButton(buttonSection.getName().charAt(0), button);
            }

            this.connectionTypesMenu = new GuiBlueprint(
                connectionTypesMenuSection.getString("title"),
                layer
            );
        } else {
            this.connectionTypesMenu = null;
        }

        this.locale = new Locale();
        ConfigurationSection messagesSection = config.getConfigurationSection("messages");
        if (messagesSection != null) {
            messagesSection.getValues(false).forEach((key, value) -> this.locale.setMessage(key, (String) value));
        }
    }

    public GuiBlueprint getConnectionsMenu() {
        return connectionsMenu;
    }

    public GuiBlueprint getConnectionTypesMenu() {
        return connectionTypesMenu;
    }

    public @Nullable String getMessage(String key) {
        return this.locale.getMessage(key);
    }
}
