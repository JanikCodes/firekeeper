package de.commands.admin;

import de.coaster.Database;
import de.utilities.createEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.List;

public class setNotificationChannelID {

    public static void methode(GuildMessageReceivedEvent event, String arg, TextChannel currchat) {
        if (event.getMessage().getMentionedChannels().size() == 1) {

            List<TextChannel> channel = event.getMessage().getMentionedChannels();
            String channelID = channel.get(0).getId();

            Database.setNotificationChannel(event.getGuild().getId(), channelID);
            currchat.sendMessage(createEmbed.methode("System", "You've successfully changed the level-up notification channel!", Color.green, "You can also disable the notification.", null, null).build()).queue();
        } else {
            currchat.sendMessage(createEmbed.methode("**ERROR**", "Could not find a mentioned channel! Please make sure to mention a channel!", Color.red, null, null, null).build()).queue();
        }
    }

}
