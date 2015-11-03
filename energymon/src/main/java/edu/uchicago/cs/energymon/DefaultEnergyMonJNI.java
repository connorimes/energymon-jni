package edu.uchicago.cs.energymon;

import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * Gets a default energymon implementation and exposes methods for performing
 * operations on it. This implementation is a simple wrapper around the JNI
 * functions.
 * 
 * This implementation is <b>NOT</b> thread safe and should be synchronized
 * externally. Attempting to perform operations after {@link #finish()} is
 * called will result in an {@link IllegalStateException}.
 * 
 * Failure to allocate the native resources also results in an
 * {@link IllegalStateException} in the constructor.
 * 
 * @author Connor Imes
 */
public class DefaultEnergyMonJNI implements EnergyMon {
	private volatile ByteBuffer nativePtr;

	/**
	 * Create a {@link DefaultEnergyMonJNI}.
	 * 
	 * @throws IllegalStateException
	 *             if native resources cannot be allocated
	 */
	public DefaultEnergyMonJNI() {
		nativePtr = EnergyMonJNI.get().energymonGetDefault();
		if (nativePtr == null) {
			throw new IllegalStateException("Failed to get energymon over JNI");
		}
	}

	public int init() {
		if (nativePtr == null) {
			throw new IllegalStateException("Already finished");
		}
		return EnergyMonJNI.get().energymonInit(nativePtr);
	}

	public BigInteger readTotal() {
		if (nativePtr == null) {
			throw new IllegalStateException("Already finished");
		}
		return toUnsignedBigInteger(EnergyMonJNI.get().energymonReadTotal(nativePtr));
	}

	public int finish() {
		if (nativePtr == null) {
			throw new IllegalStateException("Already finished");
		}
		int result = EnergyMonJNI.get().energymonFinish(nativePtr);
		nativePtr = null;
		return result;
	}

	public String getSource() {
		if (nativePtr == null) {
			throw new IllegalStateException("Already finished");
		}
		return EnergyMonJNI.get().energymonGetSource(nativePtr);
	}

	public BigInteger getInterval() {
		if (nativePtr == null) {
			throw new IllegalStateException("Already finished");
		}
		return toUnsignedBigInteger(EnergyMonJNI.get().energymonGetInterval(nativePtr));
	}

	private static BigInteger toUnsignedBigInteger(final byte[] data) {
		return data == null ? null : new BigInteger(1, data);
	}
}
