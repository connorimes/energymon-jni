package edu.uchicago.cs.energymon;

/**
 * JNI bindings for native energymon-default implementations.
 * 
 * @author Connor Imes
 */
public final class EnergyMonJNI {
	static {
		System.loadLibrary("energymon-default-wrapper");
	}

	public static native int energymonGetDefault();

	public static native int energymonInit(int id);

	public static native byte[] energymonReadTotal(int id);

	public static native int energymonFinish(int id);

	public static native String energymonGetSource(int id);

	public static native byte[] energymonGetInterval(int id);
}
