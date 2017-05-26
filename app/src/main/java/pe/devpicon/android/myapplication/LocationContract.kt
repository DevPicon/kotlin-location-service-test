package pe.devpicon.android.myapplication

import android.provider.BaseColumns

/**
 * Created by armando on 5/26/17.
 */
object LocationContract{
    class LocationEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "mylocations"
            val COLUMN_TITLE_LONGITUD = "longitude"
            val COLUMN_TITLE_LATITUDE = "latitude"
            val COLUMN_TITLE_HORIZONTAL_ACCURACY = "horizontal_acc"
            val COLUMN_TITLE_TIMESTAMP = "timestamp"
        }
    }
}