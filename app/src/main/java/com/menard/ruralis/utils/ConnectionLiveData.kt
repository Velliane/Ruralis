package com.menard.ruralis.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import androidx.lifecycle.LiveData

class ConnectionLiveData(private val context: Context): LiveData<ConnectionModel>() {

    val WifiData = 1
    val MobileData = 2

    override fun onActive() {
        super.onActive()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(networkReceiver, filter)
    }

    override fun onInactive() {
        super.onInactive()
        context.unregisterReceiver(networkReceiver)
    }

    private val networkReceiver = object:BroadcastReceiver() {
        override fun onReceive(context:Context, intent: Intent) {
            if (intent.extras != null)
            {
                val activeNetwork = intent.extras?.get(ConnectivityManager.EXTRA_NETWORK_INFO) as NetworkInfo
                val isConnected = (activeNetwork != null && activeNetwork.isConnectedOrConnecting())
                if (isConnected)
                {
                    when (activeNetwork.type) {
                        ConnectivityManager.TYPE_WIFI -> postValue(ConnectionModel(WifiData, true))
                        ConnectivityManager.TYPE_MOBILE -> postValue(ConnectionModel(MobileData, true))
                        ConnectivityManager.TYPE_MOBILE_DUN -> postValue(ConnectionModel(MobileData, true))

                    }
                    Log.d("Connection", isConnected.toString())
                }
                else
                {
                    postValue(ConnectionModel(0, false))
                }
            }
        }
    }
}