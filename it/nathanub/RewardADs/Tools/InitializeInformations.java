package it.nathanub.RewardADs.Tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class InitializeInformations {

	public InitializeInformations() {
		File folder = new File("plugins/RewardADs");
		File configuration = new File("plugins/RewardADs/configuration.yml");
		InputStream configurationInternal = InitializeInformations.class.getResourceAsStream("/configs/configuration.yml");
		File rewards = new File("plugins/RewardADs/rewards.yml");
		InputStream rewardsInternal = InitializeInformations.class.getResourceAsStream("/configs/rewards.yml");
		StringBuilder configurationContent = new StringBuilder();
		StringBuilder rewardsContent = new StringBuilder();

        if(!folder.exists()) {
            folder.mkdirs();
        }

        try {
        	Scanner configurationReader = new Scanner(configurationInternal);

            while(configurationReader.hasNextLine()) {
                String line = configurationReader.nextLine();
                configurationContent.append(line).append("\n");
            }
            
            configurationReader.close();
            
            Scanner rewardsReader = new Scanner(rewardsInternal);

            while(rewardsReader.hasNextLine()) {
                String line = rewardsReader.nextLine();
                rewardsContent.append(line).append("\n");
            }
            
            rewardsReader.close();

            if(!configuration.exists()) {
            	configuration.createNewFile();
            	
            	FileWriter configurationWriter = new FileWriter(configuration);
                configurationWriter.write(configurationContent + "");
                configurationWriter.close();
            }
            
            if(!rewards.exists()) {
            	rewards.createNewFile();
            	
            	FileWriter rewardsWriter = new FileWriter(rewards);
                rewardsWriter.write(rewardsContent + "");
                rewardsWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
