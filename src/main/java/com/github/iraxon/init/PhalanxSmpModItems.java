/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package com.github.iraxon.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.Item;

import com.github.iraxon.item.HeavyInfantryGolemShellItem;
import com.github.iraxon.item.CommanderGolemShellItem;
import com.github.iraxon.PhalanxSmpMod;

public class PhalanxSmpModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, PhalanxSmpMod.MODID);
	public static final RegistryObject<Item> COMMANDER_GOLEM_SHELL;
	public static final RegistryObject<Item> HEAVY_INFANTRY_GOLEM_SHELL;
	static {
		COMMANDER_GOLEM_SHELL = REGISTRY.register("commander_golem_shell", CommanderGolemShellItem::new);
		HEAVY_INFANTRY_GOLEM_SHELL = REGISTRY.register("heavy_infantry_golem_shell", HeavyInfantryGolemShellItem::new);
	}
	// Start of user code block custom items
	// End of user code block custom items
}