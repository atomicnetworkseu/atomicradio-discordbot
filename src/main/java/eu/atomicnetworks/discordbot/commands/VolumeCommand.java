package eu.atomicnetworks.discordbot.commands;

import eu.atomicnetworks.discordbot.DiscordBot;
import eu.atomicnetworks.discordbot.handler.AudioHandler;
import java.awt.Color;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 *
 * @author Kacper Mura
 * 2020 Copyright (c) by atomicradio.eu to present.
 * All rights reserved. https://github.com/VocalZero
 *
 */
public class VolumeCommand {
    
    private final DiscordBot discord;

    public VolumeCommand(DiscordBot discord) {
        this.discord = discord;
    }
    
    public void execute(GuildMessageReceivedEvent event) {
        Message message = event.getMessage();
        String[] args = message.getContentRaw().split(" ");
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(new Color(149, 79, 180));
        
        if(this.discord.getBackendManager().isMusicCommandsDenied(event.getGuild())) {
            if(!this.discord.getBackendManager().checkForPermissions(event.getMember())) {
                embed.setTitle("Insufficient Rights");
                embed.setDescription("Only members with the **administrator**-right can execute this command.\n\nYou do not have enough rights to execute this command,\nif you think this is a bug please contact a team member.");
                this.discord.getBackendManager().sendMessage(event, embed.build());
                return;
            }
        }
        
        if (this.discord.getBackendManager().getChannelId(event.getGuild()).isEmpty()) {
            embed.setDescription("You don't have a default channel yet to do this and let the bot connect to the channel write **" + this.discord.getBackendManager().getPrefix(event.getGuild()) + "join**.");
            this.discord.getBackendManager().sendMessage(event, embed.build());
            return;
        }
        VoiceChannel voiceChannel = event.getChannel().getJDA().getVoiceChannelById(this.discord.getBackendManager().getChannelId(event.getGuild()));
        if (!event.getMember().getVoiceState().inVoiceChannel()) {
            embed.setDescription("You have to be in the channel **" + voiceChannel.getName() + "** to change the volume.");
            this.discord.getBackendManager().sendMessage(event, embed.build());
            return;
        }
        if (!event.getMember().getVoiceState().getChannel().getId().equals(voiceChannel.getId())) {
            embed.setDescription("You have to be in the channel **" + voiceChannel.getName() + "** to change the volume.");
            this.discord.getBackendManager().sendMessage(event, embed.build());
            return;
        }
        if(!event.getGuild().getAudioManager().isConnected()) {
            embed.setDescription("The bot must be connected to the channel, you can do this with **" + this.discord.getBackendManager().getPrefix(event.getGuild()) + "join**.");
            this.discord.getBackendManager().sendMessage(event, embed.build());
            return;
        }
        
        if(args.length == 1) {
            embed.setDescription("The current volume is **" + this.discord.getBackendManager().getVolume(event.getGuild()) + "**,\nwith **" + this.discord.getBackendManager().getPrefix(event.getGuild()) + "vol** and a number between **1-100** you can adjust it.");
            this.discord.getBackendManager().sendMessage(event, embed.build());
            return;
        }
        
        if(args.length != 2) {
            embed.setDescription("You must enter a valid number between **1-100**!");
            this.discord.getBackendManager().sendMessage(event, embed.build());
            return;
        }
        
        try {
            
            if(this.discord.getBackendManager().getVolume(event.getGuild()) == Integer.valueOf(args[1])) {
                embed.setDescription("This volume is already set.");
                this.discord.getBackendManager().sendMessage(event, embed.build());
                return;
            }
            
            if(Integer.valueOf(args[1]) >= 1 && Integer.valueOf(args[1]) <= 100) {
                if (event.getGuild().getAudioManager().getSendingHandler() != null) {
                    AudioHandler audioHandler = (AudioHandler) event.getGuild().getAudioManager().getSendingHandler();
                    audioHandler.getPlayer().setVolume(Integer.valueOf(args[1]));
                }

                this.discord.getBackendManager().setVolume(event.getGuild(), Integer.valueOf(args[1]));
                embed.setDescription("You have successfully set the volume to **" + Integer.valueOf(args[1]) + "**.");
                this.discord.getBackendManager().sendMessage(event, embed.build());
            } else {
                embed.setDescription("You must enter a valid number between **1-100**!");
                this.discord.getBackendManager().sendMessage(event, embed.build());
            }
        } catch(NumberFormatException ex) {
            embed.setDescription("You must enter a valid number between **1-100**!");
            this.discord.getBackendManager().sendMessage(event, embed.build());
        }
    }
    
}
