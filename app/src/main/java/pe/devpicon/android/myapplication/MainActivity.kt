package pe.devpicon.android.myapplication

import android.content.IntentSender
import android.location.Location
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity(), LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private var lastTime: Long = 0
    private val googleApiClient: GoogleApiClient by lazy { initGoogleApiClient() }
    private val locationRequest: LocationRequest by lazy { initLocationRequest() }
    private val INTERVAL = 60 * 1000
    private val CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000
    private val TAG = javaClass.simpleName
    private lateinit var presenter: MainPresenter

    override fun onStart() {
        super.onStart()
        googleApiClient.connect()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(this)
    }

    override fun onStop() {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this@MainActivity)
        googleApiClient.disconnect()
        super.onStop()
    }


    private fun initLocationRequest(): LocationRequest = LocationRequest.create()
            .setPriority(PRIORITY_HIGH_ACCURACY)
            .setInterval(INTERVAL.toLong())
            .setSmallestDisplacement(10.0F)

    private fun initGoogleApiClient(): GoogleApiClient = GoogleApiClient
            .Builder(this@MainActivity)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this@MainActivity)
            .addOnConnectionFailedListener(this@MainActivity)
            .build()


    override fun onLocationChanged(location: Location?) {
        location?.let {
            showToast("onLocationChanged: ${location.longitude} - ${location.latitude} - ${location.time}")

            Log.d(TAG, "locationTime:${location.time} / lastTime:${lastTime} = ${location.time -
                    lastTime}")

            if ((location.time - lastTime) > INTERVAL) {
                presenter.saveLocationInDatabase(location)
                // Store temporarily last time registered
                lastTime = location.time
            } else {
                Log.d(TAG, "Time is less than 1 minute.")
            }

        }
    }

    override fun onConnected(bundle: Bundle?) {
        val currentLocation: Location? = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)
        currentLocation?.let {
            showToast("${currentLocation.longitude} - ${currentLocation.latitude}")
            presenter.saveLocationInDatabase(currentLocation);
        }

        startLocationUpdate()
    }

    override fun onConnectionSuspended(cause: Int) {
        when (cause) {
            CAUSE_SERVICE_DISCONNECTED -> showToast("Disconnected. Please reconnect.")
            CAUSE_NETWORK_LOST -> showToast("Network lost. Please reconnect.")
        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST)
            } catch (e: IntentSender.SendIntentException) {
                Log.e(TAG, "Error in resolution for result", e)
            }
        } else {
            showToast("Location services not available.")
        }
    }

    private fun startLocationUpdate() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient,
                locationRequest,
                this@MainActivity)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showLastRecord(myLocation: MyLocation) {
        val txtLastLocationRecorded = findViewById(R.id.lastLocationRecorded) as TextView
        txtLastLocationRecorded.text = myLocation.toString()
    }


}


