package com.github.iraxon.procedures.deepslate_golem_systems;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public enum Order {
    HALT("▼▼▼"),
    FOLLOW("▼▲▼");

    private static final Order DEFAULT = HALT;

    @Nonnull
    private final List<OrderInput> code;

    private Order(@Nonnull String s) {
        this.code = codeFromString(s);
    }

    @SuppressWarnings("null")
    public static void issueOrder(@Nonnull Order order, @Nonnull Mob soldier, @Nullable Entity orderIssuer) {
        switch (order) {
            case HALT -> SoldierState.soldierHalt(soldier);
            case FOLLOW -> {
                if (orderIssuer instanceof LivingEntity l) {
                    SoldierState.soldierFollow(soldier, l);
                } else {
                    SoldierState.PlayerLiegeUUID.get(soldier).flatMap(
                            uuid -> PhalanxUtils.getEntityByUUID(
                                    soldier.level(),
                                    Player.class,
                                    soldier.position(),
                                    128,
                                    uuid))
                            .ifPresentOrElse(
                                    liege -> SoldierState.soldierFollow(soldier, liege),
                                    () -> SoldierState.soldierHalt(soldier));
                }
            }
        }
        PhalanxUtils.sendMessage(orderIssuer, "Order sent: " + order.toString());
    }

    // Code stuff

    public static Order decodeOrDefault(@Nonnull String s) {
        return decode(s).orElse(DEFAULT);
    }

    public static Optional<Order> decode(@Nonnull String s) {
        return Arrays.stream(values()).filter(order -> stringFromCode(order.code).equals(s)).findAny();
    }

    @SuppressWarnings("null")
    @Nonnull
    public static List<OrderInput> codeFromString(@Nonnull String s) {
        return s.chars().mapToObj(c -> c == '▲' ? OrderInput.UP : OrderInput.DOWN).toList();
    }

    @SuppressWarnings("null")
    @Nonnull
    public static String stringFromCode(@Nonnull List<OrderInput> code) {
        return code.stream().map(OrderInput::asString).collect(Collectors.joining());
    }
}
