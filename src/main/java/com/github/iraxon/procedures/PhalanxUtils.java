package com.github.iraxon.procedures;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class PhalanxUtils {

    /**
     * Stream of entities that satisfy predicate
     *
     * @param <T>
     * @param world
     * @param cls
     * @param center
     * @param size
     * @param predicate
     * @return
     */
    @SuppressWarnings("null")
    public static <T extends Entity> Stream<T> getEntitiesWithPredicate(
            @Nonnull LevelAccessor world,
            @Nonnull Class<T> cls,
            @Nonnull Vec3 center,
            double size,
            @Nonnull Predicate<? super T> predicate) {

        Objects.requireNonNull(world);
        Objects.requireNonNull(cls);
        Objects.requireNonNull(center);
        Objects.requireNonNull(predicate);

        return world.getEntitiesOfClass(cls, new AABB(center, center).inflate(size / 2), predicate).stream();
    }

    /**
     * Nearest entity that matches predicate
     *
     * @param <T>
     * @param world
     * @param cls
     * @param center
     * @param size
     * @param predicate
     * @return
     */
    @Nullable
    public static <T extends Entity> T getNearestEntityWithPredicate(
            @Nonnull LevelAccessor world,
            @Nonnull Class<T> cls,
            @Nonnull Vec3 center,
            double size,
            @Nonnull Predicate<? super T> predicate) {

        return getEntitiesWithPredicate(world, cls, center, size, predicate)
                .min(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(center))).orElse(null);
    }

    @Nullable
    /**
     * Entity from UUID
     *
     * @param <T>
     * @param world
     * @param cls
     * @param center
     * @param size
     * @param uuidString
     * @return
     */
    public static <T extends Entity> T getEntityByUUID(
            @Nonnull LevelAccessor world,
            @Nonnull Class<T> cls,
            @Nonnull Vec3 center,
            double size,
            String uuidString) {

        if (uuidString == null) {
            return null;
        }
        return getNearestEntityWithPredicate(world, cls, center, size,
                (T entity) -> entity.getStringUUID().equals(uuidString));
    }

    @SuppressWarnings("null")
    /**
     * Displays a message to the entity if it's a player
     * and this is being done from server side
     * @param recipient
     * @param msg
     * @param useActionBar Uses chat if false
     */
    public static void sendMessage(@Nullable Entity recipient, String msg, boolean useActionbar) {
        if (recipient instanceof Player player && !player.level().isClientSide())
            player.displayClientMessage(Component.literal(msg), useActionbar);
    }
}
