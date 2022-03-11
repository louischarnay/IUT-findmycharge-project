package com.iut.kotlin.findmycharge

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Criteria
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    companion object{
        val INTENT_EXTRA_RESULT_1 = "user_latitude"
        val INTENT_EXTRA_RESULT_2 = "user_longitude"
    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val criteres = Criteria()
        criteres.setAccuracy(Criteria.ACCURACY_FINE)

        var fournisseur = String()
        fournisseur = locationManager.getBestProvider(criteres, true).toString()

        val localisation = locationManager.getLastKnownLocation(fournisseur)
        val latitude = localisation?.latitude
        val longitude = localisation?.longitude

        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener(View.OnClickListener{
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra(INTENT_EXTRA_RESULT_1, latitude)
            intent.putExtra(INTENT_EXTRA_RESULT_2, longitude)
            startActivity(intent)
            finish()
        })
    }
}
