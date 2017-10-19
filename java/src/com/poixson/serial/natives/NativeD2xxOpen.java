package com.poixson.serial.natives;


public class NativeD2xxOpen implements NativeD2xx {



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
//	public native boolean[] getLineStatus(long handle);
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
