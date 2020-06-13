package moka.land.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.layout_base_dialog.*
import moka.land.base.log
import moka.land.base.visibleOrGone
import moka.moka.dialog.R

abstract class _BaseDialog : AppCompatDialogFragment() {

    var onClickPositive: (() -> Unit)? = null
    var onClickNegative: (() -> Unit)? = null
    var onClickNeutral: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(DialogFragment.STYLE_NO_TITLE, 0)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_base_dialog, container, false)
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
            root.minWidth = (300 * resources.displayMetrics.density).toInt() // 200dp
        }
    }

    private fun _init() {
        content.addView(getContentView())
        constraintLayoutButton.visibleOrGone(!isNoButton())
        buttonPositive.visibility = if (getPositiveText().isNotEmpty()) View.VISIBLE else View.GONE
        buttonNegative.visibility = if (getNegativeText().isNotEmpty()) View.VISIBLE else View.GONE
        buttonNeutral.visibility = if (getNeutralText().isNotEmpty()) View.VISIBLE else View.GONE
        buttonPositive.text = getPositiveText()
        buttonNegative.text = getNegativeText()
        buttonNeutral.text = getNeutralText()

        if (getCancelable() == false) {
            this.isCancelable = false
            this.dialog?.setCanceledOnTouchOutside(false)
        }
    }

    private fun _bindView() {
        buttonPositive.setOnClickListener { onClickButton(); onClickPositive?.invoke() }
        buttonNegative.setOnClickListener { onClickButton(); onClickNegative?.invoke() }
        buttonNeutral.setOnClickListener { onClickButton(); onClickNeutral?.invoke() }
    }

    private fun onClickButton() {
        if (hideOnClick()) {
            dismiss()
        }
    }

    fun setPositiveEnabled(isEnabled: Boolean) {
        buttonPositive.isEnabled = isEnabled
        if (!isEnabled) {
            buttonPositive.alpha = 0.3f
        }
        else {
            val animation = AlphaAnimation(0.3f, 1f)
            animation.duration = 500
            buttonPositive.startAnimation(animation)
            buttonPositive.alpha = 1f
        }
    }

    fun setNegativeEnabled(isEnabled: Boolean) {
        buttonNegative.isEnabled = isEnabled
        if (!isEnabled) {
            buttonNegative.alpha = 0.3f
        }
        else {
            val animation = AlphaAnimation(0.3f, 1f)
            animation.duration = 500
            buttonNegative.startAnimation(animation)
            buttonNegative.alpha = 1f
        }
    }

    fun setNeutralEnabled(isEnabled: Boolean) {
        buttonNeutral.isEnabled = isEnabled
        if (!isEnabled) {
            buttonNeutral.alpha = 0.3f
        }
        else {
            val animation = AlphaAnimation(0.3f, 1f)
            animation.duration = 500
            buttonNeutral.startAnimation(animation)
            buttonNeutral.alpha = 1f
        }
    }

    fun show(fragmentManager: FragmentManager, onClickPositive: (() -> Unit)? = null) {
        if (this.isAdded) {
            return
        }

        this.onClickPositive = onClickPositive
        fragmentManager
            .beginTransaction()
            .add(this, this::class.java.simpleName)
            .commitAllowingStateLoss()
    }

    open fun hideOnClick(): Boolean {
        return true
    }

    open fun getPositiveText(): CharSequence = ""

    open fun getNegativeText(): CharSequence = ""

    open fun getNeutralText(): CharSequence = ""

    open fun getWidthRatio(): Float? = null

    open fun getCancelable(): Boolean? = null

    open fun isNoButton(): Boolean = false

    // -

    abstract fun init()

    abstract fun getContentView(): View

}
