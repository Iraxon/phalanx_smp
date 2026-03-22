package com.github.iraxon.procedures;

import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelAccessor;

import com.github.iraxon.entity.DeepslateGolemEntity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class DeepslateGolemRightclickedOnEntityProcedure {
	public static void execute(LevelAccessor world, Entity entity, Entity sourceentity) {
		if (world == null || entity == null || sourceentity == null)
			return;

		if (sourceentity instanceof Player p && entity instanceof DeepslateGolemEntity e) {

			if (PhalanxUtils.getGameMode(p) == GameType.CREATIVE && SoldierNBT.isNeutral(e)) {
				SoldierNBT.setPlayer(e, p);
				PhalanxUtils.sendMessage(p, "Recruited", true);

			} else {
				PhalanxUtils.sendMessage(p, "Unit Data:\n" + SoldierNBT.manifest(e), false);
			}
		}
	}
}
