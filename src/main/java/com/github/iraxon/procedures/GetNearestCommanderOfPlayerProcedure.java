package com.github.iraxon.procedures;

import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

import com.github.iraxon.entity.DeepslateGolemEntity;
import com.github.iraxon.procedures.SoldierNBT.SoldierType;

import net.minecraft.server.level.ServerPlayer;

public class GetNearestCommanderOfPlayerProcedure {

	@Nullable
	@SuppressWarnings("null")
	public static DeepslateGolemEntity execute(Entity entity) {
		if (entity instanceof ServerPlayer _player) {
			var orderIssuer = _player;
			return PhalanxUtils.getNearestEntityWithPredicate(
					orderIssuer.level(),
					DeepslateGolemEntity.class,
					orderIssuer.position(), 50,
					(DeepslateGolemEntity golem) -> {
						return SoldierNBT.getType(golem).equals(SoldierType.COMMANDER)
								&& SoldierNBT.getPlayerUUID(golem).equals(orderIssuer.getStringUUID());
					}).orElse(null);
		}
		return null;
	}
}
