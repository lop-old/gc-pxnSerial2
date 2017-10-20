package com.poixson.serial.examples;

import com.poixson.serial.pxnSerial;
import com.poixson.serial.pxnSerialFactory;
import com.poixson.serial.enums.Baud;
import com.poixson.utils.ThreadUtils;
import com.poixson.utils.Utils;


public class ExampleEcho implements Runnable {

	private String portName = null;
	private Baud   baud     = null;



	@Override
	public void run() {
		final String portName = this.getPortName();
		final Baud   baud     = this.getBaud();
		final pxnSerial serial =
			pxnSerialFactory.get()
				.setPortName(portName)
				.setBaud(baud)
				.build();
		if (!serial.open()) {
			System.out.println("Failed to open serial port: "+portName);
			System.exit(1);
		}

		for (int i=0; i<10; i++) {
			final String str = serial.readString();
			System.out.println("GOT DATA: "+str);
			ThreadUtils.Sleep(100L);
		}

		Utils.safeClose(serial);
	}



	// port name
	public String getPortName() {
		final String portName = this.portName;
		return (
			Utils.isEmpty(portName)
			? "/dev/ttyUSB0"
			: portName
		);
	}
	public ExampleEcho setPortName(final String portName) {
		this.portName = (
			Utils.isEmpty(portName)
			? null
			: portName
		);
		return this;
	}



	// baud rate
	public Baud getBaud() {
		final Baud baud = this.baud;
		return (
			baud == null
			? Baud.DEFAULT_BAUD
			: baud
		);
	}
	public ExampleEcho setBaud(final int baudValue) {
		return this.setBaud(
			Baud.FromInt(baudValue)
		);
	}
	public ExampleEcho setBaud(final String baudStr) {
		return this.setBaud(
			Baud.FromString(baudStr)
		);
	}
	public ExampleEcho setBaud(final Baud baud) {
		this.baud = baud;
		return this;
	}



}
