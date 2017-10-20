package com.poixson.serial;

import com.poixson.serial.enums.Baud;
import com.poixson.serial.enums.DataBits;
import com.poixson.serial.enums.Parity;
import com.poixson.serial.enums.StopBits;
import com.poixson.utils.Utils;
import com.poixson.utils.exceptions.RequiredArgumentException;


public class ConfigDAO {

	public static final boolean DEFAULT_RTS = false;
	public static final boolean DEFAULT_DTR = false;

	public static final int DEFAULT_FLAGS = 0;

	private final String   portName;
	private final Baud     baud;
	private final DataBits byteSize;
	private final StopBits stopBits;
	private final Parity   parity;
	private final boolean  setRTS;
	private final boolean  setDTR;
	private final int      flags;



	public ConfigDAO(final String portName) {
		this(
			portName,
			null // baud
		);
	}
	public ConfigDAO(final String portName, final int baudValue) {
		this(
			portName,
			Baud.FromInt(baudValue)
		);
	}
	public ConfigDAO(final String portName, final Baud baud) {
		this(
			portName,
			baud,
			null, // byte size
			null, // stop bits
			null  // parity
		);
	}
	public ConfigDAO(final String portName, final Baud baud,
			final DataBits byteSize, final StopBits stopBits, final Parity parity) {
		this(
			portName,
			baud,
			byteSize,
			stopBits,
			parity,
			DEFAULT_RTS,
			DEFAULT_DTR
		);
	}
	public ConfigDAO(final String portName, final Baud baud,
			final DataBits byteSize, final StopBits stopBits, final Parity parity,
			final boolean rts, final boolean dtr) {
		this(
			portName,
			baud,
			byteSize,
			stopBits,
			parity,
			rts,
			dtr,
			DEFAULT_FLAGS
		);
	}
	public ConfigDAO(final String portName, final Baud baud,
			final DataBits byteSize, final StopBits stopBits, final Parity parity,
			final boolean setRTS, final boolean setDTR, final int flags) {
		if (Utils.isEmpty(portName)) throw new RequiredArgumentException("portName");
		if (flags < 0) throw new IllegalArgumentException("flags cannot be negative: "+Integer.toString(flags));
		this.portName = portName;
		this.baud     = ( baud     == null ? Baud.DEFAULT_BAUD          : baud     );
		this.byteSize = ( byteSize == null ? DataBits.DEFAULT_BYTE_SIZE : byteSize );
		this.stopBits = ( stopBits == null ? StopBits.DEFAULT_STOP_BITS : stopBits );
		this.parity   = ( parity   == null ? Parity.DEFAULT_PARITY      : parity   );
		this.setRTS   = setRTS;
		this.setDTR   = setDTR;
		this.flags    = flags;
	}



	public String getPortName() {
		return this.portName;
	}



	public Baud getBaud() {
		return this.baud;
	}
	public String getBaudStr() {
		return this.baud.toString();
	}
	public int getBaudValue() {
		return this.baud.getValue();
	}



	public DataBits getDataBits() {
		return this.byteSize;
	}
	public int getDataBitsValue() {
		return this.byteSize.getValue();
	}



	public StopBits getStopBits() {
		return this.stopBits;
	}
	public int getStopBitsValue() {
		return this.stopBits.getValue();
	}



	public Parity getParity() {
		return this.parity;
	}
	public int getParityValue() {
		return this.parity.getValue();
	}



	public boolean getRTS() {
		return this.setRTS;
	}
	public boolean getDTR() {
		return this.setDTR;
	}



	public int getFlags() {
		return this.flags;
	}



}
