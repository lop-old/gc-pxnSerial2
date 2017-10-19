package com.poixson.serial.enums;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.poixson.serial.ConfigDAO;
import com.poixson.serial.DeviceDriver;
import com.poixson.serial.drivers.DriverD2xx;
import com.poixson.serial.drivers.DriverSerial;
import com.poixson.utils.Utils;


public enum DriverMode {

	AUTO   (0),
	SERIAL (1),
	D2XX   (2);



	public static final DriverMode DEFAULT_DRIVER_MODE = AUTO;



	private static final List<DriverMode> modes = new ArrayList<DriverMode>();
	static {
		modes.add(AUTO);
		modes.add(SERIAL);
		modes.add(D2XX);
	}



	public final int value;

	private DriverMode(final int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}



	public DeviceDriver getDriver(final ConfigDAO cfg)
			throws IOException {
		switch (this) {
		case AUTO:
//TODO:
return null;
		case SERIAL:
			return new DriverSerial(cfg);
		case D2XX:
			return new DriverD2xx(cfg);
		default:
		}
		return null;
	}



	public static DriverMode FromString(final String str) {
		if (Utils.isEmpty(str))
			return null;
		switch (str.toLowerCase()) {
		case "auto":
			return AUTO;
		case "serial":
			return SERIAL;
		case "d2xx":
			return D2XX;
		default:
		}
		return null;
	}



}
