package com.github.iraxon.procedures;

import net.minecraft.world.entity.Entity;

import com.github.iraxon.network.PhalanxSmpModVariables;

public class IssueOrdersInput1OnKeyPressedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity.getCapability(PhalanxSmpModVariables.PLAYER_VARIABLES).orElseGet(PhalanxSmpModVariables.PlayerVariables::new).isGivingOrders) {/*code*/
		}
	}
}