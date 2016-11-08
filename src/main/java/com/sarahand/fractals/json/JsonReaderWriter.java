package com.sarahand.fractals.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;

class JsonReaderWriter{
	
	private ObjectMapper mapper = new ObjectMapper();
	
	public JsonReaderWriter(PropertyNamingStrategy pns){
		mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapper.setPropertyNamingStrategy(pns);
	}
	
	public <T> T read(InputStream stream, Class<? extends T> target) {
		
		T object = null;
		try {
			object = mapper.readValue(stream, target);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return object;
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
