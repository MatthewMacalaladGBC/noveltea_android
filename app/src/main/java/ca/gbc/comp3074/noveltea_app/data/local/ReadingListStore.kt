package ca.gbc.comp3074.noveltea_app.data.local

import android.content.Context
import android.content.Context.MODE_PRIVATE
import ca.gbc.comp3074.noveltea_app.model.Book
import androidx.core.content.edit

object ReadingListStore {
    private const val PREFS = "noveltea_prefs"
    private const val KEY_LIST = "reading_list_ids"

    private fun prefs(ctx: Context) = ctx.getSharedPreferences(PREFS, MODE_PRIVATE)

    fun add(ctx: Context, bookId: String) {
        val cur = prefs(ctx).getStringSet(KEY_LIST, emptySet())!!.toMutableSet()
        cur.add(bookId.toString())
        prefs(ctx).edit { putStringSet(KEY_LIST, cur) }
    }

    fun remove(ctx: Context, bookId: String) {
        val cur = prefs(ctx).getStringSet(KEY_LIST, emptySet())!!.toMutableSet()
        cur.remove(bookId.toString())
        prefs(ctx).edit { putStringSet(KEY_LIST, cur) }
    }

    fun getIds(ctx: Context): Set<String> =
        (prefs(ctx).getStringSet(KEY_LIST, emptySet()) ?: emptySet())
            .mapNotNull { it.toString() }
            .toSet()

    fun getBooks(ctx: Context, allBooks: List<Book>): List<Book> {
        val ids = getIds(ctx)
        return allBooks.filter { it.id in ids }
    }
}