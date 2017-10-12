/* PoiXson pxnSerial 1.x
 * copyright 2017
 * license GPL-3
 * lorenzo at poixson.com
 * http://poixson.com/
 */


#include "NativeSerial.h"

#include <jni.h>


JNIEXPORT jint JNICALL
Java_com_poixson_serial_natives_NativeSerial_test
(JNIEnv *env, jobject obj) {
	printf("\nSerial Test Works!\n\n");
	return 1;
}
