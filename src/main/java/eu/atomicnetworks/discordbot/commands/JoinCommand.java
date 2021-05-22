package eu.atomicnetworks.discordbot.commands;

import eu.atomicnetworks.discordbot.DiscordBot;
import eu.atomicnetworks.discordbot.enums.StationChannnel;
import eu.atomicnetworks.discordbot.handler.AudioHandler;
import java.awt.Color;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

/**
 *
 * @author Kacper Mura
 * 2020 Copyright (c) by atomicradio.eu to present.
 * All rights reserved. https://github.com/VocalZero
 *
 */
public class JoinCommand {
    
    private final DiscordBot discord;

    public JoinCommand(DiscordBot discord) {
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
            this.discord.getBackendManager().sendMessage(event, embed.build());
            return;
        }
        
        if(this.discord.getBackendManager().getChannelId(event.getGuild()).isEmpty()) {
            if(!event.getMember().getVoiceState().inVoiceChannel()) {
                embed.setDescription("You must join a channel in which the bot can also join to create a link.");
                this.discord.getBackendManager().sendMessage(event, embed.build());
                return;
            }
            
            this.discord.getBackendManager().setChannelId(event.getGuild(), event.getMember().getVoiceState().getChannel().getId());
        }
        
        if(event.getMember().getVoiceState().inVoiceChannel()) {
            if(!this.discord.getBackendManager().getChannelId(event.getGuild()).equals(event.getMember().getVoiceState().getChannel().getId())) {
                this.discord.getBackendManager().setChannelId(event.getGuild(), event.getMember().getVoiceState().getChannel().getId());
            }
        }
        
        if(event.getGuild().getAudioManager().isConnected()) {
            embed.setDescription("The bot is already in a channel, but you can disconnect it with **" + this.discord.getBackendManager().getPrefix(event.getGuild()) + "leave**.");
            this.discord.getBackendManager().sendMessage(event, embed.build());
            return;
        }
        
        VoiceChannel voiceChannel = event.getChannel().getJDA().getVoiceChannelById(this.discord.getBackendManager().getChannelId(event.getGuild()));
        if(voiceChannel == null) {
            embed.setDescription("The channel has been deleted or the bot no longer has permission to enter it.");
            this.discord.getBackendManager().sendMessage(event, embed.build());
            return;
        }
        
        if(event.getGuild().getAudioManager().getSendingHandler() != null) {
            AudioHandler audioHandler = (AudioHandler) event.getGuild().getAudioManager().getSendingHandler();
            audioHandler.stop();
        }
        
        try {
            event.getGuild().getAudioManager().openAudioConnection(voiceChannel);
        } catch (InsufficientPermissionException ex) {
            embed.setDescription("I do not have permission to **join** the voice channel, please contact an administrator.");
            this.discord.getBackendManager().sendMessage(event, embed.build());
            return;
        }
        
        try {
            StationChannnel stationChannnel = StationChannnel.valueOf(this.discord.getBackendManager().getMusic(event.getGuild()));
            this.discord.getBackendManager().startStream(event.getGuild(), stationChannnel.getUrl());
            this.discord.getBackendManager().setPlaying(event.getGuild(), true);
            this.discord.getBackendManager().setMusic(event.getGuild(), stationChannnel.getName());
            this.discord.getBackendManager().setChannelId(event.getGuild(), voiceChannel.getId());
        } catch(IllegalArgumentException ex) {
            this.discord.getBackendManager().startStream(event.getGuild(), StationChannnel.ONE.getUrl());
            this.discord.getBackendManager().setPlaying(event.getGuild(), true);
            this.discord.getBackendManager().setMusic(event.getGuild(), StationChannnel.ONE.getName());
            this.discord.getBackendManager().setChannelId(event.getGuild(), voiceChannel.getId());
        }
        
        embed.setDescription("The bot has successfully joined the channel,\nthe last played stream will be loaded which you can change with **" + this.discord.getBackendManager().getPrefix(event.getGuild()) + "play**.");
        this.discord.getBackendManager().sendMessage(event, embed.build());
    } 
    
}
