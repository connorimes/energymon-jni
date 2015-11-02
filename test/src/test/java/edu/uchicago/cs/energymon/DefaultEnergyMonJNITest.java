package edu.uchicago.cs.energymon;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

public class DefaultEnergyMonJNITest {

	@Test
	public void test() {
		EnergyMon em = new DefaultEnergyMonJNI();
		assertEquals("init", 0, em.init());
		assertNotNull("getSource", em.getSource());
		assertNotEquals("getInterval", 0, em.getInterval());
		assertTrue("readTotal", em.readTotal().compareTo(BigInteger.ZERO) >= 0);
		assertEquals("finish", 0, em.finish());
	}

}
