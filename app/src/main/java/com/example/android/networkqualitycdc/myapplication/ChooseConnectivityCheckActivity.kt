package com.example.android.networkqualitycdc.myapplication

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.TrafficStats
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.android.networkqualitycdc.connectionSpeedClasses.AndroidCheckConnectionSpeed
import com.example.android.networkqualitycdc.connectionSpeedClasses.DownloadSpeedCheckJava
import com.example.android.networkqualitycdc.connectionSpeedClasses.DownloadSpeedCheckEventListener
import com.example.android.networkqualitycdc.customsampler.DeviceBandwidthSamplerCustom
import com.facebook.network.connectionclass.ConnectionClassManager
import com.facebook.network.connectionclass.ConnectionQuality
import com.facebook.network.connectionclass.DeviceBandwidthSampler
import kotlinx.android.synthetic.main.activity_choose_connectivity_check.*
import okhttp3.Request
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection

class ChooseConnectivityCheckActivity : AppCompatActivity() ,
    DownloadSpeedCheckEventListener {



    /*metodi utili per facebook network connectivity */
    private val TAG = "ConnectionClass_Sample"
    var connectionQuality : SPEED = SPEED.NOT_AVAILABLE
    private var mConnectionClass : ConnectionQuality = ConnectionQuality.UNKNOWN
    private lateinit var mConnectionClassManager: ConnectionClassManager
    private lateinit var mDeviceBandwidthSamplerCustom : DeviceBandwidthSamplerCustom
    private lateinit var mDeviceBandwidthSampler : DeviceBandwidthSampler
    private lateinit var mListener : ConnectionChangedListener
    private var mTries = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_connectivity_check)


        mConnectionClassManager = ConnectionClassManager.getInstance()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            mDeviceBandwidthSamplerCustom = DeviceBandwidthSamplerCustom.getInstance()
        } else {
            mDeviceBandwidthSampler = DeviceBandwidthSampler.getInstance()

        }

            mListener = ConnectionChangedListener()

        android_check_connection.setOnClickListener {
            checkAndroidConnection()
        }

        download_speed_test.setOnClickListener {
            checkDownloadSpeed()
        }

        facebook_network_connectivity_class.setOnClickListener {
            checkFacebookNetworkConnectivity()
        }

        goto_app.setOnClickListener {
            startActivity(LoginActivity.createOpenIntent(this, connectionQuality))
        }

    }

    override fun onPause() {
        super.onPause()
        mConnectionClassManager.remove(mListener)

    }

    override fun onResume() {
        super.onResume()
        mConnectionClassManager.register(mListener)
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
        mTries = 0
        var speed = mConnectionClassManager.getCurrentBandwidthQuality()
        is_connesso.text = (!speed.equals(ConnectionQuality.UNKNOWN)).toString()
        connection_type.text = "UNKNOWN"
        connection_quality.text = speed.toString()
        connectionQuality = convertFromConnectionClassToSpeed(speed)
        DnloadImage().execute("")

    }


    // Listener to update the UI upon connectionclass change.
