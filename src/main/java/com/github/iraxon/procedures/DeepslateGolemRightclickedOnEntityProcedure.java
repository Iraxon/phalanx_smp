package com.github.iraxon.procedures;

import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelAccessor;

import java.util.Objects;

import com.github.iraxon.entity.DeepslateGolemEntity;
import com.github.iraxon.procedures.deepslate_golem_systems.SoldierState;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class DeepslateGolemRightclickedOnEntityProcedure {
	public static void execute(LevelAccessor world, Entity entity, Entity sourceentity) {
		if (world == null || entity == null || sourceentity == null)
			return;

		if (sourceentity instanceof Player p && entity instanceof DeepslateGolemEntity e) {

			if (PhalanxUtils.getGameMode(p) == GameType.CREATIVE && SoldierState.getPlayerUUID(e).equals("")) {
				SoldierState.setPlayerUUID(e, Objects.requireNonNull(p.getStringUUID()));
				PhalanxUtils.sendMessage(p, "Recruited", true);

			} else {
				PhalanxUtils.sendMessage(p, "Unit Data:\n" + SoldierState.manifest(e), false);
			}
		}
	}
}
