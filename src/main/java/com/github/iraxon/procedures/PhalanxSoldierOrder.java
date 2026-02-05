package com.github.iraxon.procedures;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.iraxon.entity.DeepslateGolemEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

public sealed interface PhalanxSoldierOrder permits PhalanxSoldierOrder.MovementOrder, PhalanxSoldierOrder.FollowOrder {

    public static void follow_order(@Nonnull DeepslateGolemEntity soldier, PhalanxSoldierOrder order) {
        if (order instanceof MovementOrder mov) {
            // soldier.getNavigation().moveTo(null, 0)
        } else if (order instanceof FollowOrder flw) {

        }
    }

    public static @Nullable PhalanxSoldierOrder deserialize(CompoundTag tag) {
        if (tag == null) {
            return null;
        }

        final String type = tag.getString("order_type");
        return switch (type) {
            case "mov" -> new MovementOrder(
                    new Vec3(
                            tag.getDouble("target_x"),
                            tag.getDouble("target_y"),
                            tag.getDouble("target_z")),
                    new Vec3(
                            tag.getDouble("continue_x"),
                            tag.getDouble("continue_y"),
                            tag.getDouble("continue_z")));
            case "flw" -> new FollowOrder(
                    Objects.requireNonNull(tag.getString("target")),
                    tag.contains("last_known_x")
                            ? new Vec3(tag.getDouble("last_known_x"), tag.getDouble("last_known_y"),
                                    tag.getDouble("last_known_z"))
                            : null);
            default -> null;
        };
    }

    public static CompoundTag serialize(PhalanxSoldierOrder order) {
        CompoundTag tag = new CompoundTag();
        if (order instanceof MovementOrder movementOrder) {
            tag.putString("order_type", "mov");
            tag.putDouble("target_x", movementOrder.target.x);
            tag.putDouble("target_y", movementOrder.target.y);
            tag.putDouble("target_z", movementOrder.target.z);
            tag.putDouble("continue_x", movementOrder.continue_vector.x);
            tag.putDouble("continue_y", movementOrder.continue_vector.y);
            tag.putDouble("continue_z", movementOrder.continue_vector.z);
        } else if (order instanceof FollowOrder followOrder) {
            tag.putString("order_type", "flw");
            tag.putString("target", followOrder.target);
            @Nullable
            Vec3 v = followOrder.last_known_location();
            if (v != null) {
                tag.putDouble("last_known_x", v.x);
                tag.putDouble("last_known_y", v.y);
                tag.putDouble("last_known_z", v.z);
            }
        }
        return tag;
    }

    public record MovementOrder(@Nonnull Vec3 target, @Nonnull Vec3 continue_vector) implements PhalanxSoldierOrder {
    }

    public record FollowOrder(@Nonnull String target, @Nullable Vec3 last_known_location)
            implements PhalanxSoldierOrder {
    }

}
