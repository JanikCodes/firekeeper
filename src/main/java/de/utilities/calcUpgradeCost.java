package de.utilities;

public class calcUpgradeCost {

    public static long methode(Integer value) {
        long ret = 0;
        if (value >= 50) {
            ret = Math.round((value * 125) * 2);
        } else {
            ret = (value * 125);
        }
        return ret;
    }
}
