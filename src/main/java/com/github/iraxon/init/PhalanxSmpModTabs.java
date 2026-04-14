/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package com.github.iraxon.init;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.core.registries.Registries;

import com.github.iraxon.PhalanxSmpMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PhalanxSmpModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PhalanxSmpMod.MODID);

	@SubscribeEvent
	public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
		if (tabData.getTabKey() == CreativeModeTabs.INGREDIENTS) {
			tabData.accept(PhalanxSmpModItems.COMMANDER_GOLEM_SHELL.get());
			tabData.accept(PhalanxSmpModItems.HEAVY_INFANTRY_GOLEM_SHELL.get());
		}
	}
}