/* PoiXson pxnSerial 1.x
 * copyright 2017
 * license GPL-3
 * lorenzo at poixson.com
 * http://poixson.com/
 */



#include "NativeD2xxProp.h"

#include <jni.h>

#include <stdio.h>
#include <string.h>

// official library
#include "ftdi/prop/ftd2xx.h"



/* load/unload */



// init()
JNIEXPORT jint JNICALL
Java_com_poixson_serial_natives_NativeD2xxProp_init
(JNIEnv *env, jobject obj) {
	return 0;
}



// unload()
JNIEXPORT jint JNICALL
Java_com_poixson_serial_natives_NativeD2xxProp_unload
(JNIEnv *env, jobject obj) {
	return 0;
}



/* list devices */



// rescanDevices()
JNIEXPORT void JNICALL
Java_com_poixson_serial_natives_NativeD2xxProp_rescanDevices
(JNIEnv *env, jobject obj) {
}



// getDeviceList()
JNIEXPORT jobjectArray JNICALL
Java_com_poixson_serial_natives_NativeD2xxProp_getDeviceList
(JNIEnv *env, jobject obj) {
	FT_STATUS status;
	// get device serial numbers
	char *list [MAX_DEVICES + 1];
	char serialBuf [MAX_DEVICES] [SERIAL_SIZE];
	for (int i=0; i<MAX_DEVICES; i++) {
		list[i] = serialBuf[i];
	}
	list[MAX_DEVICES] = NULL;
	int count;
	status = FT_ListDevices(
		(PVOID) list,
		(PVOID) &count,
		FT_LIST_ALL | FT_OPEN_BY_SERIAL_NUMBER
	);
	if (!FT_SUCCESS(status)) {
		fprintf(stderr, "Failed to get devices list: %d\n", status);
		return NULL;
	}
	if (count > MAX_DEVICES) {
		fprintf(stderr, "Found %d devices, more than max %d devices", count, MAX_DEVICES);
		count = MAX_DEVICES;
	}
	// prepare byte array
	int total = count;
	for (int i=0; i<count; i++) {
		total += strlen(list[i]);
	}
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



// openPort(port-name)
JNIEXPORT jlong JNICALL
Java_com_poixson_serial_natives_NativeD2xxProp_openPort
(JNIEnv *env, jobject obj, jstring portName) {
return 0;
}



// closePort(handle)
JNIEXPORT jboolean JNICALL
Java_com_poixson_serial_natives_NativeD2xxProp_closePort
(JNIEnv *env, jobject obj, jlong handle) {
return JNI_FALSE;
}



/* port parameters */



// setParams(handle, baud, byte-size, stop-bits, parity, flags)
JNIEXPORT jlong JNICALL
Java_com_poixson_serial_natives_NativeD2xxProp_setParams
(JNIEnv *env, jobject obj, jlong handle, jint baud,
jint byteSize, jint stopBits, jint parity, jint flags) {
return 0;
}



/*
// setBlocking(handle, blocking) - blocking/non-blocking
JNIEXPORT jlong JNICALL
Java_com_poixson_serial_natives_NativeD2xxProp_setBlocking
(JNIEnv *env, jobject obj, jlong handle, jboolean blocking) {
return 0;
}



// setVMinVTime(handle, vMin, vTime)
JNIEXPORT jlong JNICALL
Java_com_poixson_serial_natives_NativeD2xxProp_setVMinVTime
(JNIEnv *env, jobject obj, jlong handle, jint vMin, jint vTime) {
return 0;
}
*/



/* line status */



/*
// getLineStatus(handle)
JNIEXPORT jbooleanArray JNICALL
Java_com_poixson_serial_natives_NativeD2xxProp_getLineStatus
(JNIEnv *env, jobject obj, jlong handle) {
return NULL;
}



// setLineStatus(handle, rts, dtr)
JNIEXPORT jlong JNICALL
Java_com_poixson_serial_natives_NativeD2xxProp_setLineStatus
(JNIEnv *env, jobject obj,
jlong handle, jboolean setRTS, jboolean setDTR) {
return 0;
}
*/



/* bytes in buffers */



/*
// getInputBytesCount(handle)
JNIEXPORT jint JNICALL
Java_com_poixson_serial_natives_NativeD2xxProp_getInputBytesCount
(JNIEnv *env, jobject obj, jlong handle) {
return 0;
}



// getOutputBytesCount(handle)
JNIEXPORT jint JNICALL
Java_com_poixson_serial_natives_NativeD2xxProp_getOutputBytesCount
(JNIEnv *env, jobject obj, jlong handle) {
return 0;
}
*/



/* read/write */



// readBytes(handle, bytes, length)
JNIEXPORT jint JNICALL
Java_com_poixson_serial_natives_NativeD2xxProp_readBytes
(JNIEnv *env, jobject obj,
jlong handle, jbyteArray bytes, jint len) {
return 0;
}



// writeBytes(handle, bytes)
JNIEXPORT jlong JNICALL
Java_com_poixson_serial_natives_NativeD2xxProp_writeBytes
(JNIEnv *env, jobject obj, jlong handle, jbyteArray bytes) {
return 0;
}
