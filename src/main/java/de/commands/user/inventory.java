package de.commands.user;

import de.coaster.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.ArrayList;

public class inventory {


    public static void methode(Member member, TextChannel currchat) {
        EmbedBuilder ms = new EmbedBuilder();


        String itemString = "";
        ArrayList<String> ownedItemName = Database.getOwnedItems(member.getId(), "item", "name");
        ArrayList<String> ownedItemEmoji = Database.getOwnedItems(member.getId(), "item", "emojiID");
        ArrayList<String> ownedItemCount = Database.getOwnedItems(member.getId(), "item", "count");

        ms.setTitle("Inventory");
        ms.setColor(Color.red);

        int itemSize = Database.getTotalInventoryItems("item");
        int itemOwned = Database.getTotalInventoryItemsOwned("item", member.getId());

        for (int i = 0; i < ownedItemName.size() - 1; i = i + 2) {
            itemString = itemString + ownedItemEmoji.get(i) + " " + ownedItemName.get(i) + " **x" + ownedItemCount.get(i) + "** ; **" + ownedItemEmoji.get(i + 1) + "** " + ownedItemName.get(i + 1) + " **x" + ownedItemCount.get(i + 1) + "** \n";
        }

        if (itemString.isEmpty()) {
            itemString = "\n Empty";
        }

        ms.setDescription("Items collected: **" + itemOwned + "/" + itemSize + "** \n **Items** \n " + itemString);
        ms.setFooter("page 1/5");

        final String[] messageID = {null};

        currchat.sendMessage(ms.build()).queue(message -> {
            message.addReaction("◀️").queue();
            message.addReaction("▶️").queue();
            messageID[0] = message.getId();
            Database.createInventoryRelation(member.getId(), messageID[0]);
        });

    }

