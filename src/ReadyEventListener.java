import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.ReadyEvent;

public class ReadyEventListener implements IListener<ReadyEvent>
{

	@Override
	public void handle(ReadyEvent event)
	{
		/*List<IGuild> guilds = event.getClient().getGuilds();
		if(guilds != null && guilds.size()>0)
		{
			for(IGuild guild : guilds)
			{
				DiscordBot.addGuild(guild);
			}
		}*/
		try {
			Gson gson = new Gson();
			JsonReader reader;
			reader = new JsonReader(new FileReader("servers.json"));
			GatherObject obj = gson.fromJson(reader, GatherObject.class);
			obj.setDiscordObjects();
			DiscordBot.gatherObjects.add(obj);
			obj.connectToKAGServers();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		//just get the first gather object for now to set the playing text
		//TODO: playing text wont really work if there was ever multiple servers
		Iterator<GatherObject> itr = DiscordBot.gatherObjects.iterator();
		GatherObject gather = itr.next();
		if(gather == null) return;
		DiscordBot.setPlayingText(gather.numPlayersInQueue()+"/"+gather.getMaxQueueSize()+" in queue");
		DiscordBot.setChannelCaption(gather.getGuild() , gather.numPlayersInQueue()+"-in-q");
		
		
	}

}