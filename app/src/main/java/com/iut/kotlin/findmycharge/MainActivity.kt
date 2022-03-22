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
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    companion object{
        val INTENT_EXTRA_RESULT_1 = "user_latitude"
        val INTENT_EXTRA_RESULT_2 = "user_longitude"
        val INTENT_EXTRA_RESULT_3 = "bornes_list"
    }
    var adapter : BornesListAdapter? = null

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Récupération localisation actuelle
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val criteres = Criteria()
        criteres.setAccuracy(Criteria.ACCURACY_FINE)

        var fournisseur = String()
        fournisseur = locationManager.getBestProvider(criteres, true).toString()

        val localisation = locationManager.getLastKnownLocation(fournisseur)
        val latitude = localisation?.latitude
        Log.d("MainActivity", "latitude=$latitude")
        val longitude = localisation?.longitude
        Log.d("MainActivity", "longitude=$longitude")

        //Récupération composants graphiques
        val button = findViewById<Button>(R.id.button)
        val tv = findViewById<TextView>(R.id.tv_nbBornes)
        val lv = findViewById<ListView>(R.id.lv_bornes)

        //Récupération json
        val listBornes = HttpConnectServerAsyncTask().execute(latitude, longitude, 10000).get()

        //Affichage nombre max de bornes
        tv.text = listBornes.size.toString() + " borne(s) près de votre position"

        //Affichage de la liste
        adapter = BornesListAdapter(listBornes)
        lv?.adapter = adapter

        //Création Listeners
        button.setOnClickListener(View.OnClickListener{
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra(INTENT_EXTRA_RESULT_1, latitude)
            intent.putExtra(INTENT_EXTRA_RESULT_2, longitude)
            intent.putExtra(INTENT_EXTRA_RESULT_3, listBornes)
            startActivity(intent)
        })
    }
}
