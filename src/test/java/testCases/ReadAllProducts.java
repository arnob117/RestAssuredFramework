package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.*;

import static io.restassured.RestAssured.*;

import java.util.concurrent.TimeUnit;

public class ReadAllProducts {
	
	String baseURI = "https://techfios.com/api-prod/api/product";
	
	String username = "demo@techfios.com";
	String password = "abc123";
	
	@Test
	public void readAllProducts() {
		
		
		/* given: all input details (baseURI, Headers, Authorization, Payload/Body or QueryParameters)
		 * when: submit api requests (http methods, endpoint/resource)
		 * then: validate response (status code, headers, responseTime, Payload/Body)
		 * 
		 * HTTP method: GET
		 * base URI = https://techfios.com/api-prod/api/product
		 * endPoint/resource = /read.php
		 * headers/s:
		 * Content-Type= application/json; charset = UTF-8
		 * Authorization: 
		 * baseic auth = username,password
		 * StatusCode = 200
		 * */
		
		Response response = 
		
		given()
			.baseUri(baseURI)
			.header("Content-Type", "application/json; charset=UTF-8")
			.auth().preemptive().basic(username, password).
		when()
			.get("/read.php").
		then()
			.extract().response();
		
		int responseCode = response.getStatusCode();
		System.out.println("Response code is: " + responseCode);
		Assert.assertEquals(responseCode, 200);
		
		long responseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
		System.out.println("Response time: " + responseTime);
		if (responseTime <= 2000) {
			System.out.println("Response time within range.");
		}
		else {
			System.out.println("Response time out of range");
		}
			
		String responseHeader = response.getHeader("Content-Type");
		System.out.println("Response Header: " + responseHeader);
		Assert.assertEquals(responseHeader, "application/json; charset=UTF-8");
		
		//connection error: sqlstate[HY000]
		String responseBody = response.getBody().asString();
		//System.out.println("Response body: " + responseBody); (crashes the laptop)
		
		JsonPath jp = new JsonPath(responseBody);
		String firstProductId = jp.getString("records[0].id");
		System.out.println("First producct ID: " + firstProductId);
		
		if (firstProductId != null) {
			
			System.out.println("Response Body contains first product ID.");
		}
		else {
			
			System.out.println("Resposne body does not contain first product ID.");
		}
		
	}
	
}
