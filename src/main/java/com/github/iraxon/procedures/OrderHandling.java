package com.github.iraxon.procedures;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.iraxon.network.PhalanxSmpModVariables;

import net.minecraft.world.entity.Entity;

public class OrderHandling {

    private static final ConcurrentHashMap<String, ArrayList<CommandInput>> stateMap = new ConcurrentHashMap<>();

    public static void handle(@Nullable Entity orderIssuer, @Nullable CommandInput input) {
        if (orderIssuer == null || input == null) {
            return;
        }

        // Do nothing if player is not giving orders
        if (!orderIssuer.getCapability(PhalanxSmpModVariables.PLAYER_VARIABLES)
                .map(variables -> variables.isGivingOrders).orElse(false)) {
            return;
        }

        final ArrayList<CommandInput> currentOrder = retrieveState(orderIssuer);
        currentOrder.add(input);
    }

    public static void finalizeOrder(@Nullable Entity orderIssuer) {

        if (orderIssuer == null) {
            return;
        }

        @Nullable
        final var completeOrder = stateMap.remove(orderIssuer.getStringUUID());

        if (completeOrder == null) {
            return;
        }

        executeOrder(orderIssuer, completeOrder);
    }

    private static ArrayList<CommandInput> retrieveState(@Nonnull Entity orderIssuer) {
        return stateMap.computeIfAbsent(orderIssuer.getStringUUID(), s -> new ArrayList<CommandInput>());
    }

    public static enum CommandInput {
        W,
        S;

        @Nonnull
        public static CommandInput of(int c) {
            return switch (c) {
                case (int) 'W' -> W;
                case (int) 'S' -> S;
                default -> W;
            };
        }
    }

    private static final List<CommandInput> HALT = orderList("SSS"),
            CHARGE = orderList("WWW"),
            COLUMN_NARROW = orderList("SWS"),
            COLUMN_WIDE = orderList("SWWWS"),
            LINE = orderList("SWS"),
            SQUARE = orderList("SSWWS");

    public static void executeOrder(@Nonnull Entity orderIssuer, @Nonnull List<CommandInput> order) {

        if (order.equals(CHARGE)) {
            // TODO: Finish this and other orders
        }
    }

    public static List<CommandInput> orderList(String s) {
        return s.chars().mapToObj(CommandInput::of).toList();
    }

}
