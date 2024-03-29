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
public class SettingsCommand {

    private final DiscordBot discord;

    public SettingsCommand(DiscordBot discord) {
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
            embed.setDescription("✨ l **Settings**\n** **\n**"
                    + this.discord.getBackendManager().getPrefix(event.getGuild()) + "settings prefix** • Set your own command prefix for the server.\n**"
                    + this.discord.getBackendManager().getPrefix(event.getGuild()) + "settings nick** • " + (this.discord.getBackendManager().getTag(event.getGuild()) ? "The bot will not change its name if the station is changed." : "The bot changes its name again when the station is changed.") + "\n**"
                    + this.discord.getBackendManager().getPrefix(event.getGuild()) + "settings rights** • " + (this.discord.getBackendManager().isMusicCommandsDenied(event.getGuild()) ? "Enables the ability for users to use commands to control the music." : "Disables the ability for users to use commands to control the music.") + "\n**"
                    + this.discord.getBackendManager().getPrefix(event.getGuild()) + "settings reset** • Resets all settings made for this server.");
            this.discord.getBackendManager().sendMessage(event, embed.build());
            return;
        }

        switch (args[1].toLowerCase()) {
            case "prefix":
                if (args.length == 3) {
                    if (args[2].length() < 6) {
                        if (this.discord.getBackendManager().getPrefix(event.getGuild()).equalsIgnoreCase(args[2])) {
                            embed.setDescription("That is the same prefix.");
                            this.discord.getBackendManager().sendMessage(event, embed.build());
                            return;
                        }
                        this.discord.getBackendManager().setPrefix(event.getGuild(), args[2].toLowerCase());
                        embed.setDescription("**Successful**, the new prefix of the server is `" + this.discord.getBackendManager().getPrefix(event.getGuild()) + "`.");
                        this.discord.getBackendManager().sendMessage(event, embed.build());
                    } else {
                        embed.setDescription("The word is too long. You can use 5 letters.");
                        this.discord.getBackendManager().sendMessage(event, embed.build());
                    }
                    return;
                }
                embed.setDescription("With **" + this.discord.getBackendManager().getPrefix(event.getGuild()) + "settings prefix <prefix>** you can set which prefix the bot should react to in the future.\n\nCurrent prefix: `" + this.discord.getBackendManager().getPrefix(event.getGuild()) + "`");
                this.discord.getBackendManager().sendMessage(event, embed.build());
                break;
            case "nick":
                if (this.discord.getBackendManager().getTag(event.getGuild())) {
                    this.discord.getBackendManager().setTag(event.getGuild(), false);
                    embed.setDescription("**Successful**, the bot now no longer changes its name when the station is changed.");
                    this.discord.getBackendManager().sendMessage(event, embed.build());
                    return;
                } else {
                    this.discord.getBackendManager().setTag(event.getGuild(), true);
                    embed.setDescription("**Successful**, the bot will change its name again when the station is changed.");
                    this.discord.getBackendManager().sendMessage(event, embed.build());
                    return;
                }
            case "rights":
                if (this.discord.getBackendManager().isMusicCommandsDenied(event.getGuild())) {
                    this.discord.getBackendManager().setMusicCommands(event.getGuild(), false);
                    embed.setDescription("**Successful**, the commands `.play` & `.vol` can now be executed by all users again.");
                    this.discord.getBackendManager().sendMessage(event, embed.build());
                    return;
                } else {
                    this.discord.getBackendManager().setMusicCommands(event.getGuild(), true);
                    embed.setDescription("**Successful**, the commands `.play` & `.vol` can now only be used by users with the administrator authorisation.");
                    this.discord.getBackendManager().sendMessage(event, embed.build());
                    return;
                }
            case "reset":
                this.discord.getBackendManager().setPrefix(event.getGuild(), ".");
                this.discord.getBackendManager().setMusic(event.getGuild(), "one");
                this.discord.getBackendManager().setTag(event.getGuild(), true);
                this.discord.getBackendManager().setMusicCommands(event.getGuild(), false);
                embed.setDescription("**Successful**, the name of the bot changes again, the music commands can be executed by everyone again and the tag is `.` again.");
                this.discord.getBackendManager().sendMessage(event, embed.build());
                break;
            default:
                embed.setDescription("✨ l **Settings**\n** **\n**"
                        + this.discord.getBackendManager().getPrefix(event.getGuild()) + "settings prefix** • Set your own command prefix for the server.\n**"
                        + this.discord.getBackendManager().getPrefix(event.getGuild()) + "settings nick** • " + (this.discord.getBackendManager().getTag(event.getGuild()) ? "The bot will not change its name if the station is changed." : "The bot changes its name again when the station is changed.") + "\n**"
                        + this.discord.getBackendManager().getPrefix(event.getGuild()) + "settings rights** • " + (this.discord.getBackendManager().isMusicCommandsDenied(event.getGuild()) ? "Enables the ability for users to use commands to control the music." : "Disables the ability for users to use commands to control the music.") + "\n**"
                        + this.discord.getBackendManager().getPrefix(event.getGuild()) + "settings reset** • Resets all settings made for this server.");
                this.discord.getBackendManager().sendMessage(event, embed.build());
                break;
        }
    }

}
