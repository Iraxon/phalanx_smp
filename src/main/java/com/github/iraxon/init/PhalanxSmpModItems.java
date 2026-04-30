/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package com.github.iraxon.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.Item;

import com.github.iraxon.item.InfantrySpearItem;
import com.github.iraxon.item.DeepslateRodItem;
import com.github.iraxon.PhalanxSmpMod;

public class PhalanxSmpModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, PhalanxSmpMod.MODID);
	public static final RegistryObject<Item> INFANTRY_SPEAR;
	public static final RegistryObject<Item> DEEPSLATE_ROD;
	static {
		INFANTRY_SPEAR = REGISTRY.register("infantry_spear", InfantrySpearItem::new);
		DEEPSLATE_ROD = REGISTRY.register("deepslate_rod", DeepslateRodItem::new);
	}
	// Start of user code block custom items
	// End of user code block custom items
}