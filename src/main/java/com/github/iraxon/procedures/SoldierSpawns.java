package com.github.iraxon.procedures;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.iraxon.entity.DeepslateGolemEntity;
import com.github.iraxon.init.PhalanxSmpModEntities;
import com.github.iraxon.procedures.SoldierNBT.SoldierType;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;

public class SoldierSpawns {

    public static Optional<DeepslateGolemEntity> spawnCommander(@Nonnull ServerLevel world,
            @Nonnull BlockPos position) {
        return _spawn(world, position, SoldierType.COMMANDER, null);
    }

    public static Optional<DeepslateGolemEntity> spawn(@Nonnull ServerLevel world, @Nonnull BlockPos position,
            @Nonnull SoldierType type,
            @Nonnull DeepslateGolemEntity commander) {
        return _spawn(world, position, type, commander);
    }

    private static Optional<DeepslateGolemEntity> _spawn(@Nonnull ServerLevel world, @Nonnull BlockPos position,
            @Nonnull SoldierType type,
            @Nullable DeepslateGolemEntity commander) {

        final var rVal = golem(world, position);
        rVal.ifPresent(
                (@Nonnull DeepslateGolemEntity golem) -> {
                    @Nonnull
                    final var true_commander = (commander == null || type.equals(SoldierType.COMMANDER)) ? golem
                            : commander;
                    SoldierNBT.setType(golem, type);
                    SoldierNBT.setCommander(golem, true_commander);
                });
        return rVal;
    }

    @Nonnull
    private static Optional<DeepslateGolemEntity> golem(@Nonnull ServerLevel world, @Nonnull BlockPos position) {
        return Objects.requireNonNull(Optional.ofNullable(
                PhalanxSmpModEntities.DEEPSLATE_GOLEM.get().spawn(world, position, MobSpawnType.MOB_SUMMONED)));
    }

}
