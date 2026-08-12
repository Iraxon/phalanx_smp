package com.github.iraxon.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class UpdateLastAttackerProcedure {
	public static boolean execute(LevelAccessor world, Entity entity, Entity sourceentity) {
		// if (!(entity instanceof DeepslateGolemEntity golem && sourceentity instanceof LivingEntity attacker)) {
		// 	return false;
		// }
		// if (SoldierAIUtils.isAllied(golem, attacker) && !(attacker instanceof Player)) {
		// 	return false;
		// } else {
		// 	SoldierNBT.setLastAttackerUUID(golem, attacker);
		// 	return true;
		// }

		return true;
	}
}
