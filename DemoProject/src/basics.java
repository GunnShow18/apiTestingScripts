import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;

import files.ReUsableMethods;
import files.payload;

public class basics {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// validate if Add Place API is working as expected
		
		//Add place -> Update Place with New Address -> Get Place to validate if new address is present in response
				// End-to-end automation testing
		
		
		// Given - All input details
		// When - Submit the API - resource, http method
		// Then - Validate the response

		RestAssured.baseURI = "https://rahulshettyacademy.com";
		// given belongs to the rest assured package
		String response = 
				
		// Get Place
		given()
		.log() // log is valid for input
		.all()
		.queryParam("key", "qaclick123")
		.header("Content-Type","application/json")
		.body
		(payload.addPlace())
		.when()
		.post("maps/api/place/add/json")
		.then()
		.assertThat()
		.statusCode(200)
		.body("scope", equalTo("APP"))
		.header("server", "Apache/2.4.18 (Ubuntu)")
		.extract().response().asString();
	
		// printing response and not the request
		System.out.println(response);	
		
		JsonPath js = new JsonPath(response); //parses Json files
		String placeId = js.getString("place_id"); // paths use dot notation /Parent-child relationship
		
		System.out.println(placeId);
		
		//Update place
		String newAddress = "Summer Walk, Africa";
		
		given()
		.log()
		.all()
		.queryParam("key", "qaclick123")
		.header("Content-Type", "application/json")
		.body("{\r\n" + 
				"\"place_id\":\"" + placeId + "\",\r\n" + 
				"\"address\":\"" + newAddress + "\",\r\n" + 
				"\"key\":\"qaclick123\"\r\n" + 
				"}\r\n" + 
				"")
		.when()
		.put("maps/api/place/update/json")
		.then()
		.assertThat()
		.log()
		.all()
		.statusCode(200)
		.body("msg", equalTo("Address successfully updated"));
		
		// Get Place
		
		String getPlaceResponse =
		given()
		.log()
		.all()
		.queryParam("key", "qaclick123")
		.queryParam("place_id", placeId)
		.when()
		.get("maps/api/place/get/json")
		.then()
		.assertThat()
		.log()
		.all()
		.statusCode(200).extract().asString();
		
		JsonPath js2 = ReUsableMethods.rawToJson(getPlaceResponse); // Created Java related class to wrap Java related code
		String actualAddress = js2.getString("address");
		System.out.println(actualAddress);
		Assert.assertEquals(actualAddress, newAddress); // imported TestNG function 
		//Cucumber Junit, TestNG
	}

}
