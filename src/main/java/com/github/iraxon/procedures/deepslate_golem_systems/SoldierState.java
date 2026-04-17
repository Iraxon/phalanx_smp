package com.github.iraxon.procedures.deepslate_golem_systems;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.iraxon.PhalanxSmpMod;
import com.github.iraxon.entity.DeepslateGolemEntity;
import com.github.iraxon.procedures.PhalanxUtils;
import com.github.iraxon.procedures.PhalanxUtils.GenericNBTStoredVariable;
import static com.github.iraxon.procedures.PhalanxUtils.NBTStringStoredVariable;
import com.github.iraxon.procedures.PhalanxUtils.Vec3NBT;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

/**
 * This is the home of low-level methods
 * to micromanage the troops. They are meant
 * to be called as part of more complicated
 * orders.
 */
public class SoldierState {

    @SuppressWarnings("null")
    public static final GenericNBTStoredVariable<Mob, String, String> playerUUID = NBTStringStoredVariable(
            "phalanx_soldier_player_liege_uuid",
            Function.identity(),
            Function.identity());

    private static final String KEY_SOLDIER_TYPE = "phalanx_soldier_type";

    public static void setType(Mob soldier, SoldierType type) {
        if (soldier == null) {
            return;
        }
        if (getTypeOptional(soldier).isPresent()) {
            PhalanxSmpMod.LOGGER.error("Attempted to give soldier type " + type + " to soldier with existing type "
                    + getType(soldier) + ", UUID " + soldier.getStringUUID());
            return;
        }
        soldier.getPersistentData().putString(KEY_SOLDIER_TYPE,
                type == null ? SoldierType.DEFAULT.encode() : type.encode());
    }

    public static Optional<SoldierType> getTypeOptional(@Nonnull Mob soldier) {
        return SoldierType.decodeOptional(soldier.getPersistentData().getString(KEY_SOLDIER_TYPE));
    }

    public static SoldierType getType(@Nonnull Mob soldier) {
        return SoldierType.decode(soldier.getPersistentData().getString(KEY_SOLDIER_TYPE));
    }

    private static final String KEY_STATE_TYPE = "phalanx_soldier_state_type";
    private static final String KEY_STATE_PAYLOAD = "phalanx_soldier_state_payload";

    private static final String STATE_TYPE_STRING_MOVE = "move";
    private static final String STATE_TYPE_STRING_FOLLOW = "follow";
    private static final String STATE_TYPE_STRING_HALT = "halt";

    private static final String KEY_MOVE_STATE_MOVE_POSITION = "movePosition";
    private static final String KEY_MOVE_STATE_LOOK_VECTOR = "lookVector";

    private static final String KEY_FOLLOW_STATE_TARGET = "target";

    public static void soldierMove(Mob soldier, @Nullable Vec3 moveTarget, @Nullable Vec3 lookVector) {
        if (soldier == null) {
            return;
        }
        final var data = soldier.getPersistentData();
        data.putString(KEY_STATE_TYPE, STATE_TYPE_STRING_MOVE);
        var payload = new CompoundTag();
        Vec3NBT.storeVec3(payload, KEY_MOVE_STATE_MOVE_POSITION, moveTarget);
        Vec3NBT.storeVec3(payload, KEY_MOVE_STATE_LOOK_VECTOR, lookVector);
        data.put(KEY_STATE_PAYLOAD, payload);
    }

    public static void soldierFollow(Mob soldier, LivingEntity followTarget) {
        if (soldier == null) {
            return;
        }
        final var data = soldier.getPersistentData();
        data.putString(KEY_STATE_TYPE, STATE_TYPE_STRING_FOLLOW);
        var payload = new CompoundTag();
        payload.putString(KEY_FOLLOW_STATE_TARGET, Objects.requireNonNull(followTarget.getStringUUID()));
        data.put(KEY_STATE_PAYLOAD, payload);
    }

    public static void soldierHalt(Mob soldier) {
        if (soldier == null) {
            return;
        }
        final var data = soldier.getPersistentData();
        data.putString(KEY_STATE_TYPE, STATE_TYPE_STRING_HALT);
        data.remove(KEY_STATE_PAYLOAD);
    }

    public static void soldierTick(Mob soldier) {
        if (soldier == null) {
            return;
        }
        final var data = soldier.getPersistentData();
        final var typeString = data.getString(KEY_STATE_TYPE);

        switch (typeString) {
            case STATE_TYPE_STRING_MOVE -> {
                final var payload = Objects.requireNonNull(data.getCompound(KEY_STATE_PAYLOAD));
                standAtTick(soldier, Vec3NBT.retrieveVec3(payload, KEY_MOVE_STATE_MOVE_POSITION),
                        Vec3NBT.retrieveVec3(payload, KEY_MOVE_STATE_LOOK_VECTOR));
            }
            case STATE_TYPE_STRING_FOLLOW ->
                PhalanxUtils.getEntityByUUID(
                        Objects.requireNonNull(soldier.level()),
                        LivingEntity.class,
                        Objects.requireNonNull(soldier.position()), 64,
                        data.getCompound(KEY_STATE_PAYLOAD).getString(KEY_FOLLOW_STATE_TARGET))
                        .ifPresent(target -> followEntityTick(soldier, target));

            // default is "halt"
            default -> standAtTick(soldier, null, null);
        }
    }

    private static void standAtTick(Mob soldier, @Nullable Vec3 movePosition, @Nullable Vec3 lookVector) {

        if (soldier == null) {
            return;
        }

        final boolean atMovePosition;

        if (movePosition == null) {
            soldier.getNavigation().stop();
            atMovePosition = true;
        } else {
            atMovePosition = movePosition.subtract(Objects.requireNonNull(soldier.position()))
                    .horizontalDistanceSqr() < 0.75 * 0.75;
            soldier.getNavigation().moveTo(movePosition.x, movePosition.y, movePosition.z, 1.0);
        }

        if (atMovePosition && lookVector != null) {
            soldier.lookAt(EntityAnchorArgument.Anchor.EYES,
                    Objects.requireNonNull(soldier.getEyePosition().add(lookVector)));
        }
    }

    private static void followEntityTick(Mob soldier, Entity target) {
        if (soldier == null || target == null) {
            return;
        }
        @SuppressWarnings("null")
        // Following the target at a distance of 0.5
        final var movePosition = target.position()
                .add(soldier.position().subtract(target.position()).normalize().scale(0.5));
        soldier.getNavigation().moveTo(movePosition.x, movePosition.y, movePosition.z, 1.3);
    }

    private static void attackEntity(Mob soldier, LivingEntity target) {
        if (soldier == null || target == null) {
            return;
        }
        followEntityTick(soldier, target);
        soldier.setTarget(target);
    }

    public static String manifest(DeepslateGolemEntity e) {
        if (e == null) {
            return "Null entity";
        }
        final var data = e.getPersistentData();
        return "\n" + data.getString(KEY_STATE_TYPE) + "\n" + data.getCompound(KEY_STATE_PAYLOAD).toString();
    }
}
