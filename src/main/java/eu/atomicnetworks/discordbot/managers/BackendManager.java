package eu.atomicnetworks.discordbot.managers;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import eu.atomicnetworks.discordbot.DiscordBot;
import eu.atomicnetworks.discordbot.handler.AudioHandler;
import eu.atomicnetworks.discordbot.object.GuildData;
import eu.atomicnetworks.discordbot.object.Listeners;
import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ContextException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.internal.utils.PermissionUtil;

/**
 *
 * @author Kacper Mura
 * 2020 Copyright (c) by atomicradio.eu to present.
 * All rights reserved. https://github.com/VocalZero
 *
 */
public class BackendManager {
    
    private final DiscordBot discord;
    private LoadingCache<String, GuildData> guildCache;
    private Listeners listeners;

    public BackendManager(DiscordBot discord) {
        this.discord = discord;
        this.listeners = new Listeners();
        this.listeners.setListener(new HashMap<>());
        initCache();
    }
    
    private void initCache() {
        this.guildCache = (LoadingCache<String, GuildData>) CacheBuilder.newBuilder().maximumSize(100L).expireAfterWrite(10L, TimeUnit.MINUTES).build((CacheLoader) new CacheLoader<String, GuildData>() {
            @Override
            public GuildData load(String id) throws Exception {
                CompletableFuture<GuildData> completableFuture = new CompletableFuture<>();
                discord.getGuildManager().getGuild(id, result -> {
                    completableFuture.complete(result);
                });
                return completableFuture.get();
            }
        });
    }

    public LoadingCache<String, GuildData> getGuildCache() {
        return guildCache;
    }
    
    public GuildData getGuild(Guild guild) {
        try {
            return this.guildCache.get(guild.getId());
        } catch (ExecutionException ex) {
            return null;
        }
    }
    
    public GuildData getGuild(String id) {
        try {
            return this.guildCache.get(id);
        } catch (ExecutionException ex) {
            return null;
        }
    }
    
    public String getPrefix(Guild guild) {
        return this.getGuild(guild).getPrefix();
    }
    
    public void setPrefix(Guild guild, String prefix) {
        this.getGuild(guild).setPrefix(prefix);
        this.discord.getGuildManager().saveGuild(this.getGuild(guild));
    }
    
    public boolean getPlaying(Guild guild) {
        return this.getGuild(guild).isPlaying();
    }
    
    public void setPlaying(Guild guild, boolean started) {
        this.getGuild(guild).setPlaying(started);
        this.discord.getGuildManager().saveGuild(this.getGuild(guild));
    }
    
    public String getMusic(Guild guild) {
        return this.getGuild(guild).getMusic();
    }
    
    public void setMusic(Guild guild, String music) {
        this.getGuild(guild).setMusic(music);
        this.discord.getGuildManager().saveGuild(this.getGuild(guild));
    }
    
    public boolean getTag(Guild guild) {
        return this.getGuild(guild).isTag();
    }
    
    public void setTag(Guild guild, boolean restart) {
        this.getGuild(guild).setTag(restart);
        this.discord.getGuildManager().saveGuild(this.getGuild(guild));
    }
    
    public String getChannelId(Guild guild) {
        return this.getGuild(guild).getChannelId();
    }
    
    public void setChannelId(Guild guild, String channel) {
        this.getGuild(guild).setChannelId(channel);
        this.discord.getGuildManager().saveGuild(this.getGuild(guild));
    }
    
    public int getVolume(Guild guild) {
        return this.getGuild(guild).getVolume();
    }
    
    public void setVolume(Guild guild, int volume) {
        this.getGuild(guild).setVolume(volume);
        this.discord.getGuildManager().saveGuild(this.getGuild(guild));
    }
    
    public int getBassLevel(Guild guild) {
        return this.getGuild(guild).getBassLevel();
    }
    
    public void setBassLevel(Guild guild, int bass) {
        this.getGuild(guild).setBassLevel(bass);
        this.discord.getGuildManager().saveGuild(this.getGuild(guild));
    }
    
    public boolean isMusicCommandsDenied(Guild guild) {
        return this.getGuild(guild).isMusicCommandsDenied();
    }

    public void setMusicCommands(Guild guild, boolean musicCommands) {
        this.getGuild(guild).setMusicCommands(musicCommands);
        this.discord.getGuildManager().saveGuild(this.getGuild(guild));
    }
    
