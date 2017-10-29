package com.poixson.serial;

import java.io.IOException;
import java.io.InputStream;

import com.poixson.utils.exceptions.RequiredArgumentException;


public class pxnSerialInputStream extends InputStream {

	private final pxnSerial serial;



	public pxnSerialInputStream(final pxnSerial serial) {
		if (serial == null) throw new RequiredArgumentException("serial");
		this.serial = serial;
	}



	@Override
	public int read() throws IOException {
		return this.serial.readByte();
	}
	@Override
	public int read(byte bytes[]) throws IOException {
		return this.serial.readBytes(bytes, bytes.length);
	}
	@Override
	public int read(byte[] bytes, final int offset,
			final int len) throws IOException {
		return this.serial.readBytes(
			bytes,
			len
		);
	}



	@Override
	public int available() {
		return this.serial.available();
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
