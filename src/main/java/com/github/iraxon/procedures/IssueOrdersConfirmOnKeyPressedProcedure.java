package com.github.iraxon.procedures;

import com.github.iraxon.procedures.deepslate_golem_systems.OrderInputManager;

import net.minecraft.world.entity.Entity;

public class IssueOrdersConfirmOnKeyPressedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		OrderInputManager.inputConfirm(entity);
	}
}
