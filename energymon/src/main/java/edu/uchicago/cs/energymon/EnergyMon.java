package edu.uchicago.cs.energymon;

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
	 * Get the total energy in microjoules. The resulting value should be
	 * treated as unsigned.
	 * 
	 * @return energy (in uJ), or 0 on failure
	 * @throws IllegalStateException
	 *             if already finished
	 */
	long readTotal();

	/**
	 * Get a human-readable description of the energy monitoring source.
	 * 
	 * @return description, or null on failure
	 * @throws IllegalStateException
	 *             if already finished
	 */
	String getSource();

	/**
	 * Get the refresh interval in microseconds of the underlying sensor(s). The
	 * resulting value should be treated as unsigned.
	 * 
	 * @return the refresh interval, or 0 on failure
	 * @throws IllegalStateException
	 *             if already finished
	 */
	long getInterval();

	/**
	 * Get the precision in microjoules of the underlying sensor(s). The
	 * resulting value should be treated as unsigned.
	 * 
	 * @return the precision, or 0 on failure
	 * @throws IllegalStateException
	 *             if already finished
	 */
	long getPrecision();

	/**
	 * Get if the implementaiton requires exclusive access.
	 * 
	 * @return boolean
	 * @throws IllegalStateException
	 *             if already finished
	 */
	boolean isExclusive();

	/**
	 * Stop background tasks, close open file(s), free memory allocations, etc.
	 * Typically frees the state field of the energymon struct.
	 * 
	 * @return 0 on success, failure code otherwise
	 * @throws IllegalStateException
	 *             if already finished
	 */
	int finish();
}
