package land.moka.base

import android.content.res.Resources
import moka.land.base.R

fun dip(value: Int): Int = (value * Resources.getSystem().displayMetrics.density).toInt()
fun dip(value: Float): Int = (value * Resources.getSystem().displayMetrics.density).toInt()

fun px(value: Int): Float = value.toFloat() / Resources.getSystem().displayMetrics.density
fun px(value: Float): Float = value / Resources.getSystem().displayMetrics.density

val toolBarSize by lazy { Resources.getSystem().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material) }

val statusBarSize by lazy {
    val resourceId = Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        Resources.getSystem().getDimensionPixelSize(resourceId)
    }
    else {
        19
    }
}

val deviceWidthPixel by lazy { Resources.getSystem().displayMetrics.widthPixels }
val deviceHeightPixel by lazy { Resources.getSystem().displayMetrics.heightPixels }
