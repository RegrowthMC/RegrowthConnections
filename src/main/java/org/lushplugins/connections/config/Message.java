package org.lushplugins.connections.config;

import org.jetbrains.annotations.Nullable;
import org.lushplugins.connections.RegrowthConnections;

public class Message {
    private final String content;

    private Message(@Nullable String content) {
        this.content = content;
    }

    public boolean hasContent() {
        return content != null;
    }

    public @Nullable String content() {
        return content;
    }

    public static Message of(String content) {
        return new Message(content);
    }

    public static Message withKey(String key) {
        return new Message(RegrowthConnections.getInstance().getConfigManager().getMessage(key));
    }
}
