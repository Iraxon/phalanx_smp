package com.github.iraxon.procedures;

import net.minecraft.world.entity.Entity;

import com.github.iraxon.network.PhalanxSmpModVariables;

public class IssueOrdersDownInputOnKeyPressedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		{
			entity.getCapability(PhalanxSmpModVariables.PLAYER_VARIABLES).ifPresent(capability -> {
				capability.isGivingOrders = true;
				capability.markSyncDirty();
			});
		}
	}
}