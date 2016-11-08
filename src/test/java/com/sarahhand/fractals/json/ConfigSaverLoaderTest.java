package com.sarahhand.fractals.json;

import static org.junit.Assert.assertThat;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.sarahand.fractals.json.ConfigSaverLoader;
import com.sarahhand.fractals.model.MandelbrotConfig;

public class ConfigSaverLoaderTest{

	@Test
	public void saveTest(){
		
		//Arrange
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Filename:");
		
		String filename = scan.nextLine();
		ConfigSaverLoader saverLoader = ConfigSaverLoader.getDefaultConfigSaverLoader();
		scan.close();

		//Act
		saverLoader.save(MandelbrotConfig.DEAFAULT_CONFIG, filename);
	}
	
	@Test
	public void loadTest(){
		
		//Arrange
		String fileName = "C:\\Users\\jhand\\Desktop\\Test.json";
		ConfigSaverLoader saverLoader = ConfigSaverLoader.getDefaultConfigSaverLoader();

		//Act
		MandelbrotConfig config = null;
		try (InputStream stream = new FileInputStream(fileName)){
			config = (MandelbrotConfig)saverLoader.load(MandelbrotConfig.class, stream);
		} catch (IOException e) {
			System.err.println("Error reading contact from file=" + fileName);
			e.printStackTrace();
		}
		
		//Assert
		assertThat(config.getCenter(), Matchers.equalTo(MandelbrotConfig.DEAFAULT_CONFIG.getCenter()));
		assertThat(config.getZoom(), Matchers.equalTo(MandelbrotConfig.DEAFAULT_CONFIG.getZoom()));
		assertThat(config.getMaxDwell(), Matchers.equalTo(MandelbrotConfig.DEAFAULT_CONFIG.getMaxDwell()));
	}
}
