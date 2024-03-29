package com.poixson.serial.natives;

import java.util.concurrent.atomic.AtomicReference;

import com.poixson.utils.ThreadUtils;


public class NativeD2xxProp implements NativeD2xx {

	private static final AtomicReference<NativeD2xxProp> instance =
			new AtomicReference<NativeD2xxProp>(null);



	public static NativeD2xxProp get() {
		// existing instance
		{
			final NativeD2xxProp nat = instance.get();
			if (nat != null)
				return nat;
		}
		// new instance
		{
			final NativeD2xxProp nat = new NativeD2xxProp();
			for (int i=0; i<5; i++) {
				if (instance.compareAndSet(null, nat)) {
					if (nat.init() != 0L) {
//TODO:
throw new RuntimeException("Failed to init serial native!");
					}
					return nat;
				}
				final NativeD2xxProp n = instance.get();
				if (n != null)
					return n;
				ThreadUtils.Sleep(50L);
			}
		}
		return null;
	}
	private NativeD2xxProp() {}



	// ------------------------------------------------------------------------------- //



	@Override
	public native int init();
	@Override
	public native int unload();


	@Override
	public native void rescanDevices();
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


	@Override
	public native boolean[] getLineStatus(long handle);
	@Override
	public native long setLineStatus(long handle, boolean setRTS, boolean setDTR);


//	@Override
//	public native int getInputBytesCount(long handle);
//	@Override
//	public native int getOutputBytesCount(long handle);


	@Override
	public native int readBytes(long handle, byte[] bytes, int len);
	@Override
	public native long writeBytes(long handle, byte[] bytes);



}
