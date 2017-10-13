/* PoiXson pxnSerial 1.x
 * copyright 2017
 * license GPL-3
 * lorenzo at poixson.com
 * http://poixson.com/
 */



#include "NativeD2xxOpen.h"

#include <jni.h>

// open source library
#include "ftdi/open/ftdi.h"



/* list devices */



// natGetDeviceList()
JNIEXPORT jobjectArray JNICALL
Java_com_poixson_serial_natives_NativeD2xxOpen_natGetDeviceList
(JNIEnv *env, jobject obj) {
return NULL;
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
