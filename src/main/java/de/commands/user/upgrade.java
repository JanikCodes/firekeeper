package de.commands.user;

import de.coaster.Database;
import de.utilities.createEmbed;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class upgrade {

    public static void methode(GuildMessageReceivedEvent event, String arg1, String arg2, TextChannel currchat, String prefix){
        Database.deleteUpgradeData(event.getMember().getId());
        if (arg1.equals("weapon")) {
            if (arg2.matches("[0-9]+")) {
                if (Database.findItemInInventory(event.getMember().getId(), Integer.parseInt(arg2))) {
                    String itemName = Database.getItemName(Integer.parseInt(arg2));
                    Integer itemStats = Database.getItemBonus(Integer.parseInt(arg2), event.getMember().getId());
                    Integer itemLevel = Database.getItemLevel(Integer.parseInt(arg2), event.getMember().getId());
                    Integer itemCost = 25000 + (30000 * itemLevel);
                    Message msg = currchat.sendMessage(createEmbed.methode("Forge", "Do you want to upgrade the weapon?\n Weapon: **" + itemName + " +" + itemLevel + "** \n Current damage: **" + itemStats + "** \n After upgrade: **" + (itemStats + 5) + "** \n Upgrade cost: **" + itemCost + " souls**", Color.green, "React with the reactions below to upgrade!", null, null).build()).complete();
                    msg.addReaction("✅").queue();
                    msg.addReaction("❌").queue();
                    Database.createItemUpgradeRelation(Integer.parseInt(arg2), event.getMember().getId(), msg.getId(), arg1);

                } else {
                    //Error finding items
                    currchat.sendMessage(createEmbed.methode("**ERROR**", "Could not find the Item you were looking for.", Color.red, null, null, null).build()).complete();
                }
            } else {
                //Error no correct number
                currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! The **item ID** you typed is **wrong**, check the item ID in your inventory!", Color.red, null, null, null).build()).complete();
            }
        } else if (arg1.equals("armor")) {
            if (arg2.matches("[0-9]+")) {
                if (Database.findItemInInventory(event.getMember().getId(), Integer.parseInt(arg2))) {
                    String itemName = Database.getItemName(Integer.parseInt(arg2));
                    Integer itemStats = Database.getItemBonus(Integer.parseInt(arg2), event.getMember().getId());
                    Integer itemLevel = Database.getItemLevel(Integer.parseInt(arg2), event.getMember().getId());
                    Integer itemCost = 25000 + (30000 * itemLevel);
                    Message msg = currchat.sendMessage(createEmbed.methode("Forge", "Do you want to upgrade the armor?\n Armor: **" + itemName + " +" + itemLevel + "** \n Current resistance: **" + itemStats + "** \n After upgrade: **" + (itemStats + 5) + "** \n Upgrade cost: **" + itemCost + " souls**", Color.green, "React with the reactions below to upgrade!", null, null).build()).complete();
                    msg.addReaction("✅").queue();
                    msg.addReaction("❌").queue();
                    Database.createItemUpgradeRelation(Integer.parseInt(arg2), event.getMember().getId(), msg.getId(), arg1);
                } else {
                    //Error finding items
                    currchat.sendMessage(createEmbed.methode("**ERROR**", "Could not find the Item you were looking for.", Color.red, null, null, null).build()).complete();
                }
            } else {
                //Error no correct number
                currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! The **item ID** you typed is **wrong**, check the item ID in your inventory!", Color.red, null, null, null).build()).complete();
            }
        } else {
            currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! Type `" + prefix + "help` to see all commands.", Color.red, null, null, null).build()).complete();
        }
    }
}
