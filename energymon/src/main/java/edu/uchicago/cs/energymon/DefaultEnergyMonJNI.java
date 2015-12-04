package edu.uchicago.cs.energymon;

import java.nio.ByteBuffer;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Gets a default energymon implementation and exposes methods for performing
 * operations on it. This implementation is a simple wrapper around the JNI
 * functions.
 * 
 * Attempting to perform operations after {@link #finish()} is called will
 * result in an {@link IllegalStateException}.
 * 
 * @author Connor Imes
 */
public class DefaultEnergyMonJNI implements EnergyMon {
	protected volatile ByteBuffer nativePtr;
	// r/w lock for pointer to prevent race conditions that could cause crash
	protected final ReadWriteLock lock;

	/**
	 * Don't allow public instantiation. Should use {@link #create()} which
	 * throws exceptions on failure.
	 * 
	 * @param nativePtr
	 */
	protected DefaultEnergyMonJNI(final ByteBuffer nativePtr) {
		this.nativePtr = nativePtr;
		this.lock = new ReentrantReadWriteLock(true);
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
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return EnergyMonJNI.get().energymonReadTotal(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public String getSource() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return EnergyMonJNI.get().energymonGetSource(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getInterval() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return EnergyMonJNI.get().energymonGetInterval(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * This method does NOT enforce if this instance is already finished!
	 * 
	 * @return int
	 */
	protected int finishAndFree() {
		final int result = EnergyMonJNI.get().energymonFinish(nativePtr);
		EnergyMonJNI.get().energymonFree(nativePtr);
		nativePtr = null;
		return result;
	}

	public int finish() {
		try {
			lock.writeLock().lock();
			enforceNotFinished();
			return finishAndFree();
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		// last-ditch effort to cleanup if user didn't follow protocol
		try {
			if (nativePtr != null) {
				finishAndFree();
			}
		} finally {
			super.finalize();
		}
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