    public static EmbedBuilder editInventoryPage(Member member, int page, String messageID, String dir) {
        String itemString = "";
        String weaponString = "";
        String armorString = "";
        String specialString = "";
        String titlesString = Database.showTitlesInInventory(member.getId());
        int maxpages = 5;

        EmbedBuilder ms = new EmbedBuilder();
        ms.clearFields();
        ms.setTitle("Inventory");
        ms.setColor(Color.red);

        if (dir.equals("forward")) {
            if (page > maxpages) {
                page = 1;
                Database.setInventoryPage(member.getId(), messageID, 1);
            } else {
                Database.setInventoryPage(member.getId(), messageID, Database.getInventoryPage(member.getId(), messageID) + 1);
            }
        } else {
            if (page < 1) {
                page = maxpages;
                Database.setInventoryPage(member.getId(), messageID, maxpages);
            } else {
                Database.setInventoryPage(member.getId(), messageID, Database.getInventoryPage(member.getId(), messageID) - 1);
            }
        }
        switch (page) {
            case 1:
                ArrayList<String> ownedItemName = Database.getOwnedItems(member.getId(), "item", "name");
                ArrayList<String> ownedItemEmoji = Database.getOwnedItems(member.getId(), "item", "emojiID");
                ArrayList<String> ownedItemCount = Database.getOwnedItems(member.getId(), "item", "count");

                for (int i = 0; i < ownedItemName.size() - 1; i = i + 2) {
                    itemString = itemString + ownedItemEmoji.get(i) + " " + ownedItemName.get(i) + " **x" + ownedItemCount.get(i) + "** , **" + ownedItemEmoji.get(i + 1) + "** " + ownedItemName.get(i + 1) + " **x" + ownedItemCount.get(i + 1) + "** \n";
                }
                if (itemString.isEmpty()) {
                    itemString = "\n Empty";
                }

                int itemSize = Database.getTotalInventoryItems("item");
                int itemOwned = Database.getTotalInventoryItemsOwned("item", member.getId());
                ms.setDescription("Items collected: **" + itemOwned + "/" + itemSize + "** \n **Items** \n" + itemString);
                break;
            case 2:
                ArrayList<String> ownedWeaponName = Database.getOwnedItems(member.getId(), "weapon", "name");
                ArrayList<String> ownedWeaponEmoji = Database.getOwnedItems(member.getId(), "weapon", "emojiID");
                ArrayList<String> ownedWeaponBonus = Database.getOwnedItems(member.getId(), "weapon", "(select bonus + (5 * level))");
                ArrayList<String> ownedWeaponID = Database.getOwnedItems(member.getId(), "weapon", "i.idItem");
                ArrayList<String> ownedWeaponLevel = Database.getOwnedItems(member.getId(), "weapon", "r.level");


                for (int i = 0; i < ownedWeaponName.size(); i++) {
                    int itemlevelInt = Integer.parseInt(ownedWeaponLevel.get(i));
                    String itemlevel = "";
                    if (itemlevelInt > 0) {
                        itemlevel = " +" + ownedWeaponLevel.get(i);
                    }
                    ms.addField(ownedWeaponEmoji.get(i) + " " + ownedWeaponName.get(i) + itemlevel, "ID: **" + ownedWeaponID.get(i) + "**\n Damage: **" + ownedWeaponBonus.get(i) + "**", true);
                }

                int weaponSize = Database.getTotalInventoryItems("weapon");
                int weaponOwned = Database.getTotalInventoryItemsOwned("weapon", member.getId());
                String weaponName = Database.getItemName(Database.getStatistic("e_weapon", member.getId()));
                ms.setDescription("Weapons collected: **" + weaponOwned + "/" + weaponSize + "** \n Currently Equipped: **" + weaponName + "** \n **Weapons**");
                break;
            case 3:
                ArrayList<String> ownedArmorName = Database.getOwnedItems(member.getId(), "armor", "name");
                ArrayList<String> ownedArmorEmoji = Database.getOwnedItems(member.getId(), "armor", "emojiID");
                ArrayList<String> ownedArmorBonus = Database.getOwnedItems(member.getId(), "armor", "(select bonus + (5 * level))");
                ArrayList<String> ownedArmorID = Database.getOwnedItems(member.getId(), "armor", "i.idItem");
                ArrayList<String> ownedArmorLevel = Database.getOwnedItems(member.getId(), "armor", "r.level");

                for (int i = 0; i < ownedArmorName.size(); i++) {
                    int itemlevelInt = Integer.parseInt(ownedArmorLevel.get(i));
                    String itemlevel = "";
                    if (itemlevelInt > 0) {
                        itemlevel = " +" + ownedArmorLevel.get(i);
                    }
                    ms.addField(ownedArmorEmoji.get(i) + " " + ownedArmorName.get(i) + itemlevel, "ID: **" + ownedArmorID.get(i) + "** \n Armor: **" + ownedArmorBonus.get(i) + "**", true);
                }

                int armorSize = Database.getTotalInventoryItems("armor");
                int armorOwned = Database.getTotalInventoryItemsOwned("armor", member.getId());
                String armorName = Database.getItemName(Database.getStatistic("e_armor", member.getId()));
                ms.setDescription("Armor sets collected: **" + armorOwned + "/" + armorSize + "** \n Currently Equipped: **" + armorName + "** \n **Armor**");
                break;
            case 4:
                ArrayList<String> ownedSpecialName = Database.getOwnedItems(member.getId(), "special", "name");
                ArrayList<String> ownedSpecialEmoji = Database.getOwnedItems(member.getId(), "special", "emojiID");

                for (int i = 0; i < ownedSpecialName.size(); i++) {
                    specialString = specialString + ownedSpecialEmoji.get(i) + " " + ownedSpecialName.get(i) + " \n";
                }
                if (specialString.isEmpty()) {
                    specialString = "\n Empty";
                }
                int specialSize = Database.getTotalInventoryItems("special");
                int specialOwned = Database.getTotalInventoryItemsOwned("special", member.getId());
                ms.setDescription("Special items collected: **" + specialOwned + "/" + specialSize + "** \n **Special** \n" + specialString);
                break;
            case 5:
                int titleSize = Database.getTotalTitles();
                int titleOwned = Database.getTotalTitlesOwned(member.getId());
                String titleName = Database.getTitle(Database.getStatistic("e_title", member.getId()));
                if (titlesString.isEmpty()) {
                    titlesString = "\n Empty";
                }

                ms.setDescription("Titles collected: **" + titleOwned + "/" + titleSize + "** \n Currently Equipped: **" + titleName + "** \n **Titles**" + titlesString);
        }

        ms.setFooter("page " + page + "/" + maxpages);

        return ms;
    }



}
