package com.github.iraxon.procedures;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import com.github.iraxon.entity.DeepslateGolemEntity;

public class DeepslateGolemAIProcedure {
	public static boolean execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return false;
		Entity oldAtkTarget = null;
		Entity newAtkTarget = null;
		String team = "";
		String teamIterator = "";
		double currentDistanceToNewTarget = 0;
		if (!(entity instanceof DeepslateGolemEntity)) {
			return false;
		}
		oldAtkTarget = entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null;
		if (!(oldAtkTarget instanceof LivingEntity && (entity.position()).distanceTo((oldAtkTarget.position())) <= 0)) {
			team = entity instanceof LivingEntity _teamEnt && _teamEnt.level().getScoreboard().getPlayersTeam(_teamEnt instanceof Player _pl ? _pl.getGameProfile().getName() : _teamEnt.getStringUUID()) != null
					? _teamEnt.level().getScoreboard().getPlayersTeam(_teamEnt instanceof Player _pl ? _pl.getGameProfile().getName() : _teamEnt.getStringUUID()).getName()
					: "";
			currentDistanceToNewTarget = 5;
			for (Entity entityiterator : world.getEntities(entity, new AABB(x, y, z, x, y, z))) {
				teamIterator = entityiterator instanceof LivingEntity _teamEnt && _teamEnt.level().getScoreboard().getPlayersTeam(_teamEnt instanceof Player _pl ? _pl.getGameProfile().getName() : _teamEnt.getStringUUID()) != null
						? _teamEnt.level().getScoreboard().getPlayersTeam(_teamEnt instanceof Player _pl ? _pl.getGameProfile().getName() : _teamEnt.getStringUUID()).getName()
						: "";
				if (!(teamIterator).isEmpty() && (team).equals(teamIterator) && (entity.position()).distanceTo((entityiterator.position())) <= currentDistanceToNewTarget) {
					newAtkTarget = entityiterator;
					currentDistanceToNewTarget = (entity.position()).distanceTo((entityiterator.position()));
				}
			}
			if (entity instanceof Mob _entity && newAtkTarget instanceof LivingEntity _ent)
				_entity.setTarget(_ent);
		}
		return true;
	}
}