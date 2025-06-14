package com.recep.recep

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object RecepUtils {
    fun isNetworkAvailable(context: Context): Boolean {
        val cm  = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false

        return (capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
            && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) == true)
    }
}