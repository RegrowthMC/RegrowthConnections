package org.lushplugins.connections.menu.button;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.connections.RegrowthConnections;
import org.lushplugins.connections.menu.ConnectionTypeGui;
import org.lushplugins.connections.user.ConnectionsUser;
import org.lushplugins.lushlib.gui.button.SimpleItemButton;
import org.lushplugins.lushlib.gui.inventory.GuiBlueprint;
import org.lushplugins.lushlib.utils.DisplayItemStack;

public class CategoryButton extends SimpleItemButton {
    private final String connectionType;

    public CategoryButton(String category, DisplayItemStack item) {
        super(item, (event) -> {
            GuiBlueprint blueprint = RegrowthConnections.getInstance().getConfigManager().getCategoryMenu();

            ConnectionTypeGui gui = new ConnectionTypeGui(blueprint.getLayers(), blueprint.getTitle(), (Player) event.getWhoClicked());
            gui.open();
        });

        this.connectionType = category;
    }

    @Override
    public ItemStack getItemStack(@Nullable Player player) {
        return DisplayItemStack.builder(this.getItem())
            .replace("%amount%", () -> {
                if (player == null) {
                    return "%amount%";
                }

                ConnectionsUser user = RegrowthConnections.getInstance().getUserCache().getCachedUser(player.getUniqueId());
                if (user == null) {
                    return "0";
                }

                return String.valueOf(user.getConnectionsMap().values().stream()
                    .filter(connection -> connection.connectionType().equals(this.connectionType))
                    .count());
            })
            .build()
            .asItemStack();
    }
}
