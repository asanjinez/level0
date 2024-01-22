package org.solution;

public enum SysOptions {
    HOME_BANKING,
    CURRENCY_CONVERTER,
    UNVERSITY,
    SHIPPING,
    FINANCE_MANAGMENT,
    EXIT;
    public static SysOptions fromIndex(int index) {
        if (index >= 0 && index < values().length) {
            return values()[index];
        } else {
            throw new IllegalArgumentException("There is no operation with index: " + index);
        }
    }
}
