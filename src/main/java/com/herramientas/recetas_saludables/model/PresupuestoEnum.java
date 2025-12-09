package com.herramientas.recetas_saludables.model;

public enum PresupuestoEnum {
    BAJO("Bajo"),
    MEDIO("Medio"),
    ALTO("Alto");

    private final String displayName;

    PresupuestoEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
