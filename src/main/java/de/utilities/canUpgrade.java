package de.utilities;

public class canUpgrade {

    public static boolean methode(long endPrice, int soulAmount) {

        if (soulAmount >= endPrice) {
            return true;
        }
        return false;
    }

}
