package com.poixson.serial;

import com.poixson.serial.enums.Baud;
import com.poixson.serial.enums.DataBits;
import com.poixson.serial.enums.DriverType;
import com.poixson.serial.enums.Parity;
import com.poixson.serial.enums.StopBits;
import com.poixson.serial.exceptions.SerialInvalidParameterException;
import com.poixson.serial.natives.NativeSerial;
import com.poixson.utils.ErrorMode;
import com.poixson.utils.Utils;


public class pxnSerialFactory {

//	public static final long DEFAULT_READ_TIMEOUT  = 1000L;
//	public static final long DEFAULT_READ_INTERVAL = 100L;

	private DriverType driverType = null;

	private String   portName = null;
	private Baud     baud     = null;

	private DataBits byteSize = null;
	private StopBits stopBits = null;
	private Parity   parity   = null;

	private Boolean  rts      = null;
	private Boolean  dtr      = null;
	private Integer  flags    = null;

	private ErrorMode errorMode = null;

//	private final xTime readTimeout  = xTime.get();
//	private final xTime readInterval = xTime.get();



	public static pxnSerialFactory get() {
		return new pxnSerialFactory();
	}
	public static pxnSerialFactory get(final String portName) {
		return new pxnSerialFactory(portName);
	}
	public static pxnSerialFactory get(final String portName, final int baud) {
		return new pxnSerialFactory(portName, baud);
	}
	public static pxnSerialFactory get(final String portName, final Baud baud) {
		return new pxnSerialFactory(portName, baud);
	}
	public static pxnSerialFactory get(final String portName, final Baud baud, final DriverType driverType) {
		return new pxnSerialFactory(portName, baud, driverType);
	}



	public pxnSerialFactory() {
		LoadLibraries();
	}
	public pxnSerialFactory(final String portName) {
		this();
		this.setPortName(portName);
	}
	public pxnSerialFactory(final String portName, final int baud) {
		this();
		this.setPortName(portName);
		this.setBaud(baud);
	}
	public pxnSerialFactory(final String portName, final Baud baud) {
		this();
		this.setPortName(portName);
		this.setBaud(baud);
	}
	public pxnSerialFactory(final String portName, final Baud baud, final DriverType driverType) {
		this();
		this.setPortName(portName);
		this.setBaud(baud);
		this.setDriverType(driverType);
	}



	public static void LoadLibraries() {
		pxnSerial.LoadLibraries();
	}



	public pxnSerial build() throws SerialInvalidParameterException {
		final ConfigDAO    cfg = this.getConfig();
		final DeviceNative nat = this.getNative();
		final pxnSerial serial = new pxnSerial(cfg, nat);
		serial.setErrorMode(this.getErrorMode());
		return serial;
	}
	public ConfigDAO getConfig() throws SerialInvalidParameterException {
		final String portName = this.getPortName();
		if (Utils.isBlank(portName))
			throw new SerialInvalidParameterException("portName is required");
		return
			new ConfigDAO(
				portName,
				this.getBaud(),
				this.getByteSize(),
				this.getStopBits(),
				this.getParity(),
				this.getRTS(),
				this.getDTR(),
				this.getFlagsInt()
//TODO:
//				readTimeout,
//				readInterval
			);
	}
	public DeviceNative getNative() {
		final DriverType driverType = this.getDriverType();
		return (
			driverType == null
			? NativeSerial.get()
			: driverType.getNative()
		);
	}



	// error mode
	public ErrorMode getErrorMode() {
		return this.errorMode;
	}
	public pxnSerialFactory setErrorMode(final ErrorMode mode) {
		this.errorMode = mode;
		return this;
	}



	// driver type
	public String getDriverTypeStr() {
		final DriverType driverType = this.getDriverType();
		return (
			driverType == null
			? null
			: driverType.toString()
		);
	}
	public DriverType getDriverType() {
		return this.driverType;
	}
	public pxnSerialFactory setDriverType(final DriverType driverType ) {
		this.driverType = driverType;
		return this;
	}



	// port name
	public String getPortName() {
		return this.portName;
	}
	public pxnSerialFactory setPortName(final String portName) {
		this.portName = portName;
		return this;
	}



