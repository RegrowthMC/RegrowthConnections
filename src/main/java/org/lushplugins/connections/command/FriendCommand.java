package org.lushplugins.connections.command;

import org.lushplugins.connections.RegrowthConnections;
import org.lushplugins.connections.config.Message;
import org.lushplugins.connections.user.ConnectionsUser;
import org.lushplugins.connections.utils.lamp.parameter.annotation.CachedUser;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.command.CommandActor;

@SuppressWarnings("unused")
@Command("friend")
public class FriendCommand {

    @Subcommand("request")
    public String request(CommandActor actor, @CachedUser ConnectionsUser user, ConnectionsUser target) {


        return "response message";
    }

    @Subcommand("reload")
    @CommandPermission("fairymagic.reload")
    public Message reload(CommandActor actor) {
        RegrowthConnections.getInstance().getConfigManager().reloadConfig();
        return Message.withKey("reloaded");
    }
}
