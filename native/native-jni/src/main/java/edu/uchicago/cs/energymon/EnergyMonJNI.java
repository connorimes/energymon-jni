package edu.uchicago.cs.energymon;

import java.nio.ByteBuffer;

/**
 * JNI bindings for native energymon-default implementations.
 * 
 * @author Connor Imes
 */
public final class EnergyMonJNI {
	static {
		System.loadLibrary("energymon-default-wrapper");
	}

	public static native ByteBuffer energymonGetDefault();

	public static native int energymonInit(ByteBuffer ptr);

	public static native byte[] energymonReadTotal(ByteBuffer ptr);

	public static native int energymonFinish(ByteBuffer ptr);

	public static native String energymonGetSource(ByteBuffer ptr);

	public static native byte[] energymonGetInterval(ByteBuffer ptr);
}
