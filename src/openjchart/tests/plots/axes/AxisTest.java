package openjchart.tests.plots.axes;

import static org.junit.Assert.assertEquals;
import openjchart.plots.axes.Axis;

import org.junit.Before;
import org.junit.Test;

public class AxisTest {
	private static Axis axis;

	@Before
	public void setUp() throws Exception {
		axis = new Axis(-5, 5);
	}

	@Test
	public void testGetMin() {
		assertEquals(-5, axis.getMin());
	}

	@Test
	public void testGetMax() {
		assertEquals(5, axis.getMax());
	}

	@Test
	public void testGetRange() {
		double delta = 1e-5;
		assertEquals(10, axis.getRange(), delta);
	}
}