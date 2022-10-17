package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.*;

import static io.restassured.RestAssured.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CreateOneProduct {
	
	String baseURI = "https://techfios.com/api-prod/api/product";
	String payLoadFilePath = "src\\main\\java\\data\\createPayload.json";
	HashMap<String,String> createPayload; //we are using map to store data used to create new product
	
	String username = "demo@techfios.com";
	String password = "abc123";
	String firstProductId;
	String readOneProductId;
	
	public Map<String,String> createPayloadMap() {
		
		createPayload = new HashMap<String,String>();
		
		createPayload.put("name", "Amazing Pillow 2.0 By MD");
		createPayload.put("price", "199");
		createPayload.put("description", "The best pillow for amazing programmers");
		createPayload.put("category_id", "2");
		createPayload.put("category_name", "Electronics");
		
		return createPayload;
		
	} //this creates a new map with the payload data used to create a new product
	
	
	@Test(priority=1)
	public void createOneProduct() {
		
		
		/* given: all input details (baseURI, Headers, Authorization, Payload/Body or QueryParameters)
		 * when: submit api requests (http methods, endpoint/resource)
		 * then: validate response (status code, headers, responseTime, Payload/Body)
		 * 
		 * Create one product
		 * HTTP method: POST
		 * base URI = https://techfios.com/api-prod/api/product
		 * endPoint/resource = /create.php
		 * headers/s:
		 * Content-Type= application/json; charset = UTF-8
		 * QueryParameters:
		 * id = 5573
		 * Authorization: 
		 * basic auth = username,password
		 * StatusCode = 201
		 * 
		 * payload/body:
		 * "name": "Amazing Pillow 2.0 by MD
		 * "price": "199"
		 * "description": "The best pillow for amazing programmers."
		 * "category_id": 2
		 * "category_name": "Electronics"
		 * 
		 * CreateOneProduct
		 * Validate (StatusCode = 201, header(Content-Type=application/json; charset=UTF8, responseTime, responseBody)
		 * */
		
		Response response = 
		
		given()
			.baseUri(baseURI)
			.header("Content-Type", "application/json; charset = UTF-8")
			.auth().preemptive().basic(username, password)
			.body(createPayloadMap()).
		when()
			.post("/create.php"). //this is now post and cretae
		then()
			.extract().response();
		
		System.out.println("Running Read one product");
		
		int statusCode = response.getStatusCode();
		System.out.println("Status code is: " + statusCode);
		Assert.assertEquals(statusCode, 201);
		
		long responseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
		System.out.println("Response time: " + responseTime);
		if (responseTime <= 2000) {
			System.out.println("Response time within range.");
		}
		else {
			System.out.println("Response time out of range");
		}
		
		//this is validating that the product was created
		String responseHeader = response.getHeader("Content-Type");
		System.out.println("Response Header: " + responseHeader);
		Assert.assertEquals(responseHeader, "application/json; charset=UTF-8");
		
		//connection error: sqlstate[HY000]
		String responseBody = response.getBody().asString();
		System.out.println("Response body: " + responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		String productMessage = jp.getString("message");
		System.out.println("Product Message: " + productMessage);
		Assert.assertEquals(productMessage, "Product was created.");
		
	}
	
	@Test(priority=2)
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
		
		//connection error: sqlstate[HY000]
		String responseBody = response.getBody().asString();
		//System.out.println("Response body: " + responseBody); (crashes the laptop)
		
		JsonPath jp = new JsonPath(responseBody);
		firstProductId = jp.getString("records[0].id");
		System.out.println("First producct ID: " + firstProductId);
		
		if (firstProductId != null) {
			
			System.out.println("Response Body contains first product ID.");
		}
		else {
			
			System.out.println("Resposne body does not contain first product ID.");
		}
		
	}
	
	@Test(priority=3)
	public void readOneProduct() {//we cant pass string with a test anntation
		
		readOneProductId = firstProductId;
		
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
			.queryParam("id", readOneProductId).
		when()
			.get("/read_one.php"). //this also changes for read one 
		then()
			.extract().response();
		
		System.out.println("Running Read one product");
		
		int statusCode = response.getStatusCode();
		System.out.println("Status code is: " + statusCode);
		Assert.assertEquals(statusCode, 200);
		
		long responseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
		System.out.println("Response time: " + responseTime);
		if (responseTime <= 2000) {
			System.out.println("Response time within range.");
		}
		else {
			System.out.println("Response time out of range");
		}
		
		String responseBody = response.getBody().asString();
		System.out.println("Read One Product response body: " + responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		String actualProductName = jp.getString("name");
		System.out.println("Actual Product name: " + actualProductName);
		String actualProductPrice = jp.getString("price");
		System.out.println("Actual Product price: " + actualProductPrice);
		String actualProductDescription = jp.getString("description");
		System.out.println("Actual Product description: " + actualProductDescription);
		
		String expectedProductName = createPayloadMap().get("name");
		String expectedProductPrice = createPayloadMap().get("price");
		String expectedProductDescription = createPayloadMap().get("description");
		//to output expected data, put in syso with expected vairables
		
		Assert.assertEquals(actualProductName, expectedProductName);
		Assert.assertEquals(actualProductPrice, expectedProductPrice);
		Assert.assertEquals(actualProductDescription, expectedProductDescription);
		
	}
	
}
