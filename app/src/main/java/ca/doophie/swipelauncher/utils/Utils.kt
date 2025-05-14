@file:Suppress("DEPRECATION")

package ca.doophie.swipelauncher.utils

import android.app.Activity
import android.util.DisplayMetrics
import android.util.Size

fun Activity.getScreenSize(): Size {
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    val height = displayMetrics.heightPixels;
    val width = displayMetrics.widthPixels;

    return Size(width, height)

}