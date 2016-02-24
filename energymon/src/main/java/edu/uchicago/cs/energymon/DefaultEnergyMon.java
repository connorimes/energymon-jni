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
public class DefaultEnergyMon implements EnergyMon {
	protected volatile ByteBuffer nativePtr;
	// r/w lock for pointer to prevent race conditions that could cause crash
	protected final ReadWriteLock lock;

	/**
	 * Don't allow public instantiation. Should use {@link #create()} which
	 * throws exceptions on failure.
	 * 
	 * @param nativePtr
	 */
	protected DefaultEnergyMon(final ByteBuffer nativePtr) {
		this.nativePtr = nativePtr;
		this.lock = new ReentrantReadWriteLock(true);
	}

	/**
	 * Create a new {@link DefaultEnergyMon}.
	 * 
	 * @throws IllegalStateException
	 *             if native resources cannot be allocated
	 */
	public static DefaultEnergyMon create() {
		final ByteBuffer ptr = EnergyMonJNI.get().alloc();
		if (ptr == null) {
			throw new IllegalStateException("Failed to allocate energymon over JNI");
		}
		if (EnergyMonJNI.get().getDefault(ptr) != 0) {
			EnergyMonJNI.get().free(ptr);
			throw new IllegalStateException("Failed to get default energymon over JNI");
		}
		if (EnergyMonJNI.get().init(ptr) != 0) {
			EnergyMonJNI.get().free(ptr);
			throw new IllegalStateException("Failed to initialize energymon over JNI");
		}
		return new DefaultEnergyMon(ptr);
	}

	public long readTotal() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return EnergyMonJNI.get().readTotal(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public String getSource() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return EnergyMonJNI.get().getSource(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getInterval() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return EnergyMonJNI.get().getInterval(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getPrecision() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return EnergyMonJNI.get().getPrecision(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public boolean isExclusive() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return EnergyMonJNI.get().isExclusive(nativePtr);
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
		final int result = EnergyMonJNI.get().finish(nativePtr);
		EnergyMonJNI.get().free(nativePtr);
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
