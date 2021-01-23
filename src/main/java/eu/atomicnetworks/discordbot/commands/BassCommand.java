package eu.atomicnetworks.discordbot.commands;

import eu.atomicnetworks.discordbot.DiscordBot;
import eu.atomicnetworks.discordbot.handler.AudioHandler;
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
public class BassCommand {

    private final DiscordBot discord;

    public BassCommand(DiscordBot discord) {
        this.discord = discord;
    }

    public void execute(GuildMessageReceivedEvent event) {
        Message message = event.getMessage();
        String[] args = message.getContentRaw().split(" ");

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(new Color(149, 79, 180));
        
        if (!this.discord.getBackendManager().checkForPermissions(event.getMember())) {
            embed.setTitle("Insufficient Rights");
            embed.setDescription("Only members with the **administrator**-right can execute this command.\n\nYou do not have enough rights to execute this command,\nif you think this is a bug please contact a team member.");
            this.discord.getBackendManager().sendMessage(event, embed.build());
            return;
        }

        if (args.length == 1) {
            embed.setDescription(":cloud_tornado: l **Bass**\n\n"
                    + "If you love bass as much as we do, you've come to the right place.\n"
                    + "You can increase it with the command **.bass <level>** and reset it with **.bass reset**.");
            this.discord.getBackendManager().sendMessage(event, embed.build());
            return;
        }

        if (args.length == 2) {
            AudioHandler audioHandler = (AudioHandler) event.getGuild().getAudioManager().getSendingHandler();
            
            if (args[1].equalsIgnoreCase("reset")) {
                if (audioHandler != null) {
                    audioHandler.removeFilter();
                }
                embed.setDescription("**Successful**, the bass boost has been deactivated.");
                this.discord.getBackendManager().sendMessage(event, embed.build());
                return;
            }
            
            try {
                int bassLevel = Integer.valueOf(args[1]);

                if (bassLevel >= 0 && bassLevel <= 5) {
                    switch (bassLevel) {
                        case 0:
                            if (audioHandler != null) {
                                audioHandler.removeFilter();
                            }
                            this.discord.getBackendManager().setBassLevel(event.getGuild(), 0);
                            embed.setDescription("**Successful**, the bass boost has been deactivated.");
                            this.discord.getBackendManager().sendMessage(event, embed.build());
                            break;
                        case 1:
                            if (audioHandler != null) {
                                audioHandler.bassFilter(20);
                            }
                            this.discord.getBackendManager().setBassLevel(event.getGuild(), 1);
                            embed.setDescription("**Successful**, the level is now **1**.");
                            this.discord.getBackendManager().sendMessage(event, embed.build());
                            break;
                        case 2:
                            if (audioHandler != null) {
                                audioHandler.bassFilter(40);
                            }
                            this.discord.getBackendManager().setBassLevel(event.getGuild(), 2);
                            embed.setDescription("**Successful**, the level is now **2**.");
                            this.discord.getBackendManager().sendMessage(event, embed.build());
                            break;
                        case 3:
                            if (audioHandler != null) {
                                audioHandler.bassFilter(60);
                            }
                            this.discord.getBackendManager().setBassLevel(event.getGuild(), 3);
                            embed.setDescription("**Successful**, the level is now **3**.");
                            this.discord.getBackendManager().sendMessage(event, embed.build());
                            break;
                        case 4:
                            if (audioHandler != null) {
                                audioHandler.bassFilter(80);
                            }
                            this.discord.getBackendManager().setBassLevel(event.getGuild(), 4);
                            embed.setDescription("**Successful**, the level is now **4**.");
                            this.discord.getBackendManager().sendMessage(event, embed.build());
                            break;
                        case 5:
                            if (audioHandler != null) {
                                audioHandler.bassFilter(100);
                            }
                            this.discord.getBackendManager().setBassLevel(event.getGuild(), 5);
                            embed.setDescription("**Successful**, the level is now **5**.");
                            this.discord.getBackendManager().sendMessage(event, embed.build());
                            break;
                    }
                } else {
                    embed.setDescription("You must enter a valid number between **1-5**.");
                    this.discord.getBackendManager().sendMessage(event, embed.build());
                }
            } catch (NumberFormatException ex) {
                embed.setDescription("You must enter a valid number between **1-5**.");
                this.discord.getBackendManager().sendMessage(event, embed.build());
            }
        }
    }

}
