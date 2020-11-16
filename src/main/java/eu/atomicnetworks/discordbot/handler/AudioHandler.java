package eu.atomicnetworks.discordbot.handler;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import eu.atomicnetworks.discordbot.DiscordBot;
import java.nio.ByteBuffer;
import java.text.MessageFormat;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.Guild;

/**
 *
 * @author kacpe
 */
public class AudioHandler extends AudioEventAdapter implements AudioSendHandler {

    private final DiscordBot discord;
    private final AudioPlayer player;
    private AudioFrame lastFrame;
    private Guild guild;

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
        this.discord.consoleInfo("Stream started playing on guild " + guild.getName() + ". (" + guild.getId() + ")");
        this.discord.getBackendManager().getPlaying().add(guild.getId());
        this.discord.consoleInfo(MessageFormat.format("Playing now on {0} guilds.", this.discord.getBackendManager().getPlaying().size()));
    }

    @Override
    public void onTrackEnd(com.sedmelluq.discord.lavaplayer.player.AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        this.discord.consoleInfo("Stream reached end on guild " + guild.getName() + ". (" + guild.getId() + ")");
        this.discord.getBackendManager().getPlaying().remove(guild.getId());
        this.discord.consoleInfo(MessageFormat.format("Playing now on {0} guilds.", this.discord.getBackendManager().getPlaying().size()));
    }

    @Override
    public void onTrackException(com.sedmelluq.discord.lavaplayer.player.AudioPlayer player, AudioTrack track, FriendlyException exception) {
        this.discord.consoleError("Stream error on guild " + guild.getName() + ". (" + guild.getId() + ")");
    }

    @Override
    public void onTrackStuck(com.sedmelluq.discord.lavaplayer.player.AudioPlayer player, AudioTrack track, long thresholdMs) {
        System.out.println("Stream stuck! " + thresholdMs + "ms.");
    }

    @Override
    public boolean canProvide() {
        lastFrame = player.provide();
        return lastFrame != null;
    }

    public void stop() {
        player.stopTrack();
        player.destroy();
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
