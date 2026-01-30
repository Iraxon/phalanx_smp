package com.github.iraxon.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import java.util.ArrayList;

import com.github.iraxon.network.PhalanxSmpModVariables;

public class ClaimTotemOnTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		double distance = 0;
		double currentDistance = 0;
		if (!world.isClientSide()) {
			if ((getBlockNBTString(world, BlockPos.containing(x, y, z), "owner")).isEmpty()) {
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"setblock ~ ~ ~ air destroy");
			} else {
				for (Entity entityiterator : new ArrayList<>(world.players())) {
					distance = Math.pow(entityiterator.getX() - x, 2) + Math.pow(entityiterator.getY() - y, 2) + Math.pow(entityiterator.getZ() - z, 2);
					currentDistance = Math.pow(entityiterator.getX() - entityiterator.getCapability(PhalanxSmpModVariables.PLAYER_VARIABLES).orElseGet(PhalanxSmpModVariables.PlayerVariables::new).nearestClaimX, 2)
							+ Math.pow(entityiterator.getY() - entityiterator.getCapability(PhalanxSmpModVariables.PLAYER_VARIABLES).orElseGet(PhalanxSmpModVariables.PlayerVariables::new).nearestClaimY, 2)
							+ Math.pow(entityiterator.getZ() - entityiterator.getCapability(PhalanxSmpModVariables.PLAYER_VARIABLES).orElseGet(PhalanxSmpModVariables.PlayerVariables::new).nearestClaimZ, 2);
					if (!DoesClaimExistProcedure.execute(world, entityiterator) || distance < currentDistance) {
						{
							entityiterator.getCapability(PhalanxSmpModVariables.PLAYER_VARIABLES).ifPresent(capability -> {
								capability.nearestClaimX = x;
								capability.nearestClaimY = y;
								capability.nearestClaimZ = z;
								capability.markSyncDirty();
							});
						}
					}
				}
			}
		}
	}

	private static String getBlockNBTString(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getString(tag);
		return "";
	}
}