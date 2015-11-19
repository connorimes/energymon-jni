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
 * @author Connor Imes
 */
public class DefaultEnergyMonJNI implements EnergyMon {
	protected volatile ByteBuffer nativePtr;

	/**
	 * Don't allow public instantiation. Should use {@link #create()} which
	 * throws exceptions on failure.
	 * 
	 * @param nativePtr
	 */
	protected DefaultEnergyMonJNI(final ByteBuffer nativePtr) {
		this.nativePtr = nativePtr;
	}

	/**
	 * Create a new {@link DefaultEnergyMonJNI}.
	 * 
	 * @throws IllegalStateException
	 *             if native resources cannot be allocated
	 */
	public static DefaultEnergyMonJNI create() {
		final ByteBuffer ptr = EnergyMonJNI.get().energymonAlloc();
		if (ptr == null) {
			throw new IllegalStateException("Failed to allocate energymon over JNI");
		}
		if (EnergyMonJNI.get().energymonGetDefault(ptr) != 0) {
			EnergyMonJNI.get().energymonFree(ptr);
			throw new IllegalStateException("Failed to get default energymon over JNI");
		}
		if (EnergyMonJNI.get().energymonInit(ptr) != 0) {
			EnergyMonJNI.get().energymonFree(ptr);
			throw new IllegalStateException("Failed to initialize energymon over JNI");
		}
		return new DefaultEnergyMonJNI(ptr);
	}

	public long readTotal() {
		enforceNotFinished();
		return EnergyMonJNI.get().energymonReadTotal(nativePtr);
	}

	public String getSource() {
		enforceNotFinished();
		return EnergyMonJNI.get().energymonGetSource(nativePtr);
	}

	public long getInterval() {
		enforceNotFinished();
		return EnergyMonJNI.get().energymonGetInterval(nativePtr);
	}

	public int finish() {
		enforceNotFinished();
		int result = EnergyMonJNI.get().energymonFinish(nativePtr);
		EnergyMonJNI.get().energymonFree(nativePtr);
		nativePtr = null;
		return result;
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
