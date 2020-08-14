package moka.land.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import moka.land.base.visibleOrGone
import moka.moka.dialog.databinding.LayoutBaseDialogBinding

abstract class _BaseDialog : AppCompatDialogFragment() {

    val __view by lazy { LayoutBaseDialogBinding.inflate(layoutInflater) }

    var onClickPositive: (() -> Unit)? = null
    var onClickNegative: (() -> Unit)? = null
    var onClickNeutral: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(DialogFragment.STYLE_NO_TITLE, 0)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return __view.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _init()
        _bindView()

        init()
    }

    override fun onResume() {
        super.onResume()
        if (null != getWidthRatio()) {
            if (getWidthRatio() == 1f) {
                dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            }
            else {
                dialog?.window?.setLayout((context!!.resources.displayMetrics.widthPixels * getWidthRatio()!!).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
            }
        }
        else {
            __view.root.minWidth = (300 * resources.displayMetrics.density).toInt() // 200dp
        }
    }

    private fun _init() {
        __view.content.addView(getContentView())
        __view.constraintLayoutButton.visibleOrGone(!isNoButton())
        __view.buttonPositive.visibility = if (getPositiveText().isNotEmpty()) View.VISIBLE else View.GONE
        __view.buttonNegative.visibility = if (getNegativeText().isNotEmpty()) View.VISIBLE else View.GONE
        __view.buttonNeutral.visibility = if (getNeutralText().isNotEmpty()) View.VISIBLE else View.GONE
        __view.buttonPositive.text = getPositiveText()
        __view.buttonNegative.text = getNegativeText()
        __view.buttonNeutral.text = getNeutralText()
        __view.buttonPositive.setTextColor(getPositiveTextColor() ?: Color.parseColor("#FF515151"))
        __view.buttonNegative.setTextColor(getNegativeTextColor() ?: Color.parseColor("#FF515151"))
        __view.buttonNeutral.setTextColor(getNeutralTextColor() ?: Color.parseColor("#FF515151"))

        if (getCancelable() == false) {
            this.isCancelable = false
            this.dialog?.setCanceledOnTouchOutside(false)
        }
    }

    private fun _bindView() {
        __view.buttonPositive.setOnClickListener { onClickButton(); onClickPositive?.invoke() }
        __view.buttonNegative.setOnClickListener { onClickButton(); onClickNegative?.invoke() }
        __view.buttonNeutral.setOnClickListener { onClickButton(); onClickNeutral?.invoke() }
    }

    private fun onClickButton() {
        if (hideOnClick()) {
            dismiss()
        }
    }

    fun setPositiveEnabled(isEnabled: Boolean) {
        __view.buttonPositive.isEnabled = isEnabled
        if (!isEnabled) {
            __view.buttonPositive.alpha = 0.3f
        }
        else {
            val animation = AlphaAnimation(0.3f, 1f)
            animation.duration = 500
            __view.buttonPositive.startAnimation(animation)
            __view.buttonPositive.alpha = 1f
        }
    }

    fun setNegativeEnabled(isEnabled: Boolean) {
        __view.buttonNegative.isEnabled = isEnabled
        if (!isEnabled) {
            __view.buttonNegative.alpha = 0.3f
        }
        else {
            val animation = AlphaAnimation(0.3f, 1f)
            animation.duration = 500
            __view.buttonNegative.startAnimation(animation)
            __view.buttonNegative.alpha = 1f
        }
    }

    fun setNeutralEnabled(isEnabled: Boolean) {
        __view.buttonNeutral.isEnabled = isEnabled
        if (!isEnabled) {
            __view.buttonNeutral.alpha = 0.3f
        }
        else {
            val animation = AlphaAnimation(0.3f, 1f)
            animation.duration = 500
            __view.buttonNeutral.startAnimation(animation)
            __view.buttonNeutral.alpha = 1f
        }
    }

    fun show(fragmentManager: FragmentManager, onClickPositive: (() -> Unit)? = null) {
        if (this.isAdded) {
            return
        }
        if (null != onClickPositive) {
            this.onClickPositive = onClickPositive
        }
        fragmentManager
            .beginTransaction()
            .add(this, this::class.java.simpleName)
            .commitAllowingStateLoss()
    }

    open fun hideOnClick(): Boolean = true

    open fun getPositiveText(): CharSequence = ""

    open fun getNegativeText(): CharSequence = ""

    open fun getNeutralText(): CharSequence = ""

    open fun getPositiveTextColor(): Int? = null

    open fun getNegativeTextColor(): Int? = null

    open fun getNeutralTextColor(): Int? = null

    open fun getWidthRatio(): Float? = null

    open fun getCancelable(): Boolean? = null

    open fun isNoButton(): Boolean = false

    // -

    abstract fun init()

    abstract fun getContentView(): View

}
