package it.nathanub.RewardADs;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import it.nathanub.RewardADs.Logic.Announcer;
import it.nathanub.RewardADs.Logic.Events;
import it.nathanub.RewardADs.Tools.DB;
import it.nathanub.RewardADs.Tools.InitializeInformations;
import it.nathanub.RewardADs.Tools.ReadInformations;

public class Main extends JavaPlugin {
	private static Main instance;
	public static String code = null;
	public static String[] announceMessages = new String[10];
	public static int announcesDelay = 0;
	public static boolean announcerEnabled = false;
	public static int earnPerAd = 0;
	public static String[] rewardsCommands = new String[10];
	public static Map<String, Map<String, String>> rewards = new HashMap<String, Map<String, String>>();

	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {
		instance = this;
		
		getLogger().info("Plugin RewardADs enabled!");
		getServer().getPluginManager().registerEvents(new Events(this), this);
		
		new InitializeInformations();
		new ReadInformations();
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Announcer(), 10, 20 * announcesDelay);
		
		new DB().getServerName();
		new DB().setEarnPerAD();
	}
	
	public static void load() {
		new InitializeInformations();
		new ReadInformations();
		
		new DB().getServerName();
		new DB().setEarnPerAD();
	}
	
	public static Main getInstance() {
        return instance;
    }
}
