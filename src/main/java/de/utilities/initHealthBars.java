package de.utilities;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

public class initHealthBars {

    public static String methode(Member member, int plrHealth, int maxPlrHealth, int bossHealth, int maxBossHealth, String bossDamage, String playerDamage) {
        String desc = null;

        int plrProc = (plrHealth * 100) / maxPlrHealth;
        int bossProc = (bossHealth * 100) / maxBossHealth;

        String plrHealthDisplay = calcXPBar.methode(plrProc, "boss");
        String bossHealthDisplay = calcXPBar.methode(bossProc, "boss");

        desc = member.getEffectiveName() + "'s health: **" + plrHealth + "/" + maxPlrHealth + "** \n" + plrHealthDisplay + bossDamage + "\n" + "Boss health: **" + bossHealth + "/" + maxBossHealth + "**\n " + bossHealthDisplay + playerDamage;

        return desc;
    }


    public static String initDescriptionPvp(String player1ID, String player2ID, int plrHealth1, int maxPlrHealth1, int plrHealth2, int maxPlrHealth2, String playerDamage1, String playerDamage2,GuildMessageReactionAddEvent event) {
        String desc = null;

        int plrProc1 = (plrHealth1 * 100) / maxPlrHealth1;
        int plrProc2 = (plrHealth2 * 100) / maxPlrHealth2;

        String plrHealthDisplay1 = calcXPBar.methode(plrProc1, "boss");
        String plrHealthDisplay2 = calcXPBar.methode(plrProc2, "boss");

        Member player1 = event.getMember().getGuild().getMemberById(player1ID);
        Member player2 = event.getMember().getGuild().getMemberById(player2ID);

        desc = player1.getEffectiveName() + "'s health: **" + plrHealth1 + "/" + maxPlrHealth1 + "** \n" + plrHealthDisplay1 + playerDamage2 + "\n" + player2.getEffectiveName() + "'s health: **" + plrHealth2 + "/" + maxPlrHealth2 + "**\n " + plrHealthDisplay2 + playerDamage1;

        return desc;
    }
}
