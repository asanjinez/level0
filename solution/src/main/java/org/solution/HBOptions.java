package org.solution;

public enum HBOptions {
    VIEW,
    DEPOSIT,
    WITHDRAW,
    TRANSFER,
    BACK;
    public static HBOptions fromIndex(int index) {
        if (index >= 0 && index < values().length) {
            return values()[index];
        } else {
            throw new IllegalArgumentException("There is no operation with index: " + index);
        }
    }
}
