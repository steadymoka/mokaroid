package moka.land.base

import android.util.Log

fun logE(a: String, message: String) {
    if (DEBUG)
        Log.e(a, message)
}

fun logI(a: String, message: String) {
    if (DEBUG)
        Log.i(a, message)
}

fun logWtf(a: String, message: String) {
    if (DEBUG)
        Log.wtf(a, message)
}

fun logD(a: String, message: String) {
    if (DEBUG)
        Log.d(a, message)
}

fun logA(a: String, message: String) {
    if (DEBUG)
        Log.println(Log.ASSERT, a, message)
}

fun log(message: String) {
    if (DEBUG)
        Log.wtf("debugging..", message)
}

fun printCurrentThread() {
    if (DEBUG)
        Log.wtf("debugging.. ", "Current Thread :: ${Thread.currentThread().name}")
}
