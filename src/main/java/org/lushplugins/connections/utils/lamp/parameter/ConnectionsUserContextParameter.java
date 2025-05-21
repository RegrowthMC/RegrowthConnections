package org.lushplugins.connections.utils.lamp.parameter;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.connections.RegrowthConnections;
import org.lushplugins.connections.user.ConnectionsUser;
import org.lushplugins.connections.utils.lamp.parameter.annotation.CachedUser;
import revxrsal.commands.Lamp;
import revxrsal.commands.annotation.list.AnnotationList;
import revxrsal.commands.autocomplete.AsyncSuggestionProvider;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.node.ExecutionContext;
import revxrsal.commands.parameter.ContextParameter;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ConnectionsUserContextParameter implements ContextParameter.Factory<CommandActor> {

    @SuppressWarnings("unchecked")
    @Override
    public @Nullable <T> ContextParameter<CommandActor, T> create(@NotNull Type parameterType, @NotNull AnnotationList annotations, @NotNull Lamp<CommandActor> lamp) {
        if (parameterType != ConnectionsUser.class) {
            return null;
        }

        CachedUser userAnnotation = annotations.get(CachedUser.class);
        if (userAnnotation == null) {
            return null;
        }

        return (parameter, context) -> (T) RegrowthConnections.getInstance().getUserCache().getCachedUser(context.actor().uniqueId());
    }

    public static class SuggestionProvider implements revxrsal.commands.autocomplete.SuggestionProvider<BukkitCommandActor>, AsyncSuggestionProvider<BukkitCommandActor> {

        @Override
        public @NotNull CompletableFuture<Collection<String>> getSuggestionsAsync(@NotNull ExecutionContext<BukkitCommandActor> context) {
            HashSet<String> usernames = Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .collect(Collectors.toCollection(HashSet::new));

            // TODO: Verify that the below methods calculate only the current argument
            if (context.input().hasRemaining()) {
                return RegrowthConnections.getInstance().getStorageManager().findSimilarUsernames(context.input().peekRemaining()).thenApply(offlineUsernames -> {
                    usernames.addAll(offlineUsernames);
                    return usernames;
                });
            } else {
                return CompletableFuture.completedFuture(usernames);
            }
        }

        @Override
        public @NotNull Collection<String> getSuggestions(@NotNull ExecutionContext<BukkitCommandActor> context) {
            return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .toList();
        }
    }
}
