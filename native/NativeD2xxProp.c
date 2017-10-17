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



/* list devices */



// natGetDeviceList()
JNIEXPORT jobjectArray JNICALL
Java_com_poixson_serial_natives_NativeD2xxProp_natGetDeviceList
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



// natOpenPort(port-name)
JNIEXPORT jlong JNICALL
Java_com_poixson_serial_natives_NativeD2xxProp_natOpenPort
(JNIEnv *env, jobject obj, jstring portName) {
return 0;
}



// natClosePort(handle)
JNIEXPORT jboolean JNICALL
Java_com_poixson_serial_natives_NativeD2xxProp_natClosePort
(JNIEnv *env, jobject obj, jlong handle) {
return JNI_FALSE;
}



/* port parameters */



// natSetParams(handle, baud, byte-size, stop-bits, parity, flags)
JNIEXPORT jlong JNICALL
Java_com_poixson_serial_natives_NativeD2xxProp_natSetParams
(JNIEnv *env, jobject obj, jlong handle, jint baud,
jint byteSize, jint stopBits, jint parity, jint flags) {
return 0;
}



/*
// natSetBlocking(handle, blocking) - blocking/non-blocking
JNIEXPORT jlong JNICALL
Java_com_poixson_serial_natives_NativeD2xxProp_natSetBlocking
(JNIEnv *env, jobject obj, jlong handle, jboolean blocking) {
return 0;
}



// natSetVMinVTime(handle, vMin, vTime)
JNIEXPORT jlong JNICALL
Java_com_poixson_serial_natives_NativeD2xxProp_natSetVMinVTime
(JNIEnv *env, jobject obj, jlong handle, jint vMin, jint vTime) {
return 0;
}
*/



/* line status */



/*
// natGetLineStatus(handle)
JNIEXPORT jbooleanArray JNICALL
Java_com_poixson_serial_natives_NativeD2xxProp_natGetLineStatus
(JNIEnv *env, jobject obj, jlong handle) {
return NULL;
}



// natSetLineStatus(handle, rts, dtr)
JNIEXPORT jlong JNICALL
Java_com_poixson_serial_natives_NativeD2xxProp_natSetLineStatus
(JNIEnv *env, jobject obj,
jlong handle, jboolean setRTS, jboolean setDTR) {
return 0;
}
*/



/* bytes in buffers */



/*
// natGetInputBytesCount(handle)
JNIEXPORT jint JNICALL
Java_com_poixson_serial_natives_NativeD2xxProp_natGetInputBytesCount
(JNIEnv *env, jobject obj, jlong handle) {
return 0;
}



// natGetOutputBytesCount(handle)
JNIEXPORT jint JNICALL
Java_com_poixson_serial_natives_NativeD2xxProp_natGetOutputBytesCount
(JNIEnv *env, jobject obj, jlong handle) {
return 0;
}
*/



/* read/write */



// natReadBytes(handle, bytes, length)
JNIEXPORT jint JNICALL
Java_com_poixson_serial_natives_NativeD2xxProp_natReadBytes
(JNIEnv *env, jobject obj,
jlong handle, jbyteArray bytes, jint len) {
return 0;
}



// natWriteBytes(handle, bytes)
JNIEXPORT jlong JNICALL
Java_com_poixson_serial_natives_NativeD2xxProp_natWriteBytes
(JNIEnv *env, jobject obj, jlong handle, jbyteArray bytes) {
return 0;
}
