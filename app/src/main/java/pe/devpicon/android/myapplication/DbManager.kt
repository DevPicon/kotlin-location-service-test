package pe.devpicon.android.myapplication

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import android.util.Log
import pe.devpicon.android.myapplication.LocationContract.LocationEntry.Companion.COLUMN_TITLE_HORIZONTAL_ACCURACY
import pe.devpicon.android.myapplication.LocationContract.LocationEntry.Companion.COLUMN_TITLE_LATITUDE
import pe.devpicon.android.myapplication.LocationContract.LocationEntry.Companion.COLUMN_TITLE_LONGITUD
import pe.devpicon.android.myapplication.LocationContract.LocationEntry.Companion.COLUMN_TITLE_TIMESTAMP
import pe.devpicon.android.myapplication.LocationContract.LocationEntry.Companion.TABLE_NAME
import java.sql.SQLException


/**
 * Created by armando on 5/26/17.
 */
class DbManager private constructor(context: Context) {

    private val locationDbHelper: LocationDbHelper = LocationDbHelper(context)

    private val TAG = DbManager::class.java.simpleName

    fun insertLocationInDatabase(mylocation: MyLocation): Long {
        val database = locationDbHelper.writableDatabase
        val locationValue = ContentValues()
        locationValue.put(LocationContract.LocationEntry.COLUMN_TITLE_LATITUDE, mylocation.latitude)
        locationValue.put(LocationContract.LocationEntry.COLUMN_TITLE_LONGITUD, mylocation.longitude)
        locationValue.put(LocationContract.LocationEntry.COLUMN_TITLE_HORIZONTAL_ACCURACY,
                mylocation.horizontalAccuracy)

        var currentTimeMillis = mylocation.time
        Log.d(javaClass.simpleName, "currentTimeMillis:${currentTimeMillis}")

        locationValue.put(LocationContract.LocationEntry.COLUMN_TITLE_TIMESTAMP, currentTimeMillis)

        try {
            database.beginTransaction()
            database.insert(TABLE_NAME, null, locationValue)
            database.setTransactionSuccessful()

        } catch (e: SQLException) {
            Log.e(javaClass.simpleName, "Ha ocurrido un error durante la inserción.", e)
            currentTimeMillis = -1
        } finally {
            database.endTransaction()
        }

        return currentTimeMillis;
    }

    fun getLastLocationStored(): MyLocation? {
        val database = locationDbHelper.readableDatabase
        val cursor = database.query(TABLE_NAME, null, null, null, null, null, BaseColumns._ID + " DESC", "1")

        if(cursor.moveToLast()){
            val myLastLocation = MyLocation(
                    cursor.getDouble(cursor.getColumnIndex(COLUMN_TITLE_LATITUDE)),
                    cursor.getDouble(cursor.getColumnIndex(COLUMN_TITLE_LONGITUD)),
                    cursor.getFloat(cursor.getColumnIndex(COLUMN_TITLE_HORIZONTAL_ACCURACY)),
                    cursor.getLong(cursor.getColumnIndex(COLUMN_TITLE_TIMESTAMP)))

            return myLastLocation
        }
        return null
    }

    companion object {
        private var instance: DbManager? = null

        @Synchronized fun getInstance(context: Context): DbManager {
            if (instance == null) {
                instance = DbManager(context.applicationContext)
            }

            return instance as DbManager
        }
    }
}


