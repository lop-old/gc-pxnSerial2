/* PoiXson pxnSerial 1.x
 * copyright 2017
 * license GPL-3
 * lorenzo at poixson.com
 * http://poixson.com/
 */



#include "NativeSerial.h"

#include <jni.h>

#include <stdlib.h>
#include <termios.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/ioctl.h>
#include <errno.h>



/* load/unload */



// natInit()
JNIEXPORT jint JNICALL
Java_com_poixson_serialplus_natives_NativeSerial_natInit
(JNIEnv *env, jobject obj) {
	return 0;
}



// natUnload()
JNIEXPORT jint JNICALL
Java_com_poixson_serialplus_natives_NativeSerial_natUnload
(JNIEnv *env, jobject obj) {
	return 0;
}



/* list devices */



// natGetDeviceList()
JNIEXPORT jobjectArray JNICALL
Java_com_poixson_serial_natives_NativeSerial_natGetDeviceList
(JNIEnv *env, jobject obj) {
	return NULL;
}



/* open/close port */



// natOpenPort(port-name)
JNIEXPORT jlong JNICALL
Java_com_poixson_serial_natives_NativeSerial_natOpenPort
(JNIEnv *env, jobject obj, jstring portName) {
	const char *port = (*env)->GetStringUTFChars(env, portName, 0);
	fprintf(stderr, "Opening serial port: %s\n", port);
	jlong handle = open(port, O_RDWR | O_NOCTTY | O_NDELAY);
	if (handle <= 0) {
		// permission denied
		if (errno == EACCES) {
			fprintf(stderr, "Permission denied to port: %s\n", port);
			handle = NATIVESERIAL_ERROR_PERMISSION_DENIED; // -4
		} else
		// port busy
		if (errno == EBUSY) {
			fprintf(stderr, "Port is busy: %s\n", port);
			handle = NATIVESERIAL_ERROR_PORT_BUSY; // -3
		} else
		// port not found
		if (errno == ENOENT) {
			fprintf(stderr, "Port not found: %s\n", port);
			handle = NATIVESERIAL_ERROR_PORT_NOT_FOUND; // -2
		// unknown fail
		} else {
			fprintf(stderr, "Failed to open port: %s\n", port);
			handle = NATIVESERIAL_ERROR_UNKNOWN_FAIL; // -1
		}
		(*env)->ReleaseStringUTFChars(env, portName, port);
		return handle;
	}
	// exclusive port lock
	ioctl(handle, TIOCEXCL);
	// set blocking
	fcntl(handle, F_SETFL, 0);
	(*env)->ReleaseStringUTFChars(env, portName, port);
	return handle;
}



// natClosePort(handle)
JNIEXPORT jboolean JNICALL
Java_com_poixson_serial_natives_NativeSerial_natClosePort
(JNIEnv *env, jobject obj, jlong handle) {
	if (handle <= 0) {
		return JNI_FALSE;
	}
	// clear exclusive port lock
	ioctl(handle, TIOCNXCL);
	return (
		close(handle) == 0
		? JNI_TRUE
		: JNI_FALSE
	);
}



/* port parameters */



