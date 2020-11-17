package eu.atomicnetworks.discordbot;

import com.google.gson.Gson;
import eu.atomicnetworks.discordbot.commands.HelpCommand;
import eu.atomicnetworks.discordbot.commands.InfoCommand;
import eu.atomicnetworks.discordbot.commands.JoinCommand;
import eu.atomicnetworks.discordbot.commands.LeaveCommand;
import eu.atomicnetworks.discordbot.commands.PlayCommand;
import eu.atomicnetworks.discordbot.commands.SetupCommand;
import eu.atomicnetworks.discordbot.commands.VolumeCommand;
import eu.atomicnetworks.discordbot.handler.AudioHandler;
import eu.atomicnetworks.discordbot.managers.BackendManager;
import eu.atomicnetworks.discordbot.managers.GuildManager;
import eu.atomicnetworks.discordbot.managers.LoggerManager;
import eu.atomicnetworks.discordbot.managers.MongoManager;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 *
 * @author kacpe
 */
public class DiscordBot {

    private Gson gson;
    private JDA jda;
    private long startTimeMillis;

    private LoggerManager loggerManager;
    private MongoManager mongoManager;
    private GuildManager guildManager;
    private BackendManager backendManager;
    
    private HelpCommand helpCommand;
    private InfoCommand infoCommand;
    private JoinCommand joinCommand;
    private LeaveCommand leaveCommand;
    private PlayCommand playCommand;
    private SetupCommand setupCommand;
    private VolumeCommand volumeCommand;

    public static void main(String[] args) {
        new DiscordBot().loadBanner();
        new DiscordBot().init();
    }

    private void init() {
        this.gson = new Gson();
        this.startTimeMillis = System.currentTimeMillis();
        
        this.loggerManager = new LoggerManager();
        this.mongoManager = new MongoManager(this);
        this.guildManager = new GuildManager(this);
        this.backendManager = new BackendManager(this);
        
        this.helpCommand = new HelpCommand(this);
        this.infoCommand = new InfoCommand(this);
        this.joinCommand = new JoinCommand(this);
        this.leaveCommand = new LeaveCommand(this);
        this.playCommand = new PlayCommand(this);
        this.setupCommand = new SetupCommand(this);
        this.volumeCommand = new VolumeCommand(this);

        JDABuilder builder = JDABuilder.createDefault("Nzc3OTU0NTg0MDEzOTYzMjY1.X7K8qg.f7kbG0-yhaYy6WBfJiPrEf1DaO4");
        builder.setActivity(Activity.listening("atomicradio.eu"));
        builder.addEventListeners(new ListenerAdapter() {

            @Override
            public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
                Message message = event.getMessage();
                String prefix = backendManager.getPrefix(event.getGuild());
                
                if(message.getMentionedUsers().stream().filter(t -> t.getId().equals(event.getGuild().getSelfMember().getId())).findFirst().orElse(null) != null) {
                    helpCommand.execute(event);
                    return;
                }
                
                if(!message.getContentRaw().toLowerCase().startsWith(prefix)) {
                    return;
                }
                consoleInfo(MessageFormat.format("{0} ({1}) ran command {2} in {3} (#{4})", event.getAuthor().getName(), event.getAuthor().getId(), message.getContentRaw().toLowerCase().split(" ")[0], event.getGuild().getName(), event.getChannel().getName()));
            
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
                } else if(message.getContentRaw().toLowerCase().startsWith(prefix + "setup")) {
                    setupCommand.execute(event);
                } else if(message.getContentRaw().toLowerCase().startsWith(prefix + "vol") || message.getContentRaw().toLowerCase().startsWith(prefix + "volume")) {
                    volumeCommand.execute(event);
                }
            }

            @Override
            public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
                if(backendManager.getPlaying(event.getGuild())) {
                    if(event.getChannelJoined().getId().equals(backendManager.getChannelId(event.getGuild()))) {
                        if(event.getMember().getId().equals(event.getGuild().getSelfMember().getId())) {
                            return;
                        }
                        if(event.getGuild().getAudioManager().getSendingHandler() == null) {
                            VoiceChannel voiceChannel = event.getChannelJoined();
                            event.getGuild().getAudioManager().openAudioConnection(voiceChannel);
                            switch(backendManager.getMusic(event.getGuild())) {
                                case "one":
                                    backendManager.startStream(event.getGuild(), "https://listen.atomicradio.eu/one/highquality.mp3");
                                    backendManager.setPlaying(event.getGuild(), true);
                                    backendManager.setMusic(event.getGuild(), "one");
                                    backendManager.setChannelId(event.getGuild(), voiceChannel.getId());
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
                            } catch (InsufficientPermissionException ex) {
                                consoleInfo("Missing rights on " + guild.getName() + ". (" + guild.getId() + ")");
                            }
                            consoleInfo("Joined channel " + voiceChannel.getName() + " on guild " + guild.getName() + ". (" + guild.getId() + ")");
                        }
                    }
                }
            }

        });
        try {
            this.jda = builder.build();
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

    public JDA getJda() {
        return jda;
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

}
