package com.poixson.serial.enums;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.poixson.serial.ConfigDAO;
import com.poixson.serial.DeviceNative;
import com.poixson.serial.natives.NativeD2xxOpen;
import com.poixson.serial.natives.NativeD2xxProp;
import com.poixson.serial.natives.NativeSerial;
import com.poixson.utils.Utils;


public enum DriverType {

	SERIAL    (0x0),
	D2XX_OPEN (0x1),
	D2XX_PROP (0x2),
	D2XX      (0x3),
	AUTO      (0x7);



	public static final DriverType DEFAULT_DRIVER_MODE = AUTO;



	private static final List<DriverType> driverTypes = new ArrayList<DriverType>();
	static {
		driverTypes.add(AUTO);
		driverTypes.add(SERIAL);
		driverTypes.add(D2XX);
		driverTypes.add(D2XX_OPEN);
		driverTypes.add(D2XX_PROP);
	}



	public final int value;

	private DriverType(final int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}



	public DeviceNative getNative(final ConfigDAO cfg) throws IOException {
		switch (this) {
		case SERIAL:
			return NativeSerial.get();
		case D2XX_OPEN:
			return NativeD2xxOpen.get();
		case D2XX_PROP:
			return NativeD2xxProp.get();
		case D2XX:
//TODO:
			break;
		case AUTO:
//TODO:
			break;
		default:
//TODO:
			break;
		}
		return null;
	}



	public static DriverType FromString(final String str) {
		if (Utils.isEmpty(str))
			return null;
		String match = str.toLowerCase();
//		StringUtils.
//		match = str.replace("");
		switch (match) {
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