// natSetParams(handle, baud, byte-size, stop-bits, parity, flags)
JNIEXPORT jlong JNICALL
Java_com_poixson_serial_natives_NativeSerial_natSetParams
(JNIEnv *env, jobject obj, jlong handle, jint baud,
jint byteSize, jint stopBits, jint parity, jint flags) {
	if (handle <= 0) {
		return handle;
	}
	struct termios tty;
	if (tcgetattr(handle, &tty) != 0) {
		fprintf(stderr, "Failed to get port attributes\n");
		close(handle);
		handle = NATIVESERIAL_ERROR_INCORRECT_SERIAL_PORT; // -5
		return handle;
	}

	tty.c_cflag |= (CLOCAL | CREAD);
	tty.c_cflag &= ~CRTSCTS;
	tty.c_lflag &= ~(ICANON | ECHO | ECHOE | ECHOK | ECHONL | ECHOCTL | ECHOPRT | ECHOKE | ISIG | IEXTEN);
	tty.c_iflag &= ~(IXON | IXOFF | IXANY | INPCK | IGNPAR | PARMRK | ISTRIP | IGNBRK | BRKINT | INLCR | IGNCR| ICRNL);
#ifdef IUCLC
	tty.c_iflag &= ~IUCLC;
#endif
	tty.c_oflag &= ~OPOST;
	if (flags & IGNPAR) {
		tty.c_iflag |= IGNPAR;
	}
	if (flags & PARMRK) {
		tty.c_iflag |= PARMRK;
	}

	// set baud rate
	speed_t baudValue = GetBaudByNumber(baud);
	if (baudValue > 0) {
		if (cfsetispeed(&tty, baudValue) < 0 || cfsetospeed(&tty, baudValue) < 0) {
			fprintf(stderr, "Failed to set baud rate\n");
			close(handle);
			handle = NATIVESERIAL_ERROR_INCORRECT_SERIAL_PORT; // -5
			return handle;
		}
	}

	// set data bits
	int byteSizeValue = GetByteSizeByNumber(byteSize);
	if (byteSizeValue != -1) {
		tty.c_cflag &= ~CSIZE;
		tty.c_cflag |= byteSize;
	}

	// set stop bits
	if (stopBits == 0) { // 1 bit
		tty.c_cflag &= ~CSTOPB;
	} else
	// 1 = 1.5 bits ; 2 = 2 bits
	if ( (stopBits == 1) || (stopBits == 2) ) {
		tty.c_cflag |= CSTOPB;
	}

	// clear parity
#ifdef PAREXT
	tty.c_cflag &= ~(PARENB | PARODD | PAREXT);
#elif defined CMSPAR
	tty.c_cflag &= ~(PARENB | PARODD | CMSPAR);
#else
	tty.c_cflag &= ~(PARENB | PARODD);
#endif
	// odd parity
	if (parity == 1) {
		tty.c_cflag |= (PARENB | PARODD);
		tty.c_iflag |= INPCK;
	} else
	// even parity
	if (parity == 2) {
		tty.c_cflag |= PARENB;
		tty.c_iflag |= INPCK;
	} else
	// mark parity
	if (parity == 3) {
#ifdef PAREXT
		tty.c_cflag |= (PARENB | PARODD | PAREXT);
		tty.c_iflag |= INPCK;
#elif defined CMSPAR
		tty.c_cflag |= (PARENB | PARODD | CMSPAR);
		tty.c_iflag |= INPCK;
#endif
	} else
	// space parity
	if (parity == 4) {
#ifdef PAREXT
		tty.c_cflag |= (PARENB | PAREXT);
		tty.c_iflag |= INPCK;
#elif defined CMSPAR
		tty.c_cflag |= (PARENB | CMSPAR);
		tty.c_iflag |= INPCK;
#endif
	}

	// set the settings
	if (tcsetattr(handle, TCSAFLUSH, &tty) != 0) {
		fprintf(stderr, "Failed to set port attributes\n");
		close(handle);
		handle = NATIVESERIAL_ERROR_INCORRECT_SERIAL_PORT; // -5
		return handle;
	}
	tcflush(handle, TCIOFLUSH);
	return handle;
}



