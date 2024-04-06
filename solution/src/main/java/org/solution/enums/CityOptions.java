package org.solution.enums;

public enum CityOptions {
    LONDON,
    MANCHESTER,
    LIVERPOOL,
    BACK;

    public static CityOptions fromIndex(int index) {
        if (index >= 0 && index < values().length) {
            return values()[index];
        } else {
            throw new IllegalArgumentException("There is no operation with index: " + index);
        }
    }
    public static CityOptions fromString(String string) {
        try {
            return CityOptions.valueOf(string.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid currency code: " + string);
        }
    }
    public static void showValues() {
        for (int i = 0; i < values().length; i++) {
            System.out.println(i + ") " + values()[i]);
        }
    }
}
