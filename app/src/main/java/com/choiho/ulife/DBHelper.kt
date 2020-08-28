package com.choiho.ulife

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.choiho.ulife.FeedReaderContract.SQL_CREATE_ENTRIES
import com.choiho.ulife.FeedReaderContract.SQL_DELETE_ENTRIES

object FeedReaderContract {
    // Table contents are grouped together in an anonymous object.
    object FeedEntry : BaseColumns {
        const val TABLE_NAME = "entry"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_SUBTITLE = "subtitle"
    }

    const val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${FeedEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${FeedEntry.COLUMN_NAME_TITLE} TEXT," +
                "${FeedEntry.COLUMN_NAME_SUBTITLE} TEXT)"

    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${FeedEntry.TABLE_NAME}"
}

class DBHelper(context: Context?) : SQLiteOpenHelper(context, _DBName, null, _DBVersion) {

    companion object {
        // If you change the database schema, you must increment the database version.
        const val _DBVersion = 1
        const val _DBName = "FeedReader.db"
    }

    // Table contents are grouped together in an anonymous object.

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db!!.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun writeDB(title: String, value: String) {
        // Gets the data repository in write mode
        if (readDB(title) != "") updateDB(title, value)
        else {
            val db = this.writableDatabase

            // Create a new map of values, where column names are the keys
            val values = ContentValues().apply {
                put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, title)
                put(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE, value)
            }
            // Insert the new row, returning the primary key value of the new row
            val newRowId = db?.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values)
        }
    }

    fun readDB(title: String): String {
        val db = this.readableDatabase

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        val projection = arrayOf(BaseColumns._ID, FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE)

        // Filter results WHERE "title" = 'My Title'
        val selection = "${FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE} = ?"
        val selectionArgs = arrayOf(title)

        // How you want the results sorted in the resulting Cursor
        val sortOrder = "${FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE} DESC"

        val cursor = db.query(
            FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )

//        val itemIds = mutableListOf<Long>()
        var value = ""
        with(cursor) {
            while (moveToNext()) {
//                val itemId = getLong(getColumnIndexOrThrow(BaseColumns._ID))
//                itemIds.add(itemId)
                value = getString(getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE))
                break
            }
        }

        return value
    }

    fun deleteDB(title:String) {
        val db = this.writableDatabase
        // Define 'where' part of query.
        val selection = "${FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE} LIKE ?"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(title)
        // Issue SQL statement.
        val deletedRows = db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, selection, selectionArgs)
    }

    fun updateDB(title:String, value:String) {
        val db = this.writableDatabase

        // New value for one column
        val values = ContentValues().apply {
            put(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE, value)
        }

        // Which row to update, based on the title
        val selection = "${FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE} LIKE ?"
        val selectionArgs = arrayOf(title)
        val count = db.update(
            FeedReaderContract.FeedEntry.TABLE_NAME,
            values,
            selection,
            selectionArgs)
    }
}