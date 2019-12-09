package moka.land.component

import android.annotation.SuppressLint
import moka.land.BuildConfig
import moka.land._Application
import moka.land.component.base.SharedPreferenceManager


@SuppressLint("StaticFieldLeak")
object AuthManager : SharedPreferenceManager(_Application.context, "moka.land.auth_manager") {

    private var TAG = javaClass.simpleName

    fun isLoggedIn(): Boolean {
        return jwtToken.isNotEmpty() && currentUid.isNotEmpty()
    }

    fun signOut() {
        jwtToken = ""
        jwtRefreshToken = ""
        currentUid = ""
    }

    /* */

    const val apiKey: String = BuildConfig.apikey

    /* */

    var jwtToken: String
        get() = getExtraString("${TAG}.KEY_JWT_TOKEN", "")
        set(jwtToken) = setExtraString("${TAG}.KEY_JWT_TOKEN", jwtToken).run {}

    /* */

    var jwtRefreshToken: String
        get() = getExtraString("${TAG}.KEY_JWT_REFRESH_TOKEN", "")
        set(jwtRefreshToken) = setExtraString("${TAG}.KEY_JWT_REFRESH_TOKEN", jwtRefreshToken).run {}

    /* */

    var currentUid: String
        get() = getExtraString("${TAG}.CURRENT_UID", "")
        set(currentUid) = setExtraString("${TAG}.CURRENT_UID", currentUid).run {}

}
