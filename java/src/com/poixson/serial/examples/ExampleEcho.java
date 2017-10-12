package com.poixson.serial.examples;

import com.poixson.serial.enums.Baud;
import com.poixson.utils.Utils;


public class ExampleEcho implements Runnable {

	private String portName = null;
	private Baud   baud     = null;



	@Override
	public void run() {
	}



	public ExampleEcho setPortName(final String portName) {
		this.portName = (
			Utils.isEmpty(portName)
			? null
			: portName
		);
		return this;
	}
	public ExampleEcho setBaud(final String baudStr) {
		this.baud = Baud.FromString(baudStr);
		return this;
	}



}
