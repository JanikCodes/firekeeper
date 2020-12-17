package de.commands.user;

import de.coaster.Database;
import de.objects.Clan;
import de.utilities.createEmbed;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.awt.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class clanLogic {

    public static void createClan(GuildMessageReceivedEvent event, String[] args){
        Message msg = event.getMessage();
        TextChannel channel = event.getChannel();
        String content = event.getMessage().getContentStripped();
        Pattern pattern = Pattern.compile("\"(.*?)\"");
        Matcher matcher = pattern.matcher(content);
        String clanName = "";
        String clanLogoURL = "";
        if (matcher.find()) {
            clanName = matcher.group(1);
        } else {
            channel.sendMessage(createEmbed.methode("**ERROR**", "Could not parse the Clan Name from your message.", Color.red, null, null, null).build()).queue();
            return;
        }
        if (clanName.length() > 16){
            System.out.println(clanName +  clanName.length());
            channel.sendMessage(createEmbed.methode("**ERROR**", "The Clan Name has a character limit of 16.", Color.red, null, null, null).build()).queue();
            return;
        }
        if (msg.getEmotes().size() == 1){
            clanLogoURL = msg.getEmotes().get(0).getImageUrl();
        } else {
            channel.sendMessage(createEmbed.methode("**ERROR**", "Please provide an custom emote for you Clan banner", Color.red, null, null, null).build()).queue();
            return;
        }
        String clanTag = args[args.length-2];

        if (clanTag.length() > 4){
            channel.sendMessage(createEmbed.methode("**ERROR**", "The Clan Tag has a character limit of 4.", Color.red, null, null, null).build()).queue();
            return;
        }

        String clanOwnerID = event.getAuthor().getId();

        if (Database.doesClanTagExist(clanTag)){
            channel.sendMessage(createEmbed.methode("**ERROR**", "The provided Clan Tag already exists.", Color.red, null, null, null).build()).queue();
            return;
        }
        if (Database.isUserInClan(clanOwnerID)){
            channel.sendMessage(createEmbed.methode("**ERROR**", "You can't create a Clan because you are are already in a Clan.", Color.red, null, null, null).build()).queue();
            return;
        }

        if (Database.insertClanIntoDatabase(clanName, clanTag, clanOwnerID, clanLogoURL)){
            channel.sendMessage(createEmbed.methode("**CLAN**", "Your Clan `"+clanName+"` has been successfully created.", Color.green, null, null, null).build()).queue();
        } else {
            channel.sendMessage(createEmbed.methode("**ERROR**", "Something went wrong while trying to create your Clan.", Color.red, null, null, null).build()).queue();
        }

    }

    public static void sendLeaveClan(GuildMessageReceivedEvent event) {
        User author = event.getAuthor();
        TextChannel channel = event.getChannel();
        Message msg = event.getMessage();

        if (!Database.isUserInClan(author.getId())){
            channel.sendMessage(createEmbed.methode("**ERROR**", "You need to be in a Clan to be able to leave one", Color.red, null, null, null).build()).queue();
            return;
        }

        if (Database.getClanRole(author.getId()).equalsIgnoreCase("Owner")){
            channel.sendMessage(createEmbed.methode("**ERROR**", "A Clan-Owner cannot leave a Clan. You need to transfer the ownership first.", Color.red, null, null, null).build()).queue();
            return;
        }

        Clan clan = Database.getClanOfUser(author.getId());

        Database.insertClanMessage(clan, author.getId(), msg.getId(), "Leave");
        Message leaveMsg = channel.sendMessage(createEmbed.methode("**ERROR**", "Are you sure you want to leave you Clan?\n" +
                "React to ✅ to confirm your action.", Color.GRAY, null, null, null).build()).complete();
        leaveMsg.addReaction("✅").queue();
        leaveMsg.addReaction("❌").queue();
    }

    public static void doLeaveClan(GuildMessageReactionAddEvent event) {
        Database.leaveClan(event.getMessageId(), event.getUserId());
        event.getChannel().sendMessage(createEmbed.methode("**CLAN**", "You successfully left your Clan.", Color.green, null, null, null).build()).queue();
    }


    public static void sendClanInviteMessage(GuildMessageReceivedEvent event) {

        Message msg = event.getMessage();
        List<Member> members = msg.getMentionedMembers();
        Member author = event.getMember();
        TextChannel channel = event.getChannel();

        String role = Database.getClanRole(author.getId());
        Clan clan = Database.getClanOfUser(author.getId());

        if (!Database.isUserInClan(author.getId())){
            channel.sendMessage(createEmbed.methode("**ERROR**", "You need to be in a Clan to use this command", Color.red, null, null, null).build()).queue();
            return;
        }

        if (!(role.equalsIgnoreCase("Owner") || role.equalsIgnoreCase("Admin"))){
            channel.sendMessage(createEmbed.methode("**ERROR**", "You can't use this command. Only Clan-Owners and Clan-Admins can use this command.", Color.red, null, null, null).build()).queue();
            return;
        }

        if (members.size() < 1){
            channel.sendMessage(createEmbed.methode("**ERROR**", "Please mention @ at least one user to invite them", Color.red, null, null, null).build()).queue();
            return;
        }

        for (Member m : members){
            if (Database.isUserInClan(m.getId())){
                channel.sendMessage(createEmbed.methode("**ERROR**", "User "+m.getEffectiveName()+" is already in a Clan.", Color.red, null, null, null).build()).queue();
                continue;
            }
            Message inviteMessage = channel.sendMessage(createEmbed.methode("**CLAN**", "User "+author.getEffectiveName()+" invited you into their Clan `"+clan.getName()+"`.\n" +
                    "You can react to accept or deny the invitation.", Color.gray, null, null, clan.getUrl()).build()).complete();
            inviteMessage.addReaction("✅").queue();
            inviteMessage.addReaction("❌").queue();
            Database.insertClanMessage(clan, m.getId(), inviteMessage.getId(), "Invite");
        }


    }

    public static void doClanJoin(GuildMessageReactionAddEvent event) {
//        Database.joinClan(event.getUser().getId());
        event.getChannel().sendMessage(createEmbed.methode("**CLAN**", "You successfully left your Clan.", Color.green, null, null, null).build()).queue();
    }
}