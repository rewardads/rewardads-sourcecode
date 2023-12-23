package it.nathanub.RewardADs.Tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.nathanub.RewardADs.Main;

public class ReadInformations {

	public ReadInformations() {
		File configuration = new File("plugins/RewardADs/configuration.yml");
		File rewards = new File("plugins/RewardADs/rewards.yml");
		
		try(Scanner configurationReader = new Scanner(configuration)) {
			while(configurationReader.hasNextLine()) {
	            String line = configurationReader.nextLine();
	            
	            if(line.startsWith("#")) return;
	            
	            if(line.startsWith("code")) {
	            	Main.code = removeSpaces(line.replace("code:", "")).toLowerCase();
	            } else if(line.contains("announce-message")) {
	            	 String[] messages = line.replace("announce-messages:", "").split("&&");
	            	 
	            	 if(messages != null) {
	            		 for(String message : messages) {
	            			 addString(Main.announceMessages, removeSpaces(message.replace('"' + "", "").replace("&", "§")));
	            		 }
	            	 } else {
	            		 new Error("configuration.yml", "no announce-messages provided!");
	            	 }
	            } else if(line.contains("announce-delay")) {
	            	Pattern pattern = Pattern.compile("\\d+");
	                Matcher matcher = pattern.matcher(line.replace("announce-delay:", ""));

	                if(matcher.find()) {
	                    Main.announcesDelay = Integer.parseInt(matcher.group());
	                } else {
	                	new Error("configuration.yml", "non-valid announce-delay provided!"); 
	                }
	            } else if(line.contains("announcer-enabled")) {
	            	String lineFixed = line.replace("announce-enabled:", "");
	            	
	            	if(lineFixed.contains("true")) {
	            		Main.announcerEnabled = true;
	            	} else if(lineFixed.contains("false")) {
	            		Main.announcerEnabled = false;
	            	} else {
	            		new Error("configuration.yml", "only true and false are valid in announcer-enabled!"); 
	            	}
	            } else if(line.startsWith("earn-per-ad")) {
	            	Pattern pattern = Pattern.compile("\\d+");
	                Matcher matcher = pattern.matcher(line.replace("earn-per-ad:", ""));

	                if(matcher.find()) {
	                    Main.earnPerAd = Integer.parseInt(matcher.group());
	                } else {
	                	new Error("configuration.yml", "non-valid earn-per-ad provided!"); 
	                }
	            } else if(line.startsWith("rewards-commands")) {
	            	String[] commands = line.replace("rewards-commands:", "").split("&&");
	            	
	            	if(commands != null) {
		            	for(String command : commands) {
		            		addString(Main.rewardsCommands, removeSpaces(command.replace('"' + "", "").replace("&", "§")));
		            	}
	            	} else {
	            		new Error("configuration.yml", "no rewards-commands provided!");
	            	}
	            }
	        }
			
			configurationReader.close();
			
			Scanner rewardsReader = new Scanner(rewards);
			StringBuilder content = new StringBuilder();
			
			while(rewardsReader.hasNextLine()) {
	            String line = rewardsReader.nextLine();
	            content.append(line).append("\n");
	        }
			
			Main.rewards = parseConfig(content + "");
			
			rewardsReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static Map<String, Map<String, String>> parseConfig(String config) {
        Map<String, Map<String, String>> result = new HashMap<>();
        String[] lines = config.split("\n");

        String currentReward = null;
        Map<String, String> currentRewardData = null;

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }

            if (line.matches("\\w+:")) {
                if (currentReward != null) {
                    result.put(currentReward, currentRewardData);
                }
                currentReward = line.substring(0, line.length() - 1);
                currentRewardData = new HashMap<>();
            } else {
                String[] parts = line.trim().split(": ");
                if (parts.length == 2) {
                    currentRewardData.put(parts[0], parts[1]);
                }
            }
        }

        if (currentReward != null) {
            result.put(currentReward, currentRewardData);
        }

        return result;
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
	
	private static void addString(String[] array, String newString) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null || array[i].isEmpty()) {
                array[i] = newString;
                return;
            }
        }

        System.out.println("Array is full. Cannot add more elements.");
    }
}
