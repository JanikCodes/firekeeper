package de.utilities;

import de.coaster.Main;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class checkChannel {

    public static boolean methode(GuildMessageReceivedEvent event, TextChannel currchat, String allowedChannel,String allowedChannel2,String allowedChannel3) {
        if (allowedChannel.equals(currchat.getId()) || allowedChannel.equals("0") || allowedChannel2.equals(currchat.getId()) || allowedChannel3.equals(currchat.getId())) {
            return true;
        } else {
            TextChannel channelstring = event.getChannel().getGuild().getTextChannelById(allowedChannel);
            currchat.sendMessage(createEmbed.methode("Error", "You are not allowed to use this command in this channel! use " + channelstring.getAsMention(), Color.red, null, null, null).build()).queue();
            return false;
        }

    }

}
