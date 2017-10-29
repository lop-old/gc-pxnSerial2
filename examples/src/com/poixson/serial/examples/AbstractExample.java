package com.poixson.serial.examples;

import com.poixson.serial.enums.Baud;
import com.poixson.utils.Utils;


public abstract class AbstractExample implements Runnable {

	private String portName = null;
	private Baud   baud     = null;



	public AbstractExample() {
	}
	public AbstractExample(final String portName, final Baud baud) {
		this.portName = portName;
		this.baud     = baud;
	}



	@Override
	public void run() {
		System.out.println(
			(new StringBuilder())
				.append("Using port: ")
				.append(this.getPortName())
				.append("  baud: ")
				.append(this.getBaudStr())
				.toString()
		);
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
	public AbstractExample setPortName(final String portName) {
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
	public int getBaudInt() {
		final Baud baud = this.getBaud();
		return baud.getValue();
	}
	public String getBaudStr() {
		final Baud baud = this.getBaud();
		return baud.toString();
	}
	public AbstractExample setBaud(final Baud baud) {
		this.baud = baud;
		return this;
	}
	public AbstractExample setBaud(final int baudValue) {
		return this.setBaud(
			Baud.FromInt(baudValue)
		);
	}
	public AbstractExample setBaud(final String baudStr) {
		return this.setBaud(
			Baud.FromString(baudStr)
		);
	}



}
