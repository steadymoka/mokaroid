package moka.land.permissionmanager

import android.content.Context
import java.lang.ref.WeakReference

interface Runnable {

    fun setOption(config: BaseConfig<PermissionManager>.() -> Unit): Runnable

    fun check(onPermission: ((isGranted: Boolean, deniedPermissions: List<String>?) -> Unit)? = null)

}

class PermissionManager : BaseConfig<PermissionManager>(), Runnable {

    @Transient
    private lateinit var context: WeakReference<Context>

    override fun setOption(config: BaseConfig<PermissionManager>.() -> Unit): Runnable {
        this.config()
        return this
    }

    /**
     * Start permission manager
     * Get permission manager instance
     */

    override fun check(onPermission: ((isGranted: Boolean, deniedPermissions: List<String>?) -> Unit)?) {
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

        fun with(context: Context): BaseConfig<PermissionManager> {
            return PermissionManager().apply {
                this.context = WeakReference(context)
            }
        }
    }

}
