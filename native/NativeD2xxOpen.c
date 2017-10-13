/* PoiXson pxnSerial 1.x
 * copyright 2017
 * license GPL-3
 * lorenzo at poixson.com
 * http://poixson.com/
 */



#include "NativeD2xxOpen.h"

#include <jni.h>

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <sys/types.h>
#include <ctype.h>
#include <unistd.h>

// open source library
#include "ftdi/open/ftdi.h"



/* list devices */



// natGetDeviceList()
JNIEXPORT jobjectArray JNICALL
Java_com_poixson_serial_natives_NativeD2xxOpen_natGetDeviceList
(JNIEnv *env, jobject obj) {
	struct ftdi_context* ftdi;
	ftdi = ftdi_new();
	if (ftdi == 0) {
		fprintf(stderr, "ftdi_new() failed\n");
		return NULL;
	}
	// get device serial numbers
	struct ftdi_device_list* devList;
	int result;
	result = ftdi_usb_find_all(
		ftdi,
		&devList,
		0, 0 // default vendor/product id's
	);
	if (result < 0) {
		fprintf(stderr, "ftdi_usb_find_all() failed: %d (%s)\n", result, ftdi_get_error_string(ftdi));
		ftdi_free(ftdi);
		return NULL;
	}
	int count = result;
	if (count > MAX_DEVICES) {
		fprintf(stderr, "Found %d devices, more than max %d devices", count, MAX_DEVICES);
		count = MAX_DEVICES;
	}
	struct ftdi_device_list* dev = devList;
	char manu  [SERIAL_SIZE];
	char desc  [SERIAL_SIZE];
	char *list [MAX_DEVICES];
	char serialBuf [MAX_DEVICES] [SERIAL_SIZE];
	int index = 0;
	int total = count;
	while (dev!=NULL) {
		result = ftdi_usb_get_strings2(
			ftdi,
			dev->dev,
			manu,   SERIAL_SIZE,
			desc,   SERIAL_SIZE,
			serialBuf[index], SERIAL_SIZE
		);
		list[index] = serialBuf[index];
		if (result < 0) {
			fprintf(stderr, "ftdi_usb_get_strings2(%d) failed: %d, (%s)\n", index, result, ftdi_get_error_string(ftdi));
			ftdi_list_free(&devList);
			ftdi_free(ftdi);
			return NULL;
		}
		total += strlen(list[index]);
		dev = dev->next;
		index++;
	}
	ftdi_list_free(&devList);
	ftdi_free(ftdi);
	// merge serial numbers to byte array
	char resultBytes [total];
	int pos = 0;
	for (int i=0; i<count; i++) {
		int len = strlen(list[i]);
		memcpy(
			resultBytes + pos,
			list[i],
			len
		);
		pos += len;
		resultBytes[pos++] = 0;
	}
	// convert to java byte array
	jbyteArray jbytes = (*env)->NewByteArray(env, total);
	(*env)->SetByteArrayRegion(env, jbytes, 0, total, (jbyte*) resultBytes);
	return jbytes;
}



/* open/close port */



// natOpenPort(port-name)
JNIEXPORT jlong JNICALL
Java_com_poixson_serial_natives_NativeD2xxOpen_natOpenPort
(JNIEnv *env, jobject obj, jstring portName) {
return 0;
}



// natClosePort(handle)
JNIEXPORT jboolean JNICALL
Java_com_poixson_serial_natives_NativeD2xxOpen_natClosePort
(JNIEnv *env, jobject obj, jlong handle) {
return JNI_FALSE;
}



/* port parameters */



// natSetParams(handle, baud, byte-size, stop-bits, parity, flags)
JNIEXPORT jlong JNICALL
Java_com_poixson_serial_natives_NativeD2xxOpen_natSetParams
(JNIEnv *env, jobject obj, jlong handle, jint baud,
jint byteSize, jint stopBits, jint parity, jint flags) {
return 0;
}



// natSetBlocking(handle, blocking) - blocking/non-blocking
JNIEXPORT jlong JNICALL
Java_com_poixson_serial_natives_NativeD2xxOpen_natSetBlocking
(JNIEnv *env, jobject obj, jlong handle, jboolean blocking) {
return 0;
}



// natSetVMinVTime(handle, vMin, vTime)
JNIEXPORT jlong JNICALL
Java_com_poixson_serial_natives_NativeD2xxOpen_natSetVMinVTime
(JNIEnv *env, jobject obj, jlong handle, jint vMin, jint vTime) {
return 0;
}



/* line status */



// natSetLineStatus(handle, rts, dtr)
JNIEXPORT jlong JNICALL
Java_com_poixson_serial_natives_NativeD2xxOpen_natSetLineStatus
(JNIEnv *env, jobject obj,
jlong handle, jboolean setRTS, jboolean setDTR) {
return 0;
}



/* bytes in buffers */



// natGetInputBytesCount(handle)
JNIEXPORT jint JNICALL
Java_com_poixson_serial_natives_NativeD2xxOpen_natGetInputBytesCount
(JNIEnv *env, jobject obj, jlong handle) {
return 0;
}



// natGetOutputBytesCount(handle)
JNIEXPORT jint JNICALL
Java_com_poixson_serial_natives_NativeD2xxOpen_natGetOutputBytesCount
(JNIEnv *env, jobject obj, jlong handle) {
return 0;
}



/* read/write */



// natReadBytes(handle, bytes, length)
JNIEXPORT jint JNICALL
Java_com_poixson_serial_natives_NativeD2xxOpen_natReadBytes
(JNIEnv *env, jobject obj,
jlong handle, jbyteArray bytes, jint len) {
return 0;
}



// natWriteBytes(handle, bytes)
JNIEXPORT jlong JNICALL
Java_com_poixson_serial_natives_NativeD2xxOpen_natWriteBytes
(JNIEnv *env, jobject obj, jlong handle, jbyteArray bytes) {
return 0;
}
