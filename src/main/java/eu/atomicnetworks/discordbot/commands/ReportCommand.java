package eu.atomicnetworks.discordbot.commands;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import eu.atomicnetworks.discordbot.DiscordBot;
import java.awt.Color;
import java.util.HashMap;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 *
 * @author kacpe
 */
public class ReportCommand {

    private final DiscordBot discord;
    private final WebhookClient webhookClient;
    private HashMap<String, Long> memberCooldown;

    public ReportCommand(DiscordBot discord) {
        this.discord = discord;
        this.webhookClient = WebhookClient.withUrl("https://discordapp.com/api/webhooks/736961628075065395/7vkpQW0EhUR4oO_EFaJmWRHxeh0n1_Ov_eqcEd60KxLvOaB5tCl-KOj4CDaZMn6iU3h1");
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
            event.getChannel().sendMessage(embed.build()).queue();
            return;
        }
        
        if(this.memberCooldown.containsKey(event.getMember().getId())) {
            if(this.memberCooldown.get(event.getMember().getId()) > System.currentTimeMillis()) {
                embed.setDescription("You can only report one song every 15 minutes.");
                event.getChannel().sendMessage(embed.build()).queue();
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
                    event.getChannel().sendMessage(embed.build()).queue();
                    return;
                }
                if(args[2].equalsIgnoreCase("now")) {
                    embed.setDescription("**Thank you for reporting this Song at atr.one!**\nPlease note that it may take 24 hours before **"
                        + this.discord.getApiManager().getArtist("one") + " - " + this.discord.getApiManager().getTitle("one") + "** checked and deleted if necessary.");
                    embed.setFooter("You can report Songs only every 15 Minutes.");
                    event.getChannel().sendMessage(embed.build()).queue();
                    this.memberCooldown.put(event.getMember().getId(), System.currentTimeMillis()+900000);
                    this.sendReportToTeam("one", "now", event);
                } else if(args[2].equalsIgnoreCase("last")) {
                    embed.setDescription("**Thank you for reporting this Song at atr.one!**\nPlease note that it may take 24 hours before **"
                        + this.discord.getApiManager().getLastArtist("one") + " - " + this.discord.getApiManager().getLastTitle("one") + "** checked and deleted if necessary.");
                    embed.setFooter("You can report Songs only every 15 Minutes.");
                    event.getChannel().sendMessage(embed.build()).queue();
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
                    event.getChannel().sendMessage(embed.build()).queue();
                    return;
                }
                if(args[2].equalsIgnoreCase("now")) {
                    embed.setDescription("**Thank you for reporting this Song at atr.dance!**\nPlease note that it may take 24 hours before **"
                        + this.discord.getApiManager().getArtist("dance") + " - " + this.discord.getApiManager().getTitle("dance") + "** checked and deleted if necessary.");
                    embed.setFooter("You can report Songs only every 15 Minutes.");
                    event.getChannel().sendMessage(embed.build()).queue();
                    this.memberCooldown.put(event.getMember().getId(), System.currentTimeMillis()+900000);
                    this.sendReportToTeam("dance", "now", event);
                } else if(args[2].equalsIgnoreCase("last")) {
                    embed.setDescription("**Thank you for reporting this Song at atr.dance!**\nPlease note that it may take 24 hours before **"
                        + this.discord.getApiManager().getLastArtist("dance") + " - " + this.discord.getApiManager().getLastTitle("dance") + "** checked and deleted if necessary.");
                    embed.setFooter("You can report Songs only every 15 Minutes.");
                    event.getChannel().sendMessage(embed.build()).queue();
                    this.memberCooldown.put(event.getMember().getId(), System.currentTimeMillis()+900000);
                    this.sendReportToTeam("dance", "last", event);
                }
                break;
            case "trap":
                if(args[2].equalsIgnoreCase("now")) {
                    embed.setDescription("**Thank you for reporting this Song at atr.trap!**\nPlease note that it may take 24 hours before **"
                        + this.discord.getApiManager().getArtist("trap") + " - " + this.discord.getApiManager().getTitle("trap") + "** checked and deleted if necessary.");
                    embed.setFooter("You can report Songs only every 15 Minutes.");
                    event.getChannel().sendMessage(embed.build()).queue();
                    this.memberCooldown.put(event.getMember().getId(), System.currentTimeMillis()+900000);
                    this.sendReportToTeam("trap", "now", event);
                } else if(args[2].equalsIgnoreCase("last")) {
                    embed.setDescription("**Thank you for reporting this Song at atr.trap!**\nPlease note that it may take 24 hours before **"
                        + this.discord.getApiManager().getLastArtist("trap") + " - " + this.discord.getApiManager().getLastTitle("trap") + "** checked and deleted if necessary.");
                    embed.setFooter("You can report Songs only every 15 Minutes.");
                    event.getChannel().sendMessage(embed.build()).queue();
                    this.memberCooldown.put(event.getMember().getId(), System.currentTimeMillis()+900000);
                    this.sendReportToTeam("trap", "last", event);
                }
                break;
        }
    }
    
    private void sendReportToTeam(String channel, String song, GuildMessageReceivedEvent event) {
        WebhookEmbedBuilder webhookEmbedBuilder = new WebhookEmbedBuilder();
        webhookEmbedBuilder.setColor(9785268);
        if(song.equalsIgnoreCase("now")) {
            webhookEmbedBuilder.setAuthor(new WebhookEmbed.EmbedAuthor("atr." + channel + " - " + this.discord.getApiManager().getPlaylist(channel), null, null));
            webhookEmbedBuilder.setDescription("**Lied:** " + this.discord.getApiManager().getTitle(channel) + "\n"
                    + "**Interpret:** " + this.discord.getApiManager().getArtist(channel));
            webhookEmbedBuilder.setFooter(new WebhookEmbed.EmbedFooter(event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl()));
            this.webhookClient.send(webhookEmbedBuilder.build());
        } else if(song.equalsIgnoreCase("last")) {
            webhookEmbedBuilder.setAuthor(new WebhookEmbed.EmbedAuthor("atr." + channel + " - " + this.discord.getApiManager().getLastPlaylist(channel), null, null));
            webhookEmbedBuilder.setDescription("**Lied:** " + this.discord.getApiManager().getLastTitle(channel) + "\n"
                    + "**Interpret:** " + this.discord.getApiManager().getLastArtist(channel));
            webhookEmbedBuilder.setFooter(new WebhookEmbed.EmbedFooter(event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl()));
            this.webhookClient.send(webhookEmbedBuilder.build());
        }
    }

}
