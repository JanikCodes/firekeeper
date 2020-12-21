package de.coaster;

import de.utilities.createEmbed;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.List;

public class GuildsListener extends ListenerAdapter {

    public void onGuildJoin(GuildJoinEvent event){

            String serverID = event.getGuild().getId();
            Database.insertIntoDatabase(serverID);

            List<Member> allmembers = event.getGuild().getMembers();
            Database.addMembersToDatabase(allmembers);
        try{
            event.getGuild().getDefaultChannel().sendMessage(createEmbed.methode("Thank you!", "Thanks for inviting me! \n The default **prefix** for commands is **!** , **but** you can easly change that with the command \n `!setprefix [newPrefix]`", Color.green, "Type !help for all commands", null, null).build()).queue();
            System.out.println("Wrote server welcome message while joining new server");
        } catch (InsufficientPermissionException exception) {
            System.out.println("Didnt have the permission to write when he joined a new server");
        } catch(NullPointerException np){

        }
    }

    public void onGuildLeave(GuildLeaveEvent event){

    }
}
