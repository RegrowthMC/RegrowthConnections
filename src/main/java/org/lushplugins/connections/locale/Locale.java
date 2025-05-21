package org.lushplugins.connections.locale;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class Locale {
    private final HashMap<String, String> messages = new HashMap<>();

    public boolean hasMessage(@NotNull String key) {
        return this.messages.containsKey(key);
    }

    public @Nullable String getMessage(@NotNull String key) {
        return this.messages.get(key);
    }

    public void setMessage(@NotNull String key, @Nullable String value) {
        this.messages.put(key, value);
    }

    public void removeMessage(@NotNull String key) {
        this.messages.remove(key);
    }
}