	// baud
	public Baud getBaud() {
		return this.baud;
	}
	public int getBaudInt() {
		final Baud baud = this.getBaud();
		return (
			baud == null
			? -1
			: baud.value
		);
	}
	public pxnSerialFactory setBaud(final Baud baud) {
		this.baud = baud;
		return this;
	}
	public pxnSerialFactory setBaud(final int value)
			throws SerialInvalidParameterException {
		if (value == -1) {
			return this.setBaud(null);
		}
		final Baud baud = Baud.FromInt(value);
		if (baud == null) throw new SerialInvalidParameterException("baud", value);
		return this.setBaud(baud);
	}



	// bit size
	public DataBits getByteSize() {
		return this.byteSize;
	}
	public int getDataBitsInt() {
		final DataBits bits = this.getByteSize();
		return (
			bits == null
			? -1
			: bits.value
		);
	}
	public pxnSerialFactory setByteSize(final DataBits bits) {
		this.byteSize = bits;
		return this;
	}
	public pxnSerialFactory setByteSize(final int value)
			throws SerialInvalidParameterException {
		if (value == -1) {
			return this.setByteSize(null);
		}
		final DataBits bits = DataBits.FromInt(value);
		if (bits == null) throw new SerialInvalidParameterException("bit size", value);
		return this.setByteSize(bits);
	}



	// stop bits
	public StopBits getStopBits() {
		return this.stopBits;
	}
	public pxnSerialFactory setStopBits(final StopBits bits) {
		this.stopBits = bits;
		return this;
	}
	public pxnSerialFactory setStopBits(final int value)
			throws SerialInvalidParameterException {
		if (value == -1) {
			return this.setStopBits( (StopBits)null );
		}
		final StopBits bits = StopBits.FromInt(value);
		if (bits == null) throw new SerialInvalidParameterException("stop bits", value);
		return this.setStopBits(bits);
	}
	public pxnSerialFactory setStopBits(final double value)
			throws SerialInvalidParameterException {
		if (value == -1.0) {
			return this.setStopBits( (StopBits)null );
		}
		final StopBits bits = StopBits.FromDouble(value);
		if (bits == null) throw new SerialInvalidParameterException("stop bits", value);
		return this.setStopBits(bits);
	}
	public pxnSerialFactory setStopBits(final String value)
			throws SerialInvalidParameterException {
		if (Utils.isEmpty(value)) {
			return this.setStopBits( (StopBits)null );
		}
		final StopBits bits = StopBits.FromString(value);
		if (bits == null) throw new SerialInvalidParameterException("stop bits", value);
		return this.setStopBits(bits);
	}



	// parity
	public Parity getParity() {
		return this.parity;
	}
	public int getParityInt() {
		final Parity parity = this.getParity();
		return (
			parity == null
			? -1
			: parity.value
		);
	}
	public pxnSerialFactory setParity(final Parity parity) {
		this.parity = parity;
		return this;
	}
	public pxnSerialFactory setParity(final String value)
			throws SerialInvalidParameterException {
		if (Utils.isEmpty(value)) {
			return this.setParity( (Parity)null );
		}
		final Parity parity = Parity.FromString(value);
		if (parity == null) throw new SerialInvalidParameterException("parity", value);
		return this.setParity(parity);
	}



	// line status
	public boolean getRTS() {
		final Boolean value = this.rts;
		return (
			value == null
			? pxnSerial.DEFAULT_RTS
			: value.booleanValue()
		);
	}
	public boolean getDTR() {
		final Boolean value = this.dtr;
		return (
			value == null
			? pxnSerial.DEFAULT_DTR
			: value.booleanValue()
		);
	}
	public pxnSerialFactory setRTS(final boolean value) {
		this.rts = Boolean.valueOf(value);
		return this;
	}
	public pxnSerialFactory setDTR(final boolean value) {
		this.dtr = Boolean.valueOf(value);
		return this;
	}



	// flags
	public int getFlagsInt() {
		final Integer flags = this.flags;
		return (
			flags == null
			? pxnSerial.DEFAULT_FLAGS
			: flags.intValue()
		);
	}
	public pxnSerialFactory setFlags(final int flags) {
		this.flags = Integer.valueOf(flags);
		return this;
	}



//TODO:
//	// blocking/non-blocking
//	public pxnSerialFactory setBlocking(final boolean blocking) {
//		this.blocking = blocking;
//		return this;
//	}
//	public pxnSerialFactory setBlocking() {
//		return this.setBlocking(true);
//	}
//	public pxnSerialFactory setNonBlocking() {
//		return this.setBlocking(false);
//	}



}
