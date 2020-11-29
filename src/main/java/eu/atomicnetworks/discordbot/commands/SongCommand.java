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
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
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

        InputStream inputStream = null;
        if (args.length == 1) {
            String channel = this.discord.getBackendManager().getMusic(event.getGuild());
            if(channel.isEmpty()) {
                channel = "one";
            }
            try {
                URL url = new URL(MessageFormat.format("http://10.10.10.105:9000/cards?author={0}&title={1}&start_at=00:{2}&end_at=00:{3}&station={4}&playing={5}&image={6}", URLEncoder.encode(this.discord.getApiManager().getArtist(channel), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getTitle(channel), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getDurationTimestampNow(channel), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getDurationTimestamp(channel), StandardCharsets.UTF_8.toString()), channel, "now", URLEncoder.encode(this.discord.getApiManager().get500Artwork(channel).replace("https://api.atomicradio.eu", "http://10.10.10.105:9000"), StandardCharsets.UTF_8.toString())));
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
            case "one":
                if (args.length == 2) {
                    try {
                        URL url = new URL(MessageFormat.format("http://10.10.10.105:9000/cards?author={0}&title={1}&start_at=00:{2}&end_at=00:{3}&station={4}&playing={5}&image={6}", URLEncoder.encode(this.discord.getApiManager().getArtist("one"), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getTitle("one"), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getDurationTimestampNow("one"), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getDurationTimestamp("one"), StandardCharsets.UTF_8.toString()), "one", "now", URLEncoder.encode(this.discord.getApiManager().get500Artwork("one").replace("https://api.atomicradio.eu", "http://10.10.10.105:9000"), StandardCharsets.UTF_8.toString())));
                        inputStream = url.openStream();
                        event.getChannel().sendFile(inputStream, "song.png", new AttachmentOption[0]).queue();
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(SongCommand.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(SongCommand.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return;
                }

                if (args[2].equalsIgnoreCase("now")) {
                    try {
                        URL url = new URL(MessageFormat.format("http://10.10.10.105:9000/cards?author={0}&title={1}&start_at=00:{2}&end_at=00:{3}&station={4}&playing={5}&image={6}", URLEncoder.encode(this.discord.getApiManager().getArtist("one"), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getTitle("one"), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getDurationTimestampNow("one"), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getDurationTimestamp("one"), StandardCharsets.UTF_8.toString()), "one", "now", URLEncoder.encode(this.discord.getApiManager().get500Artwork("one").replace("https://api.atomicradio.eu", "http://10.10.10.105:9000"), StandardCharsets.UTF_8.toString())));
                        inputStream = url.openStream();
                        event.getChannel().sendFile(inputStream, "song.png", new AttachmentOption[0]).queue();
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(SongCommand.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(SongCommand.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (args[2].equalsIgnoreCase("last")) {
                    try {
                        URL url = new URL(MessageFormat.format("http://10.10.10.105:9000/cards?author={0}&title={1}&start_at=00:{2}&end_at=00:{3}&station={4}&playing={5}&image={6}", URLEncoder.encode(this.discord.getApiManager().getLastArtist("one"), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getLastTitle("one"), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getLastDurationTimestampNow("one"), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getLastDurationTimestamp("one"), StandardCharsets.UTF_8.toString()), "one", "last", URLEncoder.encode(this.discord.getApiManager().getLast500Artwork("one").replace("https://api.atomicradio.eu", "http://10.10.10.105:9000"), StandardCharsets.UTF_8.toString())));
                        inputStream = url.openStream();
                        event.getChannel().sendFile(inputStream, "song.png", new AttachmentOption[0]).queue();
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(SongCommand.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(SongCommand.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (args[2].equalsIgnoreCase("next")) {
                    try {
                        URL url = new URL(MessageFormat.format("http://10.10.10.105:9000/cards?author={0}&title={1}&start_at=00:{2}&end_at=00:{3}&station={4}&playing={5}&image={6}", URLEncoder.encode(this.discord.getApiManager().getNextArtist("one"), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getNextTitle("one"), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getNextDurationTimestampNow("one"), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getNextDurationTimestamp("one"), StandardCharsets.UTF_8.toString()), "one", "next", URLEncoder.encode(this.discord.getApiManager().getNext500Artwork("one").replace("https://api.atomicradio.eu", "http://10.10.10.105:9000"), StandardCharsets.UTF_8.toString())));
                        inputStream = url.openStream();
                        event.getChannel().sendFile(inputStream, "song.png", new AttachmentOption[0]).queue();
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(SongCommand.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(SongCommand.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            case "dance":
                if (args.length == 2) {
                    try {
                        URL url = new URL(MessageFormat.format("http://10.10.10.105:9000/cards?author={0}&title={1}&start_at=00:{2}&end_at=00:{3}&station={4}&playing={5}&image={6}", URLEncoder.encode(this.discord.getApiManager().getArtist("dance"), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getTitle("dance"), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getDurationTimestampNow("dance"), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getDurationTimestamp("dance"), StandardCharsets.UTF_8.toString()), "dance", "now", URLEncoder.encode(this.discord.getApiManager().get500Artwork("dance").replace("https://api.atomicradio.eu", "http://10.10.10.105:9000"), StandardCharsets.UTF_8.toString())));
                        inputStream = url.openStream();
                        event.getChannel().sendFile(inputStream, "song.png", new AttachmentOption[0]).queue();
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(SongCommand.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(SongCommand.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return;
                }

                if (args[2].equalsIgnoreCase("now")) {
                    try {
                        URL url = new URL(MessageFormat.format("http://10.10.10.105:9000/cards?author={0}&title={1}&start_at=00:{2}&end_at=00:{3}&station={4}&playing={5}&image={6}", URLEncoder.encode(this.discord.getApiManager().getArtist("dance"), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getTitle("dance"), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getDurationTimestampNow("dance"), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getDurationTimestamp("dance"), StandardCharsets.UTF_8.toString()), "dance", "now", URLEncoder.encode(this.discord.getApiManager().get500Artwork("dance").replace("https://api.atomicradio.eu", "http://10.10.10.105:9000"), StandardCharsets.UTF_8.toString())));
                        inputStream = url.openStream();
                        event.getChannel().sendFile(inputStream, "song.png", new AttachmentOption[0]).queue();
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(SongCommand.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(SongCommand.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (args[2].equalsIgnoreCase("last")) {
                    try {
                        URL url = new URL(MessageFormat.format("http://10.10.10.105:9000/cards?author={0}&title={1}&start_at=00:{2}&end_at=00:{3}&station={4}&playing={5}&image={6}", URLEncoder.encode(this.discord.getApiManager().getLastArtist("dance"), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getLastTitle("dance"), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getLastDurationTimestampNow("dance"), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getLastDurationTimestamp("dance"), StandardCharsets.UTF_8.toString()), "dance", "last", URLEncoder.encode(this.discord.getApiManager().getLast500Artwork("dance").replace("https://api.atomicradio.eu", "http://10.10.10.105:9000"), StandardCharsets.UTF_8.toString())));
                        inputStream = url.openStream();
                        event.getChannel().sendFile(inputStream, "song.png", new AttachmentOption[0]).queue();
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(SongCommand.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(SongCommand.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (args[2].equalsIgnoreCase("next")) {
                    try {
                        URL url = new URL(MessageFormat.format("http://10.10.10.105:9000/cards?author={0}&title={1}&start_at=00:{2}&end_at=00:{3}&station={4}&playing={5}&image={6}", URLEncoder.encode(this.discord.getApiManager().getNextArtist("dance"), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getNextTitle("dance"), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getNextDurationTimestampNow("dance"), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getNextDurationTimestamp("dance"), StandardCharsets.UTF_8.toString()), "dance", "next", URLEncoder.encode(this.discord.getApiManager().getNext500Artwork("dance").replace("https://api.atomicradio.eu", "http://10.10.10.105:9000"), StandardCharsets.UTF_8.toString())));
                        inputStream = url.openStream();
                        event.getChannel().sendFile(inputStream, "song.png", new AttachmentOption[0]).queue();
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(SongCommand.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(SongCommand.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            case "trap":
                if (args.length == 2) {
                    try {
                        URL url = new URL(MessageFormat.format("http://10.10.10.105:9000/cards?author={0}&title={1}&start_at=00:{2}&end_at=00:{3}&station={4}&playing={5}&image={6}", URLEncoder.encode(this.discord.getApiManager().getArtist("trap"), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getTitle("trap"), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getDurationTimestampNow("trap"), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getDurationTimestamp("trap"), StandardCharsets.UTF_8.toString()), "trap", "now", URLEncoder.encode(this.discord.getApiManager().get500Artwork("trap").replace("https://api.atomicradio.eu", "http://10.10.10.105:9000"), StandardCharsets.UTF_8.toString())));
                        inputStream = url.openStream();
                        event.getChannel().sendFile(inputStream, "song.png", new AttachmentOption[0]).queue();
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(SongCommand.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(SongCommand.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return;
                }

                if (args[2].equalsIgnoreCase("now")) {
                    try {
                        URL url = new URL(MessageFormat.format("http://10.10.10.105:9000/cards?author={0}&title={1}&start_at=00:{2}&end_at=00:{3}&station={4}&playing={5}&image={6}", URLEncoder.encode(this.discord.getApiManager().getArtist("trap"), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getTitle("trap"), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getDurationTimestampNow("trap"), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getDurationTimestamp("trap"), StandardCharsets.UTF_8.toString()), "trap", "now", URLEncoder.encode(this.discord.getApiManager().get500Artwork("trap").replace("https://api.atomicradio.eu", "http://10.10.10.105:9000"), StandardCharsets.UTF_8.toString())));
                        inputStream = url.openStream();
                        event.getChannel().sendFile(inputStream, "song.png", new AttachmentOption[0]).queue();
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(SongCommand.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(SongCommand.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (args[2].equalsIgnoreCase("last")) {
                    try {
                        URL url = new URL(MessageFormat.format("http://10.10.10.105:9000/cards?author={0}&title={1}&start_at=00:{2}&end_at=00:{3}&station={4}&playing={5}&image={6}", URLEncoder.encode(this.discord.getApiManager().getLastArtist("trap"), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getLastTitle("trap"), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getLastDurationTimestampNow("trap"), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getLastDurationTimestamp("trap"), StandardCharsets.UTF_8.toString()), "trap", "last", URLEncoder.encode(this.discord.getApiManager().getLast500Artwork("trap").replace("https://api.atomicradio.eu", "http://10.10.10.105:9000"), StandardCharsets.UTF_8.toString())));
                        inputStream = url.openStream();
                        event.getChannel().sendFile(inputStream, "song.png", new AttachmentOption[0]).queue();
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(SongCommand.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(SongCommand.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (args[2].equalsIgnoreCase("next")) {
                    try {
                        URL url = new URL(MessageFormat.format("http://10.10.10.105:9000/cards?author={0}&title={1}&start_at=00:{2}&end_at=00:{3}&station={4}&playing={5}&image={6}", URLEncoder.encode(this.discord.getApiManager().getNextArtist("trap"), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getNextTitle("trap"), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getNextDurationTimestampNow("trap"), StandardCharsets.UTF_8.toString()), URLEncoder.encode(this.discord.getApiManager().getNextDurationTimestamp("trap"), StandardCharsets.UTF_8.toString()), "trap", "next", URLEncoder.encode(this.discord.getApiManager().getNext500Artwork("trap").replace("https://api.atomicradio.eu", "http://10.10.10.105:9000"), StandardCharsets.UTF_8.toString())));
                        inputStream = url.openStream();
                        event.getChannel().sendFile(inputStream, "song.png", new AttachmentOption[0]).queue();
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(SongCommand.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(SongCommand.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
        }
    }

}
