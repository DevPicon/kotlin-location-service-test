package pe.devpicon.android.myapplication

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID

/**
 * Created by armando on 5/26/17.
 */
class LocationDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        val DATABASE_NAME = "mylocations.db"
        val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val SQL_CREATE_TABLE = "CREATE TABLE ${LocationContract.LocationEntry.TABLE_NAME} (" +
                "${_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${LocationContract.LocationEntry.COLUMN_TITLE_LATITUDE} DOUBLE NOT NULL," +
                "${LocationContract.LocationEntry.COLUMN_TITLE_LONGITUD} DOUBLE NOT NULL," +
                "${LocationContract.LocationEntry.COLUMN_TITLE_HORIZONTAL_ACCURACY} DOUBLE NOT " +
                "NULL," +
                "${LocationContract.LocationEntry.COLUMN_TITLE_TIMESTAMP} BIGINT NOT NULL);"

        db?.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${LocationContract.LocationEntry.TABLE_NAME}")
        onCreate(db)
    }

}