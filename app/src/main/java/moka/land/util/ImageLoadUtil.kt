package moka.land.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import java.io.File

fun ImageView.load(
    context: Context,
    uri: Uri?,
    duration: Int = 0,
    width: Int = 0,
    height: Int = 0,
    radius: Int = 0,
    failCallback: (() -> Unit)? = null,
    onReady: (() -> Unit)? = null
) {
    this._load(context, uri, null, null, null, duration, width, height, radius, true, failCallback, onReady)
}

fun ImageView.load(
    context: Context,
    url: String?,
    duration: Int = 0,
    width: Int = 0,
    height: Int = 0,
    radius: Int = 0,
    failCallback: (() -> Unit)? = null,
    onReady: (() -> Unit)? = null
) {
    this._load(context, null, url, null, null, duration, width, height, radius, true, failCallback, onReady)
}

fun ImageView.load(
    context: Context,
    resId: Int?,
    duration: Int = 0,
    width: Int = 0,
    height: Int = 0,
    radius: Int = 0,
    centerCrop: Boolean = true,
    failCallback: (() -> Unit)? = null,
    onReady: (() -> Unit)? = null
) {
    this._load(context, null, null, resId, null, duration, width, height, radius, centerCrop, failCallback, onReady)
}

fun ImageView.load(
    context: Context,
    file: File?,
    duration: Int = 0,
    width: Int = 0,
    height: Int = 0,
    radius: Int = 0,
    failCallback: (() -> Unit)? = null,
    onReady: (() -> Unit)? = null
) {
    this._load(context, null, null, null, file, duration, width, height, radius, true, failCallback, onReady)
}

private fun ImageView._load(
    context: Context,
    uri: Uri?,
    url: String?,
    resId: Int?,
    file: File?,
    duration: Int = 0,
    width: Int = -1,
    height: Int = -1,
    radius: Int = 0,
    centerCrop: Boolean = true,
    failCallback: (() -> Unit)? = null,
    onReady: (() -> Unit)? = null
) {
    Glide.with(context)
        .load(uri ?: file ?: url ?: resId)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                failCallback?.invoke()
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                onReady?.invoke()
                return false
            }
        })
        .apply {

            thumbnail(0.5f)

            if (duration != 0)
                transition(DrawableTransitionOptions.withCrossFade(duration))

            if (width != -1 && height != -1)
                override(width, height)

            if (radius != 0)
                transform(RoundedCorners(radius))

            if (centerCrop)
                centerCrop()
            else
                centerInside()
        }
        .into(this)
}

/**
 *
 * To circle
 */
fun ImageView.circle(context: Context, url: String?) {
    this._circle(context, url, null, null)
}

fun ImageView.circle(context: Context, resId: Int?) {
    this._circle(context, null, resId, null)
}

fun ImageView.circle(context: Context, file: File?) {
    this._circle(context, null, null, file)
}

private fun ImageView._circle(context: Context, url: String?, resId: Int?, file: File?) {
    Glide.with(context)
        .load(file ?: url ?: resId)
        .centerCrop()
        .apply(RequestOptions().circleCrop())
        .into(this)
}

/**
 *
 * To blur
 */
fun ImageView.blur(context: Context, resId: Int) {
    this._blur(context, null, resId)
}

fun ImageView.blur(context: Context, url: String?) {
    this._blur(context, url, null)
}

private fun ImageView._blur(context: Context, url: String?, resId: Int?) {
    Glide
        .with(context)
        .load(url ?: resId)
        .centerCrop()
        .into(this)
}

/**
 *
 * To radius
 */
fun ImageView.radius(context: Context, url: String?, radius: Int = 10) {
    this._radius(context, url, null, radius)
}

fun ImageView.radius(context: Context, resId: Int?, radius: Int = 10) {
    this._radius(context, null, resId, radius)
}

private fun ImageView._radius(context: Context, url: String?, resId: Int?, radius: Int = 10) {
    Glide
        .with(context)
        .load(url ?: resId)
        .transform(RoundedCorners(radius))
        .into(this)
}
