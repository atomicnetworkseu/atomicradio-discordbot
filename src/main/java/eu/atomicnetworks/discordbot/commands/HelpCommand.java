package eu.atomicnetworks.discordbot.commands;

import eu.atomicnetworks.discordbot.DiscordBot;
import java.awt.Color;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 *
 * @author kacpe
 */
public class HelpCommand {
    
    private final DiscordBot discord;

    public HelpCommand(DiscordBot discord) {
        this.discord = discord;
    }
    
    public void execute(GuildMessageReceivedEvent event) {
        Message message = event.getMessage();
        String[] args = message.getContentRaw().split(" ");
        event.getChannel().sendTyping().completeAfter(1, TimeUnit.SECONDS);
        
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(new Color(149, 79, 180));
        embed.setAuthor("Helpdesk", null, "https://cdn.atomicnetworks.eu/atnw/logo/logo_white.png");
        embed.setTitle("Hello, we are happy to see you. You can see all the available commands below.");
        if(!this.discord.getBackendManager().checkForPermissions(event.getMember())) {
            embed.setDescription(MessageFormat.format("** **\n"
                + ":gear: | **Commands**\n\n"
                + "**{0}help**  • This gives you an overview of all available commands.\n"
                + "**{0}join**  • With this command you can connect the bot to the channel.\n"
                + "**{0}play**  • If you prefer to listen to another station from us.\n"
                + "**{0}song**  • You want to know which song is playing?, with this command you can easily find out.\n"
                + "**{0}vol**  • This allows you to adjust the volume.\n"
                + "**{0}report**  • You don't like a song? then let us know and we will improve the playlist!\n"
                + "**" + this.discord.getBackendManager().getPrefix(event.getGuild()) + "info**  • Here you can find information about our stations and the Musicbot.\n\n"
                + "** **\n", this.discord.getBackendManager().getPrefix(event.getGuild())));
        } else {
            embed.setDescription(MessageFormat.format("** **\n"
                + ":gear: | **Commands**\n\n"
                + "**{0}help**  • This gives you an overview of all available commands.\n"
                + "**{0}join**  • With this command you can connect the bot to the channel.\n"
                + "**{0}play**  • If you prefer to listen to another station from us.\n"
                + "**{0}song**  • You want to know which song is playing?, with this command you can easily find out.\n"
                + "**{0}vol**  • This allows you to adjust the volume.\n"
                + "**{0}report**  • You don't like a song? then let us know and we will improve the playlist!\n"
                + "**" + this.discord.getBackendManager().getPrefix(event.getGuild()) + "info**  • Here you can find information about our stations and the Musicbot.\n\n"
                + "** **\n"
                + ":hammer_pick: | ** Extended commands**\n"
                + "Which can only be used by members with the `administrator` authorization.\n"
                + "** **\n"
                + "**" + this.discord.getBackendManager().getPrefix(event.getGuild()) + "leave**  • Let the bot disconnect with this command.\n"
                + "**" + this.discord.getBackendManager().getPrefix(event.getGuild()) + "setup**  • Define the channel where the bot should join automatically.\n"
                + "**" + this.discord.getBackendManager().getPrefix(event.getGuild()) + "settings**  • With this command you can make small adjustments.\n\n"
                + "** **\n", this.discord.getBackendManager().getPrefix(event.getGuild())));
        }
        embed.setFooter("If you have a suggestion for improvement,\ncriticism or something similar, just contact us!" , "https://cdn.atomicnetworks.eu/atnw/logo/logo.png");
        event.getChannel().sendMessage(embed.build()).queue();
    } 
    
}
