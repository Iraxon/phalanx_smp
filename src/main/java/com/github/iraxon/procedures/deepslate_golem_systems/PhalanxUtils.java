package com.github.iraxon.procedures.deepslate_golem_systems;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.logging.log4j.util.TriConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
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
    public static <T extends Entity> Optional<T> getNearestEntityWithPredicate(
            @Nonnull LevelAccessor world,
            @Nonnull Class<T> cls,
            @Nonnull Vec3 center,
            double size,
            @Nonnull Predicate<? super T> predicate) {

        return getEntitiesWithPredicate(world, cls, center, size, predicate)
                .min(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(center)));
    }

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
    public static <T extends Entity> Optional<T> getEntityByUUID(
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

    /**
     * Displays an actionbar message to the entity if it's a player
     * and this is being done from server side
     *
     * @param recipient
     * @param msg
     */
    public static void sendMessage(@Nullable Entity recipient, @Nonnull String msg) {
        sendMessage(recipient, msg, true);
    }

    /**
     * Displays a message to the entity if it's a player
     * and this is being done from server side
     *
     * @param recipient
     * @param msg
     * @param useActionBar Uses chat if false
     */
    @SuppressWarnings("null")
    public static void sendMessage(@Nullable Entity recipient, @Nonnull String msg, boolean useActionbar) {
        if (recipient instanceof Player player && !player.level().isClientSide())
            player.displayClientMessage(Component.literal(msg), useActionbar);
    }

    @SuppressWarnings("null")
    @Nonnull
    public static GameType getGameMode(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            return serverPlayer.gameMode.getGameModeForPlayer();
        } else if (player.level().isClientSide()) {
            var playerInfo = Minecraft.getInstance().getConnection().getPlayerInfo(player.getGameProfile().getId());
            if (playerInfo != null)
                return playerInfo.getGameMode();
        }
        throw new RuntimeException("Player was neither ServerPlayer nor client player: " + player.getName());
    }

    public static class Vec3NBT {

        public static void storeVec3(CompoundTag nbt, String key, @Nullable Vec3 vec3) {
            if (nbt == null || key == null) {
                return;
            }
            if (vec3 == null) {
                nbt.remove(key);
                return;
            }

            var storedVector = new CompoundTag();
            storedVector.putDouble("x", vec3.x);
            storedVector.putDouble("y", vec3.y);
            storedVector.putDouble("z", vec3.z);
            nbt.put(key, storedVector);
        }

        @Nullable
        public static Vec3 retrieveVec3(@Nonnull CompoundTag nbt, @Nonnull String key) {
            Objects.requireNonNull(nbt);
            Objects.requireNonNull(key);

            if (nbt.contains(key)) {
                final var storedVector = nbt.getCompound(key);
                return new Vec3(
                        storedVector.getDouble("x"), storedVector.getDouble("y"), storedVector.getDouble("z"));
            }
            return null;
        }

    }

    public static class NBTVariableWrapper<E extends Entity, T> {
        @Nonnull
        private final String KEY;
        private final TriConsumer<CompoundTag, String, T> NBTSetter;
        private final BiFunction<CompoundTag, String, T> NBTGetter;

        public NBTVariableWrapper(
                @Nonnull String KEY,
                TriConsumer<CompoundTag, String, T> NBTSetter,
                BiFunction<CompoundTag, String, T> NBTGetter) {

            this.KEY = KEY;
            this.NBTSetter = NBTSetter;
            this.NBTGetter = NBTGetter;
        }

        public boolean set(@Nonnull E mob, T value) {
            if (isSet(mob)) {
                return false;
            } else {
                NBTSetter.accept(mob.getPersistentData(), KEY, value);
                return true;
            }
        }

        public Optional<T> get(@Nonnull E mob) {
            return isSet(mob) ? Optional.of(NBTGetter.apply(mob.getPersistentData(), KEY)) : Optional.empty();
        }

        public boolean reset(@Nonnull E mob) {
            final var result = isSet(mob);
            mob.getPersistentData().remove(KEY);
            return result;
        }

        public boolean isSet(@Nonnull E mob) {
            return mob.getPersistentData().contains(KEY);
        }
    }

    @SuppressWarnings("null")
    public static void setItemInHand(LivingEntity entity, InteractionHand hand, ItemStack itemstack) {
        if (entity == null || hand == null || itemstack == null)
            return;
        final var stackCopy = itemstack.copy();
        entity.setItemInHand(hand, stackCopy);
        if (entity instanceof Player p) {
            p.getInventory().setChanged();
        }
    }

    public static void setItemInHand(LivingEntity entity, InteractionHand hand, int count, Item item) {
        if (entity == null || hand == null || item == null)
            return;
        entity.setItemInHand(hand, new ItemStack(item, count));
    }

    public static void spawnItem(
            @Nonnull LevelAccessor world,
            @Nonnull Vec3 pos,
            @Nonnull Supplier<ItemStack> itemstackSupplier) {

        if (world instanceof ServerLevel serverWorld) {
            @SuppressWarnings("null")
            final var toSpawn = new ItemEntity(serverWorld, pos.x, pos.y, pos.z, itemstackSupplier.get());
            toSpawn.setPickUpDelay(10);
            serverWorld.addFreshEntity(toSpawn);
        }
    }
}
