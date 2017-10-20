/* PoiXson pxnSerial 1.x
 * copyright 2017
 * license GPL-3
 * lorenzo at poixson.com
 * http://poixson.com/
 */



#ifndef PXNSERIAL_NATIVE_D2XX_OPEN_C
	#define PXNSERIAL_NATIVE_D2XX_OPEN_C 1
	#include "NativeD2xxOpen.c"
#endif

#include <jni.h>



/* load/unload */

JNIEXPORT jint JNICALL
Java_com_poixson_serial_natives_NativeD2xxOpen_init
(JNIEnv *env, jobject obj);

JNIEXPORT jint JNICALL
Java_com_poixson_serial_natives_NativeD2xxOpen_unload
(JNIEnv *env, jobject obj);



/* list devices */

JNIEXPORT void JNICALL
Java_com_poixson_serial_natives_NativeD2xxOpen_rescanDevices
(JNIEnv *env, jobject obj);

JNIEXPORT jobjectArray JNICALL
Java_com_poixson_serial_natives_NativeD2xxOpen_getDeviceList
(JNIEnv *env, jobject obj);



/* open/close port */

JNIEXPORT jlong JNICALL
Java_com_poixson_serial_natives_NativeD2xxOpen_openPort
(JNIEnv *env, jobject obj, jstring portName);

JNIEXPORT jboolean JNICALL
Java_com_poixson_serial_natives_NativeD2xxOpen_closePort
(JNIEnv *env, jobject obj, jlong handle);



/* port parameters */

JNIEXPORT jlong JNICALL
Java_com_poixson_serial_natives_NativeD2xxOpen_setParams
(JNIEnv *env, jobject obj, jlong handle, jint baud,
jint byteSize, jint stopBits, jint parity, jint flags);

/*
JNIEXPORT jlong JNICALL
Java_com_poixson_serial_natives_NativeD2xxOpen_setBlocking
(JNIEnv *env, jobject obj, jlong handle, jboolean blocking);

JNIEXPORT jlong JNICALL
Java_com_poixson_serial_natives_NativeD2xxOpen_setVMinVTime
(JNIEnv *env, jobject obj, jlong handle, jint vMin, jint vTime);
*/



/* line status */

JNIEXPORT jbooleanArray JNICALL
Java_com_poixson_serial_natives_NativeD2xxOpen_getLineStatus
(JNIEnv *env, jobject obj, jlong handle);

JNIEXPORT jlong JNICALL
Java_com_poixson_serial_natives_NativeD2xxOpen_setLineStatus
(JNIEnv *env, jobject obj,
jlong handle, jboolean setRTS, jboolean setDTR);



/* bytes in buffers */

/*
JNIEXPORT jint JNICALL
Java_com_poixson_serial_natives_NativeD2xxOpen_getInputBytesCount
(JNIEnv *env, jobject obj, jlong handle);

JNIEXPORT jint JNICALL
Java_com_poixson_serial_natives_NativeD2xxOpen_getOutputBytesCount
(JNIEnv *env, jobject obj, jlong handle);
*/



/* read/write */

JNIEXPORT jint JNICALL
Java_com_poixson_serial_natives_NativeD2xxOpen_readBytes
(JNIEnv *env, jobject obj,
jlong handle, jbyteArray bytes, jint len);

JNIEXPORT jlong JNICALL
Java_com_poixson_serial_natives_NativeD2xxOpen_writeBytes
(JNIEnv *env, jobject obj, jlong handle, jbyteArray bytes);
