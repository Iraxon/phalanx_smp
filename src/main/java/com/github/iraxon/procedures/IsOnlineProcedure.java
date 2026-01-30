package com.github.iraxon.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import com.github.iraxon.init.PhalanxSmpModGameRules;

public class IsOnlineProcedure {
	public static boolean execute(LevelAccessor world, double x, double y, double z, String name) {
		if (name == null)
			return false;
		boolean r = false;
		world.getLevelData().getGameRules().getRule(PhalanxSmpModGameRules.PHALANX_VAR_ONLINE).set(false, world.getServer());
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					("execute if entity {k0} run gamerule phalanxVarOnline true".replace("{k0}", name)));
		return world.getLevelData().getGameRules().getBoolean(PhalanxSmpModGameRules.PHALANX_VAR_ONLINE);
	}
}