/*
// natSetBlocking(handle, blocking) - blocking/non-blocking
JNIEXPORT jlong JNICALL
Java_com_poixson_serial_natives_NativeSerial_natSetBlocking
(JNIEnv *env, jobject obj, jlong handle, jboolean blocking) {
	if (handle <= 0) {
		return handle;
	}
	// get current options
	struct termios tty;
	if (tcgetattr(handle, &tty) != 0) {
		fprintf(stderr, "Failed to get port attributes\n");
		close(handle);
		handle = NATIVESERIAL_ERROR_INCORRECT_SERIAL_PORT; // -5
		return handle;
	}

	// set blocking/timeout
	tty.c_cc[VMIN]  = (blocking == JNI_FALSE ? 0 : 1); // Minimum number of characters to read
	tty.c_cc[VTIME] = (blocking == JNI_FALSE ? 0 : 1); // Time to wait for data (tenths of seconds)

	// set the settings
	if (tcsetattr(handle, TCSAFLUSH, &tty) != 0) {
		fprintf(stderr, "Failed to set port attributes\n");
		close(handle);
		handle = NATIVESERIAL_ERROR_INCORRECT_SERIAL_PORT; // -5
		return handle;
	}
	tcflush(handle, TCIOFLUSH);
	return handle;
}



// natSetVMinVTime(handle, vMin, vTime)
JNIEXPORT jlong JNICALL
Java_com_poixson_serial_natives_NativeSerial_natSetVMinVTime
(JNIEnv *env, jobject obj, jlong handle, jint vMin, jint vTime) {
	if (handle <= 0) {
		return handle;
	}
	// get current options
	struct termios tty;
	if (tcgetattr(handle, &tty) != 0) {
		fprintf(stderr, "Failed to get port attributes\n");
		close(handle);
		handle = NATIVESERIAL_ERROR_INCORRECT_SERIAL_PORT; // -5
		return handle;
	}

	// Minimum number of characters to read
	if (vMin  > 0) {
		tty.c_cc[VMIN] = vMin;
	}
	// Time to wait for data (tenths of seconds)
	if (vTime > 0) {
		tty.c_cc[VTIME] = vTime;
	}

	// set the settings
	if (tcsetattr(handle, TCSAFLUSH, &tty) != 0) {
		fprintf(stderr, "Failed to set port attributes\n");
		close(handle);
		handle = NATIVESERIAL_ERROR_INCORRECT_SERIAL_PORT; // -5
		return handle;
	}
	tcflush(handle, TCIOFLUSH);
	return handle;
}
*/



/* line status */



/*
// natGetLineStatus(handle)
JNIEXPORT jbooleanArray JNICALL
Java_com_poixson_serial_natives_NativeSerial_natGetLineStatus
(JNIEnv *env, jobject obj, jlong handle) {
	if (handle <= 0) {
		return handle;
	}
	int status;
	ioctl(handle, TIOCMGET, &status);
	jboolean[] results = new jboolean[4];
	// CTS
	results[0] = (status & TIOCM_CTS ? JNI_TRUE : JNI_FALSE);
	// DSR
	results[1] = (status & TIOCM_DSR ? JNI_TRUE : JNI_FALSE);
	// Ring
	results[2] = (status & TIOCM_RNG ? JNI_TRUE : JNI_FALSE);
	// RLSD(DCD)
	results[3] = (status & TIOCM_CAR ? JNI_TRUE : JNI_FALSE);
	jbooleanArray resultArray = (*env)->NewBooleanArray(env, 4);
	(*env)->SetBooleanArrayRegion(resultArray, 0, 4, results);
	return resultArray;
}



// natSetLineStatus(handle, rts, dtr)
JNIEXPORT jlong JNICALL
Java_com_poixson_serial_natives_NativeSerial_natSetLineStatus
(JNIEnv *env, jobject obj,
jlong handle, jboolean setRTS, jboolean setDTR) {
	if (handle <= 0) {
		return handle;
	}
	// get current options
	struct termios tty;
	if (tcgetattr(handle, &tty) != 0) {
		fprintf(stderr, "Failed to get port attributes\n");
		close(handle);
		handle = NATIVESERIAL_ERROR_INCORRECT_SERIAL_PORT; // -5
		return handle;
	}

	int lineStatus;
	if (ioctl(handle, TIOCMGET, &lineStatus) >= 0) {
		// RTS (request to send)
		if (setRTS == JNI_TRUE) {
			lineStatus |= TIOCM_RTS;
		} else {
			lineStatus &= ~TIOCM_RTS;
		}
		// DTR (data terminal ready)
		if (setDTR == JNI_TRUE) {
			lineStatus |= TIOCM_DTR;
		} else {
			lineStatus &= ~TIOCM_DTR;
		}
		if (ioctl(handle, TIOCMSET, &lineStatus) >= 0) {
			return JNI_TRUE;
		}
	}

	// set the settings
	if (tcsetattr(handle, TCSAFLUSH, &tty) != 0) {
		fprintf(stderr, "Failed to set port attributes\n");
		close(handle);
		handle = NATIVESERIAL_ERROR_INCORRECT_SERIAL_PORT; // -5
		return handle;
	}
	tcflush(handle, TCIOFLUSH);
	return JNI_FALSE;
}
*/



/* bytes in buffers */



