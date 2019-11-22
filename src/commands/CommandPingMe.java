package commands;
import java.util.Arrays;

import core.DiscordBot;
import core.GatherObject;
import core.PlayerObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

/**
 * Chat command for players to add to/remove from the soft queue/ping list. Must be used in command channel. 
 * Prints a response message to the command channel depending on the result of the command.
 * The soft queue is for players to join if they are interested in a game but do not want to commit to a full queue.
 * This means that they do not mind other players mentioning them
 * <p>
 * 
 * @author cameron
 *
 */
public class CommandPingMe extends Command<IMessage, IUser, IChannel, IGuild>
{
	public CommandPingMe(Commands<IMessage, IUser, IChannel, IGuild> commands)
	{
		super(commands, Arrays.asList("pingme", "softqueue", "softadd", "soft", "interested", "interest", "int", "uninterested", "uninterest", "unint", "role", "gather"), "Add or remove yourself from the soft queue");
	}

	@Override
	public boolean isChannelValid(IChannel channel) {
		GatherObject gather = DiscordBot.getGatherObjectForChannel(channel);
		if(gather==null) return false;
		else return true;
	}

	@Override
	public String onCommand(String[] splitMessage, String messageString, IMessage messageObject, IUser user, IChannel channel, IGuild guild)
	{
		GatherObject gather = DiscordBot.getGatherObjectForChannel(channel);
		if(gather==null) return null;

		PlayerObject player = DiscordBot.players.getOrCreatePlayerObject(user);
		if(player==null)
		{
			return "You must be linked to play gather " + user.getDisplayName(guild) + "! Use **!link KAGUsernameHere** to get started or **!linkhelp** for more information";
		}
		
		int val = gather.toggleInterested(user);
		
		switch(val)
		{
		case 2:
			return user.getDisplayName(guild)+" is no longer **interested**";
		case 0:
			return user.getDisplayName(guild)+" is **interested** they want to be notified when there is enough players for a game!";
		}
		//the switch handles all cases, so this should never be triggered
		return null;
	}
}