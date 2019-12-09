package moka.land.component.base

import android.content.Context
import android.content.SharedPreferences

open class SharedPreferenceManager(
    private val context: Context,
    private val SHARED_PREFERENCE_NAME: String) {

    private fun getEditor(context: Context): SharedPreferences.Editor {
        return getSharedPreferences(context).edit()
    }

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    fun clearPreference(context: Context) {
        getEditor(context).clear().commit()
    }

    /* */

    fun setExtraLong(KEY: String, value: Long) = getEditor(context).putLong(KEY, value).commit()

    fun setExtraInt(KEY: String, value: Int) = getEditor(context).putInt(KEY, value).commit()

    fun setExtraFloat(KEY: String, value: Float) = getEditor(context).putFloat(KEY, value).commit()

    fun setExtraString(KEY: String, value: String) = getEditor(context).putString(KEY, value).commit()

    fun setExtraBoolean(KEY: String, value: Boolean) = getEditor(context).putBoolean(KEY, value).commit()

    /* */

    fun getExtraLong(KEY: String, defaultValue: Long): Long = getSharedPreferences(context).getLong(KEY, defaultValue)

    fun getExtraInt(KEY: String, defaultValue: Int): Int = getSharedPreferences(context).getInt(KEY, defaultValue)

    fun getExtraFloat(KEY: String, defaultValue: Float): Float = getSharedPreferences(context).getFloat(KEY, defaultValue)

    fun getExtraString(KEY: String, defaultValue: String): String = getSharedPreferences(context).getString(KEY, defaultValue).toString()

    fun getExtraBoolean(KEY: String, defaultValue: Boolean): Boolean = getSharedPreferences(context).getBoolean(KEY, defaultValue)

}
