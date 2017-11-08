import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.handle.obj.IMessage;

/**Command for voting to scramble the teams of a game. Must be used in command channel. 
 * @author cameron
 * @see GatherObject#addScrambleVote(PlayerObject)
 */
public class CommandScramble implements CommandExecutor
{
	/**The function that is called when the command is used
	 * @param message
	 * @see https://github.com/BtoBastian/sdcf4j
	 * @see #CommandScramble
	 */
	@Command(aliases = {"!scramble"}, description = "Scramble the teams")
	public void onCommand(IMessage message)
	{
		GatherObject gather = DiscordBot.getGatherObjectForChannel(message.getChannel());
		if(gather==null) return;

		PlayerObject player = DiscordBot.players.getObject(message.getAuthor());
		if(player==null)
		{
			DiscordBot.sendMessage(gather.getCommandChannel(), "You must be **in the game** to scramble "+message.getAuthor().getDisplayName(message.getGuild())+"!");
			return;
		}
		gather.addScrambleVote(player);
	}
}