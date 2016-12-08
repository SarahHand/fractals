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
import com.sarahhand.fractals.model.ColorPalette;
import com.sarahhand.json.JsonReaderWriter;

/** This class is used for saving and loading ColorPalettes into JSON files.
 * 
 * @author M00031
 *
 */
public class ColorPaletteSaverLoader {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private static final ColorPaletteSaverLoader DEFAULT = new ColorPaletteSaverLoader();

	private ColorPaletteSaverLoader() {}

	public static ColorPaletteSaverLoader getDefaultColorPaletteSaverLoader() {
		return DEFAULT;
	}

	/** Loads the ColorPalette from the specified file (fileName) and
	 * returns it.
	 * 
	 * @param target
	 * @param fileName
	 * @return
	 */
	public ColorPalette load(Class<? extends ColorPalette> target, String fileName) {
		try(InputStream fis = new FileInputStream(fileName);) {
			return load(target, fis);
		} catch(IOException e) {
			log.error("IOException thrown from loading file", e);
		}
		return null;
	}

	/** Loads the ColorPalette from the specified stream and returns it.
	 * 
	 * @param target
	 * @param stream
	 * @return
	 */
	public ColorPalette load(Class<? extends ColorPalette> target, InputStream stream) {
		JsonReaderWriter readerWriter = new JsonReaderWriter(PropertyNamingStrategy.SNAKE_CASE);
		ColorPalette palette = readerWriter.<ColorPalette>read(stream, target);
		return palette;
	}

	/** Saves the specified ColorPalette to the specified file (fileName).
	 * 
	 * @param palette
	 * @param fileName
	 * @return
	 */
	public boolean save(ColorPalette palette, String fileName) {
		File file = new File(fileName);
		try {
			file.createNewFile();
		} catch (IOException e) {
			log.error("IOException saving file", e);
			return false;
		}
		JsonReaderWriter readerWriter = new JsonReaderWriter(PropertyNamingStrategy.SNAKE_CASE);
		try(OutputStream fos = new FileOutputStream(fileName);) {
			readerWriter.<ColorPalette>write(palette, fos);
		} catch(IOException ioe) {
			return false;
		}
		return true;
	}
}