package eu.atomicnetworks.discordbot.commands;

import eu.atomicnetworks.discordbot.DiscordBot;
import eu.atomicnetworks.discordbot.handler.AudioHandler;
import java.awt.Color;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.EmbedBuilder;
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
        
        if(this.discord.getBackendManager().getChannelId(event.getGuild()).isEmpty()) {
            embed.setDescription("You don't have a default channel yet to do this and let the bot connect to the channel write **" + this.discord.getBackendManager().getPrefix(event.getGuild()) + "setup**.");
            event.getChannel().sendMessage(embed.build()).queue();
            return;
        }
        
        if(event.getGuild().getAudioManager().isConnected()) {
            embed.setDescription("**The bot is already in a channel**,\nbut a member with the permission **administrator** can disconnect the bot from the channel with **" + this.discord.getBackendManager().getPrefix(event.getGuild()) + "leave**.");
            event.getChannel().sendMessage(embed.build()).queue();
            return;
        }
        
        VoiceChannel voiceChannel = event.getChannel().getJDA().getVoiceChannelById(this.discord.getBackendManager().getChannelId(event.getGuild()));
        if(voiceChannel == null) {
            embed.setDescription("The channel has been deleted or the bot no longer has permission to enter it.");
            event.getChannel().sendMessage(embed.build()).queue();
            return;
        }
        
        if(event.getGuild().getAudioManager().getSendingHandler() != null) {
            AudioHandler audioHandler = (AudioHandler) event.getGuild().getAudioManager().getSendingHandler();
            audioHandler.stop();
        }
        
        try {
            event.getGuild().getAudioManager().openAudioConnection(voiceChannel);
        } catch (InsufficientPermissionException ex) {
            embed.setDescription("I do not have permission to join the voice channel, please contact an administrator.");
            event.getChannel().sendMessage(embed.build()).queue();
            return;
        }
        
        switch(this.discord.getBackendManager().getMusic(event.getGuild())) {
            case "one":
                this.discord.getBackendManager().startStream(event.getGuild(), "http://10.10.10.105:8000/one/highquality.mp3");
                this.discord.getBackendManager().setPlaying(event.getGuild(), true);
                this.discord.getBackendManager().setMusic(event.getGuild(), "one");
                this.discord.getBackendManager().setChannelId(event.getGuild(), voiceChannel.getId());
                break;
            case "dance":
                this.discord.getBackendManager().startStream(event.getGuild(), "http://10.10.10.105:8010/dance/highquality.mp3");
                this.discord.getBackendManager().setPlaying(event.getGuild(), true);
                this.discord.getBackendManager().setMusic(event.getGuild(), "dance");
                this.discord.getBackendManager().setChannelId(event.getGuild(), voiceChannel.getId());
                break;
            case "trap":
                this.discord.getBackendManager().startStream(event.getGuild(), "http://10.10.10.105:8020/trap/highquality.mp3");
                this.discord.getBackendManager().setPlaying(event.getGuild(), true);
                this.discord.getBackendManager().setMusic(event.getGuild(), "trap");
                this.discord.getBackendManager().setChannelId(event.getGuild(), voiceChannel.getId());
                break;
            default:
                this.discord.getBackendManager().startStream(event.getGuild(), "http://10.10.10.105:8000/one/highquality.mp3");
                this.discord.getBackendManager().setPlaying(event.getGuild(), true);
                this.discord.getBackendManager().setMusic(event.getGuild(), "one");
                this.discord.getBackendManager().setChannelId(event.getGuild(), voiceChannel.getId());
                break;
        }
        
        embed.setDescription("The bot has successfully joined the channel,\nthe last played stream will be loaded which you can change with **" + this.discord.getBackendManager().getPrefix(event.getGuild()) + "play**.");
        event.getChannel().sendMessage(embed.build()).queue();
    } 
    
}
