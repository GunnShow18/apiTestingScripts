package files;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class StaticJson {
	
	@Test
	public void addBook() throws IOException {
		RestAssured.baseURI = "http://216.10.245.166";
		String jsonPath = "C:\\Users\\SQ363ZW\\Desktop\\EY Projects\\Rest API Testing\\Addbookdetails.json";
		
		
		String response = given()
		.header("Content-Type", "application/json")
		.body(GenerateStringFromResource(jsonPath))
		.when()
		.post("/Library/Addbook.php")
		.then()
		.assertThat()
		.statusCode(200)
		.extract()
		.response()
		.asString();
		
		JsonPath js = ReUsableMethods.rawToJson(response);
		String id = js.getString("ID");
		System.out.println(id);
		
		//deleteBook
	}
	
	@DataProvider(name="BooksData") // assigning name to dataProdiver
	public Object[][] getData() {
		//array = collection of elements
		//multidimensional arrays are collection of arrays
		return new Object[][] {{"Blues Clues", "123", "14B", "Bill Cosby" },{"Red Clues", "321", "13B", "Billy Blanks"},{"Green Clues", "213", "16B", "Bill Clinton"} };
	}

	
	public static String GenerateStringFromResource(String path) throws IOException {
		return new String(Files.readAllBytes(Paths.get(path)));
	}

}
