package it.nathanub.RewardADs.Logic;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;

import it.nathanub.RewardADs.Main;
import it.nathanub.RewardADs.Tools.DB;
import net.md_5.bungee.api.ChatColor;

public class Events implements Listener {

	public Events(Main main) {}

	@EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        String commandFixed[] = event.getMessage().substring(1).toLowerCase().split(" ");
        Player player = event.getPlayer();
        boolean requested = false;
        boolean found = false;
        
        for(String command : Main.rewardsCommands) {
        	if(command != null) {
        		if(command.equals(commandFixed[0])) {
        			event.setCancelled(true);
        			
        			if(commandFixed.length > 1) {
        				requested = true;
        				
            			for(Map.Entry<String, Map<String, String>> entry : Main.rewards.entrySet()) {
            				Map<String, String> data = entry.getValue();
            	            
            				if(entry.getKey().toLowerCase().contains(commandFixed[1])) {
            					if(!found) {
            						new Buy(player, entry.getKey(), data);
            					}
            					
            					found = true;
            				}
            	        }
            		} else {
            			new RewardsGUI(event.getPlayer());
            		}
        		}
        	}
        }
        
        if(commandFixed[0].equals("rewardads")) {
        	event.setCancelled(true);
        	
        	if(commandFixed.length > 1) {
        		if(commandFixed[1].contains("reload")) {
        			if(player.isOp()) {
        				Main.load();
        				
        				player.sendMessage("§6Reward§eADs §asuccessful reloaded!");
        			}
        		}
        	} else {
        		player.sendMessage("§a" + new DB().getServerName() + " §7is using §6Reward§eADs");
        		player.sendMessage("§7created by §6Nathanub §7& §6Kutateki§7!");
        	}
    		
    	}
        
        if(requested && !found) player.sendMessage("§cThere are no rewards with this name!");
	 }
	
	@EventHandler
	public void onCLick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		ItemStack clickedItem = event.getCurrentItem();

		if(event.getInventory().getTitle().contains("Rewards")) {
			event.setCancelled(true);
			
			if(clickedItem != null && clickedItem.getType() != Material.AIR) {
	            String name = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
	            
	            for(Map.Entry<String, Map<String, String>> entry : Main.rewards.entrySet()) {
	    			Map<String, String> data = entry.getValue();
	                
	                if(entry.getKey().contains(name)) {
	                	new Buy(player, name, data);
	                }
	            }
	        }
		}
	}
}
