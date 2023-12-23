package it.nathanub.RewardADs.Tools;

import org.bukkit.Bukkit;

import it.nathanub.RewardADs.Main;

public class Error {

	public Error(String where, String error) {
		Bukkit.getServer().getPluginManager().disablePlugin(Main.getInstance());
		Main.getInstance().getLogger().severe("At " + where + ", " + error);
	}
}
