package com.poixson.serial.natives;

import java.util.concurrent.atomic.AtomicReference;

import com.poixson.utils.ThreadUtils;


public class NativeSerial implements DeviceNative {

	private static final AtomicReference<NativeSerial> instance =
			new AtomicReference<NativeSerial>(null);



	public static NativeSerial get() {
		// existing instance
		{
			final NativeSerial nat = instance.get();
			if (nat != null)
				return nat;
		}
		// new instance
		{
			final NativeSerial nat = new NativeSerial();
			for (int i=0; i<5; i++) {
				if (instance.compareAndSet(null, nat)) {
					if (nat.init() != 0L) {
//TODO:
throw new RuntimeException("Failed to init serial native!");
					}
					return nat;
				}
				final NativeSerial n = instance.get();
				if (n != null)
					return n;
				ThreadUtils.Sleep(50L);
			}
		}
		return null;
	}
	private NativeSerial() {}



	// ------------------------------------------------------------------------------- //



	@Override
	public native int init();
	@Override
	public native int unload();


	@Override
	public native byte[] getDeviceList();


	@Override
	public native long openPort(String portName);
	@Override
	public native boolean closePort(long handle);


	@Override
	public native long setParams(long handle, int baud, int byteSize, int stopBits, int parity, int flags);
//	@Override
//	public native long setBlocking(long handle, boolean blocking);
//	@Override
//	public native long setVMinVTime(long handle, int vMin, int vTime);


//	@Override
//	public native long getLineStatus(long handle);
//	@Override
//	public native long setLineStatus(long handle, boolean setRTS, boolean setDTR);


//	@Override
//	public native int getInputBytesCount(long handle);
//	@Override
//	public native int getOutputBytesCount(long handle);


	@Override
	public native int readBytes(long handle, byte[] bytes, int len);
	@Override
	public native long writeBytes(long handle, byte[] bytes);



}
