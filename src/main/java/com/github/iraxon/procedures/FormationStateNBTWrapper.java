package com.github.iraxon.procedures;

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
    public FormationStateNBTWrapper of(CompoundTag data) {
        return new FormationStateNBTWrapper(data);
    }

    public static final String FormationKey = "formation";

    public enum Formation {
        LINE(0),
        SQUARE(1),
        COLUMN(2);

        public final int index;

        private Formation(int index) {
            this.index = index;
        }

        public static Formation get(int index) {
            return Stream.of(values()).filter(v -> v.index == index).findAny().orElse(COLUMN);
        }
    }

    public void setFormation(Formation f) {
        data.putInt(FormationKey, f.index);
    }

    public Formation formation() {
        return Formation.get(data.getInt(FormationKey));
    }

}
