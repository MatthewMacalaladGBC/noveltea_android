package ca.gbc.comp3074.noveltea_app.data.local

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit

object NameStore {
    private const val PREFS = "noveltea_prefs"
    private const val KEY_NAME = "display_name"

    fun getName(ctx: Context): String? =
        ctx.getSharedPreferences(PREFS, MODE_PRIVATE).getString(KEY_NAME, null)

    fun setName(ctx: Context, name: String) {
        ctx.getSharedPreferences(PREFS, MODE_PRIVATE).edit { putString(KEY_NAME, name) }
    }

    fun clear(ctx: Context) {
        ctx.getSharedPreferences(PREFS, MODE_PRIVATE).edit { remove(KEY_NAME) }
    }
}