/* PoiXson pxnSerial 1.x
 * copyright 2017
 * license GPL-3
 * lorenzo at poixson.com
 * http://poixson.com/
 */


#ifndef PXNSERIAL_NATIVE_D2XX_C
	#define PXNSERIAL_NATIVE_D2XX_C 1
	#include "NativeD2xx.c"
#endif

#include <jni.h>


JNIEXPORT jint JNICALL
Java_com_poixson_serial_natives_NativeD2xx_test
(JNIEnv *env, jobject obj);
