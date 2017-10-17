package com.poixson.serial.natives;

import com.poixson.serial.NativeD2xx;


public class NativeD2xxProp implements NativeD2xx {



	@Override
	public native byte[] natGetDeviceList();


	@Override
	public native long natOpenPort(String portName);
	@Override
	public native boolean natClosePort(long handle);


	@Override
	public native long natSetParams(long handle, int baud, int byteSize, int stopBits, int parity, int flags);
//	@Override
//	public native long natSetBlocking(long handle, boolean blocking);
//	@Override
//	public native long natSetVMinVTime(long handle, int vMin, int vTime);


//	@Override
//	public native long natGetLineStatus(long handle);
//	@Override
//	public native long natSetLineStatus(long handle, boolean setRTS, boolean setDTR);


//	@Override
//	public native int natGetInputBytesCount(long handle);
//	@Override
//	public native int natGetOutputBytesCount(long handle);


	@Override
	public native int natReadBytes(long handle, byte[] bytes, int len);
	@Override
	public native long natWriteBytes(long handle, byte[] bytes);



}
