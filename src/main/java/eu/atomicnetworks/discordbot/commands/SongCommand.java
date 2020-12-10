package eu.atomicnetworks.discordbot.commands;

import eu.atomicnetworks.discordbot.DiscordBot;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.utils.AttachmentOption;

/**
 *
 * @author Kacper Mura 2020 Copyright (c) by atomicradio.eu to present. All
 * rights reserved. https://github.com/VocalZero
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

        try {
            InputStream inputStream = null;
            if (args.length == 1) {
                try {
                    URL url = new URL(MessageFormat.format("https://api.atomicradio.eu/cards?author={0}&title={1}&start_at=00:{2}&end_at=00:{3}&station={4}&playing={5}&image={6}", URLEncoder.encode(this.discord.getApiManager().getArtist(channel), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getTitle(channel), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getDurationTimestampNow(channel), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getDurationTimestamp(channel), StandardCharsets.UTF_8.toString()), channel, "now", URLEncoder.encode(this.discord.getApiManager().get500Artwork(channel), StandardCharsets.UTF_8.toString())));
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
                    URL url = new URL(MessageFormat.format("https://api.atomicradio.eu/cards?author={0}&title={1}&start_at=00:{2}&end_at=00:{3}&station={4}&playing={5}&image={6}", URLEncoder.encode(this.discord.getApiManager().getLastArtist(channel), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getLastTitle(channel), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getLastDurationTimestampNow(channel), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getLastDurationTimestamp(channel), StandardCharsets.UTF_8.toString()), channel, "last", URLEncoder.encode(this.discord.getApiManager().getLast500Artwork(channel), StandardCharsets.UTF_8.toString())));
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
                    URL url = new URL(MessageFormat.format("https://api.atomicradio.eu/cards?author={0}&title={1}&start_at=00:{2}&end_at=00:{3}&station={4}&playing={5}&image={6}", URLEncoder.encode(this.discord.getApiManager().getNextArtist(channel), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getNextTitle(channel), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getNextDurationTimestampNow(channel), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getNextDurationTimestamp(channel), StandardCharsets.UTF_8.toString()), channel, "next", URLEncoder.encode(this.discord.getApiManager().getNext500Artwork(channel), StandardCharsets.UTF_8.toString())));
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
                        URL url = new URL(MessageFormat.format("https://api.atomicradio.eu/cards?author={0}&title={1}&start_at=00:{2}&end_at=00:{3}&station={4}&playing={5}&image={6}", URLEncoder.encode(this.discord.getApiManager().getArtist(channel), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getTitle(channel), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getDurationTimestampNow(channel), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getDurationTimestamp(channel), StandardCharsets.UTF_8.toString()), channel, "now", URLEncoder.encode(this.discord.getApiManager().get500Artwork(channel), StandardCharsets.UTF_8.toString())));
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
            embed.setDescription("I do not have permission to attach files, please contact an administrator.");
            event.getChannel().sendMessage(embed.build()).queue();
        }
    }

}
