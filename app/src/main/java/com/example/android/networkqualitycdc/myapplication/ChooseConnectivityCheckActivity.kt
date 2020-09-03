package com.example.android.networkqualitycdc.myapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.android.networkqualitycdc.connectionSpeedClasses.AndroidCheckConnectionSpeed
import com.example.android.networkqualitycdc.connectionSpeedClasses.DownloadSpeedCheckJava
import com.example.android.networkqualitycdc.connectionSpeedClasses.MyEventListener
import com.facebook.network.connectionclass.ConnectionClassManager
import com.facebook.network.connectionclass.ConnectionQuality
import com.facebook.network.connectionclass.DeviceBandwidthSampler
import kotlinx.android.synthetic.main.activity_choose_connectivity_check.*

class ChooseConnectivityCheckActivity : AppCompatActivity() , MyEventListener {


    private val KEY_CONNECTION_SPEED: String? = "KEY_CONNECTION_SPEED"

    /*metodi utili per facebook network connectivity */
    private var mConnectionClass : ConnectionQuality = ConnectionQuality.UNKNOWN;
    private lateinit var mConnectionClassManager: ConnectionClassManager;
    private lateinit var mDeviceBandwidthSampler : DeviceBandwidthSampler;
    private lateinit var mListener : ConnectionChangedListener



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_connectivity_check)

        mConnectionClassManager = ConnectionClassManager.getInstance()
        mDeviceBandwidthSampler = DeviceBandwidthSampler.getInstance()


        android_check_connection.setOnClickListener {
            startActivity(createOpenIntent(this, connectionSpeed = checkAndroidConnection()))
        }

        download_speed_test.setOnClickListener {
            checkDownloadSpeed()
        }

        facebook_network_connectivity_class.setOnClickListener {
            startActivity(createOpenIntent(this, connectionSpeed = checkFacebookNetworkConnectivity()))
        }


    }

    fun createOpenIntent(context: Context, connectionSpeed : SPEED): Intent {
        val i = Intent(context, LoginActivity::class.java)
        i.putExtra(KEY_CONNECTION_SPEED, connectionSpeed)
        return i
    }

    //TODO-AMPLIARE LE VELOCITA' POSSIBILI DA RESTITUIRE MODIFICANDO ANDROIDCHECKCONNECTIONSPEED
    //TODO-Aggiungere informazioni velocità in un alert bloccante : https://www.journaldev.com/309/android-alert-dialog-using-kotlin
    fun checkAndroidConnection(): SPEED {

        var alert = AlertDialog.Builder(this)

        if (!AndroidCheckConnectionSpeed.isConnected(this)) {
            alert.setMessage("velocità non disponibile")
            alert.show()
            return SPEED.NOT_AVAILABLE
        }

        if (AndroidCheckConnectionSpeed.isConnectedFast(this)) {
            alert.setMessage("ottima velocità")
            alert.show()
            return SPEED.EXCELLENT
        }
        else {
            alert.setMessage("scarsa velocità")
            alert.show()
            return SPEED.POOR
        }
    }


    override fun onEventFailed() {
        var alert = AlertDialog.Builder(this)
        alert.setMessage("velocità non disponibile")
        this.runOnUiThread{alert.show()}
        startActivity(createOpenIntent(this, SPEED.NOT_AVAILABLE))
    }

    override fun onEventCompleted(speed: SPEED) {
        var alert = AlertDialog.Builder(this)

        if(speed.equals(SPEED.EXCELLENT)){
            alert.setMessage("ottima velocità")
            this.runOnUiThread{alert.show()}

        }
        else if(speed.equals(SPEED.POOR)){
            alert.setMessage("scarsa velocità")
            this.runOnUiThread{alert.show()}

        } else {
            alert.setMessage("velocità non disponibile")
            this.runOnUiThread{alert.show()}

        }
        startActivity(createOpenIntent(this, speed))
    }

    fun checkDownloadSpeed(){

        DownloadSpeedCheckJava().downloadSpeedCheck(this)

    }

    fun checkFacebookNetworkConnectivity(): SPEED {

        var alert = AlertDialog.Builder(this)

        if(mConnectionClassManager.getCurrentBandwidthQuality().toString().equals(SPEED.EXCELLENT.toString())){
            alert.setMessage("ottima velocità")
            alert.show()
            return SPEED.EXCELLENT
        }
        else if(mConnectionClassManager.getCurrentBandwidthQuality().toString().equals(SPEED.POOR.toString())){
            alert.setMessage("scarsa velocità")
            alert.show()
            return SPEED.POOR
        } else {
            alert.setMessage("velocità non disponibile")
            alert.show()
            return SPEED.NOT_AVAILABLE
        }
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
