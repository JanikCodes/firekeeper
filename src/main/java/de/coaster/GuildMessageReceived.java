package de.coaster;

import java.awt.*;
import java.util.List;
import de.commands.admin.setNotificationChannelID;
import de.commands.admin.setOnlyChannelID;
import de.commands.patreon.collectDaily;
import de.commands.patreon.giveSouls;
import de.commands.user.*;
import de.utilities.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildMessageReceived extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        try {
            String[] args = event.getMessage().getContentRaw().split("\\s+");
            String serverID = event.getGuild().getId();
            String prefix = Database.getPrefix(serverID);
            TextChannel currchat = event.getChannel();
            String allowedChannel = Database.getOnlyChannel(serverID);
            String allowedChannel2 = Database.getOnlyChannel2(serverID);

            if (args[0].equalsIgnoreCase(prefix + "help")) {
                if (checkChannel.methode(event, currchat, allowedChannel,allowedChannel2)) {
                    if (args.length == 1) {
                        //Displays whole help command list
                        help.methode(prefix,currchat);
                    } else if (args.length == 2) {
                        //more information about 1 command
                        help.moreInformation(args[1],currchat);
                    }
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "setprefix")) {
                if (checkChannel.methode(event, currchat, allowedChannel,allowedChannel2)) {
                    String memberID = event.getMember().getId();
                    String ownerID = event.getGuild().getOwnerId();
                    doesUserExist(event.getMember());

                    if (memberID.equals(ownerID)) {        //has permissions to do that
                        if (args.length == 2) {
                            if (args[1].length() <= 4) {
                                String oldPrefix = Database.getPrefix(serverID);
                                Database.changePrefix(serverID, args[1]);

                                EmbedBuilder messageStyle = createEmbed.methode("System", "You've successfully changed the prefix from `" + oldPrefix + "` to `" + args[1] + "`", Color.red, null, null, null);
                                currchat.sendMessage(messageStyle.build()).queue();
                            } else {
                                currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! The prefix cannot be longer than 4 characters! ", Color.red, null, null, null).build()).queue();
                            }
                        } else {
                            currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! Use `" + prefix + "help` to find the right syntax.", Color.red, null, null, null).build()).queue();
                        }
                    } else {
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "You don't have the permissions to execute this command! Only the **server owner** can use this command.", Color.red, null, null, null).build()).queue();
                    }
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "refreshDB")) {
                if (event.getMember().getId().equals("321649314382348288")) {
                    System.out.println("Adding data");
                    List<Member> allmembers = event.getGuild().getMembers();

                    Database.addMembersToDatabase(allmembers);
                    Database.getUsernames(allmembers);
                } else {
                    currchat.sendMessage(createEmbed.methode("**ERROR**", "You don't have the permissions to execute this command! Only the **Bot developer** can use this command.", Color.red, null, null, null).build()).queue();
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "giveSpecialToEveryone")) {
                if (event.getMember().getId().equals("321649314382348288")) {
                    System.out.println("Adding data");
                    List<User> realMemberCount = event.getJDA().getUsers();

                    for (int i = 0; i < realMemberCount.size(); i++) {
                        Database.giveItem(realMemberCount.get(i).getId(), Integer.parseInt(args[1]));
                    }
                } else {
                    currchat.sendMessage(createEmbed.methode("**ERROR**", "You don't have the permissions to execute this command! Only the **Bot developer** can use this command.", Color.red, null, null, null).build()).queue();
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "rank")) {
                if (checkChannel.methode(event, currchat, allowedChannel,allowedChannel2)) {
                    if (args.length == 1) {
                        doesUserExist(event.getMember());
                        getRank.methode(event.getMember(), event, prefix);
                    } else if (args.length == 2) {
                        if (event.getMessage().getMentions().size() > 0) {
                            Member member = event.getMessage().getMentionedMembers().get(0);
                            doesUserExist(member);
                            getRank.methode(member, event, prefix);
                        } else {
                            currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! Type `" + prefix + "help` to see all commands.", Color.red, null, null, null).build()).queue();
                        }
                    }
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "sell")) {
                if (checkChannel.methode(event, currchat, allowedChannel,allowedChannel2)) {
                    if (args.length == 1) {
                        doesUserExist(event.getMember());
                        sell.methode(event, prefix, currchat);
                    } else {
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! Type `" + prefix + "help` to see all commands.", Color.red, null, null, null).build()).queue();
                    }
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "patreon")) {
                if (checkChannel.methode(event, currchat, allowedChannel,allowedChannel2)) {
                    if (args.length == 1) {
                        currchat.sendMessage(createEmbed.methode("Support us!", "This [link](https://www.patreon.com/firekeeperbot?fan_landing=true) will send you to our **Patreon**! Support us there and gain alot of benefits, we **appreciate every support!** \n ❤", Color.pink, "If you bought anything, message janik#0737!", null, null).build()).queue();
                    } else {
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! Type `" + prefix + "help` to see all commands.", Color.red, null, null, null).build()).queue();
                    }
                }
            }


            if (args[0].equalsIgnoreCase(prefix + "achievements")) {
                if (checkChannel.methode(event, currchat, allowedChannel,allowedChannel2)) {
                    if (args.length == 1) {
                        doesUserExist(event.getMember());
                        String memberID = event.getMember().getId();
                        Database.deleteAchievementData(memberID);
                        checkRewards.methode(memberID,event.getMember());
                        achievements.methode(event.getMember(), currchat);
                    } else {
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! Type `" + prefix + "help` to see all commands.", Color.red, null, null, null).build()).queue();
                    }
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "setNotificationChannel")) {
                doesUserExist(event.getMember());
                String memberID = event.getMember().getId();
                String ownerID = event.getGuild().getOwnerId();
                if (memberID.equals(ownerID)) {        //has permissions to do that
                    if (args.length == 1) {
                        //Error
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! You'll need to type the desired channel as the second argument! e.g. `" + prefix + "setNotificationChannel #bot-cmds`", Color.red, null, null, null).build()).queue();
                    } else if (args.length == 2) {
                        setNotificationChannelID.methode(event, args[1], currchat);
                    } else {
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! You'll need to type the desired channel as the second argument! e.g. `" + prefix + "setNotificationChannel #bot-cmds`", Color.red, null, null, null).build()).queue();
                    }
                } else {
                    currchat.sendMessage(createEmbed.methode("**ERROR**", "You don't have the permissions to execute this command! Only the **server owner** can use this command.", Color.red, null, null, null).build()).queue();
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "setCommandsChannel")) {
                doesUserExist(event.getMember());
                String memberID = event.getMember().getId();
                String ownerID = event.getGuild().getOwnerId();
                if (memberID.equals(ownerID)) {        //has permissions to do that
                    if (args.length == 1) {
                        //Error
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! You'll need to type the desired channel as the second argument! e.g. `" + prefix + "setCommandsChannel #bot-cmds`", Color.red, null, null, null).build()).queue();
                    } else if (args.length == 2) {
                        setOnlyChannelID.methode(event, args[1], currchat,1);
                    } else {
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! You'll need to type the desired channel as the second argument! e.g. `" + prefix + "setCommandsChannel #bot-cmds`", Color.red, null, null, null).build()).queue();
                    }
                } else {
                    currchat.sendMessage(createEmbed.methode("**ERROR**", "You don't have the permissions to execute this command! Only the **server owner** can use this command.", Color.red, null, null, null).build()).queue();
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "setCommandsChannel2")) {
                doesUserExist(event.getMember());
                String memberID = event.getMember().getId();
                String ownerID = event.getGuild().getOwnerId();
                if (memberID.equals(ownerID)) {        //has permissions to do that
                    if (args.length == 1) {
                        //Error
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! You'll need to type the desired channel as the second argument! e.g. `" + prefix + "setCommandsChannel #bot-cmds`", Color.red, null, null, null).build()).queue();
                    } else if (args.length == 2) {
                        setOnlyChannelID.methode(event, args[1], currchat,2);
                    } else {
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! You'll need to type the desired channel as the second argument! e.g. `" + prefix + "setCommandsChannel #bot-cmds`", Color.red, null, null, null).build()).queue();
                    }
                } else {
                    currchat.sendMessage(createEmbed.methode("**ERROR**", "You don't have the permissions to execute this command! Only the **server owner** can use this command.", Color.red, null, null, null).build()).queue();
                }
            }


            if (args[0].equalsIgnoreCase(prefix + "profile")) {
                if (checkChannel.methode(event, currchat, allowedChannel,allowedChannel2)) {
                    if (args.length == 1) {
                        doesUserExist(event.getMember());
                        Database.deleteProfilRelation(event.getMember().getId());
                        getProfil.methode(event.getMember(), event, prefix, true);
                    } else if (args.length == 2) {
                        if (event.getMessage().getMentions().size() > 0) {
                            Member member = event.getMessage().getMentionedMembers().get(0);
                            doesUserExist(member);
                            getProfil.methode(member, event, prefix, false);
                        }
                    }
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "souls")) {
                if (checkChannel.methode(event, currchat, allowedChannel,allowedChannel2)) {

                    if (args.length == 1) {
                        doesUserExist(event.getMember());
                        getSouls.methode(event.getMember(), event, prefix);
                    } else if (args.length == 2) {
                        if (event.getMessage().getMentions().size() > 0) {
                            Member member = event.getMessage().getMentionedMembers().get(0);
                            doesUserExist(member);
                            getSouls.methode(member, event, prefix);
                        }
                    }
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "inventory")) {
                if (checkChannel.methode(event, currchat, allowedChannel,allowedChannel2)) {
                    if (args.length == 1) {
                        doesUserExist(event.getMember());
                        Database.deleteInventoryData(event.getMember());
                        checkRewards.methode(event.getMember().getId(), event.getMember());
                        inventory.methode(event.getMember(), currchat);
                    } else {
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! Type `" + prefix + "help` to see all commands.", Color.red, null, null, null).build()).queue();
                    }
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "how")) {
                if (checkChannel.methode(event, currchat, allowedChannel,allowedChannel2)) {
                    if (args.length == 1) {
                        doesUserExist(event.getMember());
                        how.methode(currchat);
                    }
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "cleartime")) {
                if (checkChannel.methode(event, currchat, allowedChannel,allowedChannel2)) {
                    if (args.length == 1) {
                        if (event.getMember().getId().equals("321649314382348288")) {
                            //Clear the time for everyone DEBUG TOOL
                            Database.clearAreaTime();
                            Database.clearBossTime();
                            Database.clearDailyTime();
                        } else {
                            currchat.sendMessage(createEmbed.methode("**ERROR**", "You don't have the permissions to execute this command! Only the **Bot developer** can use this command.", Color.red, null, null, null).build()).queue();
                        }
                    }
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "levelNotification")) {

                String memberID = event.getMember().getId();
                String ownerID = event.getGuild().getOwnerId();
                if (memberID.equals(ownerID)) { //has permissions to do that
                    if (args.length == 1) {
                        //Error
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! You need to type additional parameters, type `" + prefix + "levelNotification false` or `" + prefix + "levelNotification true`", Color.red, null, null, null).build()).queue();
                    } else if (args.length == 2) {
                        if (args[1].equals("true") || args[1].equals("TRUE")) {
                            Database.setNotificationState(serverID, 1);
                            currchat.sendMessage(createEmbed.methode("System", "Successfully changed the level up notification state to **true**", Color.red, null, null, null).build()).queue();
                        } else if (args[1].equals("false") || args[1].equals("FALSE")) {
                            doesUserExist(event.getMember());
                            Database.setNotificationState(serverID, 2);
                            currchat.sendMessage(createEmbed.methode("System", "Successfully changed the level up notification state to **false**", Color.red, null, null, null).build()).queue();
                        } else {
                            //Error
                            currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong input! Type `true` or `false`", Color.red, null, null, null).build()).queue();
                        }
                    }
                } else {
                    //Error
                    currchat.sendMessage(createEmbed.methode("**ERROR**", "You don't have the permissions to execute this command! Only the **server owner** can use this command.", Color.red, null, null, null).build()).queue();
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "vote")) {
                if (args.length == 1) {
                    currchat.sendMessage(createEmbed.methode("Vote", "You can use this link [top.gg](https://top.gg/bot/760993270133555231/vote) to vote! \n \uD83C\uDFC6 **Rewards:** \uD83C\uDFC6 \n - **reduced areaclear / bossfight cooldown for 12h!** \n - **5000 souls** \n - **1 random item**", Color.red, null, null, null).build()).queue();
                } else {
                    //Error
                    currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! Type `" + prefix + "help` to see all commands.", Color.red, null, null, null).build()).queue();
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "equip")) {
                if (checkChannel.methode(event, currchat, allowedChannel,allowedChannel2)) {
                    if (args.length == 3) {
                        doesUserExist(event.getMember());
                        equip.methode(args[1],args[2],event,currchat,prefix);
                    } else {
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! You can only use `weapon`, `armor` or `title` as the second argument! \n Example syntax: `" + prefix + "equip weapon 17`", Color.red, null, null, null).build()).queue();
                    }
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "upgrade")) {
                if (checkChannel.methode(event, currchat, allowedChannel,allowedChannel2)) {
                    if (args.length == 3) {
                        doesUserExist(event.getMember());
                        upgrade.methode(event,args[1],args[2],currchat,prefix);
                    } else {
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! You can only use `weapon`, `armor` as the second argument! \n Example syntax: `" + prefix + "upgrade weapon 17`", Color.red, null, null, null).build()).queue();
                    }
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "givesouls")) {
                if (checkChannel.methode(event, currchat, allowedChannel,allowedChannel2)) {
                    if (findRole.methode(event.getMember(), Main.tier1) != null || findRole.methode(event.getMember(), Main.tier2) != null) {
                        if (args.length == 3) {
                            doesUserExist(event.getMember());
                            giveSouls.methode(event,args[2],currchat);
                        } else {
                            currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! Type `" + prefix + "help` to see all commands.", Color.red, null, null, null).build()).queue();
                        }
                    } else {
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "You don't have the permissions to use this command! \n **Only patreon members** can use this command! \n Become a **patreon member** today and support us! Click [here](" + Main.patreonlink + ")! ❤", Color.red, null, null, null).build()).queue();
                    }
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "invite")) {
                if (args.length == 1) {
                    doesUserExist(event.getMember());
                    currchat.sendMessage(createEmbed.methode("Invite", "You can use this [link](" + Main.botAddLink + ") to invite this bot to your server! \n Thank you **so much** if you add this **bot** to your server! ❤️", Color.red, null, null, null).build()).queue();
                } else {
                    //Error
                    currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! Type `" + prefix + "help` to see all commands.", Color.red, null, null, null).build()).queue();
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "Daily")) {
                if (args.length == 1) {
                    if(findRole.methode(event.getMember(), Main.tier2) != null) {
                        doesUserExist(event.getMember());
                        collectDaily.methode(event.getMember().getId(), currchat);
                    }else{
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "**Only patreon members tier 2** can use this command! \n Become a **patreon member** today and support us! Click [here](" + Main.patreonlink + ")! ❤", Color.red, null, null, null).build()).queue();
                    }
                } else {
                    //Error
                    currchat.sendMessage(createEmbed.methode("**ERROR**", "**Only patreon members tier 2** can use this command! \n Become a **patreon member** today and support us! Click [here](" + Main.patreonlink + ")! ❤", Color.red, null, null, null).build()).queue();
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "links")) {
                if (args.length == 1) {
                    if (event.getMember().getId().equals("321649314382348288")) {
                        TextChannel linkschannel = event.getChannel().getGuild().getTextChannelById("772885344202391622");
                        linkschannel.sendMessage(createEmbed.methode("**Information**", "Thanks for using this bot! I **highly** appreciate it. \n \n \n Official top.gg website: [top.gg](https://top.gg/bot/760993270133555231) \n Support me to keep the bot alive! [paypal](https://www.paypal.com/paypalme/janiksielaff)", Color.red, "If you donate, please DM me!", null, null).build()).queue();
                    } else {
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "You don't have the permissions to execute this command! Only the **Bot developer** can use this command.", Color.red, null, null, null).build()).queue();
                    }
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "threads")) {
                if (args.length == 1) {
                    String mes = java.lang.Thread.activeCount() + " Threads";
                    currchat.sendMessage(mes).queue();
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "displayCD")) {
                if (args.length == 1) {
                    if(checkChannel.methode(event,currchat,allowedChannel,allowedChannel2)){
                        doesUserExist(event.getMember());
                        displaytime.methode(currchat,event);
                    }
                }else{
                    currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! Type `" + prefix + "help` to see all commands.", Color.red, null, null, null).build()).queue();
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "cleararea")) {
                if (checkChannel.methode(event, currchat, allowedChannel,allowedChannel2)) {
                    if (args.length == 1) {
                        doesUserExist(event.getMember());
                        clearArea.methode(event, currchat);
                    } else {
                        //Error
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! Type `" + prefix + "help` to see all commands.", Color.red, null, null, null).build()).queue();
                    }
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "serverinfo")) {
                if (checkChannel.methode(event, currchat, allowedChannel,allowedChannel2)) {
                    if (args.length == 1) {
                        serverinfo.methode(event,serverID,prefix,currchat);
                    } else {
                        //Error
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! Type `" + prefix + "help` to see all commands.", Color.red, null, null, null).build()).queue();
                    }
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "globalinfo")) {
                if (checkChannel.methode(event, currchat, allowedChannel,allowedChannel2)) {
                    if (args.length == 1) {
                        serverinfo.globalInfo(event,currchat);
                    } else {
                        //Error
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! Type `" + prefix + "help` to see all commands.", Color.red, null, null, null).build()).queue();
                    }
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "fightboss")) {
                if (checkChannel.methode(event, currchat, allowedChannel,allowedChannel2)) {
                    if (args.length == 1) {
                        doesUserExist(event.getMember());
                        Database.deleteBossFightRelation(event.getMember().getId());
                        fightBoss.methode(event, currchat);
                    } else {
                        //Error
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! Type `" + prefix + "help` to see all commands.", Color.red, null, null, null).build()).queue();
                    }
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "leaderboard")) {
                if (checkChannel.methode(event, currchat, allowedChannel,allowedChannel2)) {
                    if (args.length == 1) {
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! Type `" + prefix + "leaderboard ranks` or `" + prefix + "leaderboard souls` or \n`" + prefix + "leaderboard kills`", Color.red, null, null, null).build()).queue();
                    } else if (args.length > 1 && args.length <= 2) {
                        doesUserExist(event.getMember());
                        leaderboard.methode(args[1],event,currchat);
                    }
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "duel")) {
                if (checkChannel.methode(event, currchat, allowedChannel,allowedChannel2)) {
                    doesUserExist(event.getMember());
                    if (args.length == 2) {
                        if (event.getMessage().getMentions().size() > 0) {
                            User user = event.getMessage().getMentionedUsers().get(0);
                            Member member = event.getMessage().getMentionedMembers().get(0);
                            doesUserExist(member);
                            if (event.getAuthor() != user) {
                                currchat.sendMessage(createEmbed.methode("**PVP**", event.getMember().getAsMention() + " is challenging " + member.getAsMention() + " to a duel!", Color.red, "Accept or decline with the reactions below", null, null).build()).queue(message -> {
                                    message.addReaction("✅").queue();
                                    message.addReaction("❌").queue();
                                    Database.createPvpRelation(message.getId(), event.getMember().getId(), member.getId());
                                });

                            } else {
                                currchat.sendMessage(createEmbed.methode("**ERROR**", "You cannot duel yourself!", Color.red, null, null, null).build()).queue();
                            }
                        } else {
                            currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! Type `" + prefix + "help` to see all commands.", Color.red, null, null, null).build()).queue();
                        }
                    } else {
                        //Error
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! Type `" + prefix + "help` to see all commands.", Color.red, null, null, null).build()).queue();
                    }
                }
            }
        /*
            if (args[0].equalsIgnoreCase(prefix + "clan")){
                if (args.length==1){
                    //TODO send basic clan info message
                } else if (args[1].equalsIgnoreCase("create")){
                    if (args.length >= 4){
                        clanLogic.createClan(event, args);
                    }
                } else if (args[1].equalsIgnoreCase("leave")){
                    clanLogic.sendLeaveClan(event);
                } else if (args[1].equalsIgnoreCase("invite")){
                    clanLogic.sendClanInviteMessage(event);
                }
            }

        */
        } catch (InsufficientPermissionException exception) {
        }catch (NullPointerException p){
        }
    }

    public static void doesUserExist(Member member){
        Database.addMemberToTable(member.getId(),member.getEffectiveName());
    }
}
