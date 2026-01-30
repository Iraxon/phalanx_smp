package com.github.iraxon.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class IsPlayerInProtectedLandProcedure {
	public static boolean execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return false;
		String owner = "";
		if (!DoesClaimExistProcedure.execute(world, entity)) {
			return false;
		}
		owner = GetLandownerProcedure.execute(world, entity);
		if ((owner).isEmpty() || IsOnlineProcedure.execute(world, x, y, z, owner)) {
			return false;
		}
		return true;
	}
}