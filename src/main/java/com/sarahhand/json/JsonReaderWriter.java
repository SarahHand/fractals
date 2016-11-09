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

public class JsonReaderWriter{
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private ObjectMapper mapper = new ObjectMapper();
	
	public JsonReaderWriter(PropertyNamingStrategy pns){
		mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapper.setPropertyNamingStrategy(pns);
	}
	
	public <T> T read(InputStream stream, Class<? extends T> target) {
		try {
			T object = mapper.readValue(stream, target);
			return object;
		} catch (IOException e) {
			log.error("Error reading JSON from stream.", e);
		}
		return null;
	}
	
	public <T> void write(T object, OutputStream output){
		try{
			mapper.writeValue(output, object);
		} catch (IOException e){
			log.error("Error writing JSON to stream.", e);
		}
	}
}
