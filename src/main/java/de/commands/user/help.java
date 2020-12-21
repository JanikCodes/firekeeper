package de.commands.user;

import de.utilities.createEmbed;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class help {

    static String[][] cmdsUsers = {
            {"PROFILE", "profile", "**Displays your character statistics!** \n You can also view other peoples profiles by typing an additional mention after the command! e.g. `!profile @person`"},
            {"CLEARAREA", "cleararea", "Use this command to **clear** an combat **area** to gain **souls**! \n You can use this command every **4 hours**!"},
            {"FIGHTBOSS", "fightboss", "Use this command to randomly fight against a boss! \n You can use this command every **3h**! \n Make sure you have enough levels in **vitality, strength and dexterity**"},
            {"DUEL", "duel", "Use this command to fight another player in a PVP duel!"},
            {"INVENTORY", "inventory", "Opens your inventory with 5 pages! Everything on **page 1** can be sold! \n **Page 2** is for weapons and **page 3** is for armor. \n **Page 4** are for **special items**, they don't have any purpose or function besides beeing collectibles. \n **Page 5** is for titles that you can earn by completing achievements!"},
            {"ACHIEVEMENTS", "achievements", "This command is used to display all your achievements! \n It''ll also show you which one you have completed and the rewards."},
            {"EQUIP", "equip", "Use this command to equip weapons/armor or titles! \n Example: This command needs 3 arguments, e.g. `!equip armor 22` or `!equip weapon 17`"},
            {"UPGRADE", "upgrade", "Use this command to upgrade weapons/armor \n Example: This command needs 3 arguments, e.g. `!upgrade armor 22` or `!upgrade weapon 17`"},
            {"HOW", "how", "Displays you some relevant informations about the bot and how it works."},
            {"SELL", "sell", "Use this command to sell everything in your inventory on page 1!"},
            {"LEADERBOARD", "leaderboard", "Displays the **top 15** players. \n There are 3 leaderboards at the moment, `ranks` and `souls` and `kills`!"},
            {"RANK", "rank", "Shows your **level** and **global position** on the leaderboard! \n You can also see your XP-bar!"},
            {"DISPLAYCD", "displayCD", "Displays all cooldowns!"},
            {"VOTE", "vote", "Use this command to vote on top.gg \n It helps the Bot and the user who voted. Users who voted will recieve **1 random item, 5k souls and** **reduced Bossfight/Cleararea cooldown**!"},
            {"SOULS", "souls", "Displays your souls. \n You can also view other peoples souls by mentioning them after the command \n e.g. `!souls @person`"},
            {"SERVERINFO", "serverinfo", "This command is used to display all server related information."},
            {"GLOBALINFO", "globalinfo", "This command is used to display all information across all servers!"},
            {"INVITE", "invite", "This command is used to **invite the bot** to your own server"},
            {"PATREON", "patreon", "This command is used to visit our Patreon! You can get alot of benefits by supporting us! \n **Thanks!** ❤"},

    };

    static String[][] cmdsAdmins = {
            {"SETPREFIX", "setprefix [new prefix]", "Changes the server prefix for this bot."},
            {"LEVELNOTIFICATION", "levelNotification [true/false]", "Determinds if the bot will alert the user that he has leveled up!"},
            {"SETNOTIFICATIONCHANNEL", "setNotificationChannel [#channel]", "Configures the channel where the level up notification will be send!"},
            {"SETCOMMANDSCHANNEL", "setCommandsChannel [#channel]", "Configures the channel where user can write the commands! \n If this is not set, users will be able to type commands in every channel."},
            {"SETCOMMANDSCHANNEL2","setCommandsChannel2 [#channel]","Configures the second channel where user can write the commands!"},
            {"SETCOMMANDSCHANNEL3","setCommandsChannel3 [#channel]","Configures the third channel where user can write the commands!"}
    };

    static String[][] cmdsPatreon = {
            {"GIVESOULS", "giveSouls [@person] [amount]", "Coming soon"},
            {"DAILY", "Daily", "Coming soon"},
    };

    public static void methode(String prefix, TextChannel currchat){
        EmbedBuilder ms = new EmbedBuilder();
        ms.setTitle("System");
        ms.setColor(Color.red);
        ms.setDescription("Type additional arguments in the **[ ]**");

        String userString = "";
        String adminString = "";
        String patreonString = "";

        for (int c = 0; c < cmdsUsers.length; c = c + 2) {
            if (c + 1 == cmdsUsers.length) {
                userString = userString + "`" + prefix + cmdsUsers[c][1] + "`";
            } else {
                userString = userString + "`" + prefix + cmdsUsers[c][1] + "` `" + prefix + cmdsUsers[c + 1][1] + "` ";
            }
        }
        for (int x = 0; x < cmdsAdmins.length; x++) {
            adminString = adminString + "`" + prefix + cmdsAdmins[x][1] + "` \n ";
        }
        for (int x = 0; x < cmdsPatreon.length; x++) {
            patreonString = patreonString + "`" + prefix + cmdsPatreon[x][1] + "` \n ";
        }

        ms.addField("Users", userString, false);
        ms.addField("Admins", adminString, false);
        ms.addField("Patreon", patreonString, false);
        currchat.sendMessage(ms.build()).queue();

    }

    public static void moreInformation(String arg,TextChannel currchat){
        for (int c = 0; c < cmdsUsers.length; c++) {
            if (cmdsUsers[c][0].equals(arg.toUpperCase())) {
                currchat.sendMessage(createEmbed.methode("System", cmdsUsers[c][2], Color.red, null, null, null).build()).queue();
                break;
            }
        }
        for (int c = 0; c < cmdsAdmins.length; c++) {
            if (cmdsAdmins[c][0].equals(arg.toUpperCase())) {
                currchat.sendMessage(createEmbed.methode("System", cmdsAdmins[c][2], Color.red, null, null, null).build()).queue();
                break;
            }
        }
    }
}
