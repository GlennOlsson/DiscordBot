package commands;

import net.dv8tion.jda.core.entities.*;

public class Source {
	public Source(MessageChannel channel){
		channel.sendMessage("Nice, you are qurious! Here's the link to github: https://github.com/kakan9898/DiscordBot").queue();
		return;
	}

	
}
