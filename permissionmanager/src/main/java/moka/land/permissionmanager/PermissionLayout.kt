package moka.land.permissionmanager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import moka.land.permissionmanager.util.PermissionUtil

class PermissionLayout : AppCompatActivity() {

    private lateinit var config: BaseConfig<PermissionManager>

    /**
     * LifeCycle functions
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)

        initConfig(savedInstanceState)
        checkPermissions(config)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        outState.putSerializable(KEY_CONFIG_EXTRA, config)
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val deniedPermissions = permissions.filter { !PermissionUtil.isGranted(this, it) }

        if (deniedPermissions.isEmpty()) {
            onResult(null)
        }
        else {
            val onNever = deniedPermissions.any { !ActivityCompat.shouldShowRequestPermissionRationale(this, it) }
            if (onNever) {
                onNeverAskAgain(deniedPermissions)
            }
            else {
                onDenyDialog(deniedPermissions)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            CODE_FROM_SETTING -> {
                checkPermissions(config)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * Business logic functions
     */

    private fun initConfig(savedInstanceState: Bundle?) {
        val config = if (null != savedInstanceState) {
            savedInstanceState.getSerializable(KEY_CONFIG_EXTRA) as? BaseConfig<PermissionManager>
        }
        else {
            intent.getSerializableExtra(KEY_CONFIG_EXTRA) as? BaseConfig<PermissionManager>
        }
        this.config = config ?: BaseConfig()
    }

    private fun checkPermissions(config: BaseConfig<PermissionManager>) {
        val requirePermissions = config.permissions!!.filter { !PermissionUtil.isGranted(this, it) }

        when {
            requirePermissions.isEmpty() -> {
                onResult(null)
            }

            else -> {
                requestPermissions(requirePermissions.toTypedArray())
            }
        }
    }

    private fun requestPermissions(permissions: Array<String>) {
        ActivityCompat.requestPermissions(this, permissions, CODE_REQUEST_PERMISSION)
    }

    private fun onResult(deniedPermissions: List<String>? = null) {
        if (deniedPermissions.isNullOrEmpty()) {
            PermissionManager.onPermission?.invoke(true, null)
        }
        else {
            PermissionManager.onPermission?.invoke(false, deniedPermissions)
        }

        finish()
        overridePendingTransition(0, 0)
    }

    /**
     * On results functions
     */

    private fun onDenyDialog(deniedPermissions: List<String>) {
        onResult(deniedPermissions)
    }

    private fun onNeverAskAgain(deniedPermissions: List<String>) {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다")
            .setMessage("[설정] > [권한] 에서 권한을 승인해주세요")
            .setCancelable(false)
            .setPositiveButton("설정가기") { _, _ ->
                this.startActivityForResult(PermissionUtil.getSettingIntent(this), CODE_FROM_SETTING)
            }
            .setNegativeButton("취소") { _, _ ->
                onResult(deniedPermissions)
            }
            .show()
    }

    companion object {

        private const val CODE_REQUEST_PERMISSION = 1004
        private const val CODE_FROM_SETTING = 1004 + 10

        private const val KEY_CONFIG_EXTRA = "PermissionLayout.KEY_CONFIG_EXTRA"

        fun getIntent(context: Context, config: PermissionManager): Intent {
            return Intent(context, PermissionLayout::class.java).apply {
                putExtra(KEY_CONFIG_EXTRA, config)
            }
        }

    }

}
