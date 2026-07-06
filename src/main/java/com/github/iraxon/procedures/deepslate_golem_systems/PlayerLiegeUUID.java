package com.github.iraxon.procedures.deepslate_golem_systems;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.world.entity.Entity;
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

    public static boolean tryProgramLoyalty(@Nullable Entity entity, @Nullable Entity player) {

        if (entity instanceof Mob m && player instanceof Player p) {

            if (!PlayerLiegeUUID.isSet(m)) {
                PlayerLiegeUUID.set(m, p);
                PhalanxUtils.sendMessage(p, "Loyalty programmed");
                return true;
            }

            if (!PlayerLiegeUUID.isLoyalTo(m, p)) {
                PhalanxUtils.sendMessage(p, "Not loyal to you");
            }

            // No message if already loyal
        }
        return false;
    }

    /**
     * Determine if the soldier should attack the target by the following rules:
     * 1. Never attack teammates.
     * 2. Attack hostile mobs.
     * 3. Attack mobs on a different team, if both soldier and target are on teams.
     *
     * @param soldier
     * @param target
     * @return
     */
    public static boolean areEnemies(Entity soldier, Entity target) {

        if (soldier == null || target == null) {
            return false;
        }

        if (soldier.isAlliedTo(target)) {
            return false;
        }
        if (PhalanxUtils.isTaggedAs(target, "phalanx:hostile")) {
            return true;
        }
        final Optional<String> soldierTeam = PhalanxUtils.getTeam(soldier);
        final Optional<String> targetTeam = PhalanxUtils.getTeam(target);
        if (soldierTeam.isPresent() && targetTeam.isPresent() && !soldierTeam.equals(targetTeam)) {
            return true;
        }
        // Commented out: Attack soldiers under different player
        // if (soldier instanceof Mob soldier_mob && target instanceof Mob target_mob
        //         && !PlayerLiegeUUID.get(soldier_mob).equals(PlayerLiegeUUID.get(target_mob))) {
        //     return true;
        // }
        return false;
    }
}
