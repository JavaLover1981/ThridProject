package testNG_Parameters;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class Subtract {

	@Test
	@Parameters({"c", "d"})
	public void add(int a, int b) {
		int subtract = a-b;
		System.out.println("Sum of two numbers: " + subtract);
	}
}
