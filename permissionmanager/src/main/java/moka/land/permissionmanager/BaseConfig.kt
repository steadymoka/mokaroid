package moka.land.permissionmanager

import java.io.Serializable

@kotlinx.serialization.Serializable
open class BaseConfig<out T : BaseConfig<T>> : Serializable {

    internal var permissions: Array<String>? = null

    fun setPermission(vararg permission: String): T {
        this.permissions = permission as Array<String>
        return this as T
    }

}
