package edu.uchicago.cs.energymon;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;

import org.junit.Test;

/**
 * Test the JNI methods. Those calls without asserts means we can't say anything
 * about the value, but need to make sure it doesn't crash.
 * 
 * @author Connor Imes
 */
public final class EnergyMonJNITest {

	@Test
	public void test_normal() {
		EnergyMonJNI em = EnergyMonJNI.get();
		assertNotNull("EnergyMonJNI", em);
		ByteBuffer ptr = em.alloc();
		assertNotNull("ptr", ptr);
		assertEquals("energymonGetDefault", 0, em.getDefault(ptr));
		assertEquals("energymonInit", 0, em.init(ptr));
		assertNotEquals("energymonGetInterval", 0, em.getInterval(ptr));
		assertNotNull("energymonGetSource", em.getSource(ptr));
		em.getPrecision(ptr);
		em.isExclusive(ptr);
		em.readTotal(ptr);
		assertEquals("energymonFinish", 0, em.finish(ptr));
		em.free(ptr);
	}

	@Test
	public void test_null_ptr() {
		EnergyMonJNI em = EnergyMonJNI.get();
		assertNotNull("EnergyMonJNI", em);
		assertNotEquals("energymonGetDefault", 0, em.getDefault(null));
		assertNotEquals("energymonInit", 0, em.init(null));
		assertEquals("energymonGetInterval", 0, em.getInterval(null));
		assertNull("energymonGetSource", em.getSource(null));
		assertEquals("getPrecision", 0, em.getPrecision(null));
		assertFalse("getPrecision", em.isExclusive(null));
		assertEquals("energymonReadTotal", 0, em.readTotal(null));
		assertNotEquals("energymonFinish", 0, em.finish(null));
		em.free(null);
	}

	@Test
	public void test_uninitialized_ptr() {
		EnergyMonJNI em = EnergyMonJNI.get();
		assertNotNull("EnergyMonJNI", em);
		ByteBuffer ptr = em.alloc();
		assertNotNull("ptr", ptr);
		assertEquals("energymonGetDefault", 0, em.getDefault(ptr));
		em.getInterval(ptr);
		em.getSource(ptr);
		em.getPrecision(ptr);
		em.isExclusive(ptr);
		em.readTotal(ptr);
		em.finish(ptr);
		em.free(ptr);
	}

	@Test
	public void test_without_get() {
		EnergyMonJNI em = EnergyMonJNI.get();
		assertNotNull("EnergyMonJNI", em);
		ByteBuffer ptr = em.alloc();
		assertNotNull("ptr", ptr);
		assertNotEquals("energymonInit", 0, em.init(ptr));
		assertEquals("energymonGetInterval", 0, em.getInterval(ptr));
		assertNull("energymonGetSource", em.getSource(ptr));
		assertEquals("getPrecision", 0, em.getPrecision(ptr));
		assertFalse("getPrecision", em.isExclusive(ptr));
		assertEquals("energymonReadTotal", 0, em.readTotal(ptr));
		assertNotEquals("energymonFinish", 0, em.finish(ptr));
		em.free(ptr);
	}

	@Test
	public void test_access_after_finish() {
		EnergyMonJNI em = EnergyMonJNI.get();
		assertNotNull("EnergyMonJNI", em);
		ByteBuffer ptr = em.alloc();
		assertNotNull("ptr", ptr);
		assertEquals("energymonGetDefault", 0, em.getDefault(ptr));
		assertEquals("energymonInit", 0, em.init(ptr));
		assertEquals("energymonFinish", 0, em.finish(ptr));
		assertEquals("energymonGetInterval", 0, em.getInterval(ptr));
		assertNull("energymonGetSource", em.getSource(ptr));
		assertEquals("getPrecision", 0, em.getPrecision(ptr));
		assertFalse("getPrecision", em.isExclusive(ptr));
		assertEquals("energymonReadTotal", 0, em.readTotal(ptr));
		assertNotEquals("energymonFinish", 0, em.finish(ptr));
		em.free(ptr);
	}

}
