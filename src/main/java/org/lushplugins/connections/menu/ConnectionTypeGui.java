package org.lushplugins.connections.menu;

import org.bukkit.entity.Player;
import org.lushplugins.connections.RegrowthConnections;
import org.lushplugins.connections.menu.button.ConnectionButton;
import org.lushplugins.connections.user.ConnectionsUser;
import org.lushplugins.lushlib.gui.inventory.GuiLayer;
import org.lushplugins.lushlib.gui.inventory.PagedGui;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class ConnectionTypeGui extends PagedGui {
    private final String connectionType;

    public ConnectionTypeGui(String connectionType, List<GuiLayer> layers, String title, Player player) {
        super(layers, title, player);
        this.connectionType = connectionType;
    }

    @Override
    public void refresh() {
        super.refresh();

        ConnectionsUser user = RegrowthConnections.getInstance().getUserCache().getCachedUser(this.getPlayer().getUniqueId());
        if (user == null) {
            return;
        }

        ArrayDeque<UUID> connections = user.getConnectionsMap().entrySet().stream()
            .filter(connection -> this.connectionType.equals(connection.getValue().connectionType()))
            .map(Map.Entry::getKey)
            .sorted()
            .collect(Collectors.toCollection(ArrayDeque::new));

        // TODO
        this.getButtons().forEach((slot, button) -> {
            if (button instanceof ConnectionButton connection) {
                UUID target = connections.pop();
            }
        });
    }
}
