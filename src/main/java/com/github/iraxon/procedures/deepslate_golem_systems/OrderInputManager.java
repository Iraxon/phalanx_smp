package com.github.iraxon.procedures.deepslate_golem_systems;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;

public class OrderInputManager {

    private static final String ORDER_MANAGER_KEY = "phalanx_order_manager";

    // NBT Accessing methods

    @Nonnull
    private static String getOrderCodeString(@Nonnull Entity orderIssuer) {
        return Objects.requireNonNull(orderIssuer.getPersistentData().getString(ORDER_MANAGER_KEY));
    }

    private static void addInput(@Nonnull Entity orderIssuer, @Nonnull OrderInput input) {
        orderIssuer.getPersistentData().putString(ORDER_MANAGER_KEY,
                getOrderCodeString(orderIssuer) + input.asString());
    }

    private static void clearInputs(@Nonnull Entity orderIssuer) {
        orderIssuer.getPersistentData().remove(ORDER_MANAGER_KEY);
    }

    // End NBT Accessing

    @Nonnull
    private static List<OrderInput> getOrderCode(@Nonnull Entity orderIssuer) {
        return Objects.requireNonNull(getOrderCodeString(orderIssuer).chars().mapToObj(Character::toString)
                .map(OrderInput::fromString).toList());
    }

    /**
     * @return Whether the orderIssuer is currently typing an order
     */
    public static boolean isActive(@Nonnull Entity orderIssuer) {
        return getOrderCodeString(orderIssuer).length() > 0;
    }

    @Nonnull
    private static String infoMessage(@Nonnull Entity orderIssuer) {
        return isActive(orderIssuer)
                ? ("Typing: "
                        + getOrderCodeString(orderIssuer))
                : "No Order";
    }

    // Input methods

    public static void inputUp(@Nullable Entity orderIssuer) {
        if (orderIssuer == null) {
            return;
        }
        addInput(orderIssuer, OrderInput.UP);
        PhalanxUtils.sendMessage(orderIssuer, infoMessage(orderIssuer));
    }

    public static void inputDown(@Nullable Entity orderIssuer) {
        if (orderIssuer == null) {
            return;
        }
        addInput(orderIssuer, OrderInput.DOWN);
        PhalanxUtils.sendMessage(orderIssuer, infoMessage(orderIssuer));
    }

    public static void inputCancel(@Nullable Entity orderIssuer) {
        if (orderIssuer == null) {
            return;
        }
        clearInputs(orderIssuer);
        PhalanxUtils.sendMessage(orderIssuer, "Canceled");
    }

    @SuppressWarnings("null")
    public static void inputConfirm(@Nullable Entity orderIssuer) {
        if (orderIssuer == null) {
            return;
        }
        final var orderCode = getOrderCode(orderIssuer);
        final var orderCodeString = Order.stringFromCode(orderCode);

        clearInputs(orderIssuer);

        Order.decode(orderCodeString).ifPresentOrElse(
                order -> processOrder(orderCodeString, order, orderIssuer),
                () -> PhalanxUtils.sendMessage(orderIssuer, "Unknown order code: " + orderCodeString));
    }

    @SuppressWarnings("null")
    private static void processOrder(@Nonnull String orderCodeString, @Nonnull Order order,
            @Nullable Entity orderIssuer) {

        if (orderIssuer == null) {
            return;
        }
        findOrderRecipient(orderIssuer).ifPresentOrElse(
                recipient -> Order.issueOrder(order, recipient, orderIssuer),
                () -> PhalanxUtils.sendMessage(orderIssuer, "No recipient for order: " + orderCodeString));

    }

    @SuppressWarnings("null")
    private static Optional<Mob> findOrderRecipient(@Nonnull Entity orderIssuer) {
        return PhalanxUtils.getNearestEntityWithPredicate(
                orderIssuer.level(),
                Mob.class,
                orderIssuer.position(),
                128,
                entity -> SoldierState.PlayerLiegeUUID.get(entity).filter(orderIssuer.getStringUUID()::equals)
                        .isPresent());
    }
}
