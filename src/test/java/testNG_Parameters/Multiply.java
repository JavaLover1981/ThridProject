package testNG_Parameters;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class Multiply {

	@Test
	@Parameters({"c", "d"})
	public void add(int a, int b) {
		int multip = a*b;
		System.out.println("Sum of two numbers: " + multip);
	}
}
