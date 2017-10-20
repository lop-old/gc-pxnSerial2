package com.poixson.serial.natives;


public interface DeviceNative {



	public int init();
	public int unload();

	public void rescanDevices();
	public byte[] getDeviceList();

	public long openPort(String portName);
	public boolean closePort(long handle);

	public long setParams(long handle, int baud, int byteSize, int stopBits, int parity, int flags);
//	public long setBlocking(long handle, boolean blocking);
//	public long setVMinVTime(long handle, int vMin, int vTime);

//	public boolean[] getLineStatus(long handle);
//	public long setLineStatus(long handle, boolean setRTS, boolean setDTR);

//	public int getInputBytesCount(long handle);
//	public int getOutputBytesCount(long handle);

	public int readBytes(long handle, byte[] bytes, int len);
	public long writeBytes(long handle, byte[] bytes);



}
