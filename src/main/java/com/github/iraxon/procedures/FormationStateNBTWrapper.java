package com.github.iraxon.procedures;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import com.github.iraxon.procedures.OrderInputManager.OrderInput;

import net.minecraft.nbt.CompoundTag;

public record FormationStateNBTWrapper(CompoundTag data) {

    @Nonnull
    /**
     * Please use instead of constructor
     *
     * @return
     */
    public static FormationStateNBTWrapper of(@Nonnull CompoundTag data) {
        return new FormationStateNBTWrapper(Objects.requireNonNull(data));
    }

    public static final String FORMATION_KEY = "formation";

    public static enum Formation {

        NARROW_COLUMN(0, Set.of(Order.HALT, Order.FOLLOW), false),
        COLUMN(1, Set.of(Order.HALT, Order.FOLLOW), false),

        LINE(2, Set.of(Order.HALT, Order.ADVANCE, Order.CHARGE), true),
        SQUARE(3, Set.of(Order.HALT, Order.ADVANCE), true);

        public final int index;
        /**
         * Do not mutate
         */
        public final Set<Order> validOrders;

        public final boolean uses_direction;

        private Formation(int index, Set<Order> validOrders, boolean uses_direction) {
            this.index = index;
            this.validOrders = validOrders;
            this.uses_direction = uses_direction;
        }

        @SuppressWarnings("null")
        @Nonnull
        public static Formation get(int index) {
            return Stream.of(values()).filter(v -> v.index == index).findAny().orElse(NARROW_COLUMN);
        }
    }

    public void setFormation(@Nonnull Formation f) {
        Objects.requireNonNull(f);
        data.putInt(FORMATION_KEY, f.index);
    }

    @Nonnull
    public Formation getFormation() {
        return Formation.get(data.getInt(FORMATION_KEY));
    }

    public static final String ORDER_KEY = "order";

    public static enum Order {
        HALT(0, codeFromString("▼▼▼")),
        ADVANCE(1, codeFromString("▲▲▼")),
        CHARGE(2, codeFromString("▲▲▲")),
        FOLLOW(3, codeFromString("▲▼▲"));

        public final int index;
        public final List<OrderInputManager.OrderInput> code;

        private Order(int index, List<OrderInputManager.OrderInput> code) {
            this.index = index;
            this.code = code;
        }

        public boolean is_valid_for(Formation f) {
            return f.validOrders.contains(this);
        }

        @SuppressWarnings("null")
        @Nonnull
        public static Order get(int index) {
            return Stream.of(values()).filter(v -> v.index == index).findAny().orElse(HALT);
        }

        @SuppressWarnings("null")
        @Nonnull
        public static Optional<Order> get(List<OrderInput> code) {
            return Stream.of(values()).filter(v -> v.code.equals(code)).findAny();
        }

        @SuppressWarnings("null")
        @Nonnull
        private static List<OrderInput> codeFromString(@Nonnull String s) {
            return s.chars().mapToObj(
                    c -> switch (c) {
                        case '^', '▲' -> OrderInput.UP;
                        default -> OrderInput.DOWN; // ▼
                    }).toList();
        }
    }

    /**
     * Set current order
     *
     * @param o
     * @return Whether the order was successfully set
     */
    public boolean setOrder(@Nonnull Order o) {

        Objects.requireNonNull(o);

        if (o.is_valid_for(getFormation())) {
            data.putInt(ORDER_KEY, o.index);
            return true;

        } else {
            data.putInt(ORDER_KEY, Order.HALT.index);
            return false;
        }
    }

    @Nonnull
    public Order getOrder() {
        return Order.get(data.getInt(ORDER_KEY));
    }

    public static final String DIRECTION_KEY = "direction";

    public static enum Direction {
        SOUTH(0),
        SOUTHWEST(1),
        WEST(2),
        NORTHWEST(3),
        NORTH(4),
        NORTHEAST(5),
        EAST(6),
        SOUTHEAST(7);

        public final int index;

        @SuppressWarnings("unchecked")
        private static final Map<Integer, Direction> getMap = Map
                .ofEntries(Stream.of(values()).map(
                        dir -> Map.entry(dir.index, dir)).toArray(Entry[]::new));

        private Direction(int index) {
            this.index = index;
        }

        public boolean is_valid_for(Formation f) {
            return f.uses_direction;
        }

        /**
         * Returns rotated direction
         *
         * @param change In 45-degree increments clockwise (e.g. +2 for 90 degrees
         *               clockwise)
         * @return New direction
         */
        public Direction rotate(int change) {
            return Direction.get((index + change) % 8);
        }

        @SuppressWarnings("null")
        @Nonnull
        public static Direction get(int index) {
            return getMap.getOrDefault(index, SOUTH);
        }
    }

    public void setDirection(@Nonnull Direction d) {

        Objects.requireNonNull(d);

        if (d.is_valid_for(getFormation()))
            data.putInt(DIRECTION_KEY, d.index);

        else
            data.putInt(DIRECTION_KEY, Direction.SOUTH.index);
    }

    @Nonnull
    public Direction getDirection() {
        return Direction.get(data.getInt(ORDER_KEY));
    }

    public String manifest() {
        return ("Direction: " + getDirection() + "\n"
                + "Formation: " + getFormation() + "\n"
                + "Order: " + getOrder() + "\n");
    }

}
