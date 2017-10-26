package com.poixson.serial;

import java.io.IOException;
import java.io.OutputStream;

import com.poixson.utils.exceptions.RequiredArgumentException;


public class pxnSerialOutputStream extends OutputStream {

	private final pxnSerial serial;



	public pxnSerialOutputStream(final pxnSerial serial) {
		if (serial == null) throw new RequiredArgumentException("serial");
		this.serial = serial;
	}



	@Override
	public void write(final int b) throws IOException {
		final byte[] bytes = new byte[1];
		bytes[0] = (byte) b;
		this.write(bytes);
	}
	@Override
	public void write(final byte[] bytes) throws IOException {
		final boolean result =
			this.serial.write(bytes);
		if (!result) throw new IOException("Failed to write byte");
	}
	@Override
	public void write(final byte[] bytes, final int offset, final int len)
			throws IOException {
		final boolean result =
			this.serial.write(bytes, len);
		if (!result) throw new IOException("Failed to write byte");
	}



	@Override
	public void flush() throws IOException {
	}



	@Override
	public void close() throws IOException {
		try {
			super.close();
		} finally {
			this.serial.close();
		}
	}



}
