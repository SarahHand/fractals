package com.sarahhand.apps;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import junit.framework.TestCase;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase{
    
	@Test
	public void testApp_happyPath(){
		//Arrange
		String testName = "happyPath";
		
		//Act
		App app = new App(testName);
		
		//Assert
		assertThat(app, notNullValue());
		assertThat(app.getName(), equalTo("hello"));
	}
}
