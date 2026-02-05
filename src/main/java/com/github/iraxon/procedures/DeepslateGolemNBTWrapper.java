package com.github.iraxon.procedures;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

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
    public static DeepslateGolemNBTWrapper of(DeepslateGolemEntity golem) {

        final String uuid = golem.getStringUUID();

        return Objects.requireNonNullElseGet(cache.get(uuid), () -> {
            final var rVal = new DeepslateGolemNBTWrapper(golem, golem.getPersistentData());
            cache.put(uuid, rVal);
            return rVal;
        });
    }

    @Nullable
    /**
     * Finds this unit's commander
     *
     * @param data
     * @param golem
     * @return Commander entity or null if none found
     */
    public DeepslateGolemEntity getCommander() {

        final String UNIT_COMMANDER_NBT_KEY = "phalanx_golem_commander";

        if (data().getString(UNIT_COMMANDER_NBT_KEY).equals("")) {
            return golem;
        }
        final String commanderUUID = data().getString(UNIT_COMMANDER_NBT_KEY);
        return PhalanxUtils.getEntityByUUID(Objects.requireNonNull(golem.level()),
                DeepslateGolemEntity.class,
                Objects.requireNonNull(golem.position()), 64, commanderUUID);
    }

    @Nonnull
    public String type() {
        return Objects.requireNonNull(data.getString("phalanx_golem_type"));

    }

    @Nonnull
    public String playerUUID() {
        return Objects.requireNonNull(data.getString("phalanx_golem_player"));
    }
}
