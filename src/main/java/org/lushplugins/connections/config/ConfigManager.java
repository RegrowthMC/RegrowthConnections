package org.lushplugins.connections.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.connections.RegrowthConnections;
import org.lushplugins.connections.locale.Locale;
import org.lushplugins.connections.menu.button.CategoryButton;
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
    private GuiBlueprint categoryMenu;
    private GuiBlueprint categoriesMenu;
    private Locale locale;

    public ConfigManager() {
        RegrowthConnections.getInstance().saveDefaultConfig();
    }

    public void reloadConfig() {
        RegrowthConnections plugin = RegrowthConnections.getInstance();
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        ConfigurationSection categoryMenuSection = config.getConfigurationSection("category-menu");
        if (categoryMenuSection != null) {
            GuiLayer layer = new GuiLayer(categoryMenuSection.getStringList("format"));

            for (ConfigurationSection buttonSection : YamlUtils.getConfigurationSections(categoryMenuSection, "buttons")) {
                Button button;
                String type = buttonSection.getString("type");
                DisplayItemStack item = YamlConverter.getDisplayItem(buttonSection);
                switch (type) {
                    case "listed_member" -> button = new ConnectionButton(item);
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

            this.categoryMenu = new GuiBlueprint(
                categoryMenuSection.getString("title"),
                layer
            );
        } else {
            this.categoryMenu = null;
        }

        ConfigurationSection categoriesMenuSection = config.getConfigurationSection("categories-menu");
        if (categoriesMenuSection != null) {
            GuiLayer layer = new GuiLayer(categoriesMenuSection.getStringList("format"));

            for (ConfigurationSection buttonSection : YamlUtils.getConfigurationSections(categoriesMenuSection, "buttons")) {
                Button button;
                String type = buttonSection.getString("type");
                switch (type) {
                    case "category" -> {
                        String categoryName = buttonSection.getString("category");
                        DisplayItemStack item = YamlConverter.getDisplayItem(buttonSection);
                        button = new CategoryButton(categoryName, item);
                    }
                    case null, default -> {
                        DisplayItemStack item = YamlConverter.getDisplayItem(buttonSection);
                        button = new SimpleItemButton(item, (ignored) -> {});
                    }
                }

                layer.setButton(buttonSection.getName().charAt(0), button);
            }

            this.categoriesMenu = new GuiBlueprint(
                categoriesMenuSection.getString("title"),
                layer
            );
        } else {
            this.categoriesMenu = null;
        }

        this.locale = new Locale();
        ConfigurationSection messagesSection = config.getConfigurationSection("messages");
        if (messagesSection != null) {
            messagesSection.getValues(false).forEach((key, value) -> this.locale.setMessage(key, (String) value));
        }
    }

    public GuiBlueprint getCategoryMenu() {
        return categoryMenu;
    }

    public GuiBlueprint getCategoriesMenu() {
        return categoriesMenu;
    }

    public @Nullable String getMessage(String key) {
        return this.locale.getMessage(key);
    }
}
