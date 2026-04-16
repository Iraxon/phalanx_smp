package com.github.iraxon.procedures.deepslate_golem_systems;

import java.util.Arrays;
import java.util.Optional;

public enum SoldierType {
        COMMANDER("commander"),
        HEAVY_INFANTRY("heavy_infantry");

        public static final SoldierType DEFAULT = HEAVY_INFANTRY;

        private final String name;

        private SoldierType(String name) {
            this.name = name;
        }

        public String encode() {
            return this.name;
        }

        public static Optional<SoldierType> decode(String name) {
            return Arrays.stream(values()).filter(t -> name.equals(t.name)).findAny();
        }
    }
