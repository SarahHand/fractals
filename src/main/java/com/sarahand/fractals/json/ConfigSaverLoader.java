package com.sarahand.fractals.json;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.sarahhand.fractals.model.Config;
import com.sarahhand.json.JsonReaderWriter;

/** This class is used to save and load mandelbrotConfigs to a JSON file.
 * 
 * @author M00031
 *
 */
public class ConfigSaverLoader{

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private static final ConfigSaverLoader DEFAULT = new ConfigSaverLoader();

	private ConfigSaverLoader(){}

	public static ConfigSaverLoader getDefaultConfigSaverLoader(){
		return DEFAULT;
	}

	/** This method loads a mandelbrotConfig from a JSON file using a Config and a String.
	 * It creates an InputStream and passes this into the other load() method.
	 * 
	 * @param config
	 * @param fileName
	 * @return
	 */
	public Config load(Config config, String fileName){

		try(InputStream fis = new FileInputStream(fileName);) {
			return load(config.getClass(), fis);
		} catch(IOException e) {
			log.error("IOException thrown from loading file", e);
		}
		
		return config;
	}

	/** This method is the same as the previous method except that it takes a class and and InputStream.
	 * 
	 * @param target
	 * @param stream
	 * @return
	 */
	public Config load(Class<? extends Config> target, InputStream stream){

		JsonReaderWriter readerWriter = new JsonReaderWriter(PropertyNamingStrategy.SNAKE_CASE);

		Config config = readerWriter.<Config>read(stream, target);

		return config;
	}

	/** This method saves a mandelbrotConfig to a JSON file.
	 * 
	 * @param config
	 * @param fileName
	 * @return
	 */
	public boolean save(Config config, String fileName){

		File file = new File(fileName);

		try {
			file.createNewFile();
		} catch (IOException e) {
			log.error("IOException saving file", e);
			return false;
		}

		JsonReaderWriter readerWriter = new JsonReaderWriter(PropertyNamingStrategy.SNAKE_CASE);

		try (OutputStream fos = new FileOutputStream(fileName);){

			readerWriter.<Config>write(config, fos);
		}catch (IOException e1){
			// ^^^^^ this will never happen because of line 42...
			return false; // ...but just in case...
		}

		return true;
	}
}
