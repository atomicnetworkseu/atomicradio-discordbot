package eu.atomicnetworks.discordbot.handler;

import com.sedmelluq.discord.lavaplayer.filter.AudioFilter;
import com.sedmelluq.discord.lavaplayer.filter.FloatPcmAudioFilter;
import com.sedmelluq.discord.lavaplayer.filter.equalizer.Equalizer;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import eu.atomicnetworks.discordbot.DiscordBot;
import eu.atomicnetworks.discordbot.enums.StationChannnel;
import java.nio.ByteBuffer;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.Guild;

/**
 *
 * @author Kacper Mura
 * 2020 Copyright (c) by atomicradio.eu to present.
 * All rights reserved. https://github.com/VocalZero
 *
 */
public class AudioHandler extends AudioEventAdapter implements AudioSendHandler {

    private final DiscordBot discord;
    private final AudioPlayer player;
    private final Guild guild;
    private AudioFrame lastFrame;

    public AudioHandler(DiscordBot discord, AudioPlayer player, Guild guild) {
        this.discord = discord;
        this.player = player;
        this.guild = guild;
    }

    @Override
    public void onPlayerPause(com.sedmelluq.discord.lavaplayer.player.AudioPlayer player) {
        System.out.println("Player was paused");
    }

    @Override
    public void onPlayerResume(com.sedmelluq.discord.lavaplayer.player.AudioPlayer player) {
        System.out.println("Player was resumed");
    }

    @Override
    public void onTrackStart(com.sedmelluq.discord.lavaplayer.player.AudioPlayer player, AudioTrack track) {
        this.discord.consoleInfo("[SHARD " + guild.getJDA().getShardInfo().getShardId() + "] Stream started playing on guild " + guild.getName() + ". (" + guild.getId() + ")");
        this.discord.consoleInfo(MessageFormat.format("[SHARD {0}] Playing now on {1} guilds.", guild.getJDA().getShardInfo().getShardId(), this.discord.getBackendManager().getPlayingCount()));
    }

    @Override
    public void onTrackEnd(com.sedmelluq.discord.lavaplayer.player.AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        this.discord.consoleInfo("[SHARD " + guild.getJDA().getShardInfo().getShardId() + "] Stream reached end on guild " + guild.getName() + ". (" + endReason.toString() + ")");
        this.discord.consoleInfo(MessageFormat.format("[SHARD {0}] Playing now on {1} guilds.", guild.getJDA().getShardInfo().getShardId(), (this.discord.getBackendManager().getPlayingCount()-1)));
    }

    @Override
    public void onTrackException(com.sedmelluq.discord.lavaplayer.player.AudioPlayer player, AudioTrack track, FriendlyException exception) {
        this.discord.consoleError("[SHARD " + guild.getJDA().getShardInfo().getShardId() + "] Stream error on guild " + guild.getName() + ". (" + exception.getMessage() + ")");
        Logger.getLogger(AudioHandler.class.getName()).log(Level.SEVERE, null, exception);
        
        if(this.discord.getBackendManager().getPlaying(guild)) {
            try {
                StationChannnel stationChannnel = StationChannnel.valueOf(this.discord.getBackendManager().getMusic(guild));
                this.discord.getBackendManager().startStream(guild, stationChannnel.getUrl());
            } catch (IllegalArgumentException ex) {
                this.discord.getBackendManager().startStream(guild, StationChannnel.ONE.getUrl());
            }
        }
    }

    @Override
    public void onTrackStuck(com.sedmelluq.discord.lavaplayer.player.AudioPlayer player, AudioTrack track, long thresholdMs) {
        this.discord.consoleWarning("[SHARD " + guild.getJDA().getShardInfo().getShardId() + "] Stream stuck on guild " + guild.getName() + ". " + thresholdMs + "ms.");
    }

    @Override
    public boolean canProvide() {
        lastFrame = player.provide();
        return lastFrame != null;
    }

    public void stop() {
        player.stopTrack();
        player.destroy();
        guild.getAudioManager().setSendingHandler(null);
    }
    
    public void bassFilter(float percentage) {
        float[] gainFrequency = { -0.05f, 0.07f, 0.16f, 0.03f, -0.05f, -0.11f };
        float multiplier = percentage / 100;
        
        player.setFilterFactory((track, format, output)->{
            List<AudioFilter> audioFilterList = new ArrayList<>();
            
            FloatPcmAudioFilter floatPcmAudioFilter = output;
            Equalizer equalizer = new Equalizer(format.channelCount, floatPcmAudioFilter);
            for (int i = 0; i < gainFrequency.length; i++) {
                equalizer.setGain(i, gainFrequency[i]*multiplier);
            }
            // floatPcmAudioFilter = equalizer;
            audioFilterList.add(equalizer);
            
            this.discord.consoleInfo("[SHARD " + guild.getJDA().getShardInfo().getShardId() + "] Audio Bass Filter was activated on guild " + guild.getName() + ". " + percentage + "%");
            return audioFilterList;
        });
    }
    
    public void removeFilter() {
        player.setFilterFactory(null);
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        return ByteBuffer.wrap(lastFrame.getData());
    }

    @Override
    public boolean isOpus() {
        return true;
    }
}
