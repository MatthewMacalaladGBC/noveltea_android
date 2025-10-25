package ca.gbc.comp3074.noveltea_app

import android.content.Context
import android.content.Context.MODE_PRIVATE

object ReadingListStore {
    private const val PREFS = "noveltea_prefs"
    private const val KEY_LIST = "reading_list_ids"

    private fun prefs(ctx: Context) = ctx.getSharedPreferences(PREFS, MODE_PRIVATE)

    fun add(ctx: Context, bookId: Int) {
        val cur = prefs(ctx).getStringSet(KEY_LIST, emptySet())!!.toMutableSet()
        cur.add(bookId.toString())
        prefs(ctx).edit().putStringSet(KEY_LIST, cur).apply()
    }

    fun remove(ctx: Context, bookId: Int) {
        val cur = prefs(ctx).getStringSet(KEY_LIST, emptySet())!!.toMutableSet()
        cur.remove(bookId.toString())
        prefs(ctx).edit().putStringSet(KEY_LIST, cur).apply()
    }

    fun getIds(ctx: Context): Set<Int> =
        (prefs(ctx).getStringSet(KEY_LIST, emptySet()) ?: emptySet())
            .mapNotNull { it.toIntOrNull() }
            .toSet()

    fun getBooks(ctx: Context, allBooks: List<Book>): List<Book> {
        val ids = getIds(ctx)
        return allBooks.filter { it.id in ids }
    }
}
