package com.poixson.serial.examples;

import com.poixson.serial.pxnSerial;
import com.poixson.serial.pxnSerialFactory;
import com.poixson.serial.enums.Baud;
import com.poixson.utils.ThreadUtils;
import com.poixson.utils.Utils;


public class ExampleEcho extends AbstractExample {



	@Override
	public void run() {
		super.run();
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



}
