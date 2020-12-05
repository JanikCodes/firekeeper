package de.utilities;

public class getChance {

    private static int methode(Integer value) {
        int chance = 0;
        if (value < 10) {
            chance = 1;
        } else if (value == 100) {
            chance = 10;
        } else {
            chance = Integer.parseInt(Integer.toString(value).substring(0, 1));
        }
        return chance;
    }

}
