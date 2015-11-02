package edu.uchicago.cs.energymon;

import java.math.BigInteger;

/**
 * Gets a default energymon implementation and exposes methods for performing
 * operations on it. This implementation is a simple wrapper around the JNI
 * functions. It is NOT thread safe and should be synchronized externally.
 * Additionally, it does NOT ensure proper protocol (i.e. initialization before
 * access, no access after destruction).
 * 
 * @author Connor Imes
 */
public class DefaultEnergyMonJNI implements EnergyMon {
	private final int nativeId;

	/**
	 * Create a {@link DefaultEnergyMonJNI}.
	 * 
	 * @throws IllegalStateException
	 *             if resources cannot be allocated
	 */
	public DefaultEnergyMonJNI() {
		nativeId = EnergyMonJNI.energymonGetDefault();
		if (nativeId < 0) {
			throw new IllegalStateException("Failed in get energymon over JNI");
		}
	}

	/**
	 * Initialize the energymon implementation (required).
	 * 
	 * @return 0 on success, anything else on failure
	 */
	public int init() {
		return EnergyMonJNI.energymonInit(nativeId);
	}

	/**
	 * Read the energy data in microjoules.
	 * 
	 * @return microjoules
	 */
	public BigInteger readTotal() {
		return toUnsignedBigInteger(EnergyMonJNI.energymonReadTotal(nativeId));
	}

	/**
	 * Clean up allocated resources (may include stopping polling threads and
	 * freeing natively allocated data). The {@link DefaultEnergyMonJNI} cannot
	 * be used after this function is called!
	 * 
	 * @return 0 on success, anything else on failure
	 */
	public int finish() {
		return EnergyMonJNI.energymonFinish(nativeId);
	}

	/**
	 * Get a human-readable name for the energymon source.
	 * 
	 * @return source
	 */
	public String getSource() {
		return EnergyMonJNI.energymonGetSource(nativeId);
	}

	/**
	 * Get the refresh interval of the energymon in microseconds.
	 * 
	 * @return interval
	 */
	public BigInteger getInterval() {
		return toUnsignedBigInteger(EnergyMonJNI.energymonGetInterval(nativeId));
	}

	private static BigInteger toUnsignedBigInteger(final byte[] data) {
		if (data == null) {
			throw new IllegalStateException("Got null data");
		}
		return new BigInteger(1, data);
	}
}
