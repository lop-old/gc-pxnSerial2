package com.poixson.serial.exceptions;

import java.security.InvalidParameterException;

import com.poixson.utils.StringUtils;


public class SerialInvalidParameterException extends InvalidParameterException {
	private static final long serialVersionUID = 1L;



	public SerialInvalidParameterException(final String argName) {
		super(
			StringUtils.FormatMessage(
				"Invalid parameter for {}",
				argName
			)
		);
	}
	public SerialInvalidParameterException(final String argName, final Object argValue) {
		super(
			StringUtils.FormatMessage(
				"Invalid parameter for {}: {}",
				argName,
				argValue
			)
		);
	}
	public SerialInvalidParameterException() {
		super();
	}



}
