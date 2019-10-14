package land.moka.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import land.moka.dialog.databinding.LayoutBaseDialogBinding

abstract class BaseDialog : AppCompatDialogFragment() {

    private lateinit var rootBinding: LayoutBaseDialogBinding

    var onClickPositive: (() -> Unit)? = null
    var onClickNegative: (() -> Unit)? = null
    var onClickNeutral: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(DialogFragment.STYLE_NO_TITLE, 0)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<LayoutBaseDialogBinding>(inflater, R.layout.layout_base_dialog, container, false)
            .apply {
                rootBinding = this
                lifecycleOwner = this@BaseDialog

                _init()
                _bindView()

                init()
            }.root
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
            rootBinding.root.minWidth = (300 * resources.displayMetrics.density).toInt() // 200dp
        }
    }

    private fun _init() {
        rootBinding.content.addView(getContentView())
        rootBinding.buttonPositive.visibility = if (isPositive()) View.VISIBLE else View.GONE
        rootBinding.buttonNegative.visibility = if (isNegativeText()) View.VISIBLE else View.GONE
        rootBinding.buttonNeutral.visibility = if (isNeutralText()) View.VISIBLE else View.GONE
        rootBinding.buttonPositive.text = getPositiveText()
        rootBinding.buttonNegative.text = getNegativeText()
        rootBinding.buttonNeutral.text = getNeutralText()
    }

    private fun _bindView() {
        rootBinding.buttonPositive.setOnClickListener { onClickPositive?.invoke(); dismiss() }
        rootBinding.buttonNegative.setOnClickListener { onClickNegative?.invoke(); dismiss() }
        rootBinding.buttonNeutral.setOnClickListener { onClickNeutral?.invoke(); dismiss() }
    }

    open fun getPositiveText(): CharSequence = "확인"

    open fun getNegativeText(): CharSequence = "취소"

    open fun getNeutralText(): CharSequence = "삭제"

    open fun isPositive(): Boolean = true

    open fun isNegativeText(): Boolean = true

    open fun isNeutralText(): Boolean = true

    open fun getWidthRatio(): Float? = null

    // -

    abstract fun init()

    abstract fun getContentView(): View

}
