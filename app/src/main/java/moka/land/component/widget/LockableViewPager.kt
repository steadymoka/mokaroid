package moka.land.component.widget


import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent

import androidx.viewpager.widget.ViewPager


class LockableViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ViewPager(context, attrs) {

    var isLocked = true

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (!isLocked) super.onTouchEvent(event) else false

    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (!isLocked) super.onInterceptTouchEvent(event) else false

    }

}
