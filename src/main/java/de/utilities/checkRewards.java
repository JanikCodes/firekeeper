package de.utilities;

import de.coaster.Database;
import de.coaster.Main;
import net.dv8tion.jda.api.entities.Member;

public class checkRewards {

    public static void methode(String memberID, Member member){
        Integer rank = Database.getStatistic("level", memberID);
        if (rank >= 10) {
            if (Database.completeAchievement(memberID, 7)) {
                rewardUser.methode(memberID, "souls", 15000);
            }
        }
        if (rank >= 25) {
            if (Database.completeAchievement(memberID, 8)) {
                rewardUser.methode(memberID, "title", 3);
            }
        }
        if (rank >= 40) {
            if (Database.completeAchievement(memberID, 9)) {
                rewardUser.methode(memberID, "title", 4);
            }
        }
        if (rank >= 100) {
            if (Database.completeAchievement(memberID, 17)) {
                rewardUser.methode(memberID, "title", 5);
            }
        }
        if (Database.getStatistic("souls", memberID) >= 1000000) {
            if (Database.completeAchievement(memberID, 12)) {
                rewardUser.methode(memberID, "title", 6);
            }
        }
        if (Database.getStatistic("kills", memberID) >= 10) {
            Database.completeAchievement(memberID, 13);
        }
        if (Database.getStatistic("kills", memberID) >= 50) {
            if (Database.completeAchievement(memberID, 14)) {
                rewardUser.methode(memberID, "title", 7);
            }
        }
        if (Database.getStatistic("kills", memberID) >= 100) {
            if (Database.completeAchievement(memberID, 15)) {
                rewardUser.methode(memberID, "title", 8);
            }
        }
        if (findRole.methode(member, Main.tier1) != null) {
            //Is a tier 1 supporter
            if (Database.completeAchievement(memberID, 24)) {
                rewardUser.methode(memberID, "item", 44); //Weapon
                rewardUser.methode(memberID, "item", 45); //Armor
                rewardUser.methode(memberID, "title", 14);
            }
        }
        if (findRole.methode(member, Main.tier2) != null) {
            //Is a tier 2 supporter
            if (Database.completeAchievement(memberID, 25)) {
                rewardUser.methode(memberID, "item", 46); //Weapon
                rewardUser.methode(memberID, "item", 47); //Armor
                rewardUser.methode(memberID, "title", 15);
            }
        }
        int itemSize = Database.getTotalInventoryItems("item");
        int itemOwned = Database.getTotalInventoryItemsOwned("item", memberID);
        if (itemSize == itemOwned) {
            if (Database.completeAchievement(memberID, 23)) {
                rewardUser.methode(memberID, "title", 12);
            }
        }
        int weaponSize = Database.getTotalInventoryItems("weapon");
        int weaponOwned = Database.getTotalInventoryItemsOwned("weapon", memberID);
        if (weaponSize == weaponOwned) {
            if (Database.completeAchievement(memberID, 21)) {
                rewardUser.methode(memberID, "title", 10);
            }
        }
        int armorSize = Database.getTotalInventoryItems("armor");
        int armorOwned = Database.getTotalInventoryItemsOwned("armor", memberID);
        if (armorSize == armorOwned) {
            if (Database.completeAchievement(memberID, 22)) {
                rewardUser.methode(memberID, "title", 11);
            }
        }
    }


}
