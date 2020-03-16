package files;
import static io.restassured.RestAssured.*;

import java.io.File;

import org.testng.Assert;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

//Steps to automate in Jira application:
//	1. Login to Jira to create session using login API
//	2. Add a comment to existing Issue using add comment API
//	3. Add an attachment to existing Issue using Add Attachment API
//	4. Get Issue details and verify if added comment and attachment exists using Get Issue API
//	
//	New Topics which are covered from above examples:
//		- How to create Session Filter from Authentication 
//		in Rest Assured Automation in 
//		Introducing Path Parameters and Query Parameters together
//		Single Test Sending Files as Attachments using Rest Assured with Multipart method
//		Parsing complex Json and limiting Json response through Query Parameters Handling
//		HTTPS Certification validation through Automated Code


public class JiraTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RestAssured.baseURI="http://localhost:8080/";
		
		//Login Scenario
		SessionFilter session = new SessionFilter();
	String response = given()
			.relaxedHTTPSValidation() // method may be needed to be verified through HTTPS protocol (secured protocol)
		.header("Content-Type", "application/json")
		.body("	{ \"username\": \"usernameforjira\", \"password\": \"passwordforjira\" }")
		.log()
		.all()
		.filter(session) // response data will be stored in the session object to be passed in future API requests
		.when()
		.post("/rest/auth/1/session")
		.then()
		.extract()
		.response()
		.asString();
	String expectedMessage = "Hello, how are you?";
		
	// add comment to issue
		String addCommentResponse = given()
		.pathParam("key", "10100")
		.log()
		.all()
		.header("Content-Type", "application/json")
		.header("JSESSIONID", "")
		.body("{\r\n" + 
				"    \"body\": \"" + expectedMessage + "\",\r\n" + 
				"    \"visibility\": {\r\n" + 
				"        \"type\": \"role\",\r\n" + 
				"        \"value\": \"Administrators\"\r\n" + 
				"    }\r\n" + 
				"}")
		.filter(session)
		.when()
		.post("/rest/api/2/issue/{key}/comment")
		.then()
		.log()
		.all()
		.assertThat()
		.statusCode(201)
		.extract()
		.response()
		.asString();
		JsonPath js = new JsonPath(addCommentResponse);
		String commentId = js.getString("id");
		
		// Add attachment to issue
		given()
		.header("X-Atlassian-Token", "no-check")
		.filter(session)
		.pathParam("key", "10100")
		.header("Content-Type","multipart/form-data")
		// Sending files using multiPart() method
		.multiPart("file", new File("C:\\Users\\SQ363ZW\\Desktop\\EY Projects\\eclipse-workspace\\DemoProject\\jira.txt")) // "file" is the key, "file-path" is the value passed in the new File() Object
		.when()
		.post("rest/api/2/issue/{key}/attachments")
		.then()
		.log()
		.all()
		.assertThat()
		.statusCode(200);
		
		// get issue information
		String issueDetails =
		given()
		.filter(session)
		.pathParam("key", "10100")
		.queryParam("fields", "comment") // limit number of fields returned in GET request
		.when()
		.get("rest/api/2/issue/{key}")
		.then()
		.log()
		.all()
		.extract()
		.response()
		.asString();
		System.out.println(issueDetails);
		
		JsonPath js1 = new JsonPath(issueDetails);
		int commentsCount = js1.getInt("fields.comment.comments.size");
		for (int i=0;i<commentsCount;i++) {
			String commentIdIssue =  js1.get("fields.comment.comments[" + i + "].id").toString();
			if (commentIdIssue.equalsIgnoreCase(commentId)) {
			String message = js1.get("fields.comment.comments["+i+"].body").toString();
			System.out.println(message);
			Assert.assertEquals(message, expectedMessage);
			}
		}
		
		
	}
	


}
