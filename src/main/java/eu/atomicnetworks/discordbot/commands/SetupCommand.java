package eu.atomicnetworks.discordbot.commands;

import eu.atomicnetworks.discordbot.DiscordBot;
import java.awt.Color;
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
public class SetupCommand {
    
    private final DiscordBot discord;

    public SetupCommand(DiscordBot discord) {
        this.discord = discord;
    }
    
    public void execute(GuildMessageReceivedEvent event) {
        Message message = event.getMessage();
        String[] args = message.getContentRaw().split(" ");
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(new Color(149, 79, 180));
        
        if(!this.discord.getBackendManager().checkForPermissions(event.getMember())) {
            embed.setTitle("Insufficient Rights");
            embed.setDescription("Only members with the **administrator**-right can execute this command.\n\nYou do not have enough rights to execute this command,\nif you think this is a bug please contact a team member.");
            event.getChannel().sendMessage(embed.build()).queue();
            return;
        }
        
        if(!event.getMember().getVoiceState().inVoiceChannel()) {
            embed.setDescription("You must join a channel in which the bot can also join to create a link.");
            event.getChannel().sendMessage(embed.build()).queue();
            return;
        }
        
        if(this.discord.getBackendManager().getChannelId(event.getGuild()).equals(event.getMember().getVoiceState().getChannel().getId())) {
            embed.setDescription("The channel is already the standard channel.");
            event.getChannel().sendMessage(embed.build()).queue();
            return;
        }
        
        this.discord.getBackendManager().setChannelId(event.getGuild(), event.getMember().getVoiceState().getChannel().getId());
        embed.setDescription("From now on the bot will automatically connect to this voice channel **" + event.getMember().getVoiceState().getChannel().getName() + "** when a member writes **" + this.discord.getBackendManager().getPrefix(event.getGuild()) + "join** as well when our Discordbots have to be restarted due to an update or a failure.");
        event.getChannel().sendMessage(embed.build()).queue();
    }
    
}