/*
// natGetInputBytesCount(handle)
JNIEXPORT jint JNICALL
Java_com_poixson_serial_natives_NativeSerial_natGetInputBytesCount
(JNIEnv *env, jobject obj, jlong handle) {
	if (handle <= 0) {
		return handle;
	}
	jint result = -1;
	ioctl(handle, FIONREAD, &result);
	return result;
}



// natGetOutputBytesCount(handle)
JNIEXPORT jint JNICALL
Java_com_poixson_serial_natives_NativeSerial_natGetOutputBytesCount
(JNIEnv *env, jobject obj, jlong handle) {
	if (handle <= 0) {
		return handle;
	}
	jint result = -1;
	ioctl(handle, TIOCOUTQ, &result);
	return result;
}
*/



/* read/write */



// natReadBytes(handle, bytes, length)
JNIEXPORT jint JNICALL
Java_com_poixson_serial_natives_NativeSerial_natReadBytes
(JNIEnv *env, jobject obj,
jlong handle, jbyteArray bytes, jint len) {
	if (handle <= 0) {
		return handle;
	}
	jbyte *buf = (jbyte*) malloc(len);
	jint result = (jint) read(
		handle,
		buf,
		len
	);
	if (result > 0) {
		(*env)->SetByteArrayRegion(
			env,
			bytes,
			0,
			result,
			buf
		);
	}
	free(buf);
	return result;
}



// natWriteBytes(handle, bytes)
JNIEXPORT jlong JNICALL
Java_com_poixson_serial_natives_NativeSerial_natWriteBytes
(JNIEnv *env, jobject obj, jlong handle, jbyteArray bytes) {
	jbyte* buffer = (*env)->GetByteArrayElements(env, bytes, JNI_FALSE);
	jint size = (*env)->GetArrayLength(env, bytes);
//TODO: remove this
fprintf(stderr, "WRITING: %s\n", buffer);
	jint result =
		write(
			handle,
			buffer,
			(size_t)size
		);
	(*env)->ReleaseByteArrayElements(
		env,
		bytes,
		buffer,
		0
	);
	return (
		result == size
		? JNI_TRUE
		: JNI_FALSE
	);
}



speed_t GetBaudByNumber(jint baud) {
	if (baud <= 0)     return B0;
	if (baud <= 50)    return B50;
	if (baud <= 75)    return B75;
	if (baud <= 110)   return B110;
	if (baud <= 134)   return B134;
	if (baud <= 150)   return B150;
	if (baud <= 200)   return B200;
	if (baud <= 300)   return B300;
	if (baud <= 600)   return B600;
	if (baud <= 1200)  return B1200;
	if (baud <= 1800)  return B1800;
	if (baud <= 2400)  return B2400;
	if (baud <= 4800)  return B4800;
	if (baud <= 9600)  return B9600;
	if (baud <= 19200) return B19200;
	if (baud <= 38400) return B38400;
#ifdef B57600
	if (baud <= 57600) return B57600;
#endif
#ifdef B115200
	if (baud <= 115200) return B115200;
#endif
#ifdef B230400
	if (baud <= 230400) return B230400;
#endif
#ifdef B460800
	if (baud <= 460800) return B460800;
#endif
#ifdef B500000
	if (baud <= 500000) return B500000;
#endif
#ifdef B576000
	if (baud <= 576000) return B576000;
#endif
#ifdef B921600
	if (baud <= 921600) return B921600;
#endif
#ifdef B1000000
	if (baud <= 1000000) return B1000000;
#endif
#ifdef B1152000
	if (baud <= 1152000) return B1152000;
#endif
#ifdef B1500000
	if (baud <= 1500000) return B1500000;
#endif
#ifdef B2000000
	if (baud <= 2000000) return B2000000;
#endif
#ifdef B2500000
	if (baud <= 2500000) return B2500000;
#endif
#ifdef B3000000
	if (baud <= 3000000) return B3000000;
#endif
#ifdef B3500000
	if (baud <= 3500000) return B3500000;
#endif
#ifdef B4000000
	if (baud <= 4000000) return B4000000;
#endif
	return B0;
}



int GetByteSizeByNumber(jint byteSize) {
	if (byteSize == 5) return CS5;
	if (byteSize == 6) return CS6;
	if (byteSize == 7) return CS7;
	if (byteSize == 8) return CS8;
	return -1;
}
