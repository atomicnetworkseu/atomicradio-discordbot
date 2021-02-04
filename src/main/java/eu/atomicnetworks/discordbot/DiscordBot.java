package eu.atomicnetworks.discordbot;

import com.google.gson.Gson;
import eu.atomicnetworks.discordbot.commands.BassCommand;
import eu.atomicradio.AtomicClient;
import eu.atomicnetworks.discordbot.commands.HelpCommand;
import eu.atomicnetworks.discordbot.commands.InfoCommand;
import eu.atomicnetworks.discordbot.commands.JoinCommand;
import eu.atomicnetworks.discordbot.commands.LeaveCommand;
import eu.atomicnetworks.discordbot.commands.PlayCommand;
import eu.atomicnetworks.discordbot.commands.ReportCommand;
import eu.atomicnetworks.discordbot.commands.SettingsCommand;
import eu.atomicnetworks.discordbot.commands.SetupCommand;
import eu.atomicnetworks.discordbot.commands.ShardCommand;
import eu.atomicnetworks.discordbot.commands.SongCommand;
import eu.atomicnetworks.discordbot.commands.VolumeCommand;
import eu.atomicnetworks.discordbot.handler.AudioHandler;
import eu.atomicnetworks.discordbot.handler.ServerListHandler;
import eu.atomicnetworks.discordbot.managers.BackendManager;
import eu.atomicnetworks.discordbot.managers.GuildManager;
import eu.atomicnetworks.discordbot.managers.LoggerManager;
import eu.atomicnetworks.discordbot.managers.MongoManager;
import eu.atomicnetworks.discordbot.webapi.ApiServer;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;
import javax.swing.Timer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
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

    private HelpCommand helpCommand;
    private InfoCommand infoCommand;
    private JoinCommand joinCommand;
    private LeaveCommand leaveCommand;
    private PlayCommand playCommand;
    private SetupCommand setupCommand;
    private VolumeCommand volumeCommand;
    private SettingsCommand settingsCommand;
    private ReportCommand reportCommand;
    private SongCommand songCommand;
    private BassCommand bassCommand;
    private ShardCommand shardCommand;

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

        this.helpCommand = new HelpCommand(this);
        this.infoCommand = new InfoCommand(this);
        this.joinCommand = new JoinCommand(this);
        this.leaveCommand = new LeaveCommand(this);
        this.playCommand = new PlayCommand(this);
        this.setupCommand = new SetupCommand(this);
        this.volumeCommand = new VolumeCommand(this);
        this.settingsCommand = new SettingsCommand(this);
        this.reportCommand = new ReportCommand(this);
        this.songCommand = new SongCommand(this);
        this.bassCommand = new BassCommand(this);
        this.shardCommand = new ShardCommand(this);

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault("Njk3NTE3MTA2Mjg3MzQ1NzM3.Xo4bbQ.54Yw6XMf12AUUGg5cpGEu9XpckY");
        builder.setChunkingFilter(ChunkingFilter.NONE);
        builder.enableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
        builder.disableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.EMOTE);
        builder.setBulkDeleteSplittingEnabled(true);
        builder.setMemberCachePolicy(MemberCachePolicy.VOICE.or(MemberCachePolicy.OWNER));
        builder.setLargeThreshold(50);
        builder.setActivity(Activity.listening("atomicradio.eu"));
        builder.addEventListeners(new ListenerAdapter() {
            
            @Override
            public void onReady(ReadyEvent event) {
                for (Guild guild : event.getJDA().getGuilds()) {
                    if (backendManager.getPlaying(guild)) {
                        if (backendManager.getChannelId(guild).isEmpty() || backendManager.getChannelId(guild) == null) {
                            return;
                        }
                        VoiceChannel voiceChannel = guild.getVoiceChannelById(backendManager.getChannelId(guild));
                        if (voiceChannel != null) {
                            try {
                                guild.getAudioManager().openAudioConnection(voiceChannel);
                                
                                voiceChannel.getMembers().stream().forEach((t) -> {
                                    backendManager.addListener(guild, t, voiceChannel.getId());
                                });
                                
                                if(voiceChannel.getMembers().size() >= 1) {
                                    if (guild.getAudioManager().getSendingHandler() == null) {
                                        switch (backendManager.getMusic(guild)) {
                                            case "one":
                                                backendManager.startStream(guild, "https://listen.atomicradio.eu/one/highquality.mp3");
                                                backendManager.setPlaying(guild, true);
                                                backendManager.setMusic(guild, "one");
                                                backendManager.setChannelId(guild, voiceChannel.getId());
                                                consoleInfo("[SHARD " + event.getJDA().getShardInfo().getShardId() + "] Restarting stream on " + guild.getName() + " with atr.one.");
                                                if (guild.getSelfMember().hasPermission(Permission.NICKNAME_CHANGE)) {
                                                    if (getBackendManager().getTag(guild)) {
                                                        guild.getSelfMember().modifyNickname("atomicradio » atr.one").queue();
                                                    }
                                                }
                                                break;
                                            case "dance":
                                                backendManager.startStream(guild, "https://listen.atomicradio.eu/dance/highquality.mp3");
                                                backendManager.setPlaying(guild, true);
                                                backendManager.setMusic(guild, "dance");
                                                backendManager.setChannelId(guild, voiceChannel.getId());
                                                consoleInfo("[SHARD " + event.getJDA().getShardInfo().getShardId() + "] Restarting stream on " + guild.getName() + " with atr.dance.");
                                                if (guild.getSelfMember().hasPermission(Permission.NICKNAME_CHANGE)) {
                                                    if (getBackendManager().getTag(guild)) {
                                                        guild.getSelfMember().modifyNickname("atomicradio » atr.dance").queue();
                                                    }
                                                }
                                                break;
                                            case "trap":
                                                backendManager.startStream(guild, "https://listen.atomicradio.eu/trap/highquality.mp3");
                                                backendManager.setPlaying(guild, true);
                                                backendManager.setMusic(guild, "trap");
                                                backendManager.setChannelId(guild, voiceChannel.getId());
                                                consoleInfo("[SHARD " + event.getJDA().getShardInfo().getShardId() + "] Restarting stream on " + guild.getName() + " with atr.trap.");
                                                if (guild.getSelfMember().hasPermission(Permission.NICKNAME_CHANGE)) {
                                                    if (getBackendManager().getTag(guild)) {
                                                        guild.getSelfMember().modifyNickname("atomicradio » atr.trap").queue();
                                                    }
                                                }
                                                break;
                                        }
                                    }
                                }
                                
                            } catch (InsufficientPermissionException ex) {
                                consoleInfo("[SHARD " + event.getJDA().getShardInfo().getShardId() + "] Missing rights on " + guild.getName() + ". (" + guild.getId() + ")");
                            }
                            consoleInfo("[SHARD " + event.getJDA().getShardInfo().getShardId() + "] Joined channel " + voiceChannel.getName() + " on guild " + guild.getName() + ". (" + guild.getId() + ")");
                        }
                    }
                }
            }

            @Override
            public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
                Message message = event.getMessage();
                String prefix = backendManager.getPrefix(event.getGuild());

                if (message.getMentionedUsers().stream().filter(t -> t.getId().equals(event.getGuild().getSelfMember().getId())).findFirst().orElse(null) != null) {
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setColor(new Color(149, 79, 180));
                    embed.setDescription(":bird: Hi, I am " + event.getJDA().getSelfUser().getAsMention() + ", your new favourite musicbot!\n\nYou can find out more about me with **"
                            + getBackendManager().getPrefix(event.getGuild()) + "help**,\non **" + event.getGuild().getName() + "** you can control me with the prefix `" + getBackendManager().getPrefix(event.getGuild()) + "` ");
                    backendManager.sendMessage(event, embed.build());
                    return;
                }

                if (!message.getContentRaw().toLowerCase().startsWith(prefix)) {
                    return;
                }

                if (message.getContentRaw().toLowerCase().startsWith(prefix + "help")) {
                    helpCommand.execute(event);
                } else if (message.getContentRaw().toLowerCase().startsWith(prefix + "info") || message.getContentRaw().toLowerCase().startsWith(prefix + "invite")) {
                    infoCommand.execute(event);
                } else if (message.getContentRaw().toLowerCase().startsWith(prefix + "join")) {
                    joinCommand.execute(event);
                } else if (message.getContentRaw().toLowerCase().startsWith(prefix + "leave")) {
                    leaveCommand.execute(event);
                } else if (message.getContentRaw().toLowerCase().startsWith(prefix + "play")) {
                    playCommand.execute(event);
                } else if (message.getContentRaw().toLowerCase().startsWith(prefix + "setup")) {
                    setupCommand.execute(event);
                } else if (message.getContentRaw().toLowerCase().startsWith(prefix + "vol") || message.getContentRaw().toLowerCase().startsWith(prefix + "volume")) {
                    volumeCommand.execute(event);
                } else if (message.getContentRaw().toLowerCase().startsWith(prefix + "settings")) {
                    settingsCommand.execute(event);
                } else if (message.getContentRaw().toLowerCase().startsWith(prefix + "report")) {
                    reportCommand.execute(event);
                } else if (message.getContentRaw().toLowerCase().startsWith(prefix + "song")) {
                    songCommand.execute(event);
                } else if (message.getContentRaw().toLowerCase().startsWith(prefix + "bass")) {
                    bassCommand.execute(event);
                } else if (message.getContentRaw().toLowerCase().startsWith(prefix + "shard")) {
                    shardCommand.execute(event);
                } else {
                    return;
                }
                consoleInfo(MessageFormat.format("[SHARD {0}] {1} ({2}) ran command {3} in {4} (#{5})", event.getJDA().getShardInfo().getShardId(), event.getAuthor().getName(), event.getAuthor().getId(), message.getContentRaw().toLowerCase().split(" ")[0], event.getGuild().getName(), event.getChannel().getName()));
            }

            @Override
            public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
                if (backendManager.getPlaying(event.getGuild())) {
                    if (event.getChannelJoined().getId().equals(backendManager.getChannelId(event.getGuild()))) {
                        if (event.getMember().getId().equals(event.getGuild().getSelfMember().getId())) {
                            return;
                        }
                        
                        backendManager.addListener(event.getGuild(), event.getMember(), event.getChannelJoined().getId());
                        
                        if (event.getGuild().getAudioManager().getSendingHandler() == null) {
                            VoiceChannel voiceChannel = event.getChannelJoined();
                            event.getGuild().getAudioManager().openAudioConnection(voiceChannel);
                            switch (backendManager.getMusic(event.getGuild())) {
                                case "one":
                                    backendManager.startStream(event.getGuild(), "https://listen.atomicradio.eu/one/highquality.mp3");
                                    backendManager.setPlaying(event.getGuild(), true);
                                    backendManager.setMusic(event.getGuild(), "one");
                                    backendManager.setChannelId(event.getGuild(), voiceChannel.getId());
                                    if (event.getGuild().getSelfMember().hasPermission(Permission.NICKNAME_CHANGE)) {
                                        if (getBackendManager().getTag(event.getGuild())) {
                                            event.getGuild().getSelfMember().modifyNickname("atomicradio » atr.one").queue();
                                        }
                                    }
                                    break;
                                case "dance":
                                    backendManager.startStream(event.getGuild(), "https://listen.atomicradio.eu/dance/highquality.mp3");
                                    backendManager.setPlaying(event.getGuild(), true);
                                    backendManager.setMusic(event.getGuild(), "dance");
                                    backendManager.setChannelId(event.getGuild(), voiceChannel.getId());
                                    if (event.getGuild().getSelfMember().hasPermission(Permission.NICKNAME_CHANGE)) {
                                        if (getBackendManager().getTag(event.getGuild())) {
                                            event.getGuild().getSelfMember().modifyNickname("atomicradio » atr.dance").queue();
                                        }
                                    }
                                    break;
                                case "trap":
                                    backendManager.startStream(event.getGuild(), "https://listen.atomicradio.eu/trap/highquality.mp3");
                                    backendManager.setPlaying(event.getGuild(), true);
                                    backendManager.setMusic(event.getGuild(), "trap");
                                    backendManager.setChannelId(event.getGuild(), voiceChannel.getId());
                                    if (event.getGuild().getSelfMember().hasPermission(Permission.NICKNAME_CHANGE)) {
                                        if (getBackendManager().getTag(event.getGuild())) {
                                            event.getGuild().getSelfMember().modifyNickname("atomicradio » atr.trap").queue();
                                        }
                                    }
                                    break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
                if (backendManager.getPlaying(event.getGuild())) {
                    if (event.getChannelLeft().getId().equals(backendManager.getChannelId(event.getGuild()))) {
                        backendManager.removeListener(event.getGuild(), event.getMember());
                        if (event.getChannelLeft().getMembers().size() == 1) {
                            if (event.getGuild().getAudioManager().getSendingHandler() != null) {
                                AudioHandler audioHandler = (AudioHandler) event.getGuild().getAudioManager().getSendingHandler();
                                audioHandler.stop();
                                event.getGuild().getAudioManager().setSendingHandler(null);
                            }
                        }
                    }
                }
            }

            @Override
            public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
                if (backendManager.getPlaying(event.getGuild())) {
                    if (event.getChannelJoined().getId().equals(backendManager.getChannelId(event.getGuild()))) {
                        if (event.getMember().getId().equals(event.getGuild().getSelfMember().getId())) {
                            return;
                        }
                        backendManager.addListener(event.getGuild(), event.getMember(), event.getChannelJoined().getId());
                        if (event.getGuild().getAudioManager().getSendingHandler() == null) {
                            VoiceChannel voiceChannel = event.getChannelJoined();
                            event.getGuild().getAudioManager().openAudioConnection(voiceChannel);
                            switch (backendManager.getMusic(event.getGuild())) {
                                case "one":
                                    backendManager.startStream(event.getGuild(), "https://listen.atomicradio.eu/one/highquality.mp3");
                                    backendManager.setPlaying(event.getGuild(), true);
                                    backendManager.setMusic(event.getGuild(), "one");
                                    backendManager.setChannelId(event.getGuild(), voiceChannel.getId());
                                    if (event.getGuild().getSelfMember().hasPermission(Permission.NICKNAME_CHANGE)) {
                                        if (getBackendManager().getTag(event.getGuild())) {
                                            event.getGuild().getSelfMember().modifyNickname("atomicradio » atr.one").queue();
                                        }
                                    }
                                    break;
                                case "dance":
                                    backendManager.startStream(event.getGuild(), "https://listen.atomicradio.eu/dance/highquality.mp3");
                                    backendManager.setPlaying(event.getGuild(), true);
                                    backendManager.setMusic(event.getGuild(), "dance");
                                    backendManager.setChannelId(event.getGuild(), voiceChannel.getId());
                                    if (event.getGuild().getSelfMember().hasPermission(Permission.NICKNAME_CHANGE)) {
                                        if (getBackendManager().getTag(event.getGuild())) {
                                            event.getGuild().getSelfMember().modifyNickname("atomicradio » atr.dance").queue();
                                        }
                                    }
                                    break;
                                case "trap":
                                    backendManager.startStream(event.getGuild(), "https://listen.atomicradio.eu/trap/highquality.mp3");
                                    backendManager.setPlaying(event.getGuild(), true);
                                    backendManager.setMusic(event.getGuild(), "trap");
                                    backendManager.setChannelId(event.getGuild(), voiceChannel.getId());
                                    if (event.getGuild().getSelfMember().hasPermission(Permission.NICKNAME_CHANGE)) {
                                        if (getBackendManager().getTag(event.getGuild())) {
                                            event.getGuild().getSelfMember().modifyNickname("atomicradio » atr.trap").queue();
                                        }
                                    }
                                    break;
                            }
                        }
                    } else if (event.getChannelLeft().getId().equals(backendManager.getChannelId(event.getGuild()))) {
                        backendManager.removeListener(event.getGuild(), event.getMember());
                        if (event.getChannelLeft().getMembers().size() == 1) {
                            if (event.getGuild().getAudioManager().getSendingHandler() != null) {
                                AudioHandler audioHandler = (AudioHandler) event.getGuild().getAudioManager().getSendingHandler();
                                audioHandler.stop();
                                event.getGuild().getAudioManager().setSendingHandler(null);
                            }
                        }
                    }
                }
            }

        });
        try {
            builder.setShardsTotal(6);
            builder.setShards(0, 5);
            this.shardManager = builder.build();
            
            Timer timer = new Timer(15000, (ActionEvent e) -> {
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
                            shard.getPresence().setActivity(Activity.listening(this.atomicClient.getChannelOne().getSong().getArtist() + " - " + this.atomicClient.getChannelOne().getSong().getTitle() + " on atr.one"));
                            break;
                        case 4:
                            shard.getPresence().setActivity(Activity.listening(this.atomicClient.getChannelDance().getSong().getArtist() + " - " + this.atomicClient.getChannelDance().getSong().getTitle() + " on atr.dance"));
                            break;
                        case 5:
                            shard.getPresence().setActivity(Activity.listening(this.atomicClient.getChannelTrap().getSong().getArtist() + " - " + this.atomicClient.getChannelTrap().getSong().getTitle() + " on atr.trap"));
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
