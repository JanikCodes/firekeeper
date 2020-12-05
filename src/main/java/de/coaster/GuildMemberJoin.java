package de.coaster;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildMemberJoin extends ListenerAdapter{

	public void onGuildMemberJoin(GuildMemberJoinEvent event){
			Member member = event.getMember();
			//Connect new member to Database
			String stringID = event.getMember().getId();
			Database.addMemberToTable(stringID,event.getMember().getEffectiveName());
	}

}
