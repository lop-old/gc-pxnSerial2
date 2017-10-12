/* PoiXson pxnSerial 1.x
 * copyright 2017
 * license GPL-3
 * lorenzo at poixson.com
 * http://poixson.com/
 */


#include "NativeD2xx.h"

#include <jni.h>
#include "ftd2xx.h"


JNIEXPORT jint JNICALL
Java_com_poixson_serial_natives_NativeD2xx_test
(JNIEnv *env, jobject obj) {
	printf("\nD2xx Test Works!\n\n");
	return 1;
}
