/* PoiXson pxnSerial 1.x
 * copyright 2017
 * license GPL-3
 * lorenzo at poixson.com
 * http://poixson.com/
 */



#include "NativeD2xxProp.h"

#include <jni.h>

// official library
#include "ftdi/prop/ftd2xx.h"

JNIEXPORT jobjectArray JNICALL
Java_com_poixson_serial_natives_NativeD2xxProp_natGetDeviceList
(JNIEnv *env, jobject obj) {
	printf("\nD2xx Test Works!\n\n");
	return 1;
}
