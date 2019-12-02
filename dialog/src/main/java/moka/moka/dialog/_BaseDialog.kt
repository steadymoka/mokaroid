package moka.moka.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.layout_base_dialog.*

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
        buttonPositive.setOnClickListener { onClickPositive?.invoke(); dismiss() }
        buttonNegative.setOnClickListener { onClickNegative?.invoke(); dismiss() }
        buttonNeutral.setOnClickListener { onClickNeutral?.invoke(); dismiss() }
    }

    open fun getPositiveText(): CharSequence = ""

    open fun getNegativeText(): CharSequence = ""

    open fun getNeutralText(): CharSequence = ""

    open fun getWidthRatio(): Float? = null

    open fun getCancelable(): Boolean? = null

    // -

    abstract fun init()

    abstract fun getContentView(): View

}
