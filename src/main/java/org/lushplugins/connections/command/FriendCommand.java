package org.lushplugins.connections.command;

import org.lushplugins.connections.user.ConnectionsUser;
import org.lushplugins.connections.utils.lamp.parameter.annotation.CachedUser;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.command.CommandActor;

@Command("friend")
public class FriendCommand {

    @Subcommand("request")
    public String request(CommandActor actor, @CachedUser ConnectionsUser user, ConnectionsUser target) {
        // TODO: Create Lamp parameter parser for ConnectionsUser supporting both online and offline players

        return "Test";
    }
}
