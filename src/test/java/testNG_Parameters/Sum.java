package testNG_Parameters;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class Sum {

	@Test
	@Parameters({"c", "d"})
	public void add(int a, int b) {
		int sum = a+b;
		System.out.println("Sum of two numbers: " + sum);
	}
}
