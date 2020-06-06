package moka.land.ui.dialogs


import android.view.View
import androidx.fragment.app.FragmentManager
import moka.land.R
import moka.land.adhelper.NativeAdView
import moka.land.adhelper.Period
import moka.land.base.gone
import moka.land.databinding.DialogAdBinding
import moka.land.dialog._BaseDialog


class AdDialogFragment : _BaseDialog() {

    private val _view by lazy { DialogAdBinding.inflate(layoutInflater) }

    private var onPositive: (() -> Unit)? = null

    override fun onResume() {
        super.onResume()
        if (!isAdded) {
            return
        }

        val mokaAdView = NativeAdView(activity!!)
        _view.mokaAdViewContainer.removeAllViews()
        _view.mokaAdViewContainer.addView(mokaAdView)

        mokaAdView
            .setOption {
                admobKey = "ca-app-pub-7847386025632674/8475821127"
                fbAudienceKey = "2003520433203987_2003525736536790"
                period = Period.ADMOB_FACEBOOK
                nativeLayoutResId = R.layout.view_custom_ad_native
            }
            .showNative { isSuccess ->
                if (isAdded) {
                    _view.loadingAd.gone()
                }
            }
    }

    override fun getContentView(): View = _view.root

    override fun getPositiveText(): CharSequence = "프리미엄"

    override fun getNegativeText(): CharSequence = "취소"

    override fun getWidthRatio(): Float? = 1.0f

    override fun getCancelable(): Boolean? = false

    override fun init() {
        onClickPositive = { onPositive?.invoke() }
    }

    fun showDialog(fragmentManager: FragmentManager, onAlertListener: () -> Unit) {
        this.onPositive = onAlertListener
        fragmentManager
            .beginTransaction()
            .add(this, "AdDialogFragment")
            .commitAllowingStateLoss()
    }

}
