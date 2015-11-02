package edu.uchicago.cs.energymon;

import java.math.BigInteger;

/**
 * Abstraction for EnergyMon implementations over JNI.
 * 
 * @author Connor Imes
 */
public interface EnergyMon {
	/**
	 * Open required file(s), start necessary background tasks, etc. Typically
	 * allocates the state field of the energymon struct.
	 * 
	 * @return 0 on success, failure code otherwise
	 */
	int init();

	/**
	 * Get the total energy in microjoules.
	 * 
	 * @return energy (in uJ)
	 */
	BigInteger readTotal();

	/**
	 * Stop background tasks, close open file(s), free memory allocations, etc.
	 * Typically frees the state field of the energymon struct.
	 * 
	 * @return 0 on success, failure code otherwise
	 */
	int finish();

	/**
	 * Get a human-readable description of the energy monitoring source.
	 * Implementations should ensure that the buffer is null-terminated.
	 * 
	 * @return pointer to the same buffer, or NULL on failure
	 */
	String getSource();

	/**
	 * Get the refresh interval in microseconds of the underlying sensor(s).
	 * 
	 * @return the refresh interval
	 */
	BigInteger getInterval();
}
