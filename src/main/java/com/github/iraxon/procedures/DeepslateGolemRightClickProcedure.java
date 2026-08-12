package com.github.iraxon.procedures;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionResult;

import com.github.iraxon.procedures.deepslate_golem_systems.SoldierItemInteractions;

public class DeepslateGolemRightClickProcedure {
	public static InteractionResult execute(Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return InteractionResult.PASS;
		if (entity instanceof Mob && sourceentity instanceof Player) {
			if (sourceentity.isShiftKeyDown()) {
				return SoldierItemInteractions.takeAway(entity, sourceentity);
			} else {
				return SoldierItemInteractions.interact(entity, sourceentity);
			}
		}
		return InteractionResult.PASS;
	}
}