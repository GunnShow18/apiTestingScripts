package files;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class DynamicJson {
	
	@Test(dataProvider="BooksData")
	public void addBook(String name, String isbn, String aisle, String author, String id_) {
		RestAssured.baseURI = "http://216.10.245.166";
		
		
		String response = given()
		.header("Content-Type", "application/json")
		.body(payload.addBook(name, isbn, aisle, author))
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
		String getDeleteResponse =
				given()
				.log()
				.all()
				.when()
				.delete("ID", id)
				.then()
				.assertThat()
				.log()
				.all()
				.statusCode(200).extract().asString();
	}
	
	@DataProvider(name="BooksData") // assigning name to dataProdiver
	public Object[][] getData() {
		//array = collection of elements
		//multidimensional arrays are collection of arrays
		return new Object[][] {{"Blues Clues", "123", "14B", "Bill Cosby" },{"Red Clues", "321", "13B", "Billy Blanks"},{"Green Clues", "213", "16B", "Bill Clinton"} };
	}

}
