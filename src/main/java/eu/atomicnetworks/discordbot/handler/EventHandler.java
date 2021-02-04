package eu.atomicnetworks.discordbot.handler;

import eu.atomicnetworks.discordbot.DiscordBot;
import eu.atomicnetworks.discordbot.commands.BassCommand;
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
import java.awt.Color;
import java.text.MessageFormat;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
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

/**
 *
 * @author Kacper Mura
 * 2020 Copyright (c) by atomicradio.eu to present.
 * All rights reserved. https://github.com/VocalZero
 *
 */
public class EventHandler extends ListenerAdapter {
    
    private final DiscordBot discordBot;
    private final HelpCommand helpCommand;
    private final InfoCommand infoCommand;
    private final JoinCommand joinCommand;
    private final LeaveCommand leaveCommand;
    private final PlayCommand playCommand;
    private final SetupCommand setupCommand;
    private final VolumeCommand volumeCommand;
    private final SettingsCommand settingsCommand;
    private final ReportCommand reportCommand;
    private final SongCommand songCommand;
    private final BassCommand bassCommand;
    private final ShardCommand shardCommand;

    public EventHandler(DiscordBot discordBot) {
        this.discordBot = discordBot;
        this.helpCommand = new HelpCommand(this.discordBot);
        this.infoCommand = new InfoCommand(this.discordBot);
        this.joinCommand = new JoinCommand(this.discordBot);
        this.leaveCommand = new LeaveCommand(this.discordBot);
        this.playCommand = new PlayCommand(this.discordBot);
        this.setupCommand = new SetupCommand(this.discordBot);
        this.volumeCommand = new VolumeCommand(this.discordBot);
        this.settingsCommand = new SettingsCommand(this.discordBot);
        this.reportCommand = new ReportCommand(this.discordBot);
        this.songCommand = new SongCommand(this.discordBot);
        this.bassCommand = new BassCommand(this.discordBot);
        this.shardCommand = new ShardCommand(this.discordBot);
    }

