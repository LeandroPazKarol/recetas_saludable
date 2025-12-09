package com.herramientas.recetas_saludables.model;

public enum DificultadEnum {
    FACIL("Fácil"),
    MEDIA("Media"),
    DIFICIL("Difícil");

    private final String displayName;

    DificultadEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
