package com.poixson.serial.examples;

import com.poixson.serial.enums.Baud;
import com.poixson.utils.Utils;


public class ExampleEcho implements Runnable {

	private String portName = null;
	private Baud   baud     = null;



	@Override
	public void run() {
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
