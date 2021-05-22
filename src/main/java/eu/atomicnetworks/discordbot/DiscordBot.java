package eu.atomicnetworks.discordbot;

import com.google.gson.Gson;
import com.sedmelluq.discord.lavaplayer.jdaudp.NativeAudioSendFactory;
import eu.atomicradio.AtomicClient;
import eu.atomicnetworks.discordbot.handler.EventHandler;
import eu.atomicnetworks.discordbot.handler.ServerListHandler;
import eu.atomicnetworks.discordbot.managers.BackendManager;
import eu.atomicnetworks.discordbot.managers.GuildManager;
import eu.atomicnetworks.discordbot.managers.LoggerManager;
import eu.atomicnetworks.discordbot.managers.MongoManager;
import eu.atomicnetworks.discordbot.webapi.ApiServer;
import eu.atomicradio.objects.Channels;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;
import javax.swing.Timer;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

/**
 *
 * @author Kacper Mura
 * 2020 Copyright (c) by atomicradio.eu to present.
 * All rights reserved. https://github.com/VocalZero
 *
 */
public class DiscordBot {

    private ShardManager shardManager;
    private Gson gson;
    private AtomicClient atomicClient;
    private long startTimeMillis;

    private LoggerManager loggerManager;
    private MongoManager mongoManager;
    private GuildManager guildManager;
    private BackendManager backendManager;

    private ServerListHandler serverListHandler;
    private ApiServer apiServer;

    public static void main(String[] args) {
        new DiscordBot().loadBanner();
        new DiscordBot().init();
    }

    private void init() {        
        this.gson = new Gson();
        this.atomicClient = new AtomicClient();
        this.startTimeMillis = System.currentTimeMillis();
                
        Logger jdaLogger = Logger.getLogger( "net.dv8tion" );
        jdaLogger.setLevel(Level.SEVERE);

        this.loggerManager = new LoggerManager();
        this.mongoManager = new MongoManager(this);
        this.guildManager = new GuildManager(this);
        this.backendManager = new BackendManager(this);
        
        this.apiServer = new ApiServer(this, 9091);

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault("Njk3NTE3MTA2Mjg3MzQ1NzM3.Xo4bbQ.54Yw6XMf12AUUGg5cpGEu9XpckY");
        builder.setChunkingFilter(ChunkingFilter.NONE);
        builder.enableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
        builder.disableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.EMOTE);
        builder.setBulkDeleteSplittingEnabled(true);
        builder.setMemberCachePolicy(MemberCachePolicy.VOICE.or(MemberCachePolicy.OWNER));
        builder.setLargeThreshold(50);
        builder.setActivity(Activity.listening("atomicradio.eu"));
        builder.setAudioSendFactory(new NativeAudioSendFactory());
        builder.addEventListeners(new EventHandler(this));
        try {
            builder.setShardsTotal(6);
            builder.setShards(0, 5);
            this.shardManager = builder.build();
            
            Timer timer = new Timer(15000, (ActionEvent e) -> {
                if(this.atomicClient.isLive()) {
                    this.shardManager.getShards().stream().forEach((shard) -> {
                        String streamer = this.atomicClient.getStreamer();
                        if(streamer.split(" ").length == 2) {
                            streamer = streamer.split(" ")[0];
                        }
                        shard.getPresence().setActivity(Activity.streaming("live with " + streamer + " on atr.one", "https://www.twitch.tv/atomic"));
                    });
                } else {
                    double rpfinal = (Math.floor(Math.random() * 9));
                    this.shardManager.getShards().stream().forEach((shard) -> {
                        switch ((int) rpfinal) {
                            case 1:
                                shard.getPresence().setActivity(Activity.streaming("atomicradio.eu 🎶", "https://www.twitch.tv/atomic"));
                                break;
                            case 2:
                                shard.getPresence().setActivity(Activity.listening("atr.one, dance & trap"));
                                break;
                            case 3:
                                String artistOne = this.atomicClient.getChannel(Channels.ONE).getSong().getArtist();
                                if(artistOne.contains("FEAT.")) {
                                    artistOne = artistOne.split("FEAT.")[0];
                                }
                                shard.getPresence().setActivity(Activity.listening(artistOne + " - " + this.atomicClient.getChannel(Channels.ONE).getSong().getTitle() + " on atr.one"));
                                break;
                            case 4:
                                String artistDance = this.atomicClient.getChannel(Channels.DANCE).getSong().getArtist();
                                if(artistDance.contains("FEAT.")) {
                                    artistDance = artistDance.split("FEAT.")[0];
                                }
                                shard.getPresence().setActivity(Activity.listening(artistDance + " - " + this.atomicClient.getChannel(Channels.DANCE).getSong().getTitle() + " on atr.dance"));
                                break;
                            case 5:
                                String artistTrap = this.atomicClient.getChannel(Channels.TRAP).getSong().getArtist();
                                if(artistTrap.contains("FEAT.")) {
                                    artistTrap = artistTrap.split("FEAT.")[0];
                                }
                                shard.getPresence().setActivity(Activity.listening(artistTrap + " - " + this.atomicClient.getChannel(Channels.TRAP).getSong().getTitle() + " on atr.trap"));
                                break;
                            case 6:
                                shard.getPresence().setActivity(Activity.listening(".help"));
                                break;
                            case 7:
                                shard.getPresence().setActivity(Activity.listening("the difference 🔊"));
                                break;
                            case 8:
                                shard.getPresence().setActivity(Activity.playing("on " + this.backendManager.getGuildCount() + " guilds"));
                                break;
                            case 9:
                                shard.getPresence().setActivity(Activity.playing("for " + this.backendManager.getUserCount() + " users"));
                                break;
                        }
                    });
                }
            });
            timer.setInitialDelay(0);
            timer.setRepeats(true);
            timer.start();
            
            this.serverListHandler = new ServerListHandler(this);
        } catch (LoginException ex) {
            Logger.getLogger(DiscordBot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadBanner() {
        System.out.println("\n       _                  _                    _ _       \n"
                + "      | |                (_)                  | (_)      \n"
                + "  __ _| |_ ___  _ __ ___  _  ___ _ __ __ _  __| |_  ___  \n"
                + " / _` | __/ _ \\| '_ ` _ \\| |/ __| '__/ _` |/ _` | |/ _ \\ \n"
                + "| (_| | || (_) | | | | | | | (__| | | (_| | (_| | | (_) |\n"
                + " \\__,_|\\__\\___/|_| |_| |_|_|\\___|_|  \\__,_|\\__,_|_|\\___/ \n\n"
                + " atomicradio.eu discord musicbot\n 2020 Copyright (c) by atomicradio.eu to present.\n Author: Kacper Mura\n");
    }

    public Gson getGson() {
        return gson;
    }

    public AtomicClient getAtomicClient() {
        return atomicClient;
    }

    public long getStartTimeMillis() {
        return startTimeMillis;
    }

    public void consoleInfo(String text) {
        this.loggerManager.sendInfo(text);
    }

    public void consoleWarning(String text) {
        this.loggerManager.sendWarning(text);
    }

    public void consoleError(String text) {
        this.loggerManager.sendError(text);
    }

    public MongoManager getMongoManager() {
        return mongoManager;
    }

    public GuildManager getGuildManager() {
        return guildManager;
    }

    public BackendManager getBackendManager() {
        return backendManager;
    }

    public ShardManager getShardManager() {
        return shardManager;
    }

}
