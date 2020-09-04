package com.example.android.networkqualitycdc.connectionSpeedClasses

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.telephony.TelephonyManager
import com.example.android.networkqualitycdc.myapplication.ChooseConnectivityCheckActivity


object AndroidCheckConnectionSpeed {

    // Speed Standards Used
//    o POOR: larghezza di banda sotto 150 kbps
//    o MODERATE: larghezza di banda compresa tra 120 e 550 kbps
//    o GOOD: larghezza di banda compresa tra 550 e 2000 kbps
//    o EXCELLENT: larghezza di banda sopra i 2000 kbps

    // bandwidth standard limits in kbps
    private val POOR_BANDWIDTH = 120
    private val AVERAGE_BANDWIDTH = 550
    private val GOOD_BANDWIDTH = 2000

    fun getNetworkInfo(context: Context): NetworkInfo? {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo
    }

    fun isConnected(context: Context): Boolean {
        val info = getNetworkInfo(context)
        return info != null && info.isConnected
    }

    fun connectionType(context: Context): String {
        val info = getNetworkInfo(context)
        if (info != null && info.isConnected) {
            if (info.subtypeName.isEmpty()) {
                if (info.typeName.isEmpty()) {
                    return "UNKNOWN"
                } else {
                    return info.typeName
                }
            } else {
                return info.subtypeName
            }
        } else {
            return "UNKNOWN"
        }
    }


    fun isConnectedWifi(context: Context): Boolean {
        val info = getNetworkInfo(context)
        return info != null && info.isConnected && info.type == ConnectivityManager.TYPE_WIFI
    }

    fun isConnectedMobile(context: Context): Boolean {
        val info = getNetworkInfo(context)
        return info != null && info.isConnected && info.type == ConnectivityManager.TYPE_MOBILE
    }

    fun isConnectedFast(context: Context): Boolean {
        val info = getNetworkInfo(context)
        return info != null && info.isConnected && isConnectionFast(
            info.type,
            info.subtype
        )
    }


    //Check if the connection is fast

    fun isConnectionFast(type: Int, subType: Int): Boolean {
        return if (type == ConnectivityManager.TYPE_WIFI) {
            true
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            when (subType) {
                TelephonyManager.NETWORK_TYPE_1xRTT -> false // ~ 50-100 kbps
                TelephonyManager.NETWORK_TYPE_CDMA -> false // ~ 14-64 kbps
                TelephonyManager.NETWORK_TYPE_EDGE -> false // ~ 50-100 kbps
                TelephonyManager.NETWORK_TYPE_EVDO_0 -> true // ~ 400-1000 kbps
                TelephonyManager.NETWORK_TYPE_EVDO_A -> true // ~ 600-1400 kbps
                TelephonyManager.NETWORK_TYPE_GPRS -> false // ~ 100 kbps
                TelephonyManager.NETWORK_TYPE_HSDPA -> true // ~ 2-14 Mbps
                TelephonyManager.NETWORK_TYPE_HSPA -> true // ~ 700-1700 kbps
                TelephonyManager.NETWORK_TYPE_HSUPA -> true // ~ 1-23 Mbps
                TelephonyManager.NETWORK_TYPE_UMTS -> true // ~ 400-7000 kbps
                /*
         * Above API level 7, make sure to set android:targetSdkVersion
         * to appropriate level to use these
         */
                TelephonyManager.NETWORK_TYPE_EHRPD // API level 11
                -> true // ~ 1-2 Mbps
                TelephonyManager.NETWORK_TYPE_EVDO_B // API level 9
                -> true // ~ 5 Mbps
                TelephonyManager.NETWORK_TYPE_HSPAP // API level 13
                -> true // ~ 10-20 Mbps
                TelephonyManager.NETWORK_TYPE_IDEN // API level 8
                -> false // ~25 kbps
                TelephonyManager.NETWORK_TYPE_LTE // API level 11
                -> true // ~ 10+ Mbps
                // Unknown
                TelephonyManager.NETWORK_TYPE_UNKNOWN -> false
                else -> false
            }
        } else {
            false
        }
    }


