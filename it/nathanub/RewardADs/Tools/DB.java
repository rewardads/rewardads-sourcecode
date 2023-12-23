package it.nathanub.RewardADs.Tools;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import it.nathanub.RewardADs.Main;

public class DB {
	public void setCoins(Player player, int coins) {
		Map<String, Map<String, String>> config = parseConfig(fetchDatabase("coins"));
		
		for(Map.Entry<String, Map<String, String>> entry : config.entrySet()) {
			Map<String, String> data = entry.getValue();
            
            if(entry.getKey().contains(player.getDisplayName())) {
            	for(Map.Entry<String, String> dataEntry : data.entrySet()) {
            		if(dataEntry.getKey().contains(Main.code)) {
            			data.put(Main.code, coins + "");
            		}
            	}
            }
        }
		
		writeDatabase("coins", writeConfig(config));
	}
	
	public int getCoins(Player player) {
		Map<String, Map<String, String>> config = parseConfig(fetchDatabase("coins"));
		int coins = 0;
		
		for(Map.Entry<String, Map<String, String>> entry : config.entrySet()) {
			Map<String, String> data = entry.getValue();
            
			if(entry.getKey().contains(player.getDisplayName())) {
            	for(Map.Entry<String, String> dataEntry : data.entrySet()) {
            		if(dataEntry.getKey().contains(Main.code)) {
            			coins = Integer.parseInt(dataEntry.getValue());
            		}
            	}
            }
        }
		
		return coins;
	}
	
	public void setEarnPerAD() {
		Map<String, Map<String, String>> config = parseConfig(fetchDatabase("servers"));
		
		for(Map.Entry<String, Map<String, String>> entry : config.entrySet()) {
			Map<String, String> data = entry.getValue();
            
            if(entry.getKey().contains(Main.code)) {
            	for(Map.Entry<String, String> dataEntry : data.entrySet()) {
            		if(dataEntry.getKey().contains("coins-per-ad")) {
            			data.put("coins-per-ad", Main.earnPerAd + "");
            		}
            	}
            }
        }
		
		writeDatabase("servers", writeConfig(config));
	}
	
	public String getServerName() {
		String nameServer = null;
		
		for(Map.Entry<String, Map<String, String>> entry : parseConfig(fetchDatabase("servers")).entrySet()) {
			Map<String, String> data = entry.getValue();
            
            if(entry.getKey().contains(Main.code)) {
            	for(Map.Entry<String, String> dataEntry : data.entrySet()) {
            		if(dataEntry.getKey().contains("name")) {
            			nameServer = dataEntry.getValue();
            		}
            	}
            }
        }
		
		if(nameServer == null) new Error("configuration.yml", "code is not valid!");
		
		return nameServer;
	}
	
	private void writeDatabase(String databaseName, String config) {
        try {
        	String url = "http://our.site.it";
        	String password = "super-secret-password";
            String parameters = databaseName + "=" + config + "&password=" + password;

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            byte[] postData = parameters.getBytes(StandardCharsets.UTF_8);

            con.setRequestProperty("Content-Length", String.valueOf(postData.length));

            try(DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postData);
            }

            con.getResponseCode();
        } catch (IOException e) {
        	new Error("Internal Error", "I cannot write to our database becouse they are down!");
        }
	}
	
	private String fetchDatabase(String databaseName) {
		StringBuilder content = new StringBuilder();
		
		try {
			String url = "http://our.site.it/database/" + databaseName + ".txt";
	        URL websiteUrl = new URL(url);
	        HttpURLConnection connection = (HttpURLConnection) websiteUrl.openConnection();
	
	        connection.setRequestMethod("GET");
	
	        int responseCode = connection.getResponseCode();
	
	        if(responseCode == HttpURLConnection.HTTP_OK) {
	            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	            String line;
	
	            while ((line = reader.readLine()) != null) {
	                content.append(line).append("\n");
	            }
	
	            reader.close();
	        } else {
	            new Error("Internal Error", "I cannot connect to our database becouse they are down!");
	        }
		} catch (Exception e) {
			new Error("Internal Error", "I cannot connect to our database becouse they are down!");
        }
		
		return content + "";
	}
	
	private static String writeConfig(Map<String, Map<String, String>> configData) {
	    StringBuilder result = new StringBuilder();

	    for(Map.Entry<String, Map<String, String>> entry : configData.entrySet()) {
	        String rewardName = entry.getKey();
	        Map<String, String> rewardData = entry.getValue();

	        result.append(rewardName).append(":\n");

	        for(Map.Entry<String, String> dataEntry : rewardData.entrySet()) {
	            String key = dataEntry.getKey();
	            String value = dataEntry.getValue();

	            result.append("  ").append(key).append(": ").append(value).append("\n");
	        }

	        result.append("\n");
	    }

	    return result.toString();
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
}
