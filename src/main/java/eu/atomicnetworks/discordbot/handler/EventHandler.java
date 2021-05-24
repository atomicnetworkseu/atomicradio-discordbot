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
import eu.atomicnetworks.discordbot.commands.ShardCommand;
import eu.atomicnetworks.discordbot.commands.SongCommand;
import eu.atomicnetworks.discordbot.commands.VolumeCommand;
import eu.atomicnetworks.discordbot.enums.StationChannnel;
import java.awt.Color;
import java.text.MessageFormat;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
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
                            if(!t.getUser().isBot()) {
                                this.discordBot.getBackendManager().addListener(t, voiceChannel.getId());
                            }
                        });

                        if (voiceChannel.getMembers().size() >= 1) {
                            if (guild.getAudioManager().getSendingHandler() == null) {
                                
                                try {
                                    StationChannnel stationChannnel = StationChannnel.valueOf(this.discordBot.getBackendManager().getMusic(guild));
                                    this.discordBot.getBackendManager().startStream(guild, stationChannnel.getUrl());
                                    this.discordBot.getBackendManager().setPlaying(guild, true);
                                    this.discordBot.getBackendManager().setMusic(guild, stationChannnel.getName());
                                    this.discordBot.getBackendManager().setChannelId(guild, voiceChannel.getId());
                                    this.discordBot.consoleInfo("[SHARD " + event.getJDA().getShardInfo().getShardId() + "] Restarting stream on " + guild.getName() + " with atr." + stationChannnel.getName());
                                    if (guild.getSelfMember().hasPermission(Permission.NICKNAME_CHANGE)) {
                                        if (this.discordBot.getBackendManager().getTag(guild)) {
                                            guild.getSelfMember().modifyNickname("atomicradio » atr." + stationChannnel.getName()).queue();
                                        }
                                    }
                                } catch(IllegalArgumentException ex) {
                                    StationChannnel stationChannnel = StationChannnel.ONE;
                                    this.discordBot.getBackendManager().startStream(guild, stationChannnel.getUrl());
                                    this.discordBot.getBackendManager().setPlaying(guild, true);
                                    this.discordBot.getBackendManager().setMusic(guild, stationChannnel.getName());
                                    this.discordBot.getBackendManager().setChannelId(guild, voiceChannel.getId());
                                    this.discordBot.consoleInfo("[SHARD " + event.getJDA().getShardInfo().getShardId() + "] Restarting stream on " + guild.getName() + " with atr." + stationChannnel.getName());
                                    if (guild.getSelfMember().hasPermission(Permission.NICKNAME_CHANGE)) {
                                        if (this.discordBot.getBackendManager().getTag(guild)) {
                                            guild.getSelfMember().modifyNickname("atomicradio » atr." + stationChannnel.getName()).queue();
                                        }
                                    }
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
        String command = (message.getContentDisplay().toLowerCase().split(" ")[0]).substring(prefix.length());
        
        switch(command) {
            case "help":
            case "h":
                helpCommand.execute(event);
                break;
            case "info":
            case "invite":
            case "i":
                infoCommand.execute(event);
                break;
            case "join":
            case "j":
                joinCommand.execute(event);
                break;
            case "leave":
            case "l":
                leaveCommand.execute(event);
                break;
            case "play":
            case "p":
                playCommand.execute(event);
                break;
            case "volume":
            case "vol":
            case "v":
                volumeCommand.execute(event);
                break;
            case "settings":
                settingsCommand.execute(event);
                break;
            case "report":
            case "r":
                reportCommand.execute(event);
                break;
            case "song":
            case "nowplaying":
            case "np":
                songCommand.execute(event);
                break;
            case "bass":
            case "b":
                bassCommand.execute(event);
                break;
            case "shard":
                shardCommand.execute(event);
                break;
            default:
                return;
        }
        this.discordBot.consoleInfo(MessageFormat.format("[SHARD {0}] {1} ({2}) ran command {3} in {4} (#{5})", event.getJDA().getShardInfo().getShardId(), event.getAuthor().getName(), event.getAuthor().getId(), message.getContentRaw().toLowerCase().split(" ")[0], event.getGuild().getName(), event.getChannel().getName()));
    }

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
        if (this.discordBot.getBackendManager().getPlaying(event.getGuild())) {
            VoiceChannel voiceChannel = event.getGuild().getAudioManager().getConnectedChannel();
            if (voiceChannel == null) {
                try {
                    if(event.getGuild().getVoiceChannelById(this.discordBot.getBackendManager().getChannelId(event.getGuild())) == null) {
                        return;
                    }
                    event.getGuild().getAudioManager().openAudioConnection(event.getGuild().getVoiceChannelById(this.discordBot.getBackendManager().getChannelId(event.getGuild())));
                    voiceChannel = event.getGuild().getVoiceChannelById(this.discordBot.getBackendManager().getChannelId(event.getGuild()));
                } catch (InsufficientPermissionException ex) {
                    return;
                }
            }

            if (event.getChannelJoined().getId().equals(voiceChannel.getId())) {
                if (event.getMember().getId().equals(event.getGuild().getSelfMember().getId())) {
                    event.getChannelJoined().getMembers().stream().forEach(t -> {
                        if(!t.getUser().isBot()) {
                            this.discordBot.getBackendManager().addListener(t, event.getChannelJoined().getId());
                        }
                    });
                    return;
                }

                if(!event.getMember().getUser().isBot()) {
                    this.discordBot.getBackendManager().addListener(event.getMember(), event.getChannelJoined().getId());
                }

                if (event.getGuild().getAudioManager().getSendingHandler() == null) {
                    try {
                        event.getGuild().getAudioManager().openAudioConnection(voiceChannel);
                    } catch (InsufficientPermissionException ex) {
                        return;
                    }
                    
                    try {
                        StationChannnel stationChannnel = StationChannnel.valueOf(this.discordBot.getBackendManager().getMusic(event.getGuild()));
                        this.discordBot.getBackendManager().startStream(event.getGuild(), stationChannnel.getUrl());
                        this.discordBot.getBackendManager().setPlaying(event.getGuild(), true);
                        this.discordBot.getBackendManager().setMusic(event.getGuild(), stationChannnel.getName());
                        if (event.getGuild().getSelfMember().hasPermission(Permission.NICKNAME_CHANGE)) {
                            if (this.discordBot.getBackendManager().getTag(event.getGuild())) {
                                event.getGuild().getSelfMember().modifyNickname("atomicradio » atr." + stationChannnel.getName()).queue();
                            }
                        }
                    } catch(IllegalArgumentException ex) {}
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
            
            if (event.getMember().getId().equals(event.getGuild().getSelfMember().getId())) {
                voiceChannel.getMembers().stream().forEach(t -> {
                    this.discordBot.getBackendManager().removeListener(t);
                });
                return;
            }

            if (event.getChannelLeft().getId().equals(voiceChannel.getId())) {
                this.discordBot.getBackendManager().removeListener(event.getMember());
                if (event.getChannelLeft().getMembers().size() == 1) {
                    if (event.getGuild().getAudioManager().getSendingHandler() != null) {
                        AudioHandler audioHandler = (AudioHandler) event.getGuild().getAudioManager().getSendingHandler();
                        audioHandler.stop();
                        event.getGuild().getAudioManager().setSendingHandler(null);
                    }
                }
            }
        } else {
            if (event.getMember().getId().equals(event.getGuild().getSelfMember().getId())) {
                event.getChannelLeft().getMembers().stream().forEach(t -> {
                    this.discordBot.getBackendManager().removeListener(t);
                });
            }
        }
    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
        if (event.getMember().getId().equals(event.getGuild().getSelfMember().getId())) {
            
            event.getChannelLeft().getMembers().stream().forEach(t -> {
                this.discordBot.getBackendManager().removeListener(t);
            });
            event.getChannelJoined().getMembers().stream().forEach(t -> {
                if(!t.getUser().isBot()) {
                    this.discordBot.getBackendManager().addListener(t, event.getChannelJoined().getId());
                }
            });
            
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
                        try {
                            StationChannnel stationChannnel = StationChannnel.valueOf(this.discordBot.getBackendManager().getMusic(event.getGuild()));
                            this.discordBot.getBackendManager().startStream(event.getGuild(), stationChannnel.getUrl());
                            this.discordBot.getBackendManager().setPlaying(event.getGuild(), true);
                            this.discordBot.getBackendManager().setMusic(event.getGuild(), stationChannnel.getName());
                            if (event.getGuild().getSelfMember().hasPermission(Permission.NICKNAME_CHANGE)) {
                                if (this.discordBot.getBackendManager().getTag(event.getGuild())) {
                                    event.getGuild().getSelfMember().modifyNickname("atomicradio » atr." + stationChannnel.getName()).queue();
                                }
                            }
                        } catch (IllegalArgumentException ex) {
                        }
                    }
                }
            }
            return;
        }

        if (this.discordBot.getBackendManager().getPlaying(event.getGuild())) {
            VoiceChannel voiceChannel = event.getGuild().getAudioManager().getConnectedChannel();
            if (voiceChannel == null) {
                try {
                    if(event.getGuild().getVoiceChannelById(this.discordBot.getBackendManager().getChannelId(event.getGuild())) == null) {
                        return;
                    }
                    event.getGuild().getAudioManager().openAudioConnection(event.getGuild().getVoiceChannelById(this.discordBot.getBackendManager().getChannelId(event.getGuild())));
                    voiceChannel = event.getGuild().getVoiceChannelById(this.discordBot.getBackendManager().getChannelId(event.getGuild()));
                } catch (InsufficientPermissionException ex) {
                    return;
                }
            }

            if (event.getChannelJoined().getId().equals(voiceChannel.getId())) {
                if (event.getMember().getId().equals(event.getGuild().getSelfMember().getId())) {
                    return;
                }
                if(!event.getMember().getUser().isBot()) {
                    this.discordBot.getBackendManager().addListener(event.getMember(), event.getChannelJoined().getId());
                }
                if (event.getGuild().getAudioManager().getSendingHandler() == null) {
                    
                    try {
                        StationChannnel stationChannnel = StationChannnel.valueOf(this.discordBot.getBackendManager().getMusic(event.getGuild()));
                        this.discordBot.getBackendManager().startStream(event.getGuild(), stationChannnel.getUrl());
                        this.discordBot.getBackendManager().setPlaying(event.getGuild(), true);
                        this.discordBot.getBackendManager().setMusic(event.getGuild(), stationChannnel.getName());
                        if (event.getGuild().getSelfMember().hasPermission(Permission.NICKNAME_CHANGE)) {
                            if (this.discordBot.getBackendManager().getTag(event.getGuild())) {
                                event.getGuild().getSelfMember().modifyNickname("atomicradio » atr." + stationChannnel.getName()).queue();
                            }
                        }
                    } catch (IllegalArgumentException ex) {
                    }
                }
            } else if (event.getChannelLeft().getId().equals(voiceChannel.getId())) {
                this.discordBot.getBackendManager().removeListener(event.getMember());
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
