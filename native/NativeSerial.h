/* PoiXson pxnSerial 1.x
 * copyright 2017
 * license GPL-3
 * lorenzo at poixson.com
 * http://poixson.com/
 */



#ifndef PXNSERIAL_NATIVE_SERIAL_C
	#define PXNSERIAL_NATIVE_SERIAL_C 1
	#include "NativeSerial.c"
#endif

#include <jni.h>

#include <termios.h>



#define NATIVESERIAL_ERROR_UNKNOWN_FAIL          -1
#define NATIVESERIAL_ERROR_PORT_NOT_FOUND        -2
#define NATIVESERIAL_ERROR_PORT_BUSY             -3
#define NATIVESERIAL_ERROR_PERMISSION_DENIED     -4
#define NATIVESERIAL_ERROR_INCORRECT_SERIAL_PORT -5



/* load/unload */

JNIEXPORT jint JNICALL
Java_com_poixson_serialplus_natives_NativeSerial_natInit
(JNIEnv *env, jobject obj);

JNIEXPORT jint JNICALL
Java_com_poixson_serialplus_natives_NativeSerial_natUnload
(JNIEnv *env, jobject obj);



/* list devices */

JNIEXPORT jobjectArray JNICALL
Java_com_poixson_serial_natives_NativeSerial_natGetDeviceList
(JNIEnv *env, jobject obj);



/* open/close port */

JNIEXPORT jlong JNICALL
Java_com_poixson_serial_natives_NativeSerial_natOpenPort
(JNIEnv *env, jobject obj, jstring portName);

JNIEXPORT jboolean JNICALL
Java_com_poixson_serial_natives_NativeSerial_natClosePort
(JNIEnv *env, jobject obj, jlong handle);



/* port parameters */

JNIEXPORT jlong JNICALL
Java_com_poixson_serial_natives_NativeSerial_natSetParams
(JNIEnv *env, jobject obj, jlong handle, jint baud,
jint byteSize, jint stopBits, jint parity, jint flags);

/*
JNIEXPORT jlong JNICALL
Java_com_poixson_serial_natives_NativeSerial_natSetBlocking
(JNIEnv *env, jobject obj, jlong handle, jboolean blocking);

JNIEXPORT jlong JNICALL
Java_com_poixson_serial_natives_NativeSerial_natSetVMinVTime
(JNIEnv *env, jobject obj, jlong handle, jint vMin, jint vTime);
*/



/* line status */

/*
JNIEXPORT jbooleanArray JNICALL
Java_com_poixson_serial_natives_NativeSerial_natGetLineStatus
(JNIEnv *env, jobject obj, jlong handle);

JNIEXPORT jlong JNICALL
Java_com_poixson_serial_natives_NativeSerial_natSetLineStatus
(JNIEnv *env, jobject obj,
jlong handle, jboolean setRTS, jboolean setDTR);
*/



/* bytes in buffers */

/*
JNIEXPORT jint JNICALL
Java_com_poixson_serial_natives_NativeSerial_natGetInputBytesCount
(JNIEnv *env, jobject obj, jlong handle);

JNIEXPORT jint JNICALL
Java_com_poixson_serial_natives_NativeSerial_natGetOutputBytesCount
(JNIEnv *env, jobject obj, jlong handle);
*/



/* read/write */

JNIEXPORT jint JNICALL
Java_com_poixson_serial_natives_NativeSerial_natReadBytes
(JNIEnv *env, jobject obj,
jlong handle, jbyteArray bytes, jint len);

JNIEXPORT jlong JNICALL
Java_com_poixson_serial_natives_NativeSerial_natWriteBytes
(JNIEnv *env, jobject obj, jlong handle, jbyteArray bytes);



speed_t GetBaudByNumber(jint baud);
int GetByteSizeByNumber(jint byteSize);
