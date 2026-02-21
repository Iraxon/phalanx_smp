package com.github.iraxon.procedures;

import net.minecraft.world.entity.Entity;

public class IssueOrdersDownInputOnKeyPressedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		OrderManager.of(entity).ifPresent(OrderManager::inputDown);
	}
}
