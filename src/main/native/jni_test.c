#include "jni.h"

JNIEXPORT jint JNICALL Java_JNITests_nativeCJNITest(JNIEnv* env, jobject obj, jint value) {
    return value * 2;
}