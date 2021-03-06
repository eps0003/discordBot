package commands;
import java.util.Arrays;

import core.DiscordBot;
import core.GatherObject;
import discord4j.core.object.entity.Channel;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.Member;

/**Command for checking the players in currently running games.  Must be used in command channel. 
 * @author cameron
 * @see GatherObject#playersString()
 */
public class CommandPlayers extends Command<Message, Member, Channel>
{
	public CommandPlayers(Commands<Message, Member, Channel> commands)
	{
		super(commands, Arrays.asList("players", "teams"), "Check players currently playing");
	}

	@Override
	public boolean isChannelValid(Channel channel) {
		GatherObject gather = DiscordBot.getGatherObjectForChannel(channel);
		if(gather==null) return false;
		else return true;
	}

	@Override
	public String onCommand(String[] splitMessage, String messageString, Message messageObject, Member member, Channel channel)
	{
		GatherObject gather = DiscordBot.getGatherObjectForChannel(channel);
		if(gather==null) return null;
		
		String currentPlayers = gather.playersString();
		if(!currentPlayers.isEmpty())
		{
			return "Current games: \n" + currentPlayers;
		}
		else
		{
			return "No players currently playing";
		}
	}
}