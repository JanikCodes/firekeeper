package de.commands.user;

import de.coaster.Database;
import de.coaster.Main;
import de.utilities.*;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;

public class clearArea {


    public static void methode(GuildMessageReceivedEvent event, TextChannel currchat) {
        long time = System.currentTimeMillis();
        String memberID = event.getMember().getId();
        int lastclear = Database.getStatistic("last_clear", memberID);
        int newtime = Math.toIntExact((time / 1000));
        boolean voted = hasVoted.methode(time, event);

        if ((time / 1000) - lastclear > getTime.getClearTime(time, event)) {
            //Can clear
            int stageProgess = Database.getAreaProgress(memberID);
            int intelligenceChance = Database.getStatistic("Intelligence", memberID);
            int faithChance = Database.getStatistic("Faith", memberID);
            int randomNum = getRandomNumberInRange.methode(1, 100);
            int randomIntelligenceNum = getRandomNumberInRange.methode(1, 100);
            int monsterSize = Database.getMonsterSize();
            int randomEncounterNum = getRandomNumberInRange.methode(1, monsterSize - 1);
            String encounterName = Database.getMonsterName(randomEncounterNum);
            String encounterDescrition = Database.getMonsterDescription(randomEncounterNum);
            int soulAmount = stageProgess * 40 + randomNum + 500;
            int extraSouls = ((faithChance / 2) * stageProgess) + randomNum;
            if (extraSouls >= 12500) {
                extraSouls = 12500;
            }

            ArrayList<Integer> items = Database.getAllitems();
            double howmanyitems = getRandomNumberInRange.methode(1, 2);

            if (findRole.methode(event.getMember(), Main.tier1) != null) {
                howmanyitems = howmanyitems * Main.tier1SoulMulti;
            }
            if (findRole.methode(event.getMember(), Main.tier2) != null) {
                howmanyitems = howmanyitems * Main.tier2SoulMulti;
            }

            String itemnames = "";

            for (int i = 1; i <= howmanyitems; i++) {
                int itemNumber = getRandomNumberInRange.methode(0, items.size() - 1);
                String itemName = Database.getItemName(items.get(itemNumber));
                Database.giveItem(memberID, items.get(itemNumber));

                if (i == 1) {
                    itemnames = itemnames + itemName;
                } else {
                    itemnames = itemnames + ", " + itemName;
                }
            }

            if (itemnames.equals("")) {
                itemnames = "nothing";
            }

            int intelligenceSouls = 0;
            String encounterString = "";

            intelligenceChance = 25 + (Database.getStatistic("intelligence",memberID) / 2);

            if (randomIntelligenceNum < intelligenceChance) {
                //Won against encounter
                intelligenceSouls = (intelligenceChance * stageProgess);
                if (intelligenceSouls >= 15000) {
                    intelligenceSouls = 15000;
                }
                encounterString = " Because of your **Intelligence** level, you won the encounter against the **" + encounterName + "** and collected another **" + intelligenceSouls / 2 + "** souls!";

                if (encounterName.equals("Ringed Knight")) {
                    Database.completeAchievement(memberID, 11);
                    //No rewards
                }

            } else {
                //lost against encounter
                encounterString = " Because of your **Intelligence** level, you lost the encounter against the **" + encounterName + "** because " + encounterDescrition;
            }

            double allSouls = soulAmount + extraSouls + intelligenceSouls;
            if (findRole.methode(event.getMember(), Main.tier1) != null) {
                allSouls = allSouls * Main.tier1SoulMulti;
            }
            if (findRole.methode(event.getMember(), Main.tier2) != null) {
                allSouls = allSouls * Main.tier2SoulMulti;
            }
            int intAllSouls = (int) (allSouls / 2);

            Database.giveSouls(memberID, intAllSouls);

            currchat.sendMessage(createEmbed.methode("\uD83E\uDDFE Area Progress \uD83E\uDDFE", "\uD83D\uDCCD Due to your **strength, dexterity, resistance** and **vitality** you've collected **" + soulAmount/2 + "** souls! \n\n" +
                            "\uD83D\uDCCD You also gained an extra **" + extraSouls/2 + "** souls due to your **faith level**. \n\n" +
                            "\uD83C\uDFC6 **Special encounter:** " + encounterString + "\n\n" +
                            "<:repairpowder:779816424952037427> You've collected **" + itemnames + "** \n\n" +
                            "\uD83D\uDCCD **Collected a total of** **" + intAllSouls + "** souls!"
                    , Color.ORANGE, "You can clear a new stage every 2h", event.getMember(), null).build()).queue();

            Database.updateClearTime(memberID, newtime);
            Database.giveAreaClear(event.getGuild().getId());
            if ((intAllSouls) >= 20000) {
                if (Database.completeAchievement(memberID, 4)) {
                    rewardUser.methode(memberID, "item", 10);
                    rewardUser.methode(memberID, "item", 10);
                    rewardUser.methode(memberID, "item", 10);
                }
            }


        } else {
            String endTime = calculateTime.methode(getTime.getClearTime(time, event) - ((time / 1000) - lastclear));
            //Can't clear
            if (voted) {
                currchat.sendMessage(createEmbed.methode("**ERROR**", "You've already cleared an area recently! \n You'll need to wait **" + endTime + "** to clear a new area!", Color.orange, null, null, null).build()).queue();
            } else {
                currchat.sendMessage(createEmbed.methode("**ERROR**", "You've already cleared an area recently! \n You'll need to wait **" + endTime + "** to clear a new area! \n ❕*Vote the bot on* [top.gg](https://top.gg/bot/760993270133555231/vote) *for reduced cooldown!*", Color.orange, null, null, null).build()).queue();
            }
        }
    }

}
