/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package com.github.iraxon.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;

import com.github.iraxon.block.ClaimTotemBlock;
import com.github.iraxon.PhalanxSmpMod;

public class PhalanxSmpModBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, PhalanxSmpMod.MODID);
	public static final RegistryObject<Block> CLAIM_TOTEM;
	static {
		CLAIM_TOTEM = REGISTRY.register("claim_totem", ClaimTotemBlock::new);
	}
	// Start of user code block custom blocks
	// End of user code block custom blocks
}