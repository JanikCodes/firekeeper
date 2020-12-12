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

            if (args[0].equalsIgnoreCase(prefix + "help")) {
                if (checkChannel.methode(event, currchat, allowedChannel)) {
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
                if (checkChannel.methode(event, currchat, allowedChannel)) {
                    String memberID = event.getMember().getId();
                    String ownerID = event.getGuild().getOwnerId();
                    if (memberID.equals(ownerID)) {        //has permissions to do that
                        if (args.length == 2) {
                            if (args[1].length() <= 4) {
                                String oldPrefix = Database.getPrefix(serverID);
                                Database.changePrefix(serverID, args[1]);

                                EmbedBuilder messageStyle = createEmbed.methode("System", "You've successfully changed the prefix from `" + oldPrefix + "` to `" + args[1] + "`", Color.red, null, null, null);
                                currchat.sendMessage(messageStyle.build()).complete();
                            } else {
                                currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! The prefix cannot be longer than 4 characters! ", Color.red, null, null, null).build()).complete();
                            }
                        } else {
                            currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! Use `" + prefix + "help` to find the right syntax.", Color.red, null, null, null).build()).complete();
                        }
                    } else {
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "You don't have the permissions to execute this command! Only the **server owner** can use this command.", Color.red, null, null, null).build()).complete();
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
                    currchat.sendMessage(createEmbed.methode("**ERROR**", "You don't have the permissions to execute this command! Only the **Bot developer** can use this command.", Color.red, null, null, null).build()).complete();
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
                    currchat.sendMessage(createEmbed.methode("**ERROR**", "You don't have the permissions to execute this command! Only the **Bot developer** can use this command.", Color.red, null, null, null).build()).complete();
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "rank")) {
                if (checkChannel.methode(event, currchat, allowedChannel)) {
                    if (args.length == 1) {
                        getRank.methode(event.getMember(), event, prefix);
                    } else if (args.length == 2) {
                        if (event.getMessage().getMentions().size() > 0) {
                            Member member = event.getMessage().getMentionedMembers().get(0);
                            getRank.methode(member, event, prefix);
                        } else {
                            currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! Type `" + prefix + "help` to see all commands.", Color.red, null, null, null).build()).complete();
                        }
                    }
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "sell")) {
                if (checkChannel.methode(event, currchat, allowedChannel)) {
                    if (args.length == 1) {
                        sell.methode(event, prefix, currchat);
                    } else {
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! Type `" + prefix + "help` to see all commands.", Color.red, null, null, null).build()).complete();
                    }
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "patreon")) {
                if (checkChannel.methode(event, currchat, allowedChannel)) {
                    if (args.length == 1) {
                        currchat.sendMessage(createEmbed.methode("Support us!", "This [link](https://www.patreon.com/firekeeperbot?fan_landing=true) will send you to our **Patreon**! Support us there and gain alot of benefits, we **appreciate every support!** \n ❤", Color.pink, "If you bought anything, message janik#0737!", null, null).build()).queue();
                    } else {
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! Type `" + prefix + "help` to see all commands.", Color.red, null, null, null).build()).complete();
                    }
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "achievements")) {
                if (checkChannel.methode(event, currchat, allowedChannel)) {
                    if (args.length == 1) {
                        String memberID = event.getMember().getId();
                        Database.deleteAchievementData(memberID);
                        checkRewards.methode(memberID,event.getMember());
                        achievements.methode(event.getMember(), currchat);
                    } else {
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! Type `" + prefix + "help` to see all commands.", Color.red, null, null, null).build()).complete();
                    }
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "setNotificationChannel")) {

                String memberID = event.getMember().getId();
                String ownerID = event.getGuild().getOwnerId();
                if (memberID.equals(ownerID)) {        //has permissions to do that
                    if (args.length == 1) {
                        //Error
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! You'll need to type the desired channel as the second argument! e.g. `" + prefix + "setNotificationChannel #bot-cmds`", Color.red, null, null, null).build()).complete();
                    } else if (args.length == 2) {
                        setNotificationChannelID.methode(event, args[1], currchat);
                    } else {
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! You'll need to type the desired channel as the second argument! e.g. `" + prefix + "setNotificationChannel #bot-cmds`", Color.red, null, null, null).build()).complete();
                    }
                } else {
                    currchat.sendMessage(createEmbed.methode("**ERROR**", "You don't have the permissions to execute this command! Only the **server owner** can use this command.", Color.red, null, null, null).build()).complete();
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "setCommandsChannel")) {

                String memberID = event.getMember().getId();
                String ownerID = event.getGuild().getOwnerId();
                if (memberID.equals(ownerID)) {        //has permissions to do that
                    if (args.length == 1) {
                        //Error
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! You'll need to type the desired channel as the second argument! e.g. `" + prefix + "setCommandsChannel #bot-cmds`", Color.red, null, null, null).build()).complete();
                    } else if (args.length == 2) {
                        setOnlyChannelID.methode(event, args[1], currchat);
                    } else {
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! You'll need to type the desired channel as the second argument! e.g. `" + prefix + "setCommandsChannel #bot-cmds`", Color.red, null, null, null).build()).complete();
                    }
                } else {
                    currchat.sendMessage(createEmbed.methode("**ERROR**", "You don't have the permissions to execute this command! Only the **server owner** can use this command.", Color.red, null, null, null).build()).complete();
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "profile")) {
                if (checkChannel.methode(event, currchat, allowedChannel)) {
                    if (args.length == 1) {
                        Database.deleteProfilRelation(event.getMember().getId());
                        getProfil.methode(event.getMember(), event, prefix, true);
                    } else if (args.length == 2) {
                        if (event.getMessage().getMentions().size() > 0) {
                            Member member = event.getMessage().getMentionedMembers().get(0);
                            getProfil.methode(member, event, prefix, false);
                        }
                    }
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "souls")) {
                if (checkChannel.methode(event, currchat, allowedChannel)) {

                    if (args.length == 1) {
                        getSouls.methode(event.getMember(), event, prefix);
                    } else if (args.length == 2) {
                        if (event.getMessage().getMentions().size() > 0) {
                            Member member = event.getMessage().getMentionedMembers().get(0);
                            getSouls.methode(member, event, prefix);
                        }
                    }
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "inventory")) {
                if (checkChannel.methode(event, currchat, allowedChannel)) {
                    if (args.length == 1) {
                        Database.deleteInventoryData(event.getMember());
                        checkRewards.methode(event.getMember().getId(), event.getMember());
                        inventory.methode(event.getMember(), currchat);
                    } else {
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! Type `" + prefix + "help` to see all commands.", Color.red, null, null, null).build()).complete();
                    }
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "how")) {
                if (checkChannel.methode(event, currchat, allowedChannel)) {
                    if (args.length == 1) {
                        how.methode(currchat);
                    }
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "cleartime")) {
                if (checkChannel.methode(event, currchat, allowedChannel)) {
                    if (args.length == 1) {
                        if (event.getMember().getId().equals("321649314382348288")) {
                            //Clear the time for everyone DEBUG TOOL
                            Database.clearAreaTime();
                            Database.clearBossTime();
                            Database.clearDailyTime();
                        } else {
                            currchat.sendMessage(createEmbed.methode("**ERROR**", "You don't have the permissions to execute this command! Only the **Bot developer** can use this command.", Color.red, null, null, null).build()).complete();
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
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! You need to type additional parameters, type `" + prefix + "levelNotification false` or `" + prefix + "levelNotification true`", Color.red, null, null, null).build()).complete();
                    } else if (args.length == 2) {
                        if (args[1].equals("true") || args[1].equals("TRUE")) {
                            Database.setNotificationState(serverID, 1);
                            currchat.sendMessage(createEmbed.methode("System", "Successfully changed the level up notification state to **true**", Color.red, null, null, null).build()).complete();
                        } else if (args[1].equals("false") || args[1].equals("FALSE")) {
                            Database.setNotificationState(serverID, 2);
                            currchat.sendMessage(createEmbed.methode("System", "Successfully changed the level up notification state to **false**", Color.red, null, null, null).build()).complete();
                        } else {
                            //Error
                            currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong input! Type `true` or `false`", Color.red, null, null, null).build()).complete();
                        }
                    }
                } else {
                    //Error
                    currchat.sendMessage(createEmbed.methode("**ERROR**", "You don't have the permissions to execute this command! Only the **server owner** can use this command.", Color.red, null, null, null).build()).complete();
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "vote")) {
                if (args.length == 1) {
                    currchat.sendMessage(createEmbed.methode("Vote", "You can use this link [top.gg](https://top.gg/bot/760993270133555231/vote) to vote! \n \uD83C\uDFC6 **Rewards:** \uD83C\uDFC6 \n - **reduced areaclear / bossfight cooldown for 12h!** \n - **5000 souls** \n - **1 random item**", Color.red, null, null, null).build()).queue();
                } else {
                    //Error
                    currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! Type `" + prefix + "help` to see all commands.", Color.red, null, null, null).build()).complete();
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "equip")) {
                if (checkChannel.methode(event, currchat, allowedChannel)) {
                    if (args.length == 3) {
                        equip.methode(args[1],args[2],event,currchat,prefix);
                    } else {
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! You can only use `weapon`, `armor` or `title` as the second argument! \n Example syntax: `" + prefix + "equip weapon 17`", Color.red, null, null, null).build()).complete();
                    }
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "upgrade")) {
                if (checkChannel.methode(event, currchat, allowedChannel)) {
                    if (args.length == 3) {
                        upgrade.methode(event,args[1],args[2],currchat,prefix);
                    } else {
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! You can only use `weapon`, `armor` as the second argument! \n Example syntax: `" + prefix + "upgrade weapon 17`", Color.red, null, null, null).build()).complete();
                    }
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "givesouls")) {
                if (checkChannel.methode(event, currchat, allowedChannel)) {
                    if (findRole.methode(event.getMember(), Main.tier1) != null || findRole.methode(event.getMember(), Main.tier2) != null) {
                        if (args.length == 3) {
                            giveSouls.methode(event,args[2],currchat);
                        } else {
                            currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! Type `" + prefix + "help` to see all commands.", Color.red, null, null, null).build()).complete();
                        }
                    } else {
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "You don't have the permissions to use this command! \n **Only patreon members** can use this command! \n Become a **patreon member** today and support us! Click [here](" + Main.patreonlink + ")! ❤", Color.red, null, null, null).build()).complete();
                    }
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "invite")) {
                if (args.length == 1) {
                    currchat.sendMessage(createEmbed.methode("Invite", "You can use this [link](" + Main.botAddLink + ") to invite this bot to your server! \n Thank you **so much** if you add this **bot** to your server! ❤️", Color.red, null, null, null).build()).queue();
                } else {
                    //Error
                    currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! Type `" + prefix + "help` to see all commands.", Color.red, null, null, null).build()).complete();
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "Daily")) {
                if (args.length == 1) {
                    if(findRole.methode(event.getMember(), Main.tier2) != null) {
                        collectDaily.methode(event.getMember().getId(), currchat);
                    }else{
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "**Only patreon members tier 2** can use this command! \n Become a **patreon member** today and support us! Click [here](" + Main.patreonlink + ")! ❤", Color.red, null, null, null).build()).complete();
                    }
                } else {
                    //Error
                    currchat.sendMessage(createEmbed.methode("**ERROR**", "**Only patreon members tier 2** can use this command! \n Become a **patreon member** today and support us! Click [here](" + Main.patreonlink + ")! ❤", Color.red, null, null, null).build()).complete();
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "links")) {
                if (args.length == 1) {
                    if (event.getMember().getId().equals("321649314382348288")) {
                        TextChannel linkschannel = event.getChannel().getGuild().getTextChannelById("772885344202391622");
                        linkschannel.sendMessage(createEmbed.methode("**Information**", "Thanks for using this bot! I **highly** appreciate it. \n \n \n Official top.gg website: [top.gg](https://top.gg/bot/760993270133555231) \n Support me to keep the bot alive! [paypal](https://www.paypal.com/paypalme/janiksielaff)", Color.red, "If you donate, please DM me!", null, null).build()).complete();
                    } else {
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "You don't have the permissions to execute this command! Only the **Bot developer** can use this command.", Color.red, null, null, null).build()).complete();
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
                    if(checkChannel.methode(event,currchat,allowedChannel)){
                        displaytime.methode(currchat,event);
                    }
                }else{
                    currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! Type `" + prefix + "help` to see all commands.", Color.red, null, null, null).build()).complete();
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "cleararea")) {
                if (checkChannel.methode(event, currchat, allowedChannel)) {
                    if (args.length == 1) {
                        clearArea.methode(event, currchat);
                    } else {
                        //Error
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! Type `" + prefix + "help` to see all commands.", Color.red, null, null, null).build()).complete();
                    }
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "serverinfo")) {
                if (checkChannel.methode(event, currchat, allowedChannel)) {
                    if (args.length == 1) {
                        serverinfo.methode(event,serverID,prefix,currchat);
                    } else {
                        //Error
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! Type `" + prefix + "help` to see all commands.", Color.red, null, null, null).build()).complete();
                    }
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "globalinfo")) {
                if (checkChannel.methode(event, currchat, allowedChannel)) {
                    if (args.length == 1) {
                        serverinfo.globalInfo(event,currchat);
                    } else {
                        //Error
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! Type `" + prefix + "help` to see all commands.", Color.red, null, null, null).build()).complete();
                    }
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "fightboss")) {
                if (checkChannel.methode(event, currchat, allowedChannel)) {
                    if (args.length == 1) {
                        Database.deleteBossFightRelation(event.getMember().getId());
                        fightBoss.methode(event, currchat);
                    } else {
                        //Error
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! Type `" + prefix + "help` to see all commands.", Color.red, null, null, null).build()).complete();
                    }
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "leaderboard")) {
                if (checkChannel.methode(event, currchat, allowedChannel)) {
                    if (args.length == 1) {
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! Type `" + prefix + "leaderboard ranks` or `" + prefix + "leaderboard souls` or \n`" + prefix + "leaderboard kills`", Color.red, null, null, null).build()).complete();
                    } else if (args.length > 1 && args.length <= 2) {
                        leaderboard.methode(args[1],event,currchat);
                    }
                }
            }

            if (args[0].equalsIgnoreCase(prefix + "duel")) {
                if (checkChannel.methode(event, currchat, allowedChannel)) {
                    if (args.length == 2) {
                        if (event.getMessage().getMentions().size() > 0) {
                            Member member = event.getMessage().getMentionedMembers().get(0);

                            if (event.getMember() != member) {
                                Message finalmessage = currchat.sendMessage(createEmbed.methode("**PVP**", event.getMember().getAsMention() + " is challenging " + member.getAsMention() + " to a duel!", Color.red, "Accept or decline with the reactions below", null, null).build()).complete();
                                Database.createPvpRelation(finalmessage.getId(), event.getMember().getId(), member.getId());
                                finalmessage.addReaction("✅").queue();
                                finalmessage.addReaction("❌").queue();
                            } else {
                                currchat.sendMessage(createEmbed.methode("**ERROR**", "You cannot duel yourself!", Color.red, null, null, null).build()).complete();
                            }
                        } else {
                            currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! Type `" + prefix + "help` to see all commands.", Color.red, null, null, null).build()).complete();
                        }
                    } else {
                        //Error
                        currchat.sendMessage(createEmbed.methode("**ERROR**", "Wrong Syntax! Type `" + prefix + "help` to see all commands.", Color.red, null, null, null).build()).complete();
                    }
                }
            }

        } catch (InsufficientPermissionException exception) { }
    }

    /*
    public static void fightPlayer(String idMember1, String idMember2, GuildMessageReactionAddEvent event, TextChannel currchat) {
        final int[] count1 = {0};
        final int[] count2 = {0};
        final int[] dodgecount1 = {0};
        final int[] dodgecount2 = {0};

        int vitality1 = Database.getStatistic("Vitality", idMember1);
        int strength1 = Database.getStatistic("Strength", idMember1);
        int dexterity1 = Database.getStatistic("Dexterity", idMember1);
        int resistance1 = Database.getStatistic("Resistance", idMember1);
        int intelligence1 = Database.getStatistic("Intelligence", idMember1);
        int faith1 = Database.getStatistic("Faith", idMember1);

        int vitality2 = Database.getStatistic("Vitality", idMember2);
        int strength2 = Database.getStatistic("Strength", idMember2);
        int dexterity2 = Database.getStatistic("Dexterity", idMember2);
        int resistance2 = Database.getStatistic("Resistance", idMember2);
        int intelligence2 = Database.getStatistic("Intelligence", idMember2);
        int faith2 = Database.getStatistic("Faith", idMember2);

        //Real player stats
        int playerHealth1 = (vitality1 * 10) + 50;
        final int[] currPlayerHealth1 = {playerHealth1};


        int playerHealth2 = (vitality2 * 10) + 50;
        final int[] currPlayerHealth2 = {playerHealth2};

        Member plr1Name = event.getGuild().getMemberById(idMember1);
        Member plr2Name = event.getGuild().getMemberById(idMember2);

        int weaponDamage1 = Database.getWeaponDamage(idMember1);
        int weaponDamage2 = Database.getWeaponDamage(idMember2);

        int armor1 = Database.getArmorBonus(idMember1);
        int armor2 = Database.getArmorBonus(idMember2);

        //Game stats
        int reducePlayerHealth1 = 20 + weaponDamage2 + (strength2 + dexterity2 - (resistance1 - armor1));
        int reducePlayerHealth2 = 20 + weaponDamage1 + (strength1 + dexterity1 - (resistance2 - armor2));

        if (reducePlayerHealth2 < 20) {
            reducePlayerHealth2 = 20;
        }
        if (reducePlayerHealth1 < 20) {
            reducePlayerHealth1 = 20;
        }

        final int[] roundCount = {0};
        EmbedBuilder finalmessage = createEmbed("**PVP Duel**", initDescriptionPvp(event, idMember1, idMember2, currPlayerHealth1[0], playerHealth1, currPlayerHealth2[0], playerHealth2, "", ""), Color.darkGray, null, null, null);

        String messID = currchat.sendMessage(finalmessage.build()).complete().getId();

        Message message = event.getChannel().retrieveMessageById(messID).complete();
        List<MessageEmbed> lmess = message.getEmbeds();
        MessageEmbed emb = lmess.get(0);

        String[] pvp = {
                " slashed him and dealt", " backstabed him and dealt", " made a good hit which dealt", " got him good and dealt", " hit him perfectly in the head which dealt", " poked him and dealt"
        };

        //Thanks for submiting felix ehre
        ExecutorService exec = Executors.newSingleThreadExecutor();
        int finalReducePlayerHealth = reducePlayerHealth2;
        int finalReducePlayerHealth1 = reducePlayerHealth1;

        exec.execute(() -> {
            message.editMessage(createEmbed(emb.getTitle(), initDescriptionPvp(event, idMember1, idMember2, currPlayerHealth1[0], playerHealth1, currPlayerHealth2[0], playerHealth2, "", "") + "\n \n \uD83D\uDCCD Stage: **1**\n" + "Both players are ready to **duel**!", Color.darkGray, null, null, null).build()).queue();

            try {
                Thread.sleep(3000); //Wait time
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (currPlayerHealth1[0] > 0 || currPlayerHealth2[0] > 0) {
                //Stage progress
                roundCount[0]++;

                int whoWon = getRandomNumberInRange(1, 2);
                int rnd = getRandomNumberInRange(0, pvp.length - 1);
                String Sentence = pvp[rnd];
                int dmgtestPlayer1 = 1;
                int dmgtestPlayer2 = 1;
                if (whoWon == 1) {
                    //player 1 won
                    int dodgeNum = getRandomNumberInRange(1, 20);
                    int healNum = getRandomNumberInRange(1, 25);

                    int dodgeChance2 = getChance(intelligence2);
                    int healChance2 = getChance(faith2);

                    if (dodgeNum < dodgeChance2) {
                        dodgecount1[0]++;
                        message.editMessage(createEmbed(emb.getTitle(), initDescriptionPvp(event, idMember1, idMember2, currPlayerHealth1[0], playerHealth1, currPlayerHealth2[0], playerHealth2, " Dodged!", "") + "\n \n \uD83D\uDCCD Stage: **" + roundCount[0] + "**\n You successfully **dodged** the attack!", Color.white, null, null, null).build()).queue();
                    } else if (healNum < healChance2 && currPlayerHealth2[0] != playerHealth2) {
                        int healAmount = getHealAmount(currPlayerHealth2[0], finalReducePlayerHealth, playerHealth2);
                        currPlayerHealth2[0] = currPlayerHealth2[0] + healAmount;
                        message.editMessage(createEmbed(emb.getTitle(), initDescriptionPvp(event, idMember1, idMember2, currPlayerHealth1[0], playerHealth1, currPlayerHealth2[0], playerHealth2, " Healed " + healAmount, "") + "\n \n \uD83D\uDCCD Stage: **" + roundCount[0] + "**\n You've healed yourself with a flask \n and recieved **" + healAmount + "** health!", Color.white, null, null, null).build()).queue();
                    } else {
                        int dice1 = getRandomNumberInRange(1, 10);
                        dmgtestPlayer1 = dice1 * strength2 / 10;
                        if (currPlayerHealth2[0] - (finalReducePlayerHealth + dmgtestPlayer1) <= 0) {
                            currPlayerHealth2[0] = 0;
                        } else {
                            currPlayerHealth2[0] = currPlayerHealth2[0] - (finalReducePlayerHealth + dmgtestPlayer1);
                        }
                        count2[0]++;
                        message.editMessage(createEmbed(emb.getTitle(), initDescriptionPvp(event, idMember1, idMember2, currPlayerHealth1[0], playerHealth1, currPlayerHealth2[0], playerHealth2, " -**" + (finalReducePlayerHealth + dmgtestPlayer1) + "**", "") + "\n \n \uD83D\uDCCD Stage: **" + roundCount[0] + "**\n" + plr1Name.getEffectiveName() + Sentence + " **" + (finalReducePlayerHealth + dmgtestPlayer1) + "** damage!", Color.green, null, null, null).build()).queue();
                    }
                } else {
                    //player 2 won
                    int dodgeNum = getRandomNumberInRange(1, 20);
                    int healNum = getRandomNumberInRange(1, 25);

                    int dodgeChance1 = getChance(intelligence1);
                    int healChance1 = getChance(faith1);

                    if (dodgeNum < dodgeChance1) {
                        dodgecount2[0]++;
                        message.editMessage(createEmbed(emb.getTitle(), initDescriptionPvp(event, idMember1, idMember2, currPlayerHealth1[0], playerHealth1, currPlayerHealth2[0], playerHealth2, "", " Dodged!") + "\n \n \uD83D\uDCCD Stage: **" + roundCount[0] + "**\n " + plr1Name.getEffectiveName() + " successfully **dodged** the attack!", Color.white, null, null, null).build()).queue();
                    } else if (healNum < healChance1 && currPlayerHealth1[0] != playerHealth1) {
                        int healAmount = getHealAmount(currPlayerHealth1[0], finalReducePlayerHealth1, playerHealth1);
                        currPlayerHealth1[0] = currPlayerHealth1[0] + healAmount;
                        message.editMessage(createEmbed(emb.getTitle(), initDescriptionPvp(event, idMember1, idMember2, currPlayerHealth1[0], playerHealth1, currPlayerHealth2[0], playerHealth2, "", " Healed " + healAmount) + "\n \n \uD83D\uDCCD Stage: **" + roundCount[0] + "**\n " + plr1Name.getEffectiveName() + " healed himself with a flask \n and recieved **" + healAmount + "** health!", Color.white, null, null, null).build()).queue();
                    } else {
                        int dice2 = getRandomNumberInRange(1, 10);
                        dmgtestPlayer2 = dice2 * strength2 / 10;
                        if (currPlayerHealth1[0] - (finalReducePlayerHealth1 + dmgtestPlayer2) <= 0) {
                            currPlayerHealth1[0] = 0;
                        } else {
                            currPlayerHealth1[0] = currPlayerHealth1[0] - (finalReducePlayerHealth1 + dmgtestPlayer2);
                        }
                        count1[0]++;
                        message.editMessage(createEmbed(emb.getTitle(), initDescriptionPvp(event, idMember1, idMember2, currPlayerHealth1[0], playerHealth1, currPlayerHealth2[0], playerHealth2, "", " -**" + (finalReducePlayerHealth1 + dmgtestPlayer2) + "**") + "\n \n \uD83D\uDCCD Stage: **" + roundCount[0] + "**\n" + plr2Name.getEffectiveName() + Sentence + " **" + (finalReducePlayerHealth1 + dmgtestPlayer2) + "** damage!", Color.green, null, null, null).build()).queue();
                    }
                }

                if (currPlayerHealth1[0] <= 0) {
                    try {
                        Thread.sleep(3000); //Wait time
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (idMember1.equals("321649314382348288")) {
                        if (Database.completeAchievement(idMember2, 10)) {
                            rewardUser(idMember2, "item", 23);
                        }
                    }

                    if (count1[0] == 0) {
                        if (Database.completeAchievement(idMember1, 2)) {
                            rewardUser(idMember1, "souls", 12500);
                        }
                    } else if (count2[0] == 0) {
                        if (Database.completeAchievement(idMember2, 2)) {
                            rewardUser(idMember2, "souls", 12500);
                        }
                    }

                    if (dodgecount1[0] >= 6) {
                        if (Database.completeAchievement(idMember1, 3)) {
                            rewardUser(idMember1, "title", 1);
                        }
                    }
                    if (dodgecount2[0] >= 6) {
                        if (Database.completeAchievement(idMember2, 3)) {
                            rewardUser(idMember2, "title", 1);
                        }
                    }

                    if (Database.completeAchievement(idMember2, 6)) {
                        rewardUser(idMember2, "title", 2);
                    }

                    int healthleft = (int) (10 * playerHealth2) / 100;

                    if (currPlayerHealth2[0] <= healthleft) {
                        if (Database.completeAchievement(idMember2, 18)) {
                            rewardUser(idMember2, "title", 9);
                        }
                    }
                    Database.giveKill(idMember2);

                    message.editMessage(createEmbed(emb.getTitle(), initDescriptionPvp(event, idMember1, idMember2, currPlayerHealth1[0], playerHealth1, currPlayerHealth2[0], playerHealth2, "", "") + "\n \n \uD83D\uDCCD Stage: **" + roundCount[0] + "**\n **" + plr2Name.getEffectiveName() + " won!**", Color.orange, null, null, null).build()).queue();
                    break;
                }

                if (currPlayerHealth2[0] <= 0) {
                    try {
                        Thread.sleep(3000); //Wait time
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (idMember2.equals("321649314382348288")) {
                        if (Database.completeAchievement(idMember1, 10)) {
                            rewardUser(idMember1, "item", 23);
                        }
                    }

                    if (count1[0] == 0) {
                        if (Database.completeAchievement(idMember1, 2)) {
                            rewardUser(idMember1, "souls", 12500);
                        }
                    } else if (count2[0] == 0) {
                        if (Database.completeAchievement(idMember2, 2)) {
                            rewardUser(idMember2, "souls", 12500);
                        }
                    }

                    if (dodgecount1[0] >= 6) {
                        if (Database.completeAchievement(idMember1, 3)) {
                            rewardUser(idMember1, "title", 1);

                        }
                    }
                    if (dodgecount2[0] >= 6) {
                        if (Database.completeAchievement(idMember2, 3)) {
                            rewardUser(idMember2, "title", 1);

                        }
                    }

                    if (Database.completeAchievement(idMember1, 6)) {
                        rewardUser(idMember1, "title", 2);
                    }
                    int healthleft = (int) (10 * playerHealth1) / 100;

                    if (currPlayerHealth1[0] <= healthleft) {
                        if (Database.completeAchievement(idMember1, 18)) {
                            rewardUser(idMember1, "title", 9);
                        }
                    }
                    Database.giveKill(idMember1);

                    message.editMessage(createEmbed(emb.getTitle(), initDescriptionPvp(event, idMember1, idMember2, currPlayerHealth1[0], playerHealth1, currPlayerHealth2[0], playerHealth2, "", "") + "\n \n \uD83D\uDCCD Stage: **" + roundCount[0] + "**\n **" + plr1Name.getEffectiveName() + " won!**", Color.orange, null, null, null).build()).queue();
                    break;
                }

                if (roundCount[0] >= 40) {
                    Database.completeAchievement(idMember1, 19);
                    Database.completeAchievement(idMember2, 19);
                    //No rewards
                }

                try {
                    Thread.sleep(3000); //Wait time
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            exec.shutdown();
        });
    }

     */
}
