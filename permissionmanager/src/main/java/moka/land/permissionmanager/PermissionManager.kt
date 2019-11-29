package moka.land.permissionmanager

import android.content.Context
import java.lang.ref.WeakReference

open class PermissionManager : BaseConfig<PermissionManager>() {

    @Transient
    private lateinit var context: WeakReference<Context>

    /**
     * Start permission manager
     * Get permission manager instance
     */

    fun check(onPermission: ((isGranted: Boolean, deniedPermissions: List<String>?) -> Unit)? = null) {
        if (this.permissions.isNullOrEmpty()) {
            throw Exception("permission should not be null")
        }
        PermissionManager.onPermission = onPermission

        context.get()?.run {
            val intent = PermissionLayout.getIntent(this, this@PermissionManager)
            startActivity(intent)
        }
    }

    companion object {

        @Transient
        var onPermission: ((isGranted: Boolean, deniedPermissions: List<String>?) -> Unit)? = null

        fun with(context: Context) = PermissionManager().apply {
            this.context = WeakReference(context)
        }
    }

}
