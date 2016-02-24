package edu.uchicago.cs.energymon;

import java.nio.ByteBuffer;

/**
 * JNI bindings for native energymon-default implementations. The native wrapper
 * code attempts to protect against crashes caused by incorrect usage, but
 * cannot avoid crashes caused by use-after-free or other bad {@link ByteBuffer}
 * values passed to it. Users are responsible for following the energymon
 * protocol and checking return codes.
 * 
 * Long return values should be treated as unsigned.
 * 
 * @author Connor Imes
 */
public final class EnergyMonJNI {
	private static EnergyMonJNI instance = null;

	/**
	 * Get an instance of {@link EnergyMonJNI}. On the first access, this method
	 * loads the native library. Failure to load will result in runtime
	 * exceptions or errors.
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

	public native ByteBuffer alloc();

	public native void free(ByteBuffer ptr);

	public native int getDefault(ByteBuffer ptr);

	public native int init(ByteBuffer ptr);

	public native long readTotal(ByteBuffer ptr);

	public native int finish(ByteBuffer ptr);

	public native String getSource(ByteBuffer ptr);

	public native long getInterval(ByteBuffer ptr);

	public native long getPrecision(ByteBuffer ptr);

	public native boolean isExclusive(ByteBuffer ptr);
}
