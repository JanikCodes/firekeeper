package de.coaster;
import javax.security.auth.login.LoginException;

import com.sun.net.httpserver.HttpServer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {

    public static String defaultprefix = "!";
    public static String botAddLink = "https://discord.com/api/oauth2/authorize?client_id=760993270133555231&permissions=134556736&scope=bot";
    public static int bossTime = 1800;
    public static int clearTime = 5400;
    public static int voteClearTime = 3600;
    public static int voteBossTime = 2700;

    public static int bossTimeWon = 5400;
    public static int voteBossTimeWon = 3600;

    public static int patreonTime = 1800;
    public static int patreonTimeBossWon = 1800;
    public static double tier1SoulMulti = 1.5;
    public static int tier2SoulMulti = 2;

    public static String tier1 = "782558977459617793";
    public static String tier2 = "782565550642954261";
    public static String patreonlink = "https://www.patreon.com/firekeeperbot";

    public static JDA jda;

    public static boolean test = true;
    public static boolean DEVELOPER_SERVER_MODE = false;


    private static String realVersion = "NzYwOTkzMjcwMTMzNTU1MjMx.X3UINg.s8k6-zhT13HDBRFpCFSc4EFPTvc";
    private static String testVersion = "NzYzNDQ1ODk3NDc1MzkxNDk4.X330Zg.mwrGhbZipvaBF-X2XOmQoS5JIww";
    private static String version = null;

    public static void main(String[] args) throws IOException {

        if(test){
            version = testVersion;
        }else{
            version = realVersion;
        }

        try {
            jda = JDABuilder.createDefault(version)
                    .setChunkingFilter(ChunkingFilter.ALL) // enable member chunking for all guilds
                    .setMemberCachePolicy(MemberCachePolicy.ALL) // ignored if chunking enabled
                    .enableIntents(GatewayIntent.GUILD_MEMBERS,GatewayIntent.GUILD_EMOJIS, GatewayIntent.GUILD_MESSAGE_REACTIONS,
                            GatewayIntent.GUILD_MESSAGES)
                    .setActivity(Activity.of(Activity.ActivityType.WATCHING,"the servers"))
                    .setStatus(OnlineStatus.ONLINE)
                    .addEventListeners(new CommandListener())
                    .addEventListeners(new GuildMessageReceived())
                    .addEventListeners(new GuildsListener())
                    .addEventListeners(new OnReadyListener())
                    .build();


        } catch (LoginException e) {
            System.out.println("Problem with JDA Builder");
            e.printStackTrace();
        }

        HttpServer server = HttpServer.create(new InetSocketAddress(8100), 0); // create a Httpserver listening on port 8000
        server.createContext("/dblwebhook", new Reciever());  // creates a handler for when a requests comes into https://yourserverip.com/dblwebhook
        server.setExecutor(null); // creates a default executor
        server.start(); // Starts your httpserver
        System.out.println("HttpServer started");

    }



}
