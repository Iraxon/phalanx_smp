package com.github.iraxon.procedures;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import com.github.iraxon.entity.DeepslateGolemEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class SoldierNBT {

    private static final String UNIT_COMMANDER_NBT_KEY = "phalanx_golem_commander";

    public static void setCommander(@Nonnull DeepslateGolemEntity soldier, @Nonnull DeepslateGolemEntity commander) {
        soldier.getPersistentData().putString(UNIT_COMMANDER_NBT_KEY,
                Objects.requireNonNull(commander.getStringUUID()));
    }

    /**
     * Finds this unit's commander
     *
     * @param data
     * @param soldier
     * @return Commander entity or this unit itself if none found
     */
    public static DeepslateGolemEntity getCommander(@Nonnull DeepslateGolemEntity soldier) {

        final var data = soldier.getPersistentData();

        if (data.getString(UNIT_COMMANDER_NBT_KEY).isEmpty()) {
            return soldier;
        }

        final String commanderUUID = data.getString(UNIT_COMMANDER_NBT_KEY);
        return PhalanxUtils.getEntityByUUID(Objects.requireNonNull(soldier.level()),
                DeepslateGolemEntity.class,
                Objects.requireNonNull(soldier.position()), 64, commanderUUID).orElse(soldier);
    }

    public static final String SOLDIER_TYPE_KEY = "phalanx_golem_type";

    public static enum SoldierType {
        COMMANDER(0),
        HEAVY_INFANTRY(1),
        SKIRMISHER(2);

        public final int index;

        private SoldierType(int index) {
            this.index = index;
        }

        @SuppressWarnings("null")
        @Nonnull
        public static SoldierType get(int index) {
            return Stream.of(values()).filter(v -> v.index == index).findAny().orElse(COMMANDER);
        }
    }

    public static void setType(@Nonnull DeepslateGolemEntity soldier, @Nonnull SoldierType t) {
        soldier.getPersistentData().putInt(SOLDIER_TYPE_KEY, t.index);
    }

    @Nonnull
    public static SoldierType getType(@Nonnull DeepslateGolemEntity soldier) {
        return SoldierType.get(soldier.getPersistentData().getInt(SOLDIER_TYPE_KEY));
    }

    public static final String GOLEM_PLAYER_UUID_KEY = "phalanx_golem_player";

    public static void setPlayer(@Nonnull DeepslateGolemEntity soldier, @Nonnull Player p) {
        soldier.getPersistentData().putString(GOLEM_PLAYER_UUID_KEY, Objects.requireNonNull(p.getStringUUID()));
    }

    @Nonnull
    public static String getPlayerUUID(@Nonnull DeepslateGolemEntity soldier) {
        return Objects.requireNonNull(soldier.getPersistentData().getString(GOLEM_PLAYER_UUID_KEY));
    }

    public static boolean isNeutral(@Nonnull DeepslateGolemEntity soldier) {
        return soldier.getPersistentData().getString(GOLEM_PLAYER_UUID_KEY).isEmpty();
    }

    @Nonnull
    public static FormationStateNBTWrapper formationWrapper(@Nonnull DeepslateGolemEntity soldier) {
        return FormationStateNBTWrapper.of(Objects.requireNonNull(soldier.getPersistentData()));
    }

    private static final String TARGET_POSITION_KEY = "phalanx_golem_target_pos";

    public static void setMoveTarget(@Nonnull DeepslateGolemEntity soldier, @Nonnull Vec3 pos) {
        CompoundTag posTag = new CompoundTag();
        posTag.putDouble("x", pos.x);
        posTag.putDouble("y", pos.y);
        posTag.putDouble("z", pos.z);
        soldier.getPersistentData().put(TARGET_POSITION_KEY, posTag);
    }

    @Nonnull
    public static Vec3 getMoveTarget(@Nonnull DeepslateGolemEntity soldier) {

        final var data = soldier.getPersistentData();

        if (!data.contains(TARGET_POSITION_KEY)) {
            clearMoveTarget(soldier);
        }
        CompoundTag posTag = data.getCompound(TARGET_POSITION_KEY);
        return new Vec3(posTag.getDouble("x"), posTag.getDouble("y"), posTag.getDouble("z"));
    }

    public static void clearMoveTarget(@Nonnull DeepslateGolemEntity soldier) {
        setMoveTarget(soldier, Objects.requireNonNull(soldier.position()));
    }

    private static final String LAST_ATTACKER_KEY = "phalanx_golem_last_attacker";

    private static final String LAST_ATTACKER_UUID_KEY = "attackerUUID";
    private static final String LAST_ATTACKER_EXPIRATION_TIMESTAMP_KEY = "timestamp";

    public static void setLastAttackerUUID(DeepslateGolemEntity subject, LivingEntity attacker) {
        final var lastAttackerinfo = new CompoundTag();
        lastAttackerinfo.putString(LAST_ATTACKER_UUID_KEY, Objects.requireNonNull(attacker.getStringUUID()));
        lastAttackerinfo.putLong(LAST_ATTACKER_EXPIRATION_TIMESTAMP_KEY, subject.level().getDayTime() + 3 * 60 * 20);
        subject.getPersistentData().put(LAST_ATTACKER_KEY, lastAttackerinfo);
    }

    /**
     * Last attacker, if the stored info is there and not expired
     *
     * @param subject
     * @return Empty String if info is absent or expired
     */
    @Nonnull
    public static String getLastAttackerUUID(@Nonnull DeepslateGolemEntity subject) {
        final var data = subject.getPersistentData();
        final var lastAttackerinfo = data.getCompound(LAST_ATTACKER_KEY);
        if (subject.level().getDayTime() < lastAttackerinfo.getLong(LAST_ATTACKER_EXPIRATION_TIMESTAMP_KEY)) {
            return Objects.requireNonNull(lastAttackerinfo.getString(LAST_ATTACKER_UUID_KEY));
        } else {
            data.remove(LAST_ATTACKER_KEY);
            return "";
        }
    }

    @Nonnull
    public static Optional<String> getLastAttackerUUIDOptional(@Nonnull DeepslateGolemEntity subject) {
        final var r = getLastAttackerUUID(subject);
        return Objects.requireNonNull(r.isEmpty() ? Optional.empty() : Optional.of(r));
    }

    public static String manifest(@Nonnull DeepslateGolemEntity soldier) {
        return ("Commander: " + getCommander(soldier).getStringUUID() + "\n"
                + "Player: " + getPlayerUUID(soldier) + "\n"
                + "Type: " + getType(soldier) + "\n"
                + "Move Target: " + getMoveTarget(soldier) + "\n"
                + "Last Attacker: " + getLastAttackerUUIDOptional(soldier).orElse("None") + "\n"
                + "---\n"
                + formationWrapper(soldier).manifest());
    }
}
