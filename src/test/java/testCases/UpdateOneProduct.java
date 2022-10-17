package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.*;

import static io.restassured.RestAssured.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class UpdateOneProduct {
	
	String baseURI = "https://techfios.com/api-prod/api/product";
	String payLoadFilePath = "src\\main\\java\\data\\createPayload.json";
	HashMap<String,String> updatePayload; //we are using map to store data used to create new product
	
	String username = "demo@techfios.com";
	String password = "abc123";
	String updateProductId;
	String readOneProductId;
	
	public Map<String,String> updatePayloadMap() {
		
		updatePayload = new HashMap<String,String>();
		
		updatePayload.put("id", "5663");
		updatePayload.put("name", "Amazing Pillow 2.0 By MD");
		updatePayload.put("price", "199");
		updatePayload.put("description", "The super updated pillow for amazing programmers");
		updatePayload.put("category_id", "2");
		updatePayload.put("category_name", "Electronics");
		
		return updatePayload;
		
	} //this creates a new map with the payload data used to create a new product
	
	
	@Test(priority=1)
	public void updateOneProduct() {
		
		
		Response response = 
		
		given()
			.baseUri(baseURI)
			.header("Content-Type", "application/json; charset = UTF-8")
			.auth().preemptive().basic(username, password)
			.body(updatePayloadMap()).
		when()
			.put("/update.php"). //this is now put and update
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
		
		//this is validating that the product was created
		String responseHeader = response.getHeader("Content-Type");
		System.out.println("Response Header: " + responseHeader);
		Assert.assertEquals(responseHeader, "application/json; charset=UTF-8");
		
		//connection error: sqlstate[HY000]
		String responseBody = response.getBody().asString();
		System.out.println("Response body: " + responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		String updateProductMessage = jp.getString("message");
		System.out.println("Update Product Message: " + updateProductMessage);
		Assert.assertEquals(updateProductMessage, "Product was updated.");
		
	}
	
	@Test(priority=2)
	public void readOneProduct() {//we cant pass string with a test anntation
		
		updateProductId = updatePayloadMap().get("id");
		readOneProductId = updateProductId;
		
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
		
		String expectedProductName = updatePayloadMap().get("name");
		String expectedProductPrice = updatePayloadMap().get("price");
		String expectedProductDescription = updatePayloadMap().get("description");
		//to output expected data, put in syso with expected vairables
		
		Assert.assertEquals(actualProductName, expectedProductName);
		Assert.assertEquals(actualProductPrice, expectedProductPrice);
		Assert.assertEquals(actualProductDescription, expectedProductDescription);
		
	}
	
}
