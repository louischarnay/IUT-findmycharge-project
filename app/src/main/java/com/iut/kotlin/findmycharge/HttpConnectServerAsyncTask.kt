package com.iut.kotlin.findmycharge

import android.os.AsyncTask
import android.util.Log
import android.webkit.WebView
import android.widget.TextView
import com.google.android.gms.maps.model.LatLng
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class HttpConnectServerAsyncTask : AsyncTask<Any, Void, String>() {
    override fun doInBackground(vararg params: Any?): String {
        var result: String = ""
        var host = params[0] as String
        var url: URL? = URL(host)
        var urlConnection = url?.openConnection() as HttpURLConnection

        if (urlConnection.responseCode == HttpURLConnection.HTTP_OK){
            var input = BufferedReader(InputStreamReader(urlConnection.inputStream))
            result = input.readLine()
            Log.d("AsyncTask", "flux = $result")
            input.close()
        }
        urlConnection.disconnect()
        return result
    }
}