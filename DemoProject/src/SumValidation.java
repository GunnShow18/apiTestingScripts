import org.testng.Assert;
import org.testng.annotations.Test;

import files.payload;
import io.restassured.path.json.JsonPath;

public class SumValidation {
	// Testing with mock JSON data
	@Test
	public void sumOfCourses() {
		JsonPath js = new JsonPath(payload.coursePrice());
		int count = js.getInt("courses.size()");
		int sum = 0;
		int overallPrice = js.getInt("dashboard.purchaseAmount");
		
		// Verify that sum of all courses matches overall price 
		for (int i=0;i<count;i++) {
			int price = js.getInt("courses["+i+"].price");
			int copies = js.getInt("courses["+i+"].copies");
			String title = js.getString("courses["+i+"].title");
			int courseAmount = price * copies;
			sum += courseAmount;
			
			System.out.println(title + " overall course revenue: " + courseAmount);
		}
		System.out.println("Calculated amount: " + sum + 
				"\nJSON amount: " + overallPrice);
		Assert.assertEquals(overallPrice, sum);
		
	}
}
