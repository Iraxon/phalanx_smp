package com.github.iraxon.procedures;

import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

import com.github.iraxon.entity.DeepslateGolemEntity;
import com.github.iraxon.procedures.deepslate_golem_systems.PhalanxUtils;
import com.github.iraxon.procedures.deepslate_golem_systems.SoldierState;

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
					// TODO: Re-add filter for commanders only
					golem -> SoldierState.PlayerLiegeUUID.get(golem)
							.filter(orderIssuer.getStringUUID()::equals)
							.isPresent())
					.orElse(null);
		}
		return null;
	}
}
