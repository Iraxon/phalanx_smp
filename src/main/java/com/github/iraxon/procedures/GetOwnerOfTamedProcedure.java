package com.github.iraxon.procedures;

import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.Entity;

public class GetOwnerOfTamedProcedure {
	public static Entity execute(Entity entity) {
		if (entity == null)
			return null;
		return entity instanceof TamableAnimal _tamEnt ? (Entity) _tamEnt.getOwner() : null;
	}
}