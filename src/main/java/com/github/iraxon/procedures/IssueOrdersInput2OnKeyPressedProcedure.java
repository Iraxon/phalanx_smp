package com.github.iraxon.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import com.github.iraxon.procedures.OrderHandling.CommandInput;

public class IssueOrdersInput2OnKeyPressedProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		OrderHandling.handle(entity, CommandInput.S);

	}
}
