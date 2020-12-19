package de.commands.user;

import de.coaster.Database;
import de.coaster.GuildMessageReceived;
import de.utilities.createEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class equip {

    public static void methode(String arg1, String arg2, GuildMessageReceivedEvent event, TextChannel currchat, String prefix){
        if (arg1.equals("weapon")) {
            if (arg2.matches("[0-9]+")) {
                if (Database.equipItemInInventory(event.getMember().getId(), Integer.parseInt(arg2), "e_weapon", "weapon")) {
                    currchat.sendMessage(createEmbed.methode("**Success**", "Succesfully changed your equipped weapon!", Color.green, null, null, null).build()).queue();
                } else {
                    //Had problem finding ID
                    currchat.sendMessage(createEmbed.methode("**ERROR**", "Could not find the Weapon you were looking for.", Color.red, null, null, null).build()).queue();
                }
            } else {
                //Error no correct number
                currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! The **item ID** you typed is **wrong**, check the item ID in your inventory!", Color.red, null, null, null).build()).queue();
            }
        } else if (arg1.equals("armor")) {
            if (arg2.matches("[0-9]+")) {
                if (Database.equipItemInInventory(event.getMember().getId(), Integer.parseInt(arg2), "e_armor", "armor")) {
                    currchat.sendMessage(createEmbed.methode("**Success**", "Succesfully changed your equipped armor!", Color.green, null, null, null).build()).queue();
                } else {
                    //Had problem finding ID
                    currchat.sendMessage(createEmbed.methode("**ERROR**", "Could not find the Armor you were looking for.", Color.red, null, null, null).build()).queue();
                }
            } else {
                //Error no correct number
                currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! The **item ID** you typed is **wrong**, check the item ID in your inventory!", Color.red, null, null, null).build()).queue();
            }
        } else if (arg1.equals("title")) {
            if (arg2.matches("[0-9]+")) {
                if (Database.equipTitleInInventory(event.getMember().getId(), Integer.parseInt(arg2), "e_title")) {
                    currchat.sendMessage(createEmbed.methode("**Success**", "Succesfully changed your equipped title!", Color.green, null, null, null).build()).queue();
                } else {
                    //Had problem finding ID
                    currchat.sendMessage(createEmbed.methode("**ERROR**", "Could not find the Title you were looking for.", Color.red, null, null, null).build()).queue();
                }
            } else {
                //Error no correct number
                currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! The **item ID** you typed is **wrong**, check the item ID in your inventory!", Color.red, null, null, null).build()).queue();
            }
        } else {
            //Error incorrect second argument
            currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! You can only use `weapon`, `armor` or `title` as the second argument! \n Example syntax: `" + prefix + "equip weapon 17`", Color.red, null, null, null).build()).queue();
        }
    }
}
