package com.github.iraxon.procedures;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.world.entity.Entity;

public record OrderManager(@Nonnull Entity orderIssuer, @Nonnull ArrayList<OrderInput> inputs) {

    private OrderManager(@Nonnull Entity orderIssuer) {
        this(orderIssuer, new ArrayList<>());
    }

    @Nonnull
    private static ConcurrentHashMap<String, OrderManager> cache = new ConcurrentHashMap<>();

    @SuppressWarnings("null")
    @Nonnull
    /**
     * Factory; use instead of constructor
     *
     * @param orderIssuer
     * @return
     */
    public static OrderManager of(@Nonnull Entity orderIssuer) {

        final String uuid = Objects.requireNonNull(orderIssuer).getStringUUID();

        return cache.computeIfAbsent(uuid,
                (String u) -> new OrderManager(orderIssuer));
    }

    @Nullable
    private static OrderManager ifPresent(@Nullable Entity orderIssuer) {
        if (orderIssuer == null) {
            return null;
        }
        return cache.getOrDefault(orderIssuer.getStringUUID(), null);
    }

    /**
     * Gets the OrderManager for this entity if one exists already;
     * does not make one if there wasn't one
     * @param orderIssuer
     * @return
     */
    public static Optional<OrderManager> getOptional(@Nullable Entity orderIssuer) {
        return Optional.ofNullable(ifPresent(orderIssuer));
    }

    public void inputUp() {
        this.basicInput(OrderInput.UP);
    }

    public void inputDown() {
        this.basicInput(OrderInput.DOWN);
    }

    public void inputCancel() {
        this.inputs.clear();
    }

    public void inputConfirm() {
        // TOOD: Orders
        this.inputs.clear();
    }

    private void basicInput(OrderInput input) {
        this.inputs.add(input);
    }

    /**
     * @return Whether the orderIssuer is currently typing an order
     */
    public boolean isActive() {
        return this.inputs.size() > 0;
    }

    /**
     * @return The info message for the current order issuing state;
     *         should be displayed every tick while isActive() returns true
     */
    public String infoMessage() {
        return this.isActive()
                ? ("Typing: "
                        + this.inputs.stream()
                                .map(OrderInput::rep)
                                .reduce("", String::concat))
                : "";
    }

    private static enum OrderInput {
        UP,
        DOWN;

        public String rep() {
            return switch (this) {
                case UP -> "/\\";
                case DOWN -> "\\/";
            };
        }
    }
}
