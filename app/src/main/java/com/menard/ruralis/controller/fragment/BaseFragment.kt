package com.menard.ruralis.controller.fragment

import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.menard.ruralis.utils.Constants

open class BaseFragment : Fragment() {

    //-- PERMISSIONS--//
    /**
     * Check if permissions are granted. If not, request
     */
    open fun checkPermissions(): Boolean {
        return if(ActivityCompat.checkSelfPermission(this.activity!!, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            true
        }else{
            //-- Request permissions --//
            ActivityCompat.requestPermissions(this.activity!!, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), Constants.RC_FINE_LOCATION)
            false
        }
    }

    /**
     * After requesting permissions
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            Constants.RC_FINE_LOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Permission granted", "Permission granted")
                } else {
                    Log.d("Permission denied", "Permission denied")
                }
                return
            }
        }
    }
}