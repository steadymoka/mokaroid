package moka.land.permissionmanager.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat

object PermissionUtil {

    fun isGranted(context: Context, vararg permissions: String): Boolean {
        return permissions
            .filter { !isGranted(context, it) }
            .none()
    }

    fun isGranted(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) === PackageManager.PERMISSION_GRANTED
    }

    fun getSettingIntent(context: Context): Intent {
        return Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            .setData(Uri.parse("package:" + context.packageName))
    }

}
