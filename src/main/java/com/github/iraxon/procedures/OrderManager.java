package com.github.iraxon.procedures;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.iraxon.entity.DeepslateGolemEntity;
import com.github.iraxon.procedures.DeepslateGolemNBTWrapper.GolemType;
import com.github.iraxon.procedures.FormationStateNBTWrapper.Order;

import net.minecraft.world.entity.Entity;

public record OrderManager(@Nonnull Entity orderIssuer, @Nonnull ArrayList<OrderInput> inputs) {

    @Nonnull
    private static ConcurrentHashMap<String, OrderManager> cache = new ConcurrentHashMap<>();

    private OrderManager(@Nonnull Entity orderIssuer) {
        this(orderIssuer, new ArrayList<>());
    }

    @SuppressWarnings("null")
    @Nonnull
    /**
     * Factory; use instead of constructor
     *
     * Does not work on logical client (hence Optional)
     *
     * @param orderIssuer
     * @return
     */
    public static Optional<OrderManager> of(@Nonnull Entity orderIssuer) {

        if (orderIssuer.level().isClientSide()) {
            return Optional.empty();
        }

        final String uuid = Objects.requireNonNull(orderIssuer).getStringUUID();

        return Optional.of(cache.computeIfAbsent(uuid,
                (String u) -> new OrderManager(orderIssuer)));
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
     *
     * @param orderIssuer
     * @return
     */
    public static Optional<OrderManager> getOptional(@Nullable Entity orderIssuer) {
        return Optional.ofNullable(ifPresent(orderIssuer));
    }

    public void inputUp() {
        this.addInput(OrderInput.UP);
        PhalanxUtils.sendMessage(orderIssuer, infoMessage(), true);
    }

    public void inputDown() {
        this.addInput(OrderInput.DOWN);
        PhalanxUtils.sendMessage(orderIssuer, infoMessage(), true);
    }

    public void inputCancel() {
        this.inputs.clear();
        PhalanxUtils.sendMessage(orderIssuer, "Canceled", true);
    }

    public void inputConfirm() {

        final var orderOptional = Order.get(this.inputs);
        if (orderOptional.isEmpty()) {
            PhalanxUtils.sendMessage(orderIssuer, "Unknown order code", true);
            this.inputs.clear();
            return;
        }

        @Nonnull
        final var order = Objects.requireNonNull(orderOptional.orElseThrow());

        final var recipientOptional = findOrderRecipient(orderIssuer);
        if (recipientOptional.isEmpty()) {
            PhalanxUtils.sendMessage(orderIssuer, "No commander found", true);
            this.inputs.clear();
            return;
        }

        @Nonnull
        final var recipient = Objects.requireNonNull(recipientOptional.orElseThrow());

        final var success = recipient.formationWrapper().setOrder(order);
        this.inputs.clear();

        PhalanxUtils.sendMessage(orderIssuer,
                success ? "Order sent: " + order.toString() : "Wrong formation for order: " + order.toString(),
                true);
    }

    public static void inputUp(@Nonnull Entity orderIssuer) {
        OrderManager.of(orderIssuer).ifPresent(OrderManager::inputUp);
    }

    public static void inputDown(@Nonnull Entity orderIssuer) {
        OrderManager.of(orderIssuer).ifPresent(OrderManager::inputDown);
    }

    public static void inputCancel(@Nonnull Entity orderIssuer) {
        OrderManager.of(orderIssuer).ifPresent(OrderManager::inputCancel);
    }

    public static void inputConfirm(@Nonnull Entity orderIssuer) {
        OrderManager.of(orderIssuer).ifPresent(OrderManager::inputConfirm);
    }

    private void addInput(OrderInput input) {
        this.inputs.add(input);
    }

    /**
     * @return Whether the orderIssuer is currently typing an order
     */
    public boolean isActive() {
        return this.inputs.size() > 0;
    }

    private String infoMessage() {
        return this.isActive()
                ? ("Typing: "
                        + this.inputs.stream()
                                .map(OrderInput::rep)
                                .reduce("", String::concat))
                : "No Order";
    }

    public static enum OrderInput {
        UP,
        DOWN;

        public String rep() {
            return switch (this) {
                case UP -> "▲";
                case DOWN -> "▼";
            };
        }
    }

    @SuppressWarnings("null")
    private static Optional<DeepslateGolemNBTWrapper> findOrderRecipient(@Nonnull Entity orderIssuer) {
        return PhalanxUtils.getNearestEntityWithPredicate(orderIssuer.level(), DeepslateGolemEntity.class,
                orderIssuer.position(), 50,
                golem -> {
                    final var wrapper = DeepslateGolemNBTWrapper.of(golem);
                    return wrapper.getType().equals(GolemType.COMMANDER)
                            && wrapper.getPlayerUUID().equals(orderIssuer.getStringUUID());
                }).map((@Nonnull DeepslateGolemEntity golem) -> DeepslateGolemNBTWrapper.of(golem));
    }
}
