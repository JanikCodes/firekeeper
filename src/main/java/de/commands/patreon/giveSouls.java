package de.commands.patreon;

import de.coaster.Database;
import de.coaster.GuildMessageReceived;
import de.utilities.createEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class giveSouls {

    public static void methode(GuildMessageReceivedEvent event, String arg2, TextChannel currchat){
        if (event.getMessage().getMentionedMembers().size() == 1) {
            if (arg2.matches("[0-9]+")) {
                try {
                    int soulAvailable = Database.getStatistic("souls", event.getMember().getId());
                    int soulAway = Integer.parseInt(arg2);

                    if (soulAway < soulAvailable) {
                        //Enough to donate
                        GuildMessageReceived.doesUserExist(event.getMessage().getMentionedMembers().get(0));
                        Database.reduceSouls(event.getMember().getId(), soulAway);
                        Database.giveSouls(event.getMessage().getMentionedMembers().get(0).getId(), soulAway);
                        currchat.sendMessage(createEmbed.methode("Information", "Successfully donated " + soulAway + " souls to " + event.getMessage().getMentionedMembers().get(0).getAsMention() + "!", Color.green, null, null, null).build()).queue();
                    } else {
                        //not enough souls
                        currchat.sendMessage(createEmbed.methode("Error", "You don't have enough souls to give away!", Color.red, null, null, null).build()).queue();
                    }
                } catch (NumberFormatException e) {
                    currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong number input! Please make sure it's a right number!", Color.red, null, null, null).build()).queue();
                }
            } else {
                //no number
                currchat.sendMessage(createEmbed.methode("Error", "There was a problem with the soul amount! Please make sure to insert a correct number!", Color.red, null, null, null).build()).queue();
            }
        } else {
            //No user mentioned
            currchat.sendMessage(createEmbed.methode("Error", "Could not find a mentioned user! Make sure to mention someone like `@person`.", Color.red, null, null, null).build()).queue();
        }
    }
}
