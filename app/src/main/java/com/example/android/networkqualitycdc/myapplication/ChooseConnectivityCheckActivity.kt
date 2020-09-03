package com.example.android.networkqualitycdc.myapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.android.networkqualitycdc.connectionSpeedClasses.AndroidCheckConnectionSpeed
import com.example.android.networkqualitycdc.connectionSpeedClasses.DownloadSpeedCheckJava
import kotlinx.android.synthetic.main.activity_choose_connectivity_check.*

class ChooseConnectivityCheckActivity : AppCompatActivity() {

    private val KEY_CONNECTION_SPEED: String? = "KEY_CONNECTION_SPEED"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_connectivity_check)


        android_check_connection.setOnClickListener {
            startActivity(createOpenIntent(this, connectionSpeed = checkAndroidConnection()))
        }

        download_speed_test.setOnClickListener {
            startActivity(createOpenIntent(this, connectionSpeed = checkDownloadSpeed()))
        }

        facebook_network_connectivity_class.setOnClickListener {
            startActivity(createOpenIntent(this, connectionSpeed = checkAndroidConnection()))
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

    fun checkDownloadSpeed(): SPEED {

        var alert = AlertDialog.Builder(this)

        if(DownloadSpeedCheckJava().downloadSpeedCheck().toString().equals(SPEED.EXCELLENT)){
            alert.setMessage("ottima velocità")
            alert.show()
            return SPEED.EXCELLENT
        }
        else if(DownloadSpeedCheckJava().downloadSpeedCheck().toString().equals(SPEED.POOR)){
            alert.setMessage("scarsa velocità")
            alert.show()
            return SPEED.POOR
        } else {
            return SPEED.NOT_AVAILABLE;
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
