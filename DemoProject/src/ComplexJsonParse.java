import files.payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {
	
	public static void main(String[] args) {
		JsonPath js = new JsonPath(payload.coursePrice());
		// Print number of courses returned by API
		int count = js.getInt("courses.size()"); //size() is a method in JsonPath that gets the length of an array
		System.out.println(count);
		
		// Print purchase amount
		int totalAmount = js.getInt("dashboard.purchaseAmount");
		System.out.println(totalAmount);
		
		// Print title of first course
		String courseTitle1 = js.getString("courses[0].title");
		System.out.println(courseTitle1);
		// print
		for (int i=0;i<count;i++) {
			String courseTitles = js.getString("courses["+i+"].title");
			int coursePrices = js.getInt("courses["+i+"].price");
			// print course details
			System.out.println("Course title: " + courseTitles +
					"\nCourse price: " + coursePrices);
		}
		
		// Print number of copies sole for RPA course
		for (int i=0;i<count;i++) {
			String courseTitle = js.getString("courses["+i+"].title");
			int coursesSold = js.getInt("courses["+i+"].copies");
			
			if (courseTitle.trim().equalsIgnoreCase("rpa")) {
				System.out.println("RPA courses sold: " + coursesSold);
				break;
			}
		}
		
		
	}
	

}
