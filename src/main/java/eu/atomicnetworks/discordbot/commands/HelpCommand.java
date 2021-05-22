package eu.atomicnetworks.discordbot.commands;

import eu.atomicnetworks.discordbot.DiscordBot;
import java.awt.Color;
import java.text.MessageFormat;
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
public class HelpCommand {
    
    private final DiscordBot discord;

    public HelpCommand(DiscordBot discord) {
        this.discord = discord;
    }
    
    public void execute(GuildMessageReceivedEvent event) {
        Message message = event.getMessage();
        String[] args = message.getContentRaw().split(" ");
        
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(new Color(149, 79, 180));
        embed.setAuthor("Helpdesk", null, "https://cdn.atomicnetworks.eu/discord/icon.png");
        if(!this.discord.getBackendManager().checkForPermissions(event.getMember())) {
            embed.setDescription(MessageFormat.format("** **\n"
                + ":gear: | **Commands**\n\n"
                + "**{0}help**  • Informs you about all available commands.\n"
                + "**{0}play**  • Changes the currently playing station in your channel.\n"
                + "**{0}song**  • Informs you about the currently playing song.\n"
                + "**{0}vol**  • Lets you adjust the current volume.\n"
                + "**{0}report**  • With this command it is possible to report songs that you do not like.\n"
                + "**" + this.discord.getBackendManager().getPrefix(event.getGuild()) + "info**  • Sends you information about our radio station and the Discordbot.\n\n"
                + "** **\n", this.discord.getBackendManager().getPrefix(event.getGuild())));
        } else {
            embed.setDescription(MessageFormat.format("** **\n"
                + ":gear: | **Commands**\n\n"
                + "**{0}help**  • Informs you about all available commands.\n"
                + "**{0}play**  • Changes the currently playing station in your channel.\n"
                + "**{0}song**  • Informs you about the currently playing song.\n"
                + "**{0}vol**  • Lets you adjust the current volume.\n"
                + "**{0}report**  • With this command it is possible to report songs that you do not like.\n"
                + "**" + this.discord.getBackendManager().getPrefix(event.getGuild()) + "info**  • Sends you information about our radio station and the Discordbot.\n\n"
                + "** **\n"
                + ":hammer_pick: | ** Extended commands**\n"
                + "Which can only be used by members with the `administrator` authorization.\n"
                + "** **\n"
                + "**" + this.discord.getBackendManager().getPrefix(event.getGuild()) + "join**  • Connects the bot to the specified channel.\n"
                + "**" + this.discord.getBackendManager().getPrefix(event.getGuild()) + "leave**  • Disconnects the bot from the channel.\n"
                + "**" + this.discord.getBackendManager().getPrefix(event.getGuild()) + "bass** • With this setting, you can finally enjoy your songs with more bass.\n"
                + "**" + this.discord.getBackendManager().getPrefix(event.getGuild()) + "settings**  • With this command you can make advanced adjustments.\n\n"
                + "** **\n", this.discord.getBackendManager().getPrefix(event.getGuild())));
        }
        this.discord.getBackendManager().sendMessage(event, embed.build());
    } 
    
}
