package com.poixson.serial;

import com.poixson.utils.ErrorMode;
import com.poixson.utils.Keeper;
import com.poixson.utils.NativeAutoLoader;


public class LibraryLoader {

	private static volatile LibraryLoader instance = null;
	private static final Object instanceLock = new Object();

	private boolean useD2xxOpen = false;
	private boolean useD2xxProp = false;



	public static LibraryLoader get() {
		if (instance == null) {
			synchronized (instanceLock) {
				if (instance == null) {
					instance = new LibraryLoader();
					Keeper.add(instance);
				}
			}
		}
		return instance;
	}
	private LibraryLoader() {
	}



	public boolean Load() {
		final NativeAutoLoader loader = NativeAutoLoader.get();
		loader
			.setErrorMode(ErrorMode.EXCEPTION)
			.setClassRef(pxnSerial.class)
			.addDefaultSearchPaths()
			.setResourcesPath("lib/linux64/")
			.setLocalLibPath("lib/")
			.enableExtract()
			.enableReplace();
		// load libftdi.so (open)
		if (this.useD2xxOpen) {
			if ( ! loader.Load("libftdi-open-linux64.so") ) {
				throw new RuntimeException("Failed to load libftdi open library!");
			}
		}
		// load libftd2xx.so (prop)
		if (this.useD2xxProp) {
			if ( ! loader.Load("libftdi-prop-linux64.so") ) {
				throw new RuntimeException("Failed to load ftd2xx official library!");
			}
		}
		// load pxnserial.so
		{
			if ( ! loader.Load("pxnserial-linux64.so") ) {
				throw new RuntimeException("Failed to load pxnSerial native library!");
			}
		}
		return true;
	}



	// use open d2xx
	public LibraryLoader useD2xxOpen() {
		return this.useD2xxOpen(true);
	}
	public LibraryLoader noD2xxOpen() {
		return this.useD2xxOpen(false);
	}
	public LibraryLoader useD2xxOpen(final boolean use) {
		this.useD2xxOpen = use;
		return this;
	}



	// use prop d2xx
	public LibraryLoader useD2xxProp() {
		return this.useD2xxProp(true);
	}
	public LibraryLoader noD2xxProp() {
		return this.useD2xxProp(false);
	}
	public LibraryLoader useD2xxProp(final boolean use) {
		this.useD2xxProp = use;
		return this;
	}



}
