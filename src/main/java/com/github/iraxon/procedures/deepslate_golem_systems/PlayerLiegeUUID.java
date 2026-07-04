package com.github.iraxon.procedures.deepslate_golem_systems;

import java.util.Optional;

import javax.annotation.Nonnull;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public class PlayerLiegeUUID {
    // Common NBT variable functionality
    private static final String KEY = "phalanx_soldier_player_liege_uuid";

    public static boolean set(@Nonnull Mob entity, @Nonnull String playerUUID) {
        if (isSet(entity)) {
            return false;
        } else {
            entity.getPersistentData().putString(KEY, playerUUID);
            return true;
        }
    }

    public static boolean reset(@Nonnull Mob entity) {
        final var result = isSet(entity);
        entity.getPersistentData().remove(KEY);
        return result;
    }

    public static Optional<String> get(@Nonnull Mob entity) {
        final var data = entity.getPersistentData();
        return isSet(entity) ? Optional.of(data.getString(KEY)) : Optional.empty();
    }

    public static boolean isSet(@Nonnull Mob entity) {
        return entity.getPersistentData().contains(KEY);
    }

    // More utilities

    @SuppressWarnings("null")
    public static boolean set(@Nonnull Mob entity, @Nonnull Player liege) {
        return set(entity, liege.getStringUUID());
    }

    @SuppressWarnings("null")
    public static boolean isLoyalTo(@Nonnull Mob entity, @Nonnull Player liege) {
        return isLoyalTo(entity, liege.getStringUUID());
    }

    public static boolean isLoyalTo(@Nonnull Mob entity, @Nonnull String liegeUUID) {
        return get(entity).filter(liegeUUID::equals).isPresent();
    }
}
