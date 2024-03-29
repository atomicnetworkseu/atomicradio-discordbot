package eu.atomicnetworks.discordbot.commands;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import eu.atomicnetworks.discordbot.DiscordBot;
import eu.atomicradio.objects.Channel;
import eu.atomicradio.objects.Channels;
import java.awt.Color;
import java.util.HashMap;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 *
 * @author Kacper Mura
 * 2020 Copyright (c) by atomicradio.eu to present.
 * All rights reserved. https://github.com/VocalZero
 *
 */
public class ReportCommand {

    private final DiscordBot discord;
    private final WebhookClient webhookClient;
    private HashMap<String, Long> memberCooldown;

    public ReportCommand(DiscordBot discord) {
        this.discord = discord;
        this.webhookClient = WebhookClient.withUrl("https://discord.com/api/webhooks/778719403134418984/Vjg5mCIQ2ddODEhpy-WhTWvLvDT-vX7DJZvLLdJ8u_MK2Mmume5_3mZjdcJkmnCXf2nL");
        this.memberCooldown = new HashMap<>();
    }

    public void execute(GuildMessageReceivedEvent event) {
        Message message = event.getMessage();
        String[] args = message.getContentRaw().split(" ");
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(new Color(149, 79, 180));

        if (args.length == 1 || args.length != 3) {
            embed.setDescription(":musical_score: l **Reportsystem**\n\nHere you can report songs you don't like and help us to adapt our playlists to your taste.\n\nWith the command **"
                    + this.discord.getBackendManager().getPrefix(event.getGuild()) + "report "
                    + this.discord.getBackendManager().getMusic(event.getGuild()).toLowerCase() + " now** you can report the currently playing song, \nthe last played song with **"
                    + this.discord.getBackendManager().getPrefix(event.getGuild()) + "report "
                    + this.discord.getBackendManager().getMusic(event.getGuild()).toLowerCase() + " last**.");
            this.discord.getBackendManager().sendMessage(event, embed.build());
            return;
        }
        
        try {
            Channel channel = this.discord.getAtomicClient().getChannel(Channels.valueOf(args[1].toUpperCase()));
            
            if (this.memberCooldown.containsKey(event.getMember().getId())) {
                if (this.memberCooldown.get(event.getMember().getId()) > System.currentTimeMillis()) {
                    embed.setDescription("You can only report one song every 15 minutes.");
                    this.discord.getBackendManager().sendMessage(event, embed.build());
                    return;
                } else {
                    this.memberCooldown.remove(event.getMember().getId());
                }
            }
            
            if (args[2].equalsIgnoreCase("now")) {
                embed.setDescription("**Thank you for reporting this Song at " + channel.getName() + "!**\nPlease note that it may take 24 hours before **"
                    + channel.getSong().getArtist() + " - " + channel.getSong().getTitle() + "** checked and deleted if necessary.");
                embed.setFooter("You can report Songs only every 15 Minutes.");
                this.discord.getBackendManager().sendMessage(event, embed.build());
                this.memberCooldown.put(event.getMember().getId(), System.currentTimeMillis() + 900000);
                this.sendReportToTeam(channel, "now", event);
            } else if (args[2].equalsIgnoreCase("last")) {
                embed.setDescription("**Thank you for reporting this Song at " + channel.getName() + "!**\nPlease note that it may take 24 hours before **"
                    + channel.getHistory().stream().findFirst().orElse(null).getArtist() + " - " + channel.getHistory().stream().findFirst().orElse(null).getTitle() + "** checked and deleted if necessary.");
                embed.setFooter("You can report Songs only every 15 Minutes.");
                this.discord.getBackendManager().sendMessage(event, embed.build());
                this.memberCooldown.put(event.getMember().getId(), System.currentTimeMillis() + 900000);
                this.sendReportToTeam(channel, "last", event);
            }
        } catch(IllegalArgumentException ex) {
            embed.setDescription("The specified station was not found!");
            this.discord.getBackendManager().sendMessage(event, embed.build());
        }
    }
    
    private void sendReportToTeam(Channel channel, String song, GuildMessageReceivedEvent event) {
        WebhookEmbedBuilder webhookEmbedBuilder = new WebhookEmbedBuilder();
        webhookEmbedBuilder.setColor(9785268);
        
        if(song.equalsIgnoreCase("now")) {
            webhookEmbedBuilder.setAuthor(new WebhookEmbed.EmbedAuthor(channel.getName() + " - " + channel.getSong().getPlaylist().toLowerCase(), null, null));
            webhookEmbedBuilder.setDescription("**Lied:** " + channel.getSong().getTitle() + "\n"
                    + "**Interpret:** " + channel.getSong().getArtist());
            webhookEmbedBuilder.setFooter(new WebhookEmbed.EmbedFooter(event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl()));
            this.webhookClient.send(webhookEmbedBuilder.build());
        } else if(song.equalsIgnoreCase("last")) {
            webhookEmbedBuilder.setAuthor(new WebhookEmbed.EmbedAuthor(channel.getName() + " - " + channel.getHistory().stream().findFirst().orElse(null).getPlaylist().toLowerCase(), null, null));
            webhookEmbedBuilder.setDescription("**Lied:** " + channel.getHistory().stream().findFirst().orElse(null).getTitle() + "\n"
                    + "**Interpret:** " + channel.getHistory().stream().findFirst().orElse(null).getArtist());
            webhookEmbedBuilder.setFooter(new WebhookEmbed.EmbedFooter(event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl()));
            this.webhookClient.send(webhookEmbedBuilder.build());
        }
    }

}
