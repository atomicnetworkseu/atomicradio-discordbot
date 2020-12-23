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
            event.getChannel().sendMessage(embed.build()).queue();
            return;
        }

        if (args.length == 1) {
            embed.setDescription("⚡ l **Settings**\n** **\n**"
                    + this.discord.getBackendManager().getPrefix(event.getGuild()) + "settings prefix** • Set a different command prefix.\n**"
                    + this.discord.getBackendManager().getPrefix(event.getGuild()) + "settings nick** • " + (this.discord.getBackendManager().getTag(event.getGuild()) ? "The bot will not change its name if the station is changed." : "The bot changes its name again when the station is changed.") + "\n**"
                    + this.discord.getBackendManager().getPrefix(event.getGuild()) + "settings rights** • " + (this.discord.getBackendManager().isMusicCommandsDenied(event.getGuild()) ? "Enables the ability for users to use commands to control the music." : "Disables the ability for users to use commands to control the music.") + "\n**"
                    + this.discord.getBackendManager().getPrefix(event.getGuild()) + "settings reset** • Reset all settings.");
            event.getChannel().sendMessage(embed.build()).queue();
            return;
        }

        switch (args[1].toLowerCase()) {
            case "prefix":
                if (args.length == 3) {
                    if (args[2].length() < 4) {
                        if (this.discord.getBackendManager().getPrefix(event.getGuild()).equalsIgnoreCase(args[2])) {
                            embed.setDescription("That is the same prefix.");
                            event.getChannel().sendMessage(embed.build()).queue();
                            return;
                        }
                        this.discord.getBackendManager().setPrefix(event.getGuild(), args[2].toLowerCase());
                        embed.setDescription("**Successful**, the new prefix of the server is `" + this.discord.getBackendManager().getPrefix(event.getGuild()) + "`.");
                        event.getChannel().sendMessage(embed.build()).queue();
                    } else {
                        embed.setDescription("The word is too long. You can use 3 letters.");
                        event.getChannel().sendMessage(embed.build()).queue();
                    }
                    return;
                }
                embed.setDescription("With this command you can select a new command prefix via **" + this.discord.getBackendManager().getPrefix(event.getGuild()) + "settings prefix <prefix>**.\n\nCurrent prefix: `" + this.discord.getBackendManager().getPrefix(event.getGuild()) + "`");
                event.getChannel().sendMessage(embed.build()).queue();
                break;
            case "nick":
                if (this.discord.getBackendManager().getTag(event.getGuild())) {
                    this.discord.getBackendManager().setTag(event.getGuild(), false);
                    embed.setDescription("**Successful**, the bot now no longer changes its name when the station is changed.");
                    event.getChannel().sendMessage(embed.build()).queue();
                    return;
                } else {
                    this.discord.getBackendManager().setTag(event.getGuild(), true);
                    embed.setDescription("**Successful**, the bot will change its name again when the station is changed.");
                    event.getChannel().sendMessage(embed.build()).queue();
                    return;
                }
            case "rights":
                if (this.discord.getBackendManager().isMusicCommandsDenied(event.getGuild())) {
                    this.discord.getBackendManager().setMusicCommands(event.getGuild(), false);
                    embed.setDescription("**Successful**, the commands `.play`, `.vol` & `.join` can now be executed by all users again.");
                    event.getChannel().sendMessage(embed.build()).queue();
                    return;
                } else {
                    this.discord.getBackendManager().setMusicCommands(event.getGuild(), true);
                    embed.setDescription("**Successful**, the commands `.play`, `.vol` & `.join` can now only be used by users with the administrator authorisation.");
                    event.getChannel().sendMessage(embed.build()).queue();
                    return;
                }
            case "reset":
                this.discord.getBackendManager().setPrefix(event.getGuild(), ".");
                this.discord.getBackendManager().setMusic(event.getGuild(), "one");
                this.discord.getBackendManager().setTag(event.getGuild(), true);
                this.discord.getBackendManager().setMusicCommands(event.getGuild(), false);
                embed.setDescription("**Successful**, the reset was executed successfully, there is no more tag and the prefix has been changed to `.`.");
                event.getChannel().sendMessage(embed.build()).queue();
                break;
            default:
                embed.setDescription("⚡ l **Settings**\n** **\n**"
                        + this.discord.getBackendManager().getPrefix(event.getGuild()) + "settings prefix** • Set a different command prefix.\n**"
                        + this.discord.getBackendManager().getPrefix(event.getGuild()) + "settings nick** • " + (this.discord.getBackendManager().getTag(event.getGuild()) ? "The bot will not change its name if the station is changed." : "The bot changes its name again when the station is changed.") + "\n**"
                        + this.discord.getBackendManager().getPrefix(event.getGuild()) + "settings rights** • " + (this.discord.getBackendManager().isMusicCommandsDenied(event.getGuild()) ? "Enables the ability for users to use commands to control the music." : "Disables the ability for users to use commands to control the music.") + "\n**"
                        + this.discord.getBackendManager().getPrefix(event.getGuild()) + "settings reset** • Reset all settings.");
                event.getChannel().sendMessage(embed.build()).queue();
                break;
        }
    }

}
