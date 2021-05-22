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
public class PlayCommand {

    private final DiscordBot discord;

    public PlayCommand(DiscordBot discord) {
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
        if(voiceChannel == null) {
            embed.setDescription("The channel has been deleted or the bot no longer has permission to enter it.");
            this.discord.getBackendManager().sendMessage(event, embed.build());
            return;
        }
        if (!event.getMember().getVoiceState().inVoiceChannel()) {
            embed.setDescription("You have to be in the channel **" + voiceChannel.getName() + "** to change the station.");
            this.discord.getBackendManager().sendMessage(event, embed.build());
            return;
        }
        if (!event.getMember().getVoiceState().getChannel().getId().equals(voiceChannel.getId())) {
            embed.setDescription("You have to be in the channel **" + voiceChannel.getName() + "** to change the station.");
            this.discord.getBackendManager().sendMessage(event, embed.build());
            return;
        }
        
        if(this.discord.getBackendManager().checkForPermissions(event.getMember())) {
            if(!event.getGuild().getAudioManager().isConnected()) {
                try {
                    event.getGuild().getAudioManager().openAudioConnection(voiceChannel);
                } catch (InsufficientPermissionException ex) {
                    embed.setDescription("I do not have permission to **join** the voice channel, please contact an administrator.");
                    this.discord.getBackendManager().sendMessage(event, embed.build());
                    return;
                }
            }
        } else {
            if(!event.getGuild().getAudioManager().isConnected()) {
                embed.setDescription("Currently the musicbot is not connected to the voice channel, please ask a person with **administrator**-right to execute the **.join** command.");
                this.discord.getBackendManager().sendMessage(event, embed.build());
                return;
            }
        }

        if (args.length != 2) {
            embed.setDescription("Please choose with **" + this.discord.getBackendManager().getPrefix(event.getGuild()) + "play** **one**, **dance** or **trap** a station from which you want to play!");
            this.discord.getBackendManager().sendMessage(event, embed.build());
            return;
        }
        
        try {
            StationChannnel stationChannnel = StationChannnel.valueOf(args[1].toUpperCase());
            if (this.discord.getBackendManager().getMusic(event.getGuild()).equalsIgnoreCase(stationChannnel.getName())) {
                if (this.discord.getBackendManager().getPlaying(event.getGuild())) {
                    embed.setDescription("The station is already being played.");
                    this.discord.getBackendManager().sendMessage(event, embed.build());
                    return;
                }
            }

            if (event.getGuild().getAudioManager().getSendingHandler() != null) {
                AudioHandler audioHandler = (AudioHandler) event.getGuild().getAudioManager().getSendingHandler();
                audioHandler.stop();
            }
            this.discord.getBackendManager().startStream(event.getGuild(), stationChannnel.getUrl());
            this.discord.getBackendManager().setPlaying(event.getGuild(), true);
            this.discord.getBackendManager().setMusic(event.getGuild(), stationChannnel.getName());
            this.discord.getBackendManager().setChannelId(event.getGuild(), voiceChannel.getId());

            embed.setDescription("Now **atr." + stationChannnel.getName() + "** is played.");
            this.discord.getBackendManager().sendMessage(event, embed.build());

            if (event.getGuild().getSelfMember().hasPermission(Permission.NICKNAME_CHANGE)) {
                if (this.discord.getBackendManager().getTag(event.getGuild())) {
                    event.getGuild().getSelfMember().modifyNickname("atomicradio » atr." + stationChannnel.getName()).queue();
                }
            }
        } catch(IllegalArgumentException ex) {
            embed.setDescription("The specified station was not found!");
            this.discord.getBackendManager().sendMessage(event, embed.build());
        }

    }

}
