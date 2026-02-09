package com.github.iraxon.procedures;

import net.minecraft.world.entity.Entity;
import com.github.iraxon.procedures.OrderHandling.CommandInput;

public class IssueOrdersInput1OnKeyPressedProcedure {
	public static void execute(Entity entity) {
		OrderHandling.handle(entity, CommandInput.W);
	}
}
