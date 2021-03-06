package commands;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import core.DiscordBot;
import core.GatherGame;
import core.GatherObject;
import core.SubstitutionObject;
import discord4j.core.object.entity.Channel;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.Member;

/**
 * Admin only command for clearing all current sub requests. Must be used in command channel. 
 * Calls GatherObject.SubstitutionObject.ClearSubs()
 * @author cameron
 * @see SubstitutionObject#ClearSubs()
 */
public class CommandClearSubs extends Command<Message, Member, Channel>
{
	public CommandClearSubs(Commands<Message, Member, Channel> commands)
	{
		super(commands, Arrays.asList("clearsubs"), "Admin only - clear all current sub requests, or clear subs for specific users!", "clearsubs <@user...>");
	}

	@Override
	public boolean isChannelValid(Channel channel) {
		GatherObject gather = DiscordBot.getGatherObjectForChannel(channel);
		if(gather==null) return false;
		else return true;
	}

	@Override
	public boolean hasPermission(Member member, Channel channel)
	{
		GatherObject gather = DiscordBot.getGatherObjectForChannel(channel);
		if(gather==null) return false;
		return gather.isAdmin(member);
	}

	@Override
	public String onCommand(String[] splitMessage, String messageString, Message messageObject, Member member, Channel channel)
	{
		GatherObject gather = DiscordBot.getGatherObjectForChannel(channel);
		if(gather==null) return null;

		//check if there was any mentions in the message
		List<Member> mentions = messageObject.getUserMentionIds().stream().map((snowflake) -> DiscordBot.fetchMember(gather.getGuild().getId(), snowflake)).collect(Collectors.toList());
		if(mentions!=null && mentions.size()>0)
		{
			boolean doneSomething = false;
			for(Member mentionedUser : mentions)
			{
				if(gather.substitutions.removeSubRequest(mentionedUser))
				{
					this.reply(messageObject, "Sub request **cleared** for "+mentionedUser.getDisplayName()+"!");
					doneSomething = true;
					continue;
				}
				if(gather.substitutions.removeSubVotes(mentionedUser))
				{
					this.reply(messageObject, "Sub votes **cleared** for "+mentionedUser.getDisplayName()+"!");
					doneSomething = true;
					continue;
				}
			}
			//we get here if there was mentions in the message, and we have finished looping through them
			if(!doneSomething)
			{
				//want some kind of feedback if nothing else happened
				this.reply(messageObject, "**No sub request or votes** found for that user!");
			}
			return null;
		}
		
		//check if there was any other arguments in the message
		int gameId = -1;
		if(splitMessage.length>1)
		{
			try
			{
				gameId = Integer.parseInt(splitMessage[1]);
			}
			catch (NumberFormatException e)
			{
				return "**Error** parsing supplied game id "+member.getDisplayName();
			}
		}
		else
		{
			//got here if the message had no extra arguments
			gather.substitutions.clearSubs();
			return "Sub list **cleared**";
		}
		
		GatherGame game = gather.getRunningGame(gameId);
		if(game==null)
		{
			return "**No game found** with that id "+member.getDisplayName()+"!";
		}
		if(gather.substitutions.removeSubRequests(game))
		{
			return "All sub **requests cleared** for game #"+gameId;
		}
		else if(gather.substitutions.removeSubVotes(game))
		{
			return "All sub **votes cleared** for game #"+gameId;
		}
		else
		{
			return "**No sub requests or votes** exist for game #"+gameId;
		}
	}
}