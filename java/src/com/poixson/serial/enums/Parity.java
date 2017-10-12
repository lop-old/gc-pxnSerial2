package com.poixson.serial.enums;

import com.poixson.utils.Utils;


public enum Parity {

	PARITY_NONE (0),
	PARITY_ODD  (1),
	PARITY_EVEN (2),
	PARITY_MARK (3),
	PARITY_SPACE(4);



	public static final Parity DEFAULT_PARITY = PARITY_NONE;



	public final int value;

	private Parity(final int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}



	public static Parity FromInt(final int value) {
		for (final Parity p : Parity.values()) {
			if (p.value == value)
				return p;
		}
		return null;
	}
	public static Parity FromString(final String str) {
		if (Utils.isEmpty(str)) {
			return null;
		}
		switch (str.toLowerCase()) {
		case "none":
		case "n":
			return PARITY_NONE;
		case "odd":
		case "o":
			return PARITY_ODD;
		case "even":
		case "e":
			return PARITY_EVEN;
		case "mark":
		case "m":
			return PARITY_MARK;
		case "space":
		case "s":
			return PARITY_SPACE;
		}
		return null;
	}



}
