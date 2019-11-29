package moka.land.base

import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.EditText

inline fun View.visible() {
    this.visibility = View.VISIBLE
}

inline fun View.gone() {
    this.visibility = View.GONE
}

inline fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.visibleFadeIn(duration: Long = 0L, onFinish: (() -> Unit)? = null) {
    this.visible()
    val fadeInAnimation = AlphaAnimation(0f, 1f)
    fadeInAnimation.interpolator = AccelerateInterpolator()
    fadeInAnimation.duration = duration

    fadeInAnimation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(p0: Animation?) {
        }

        override fun onAnimationEnd(p0: Animation?) {
            onFinish?.invoke()
        }

        override fun onAnimationStart(p0: Animation?) {
        }
    })
    postDelayed({ this.clearAnimation() }, fadeInAnimation.duration)
    this.startAnimation(fadeInAnimation)
}

fun View.invisibleFadeOut(duration: Long = 0L, onFinish: (() -> Unit)? = null) {
    this.invisible()

    val fadeOutAnimation = AlphaAnimation(1f, 0f)
    fadeOutAnimation.interpolator = AccelerateInterpolator()
    fadeOutAnimation.duration = duration

    fadeOutAnimation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(p0: Animation?) {
        }

        override fun onAnimationEnd(p0: Animation?) {
            onFinish?.invoke()
        }

        override fun onAnimationStart(p0: Animation?) {
        }
    })
    postDelayed({ this.clearAnimation() }, fadeOutAnimation.duration)
    this.startAnimation(fadeOutAnimation)
}

fun View.goneFadeOut(duration: Long = 0L, onFinish: (() -> Unit)? = null) {
    this.gone()

    val fadeOutAnimation = AlphaAnimation(1f, 0f)
    fadeOutAnimation.interpolator = AccelerateInterpolator()
    fadeOutAnimation.duration = duration

    fadeOutAnimation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(p0: Animation?) {
        }

        override fun onAnimationEnd(p0: Animation?) {
            onFinish?.invoke()
        }

        override fun onAnimationStart(p0: Animation?) {
        }
    })
    postDelayed({ this.clearAnimation() }, fadeOutAnimation.duration)
    this.startAnimation(fadeOutAnimation)
}

fun toGone(vararg views: View) {
    views.forEach { it.gone() }
}

fun toInvisible(vararg views: View) {
    views.forEach { it.invisible() }
}

fun toInVisibleFadeOut(vararg views: View) {
    views.forEach {
        if (it.visibility == View.VISIBLE)
            it.invisibleFadeOut()
    }
}

fun toVisible(vararg views: View) {
    views.forEach { it.visible() }
}

fun toVisibleFadeIn(vararg views: View) {
    views.forEach { it.visibleFadeIn() }
}

fun View.visibleOrGone(isVisible: Boolean) {
    if (isVisible)
        this.visible()
    else
        this.gone()
}

fun View.leftMargin(dp: Int) {
    (this.layoutParams as? ViewGroup.MarginLayoutParams)?.leftMargin = dp
}

fun View.rightMargin(dp: Int) {
    (this.layoutParams as? ViewGroup.MarginLayoutParams)?.rightMargin = dp
}

fun View.topMargin(dp: Int) {
    (this.layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin = dp
}

fun View.bottomMargin(dp: Int) {
    (this.layoutParams as? ViewGroup.MarginLayoutParams)?.bottomMargin = dp
}

fun EditText.toCursorLast() {
    this.setSelection(this.text.length)
}
