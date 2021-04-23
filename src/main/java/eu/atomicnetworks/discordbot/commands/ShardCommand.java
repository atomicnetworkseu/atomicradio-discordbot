package eu.atomicnetworks.discordbot.commands;

import eu.atomicnetworks.discordbot.DiscordBot;
import java.awt.Color;
import java.text.MessageFormat;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 *
 * @author Kacper Mura
 * 2020 Copyright (c) by atomicradio.eu to present.
 * All rights reserved. https://github.com/VocalZero
 *
 */
public class ShardCommand {
    
    private final DiscordBot discord;

    public ShardCommand(DiscordBot discord) {
        this.discord = discord;
    }
    
    public void execute(GuildMessageReceivedEvent event) {
        Message message = event.getMessage();
        String[] args = message.getContentRaw().split(" ");

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(new Color(149, 79, 180));

        if (event.getAuthor().getId().equals("223891083724193792") || event.getAuthor().getId().equals("425706045453893642") || event.getAuthor().getId().equals("394586910065950723")) {
            embed.setAuthor("Shardsystem", null, "https://cdn.atomicnetworks.eu/discord/icon.png");
            if (args.length == 1) {
                String description = MessageFormat.format("This guild is assigned to the **shard {0}**.\n\n", event.getJDA().getShardInfo().getShardId());
                for (JDA shard : this.discord.getShardManager().getShards()) {
                    int memberCount = 0;
                    memberCount = shard.getGuilds().stream().map(guild -> guild.getMemberCount()).reduce(memberCount, Integer::sum);
                    description += "**👾 Shard " + shard.getShardInfo().getShardId() + "**\n" + MessageFormat.format("In total, over **{0} users** are served here on **{1} guilds**.\nCurrently the bot is **{2}** and has a ping of **{3}ms** to the gateway.\n\n", memberCount, shard.getGuilds().size(), shard.getStatus().name(), shard.getGatewayPing());
                }
                embed.setDescription(description);
                this.discord.getBackendManager().sendMessage(event, embed.build());
                return;
            }

            if (args.length == 2) {
                return;
            }
            
            try {
                int id = Integer.valueOf(args[2]);

                switch (args[1].toLowerCase()) {
                    case "stop":
                        embed.setDescription("**Successful**, the shard " + id + " was stopped.");
                        this.discord.getBackendManager().sendMessage(event, embed.build());
                        this.discord.getShardManager().shutdown(id);
                        break;
                    case "restart":
                        embed.setDescription("**Successful**, the shard " + id + " was restarted.");
                        this.discord.getBackendManager().sendMessage(event, embed.build());
                        this.discord.getShardManager().restart(id);
                        break;
                    case "start":
                        embed.setDescription("**Successful**, the shard " + id + " was started.");
                        this.discord.getBackendManager().sendMessage(event, embed.build());
                        this.discord.getShardManager().start(id);
                        break;
                }
            } catch(NumberFormatException ex) {
            
            } catch(IllegalArgumentException ex) {
                embed.setDescription(ex.getMessage());
                this.discord.getBackendManager().sendMessage(event, embed.build());
            }
        } else {
            embed.setTitle("Insufficient Rights");
            embed.setDescription("You do not have enough rights to execute this command,\nif you think this is a bug please contact a team member.");
            this.discord.getBackendManager().sendMessage(event, embed.build());
        }

    }
    
}
