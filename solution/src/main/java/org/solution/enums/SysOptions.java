package org.solution.enums;

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
    public static void showValues() {
        for (int i = 0; i < values().length; i++) {
            System.out.println(i + ") " + values()[i]);
        }
    }
}
