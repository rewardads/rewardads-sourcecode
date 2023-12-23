package it.nathanub.RewardADs.Logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import it.nathanub.RewardADs.Main;
import it.nathanub.RewardADs.Tools.DB;
import it.nathanub.RewardADs.Tools.Error;

public class RewardsGUI {

	public RewardsGUI(Player player) {
		Inventory inventory = Bukkit.createInventory(null, 54, "Rewards");
		int i = 0;

		for(Map.Entry<String, Map<String, String>> entry : Main.rewards.entrySet()) {
			Map<String, String> data = entry.getValue();
			List<String> lore = new ArrayList<>();
			ItemStack item = null;
            
        	for(Map.Entry<String, String> dataEntry : data.entrySet()) {
        		String element = dataEntry.getKey();
        		
        		if(element.contains("material")) {
        			Material material = Material.getMaterial(dataEntry.getValue());
        			
        			if(material == null) new Error("rewards.yml", "in " + entry.getKey() + ", non-valid material!");
        			
        			item = new ItemStack(material);
        		} else if(element.contains("lore")) {
        			String[] lores = dataEntry.getValue().split("&&");
	            	
	            	if(lores != null) {
		            	for(String Lore : lores) {
		            		lore.add(removeSpaces(Lore.replace('"' + "", "").replace("&", "§").replace("%money%", new DB().getCoins(player) + "")));
		            	}
	            	} else {
	            		new Error("configuration.yml", "no lores provided!");
	            	}
        		}
        	}
        	
        	if(item == null) new Error("rewards.yml", "in " + entry.getKey() + ", no material provided!");
        	
        	ItemMeta meta = item.getItemMeta();
        	
        	meta.setLore(lore);
            meta.setDisplayName("§e" + entry.getKey());
            item.setItemMeta(meta);
        	inventory.setItem(i, item);
        	
        	i++;
        }
		
		player.openInventory(inventory);
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
