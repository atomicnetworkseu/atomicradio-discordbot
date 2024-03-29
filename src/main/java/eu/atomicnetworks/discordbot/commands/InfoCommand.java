package eu.atomicnetworks.discordbot.commands;

import eu.atomicnetworks.discordbot.DiscordBot;
import eu.atomicnetworks.discordbot.enums.StationChannnel;
import java.awt.Color;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 *
 * @author Kacper Mura
 * 2020 Copyright (c) by atomicradio.eu to present.
 * All rights reserved. https://github.com/VocalZero
 *
 */
public class InfoCommand {
    
    private final DiscordBot discord;

    public InfoCommand(DiscordBot discord) {
        this.discord = discord;
    }
    
    public void execute(GuildMessageReceivedEvent event) {
        Message message = event.getMessage();
        String[] args = message.getContentRaw().split(" ");
        
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(new Color(149, 79, 180));
        embed.setAuthor("Helpdesk » Information", null, "https://cdn.atomicnetworks.eu/discord/icon.png");
        embed.setDescription("** **\nFirst of all, thank you for using our bot and supporting our radio station,\nwe are constantly working to improve the experience with the Musicbot and are happy that there are people who like what we do.\n\n:heart: Thanks\n\n** **");
        embed.addField("Invite & Support", "[Invitelink](https://atomicradio.eu/bot)\n[Supportdiscord](https://discord.com/invite/5JVZr6c)\n\n** **", true);
        embed.addField("Social Media", "[Twitter](https://go.atomicnetworks.eu/radio/twitter)\n[Website](https://go.atomicnetworks.eu/radio)\n\n** **", true);
        embed.addField("Newsfeed", "We have published our new [website](https://atomicradio.eu)!\n\n** **", true);
        embed.addField("Legal & Datasecurity", "We comply with all\nGerman laws and have the\nnecessary [licenses](https://go.atomicnetworks.eu/radio/license), we\nonly store the selected\nstandard channels.\n\n** **", true);
        embed.addField("Station", MessageFormat.format("{0}\n** **", this.getChannel(event.getGuild())), true);
        embed.addField("Statistics", MessageFormat.format("Guilds: {0}\n"
                + "Members: {1}\n"
                + "Playing: {2}\n"
                + "\n** **", this.discord.getBackendManager().getGuildCount(), this.discord.getBackendManager().getUserCount(), String.valueOf(this.discord.getBackendManager().getConnectionCount())), true);
        embed.addField("Connection", MessageFormat.format("Uptime: {0}\n"
                + "Ping: {1}ms\n\n** **", getOnlineTime(), getPing()), true);
        this.discord.getBackendManager().sendMessage(event, embed.build());
    } 
    
    private String getChannel(Guild guild) {
        if(this.discord.getBackendManager().getMusic(guild).isEmpty()) {
            return StationChannnel.ONE.getUrl();
        }
        try {
            StationChannnel stationChannnel = StationChannnel.valueOf(this.discord.getBackendManager().getMusic(guild));
            return stationChannnel.getUrl();
        } catch(IllegalArgumentException ex) {
            return StationChannnel.ONE.getUrl();
        }
    }
    
    private long getPing() {
        try {
            InetAddress host = InetAddress.getByName("discord.com");
            long nanoTime = System.nanoTime();
            Socket socket = new Socket(host, 80);
            socket.close();
            return (System.nanoTime()-nanoTime) / 1000000;
        } catch (IOException ex) {
            Logger.getLogger(InfoCommand.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
    
    private String getOnlineTime() {
        long time = System.currentTimeMillis()-this.discord.getStartTimeMillis();

        long seconds = (time / 1000L) % 60;
        long minutes = (time / 60000L % 60L);
        long hours = (time / 3600000L) % 24;
        long days = (time / 86400000L);
        
        String onlineTime = "";
        if(days > 0) {
            onlineTime += days + " days, ";
        }
        if(hours > 0) {
            onlineTime += hours + " hours, ";
        }
        if(minutes > 0) {
            onlineTime += minutes + " minutes";
        }
        if(seconds > 0 && days == 0) {
            onlineTime += ", " + seconds + " seconds";
        }
        
        return onlineTime;
    }
    
}
