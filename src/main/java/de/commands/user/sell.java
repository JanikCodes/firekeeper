package de.commands.user;

import de.coaster.Database;
import de.utilities.createEmbed;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class sell {

    public static void methode(GuildMessageReceivedEvent event, String prefix, TextChannel currchat) {
        Integer itemvalue = Database.getTotalItemValue(event.getMember().getId());
        currchat.sendMessage(createEmbed.methode("**Lonesome Gavlan**", "Hello " + event.getMember().getAsMention() + "! \n Do you want to **sell** all your items in your **inventory**? \n You would get **" + itemvalue + "** souls! \n Are you willing to sell?", Color.red, "Accept or decline with the reactions below", null, "https://cdn.discordapp.com/attachments/773175900178743297/781193058095857704/gavlanframe.jpg").build()).queue(message -> {
            message.addReaction("✅").queue();
            message.addReaction("❌").queue();
            Database.createSellRelation(message.getId(), event.getMember().getId());
        });

    }

}
