package eu.atomicnetworks.discordbot.commands;

import eu.atomicnetworks.discordbot.DiscordBot;
import eu.atomicnetworks.discordbot.handler.AudioHandler;
import java.awt.Color;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 *
 * @author kacpe
 */
public class LeaveCommand {
    
    private final DiscordBot discord;

    public LeaveCommand(DiscordBot discord) {
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
            event.getChannel().sendMessage(embed.build()).queue();
            return;
        }
        
        if(event.getGuild().getAudioManager().getSendingHandler() != null) {
            AudioHandler audioHandler = (AudioHandler) event.getGuild().getAudioManager().getSendingHandler();
            audioHandler.stop();
        }
        this.discord.getBackendManager().setPlaying(event.getGuild(), false);
        event.getGuild().getAudioManager().closeAudioConnection();
        
        embed.setDescription("The bot has successfully left the voice channel.");
        event.getChannel().sendMessage(embed.build()).queue();
    } 
    
}
