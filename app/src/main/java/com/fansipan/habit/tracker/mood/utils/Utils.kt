package com.fansipan.habit.tracker.mood.utils

import android.os.Bundle
import java.io.Serializable

fun String.trackingEvent() {
    TrackingUtils.trackingEventScreen(this)
}

fun Boolean.invert(): Boolean = !this

inline fun <reified T : Serializable> Bundle.getDataSerializable(key: String, clazz: Class<T>): T? {
    return if (isSdk33()) {
        getSerializable(key, clazz)
    } else {
        @Suppress("DEPRECATION") getSerializable(key) as? T
    }
}