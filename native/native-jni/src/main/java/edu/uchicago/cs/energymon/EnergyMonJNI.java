package edu.uchicago.cs.energymon;

import java.nio.ByteBuffer;

/**
 * JNI bindings for native energymon-default implementations.
 * 
 * @author Connor Imes
 */
public final class EnergyMonJNI {
	private static EnergyMonJNI instance = null;

	/**
	 * Get an instance of {@link EnergyMonJNI}. On the first access, this method
	 * loads the native library. Failure to load will result in runtime
	 * exceptions.
	 * 
	 * @return {@link EnergyMonJNI}
	 */
	public static EnergyMonJNI get() {
		if (instance == null) {
			System.loadLibrary("energymon-default-wrapper");
			instance = new EnergyMonJNI();
		}
		return instance;
	}

	public native ByteBuffer energymonGetDefault();

	public native int energymonInit(ByteBuffer ptr);

	public native byte[] energymonReadTotal(ByteBuffer ptr);

	public native int energymonFinish(ByteBuffer ptr);

	public native String energymonGetSource(ByteBuffer ptr);

	public native byte[] energymonGetInterval(ByteBuffer ptr);
}
