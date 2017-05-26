package pe.devpicon.android.myapplication

import android.location.Location
import android.util.Log

/**
 * Created by armando on 5/26/17.
 */
class MainPresenter(val mainActivity: MainActivity) {
    val TAG = javaClass.simpleName

    fun saveLocationInDatabase(location: Location) {

        // Validate if this location has Horizontal Accuracy
        var horizontalAccuracy = 0f
        if (location.hasAccuracy()) {
            horizontalAccuracy = location.accuracy
        }

            val myLocation = MyLocation(location.latitude, location.longitude, horizontalAccuracy, location.time)
            val dbManager = DbManager.getInstance(mainActivity)

            dbManager.insertLocationInDatabase(myLocation)
            dbManager.getLastLocationStored()?.let {
                mainActivity.showLastRecord(it)
                Log.d(TAG, "Last record: " + it.toString())
            }



    }

}