    fun connectionSpeed(context: Context): ChooseConnectivityCheckActivity.SPEED {
        var type: Int
        var subType: Int
        val info = getNetworkInfo(context)
        if (info == null) {
            return ChooseConnectivityCheckActivity.SPEED.NOT_AVAILABLE
        } else {
            type = info.type
            subType = info.subtype
        }
        return if (type == ConnectivityManager.TYPE_WIFI) {

            /*FUNZIONA SOLO PER WIFI*/
            val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiInfo = wifiManager.connectionInfo
            val speed = wifiInfo.linkSpeed * 1000
            return networkQualityFromkbps(speed)
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            when (subType) {
                TelephonyManager.NETWORK_TYPE_1xRTT -> ChooseConnectivityCheckActivity.SPEED.POOR // ~ 50-100 kbps
                TelephonyManager.NETWORK_TYPE_CDMA -> ChooseConnectivityCheckActivity.SPEED.POOR // ~ 14-64 kbps
                TelephonyManager.NETWORK_TYPE_EDGE -> ChooseConnectivityCheckActivity.SPEED.POOR // ~ 50-100 kbps
                TelephonyManager.NETWORK_TYPE_EVDO_0 -> ChooseConnectivityCheckActivity.SPEED.GOOD // ~ 400-1000 kbps
                TelephonyManager.NETWORK_TYPE_EVDO_A -> ChooseConnectivityCheckActivity.SPEED.GOOD // ~ 600-1400 kbps
                TelephonyManager.NETWORK_TYPE_GPRS -> ChooseConnectivityCheckActivity.SPEED.POOR // ~ 100 kbps
                TelephonyManager.NETWORK_TYPE_HSDPA -> ChooseConnectivityCheckActivity.SPEED.EXCELLENT // ~ 2-14 Mbps
                TelephonyManager.NETWORK_TYPE_HSPA -> ChooseConnectivityCheckActivity.SPEED.GOOD // ~ 700-1700 kbps
                TelephonyManager.NETWORK_TYPE_HSUPA -> ChooseConnectivityCheckActivity.SPEED.EXCELLENT // ~ 1-23 Mbps
                TelephonyManager.NETWORK_TYPE_UMTS -> ChooseConnectivityCheckActivity.SPEED.EXCELLENT // ~ 400-7000 kbps
                /*
         * Above API level 7, make sure to set android:targetSdkVersion
         * to appropriate level to use these
         */
                TelephonyManager.NETWORK_TYPE_EHRPD // API level 11
                -> ChooseConnectivityCheckActivity.SPEED.EXCELLENT // ~ 1-2 Mbps
                TelephonyManager.NETWORK_TYPE_EVDO_B // API level 9
                -> ChooseConnectivityCheckActivity.SPEED.EXCELLENT // ~ 5 Mbps
                TelephonyManager.NETWORK_TYPE_HSPAP // API level 13
                -> ChooseConnectivityCheckActivity.SPEED.EXCELLENT // ~ 10-20 Mbps
                TelephonyManager.NETWORK_TYPE_IDEN // API level 8
                -> ChooseConnectivityCheckActivity.SPEED.POOR // ~25 kbps
                TelephonyManager.NETWORK_TYPE_LTE // API level 11
                -> ChooseConnectivityCheckActivity.SPEED.EXCELLENT // ~ 10+ Mbps
                // Unknown
                TelephonyManager.NETWORK_TYPE_UNKNOWN -> ChooseConnectivityCheckActivity.SPEED.NOT_AVAILABLE
                else -> ChooseConnectivityCheckActivity.SPEED.NOT_AVAILABLE
            }
        } else {
            ChooseConnectivityCheckActivity.SPEED.NOT_AVAILABLE
        }
    }

    private fun networkQualityFromkbps(kilobitPerSec: Int): ChooseConnectivityCheckActivity.SPEED {
        if (kilobitPerSec == 0) {
            return ChooseConnectivityCheckActivity.SPEED.NOT_AVAILABLE
        }
        return if (kilobitPerSec <= POOR_BANDWIDTH) {
            ChooseConnectivityCheckActivity.SPEED.POOR
        } else if (kilobitPerSec <= AVERAGE_BANDWIDTH) {
            ChooseConnectivityCheckActivity.SPEED.MODERATE
        } else if (kilobitPerSec <= GOOD_BANDWIDTH) {
            ChooseConnectivityCheckActivity.SPEED.GOOD
        } else {
            ChooseConnectivityCheckActivity.SPEED.EXCELLENT
        }

    }

}