    @Override
    public void onReady(ReadyEvent event) {
        for (Guild guild : event.getJDA().getGuilds()) {
            if (this.discordBot.getBackendManager().getPlaying(guild)) {
                if (this.discordBot.getBackendManager().getChannelId(guild).isEmpty() || this.discordBot.getBackendManager().getChannelId(guild) == null) {
                    return;
                }
                VoiceChannel voiceChannel = guild.getVoiceChannelById(this.discordBot.getBackendManager().getChannelId(guild));
                if (voiceChannel != null) {
                    try {
                        guild.getAudioManager().openAudioConnection(voiceChannel);

                        voiceChannel.getMembers().stream().forEach((t) -> {
                            this.discordBot.getBackendManager().addListener(guild, t, voiceChannel.getId());
                        });

                        if (voiceChannel.getMembers().size() >= 1) {
                            if (guild.getAudioManager().getSendingHandler() == null) {
                                switch (this.discordBot.getBackendManager().getMusic(guild)) {
                                    case "one":
                                        this.discordBot.getBackendManager().startStream(guild, "https://listen.atomicradio.eu/one/highquality.mp3");
                                        this.discordBot.getBackendManager().setPlaying(guild, true);
                                        this.discordBot.getBackendManager().setMusic(guild, "one");
                                        this.discordBot.getBackendManager().setChannelId(guild, voiceChannel.getId());
                                        this.discordBot.consoleInfo("[SHARD " + event.getJDA().getShardInfo().getShardId() + "] Restarting stream on " + guild.getName() + " with atr.one.");
                                        if (guild.getSelfMember().hasPermission(Permission.NICKNAME_CHANGE)) {
                                            if (this.discordBot.getBackendManager().getTag(guild)) {
                                                guild.getSelfMember().modifyNickname("atomicradio » atr.one").queue();
                                            }
                                        }
                                        break;
                                    case "dance":
                                        this.discordBot.getBackendManager().startStream(guild, "https://listen.atomicradio.eu/dance/highquality.mp3");
                                        this.discordBot.getBackendManager().setPlaying(guild, true);
                                        this.discordBot.getBackendManager().setMusic(guild, "dance");
                                        this.discordBot.getBackendManager().setChannelId(guild, voiceChannel.getId());
                                        this.discordBot.consoleInfo("[SHARD " + event.getJDA().getShardInfo().getShardId() + "] Restarting stream on " + guild.getName() + " with atr.dance.");
                                        if (guild.getSelfMember().hasPermission(Permission.NICKNAME_CHANGE)) {
                                            if (this.discordBot.getBackendManager().getTag(guild)) {
                                                guild.getSelfMember().modifyNickname("atomicradio » atr.dance").queue();
                                            }
                                        }
                                        break;
                                    case "trap":
                                        this.discordBot.getBackendManager().startStream(guild, "https://listen.atomicradio.eu/trap/highquality.mp3");
                                        this.discordBot.getBackendManager().setPlaying(guild, true);
                                        this.discordBot.getBackendManager().setMusic(guild, "trap");
                                        this.discordBot.getBackendManager().setChannelId(guild, voiceChannel.getId());
                                        this.discordBot.consoleInfo("[SHARD " + event.getJDA().getShardInfo().getShardId() + "] Restarting stream on " + guild.getName() + " with atr.trap.");
                                        if (guild.getSelfMember().hasPermission(Permission.NICKNAME_CHANGE)) {
                                            if (this.discordBot.getBackendManager().getTag(guild)) {
                                                guild.getSelfMember().modifyNickname("atomicradio » atr.trap").queue();
                                            }
                                        }
                                        break;
                                }
                            }
                        }

                    } catch (InsufficientPermissionException ex) {
                        this.discordBot.consoleInfo("[SHARD " + event.getJDA().getShardInfo().getShardId() + "] Missing rights on " + guild.getName() + ". (" + guild.getId() + ")");
                    }
                    this.discordBot.consoleInfo("[SHARD " + event.getJDA().getShardInfo().getShardId() + "] Joined channel " + voiceChannel.getName() + " on guild " + guild.getName() + ". (" + guild.getId() + ")");
                }
            }
        }
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        Message message = event.getMessage();
        String prefix = this.discordBot.getBackendManager().getPrefix(event.getGuild());

        if (message.getMentionedUsers().stream().filter(t -> t.getId().equals(event.getGuild().getSelfMember().getId())).findFirst().orElse(null) != null) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(new Color(149, 79, 180));
            embed.setDescription(":bird: Hi, I am " + event.getJDA().getSelfUser().getAsMention() + ", your new favourite musicbot!\n\nYou can find out more about me with **"
                    + this.discordBot.getBackendManager().getPrefix(event.getGuild()) + "help**,\non **" + event.getGuild().getName() + "** you can control me with the prefix `" + this.discordBot.getBackendManager().getPrefix(event.getGuild()) + "` ");
            this.discordBot.getBackendManager().sendMessage(event, embed.build());
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
        this.discordBot.consoleInfo(MessageFormat.format("[SHARD {0}] {1} ({2}) ran command {3} in {4} (#{5})", event.getJDA().getShardInfo().getShardId(), event.getAuthor().getName(), event.getAuthor().getId(), message.getContentRaw().toLowerCase().split(" ")[0], event.getGuild().getName(), event.getChannel().getName()));
    }

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
        if (this.discordBot.getBackendManager().getPlaying(event.getGuild())) {
            VoiceChannel voiceChannel = event.getGuild().getAudioManager().getConnectedChannel();
            if (voiceChannel == null) {
                event.getGuild().getAudioManager().openAudioConnection(event.getGuild().getVoiceChannelById(this.discordBot.getBackendManager().getChannelId(event.getGuild())));
                voiceChannel = event.getGuild().getVoiceChannelById(this.discordBot.getBackendManager().getChannelId(event.getGuild()));
            }

            if (event.getChannelJoined().getId().equals(voiceChannel.getId())) {
                if (event.getMember().getId().equals(event.getGuild().getSelfMember().getId())) {
                    return;
                }

                this.discordBot.getBackendManager().addListener(event.getGuild(), event.getMember(), event.getChannelJoined().getId());

                if (event.getGuild().getAudioManager().getSendingHandler() == null) {
                    event.getGuild().getAudioManager().openAudioConnection(voiceChannel);
                    switch (this.discordBot.getBackendManager().getMusic(event.getGuild())) {
                        case "one":
                            this.discordBot.getBackendManager().startStream(event.getGuild(), "https://listen.atomicradio.eu/one/highquality.mp3");
                            this.discordBot.getBackendManager().setPlaying(event.getGuild(), true);
                            this.discordBot.getBackendManager().setMusic(event.getGuild(), "one");
                            if (event.getGuild().getSelfMember().hasPermission(Permission.NICKNAME_CHANGE)) {
                                if (this.discordBot.getBackendManager().getTag(event.getGuild())) {
                                    event.getGuild().getSelfMember().modifyNickname("atomicradio » atr.one").queue();
                                }
                            }
                            break;
                        case "dance":
                            this.discordBot.getBackendManager().startStream(event.getGuild(), "https://listen.atomicradio.eu/dance/highquality.mp3");
                            this.discordBot.getBackendManager().setPlaying(event.getGuild(), true);
                            this.discordBot.getBackendManager().setMusic(event.getGuild(), "dance");
                            if (event.getGuild().getSelfMember().hasPermission(Permission.NICKNAME_CHANGE)) {
                                if (this.discordBot.getBackendManager().getTag(event.getGuild())) {
                                    event.getGuild().getSelfMember().modifyNickname("atomicradio » atr.dance").queue();
                                }
                            }
                            break;
                        case "trap":
                            this.discordBot.getBackendManager().startStream(event.getGuild(), "https://listen.atomicradio.eu/trap/highquality.mp3");
                            this.discordBot.getBackendManager().setPlaying(event.getGuild(), true);
                            this.discordBot.getBackendManager().setMusic(event.getGuild(), "trap");
                            if (event.getGuild().getSelfMember().hasPermission(Permission.NICKNAME_CHANGE)) {
                                if (this.discordBot.getBackendManager().getTag(event.getGuild())) {
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
        if (this.discordBot.getBackendManager().getPlaying(event.getGuild())) {
            VoiceChannel voiceChannel = event.getGuild().getAudioManager().getConnectedChannel();
            if (voiceChannel == null) {
                return;
            }

            if (event.getChannelLeft().getId().equals(voiceChannel.getId())) {
                this.discordBot.getBackendManager().removeListener(event.getGuild(), event.getMember());
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

        if (event.getMember().getId().equals(event.getGuild().getSelfMember().getId())) {
            if (this.discordBot.getBackendManager().getPlaying(event.getGuild())) {
                VoiceChannel voiceChannel = event.getChannelJoined();

                if (voiceChannel.getMembers().size() == 1) {
                    if (event.getGuild().getAudioManager().getSendingHandler() != null) {
                        AudioHandler audioHandler = (AudioHandler) event.getGuild().getAudioManager().getSendingHandler();
                        audioHandler.stop();
                        event.getGuild().getAudioManager().setSendingHandler(null);
                    }
                } else {
                    if (event.getGuild().getAudioManager().getSendingHandler() == null) {
                        switch (this.discordBot.getBackendManager().getMusic(event.getGuild())) {
                            case "one":
                                this.discordBot.getBackendManager().startStream(event.getGuild(), "https://listen.atomicradio.eu/one/highquality.mp3");
                                this.discordBot.getBackendManager().setPlaying(event.getGuild(), true);
                                this.discordBot.getBackendManager().setMusic(event.getGuild(), "one");
                                if (event.getGuild().getSelfMember().hasPermission(Permission.NICKNAME_CHANGE)) {
                                    if (this.discordBot.getBackendManager().getTag(event.getGuild())) {
                                        event.getGuild().getSelfMember().modifyNickname("atomicradio » atr.one").queue();
                                    }
                                }
                                break;
                            case "dance":
                                this.discordBot.getBackendManager().startStream(event.getGuild(), "https://listen.atomicradio.eu/dance/highquality.mp3");
                                this.discordBot.getBackendManager().setPlaying(event.getGuild(), true);
                                this.discordBot.getBackendManager().setMusic(event.getGuild(), "dance");
                                if (event.getGuild().getSelfMember().hasPermission(Permission.NICKNAME_CHANGE)) {
                                    if (this.discordBot.getBackendManager().getTag(event.getGuild())) {
                                        event.getGuild().getSelfMember().modifyNickname("atomicradio » atr.dance").queue();
                                    }
                                }
                                break;
                            case "trap":
                                this.discordBot.getBackendManager().startStream(event.getGuild(), "https://listen.atomicradio.eu/trap/highquality.mp3");
                                this.discordBot.getBackendManager().setPlaying(event.getGuild(), true);
                                this.discordBot.getBackendManager().setMusic(event.getGuild(), "trap");
                                if (event.getGuild().getSelfMember().hasPermission(Permission.NICKNAME_CHANGE)) {
                                    if (this.discordBot.getBackendManager().getTag(event.getGuild())) {
                                        event.getGuild().getSelfMember().modifyNickname("atomicradio » atr.trap").queue();
                                    }
                                }
                                break;
                        }
                    }
                }
            }
            return;
        }

        if (this.discordBot.getBackendManager().getPlaying(event.getGuild())) {
            VoiceChannel voiceChannel = event.getGuild().getAudioManager().getConnectedChannel();
            if (voiceChannel == null) {
                event.getGuild().getAudioManager().openAudioConnection(event.getGuild().getVoiceChannelById(this.discordBot.getBackendManager().getChannelId(event.getGuild())));
                voiceChannel = event.getGuild().getVoiceChannelById(this.discordBot.getBackendManager().getChannelId(event.getGuild()));
            }

            if (event.getChannelJoined().getId().equals(voiceChannel.getId())) {
                if (event.getMember().getId().equals(event.getGuild().getSelfMember().getId())) {
                    return;
                }
                this.discordBot.getBackendManager().addListener(event.getGuild(), event.getMember(), event.getChannelJoined().getId());
                if (event.getGuild().getAudioManager().getSendingHandler() == null) {
                    switch (this.discordBot.getBackendManager().getMusic(event.getGuild())) {
                        case "one":
                            this.discordBot.getBackendManager().startStream(event.getGuild(), "https://listen.atomicradio.eu/one/highquality.mp3");
                            this.discordBot.getBackendManager().setPlaying(event.getGuild(), true);
                            this.discordBot.getBackendManager().setMusic(event.getGuild(), "one");
                            if (event.getGuild().getSelfMember().hasPermission(Permission.NICKNAME_CHANGE)) {
                                if (this.discordBot.getBackendManager().getTag(event.getGuild())) {
                                    event.getGuild().getSelfMember().modifyNickname("atomicradio » atr.one").queue();
                                }
                            }
                            break;
                        case "dance":
                            this.discordBot.getBackendManager().startStream(event.getGuild(), "https://listen.atomicradio.eu/dance/highquality.mp3");
                            this.discordBot.getBackendManager().setPlaying(event.getGuild(), true);
                            this.discordBot.getBackendManager().setMusic(event.getGuild(), "dance");
                            if (event.getGuild().getSelfMember().hasPermission(Permission.NICKNAME_CHANGE)) {
                                if (this.discordBot.getBackendManager().getTag(event.getGuild())) {
                                    event.getGuild().getSelfMember().modifyNickname("atomicradio » atr.dance").queue();
                                }
                            }
                            break;
                        case "trap":
                            this.discordBot.getBackendManager().startStream(event.getGuild(), "https://listen.atomicradio.eu/trap/highquality.mp3");
                            this.discordBot.getBackendManager().setPlaying(event.getGuild(), true);
                            this.discordBot.getBackendManager().setMusic(event.getGuild(), "trap");
                            if (event.getGuild().getSelfMember().hasPermission(Permission.NICKNAME_CHANGE)) {
                                if (this.discordBot.getBackendManager().getTag(event.getGuild())) {
                                    event.getGuild().getSelfMember().modifyNickname("atomicradio » atr.trap").queue();
                                }
                            }
                            break;
                    }
                }
            } else if (event.getChannelLeft().getId().equals(voiceChannel.getId())) {
                this.discordBot.getBackendManager().removeListener(event.getGuild(), event.getMember());
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

}
