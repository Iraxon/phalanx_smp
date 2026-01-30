/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package com.github.iraxon.init;

import net.minecraftforge.fml.common.Mod;

import net.minecraft.world.level.GameRules;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PhalanxSmpModGameRules {
	public static final GameRules.Key<GameRules.BooleanValue> OFFLINE_PROTECTION_DEBUFFS = GameRules.register("offlineProtectionDebuffs", GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));
	public static final GameRules.Key<GameRules.BooleanValue> PHALANX_VAR_ONLINE = GameRules.register("phalanxVarOnline", GameRules.Category.MISC, GameRules.BooleanValue.create(false));
}