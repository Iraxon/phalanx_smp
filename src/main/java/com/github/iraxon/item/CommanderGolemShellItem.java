package com.github.iraxon.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class CommanderGolemShellItem extends Item {
	public CommanderGolemShellItem() {
		super(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.UNCOMMON));
	}
}