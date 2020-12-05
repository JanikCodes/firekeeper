package de.utilities;

import de.coaster.Database;

public class rewardUser {

    public static void methode(String memberID, String type, Integer ID) {
        if (type.equals("souls")) {
            Database.giveSouls(memberID, ID); //In dem falle nehme ich ID für den Soul Amount.
        } else if (type.equals("item")) {
            Database.giveItem(memberID, ID);
        } else if (type.equals("title")) {
            Database.giveTitle(memberID, ID);
        } else if (type.equals("level")) {
            Database.giveLevel(memberID);
        }
    }

}
