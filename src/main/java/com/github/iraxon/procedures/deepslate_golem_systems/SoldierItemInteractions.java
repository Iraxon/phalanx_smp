package com.github.iraxon.procedures.deepslate_golem_systems;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class SoldierItemInteractions {

    public static record SoldierItemInteractionDescription(@Nonnull BiConsumer<Mob, Item> action,
            boolean shouldConsumeItem) {
        public void execute(@Nonnull Mob soldier, @Nonnull Item item) {
            action.accept(soldier, item);
        }
    }

    public static final SoldierItemInteractionDescription simpleInteractionDescription = new SoldierItemInteractionDescription(
            (soldier, item) -> {
                PhalanxUtils.setItemInHand(soldier, InteractionHand.MAIN_HAND, 1, item);
            },
            true);

    public static final HashMap<Item, SoldierItemInteractionDescription> itemBehaviorMap = new HashMap<>(
            Map.ofEntries(
                    Map.entry(Items.STONE_SWORD, simpleInteractionDescription)));

    @SuppressWarnings("null")
    public static InteractionResult interact(@Nonnull Mob soldier, Player player) {

        final var heldItemstack = player.getItemInHand(InteractionHand.MAIN_HAND);
        final var item = heldItemstack.getItem();

        if (itemBehaviorMap.containsKey(item)) {
            final var desc = itemBehaviorMap.get(item);
            desc.execute(soldier, item);
            if (desc.shouldConsumeItem()) {
                heldItemstack.shrink(1);
            }
            return InteractionResult.sidedSuccess(soldier.level().isClientSide());
        }
        return InteractionResult.PASS;
    }

    public static InteractionResult interact(@Nullable Entity soldier, @Nullable Entity player) {
        if (soldier instanceof Mob m && player instanceof Player p)
            return interact(m, p);
        return InteractionResult.PASS;
    }
}
