package edu.uchicago.cs.energymon;

import java.math.BigInteger;

/**
 * Abstraction for EnergyMon implementations over JNI.
 * 
 * Proper energymon protocol must be observed to prevent exceptions and memory
 * leaks! For example, you must call {@link #finish()} before dropping the
 * instance and allowing it to be garbage collected.
 * 
 * @author Connor Imes
 */
public interface EnergyMon {
	/**
	 * Open required file(s), start necessary background tasks, etc. Typically
	 * allocates the state field of the energymon struct.
	 * 
	 * @return 0 on success, failure code otherwise
	 * @throws IllegalStateException
	 *             if already finished
	 */
	int init();

	/**
	 * Get the total energy in microjoules.
	 * 
	 * @return energy (in uJ), or null on failure
	 * @throws IllegalStateException
	 *             if already finished
	 */
	BigInteger readTotal();

	/**
	 * Stop background tasks, close open file(s), free memory allocations, etc.
	 * Typically frees the state field of the energymon struct.
	 * 
	 * @return 0 on success, failure code otherwise
	 * @throws IllegalStateException
	 *             if already finished
	 */
	int finish();

	/**
	 * Get a human-readable description of the energy monitoring source.
	 * 
	 * @return description, or null on failure
	 * @throws IllegalStateException
	 *             if already finished
	 */
	String getSource();

	/**
	 * Get the refresh interval in microseconds of the underlying sensor(s).
	 * 
	 * @return the refresh interval, or null on failure
	 * @throws IllegalStateException
	 *             if already finished
	 */
	BigInteger getInterval();
}
