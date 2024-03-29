package com.poixson.serial.enums;

import java.util.ArrayList;
import java.util.List;

import com.poixson.serial.natives.DeviceNative;
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
	@Override
	public String toString() {
		switch (this) {
		case AUTO:
			return "auto";
		case SERIAL:
			return "serial";
		case D2XX:
			return "d2xx";
		case D2XX_OPEN:
			return "d2xx-open";
		case D2XX_PROP:
			return "d2xx-prop";
		default:
			break;
		}
		return null;
	}



	public DeviceNative getNative() {
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



	public static DriverType FromNative(final DeviceNative nat) {
		if (nat instanceof NativeSerial)
			return DriverType.SERIAL;
		if (nat instanceof NativeD2xxOpen)
			return DriverType.D2XX_OPEN;
		if (nat instanceof NativeD2xxProp)
			return DriverType.D2XX_PROP;
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
