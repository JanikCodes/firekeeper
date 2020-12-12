package de.utilities;

import de.coaster.Main;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class checkChannel {

    public static boolean methode(GuildMessageReceivedEvent event, TextChannel currchat, String allowedChannel) {
        if(Main.DEVELOPER_SERVER_MODE){
            if(event.getGuild().getId().equals("763425801391308901")){
                if (allowedChannel.equals(currchat.getId()) || allowedChannel.equals("0")) {
                    return true;
                } else {
                    TextChannel channelstring = event.getChannel().getGuild().getTextChannelById(allowedChannel);
                    currchat.sendMessage(createEmbed.methode("Error", "You are not allowed to use this command in this channel! use " + channelstring.getAsMention(), Color.red, null, null, null).build()).queue();
                    return false;
                }
            }else{
                //Error message
                currchat.sendMessage(createEmbed.methode("Upcoming update!", "The bot is currently testing for an upcoming update! Please be patient.", Color.green, null, null, null).build()).queue();
                return false;
            }
        }

        if (allowedChannel.equals(currchat.getId()) || allowedChannel.equals("0")) {
            return true;
        } else {
            TextChannel channelstring = event.getChannel().getGuild().getTextChannelById(allowedChannel);
            currchat.sendMessage(createEmbed.methode("Error", "You are not allowed to use this command in this channel! use " + channelstring.getAsMention(), Color.red, null, null, null).build()).queue();
            return false;
        }

    }

}
