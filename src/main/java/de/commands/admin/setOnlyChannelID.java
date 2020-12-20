package de.commands.admin;

import de.coaster.Database;
import de.utilities.createEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.List;

public class setOnlyChannelID {

    public static void methode(GuildMessageReceivedEvent event, String arg, TextChannel currchat, int which_only) {
        if (event.getMessage().getMentionedChannels().size() == 1) {

            List<TextChannel> channel = event.getMessage().getMentionedChannels();
            String channelID = channel.get(0).getId();

            Database.setOnlyChannelID(event.getGuild().getId(), channelID,which_only);
            currchat.sendMessage(createEmbed.methode("System", "You've successfully changed the commands-channel!", Color.red, null, null, null).build()).queue();
        } else {
            currchat.sendMessage(createEmbed.methode("**ERROR**", "Could not find a mentioned channel! Please make sure to mention a channel!", Color.orange, null, null, null).build()).complete();
        }
    }

}
