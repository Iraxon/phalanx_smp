package com.github.iraxon.procedures;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.iraxon.entity.DeepslateGolemEntity;

import net.minecraft.world.entity.Entity;

public class OrderInputManager {

    private static final String ORDER_MANAGER_KEY = "phalanx_order_manager";

    // NBT Accessing methods

    @Nonnull
    private static String getOrderCodeString(@Nonnull Entity orderIssuer) {
        return Objects.requireNonNull(orderIssuer.getPersistentData().getString(ORDER_MANAGER_KEY));
    }

    private static void addInput(@Nonnull Entity orderIssuer, @Nonnull OrderInput input) {
        orderIssuer.getPersistentData().putString(ORDER_MANAGER_KEY, getOrderCodeString(orderIssuer) + input.rep);
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

    public static enum OrderInput {
        UP("▲"),
        DOWN("▼");

        private final String rep;

        private OrderInput(String rep) {
            this.rep = rep;
        }

        /**
         * @return Guaranteed to be of length 1
         */
        public String asString() {
            return this.rep;
        }

        /**
         * @param s A 1-length String representing an OrderInput
         * @return
         */
        public static OrderInput fromString(String s) {
            return Arrays.stream(values()).filter(orderInput -> orderInput.rep.equals(s)).findAny().orElse(DOWN);
        }
    }

    // Input methods

    public static void inputUp(@Nullable Entity orderIssuer) {
        if (orderIssuer == null) {
            return;
        }
        addInput(orderIssuer, OrderInput.UP);
        PhalanxUtils.sendMessage(orderIssuer, infoMessage(orderIssuer), true);
    }

    public static void inputDown(@Nullable Entity orderIssuer) {
        if (orderIssuer == null) {
            return;
        }
        addInput(orderIssuer, OrderInput.DOWN);
        PhalanxUtils.sendMessage(orderIssuer, infoMessage(orderIssuer), true);
    }

    public static void inputCancel(@Nullable Entity orderIssuer) {
        if (orderIssuer == null) {
            return;
        }
        clearInputs(orderIssuer);
        PhalanxUtils.sendMessage(orderIssuer, "Canceled", true);
    }

    public static void inputConfirm(@Nullable Entity orderIssuer) {
        // if (orderIssuer == null) {
        //     return;
        // }
        // if (orderIssuer.level().isClientSide) {
        //     return;
        // }

        // final var orderOptional = Order.get(getOrderCode(orderIssuer));
        // if (orderOptional.isEmpty()) {
        //     PhalanxUtils.sendMessage(orderIssuer, "Unknown order code", true);
        //     clearInputs(orderIssuer);
        //     return;
        // }

        // @Nonnull
        // final var order = Objects.requireNonNull(orderOptional.orElseThrow());

        // final var recipientOptional = findOrderRecipient(orderIssuer);
        // if (recipientOptional.isEmpty()) {
        //     PhalanxUtils.sendMessage(orderIssuer, "No commander found", true);
        //     clearInputs(orderIssuer);
        //     return;
        // }

        // @Nonnull
        // final var recipient = Objects.requireNonNull(recipientOptional.orElseThrow());

        // final var success = SoldierNBT.formationWrapper(recipient).setOrder(order);
        // clearInputs(orderIssuer);

        // PhalanxUtils.sendMessage(orderIssuer,
        //         success ? "Order sent: " + order.toString() : "Wrong formation for order: " + order.toString(),
        //         true);
    }

    @Nonnull
    private static Optional<DeepslateGolemEntity> findOrderRecipient(@Nonnull Entity orderIssuer) {
        return Objects.requireNonNull(Optional.ofNullable(GetNearestCommanderOfPlayerProcedure.execute(orderIssuer)));
    }
}
