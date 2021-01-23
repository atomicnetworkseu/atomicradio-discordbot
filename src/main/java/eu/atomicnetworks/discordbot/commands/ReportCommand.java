package eu.atomicnetworks.discordbot.commands;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import eu.atomicnetworks.discordbot.DiscordBot;
import eu.atomicradio.objects.Channel;
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

        if (args.length == 1) {
            embed.setDescription(":musical_score: l **Reportsystem**\n\nHere you can report songs you don't like and help us to adapt our playlists to your taste.\n\nWith the command **"
                    + this.discord.getBackendManager().getPrefix(event.getGuild()) + "report "
                    + this.discord.getBackendManager().getMusic(event.getGuild()) + " now** you can report the currently playing song, \nthe last played song with **"
                    + this.discord.getBackendManager().getPrefix(event.getGuild()) + "report "
                    + this.discord.getBackendManager().getMusic(event.getGuild()) + " last**.");
            this.discord.getBackendManager().sendMessage(event, embed.build());
            return;
        }
        
        if(this.memberCooldown.containsKey(event.getMember().getId())) {
            if(this.memberCooldown.get(event.getMember().getId()) > System.currentTimeMillis()) {
                embed.setDescription("You can only report one song every 15 minutes.");
                this.discord.getBackendManager().sendMessage(event, embed.build());
                return;
            } else {
                this.memberCooldown.remove(event.getMember().getId());
            }
        }
        
        switch(args[1].toLowerCase()) {
            case "one":
                if(args.length != 3) {
                    embed.setDescription(":musical_score: l **Reportsystem**\n\nHere you can report songs you don't like and help us to adapt our playlists to your taste.\n\nWith the command **"
                        + this.discord.getBackendManager().getPrefix(event.getGuild()) + "report "
                        + this.discord.getBackendManager().getMusic(event.getGuild()) + " now** you can report the currently playing song, \nthe last played song with **"
                        + this.discord.getBackendManager().getPrefix(event.getGuild()) + "report "
                        + this.discord.getBackendManager().getMusic(event.getGuild()) + " last**.");
                    this.discord.getBackendManager().sendMessage(event, embed.build());
                    return;
                }
                if(args[2].equalsIgnoreCase("now")) {
                    embed.setDescription("**Thank you for reporting this Song at atr.one!**\nPlease note that it may take 24 hours before **"
                        + this.discord.getAtomicClient().getChannelOne().getSong().getArtist() + " - " + this.discord.getAtomicClient().getChannelOne().getSong().getTitle() + "** checked and deleted if necessary.");
                    embed.setFooter("You can report Songs only every 15 Minutes.");
                    this.discord.getBackendManager().sendMessage(event, embed.build());
                    this.memberCooldown.put(event.getMember().getId(), System.currentTimeMillis()+900000);
                    this.sendReportToTeam("one", "now", event);
                } else if(args[2].equalsIgnoreCase("last")) {
                    embed.setDescription("**Thank you for reporting this Song at atr.one!**\nPlease note that it may take 24 hours before **"
                        + this.discord.getAtomicClient().getChannelOne().getHistory().stream().findFirst().orElse(null).getArtist() + " - " + this.discord.getAtomicClient().getChannelOne().getHistory().stream().findFirst().orElse(null).getTitle() + "** checked and deleted if necessary.");
                    embed.setFooter("You can report Songs only every 15 Minutes.");
                    this.discord.getBackendManager().sendMessage(event, embed.build());
                    this.memberCooldown.put(event.getMember().getId(), System.currentTimeMillis()+900000);
                    this.sendReportToTeam("one", "last", event);
                }
                break;
            case "dance":
                if(args.length != 3) {
                    embed.setDescription(":musical_score: l **Reportsystem**\n\nHere you can report songs you don't like and help us to adapt our playlists to your taste.\n\nWith the command **"
                        + this.discord.getBackendManager().getPrefix(event.getGuild()) + "report "
                        + this.discord.getBackendManager().getMusic(event.getGuild()) + " now** you can report the currently playing song, \nthe last played song with **"
                        + this.discord.getBackendManager().getPrefix(event.getGuild()) + "report "
                        + this.discord.getBackendManager().getMusic(event.getGuild()) + " last**.");
                    this.discord.getBackendManager().sendMessage(event, embed.build());
                    return;
                }
                if(args[2].equalsIgnoreCase("now")) {
                    embed.setDescription("**Thank you for reporting this Song at atr.dance!**\nPlease note that it may take 24 hours before **"
                        + this.discord.getAtomicClient().getChannelDance().getSong().getArtist() + " - " + this.discord.getAtomicClient().getChannelDance().getSong().getTitle() + "** checked and deleted if necessary.");
                    embed.setFooter("You can report Songs only every 15 Minutes.");
                    this.discord.getBackendManager().sendMessage(event, embed.build());
                    this.memberCooldown.put(event.getMember().getId(), System.currentTimeMillis()+900000);
                    this.sendReportToTeam("dance", "now", event);
                } else if(args[2].equalsIgnoreCase("last")) {
                    embed.setDescription("**Thank you for reporting this Song at atr.dance!**\nPlease note that it may take 24 hours before **"
                        + this.discord.getAtomicClient().getChannelDance().getHistory().stream().findFirst().orElse(null).getArtist() + " - " + this.discord.getAtomicClient().getChannelDance().getHistory().stream().findFirst().orElse(null).getTitle() + "** checked and deleted if necessary.");
                    embed.setFooter("You can report Songs only every 15 Minutes.");
                   this.discord.getBackendManager().sendMessage(event, embed.build());
                    this.memberCooldown.put(event.getMember().getId(), System.currentTimeMillis()+900000);
                    this.sendReportToTeam("dance", "last", event);
                }
                break;
            case "trap":
                if(args[2].equalsIgnoreCase("now")) {
                    embed.setDescription("**Thank you for reporting this Song at atr.trap!**\nPlease note that it may take 24 hours before **"
                        + this.discord.getAtomicClient().getChannelTrap().getSong().getArtist() + " - " + this.discord.getAtomicClient().getChannelTrap().getSong().getTitle() + "** checked and deleted if necessary.");
                    embed.setFooter("You can report Songs only every 15 Minutes.");
                    this.discord.getBackendManager().sendMessage(event, embed.build());
                    this.memberCooldown.put(event.getMember().getId(), System.currentTimeMillis()+900000);
                    this.sendReportToTeam("trap", "now", event);
                } else if(args[2].equalsIgnoreCase("last")) {
                    embed.setDescription("**Thank you for reporting this Song at atr.trap!**\nPlease note that it may take 24 hours before **"
                        + this.discord.getAtomicClient().getChannelTrap().getHistory().stream().findFirst().orElse(null).getArtist() + " - " + this.discord.getAtomicClient().getChannelTrap().getHistory().stream().findFirst().orElse(null).getTitle() + "** checked and deleted if necessary.");
                    embed.setFooter("You can report Songs only every 15 Minutes.");
                    this.discord.getBackendManager().sendMessage(event, embed.build());
                    this.memberCooldown.put(event.getMember().getId(), System.currentTimeMillis()+900000);
                    this.sendReportToTeam("trap", "last", event);
                }
                break;
        }
    }
    
    private void sendReportToTeam(String channel, String song, GuildMessageReceivedEvent event) {
        WebhookEmbedBuilder webhookEmbedBuilder = new WebhookEmbedBuilder();
        webhookEmbedBuilder.setColor(9785268);
        
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
        
        if(song.equalsIgnoreCase("now")) {
            webhookEmbedBuilder.setAuthor(new WebhookEmbed.EmbedAuthor("atr." + channel + " - " + targetChannel.getSong().getPlaylist().toLowerCase(), null, null));
            webhookEmbedBuilder.setDescription("**Lied:** " + targetChannel.getSong().getTitle() + "\n"
                    + "**Interpret:** " + targetChannel.getSong().getArtist());
            webhookEmbedBuilder.setFooter(new WebhookEmbed.EmbedFooter(event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl()));
            this.webhookClient.send(webhookEmbedBuilder.build());
        } else if(song.equalsIgnoreCase("last")) {
            webhookEmbedBuilder.setAuthor(new WebhookEmbed.EmbedAuthor("atr." + channel + " - " + targetChannel.getHistory().stream().findFirst().orElse(null).getPlaylist().toLowerCase(), null, null));
            webhookEmbedBuilder.setDescription("**Lied:** " + targetChannel.getHistory().stream().findFirst().orElse(null).getTitle() + "\n"
                    + "**Interpret:** " + targetChannel.getHistory().stream().findFirst().orElse(null).getArtist());
            webhookEmbedBuilder.setFooter(new WebhookEmbed.EmbedFooter(event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl()));
            this.webhookClient.send(webhookEmbedBuilder.build());
        }
    }

}
