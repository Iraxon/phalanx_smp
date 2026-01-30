package com.github.iraxon.procedures;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;

import com.github.iraxon.network.PhalanxSmpModVariables;

public class GetLandownerProcedure {
	public static String execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return "";
		if (DoesClaimExistProcedure.execute(world, entity) && Math.pow(entity.getX() - entity.getCapability(PhalanxSmpModVariables.PLAYER_VARIABLES).orElseGet(PhalanxSmpModVariables.PlayerVariables::new).nearestClaimX, 2)
				+ Math.pow(entity.getY() - entity.getCapability(PhalanxSmpModVariables.PLAYER_VARIABLES).orElseGet(PhalanxSmpModVariables.PlayerVariables::new).nearestClaimY, 2)
				+ Math.pow(entity.getZ() - entity.getCapability(PhalanxSmpModVariables.PLAYER_VARIABLES).orElseGet(PhalanxSmpModVariables.PlayerVariables::new).nearestClaimZ, 2) <= 3600) {
			return getBlockNBTString(world,
					BlockPos.containing(entity.getCapability(PhalanxSmpModVariables.PLAYER_VARIABLES).orElseGet(PhalanxSmpModVariables.PlayerVariables::new).nearestClaimX,
							entity.getCapability(PhalanxSmpModVariables.PLAYER_VARIABLES).orElseGet(PhalanxSmpModVariables.PlayerVariables::new).nearestClaimY,
							entity.getCapability(PhalanxSmpModVariables.PLAYER_VARIABLES).orElseGet(PhalanxSmpModVariables.PlayerVariables::new).nearestClaimZ),
					"owner");
		}
		return "";
	}

	private static String getBlockNBTString(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getString(tag);
		return "";
	}
}