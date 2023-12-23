package it.nathanub.RewardADs.Logic;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import it.nathanub.RewardADs.Tools.DB;
import it.nathanub.RewardADs.Tools.Error;

public class Buy {

	protected Buy(Player player, String reward, Map<String, String> data) {
		int cost = 0;
		
    	for(Map.Entry<String, String> dataEntry : data.entrySet()) {
    		if(dataEntry.getKey().contains("cost")) {
    			cost = Integer.parseInt(dataEntry.getValue());
    		} else if(dataEntry.getKey().contains("commands")) {
    			if(new DB().getCoins(player) >= cost) {
    				String[] commands = dataEntry.getValue().split("&&");
                	
                	if(commands != null) {
    	            	for(String command : commands) {
    	            		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), removeSpaces(command.replace('"' + "", "").replace("&", "§").replace("%playername%", player.getDisplayName()).replace("%playeruuid%", player.getUniqueId() + "").replace("%money%", new DB().getCoins(player) + "")));
    	            	}
                	} else {
                		new Error("configuration.yml", "no rewards-commands provided!");
                	}
    			}
    		}
    	}
    	
    	System.out.println(cost);
    	
    	if(new DB().getCoins(player) >= cost) {
    		Inventory openInventory = player.getOpenInventory().getTopInventory();
    		
    		new DB().setCoins(player, new DB().getCoins(player) - cost);
    		
    		if(openInventory.getTitle().equals("Rewards")) {
    			player.closeInventory();
    			
    			new RewardsGUI(player);
    		}
    		
    		player.sendMessage("§aYou've bought §6" + reward + "§a!");
    	} else {
    		player.closeInventory();
    		player.sendMessage("§cYou haven't got enough money!");
    	}
	}
	
	private static String removeSpaces(String input) {
		String trimmed = input.trim();

	    int firstNonSpaceIndex = 0;
	    while (firstNonSpaceIndex < trimmed.length() && Character.isWhitespace(trimmed.charAt(firstNonSpaceIndex))) {
	        firstNonSpaceIndex++;
	    }

	    int lastNonSpaceIndex = trimmed.length() - 1;
	    while (lastNonSpaceIndex > firstNonSpaceIndex && Character.isWhitespace(trimmed.charAt(lastNonSpaceIndex))) {
	        lastNonSpaceIndex--;
	    }

	    String result = trimmed.substring(firstNonSpaceIndex, lastNonSpaceIndex + 1);
	    return result;
    }
}
