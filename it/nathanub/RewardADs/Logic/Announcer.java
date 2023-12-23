package it.nathanub.RewardADs.Logic;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import it.nathanub.RewardADs.Main;
import it.nathanub.RewardADs.Tools.DB;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Announcer extends BukkitRunnable {

	@Override
    public void run() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			for(String message : Main.announceMessages) {
				if(message != null) {
					if(!message.contains("(button)")) {
						player.sendMessage(message);
					} else {
						String result = removeSpacesBeforeFirstLetter(message.replace("(button)", ""));
						TextComponent clickableMessage = new TextComponent(result);

						clickableMessage.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://rewardads.github.io/" + new DB().getServerName() + "/?player=" + player.getDisplayName()));
			            player.spigot().sendMessage(clickableMessage);
					}
				}
			}
		}
    }
	
	private static String removeSpacesBeforeFirstLetter(String input) {
        return input.replaceAll("^\\s+", "");
    }
}
