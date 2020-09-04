package com.example.android.networkqualitycdc.myapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.android.networkqualitycdc.connectionSpeedClasses.AndroidCheckConnectionSpeed
import com.example.android.networkqualitycdc.connectionSpeedClasses.DownloadSpeedCheckJava
import com.example.android.networkqualitycdc.connectionSpeedClasses.DownloadSpeedCheckEventListener
import com.facebook.network.connectionclass.ConnectionClassManager
import com.facebook.network.connectionclass.ConnectionQuality
import com.facebook.network.connectionclass.DeviceBandwidthSampler
import kotlinx.android.synthetic.main.activity_choose_connectivity_check.*

class ChooseConnectivityCheckActivity : AppCompatActivity() ,
    DownloadSpeedCheckEventListener {


    private val KEY_CONNECTION_SPEED: String? = "KEY_CONNECTION_SPEED"

    /*metodi utili per facebook network connectivity */
    public var connectionQuality : SPEED = SPEED.NOT_AVAILABLE
    private var mConnectionClass : ConnectionQuality = ConnectionQuality.UNKNOWN
    private lateinit var mConnectionClassManager: ConnectionClassManager
    private lateinit var mDeviceBandwidthSampler : DeviceBandwidthSampler
    private lateinit var mListener : ConnectionChangedListener



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_connectivity_check)

        mConnectionClassManager = ConnectionClassManager.getInstance()
        mDeviceBandwidthSampler = DeviceBandwidthSampler.getInstance()


        android_check_connection.setOnClickListener {
            checkAndroidConnection()
//            startActivity(createOpenIntent(this, AndroidCheckConnectionSpeed.connectionSpeed(this)))
        }

        download_speed_test.setOnClickListener {
            checkDownloadSpeed()
        }

        facebook_network_connectivity_class.setOnClickListener {
            checkFacebookNetworkConnectivity()
//            startActivity(createOpenIntent(this, connectionSpeed = checkFacebookNetworkConnectivity()))
        }

        goto_app.setOnClickListener {
            startActivity(createOpenIntent(this, connectionQuality))
        }

    }

    fun createOpenIntent(context: Context, connectionSpeed : SPEED): Intent {
        val i = Intent(context, LoginActivity::class.java)
        i.putExtra(KEY_CONNECTION_SPEED, connectionSpeed)
        return i
    }


    fun checkAndroidConnection() {

        is_connesso.text = AndroidCheckConnectionSpeed.isConnected(this).toString()
        connection_type.text = AndroidCheckConnectionSpeed.connectionType(this)
        connection_quality.text = AndroidCheckConnectionSpeed.connectionSpeed(this).toString()
        connectionQuality = AndroidCheckConnectionSpeed.connectionSpeed(this)

    }


    override fun onEventFailed() {
        is_connesso.text = "FAILED"
        connection_type.text = "FAILED"
        connection_quality.text = SPEED.NOT_AVAILABLE.toString()
        connectionQuality = SPEED.NOT_AVAILABLE
    }

    override fun onEventCompleted(speed: SPEED) {

        is_connesso.text = (!speed.equals(SPEED.NOT_AVAILABLE)).toString()
        connection_type.text = "UNKNOWN"
        connection_quality.text = speed.toString()
        connectionQuality = speed
    }

    fun checkDownloadSpeed(){

        DownloadSpeedCheckJava().downloadSpeedCheck(this)

    }

    fun checkFacebookNetworkConnectivity() {

        var speed = mConnectionClassManager.getCurrentBandwidthQuality()
        is_connesso.text = speed.toString()
        connection_type.text = "UNKNOWN"
        connection_quality.text = speed.toString()
        connectionQuality = SPEED.NOT_AVAILABLE

    }


    // Listener to update the UI upon connectionclass change.
    private inner class ConnectionChangedListener :
        ConnectionClassManager.ConnectionClassStateChangeListener {

        override fun onBandwidthStateChange(bandwidthState: ConnectionQuality) {
            mConnectionClass = bandwidthState
            runOnUiThread {
                // do something
            }
        }
    }






//    o POOR: larghezza di banda sotto 150 kbps
//    o MODERATE: larghezza di banda compresa tra 120 e 550 kbps
//    o GOOD: larghezza di banda compresa tra 550 e 2000 kbps
//    o EXCELLENT: larghezza di banda sopra i 2000 kbps

    enum class SPEED {
        POOR, MODERATE, GOOD, EXCELLENT, NOT_AVAILABLE
    }


}
