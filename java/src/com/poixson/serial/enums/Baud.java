package com.poixson.serial.enums;

import java.util.ArrayList;
import java.util.List;

import com.poixson.utils.NumberUtils;


public enum Baud {

	B50      (50),
	B75      (75),
	B110     (110),
	B134     (134),
	B150     (150),
	B200     (200),
	B300     (300),
	B600     (600),
	B1200    (1200),
	B1800    (1800),
	B2400    (2400),
	B3600    (3600),
	B4000    (4000),
	B4800    (4800),
	B7200    (7200),
	B9600    (9600),
	B14400   (14400),
	B16000   (16000),
	B19200   (19200),
	B28800   (28800),
	B38400   (38400),
	B51200   (51200),
	B56000   (56000),
	B57600   (57600),
	B64000   (64000),
	B76800   (76800),
	B115200  (115200),
	B128000  (128000),
	B153600  (153600),
	B230400  (230400),
	B250000  (250000),
	B256000  (256000),
	B460800  (460800),
	B500000  (500000),
	B576000  (576000),
	B614400  (614400),
	B921600  (921600),
	B1000000 (1000000),
	B1152000 (1152000),
	B1228800 (1228800),
	B1500000 (1500000),
	B1843200 (1843200),
	B2000000 (2000000),
	B2457600 (2457600),
	B2500000 (2500000),
	B3000000 (3000000),
	B3500000 (3500000),
	B3686400 (3686400),
	B4000000 (4000000),
	B6000000 (6000000),
	B12000000(12000000);



	public static final Baud DEFAULT_BAUD = B9600;



	private static final List<Baud> bauds = new ArrayList<Baud>();
	static {
		bauds.add(B50);
		bauds.add(B75);
		bauds.add(B110);
		bauds.add(B134);
		bauds.add(B150);
		bauds.add(B200);
		bauds.add(B300);
		bauds.add(B600);
		bauds.add(B1200);
		bauds.add(B1800);
		bauds.add(B2400);
		bauds.add(B3600);
		bauds.add(B4000);
		bauds.add(B4800);
		bauds.add(B7200);
		bauds.add(B9600);
		bauds.add(B14400);
		bauds.add(B16000);
		bauds.add(B19200);
		bauds.add(B28800);
		bauds.add(B38400);
		bauds.add(B51200);
		bauds.add(B56000);
		bauds.add(B57600);
		bauds.add(B64000);
		bauds.add(B76800);
		bauds.add(B115200);
		bauds.add(B128000);
		bauds.add(B153600);
		bauds.add(B230400);
		bauds.add(B250000);
		bauds.add(B256000);
		bauds.add(B460800);
		bauds.add(B500000);
		bauds.add(B576000);
		bauds.add(B614400);
		bauds.add(B921600);
		bauds.add(B1000000);
		bauds.add(B1152000);
		bauds.add(B1228800);
		bauds.add(B1500000);
		bauds.add(B1843200);
		bauds.add(B2000000);
		bauds.add(B2457600);
		bauds.add(B2500000);
		bauds.add(B3000000);
		bauds.add(B3500000);
		bauds.add(B3686400);
		bauds.add(B4000000);
		bauds.add(B6000000);
		bauds.add(B12000000);
	}



	public final int value;

	private Baud(final int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}



	public static Baud FromInt(final int value) {
		if (value <= 0) {
			return null;
		}
		for (final Baud b : bauds) {
			if (b.value >= value) {
				return b;
			}
		}
		return null;
	}
	public static Baud FromString(final String str) {
		final Integer val = NumberUtils.toInteger(str);
		return (
			val == null
			? null
			: FromInt(val.intValue())
		);
	}



}
