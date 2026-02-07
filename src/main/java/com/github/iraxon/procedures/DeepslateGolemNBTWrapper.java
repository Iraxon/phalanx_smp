package com.github.iraxon.procedures;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.iraxon.entity.DeepslateGolemEntity;

import net.minecraft.nbt.CompoundTag;

public record DeepslateGolemNBTWrapper(@Nonnull DeepslateGolemEntity golem, @Nonnull CompoundTag data) {

    @Nonnull
    private static ConcurrentHashMap<String, DeepslateGolemNBTWrapper> cache = new ConcurrentHashMap<>();

    @SuppressWarnings("null")
    @Nonnull
    /**
     * Please use instead of constructor
     *
     * @param golem
     * @return
     */
    public static DeepslateGolemNBTWrapper of(@Nonnull DeepslateGolemEntity golem) {

        final String uuid = Objects.requireNonNull(golem).getStringUUID();

        return cache.computeIfAbsent(uuid,
                (String u) -> new DeepslateGolemNBTWrapper(golem, golem.getPersistentData()));
    }

    private static final String UNIT_COMMANDER_NBT_KEY = "phalanx_golem_commander";

    @Nullable
    /**
     * Finds this unit's commander
     *
     * @param data
     * @param golem
     * @return Commander entity or this unit itself if none found
     */
    public DeepslateGolemEntity getCommander() {

        if (data().getString(UNIT_COMMANDER_NBT_KEY).equals("")) {
            return golem;
        }
        final String commanderUUID = data().getString(UNIT_COMMANDER_NBT_KEY);
        return PhalanxUtils.getEntityByUUID(Objects.requireNonNull(golem.level()),
                DeepslateGolemEntity.class,
                Objects.requireNonNull(golem.position()), 64, commanderUUID);
    }

    public static final String GOLEM_TYPE_KEY = "phalanx_golem_type";

    public static enum GolemType {
        COMMANDER(0),
        HEAVY_INFANTRY(1),
        SKIRMISHER(2);

        public final int index;

        private GolemType(int index) {
            this.index = index;
        }

        @SuppressWarnings("null")
        @Nonnull
        public static GolemType get(int index) {
            return Stream.of(values()).filter(v -> v.index == index).findAny().orElse(COMMANDER);
        }
    }

    @Nonnull
    public GolemType type() {
        return GolemType.get(data.getInt(GOLEM_TYPE_KEY));
    }

    public static final String GOLEM_PLAYER_UUID_KEY = "phalanx_golem_player";

    @Nonnull
    public String playerUUID() {
        return Objects.requireNonNull(data.getString(GOLEM_PLAYER_UUID_KEY));
    }

    @Nonnull
    public FormationStateNBTWrapper formationWrapper() {
        return FormationStateNBTWrapper.of(data);
    }
}
