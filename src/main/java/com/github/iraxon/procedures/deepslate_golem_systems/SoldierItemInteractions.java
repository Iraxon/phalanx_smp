package com.github.iraxon.procedures.deepslate_golem_systems;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import javax.annotation.Nonnull;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class SoldierItemInteractions {

    public static record SoldierItemInteractionDescription(@Nonnull BiConsumer<Mob, ItemStack> action,
            boolean shouldConsumeItem) {
        public void execute(@Nonnull Mob soldier, @Nonnull ItemStack itemstack) {
            action.accept(soldier, itemstack);
            if (shouldConsumeItem) {
                itemstack.shrink(1);
            }
        }
    }

    public static final SoldierItemInteractionDescription simpleInteractionDescription = new SoldierItemInteractionDescription(
            (mob, itemstack) -> {
                PhalanxUtils.setItemInHand(mob, InteractionHand.MAIN_HAND, 1, itemstack.getItem());
            },
            true);

    public static final HashMap<Item, SoldierItemInteractionDescription> itemBehaviorMap = new HashMap<>(
            Map.ofEntries(
                Map.entry(Items.STONE_SWORD, simpleInteractionDescription)
            ));

    public static InteractionResult interact(@Nonnull Mob soldier, ItemStack itemstack) {
        final var item = itemstack.getItem();
        if (itemBehaviorMap.containsKey(item)) {
            itemBehaviorMap.get(item).execute(soldier, itemstack);
            return InteractionResult.sidedSuccess(soldier.level().isClientSide());
        }
        return InteractionResult.PASS;
    }
}
