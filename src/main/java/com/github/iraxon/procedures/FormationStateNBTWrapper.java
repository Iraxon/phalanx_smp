package com.github.iraxon.procedures;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import net.minecraft.nbt.CompoundTag;

public record FormationStateNBTWrapper(CompoundTag data) {

    @Nonnull
    /**
     * Please use instead of constructor
     *
     * @return
     */
    public FormationStateNBTWrapper of(@Nonnull CompoundTag data) {
        return new FormationStateNBTWrapper(Objects.requireNonNull(data));
    }

    public static final String FORMATION_KEY = "formation";

    public static enum Formation {
        LINE(0, Set.of(Order.HALT, Order.ADVANCE, Order.CHARGE), true),
        SQUARE(1, Set.of(Order.HALT, Order.ADVANCE), true),
        COLUMN(2, Set.of(Order.HALT, Order.FOLLOW, Order.COLLECT), false),
        NARROW_COLUMN(2, Set.of(Order.HALT, Order.FOLLOW, Order.COLLECT), false);

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
            return Stream.of(values()).filter(v -> v.index == index).findAny().orElse(COLUMN);
        }
    }

    public void setFormation(@Nonnull Formation f) {
        Objects.requireNonNull(f);
        data.putInt(FORMATION_KEY, f.index);
    }

    @Nonnull
    public Formation formation() {
        return Formation.get(data.getInt(FORMATION_KEY));
    }

    public static final String ORDER_KEY = "order";

    public static enum Order {
        HALT(0),
        ADVANCE(1),
        CHARGE(2),
        FOLLOW(3),
        COLLECT(4);

        public final int index;

        private Order(int index) {
            this.index = index;
        }

        public boolean is_valid_for(Formation f) {
            return f.validOrders.contains(this);
        }

        @SuppressWarnings("null")
        @Nonnull
        public static Order get(int index) {
            return Stream.of(values()).filter(v -> v.index == index).findAny().orElse(HALT);
        }
    }

    public void setOrder(@Nonnull Order o) {

        Objects.requireNonNull(o);

        if (o.is_valid_for(formation()))
            data.putInt(ORDER_KEY, o.index);

        else
            data.putInt(ORDER_KEY, Order.HALT.index);
    }

    @Nonnull
    public Order order() {
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

        private Direction(int index) {
            this.index = index;
        }

        public boolean is_valid_for(Formation f) {
            return f.uses_direction;
        }

        @SuppressWarnings("null")
        @Nonnull
        public static Direction get(int index) {
            return Stream.of(values()).filter(v -> v.index == index).findAny().orElse(SOUTH);
        }
    }

    public void setDirection(@Nonnull Direction d) {

        Objects.requireNonNull(d);

        if (d.is_valid_for(formation()))
            data.putInt(DIRECTION_KEY, d.index);

        else
            data.putInt(DIRECTION_KEY, Direction.SOUTH.index);
    }

    @Nonnull
    public Direction direction() {
        return Direction.get(data.getInt(ORDER_KEY));
    }

}
