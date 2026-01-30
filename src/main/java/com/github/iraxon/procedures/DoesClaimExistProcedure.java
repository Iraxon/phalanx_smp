package com.github.iraxon.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;

import com.github.iraxon.network.PhalanxSmpModVariables;
import com.github.iraxon.init.PhalanxSmpModBlocks;

public class DoesClaimExistProcedure {
	public static boolean execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return false;
		return (world.getBlockState(BlockPos.containing(entity.getCapability(PhalanxSmpModVariables.PLAYER_VARIABLES).orElseGet(PhalanxSmpModVariables.PlayerVariables::new).nearestClaimX,
				entity.getCapability(PhalanxSmpModVariables.PLAYER_VARIABLES).orElseGet(PhalanxSmpModVariables.PlayerVariables::new).nearestClaimY,
				entity.getCapability(PhalanxSmpModVariables.PLAYER_VARIABLES).orElseGet(PhalanxSmpModVariables.PlayerVariables::new).nearestClaimZ))).getBlock() == PhalanxSmpModBlocks.CLAIM_TOTEM.get();
	}
}