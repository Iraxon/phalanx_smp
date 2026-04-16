package com.github.iraxon.procedures.deepslate_golem_systems;

import java.util.Arrays;
import java.util.Optional;

import javax.annotation.Nonnull;

public enum SoldierType {
        COMMANDER("commander"),
        HEAVY_INFANTRY("heavy_infantry");

        public static final SoldierType DEFAULT = HEAVY_INFANTRY;

        @Nonnull
        private final String name;

        private SoldierType(@Nonnull String name) {
            this.name = name;
        }

        @Nonnull
        public String encode() {
            return this.name;
        }

        @Nonnull
        public static Optional<SoldierType> decode(String name) {
            return Arrays.stream(values()).filter(t -> name.equals(t.name)).findAny();
        }
    }
