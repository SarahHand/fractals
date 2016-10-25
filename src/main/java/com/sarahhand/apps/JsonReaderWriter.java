package com.sarahhand.apps;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sarahhand.apps.model.Contact;

public class JsonReaderWriter{
	
	private ObjectMapper mapper = new ObjectMapper();
	
	public JsonReaderWriter(PropertyNamingStrategy pns){
		mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapper.setPropertyNamingStrategy(pns);
	}
	
	public <T> T read(String fileName, Class<? extends T> target) {
		try (InputStream stream = this.getClass().getResourceAsStream(fileName)){
			T object = mapper.readValue(stream, target);
			return object;
		} catch (IOException e) {
			System.err.println("Error reading contact from file=" + fileName);
			e.printStackTrace();
		}
		return null;
	}
	
	public <T> void write(T object, OutputStream output){
		try{
			mapper.writeValue(output, object);
		} catch (IOException e){
			System.err.println("Error writing contact to file");
			e.printStackTrace();
		}
	}
}
