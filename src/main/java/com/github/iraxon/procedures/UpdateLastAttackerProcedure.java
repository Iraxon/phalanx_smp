package com.github.iraxon.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import com.github.iraxon.entity.DeepslateGolemEntity;
import com.github.iraxon.procedures.DeepslateGolemAIProcedure.SoldierAIUtils;

public class UpdateLastAttackerProcedure {
	public static boolean execute(LevelAccessor world, Entity entity, Entity sourceentity) {
		if (!(entity instanceof DeepslateGolemEntity golem && sourceentity instanceof LivingEntity attacker)) {
			return false;
		}
		if (SoldierAIUtils.isAllied(golem, attacker) && !(attacker instanceof Player)) {
			return false;
		} else {
			SoldierNBT.setLastAttackerUUID(golem, attacker);
			return true;
		}

	}
}
