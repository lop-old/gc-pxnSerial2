package com.poixson.serial.enums;

import com.poixson.utils.Utils;


public enum StopBits {

	STOP_BITS_1  (1),
	STOP_BITS_1_5(2),
	STOP_BITS_2  (3);



	public static final StopBits DEFAULT_STOP_BITS = STOP_BITS_1;



	public final int value;

	private StopBits(final int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}



	public static StopBits FromInt(final int value) {
		if (value <= 0) {
			return null;
		}
		switch (value) {
		case 1:
			return STOP_BITS_1;
		case 15:
			return STOP_BITS_1_5;
		case 2:
			return STOP_BITS_2;
		}
		return null;
	}
	public static StopBits FromDouble(final double value) {
		if (value <= 0) {
			return null;
		}
		if (value == 1.0)
			return STOP_BITS_1;
		if (value == 1.5)
			return STOP_BITS_1_5;
		if (value == 2.0)
			return STOP_BITS_2;
		return null;
	}
	public static StopBits FromString(final String str) {
		if (Utils.isEmpty(str)) {
			return null;
		}
		switch (str.toLowerCase()) {
		case "1":
			return STOP_BITS_1;
		case "1.5":
		case "1_5":
		case "15":
			return STOP_BITS_1_5;
		case "2":
			return STOP_BITS_2;
		}
		return null;
	}



}
