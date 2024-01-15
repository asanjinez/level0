package org.solution;

public enum Operations {
    VIEW,
    DEPOSIT,
    WITHDRAW,
    TRANSFER,
    EXIT;
    public static Operations fromIndex(int index) {
        if (index >= 0 && index < values().length) {
            return values()[index];
        } else {
            throw new IllegalArgumentException("There is no operation with index: " + index);
        }
    }
}
