package com.poixson.serial.enums;

import java.util.ArrayList;
import java.util.List;

import com.poixson.utils.NumberUtils;


public enum DataBits {

	DATA_BITS_5 (5),
	DATA_BITS_6 (6),
	DATA_BITS_7 (7),
	DATA_BITS_8 (8),
	DATA_BITS_9 (9);



	public static final DataBits DEFAULT_BYTE_SIZE = DATA_BITS_8;



	private static final List<DataBits> bits = new ArrayList<DataBits>();
	static {
		bits.add(DATA_BITS_5);
		bits.add(DATA_BITS_6);
		bits.add(DATA_BITS_7);
		bits.add(DATA_BITS_8);
		bits.add(DATA_BITS_9);
	}



	public final int value;

	private DataBits(final int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}



	public static DataBits FromInt(final int value) {
		if (value <= 0) {
			return null;
		}
		for (final DataBits b : bits) {
			if (b.value >= value) {
				return b;
			}
		}
		return null;
	}
	public static DataBits FromString(final String str) {
		final Integer val = NumberUtils.toInteger(str);
		return (
			val == null
			? null
			: FromInt(val.intValue())
		);
	}



}
