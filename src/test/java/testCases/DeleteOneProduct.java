package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.*;

import static io.restassured.RestAssured.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DeleteOneProduct {
	
	String baseURI = "https://techfios.com/api-prod/api/product";
	String payLoadFilePath = "src\\main\\java\\data\\createPayload.json";
	HashMap<String,String> deletePayload; //we are using map to store data used to create new product
	
	String username = "demo@techfios.com";
	String password = "abc123";
	String deleteProductId;
	String readOneProductId;
	
	public Map<String,String> deletePayloadMap() {
		
		deletePayload = new HashMap<String,String>();
		
		deletePayload.put("id", "5570");
		
		return deletePayload;
		
	} //this creates a new map with the payload data used to create a new product
	
	
	@Test(priority=1)
	public void deleteOneProduct() {
		
		
		Response response = 
		
		given()
			.baseUri(baseURI)
			.header("Content-Type", "application/json; charset = UTF-8")
			.auth().preemptive().basic(username, password)
			.body(deletePayloadMap()).
		when()
			.delete("/delete.php"). //this is now put and update
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
		String deleteProductMessage = jp.getString("message");
		System.out.println("Delete Product Message: " + deleteProductMessage);
		Assert.assertEquals(deleteProductMessage, "Product was deleted.");
		
	}
	
	@Test(priority=2)
	public void readOneProduct() {//we cant pass string with a test anntation
		
		deleteProductId = deletePayloadMap().get("id");
		readOneProductId = deleteProductId;
		
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
		Assert.assertEquals(statusCode, 404);
		
		String responseBody = response.getBody().asString();
		System.out.println("Read One Product response body: " + responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		String actualProductMessage = jp.getString("message");
		System.out.println("Actual Product message: " + actualProductMessage);
		Assert.assertEquals(actualProductMessage, "Product does not exist.");
		
	}
	
}
