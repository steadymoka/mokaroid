package moka.land.permissionmanager

import java.io.Serializable

open class BaseConfig<out T : Runnable> : Serializable {

    internal var permissions: Array<String>? = null

    fun setPermission(vararg permission: String): Runnable {
        this.permissions = permission as Array<String>
        return this as T
    }

}
