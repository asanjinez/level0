package org.solution;

public enum Operations {
    VIEW,
    DEPOSIT,
    WITHDRAW,
    TRANSFER,
    EXIT;

    public String toUpperCaseString() {
        return this.name().toUpperCase();
    }

    public int toIndex() {
        return this.ordinal();
    }

    public static Operations fromUpperCaseString(String value) {
        try {
            return valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("There is no operation with the name: " + value);
        }
    }

    public static Operations fromIndex(int index) {
        if (index >= 0 && index < values().length) {
            return values()[index];
        } else {
            throw new IllegalArgumentException("There is no operation with index: " + index);
        }
    }
}
