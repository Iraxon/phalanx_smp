package com.github.iraxon.procedures;

import com.github.iraxon.procedures.OrderHandling.CommandInput;

import net.minecraft.world.entity.Entity;

public class IssueOrdersInput2OnKeyPressedProcedure {
	public static void execute(Entity entity) {
		OrderHandling.handle(entity, CommandInput.S);
	}
}
