package com.github.iraxon.procedures;

import java.util.Objects;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import com.github.iraxon.entity.DeepslateGolemEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class SoldierNBT {

    private static final String UNIT_COMMANDER_NBT_KEY = "phalanx_golem_commander";

    /**
     * Finds this unit's commander
     *
     * @param data
     * @param soldier
     * @return Commander entity or this unit itself if none found
     */
    public static DeepslateGolemEntity getCommander(DeepslateGolemEntity soldier) {

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

    @Nonnull
    public static SoldierType getType(DeepslateGolemEntity soldier) {
        return SoldierType.get(soldier.getPersistentData().getInt(SOLDIER_TYPE_KEY));
    }

    public static final String GOLEM_PLAYER_UUID_KEY = "phalanx_golem_player";

    public static void setPlayer(DeepslateGolemEntity soldier, @Nonnull Player p) {
        soldier.getPersistentData().putString(GOLEM_PLAYER_UUID_KEY, Objects.requireNonNull(p.getStringUUID()));
    }

    @Nonnull
    public static String getPlayerUUID(DeepslateGolemEntity soldier) {
        return Objects.requireNonNull(soldier.getPersistentData().getString(GOLEM_PLAYER_UUID_KEY));
    }

    public static boolean isNeutral(DeepslateGolemEntity soldier) {
        return soldier.getPersistentData().getString(GOLEM_PLAYER_UUID_KEY).isEmpty();
    }

    @Nonnull
    public static FormationStateNBTWrapper formationWrapper(DeepslateGolemEntity soldier) {
        return FormationStateNBTWrapper.of(Objects.requireNonNull(soldier.getPersistentData()));
    }

    private static final String TARGET_POSITION_KEY = "phalanx_golem_target_pos";

    public static void setMoveTarget(DeepslateGolemEntity soldier, @Nonnull Vec3 pos) {
        CompoundTag posTag = new CompoundTag();
        posTag.putDouble("x", pos.x);
        posTag.putDouble("y", pos.y);
        posTag.putDouble("z", pos.z);
        soldier.getPersistentData().put(TARGET_POSITION_KEY, posTag);
    }

    @Nonnull
    public static Vec3 getMoveTarget(DeepslateGolemEntity soldier) {

        final var data = soldier.getPersistentData();

        if (!data.contains(TARGET_POSITION_KEY)) {
            clearMoveTarget(soldier);
        }
        CompoundTag posTag = data.getCompound(TARGET_POSITION_KEY);
        return new Vec3(posTag.getDouble("x"), posTag.getDouble("y"), posTag.getDouble("z"));
    }

    public static void clearMoveTarget(DeepslateGolemEntity soldier) {
        setMoveTarget(soldier, Objects.requireNonNull(soldier.position()));
    }

    public static String manifest(DeepslateGolemEntity soldier) {
        return ("Commander: " + getCommander(soldier).getStringUUID() + "\n"
                + "Player: " + getPlayerUUID(soldier) + "\n"
                + "Type: " + getType(soldier) + "\n"
                + "Move Target: " + getMoveTarget(soldier) + "\n"
                + "---\n"
                + formationWrapper(soldier).manifest());
    }
}
