package eu.atomicnetworks.discordbot.commands;

import eu.atomicnetworks.discordbot.DiscordBot;
import eu.atomicnetworks.discordbot.handler.AudioHandler;
import java.awt.Color;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
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

        if (this.discord.getBackendManager().getChannelId(event.getGuild()).isEmpty()) {
            embed.setDescription("You don't have a default channel yet to do this and let the bot connect to the channel write **" + this.discord.getBackendManager().getPrefix(event.getGuild()) + "setup**.");
            event.getChannel().sendMessage(embed.build()).queue();
            return;
        }
        VoiceChannel voiceChannel = event.getChannel().getJDA().getVoiceChannelById(this.discord.getBackendManager().getChannelId(event.getGuild()));
        if(voiceChannel == null) {
            embed.setDescription("The channel has been deleted or the bot no longer has permission to enter it.");
            event.getChannel().sendMessage(embed.build()).queue();
            return;
        }
        if (!event.getMember().getVoiceState().inVoiceChannel()) {
            embed.setDescription("You have to be in the channel **" + voiceChannel.getName() + "** to change the station.");
            event.getChannel().sendMessage(embed.build()).queue();
            return;
        }
        if (!event.getMember().getVoiceState().getChannel().getId().equals(voiceChannel.getId())) {
            embed.setDescription("You have to be in the channel **" + voiceChannel.getName() + "** to change the station.");
            event.getChannel().sendMessage(embed.build()).queue();
            return;
        }
        
        if(!event.getGuild().getAudioManager().isConnected()) {
            event.getGuild().getAudioManager().openAudioConnection(voiceChannel);
        }

        if (args.length != 2) {
            embed.setDescription("Please choose with **" + this.discord.getBackendManager().getPrefix(event.getGuild()) + "play** **one**, **dance** or **trap** a station from which you want to play!");
            event.getChannel().sendMessage(embed.build()).queue();
            return;
        }

        switch (args[1].toLowerCase()) {
            case "one":
                if (this.discord.getBackendManager().getMusic(event.getGuild()).equalsIgnoreCase("one")) {
                    if (this.discord.getBackendManager().getPlaying(event.getGuild())) {
                        embed.setDescription("The station is already being played.");
                        event.getChannel().sendMessage(embed.build()).queue();
                        return;
                    }
                }

                if (event.getGuild().getAudioManager().getSendingHandler() != null) {
                    AudioHandler audioHandler = (AudioHandler) event.getGuild().getAudioManager().getSendingHandler();
                    audioHandler.stop();
                }
                this.discord.getBackendManager().startStream(event.getGuild(), "http://10.10.10.105:8000/one/highquality.mp3");
                this.discord.getBackendManager().setPlaying(event.getGuild(), true);
                this.discord.getBackendManager().setMusic(event.getGuild(), "one");
                this.discord.getBackendManager().setChannelId(event.getGuild(), voiceChannel.getId());

                embed.setDescription("Now **atr.one** is played.");
                event.getChannel().sendMessage(embed.build()).queue();

                if(event.getGuild().getSelfMember().hasPermission(Permission.NICKNAME_CHANGE)) {
                    if(this.discord.getBackendManager().getTag(event.getGuild())) {
                        event.getGuild().getSelfMember().modifyNickname("atomicradio » atr.one").queue();
                    }
                }
                break;
            case "dance":
                if (this.discord.getBackendManager().getMusic(event.getGuild()).equalsIgnoreCase("dance")) {
                    if (this.discord.getBackendManager().getPlaying(event.getGuild())) {
                        embed.setDescription("The station is already being played.");
                        event.getChannel().sendMessage(embed.build()).queue();
                        return;
                    }
                }

                if (event.getGuild().getAudioManager().getSendingHandler() != null) {
                    AudioHandler audioHandler = (AudioHandler) event.getGuild().getAudioManager().getSendingHandler();
                    audioHandler.stop();
                }
                this.discord.getBackendManager().startStream(event.getGuild(), "http://10.10.10.105:8010/dance/highquality.mp3");
                this.discord.getBackendManager().setPlaying(event.getGuild(), true);
                this.discord.getBackendManager().setMusic(event.getGuild(), "dance");
                this.discord.getBackendManager().setChannelId(event.getGuild(), voiceChannel.getId());

                embed.setDescription("Now **atr.dance** is played.");
                event.getChannel().sendMessage(embed.build()).queue();

                if(event.getGuild().getSelfMember().hasPermission(Permission.NICKNAME_CHANGE)) {
                    if(this.discord.getBackendManager().getTag(event.getGuild())) {
                        event.getGuild().getSelfMember().modifyNickname("atomicradio » atr.dance").queue();
                    }
                }
                break;
            case "trap":
                if (this.discord.getBackendManager().getMusic(event.getGuild()).equalsIgnoreCase("trap")) {
                    if (this.discord.getBackendManager().getPlaying(event.getGuild())) {
                        embed.setDescription("The station is already being played.");
                        event.getChannel().sendMessage(embed.build()).queue();
                        return;
                    }
                }

                if (event.getGuild().getAudioManager().getSendingHandler() != null) {
                    AudioHandler audioHandler = (AudioHandler) event.getGuild().getAudioManager().getSendingHandler();
                    audioHandler.stop();
                }
                this.discord.getBackendManager().startStream(event.getGuild(), "http://10.10.10.105:8020/trap/highquality.mp3");
                this.discord.getBackendManager().setPlaying(event.getGuild(), true);
                this.discord.getBackendManager().setMusic(event.getGuild(), "trap");
                this.discord.getBackendManager().setChannelId(event.getGuild(), voiceChannel.getId());

                embed.setDescription("Now **atr.trap** is played.");
                event.getChannel().sendMessage(embed.build()).queue();

                if(event.getGuild().getSelfMember().hasPermission(Permission.NICKNAME_CHANGE)) {
                    if(this.discord.getBackendManager().getTag(event.getGuild())) {
                        event.getGuild().getSelfMember().modifyNickname("atomicradio » atr.trap").queue();
                    }
                }
                break;
            default:
                embed.setDescription("Please choose with **" + this.discord.getBackendManager().getPrefix(event.getGuild()) + "play** **one**, **dance** or **trap** a station from which you want to play!");
                event.getChannel().sendMessage(embed.build()).queue();
                break;
        }

    }

}
