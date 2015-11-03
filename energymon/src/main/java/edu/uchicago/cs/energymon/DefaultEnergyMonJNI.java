package edu.uchicago.cs.energymon;

import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * Gets a default energymon implementation and exposes methods for performing
 * operations on it. This implementation is a simple wrapper around the JNI
 * functions.
 * 
 * This implementation is <b>NOT</b> thread safe and should be synchronized
 * externally. Additionally, it does <b>NOT</b> ensure proper protocol (e.g.
 * initialization before access). Attempting to perform operations after
 * {@link #finish()} is called will result in an {@link IllegalStateException}.
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
	 *             if resources cannot be allocated
	 */
	public DefaultEnergyMonJNI() {
		nativePtr = EnergyMonJNI.energymonGetDefault();
		if (nativePtr == null) {
			throw new IllegalStateException("Failed to get energymon over JNI");
		}
	}

	/**
	 * Initialize the energymon implementation (required).
	 * 
	 * @return 0 on success, anything else on failure
	 * @throws IllegalStateException
	 *             if already finished
	 */
	public int init() {
		if (nativePtr == null) {
			throw new IllegalStateException("Already finished");
		}
		return EnergyMonJNI.energymonInit(nativePtr);
	}

	/**
	 * Read the energy data in microjoules.
	 * 
	 * @return microjoules
	 * @throws IllegalStateException
	 *             if already finished
	 */
	public BigInteger readTotal() {
		if (nativePtr == null) {
			throw new IllegalStateException("Already finished");
		}
		return toUnsignedBigInteger(EnergyMonJNI.energymonReadTotal(nativePtr));
	}

	/**
	 * Clean up allocated resources (may include stopping polling threads and
	 * freeing natively allocated data). The {@link DefaultEnergyMonJNI} cannot
	 * be used after this function is called!
	 * 
	 * @return 0 on success, anything else on failure
	 * @throws IllegalStateException
	 *             if already finished
	 */
	public int finish() {
		if (nativePtr == null) {
			throw new IllegalStateException("Already finished");
		}
		int result = EnergyMonJNI.energymonFinish(nativePtr);
		nativePtr = null;
		return result;
	}

	/**
	 * Get a human-readable name for the energymon source.
	 * 
	 * @return source
	 * @throws IllegalStateException
	 *             if already finished
	 */
	public String getSource() {
		if (nativePtr == null) {
			throw new IllegalStateException("Already finished");
		}
		return EnergyMonJNI.energymonGetSource(nativePtr);
	}

	/**
	 * Get the refresh interval of the energymon in microseconds.
	 * 
	 * @return interval
	 * @throws IllegalStateException
	 *             if already finished
	 */
	public BigInteger getInterval() {
		if (nativePtr == null) {
			throw new IllegalStateException("Already finished");
		}
		return toUnsignedBigInteger(EnergyMonJNI.energymonGetInterval(nativePtr));
	}

	private static BigInteger toUnsignedBigInteger(final byte[] data) {
		if (data == null) {
			throw new IllegalStateException("Got null data");
		}
		return new BigInteger(1, data);
	}
}
