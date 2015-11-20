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
		ByteBuffer ptr = em.energymonAlloc();
		assertNotNull("ptr", ptr);
		assertEquals("energymonGetDefault", 0, em.energymonGetDefault(ptr));
		assertEquals("energymonInit", 0, em.energymonInit(ptr));
		assertNotEquals("energymonGetInterval", 0, em.energymonGetInterval(ptr));
		assertNotNull("energymonGetSource", em.energymonGetSource(ptr));
		em.energymonReadTotal(ptr);
		assertEquals("energymonFinish", 0, em.energymonFinish(ptr));
		em.energymonFree(ptr);
	}

	@Test
	public void test_null_ptr() {
		EnergyMonJNI em = EnergyMonJNI.get();
		assertNotNull("EnergyMonJNI", em);
		assertNotEquals("energymonGetDefault", 0, em.energymonGetDefault(null));
		assertNotEquals("energymonInit", 0, em.energymonInit(null));
		assertEquals("energymonGetInterval", 0, em.energymonGetInterval(null));
		assertNull("energymonGetSource", em.energymonGetSource(null));
		assertEquals("energymonReadTotal", 0, em.energymonReadTotal(null));
		assertNotEquals("energymonFinish", 0, em.energymonFinish(null));
		em.energymonFree(null);
	}

	@Test
	public void test_uninitialized_ptr() {
		EnergyMonJNI em = EnergyMonJNI.get();
		assertNotNull("EnergyMonJNI", em);
		ByteBuffer ptr = em.energymonAlloc();
		assertNotNull("ptr", ptr);
		assertEquals("energymonGetDefault", 0, em.energymonGetDefault(ptr));
		em.energymonGetInterval(ptr);
		em.energymonGetSource(ptr);
		em.energymonReadTotal(ptr);
		em.energymonFinish(ptr);
		em.energymonFree(ptr);
	}

	@Test
	public void test_without_get() {
		EnergyMonJNI em = EnergyMonJNI.get();
		assertNotNull("EnergyMonJNI", em);
		ByteBuffer ptr = em.energymonAlloc();
		assertNotNull("ptr", ptr);
		assertNotEquals("energymonInit", 0, em.energymonInit(ptr));
		assertEquals("energymonGetInterval", 0, em.energymonGetInterval(ptr));
		assertNull("energymonGetSource", em.energymonGetSource(ptr));
		assertEquals("energymonReadTotal", 0, em.energymonReadTotal(ptr));
		assertNotEquals("energymonFinish", 0, em.energymonFinish(ptr));
		em.energymonFree(ptr);
	}

	@Test
	public void test_access_after_finish() {
		EnergyMonJNI em = EnergyMonJNI.get();
		assertNotNull("EnergyMonJNI", em);
		ByteBuffer ptr = em.energymonAlloc();
		assertNotNull("ptr", ptr);
		assertEquals("energymonGetDefault", 0, em.energymonGetDefault(ptr));
		assertEquals("energymonInit", 0, em.energymonInit(ptr));
		assertEquals("energymonFinish", 0, em.energymonFinish(ptr));
		assertEquals("energymonGetInterval", 0, em.energymonGetInterval(ptr));
		assertNull("energymonGetSource", em.energymonGetSource(ptr));
		assertEquals("energymonReadTotal", 0, em.energymonReadTotal(ptr));
		assertNotEquals("energymonFinish", 0, em.energymonFinish(ptr));
		em.energymonFree(ptr);
	}

}
