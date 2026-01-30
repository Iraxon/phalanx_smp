package com.github.iraxon.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import java.util.Comparator;
import java.util.Objects;

import javax.annotation.Nullable;

public class EntityFromUUIDProcedure {

	/**
	 * Range is a 64x64x64 box
	 *
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param uuid
	 * @return
	 */
	public static @Nullable Entity execute(LevelAccessor world, double x, double y, double z, String uuid) {
		Objects.requireNonNull(world);
		Objects.requireNonNull(uuid);

		final Vec3 center = new Vec3(x, y, z);
		return world.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(64 / 2d),
				entity -> entity.getStringUUID().equals(uuid)).stream()
				.min(Comparator.comparingDouble(entity -> entity.distanceToSqr(center))).orElse(null);
	}
}
