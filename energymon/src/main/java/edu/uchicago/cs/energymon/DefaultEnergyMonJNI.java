package edu.uchicago.cs.energymon;

import java.math.BigInteger;

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
	private volatile int nativeId;

	/**
	 * Create a {@link DefaultEnergyMonJNI}.
	 * 
	 * @throws IllegalStateException
	 *             if resources cannot be allocated
	 */
	public DefaultEnergyMonJNI() {
		nativeId = EnergyMonJNI.energymonGetDefault();
		if (nativeId < 0) {
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
		if (nativeId < 0) {
			throw new IllegalStateException("Already finished");
		}
		return EnergyMonJNI.energymonInit(nativeId);
	}

	/**
	 * Read the energy data in microjoules.
	 * 
	 * @return microjoules
	 * @throws IllegalStateException
	 *             if already finished
	 */
	public BigInteger readTotal() {
		if (nativeId < 0) {
			throw new IllegalStateException("Already finished");
		}
		return toUnsignedBigInteger(EnergyMonJNI.energymonReadTotal(nativeId));
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
		if (nativeId < 0) {
			throw new IllegalStateException("Already finished");
		}
		int result = EnergyMonJNI.energymonFinish(nativeId);
		nativeId = -1;
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
		if (nativeId < 0) {
			throw new IllegalStateException("Already finished");
		}
		return EnergyMonJNI.energymonGetSource(nativeId);
	}

	/**
	 * Get the refresh interval of the energymon in microseconds.
	 * 
	 * @return interval
	 * @throws IllegalStateException
	 *             if already finished
	 */
	public BigInteger getInterval() {
		if (nativeId < 0) {
			throw new IllegalStateException("Already finished");
		}
		return toUnsignedBigInteger(EnergyMonJNI.energymonGetInterval(nativeId));
	}

	private static BigInteger toUnsignedBigInteger(final byte[] data) {
		if (data == null) {
			throw new IllegalStateException("Got null data");
		}
		return new BigInteger(1, data);
	}
}
