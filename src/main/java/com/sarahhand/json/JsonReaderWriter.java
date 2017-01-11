package com.sarahhand.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sarahhand.fractals.model.FractalConfig;
import com.sarahhand.fractals.model.FractalEnvelope;

/** This class is used to read and write to JSON files.
 * 
 * @author M00031
 *
 */
public class JsonReaderWriter{
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private ObjectMapper mapper = new ObjectMapper();

	/**
	 * The constructor.
	 * @param pns
	 */
	public JsonReaderWriter(PropertyNamingStrategy pns){
		mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapper.setPropertyNamingStrategy(pns);
	}

	/** This creates an object out using a JSON file.
	 * 
	 * @param stream
	 * @param target
	 * @return
	 */
	public <T> T read(InputStream stream, Class<? extends T> target) {
		try {
			T object = mapper.readValue(stream, target);
			return object;
		} catch (IOException e) {
			log.error("Error reading JSON from stream.", e);
		}
		return null;
	}

	/** This creates a JSON file using an object.
	 * 
	 * @param object
	 * @param output
	 */
	public <T> void write(T object, OutputStream output){
		try{
			mapper.writeValue(output, object);
		} catch (IOException e){
			log.error("Error writing JSON to stream.", e);
		}
	}

	/** This creates an object out using a JSON file.
	 * 
	 * @param stream
	 * @param target
	 * @return
	 */
	public FractalEnvelope read(InputStream stream) {
		try {
			FractalEnvelope envelope = new FractalEnvelope();
			FractalConfig newConfig = mapper.readValue(stream, FractalConfig.class);
			envelope.setConfig(newConfig);
			return envelope;
		} catch (IOException e) {
			log.error("Error reading JSON from stream.", e);
		}
		return null;
	}

	/** This creates a JSON file using an object.
	 * 
	 * @param object
	 * @param output
	 */
	public void write(OutputStream output, FractalEnvelope envelope){
		try{
			mapper.writeValue(output, envelope.getConfig().getClass());
		} catch (IOException e){
			log.error("Error writing JSON to stream.", e);
		}
	}
}