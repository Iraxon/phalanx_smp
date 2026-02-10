package com.github.iraxon.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.registries.Registries;

public class IssueOrdersInput2OnKeyPressedProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		OrderHandling.handle(entity, CommandInput.S);

	}
}