//    private inner class ConnectionChangedListener :
//        ConnectionClassManager.ConnectionClassStateChangeListener {
//
//        override fun onBandwidthStateChange(bandwidthState: ConnectionQuality) {
//            mConnectionClass = bandwidthState
//            runOnUiThread {
//                // do something
//            }
//        }
//    }


    private inner class ConnectionChangedListener :
        ConnectionClassManager.ConnectionClassStateChangeListener {

        override fun onBandwidthStateChange(bandwidthState: ConnectionQuality) {
            mConnectionClass = bandwidthState
            runOnUiThread {
                connectionQuality = convertFromConnectionClassToSpeed(mConnectionClass)
                when (connectionQuality) {
                    SPEED.POOR -> {
                        val val1 = mConnectionClassManager.downloadKBitsPerSecond
                        connection_quality.text = "$connectionQuality" + val1
                        is_connesso.text = "true"
                        /*android.support.design.widget.Snackbar.make(
                            findViewById(R.id.main),
                            "Quality is $connectionQuality$val1\n and Bandwidth under 150 kbps so poor\n quality image is downloading",
                            android.support.design.widget.Snackbar.LENGTH_LONG
                        ).show()
                        DnloadImage().execute("http://storage.googleapis.com/ix_choosemuse/uploads/2016/02/android-logo.png") // 80 kb*/
                    }
                    SPEED.MODERATE -> {
                        val val2 = mConnectionClassManager.downloadKBitsPerSecond
                        connection_quality.text = "$connectionQuality" + val2
                        is_connesso.text = "true"
//                        android.support.design.widget.Snackbar.make(
//                            findViewById(R.id.main),
//                            "Quality is $connectionQuality$val2\n and Bandwidth between 150 to 550 kbps so moderate\n quality Image is downloading",
//                            android.support.design.widget.Snackbar.LENGTH_LONG
//                        ).show()
//                        DnloadImage().execute("http://static.giantbomb.com/uploads/original/15/157771/2312725-a10.jpeg") // 454 kb
                    }
                    SPEED.GOOD -> {
                        val val3 = mConnectionClassManager.downloadKBitsPerSecond
                        connection_quality.text = "$connectionQuality" + val3
                        is_connesso.text = "true"
//                        android.support.design.widget.Snackbar.make(
//                            findViewById(R.id.main),
//                            "Quality is $connectionQuality$val3\n and Bandwidth between 550 to 2000 kbps so good\n quality Image is downloading",
//                            android.support.design.widget.Snackbar.LENGTH_LONG
//                        ).show()
//                        DnloadImage().execute("http://techclones.com/wp-content/uploads/2013/09/Best-Dark-HD-Wallpaper-Android1.png") // 1.04 mb
                    }

                    SPEED.EXCELLENT -> {
                        val val4 = mConnectionClassManager.downloadKBitsPerSecond
                        connection_quality.text = "$connectionQuality" + val4
                        is_connesso.text = "true"
//                        android.support.design.widget.Snackbar.make(
//                            findViewById(R.id.main),
//                            "Quality is $connectionQuality$val4\n and Bandwidth over 2000 kbps so high\n quality Image is downlaoding",
//                            android.support.design.widget.Snackbar.LENGTH_LONG
//                        ).show()
//                        DnloadImage().execute("http://static.giantbomb.com/uploads/original/15/157771/2312721-a7.png") // 2.49 mb
                    }

                    SPEED.NOT_AVAILABLE -> connection_quality.text = "UNKNOWN"
                }
            }
        }
    }

    private fun convertFromConnectionClassToSpeed(mConnectionClass : ConnectionQuality): SPEED {
       return when (mConnectionClass) {
            ConnectionQuality.POOR -> SPEED.POOR
            ConnectionQuality.MODERATE -> SPEED.MODERATE
            ConnectionQuality.GOOD -> SPEED.GOOD
            ConnectionQuality.EXCELLENT -> SPEED.EXCELLENT
            ConnectionQuality.UNKNOWN -> SPEED.NOT_AVAILABLE
        }
    }

    private inner class DnloadImage : AsyncTask<String, Void, Bitmap>() {

        override fun onPreExecute() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                mDeviceBandwidthSamplerCustom.startSampling()
            } else {
                mDeviceBandwidthSampler.startSampling()
            }
            //mRunningBar.setVisibility(View.VISIBLE)
        }

        override fun doInBackground(vararg url: String): Bitmap? {

            val imageURL = "https://i.pinimg.com/originals/c7/93/35/c79335a0f2c30d5da05de7d0dd356092.jpg"
//            val imageURL = "https://effigis.com/wp-content/uploads/2015/02/DigitalGlobe_WorldView2_50cm_8bit_Pansharpened_RGB_DRA_Rome_Italy_2009DEC10_8bits_sub_r_1.jpg" // Very large image

            try {
                var url = URL(imageURL)
                var connection : HttpURLConnection =  url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                var input : InputStream = connection.getInputStream()
                var myBitmap : Bitmap = BitmapFactory.decodeStream(input)
                return myBitmap
            } catch (e : IOException) {
                e.printStackTrace()
                return null
            }
        }



        override fun onPostExecute(bp: Bitmap) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                mDeviceBandwidthSamplerCustom.stopSampling()
            } else {
                mDeviceBandwidthSampler.stopSampling()
            }

            Toast.makeText(this@ChooseConnectivityCheckActivity, "" + mTries, Toast.LENGTH_SHORT).show()

            if (mConnectionClass == ConnectionQuality.UNKNOWN && mTries < 10) {
                mTries++
                DnloadImage().execute("")
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                if (!mDeviceBandwidthSamplerCustom.isSampling) {
                    //mImageView.setImageBitmap(bp)
                    //imageLoader.getInstance().displayImage(mURL,mImageView);
                    //mRunningBar.setVisibility(View.GONE)

                }
            } else {
                if (!mDeviceBandwidthSampler.isSampling) {
                    //mImageView.setImageBitmap(bp)
                    //imageLoader.getInstance().displayImage(mURL,mImageView);
                    //mRunningBar.setVisibility(View.GONE)

                }
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
