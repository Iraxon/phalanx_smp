package com.github.iraxon.procedures.deepslate_golem_systems;

import java.util.Arrays;

public enum OrderInput {
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
