package com.github.iraxon.procedures;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.iraxon.network.PhalanxSmpModVariables;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class OrderHandling {

    private static final ConcurrentHashMap<String, List<CommandInput>> stateMap = new ConcurrentHashMap<>();

    public static void handle(@Nullable Entity orderIssuer, @Nullable CommandInput input) {
        if (orderIssuer == null || input == null) {
            return;
        }

        // Do nothing if player is not giving orders
        if (!orderIssuer.getCapability(PhalanxSmpModVariables.PLAYER_VARIABLES)
                .orElseGet(PhalanxSmpModVariables.PlayerVariables::new).isGivingOrders) {
            return;
        }

        // TODO: Finish
    }

    public static enum CommandInput {
        W,
        S;

        @Nonnull
        public static CommandInput of(char c) {
            return switch (c) {
                case 'W' -> W;
                case 'S' -> S;
                default -> W;
            };
        }

        @Nonnull
        public static CommandInput of(int c) {
            return of((char) c);
        }
    }

    private static final List<CommandInput> HALT = orderList("SSS"),
            CHARGE = orderList("WWW"),
            COLUMN_NARROW = orderList("SWS"),
            COLUMN_WIDE = orderList("SWWWS"),
            LINE = orderList("SWS"),
            SQUARE = orderList("SSWWS");

    public static void executeOrder(Entity orderIssuer, List<CommandInput> order) {

        if (order.equals(List.of(CommandInput.S, CommandInput.S, CommandInput.S))) {
        }
    }

    public static List<CommandInput> orderList(String s) {
        return s.chars().mapToObj(CommandInput::of).toList();
    }

}
