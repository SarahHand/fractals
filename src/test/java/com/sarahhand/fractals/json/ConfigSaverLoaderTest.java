package com.sarahhand.fractals.json;

import static org.junit.Assert.assertThat;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.sarahhand.fractals.json.ConfigSaverLoader;
import com.sarahhand.fractals.mandelbrotset.MandelbrotConfig;

/**
 * Test ConfigSaverLoader
 * <p>
 * <b>Cases:<b><ul>
 * <li>Saving
 * <li>Loading
 * @author J9465812
 *
 */
@SuppressWarnings("javadoc")
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
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Filename:");
		String filename = scan.nextLine();
		ConfigSaverLoader saverLoader = ConfigSaverLoader.getDefaultConfigSaverLoader();

		//Act
		MandelbrotConfig config = null;
		try (InputStream stream = new FileInputStream(filename)){
			config = (MandelbrotConfig)saverLoader.load(MandelbrotConfig.class, stream);
		} catch (IOException e) {
			System.err.println("Error reading contact from file=" + filename);
			e.printStackTrace();
		}
		
		//Assert
		assertThat(config.getCenter(), Matchers.equalTo(MandelbrotConfig.DEAFAULT_CONFIG.getCenter()));
		assertThat(config.getZoom(), Matchers.equalTo(MandelbrotConfig.DEAFAULT_CONFIG.getZoom()));
		assertThat(config.getMaxDwell(), Matchers.equalTo(MandelbrotConfig.DEAFAULT_CONFIG.getMaxDwell()));
		
		scan.close();
	}
}
