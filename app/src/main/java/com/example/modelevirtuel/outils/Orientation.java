package com.example.modelevirtuel.outils;

public enum Orientation {
    NORD("Nord"), OUEST("Ouest"), SUD("Sud"), EST("Est");
    private String oriantation;

    /**
     * Constructeur
     * @param oriantation
     */
     Orientation(String oriantation) {
        this.oriantation = oriantation;

    }
}
