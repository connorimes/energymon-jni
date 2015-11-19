package edu.uchicago.cs.energymon;

import java.nio.ByteBuffer;

/**
 * Gets a default energymon implementation and exposes methods for performing
 * operations on it. This implementation is a simple wrapper around the JNI
 * functions.
 * 
 * This implementation is <b>NOT</b> thread safe and should be synchronized
 * externally. Attempting to perform operations after {@link #finish()} is
 * called will result in an {@link IllegalStateException}.
 * 
 * Failure to allocate the native resources also results in an
 * {@link IllegalStateException} in the constructor.
 * 
 * @author Connor Imes
 */
public class DefaultEnergyMonJNI implements EnergyMon {
	protected volatile ByteBuffer nativePtr;

	/**
	 * Create a {@link DefaultEnergyMonJNI}.
	 * 
	 * @throws IllegalStateException
	 *             if native resources cannot be allocated
	 */
	public DefaultEnergyMonJNI() {
		nativePtr = EnergyMonJNI.get().energymonAlloc();
		if (nativePtr == null) {
			throw new IllegalStateException("Failed to allocate energymon over JNI");
		}
		if (EnergyMonJNI.get().energymonGetDefault(nativePtr) != 0) {
			EnergyMonJNI.get().energymonFree(nativePtr);
			throw new IllegalStateException("Failed to get default energymon over JNI");
		}
		if (EnergyMonJNI.get().energymonInit(nativePtr) != 0) {
			EnergyMonJNI.get().energymonFree(nativePtr);
			throw new IllegalStateException("Failed to initialize energymon over JNI");
		}
	}

	public int init() {
		enforceNotFinished();
		// we already initialized in the constructor
		return 0;
	}

	public long readTotal() {
		enforceNotFinished();
		return EnergyMonJNI.get().energymonReadTotal(nativePtr);
	}

	public int finish() {
		enforceNotFinished();
		int result = EnergyMonJNI.get().energymonFinish(nativePtr);
		EnergyMonJNI.get().energymonFree(nativePtr);
		nativePtr = null;
		return result;
	}

	public String getSource() {
		enforceNotFinished();
		return EnergyMonJNI.get().energymonGetSource(nativePtr);
	}

	public long getInterval() {
		enforceNotFinished();
		return EnergyMonJNI.get().energymonGetInterval(nativePtr);
	}

	/**
	 * Throws an {@link IllegalStateException} if {@link #nativePtr} is null.
	 */
	protected void enforceNotFinished() {
		if (nativePtr == null) {
			throw new IllegalStateException("Already finished");
		}
	}

}
