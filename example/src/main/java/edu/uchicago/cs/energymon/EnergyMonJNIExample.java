package edu.uchicago.cs.energymon;

/**
 * A simple example.
 * 
 * @author Connor Imes
 */
public class EnergyMonJNIExample {

	public static void main(final String[] args) {
		int iterations = 10;
		if (args.length > 0) {
			iterations = Integer.parseInt(args[0]);
		}
		// create the EnergyMon object
		final EnergyMon em = DefaultEnergyMon.create();
		final String source = em.getSource();
		final long interval_us = em.getInterval();
		final long interval_ms = interval_us / 1000;
		System.out.println("Reading from energymon source: " + source
				+ " with interval " + interval_us + " microseconds");
		for (int i = 0; i < iterations; i++) {
			try {
				Thread.sleep(interval_ms);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// read from source
			System.out.println("Read value: " + em.readTotal());
		}
		// clean up resources
		// (otherwise polling threads remain active and/or memory leaks
		em.finish();
	}
}
