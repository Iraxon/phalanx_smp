package com.github.iraxon.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

import com.github.iraxon.entity.ArmyStandardEntity;
import com.github.iraxon.procedures.deepslate_golem_systems.PlayerLiegeUUID;

public class ArmyStandardRightclickedOnEntityProcedure {
	public static void execute(@Nullable LevelAccessor world, @Nullable Entity entity, @Nullable Entity sourceentity) {
		if (entity instanceof ArmyStandardEntity s && sourceentity instanceof Player p) {
			PlayerLiegeUUID.tryProgramLoyalty(s, p);
		}
	}
}
