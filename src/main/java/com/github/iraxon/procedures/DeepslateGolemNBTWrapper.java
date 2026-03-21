package com.github.iraxon.procedures;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import com.github.iraxon.entity.DeepslateGolemEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public record DeepslateGolemNBTWrapper(@Nonnull DeepslateGolemEntity golem, @Nonnull CompoundTag data) {

    @Nonnull
    private static final ConcurrentHashMap<String, DeepslateGolemNBTWrapper> serverCache = new ConcurrentHashMap<>();

    @Nonnull
    private static final ConcurrentHashMap<String, DeepslateGolemNBTWrapper> clientCache = new ConcurrentHashMap<>();

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

        return (golem.level().isClientSide ? clientCache : serverCache).computeIfAbsent(uuid,
                (String u) -> new DeepslateGolemNBTWrapper(golem, golem.getPersistentData()));
    }

    /**
     * SETTERS AND GETTERS
     */

    private static final String UNIT_COMMANDER_NBT_KEY = "phalanx_golem_commander";

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
                Objects.requireNonNull(golem.position()), 64, commanderUUID).orElse(this.golem);
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
    public GolemType getType() {
        return GolemType.get(data.getInt(GOLEM_TYPE_KEY));
    }

    public static final String GOLEM_PLAYER_UUID_KEY = "phalanx_golem_player";

    public void setPlayer(@Nonnull Player p) {
        data.putString(GOLEM_PLAYER_UUID_KEY, Objects.requireNonNull(p.getStringUUID()));
    }

    @Nonnull
    public String getPlayerUUID() {
        return Objects.requireNonNull(data.getString(GOLEM_PLAYER_UUID_KEY));
    }

    public boolean isNeutral() {
        return data.getString(GOLEM_PLAYER_UUID_KEY).isEmpty();
    }

    @Nonnull
    public FormationStateNBTWrapper formationWrapper() {
        return FormationStateNBTWrapper.of(data);
    }

    private static final String TARGET_POSITION_KEY = "phalanx_golem_target_pos";

    public void setMoveTarget(@Nonnull Vec3 pos) {
        CompoundTag posTag = new CompoundTag();
        posTag.putDouble("x", pos.x);
        posTag.putDouble("y", pos.y);
        posTag.putDouble("z", pos.z);
        data.put(TARGET_POSITION_KEY, posTag);
    }

    @Nonnull
    public Vec3 getMoveTarget() {
        if (!data.contains(GOLEM_PLAYER_UUID_KEY)) {
            clearMoveTarget();
        }
        CompoundTag posTag = data.getCompound(TARGET_POSITION_KEY);
        return new Vec3(posTag.getDouble("x"), posTag.getDouble("y"), posTag.getDouble("z"));
    }

    public void clearMoveTarget() {
        setMoveTarget(Objects.requireNonNull(this.golem.position()));
    }

    public String manifest() {
        return ("Commander: " + getCommander().getStringUUID() + "\n"
                + "Player: " + getPlayerUUID() + "\n"
                + "Type: " + getType() + "\n"
                + "Move Target: " + getMoveTarget() + "\n"
                + "---\n"
                + formationWrapper().manifest());
    }
}
