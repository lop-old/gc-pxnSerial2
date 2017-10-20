package com.poixson.serial;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.concurrent.atomic.AtomicLong;

import com.poixson.serial.enums.SerialState;
import com.poixson.utils.ErrorMode;
import com.poixson.utils.ReflectUtils;
import com.poixson.utils.Utils;
import com.poixson.utils.xCloseable;
import com.poixson.utils.exceptions.IORuntimeException;
import com.poixson.utils.exceptions.RequiredArgumentException;
import com.poixson.utils.xLogger.xLog;


public class pxnSerial implements xCloseable {

	public static final boolean DEFAULT_RTS = false;
	public static final boolean DEFAULT_DTR = false;
	public static final int     DEFAULT_FLAGS = 0;

	private static volatile boolean hasLoadedLibraries = false;
	private static final Object hasLoadedLibrariesLock = new Object();

	public static final ErrorMode DEFAULT_ERROR_MODE = ErrorMode.EXCEPTION;
	protected volatile ErrorMode errorMode = null;

	private final ConfigDAO cfg;
	private final DeviceNative nat;

	private final AtomicLong handle = new AtomicLong(0L);



	public pxnSerial(final ConfigDAO cfg, final DeviceNative nat) {
		if (cfg == null) throw new RequiredArgumentException("cfg");
		if (nat == null) throw new RequiredArgumentException("nat");
		this.cfg = cfg;
		this.nat = nat;
	}
	public void finalize() {
		Utils.safeClose(this);
		this.nat.unload();
	}



	public static void LoadLibraries() {
		if ( ! hasLoadedLibraries) {
			synchronized (hasLoadedLibrariesLock) {
				if ( ! hasLoadedLibraries) {
					hasLoadedLibraries = true;
					LibraryLoader.get()
						.Load();
				}
			}
		}
	}



	// ------------------------------------------------------------------------------- //



	public ConfigDAO getConfig() {
		return this.cfg;
	}
	public DeviceNative getNative() {
		return this.nat;
	}



	// error mode
	public ErrorMode getErrorMode() {
		return this.errorMode;
	}
	public pxnSerial setErrorMode(final ErrorMode mode) {
		this.errorMode = mode;
		return this;
	}



	public String getPortName() {
		return this.cfg.getPortName();
	}
	public String getBaudStr() {
		return this.cfg.getBaudStr();
	}



	// ------------------------------------------------------------------------------- //
	// open/close port



	// load driver and open port
	@SuppressWarnings("resource")
	public boolean open() {
		final ErrorMode errorMode = this.getErrorMode();
		// check port state
		if (this.handle.get() > 0L) {
//TODO:
throw new IORuntimeException("Port is already open: "+this.getPortName());
		}
		if (!this.handle.compareAndSet(0L, Integer.MIN_VALUE)) {
//TODO:
throw new IORuntimeException("Port is in an invalid state: "+this.getPortName());
		}
		// open the port
		{
			final long handle =
				this.nat.openPort(this.getPortName());
			if (handle <= 0L) {
//TODO: how will I handle error messages?
				throw new IORuntimeException(
					(new StringBuilder())
						.append("Failed to open port: ")
						.append(this.getPortName())
						.append(" ")
						.append(handle)
//						.append("- ")
//						.append(this.getErrorMsg())
						.toString()
				);
			}
			// set params
//TODO:
			
			
			
			
			
		}
		
		
		
return false;
	}



	@Override
	public void close() throws IOException {
		final long handle = this.getHandle();
		if (handle == 0L)
			throw new IOException("Port is already closed: "+this.getPortName());
		if (!this.handle.compareAndSet(handle, 0L))
			throw new IOException("Port is already closed: "+this.getPortName());
		if (!this.nat.closePort(handle)) {
//TODO:
			throw new IOException(
				(new StringBuilder())
					.append("Unknown problem closing port: ")
					.append(this.getPortName())
//					.append(" ")
//					.append(this.getErrorMsg())
					.toString()
			);
		}
	}



	@Override
	public boolean isClosed() {
		return (this.handle.get() <= 0L);
	}
	public boolean isOpen() {
		return (this.handle.get() > 0L);
	}
	public boolean isFailed() {
		return (this.handle.get() < 0L);
	}



	public long getHandle() {
		return this.handle.get();
	}



	public SerialState getState() {
		if (this.nat == null)
			return SerialState.CLOSED;
		final long handle = this.getHandle();
		if (handle > 0L)
			return SerialState.CONNECTED;
		if (handle < 0L)
			return SerialState.FAILED;
		return SerialState.CLOSED;
	}



	// ------------------------------------------------------------------------------- //
	// read data



	// read bytes
	public byte[] read() {
//TODO:
return null;
	}
	// read string
	public String readString() {
		final byte[] bytes = this.read();
		if (bytes == null)
			return null;
//TODO: add charset
		return new String(bytes);
	}
	// read line
	public String readLine() {
//TODO:
return null;
	}
	// read hex
	public String readHex() {
//TODO:
return null;
	}



	// ------------------------------------------------------------------------------- //
	// write data



	// write bytes
	public boolean write(final byte[] bytes) {
//TODO:
return false;
	}
	// write string
	public boolean writeString(final String str) {
//TODO:
return false;
	}
	// write line
	public boolean writeLine(final String line) {
//TODO:
return false;
	}



	// ------------------------------------------------------------------------------- //
	// logger



	// logger
	private volatile xLog _log_override = null;
	public xLog log() {
		// use override logger
		{
			final xLog log = this._log_override;
			if (log != null)
				return log;
		}
		// use default logger
		return this.logSoft();
	}
	public pxnSerial setLog(final xLog log) {
		this._log_override = log;
		return this;
	}

	private volatile SoftReference<xLog> _log_soft = null;
	private volatile String _className = null;
	private xLog logSoft() {
		if (this._log_soft != null) {
			final xLog log = this._log_soft.get();
			if (log != null)
				return log;
		}
		if (this._className == null) {
			this._className =
				ReflectUtils.getClassName(
					this.getClass()
				);
		}
		final StringBuilder str = new StringBuilder();
		str.append(this._className).append(" ").append(this.cfg.getPortName());
		final xLog log =
			xLog.getRoot()
				.get(str.toString());
		this._log_soft = new SoftReference<xLog>(log);
		return log;
	}



}
