package eu.atomicnetworks.discordbot.commands;

import eu.atomicnetworks.discordbot.DiscordBot;
import eu.atomicradio.objects.Channel;
import eu.atomicradio.objects.Channel.Song;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.utils.AttachmentOption;

/**
 *
 * @author Kacper Mura
 * 2020 Copyright (c) by atomicradio.eu to present.
 * All rights reserved. https://github.com/VocalZero
 *
 */
public class SongCommand {

    private final DiscordBot discord;

    public SongCommand(DiscordBot discord) {
        this.discord = discord;
    }

    public void execute(GuildMessageReceivedEvent event) {
        Message message = event.getMessage();
        String[] args = message.getContentRaw().split(" ");
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(new Color(149, 79, 180));
        
        String channel = this.discord.getBackendManager().getMusic(event.getGuild());
        if (channel.isEmpty()) {
            channel = "one";
        }
        
        Channel targetChannel = null;
        switch(channel.toLowerCase()) {
            case "one":
                targetChannel = this.discord.getAtomicClient().getChannelOne();
                break;
            case "dance":
                targetChannel = this.discord.getAtomicClient().getChannelDance();
                break;
            case "trap":
                targetChannel = this.discord.getAtomicClient().getChannelTrap();
                break;
        }
        
        if(targetChannel == null) {
            return;
        }

        try {
            InputStream inputStream = null;
            if (args.length == 1) {
                try {
                    URL url = new URL(MessageFormat.format("https://api.atomicradio.eu/cards?author={0}&title={1}&start_at=00:{2}&end_at=00:{3}&station={4}&playing={5}&image={6}", URLEncoder.encode(targetChannel.getSong().getArtist(), StandardCharsets.UTF_8.toString()), URLEncoder.encode(targetChannel.getSong().getTitle(), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.getDurationTimestampNow(targetChannel.getSong().getStart_at()), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.getDurationTimestamp(targetChannel.getSong().getStart_at(), targetChannel.getSong().getEnd_at()), StandardCharsets.UTF_8.toString()), channel, "now", URLEncoder.encode(targetChannel.getSong().getArtworks().getArt500(), StandardCharsets.UTF_8.toString())));
                    inputStream = url.openStream();
                    event.getChannel().sendFile(inputStream, "song.png", new AttachmentOption[0]).queue();
                } catch (MalformedURLException ex) {
                    Logger.getLogger(SongCommand.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(SongCommand.class.getName()).log(Level.SEVERE, null, ex);
                }
                return;
            }

            switch (args[1].toLowerCase()) {
                case "last":
                    try {
                        Song lastSong = targetChannel.getHistory().stream().findFirst().orElse(null);
                        URL url = new URL(MessageFormat.format("https://api.atomicradio.eu/cards?author={0}&title={1}&start_at=00:{2}&end_at=00:{3}&station={4}&playing={5}&image={6}", URLEncoder.encode(lastSong.getArtist(), StandardCharsets.UTF_8.toString()), URLEncoder.encode(lastSong.getTitle(), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.getDurationTimestampNow(lastSong.getStart_at()), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.getDurationTimestamp(lastSong.getStart_at(), lastSong.getEnd_at()), StandardCharsets.UTF_8.toString()), channel, "last", URLEncoder.encode(lastSong.getArtworks().getArt500(), StandardCharsets.UTF_8.toString())));
                        inputStream = url.openStream();
                        event.getChannel().sendFile(inputStream, "song.png", new AttachmentOption[0]).queue();
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(SongCommand.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(SongCommand.class.getName()).log(Level.SEVERE, null, ex);
                    }
                break;
                
                case "next":
                    try {
                        Song nextSong = targetChannel.getSchedule().stream().findFirst().orElse(null);
                        URL url = new URL(MessageFormat.format("https://api.atomicradio.eu/cards?author={0}&title={1}&start_at=00:{2}&end_at=00:{3}&station={4}&playing={5}&image={6}", URLEncoder.encode(nextSong.getArtist(), StandardCharsets.UTF_8.toString()), URLEncoder.encode(nextSong.getTitle(), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.getDurationTimestampNow(nextSong.getStart_at()), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.getDurationTimestamp(nextSong.getStart_at(), nextSong.getEnd_at()), StandardCharsets.UTF_8.toString()), channel, "next", URLEncoder.encode(nextSong.getArtworks().getArt500(), StandardCharsets.UTF_8.toString())));
                        inputStream = url.openStream();
                        event.getChannel().sendFile(inputStream, "song.png", new AttachmentOption[0]).queue();
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(SongCommand.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(SongCommand.class.getName()).log(Level.SEVERE, null, ex);
                    }
                break;

                case "now":
                    try {
                        URL url = new URL(MessageFormat.format("https://api.atomicradio.eu/cards?author={0}&title={1}&start_at=00:{2}&end_at=00:{3}&station={4}&playing={5}&image={6}", URLEncoder.encode(targetChannel.getSong().getArtist(), StandardCharsets.UTF_8.toString()), URLEncoder.encode(targetChannel.getSong().getTitle(), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.getDurationTimestampNow(targetChannel.getSong().getStart_at()), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.getDurationTimestamp(targetChannel.getSong().getStart_at(), targetChannel.getSong().getEnd_at()), StandardCharsets.UTF_8.toString()), channel, "now", URLEncoder.encode(targetChannel.getSong().getArtworks().getArt500(), StandardCharsets.UTF_8.toString())));
                        inputStream = url.openStream();
                        event.getChannel().sendFile(inputStream, "song.png", new AttachmentOption[0]).queue();
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(SongCommand.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(SongCommand.class.getName()).log(Level.SEVERE, null, ex);
                    }
                break;

            }
        } catch (InsufficientPermissionException ex) {
            embed.setDescription("I do not have permission to **attach files**, please contact an administrator.");
            this.discord.getBackendManager().sendMessage(event, embed.build());
        }
    }
    
    public String getDurationTimestamp(Long start_at, Long end_at) {
        long time = ((end_at*1000)-(start_at*1000));
        String minutes = String.valueOf((time / 1000) / 60);
        String seconds = String.valueOf((time / 1000) % 60);
        if(minutes.length() == 1) {
            minutes = "0" + minutes;
        }
        if(seconds.length() == 1) {
            seconds = "0" + seconds;
        }
        return minutes + ":" + seconds;
    }
    
    public String getDurationTimestampNow(Long start_at) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = LocalDateTime.ofInstant(Instant.ofEpochMilli((start_at*1000)), TimeZone.getDefault().toZoneId());
        long diff = Math.abs(Duration.between(now, start).getSeconds());
        String seconds = String.valueOf(diff % 60);
        String minutes = String.valueOf(diff / 60);
        if(minutes.length() == 1) {
            minutes = "0" + minutes;
        }
        if(seconds.length() == 1) {
            seconds = "0" + seconds;
        }
        return minutes + ":" + seconds;
    }

}
