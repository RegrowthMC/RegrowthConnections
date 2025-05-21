package org.lushplugins.connections.utils.lamp.response;

import org.lushplugins.connections.locale.Message;
import org.lushplugins.lushlib.libraries.chatcolor.ChatColorHandler;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.node.ExecutionContext;
import revxrsal.commands.response.ResponseHandler;

public class MessageResponseHandler implements ResponseHandler<BukkitCommandActor, Message> {

    @Override
    public void handleResponse(Message message, ExecutionContext<BukkitCommandActor> context) {
        if (message.hasContent()) {
            ChatColorHandler.sendMessage(context.actor().sender(), message.content());
        }
    }
}
