package testCases;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.*;

import static io.restassured.RestAssured.*;

import java.util.concurrent.TimeUnit;

public class ReadOneProduct {
	
	SoftAssert softAssert;
	
	String baseURI = "https://techfios.com/api-prod/api/product";
	
	String username = "demo@techfios.com";
	String password = "abc123";
	
	@Test
	public void readOneProduct() {
		
		softAssert = new SoftAssert();
		
		
		/* given: all input details (baseURI, Headers, Authorization, Payload/Body or QueryParameters)
		 * when: submit api requests (http methods, endpoint/resource)
		 * then: validate response (status code, headers, responseTime, Payload/Body)
		 * 
		 * Read one product
		 * HTTP method: GET
		 * base URI = https://techfios.com/api-prod/api/product
		 * endPoint/resource = /read_one.php
		 * headers/s:
		 * Content-Type= application/json
		 * QueryParameters:
		 * id = 5573
		 * Authorization: 
		 * basic auth = username,password
		 * StatusCode = 200
		 * */
		
		Response response = 
		
		given()
			.baseUri(baseURI)
			.header("Content-Type", "application/json")
			.auth().preemptive().basic(username, password)
			.queryParam("id", "5634").
		when()
			.get("/read_one.php"). //this also changes for read one 
		then()
			.extract().response();
		
		System.out.println("Running Read one product");
		
		int statusCode = response.getStatusCode();
		System.out.println("Status code is: " + statusCode);
		softAssert.assertEquals(statusCode, 201, "Read one product status codes are not matching");
		
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
		softAssert.assertEquals(responseHeader, "application/json2", "Read one product reponse headers are not matching!");
		
		String responseBody = response.getBody().asString();
		System.out.println("Response body: " + responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		String productName = jp.getString("name");
		System.out.println("Producct name: " + productName);
		
		String productPrice = jp.getString("price");
		System.out.println("Producct price: " + productPrice);
		softAssert.assertEquals(productPrice, "199");
		
		String productDescription = jp.getString("description");
		System.out.println("Producct description: " + productDescription);
		softAssert.assertEquals(productDescription, "The best pillow for amazing programmers.");
		
		softAssert.assertAll();
		
	}
	
}
