package com.sarahhand.fractals.json;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.sarahhand.fractals.model.FractalConfig;
import com.sarahhand.fractals.model.FractalEnvelope;
import com.sarahhand.json.JsonReaderWriter;

/** This class is used to save and load FractalConfigs to a JSON file.
 * 
 * @author M00031
 *
 */
public class ConfigSaverLoader{

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private static final ConfigSaverLoader DEFAULT = new ConfigSaverLoader();

	private ConfigSaverLoader(){}

	/**
	 * Returns the default ConfigSaverLoader.
	 * @return
	 */
	public static ConfigSaverLoader getDefaultConfigSaverLoader(){
		return DEFAULT;
	}

	/**
	 * Loads the fractal config from the specified file (fileName) and
	 * returns it. 
	 * @param target
	 * @param fileName
	 * @return
	 */
	public FractalConfig load(String fileName){

		try(InputStream fis = new FileInputStream(fileName);) {
			return load(fis);
		} catch(IOException e) {
			log.error("IOException thrown from loading file", e);
		}

		return null;
	}

	/**
	 * Loads the fractal config from the specified stream and returns it.
	 * 
	 * @param target
	 * @param stream
	 * @return
	 */
	public FractalConfig load(InputStream stream){

		JsonReaderWriter readerWriter = new JsonReaderWriter(PropertyNamingStrategy.SNAKE_CASE);

		FractalEnvelope envelope = readerWriter.read(stream, FractalEnvelope.class);
		if(envelope != null) {
			return envelope.getConfig();
		}
		return null;
	}

	/**
	 * Saves the specified fractal config to the specified file (fileName).
	 * @param config
	 * @param fileName
	 * @return
	 */
	public boolean save(String fileName, FractalConfig config){

		File file = new File(fileName);

		FractalEnvelope envelope = new FractalEnvelope();
		envelope.setConfig(config);

		try {
			file.createNewFile();
		} catch (IOException e) {
			log.error("IOException saving file", e);
			return false;
		}

		JsonReaderWriter readerWriter = new JsonReaderWriter(PropertyNamingStrategy.SNAKE_CASE);

		try (OutputStream fos = new FileOutputStream(fileName);){

			readerWriter.write(envelope, fos);
		}catch (IOException e1){
			// ^^^^^ this will never happen because of the previous try-catch statement...
			return false; // ...but just in case...
		}

		return true;
	}
}