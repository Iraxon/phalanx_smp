package com.github.iraxon.procedures;

import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

import com.github.iraxon.network.PhalanxSmpModVariables;

public class IssueOrdersOnKeyReleasedProcedure {
	public static void execute(@Nullable Entity entity) {

		if (entity == null) {
			return;
		}

		// Generated
		entity.getCapability(PhalanxSmpModVariables.PLAYER_VARIABLES).ifPresent(capability -> {
			capability.isGivingOrders = false;
			capability.markSyncDirty();
		});
		// End generated code

		OrderHandling.finalizeOrder(entity);
	}
}
