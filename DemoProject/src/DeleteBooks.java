import static io.restassured.RestAssured.given;

import org.testng.annotations.Test;

import files.ReUsableMethods;
import io.restassured.path.json.JsonPath;

public class DeleteBooks {
	
	// Delete place
	@Test
	public void deleteBook( ) {
		String getDeleteResponse =
				given()
				.log()
				.all()
				.when()
				.delete("id", "idgafudge1234")
				.then()
				.assertThat()
				.log()
				.all()
				.statusCode(200).extract().asString();
		
		JsonPath js = ReUsableMethods.rawToJson(getDeleteResponse);
		String id = js.getString("ID");
		System.out.println(id);
		
	}
	

}
