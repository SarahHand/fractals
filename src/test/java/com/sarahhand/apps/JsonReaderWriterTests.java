package com.sarahhand.apps;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.sarahhand.apps.model.Address;
import com.sarahhand.apps.model.Contact;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonReaderWriterTests{
	
	@Test
	public void readerTest(){

		//Arrange
		String filename = "Contact.json";
		JsonReaderWriter jrw = new JsonReaderWriter(PropertyNamingStrategy.SNAKE_CASE);

		//Act
		Contact testContact = jrw.read(filename, Contact.class);

		//Assert
		assertThat(testContact, notNullValue());
		assertThat(testContact.getAddresses(), notNullValue());
		assertThat(testContact.getPhoneNumbers(), notNullValue());
		assertThat(testContact.getFirstName(), equalTo("Roger"));
		assertThat(testContact.getAddresses(), hasItem(Matchers.<Address> hasProperty("zipCode", equalTo("98004"))));
		assertThat(testContact.getAddresses().size(), equalTo(1));
		assertThat(testContact.getPhoneNumbers().size(), equalTo(2));
	}
	
	@Test
	public void writerTest(){
		
		//Arrange
		String filename = "Contact.json";
		JsonReaderWriter jrw = new JsonReaderWriter(PropertyNamingStrategy.SNAKE_CASE);
		Contact testContact = jrw.read(filename, Contact.class);
		
		//Act
		jrw.write(testContact, System.out);
	}
}
