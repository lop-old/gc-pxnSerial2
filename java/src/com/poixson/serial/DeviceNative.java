package com.poixson.serial;


public interface DeviceNative {



	public byte[] natGetDeviceList();

	public long natOpenPort(String portName);
	public boolean natClosePort(long handle);

	public long natSetParams(long handle, int baud, int byteSize, int stopBits, int parity, int flags);
	public long natSetBlocking(long handle, boolean blocking);
	public long natSetVMinVTime(long handle, int vMin, int vTime);

	public long natSetLineStatus(long handle, boolean setRTS, boolean setDTR);

	public int natGetInputBytesCount(long handle);
	public int natGetOutputBytesCount(long handle);

	public int natReadBytes(long handle, byte[] bytes, int len);
	public long natWriteBytes(long handle, byte[] bytes);



}
