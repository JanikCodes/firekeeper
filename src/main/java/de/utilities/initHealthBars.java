package de.utilities;

import net.dv8tion.jda.api.entities.Member;

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

}
