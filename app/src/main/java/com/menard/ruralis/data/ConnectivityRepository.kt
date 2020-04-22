package com.menard.ruralis.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkInfo
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.menard.ruralis.BuildConfig
import com.menard.ruralis.utils.ConnectionModel


class ConnectivityRepository(private val context: Context) {

    private val liveData = MutableLiveData<Boolean>()
    val connectivityLiveData: LiveData<Boolean> = liveData

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            wireLiveDataApi24()
        }else{
            wireLiveDataLegacy()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    fun wireLiveDataApi24(){
        try {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                connectivityManager.registerDefaultNetworkCallback(object : NetworkCallback() {
                    override fun onAvailable(network: Network?) {
                        liveData.postValue(true)
                    }

                    override fun onLost(network: Network?) {
                        liveData.postValue(false)
                    }
                }
                )
        } catch (e: Exception) {
            liveData.postValue(false)
        }
    }

    private fun wireLiveDataLegacy(){
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        val networkReceiver = object: BroadcastReceiver() {
            override fun onReceive(context:Context, intent: Intent) {
                if (intent.extras != null) {
                    val activeNetwork = intent.extras?.get(ConnectivityManager.EXTRA_NETWORK_INFO) as? NetworkInfo
                    val isConnected = (activeNetwork != null && activeNetwork.isConnectedOrConnecting)
                    if (isConnected) {
                        when (activeNetwork?.type) {
                            ConnectivityManager.TYPE_WIFI,
                            ConnectivityManager.TYPE_MOBILE,
                            ConnectivityManager.TYPE_MOBILE_DUN -> liveData.postValue(true)
                        }
                        Log.d("Connection", isConnected.toString())
                    } else {
                        liveData.postValue(false)
                    }
                }
            }
        }

        context.registerReceiver(networkReceiver, filter)
    }

}