    public void startStream(Guild guild, String url) {
        AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        playerManager.getConfiguration().setFilterHotSwapEnabled(true);
        AudioPlayer player = playerManager.createPlayer();
        AudioHandler trackScheduler = new AudioHandler(this.discord, player, guild);
        player.addListener(trackScheduler);
        guild.getAudioManager().setSendingHandler(trackScheduler);
        player.setVolume(this.getVolume(guild));
        
        if(this.getBassLevel(guild) != 0) {
            switch(this.getBassLevel(guild)) {
                case 1:
                    trackScheduler.bassFilter(20);
                    break;
                case 2:
                    trackScheduler.bassFilter(40);
                    break;
                case 3:
                    trackScheduler.bassFilter(60);
                    break;
                case 4:
                    trackScheduler.bassFilter(80);
                    break;
                case 5:
                    trackScheduler.bassFilter(100);
                    break;
            }
        }
        
        playerManager.loadItem(url, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack at) {
                player.playTrack(at);
                discord.consoleInfo("[SHARD " + guild.getJDA().getShardInfo().getShardId() + "] Stream loaded on guild " + guild.getName() + ". (" + guild.getId() + ")");
            }

            @Override
            public void playlistLoaded(AudioPlaylist ap) {
            }

            @Override
            public void noMatches() {
                discord.consoleError("[SHARD " + guild.getJDA().getShardInfo().getShardId() + "] Stream not found on guild " + guild.getName() + ". (" + guild.getId() + ")");
            }

            @Override
            public void loadFailed(FriendlyException fe) {
                discord.consoleError("[SHARD " + guild.getJDA().getShardInfo().getShardId() + "] Stream failed to loaded on guild " + guild.getName() + ". (" + guild.getId() + ")");
            }
        });
    }
    
    public int getGuildCount() {
        int count = 0;
        for (JDA shard : this.discord.getShardManager().getShards()) {
            count = count+shard.getGuilds().size();
        }
        return count;
    }
        
    public int getUserCount() {
        int count = 0;
        for (JDA shard : this.discord.getShardManager().getShards()) {
            count = shard.getGuilds().stream().map(guild -> guild.getMemberCount()).reduce(count, Integer::sum);
        }
        return count;
    }
    
    public int getConnectionCount() {
        int count = 0;
        for (JDA shard : this.discord.getShardManager().getShards()) {
            count = shard.getGuilds().stream().filter(guild -> (guild.getAudioManager().isConnected())).map(_item -> 1).reduce(count, Integer::sum);
        }
        return count;
    }
    
    public int getPlayingCount() {
        int count = 0;
        for (JDA shard : this.discord.getShardManager().getShards()) {
            count = shard.getGuilds().stream().filter(guild -> (guild.getAudioManager().getSendingHandler() != null)).map(_item -> 1).reduce(count, Integer::sum);
        }
        return count;
    }
    
    public boolean checkForPermissions(Member member) {
        if(member == null) {
            return false;
        }
        return PermissionUtil.checkPermission(member, Permission.ADMINISTRATOR) || member.getId().equals("425706045453893642") || member.getId().equals("223891083724193792") || member.getId().equals("394586910065950723");
    }
    
    public void sendMessage(GuildMessageReceivedEvent event, MessageEmbed messageEmbed) {
        try {
            event.getChannel().sendMessage(messageEmbed).queue();
        } catch(InsufficientPermissionException ex) {
            try {
                event.getChannel().sendMessage("I do not have permissions for **" + ex.getPermission().getName().toLowerCase() + "**, please contact an administrator.").queue();
            } catch (InsufficientPermissionException ex1) {
                try {
                    event.getAuthor().openPrivateChannel().queue((channel) -> {
                        EmbedBuilder embed = new EmbedBuilder();
                        embed.setColor(new Color(149, 79, 180));
                        embed.setDescription("I do not have permissions to **" + ex.getPermission().getName().toLowerCase() + "** in " + event.getChannel().getAsMention() + ", please contact an administrator.");
                        try {
                            channel.sendMessage(embed.build()).queue();
                        } catch (InsufficientPermissionException ex2) {
                        }
                    });
                } catch (InsufficientPermissionException ex2) {
                }
            }
        }
    }

    public Listeners getListeners() {
        return listeners;
    }
    
    public void addListener(Member member, String channelId) {
        Listeners.Listener listener = new Listeners.Listener();
        listener.setUsername(member.getUser().getName());
        listener.setId(member.getUser().getId());
        listener.setAvatar(member.getUser().getAvatarUrl());
        listener.setChannelId(channelId);
        this.listeners.getListener().put(member.getUser().getId(), listener);
    }
    
    public void removeListener(Member member) {
        if(this.listeners.getListener().containsKey(member.getUser().getId())) {
            this.listeners.getListener().remove(member.getUser().getId());
        }
    }
    
}
