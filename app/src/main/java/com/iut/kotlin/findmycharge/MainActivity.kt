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
    var listBornes = mutableListOf<Bornes>()
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
        var url = "https://public.opendatasoft.com/api/records/1.0/search/?dataset=mobilityref-france-irve-202&q=&rows=0&geofilter.distance=" + latitude.toString() + "%2C+" + longitude.toString() + "%2C+10000"
        var result = HttpConnectServerAsyncTask().execute(url)
        var json = JSONObject(result.get().toString())

        //Récupération du nombre de données
        val nbBornes = json.getInt("nhits")
        url = "https://public.opendatasoft.com/api/records/1.0/search/?dataset=mobilityref-france-irve-202&q=&rows=$nbBornes&geofilter.distance=" + latitude.toString() + "%2C+" + longitude.toString() + "%2C+10000"
        result = HttpConnectServerAsyncTask().execute(url)
        json = JSONObject(result.get().toString())
        Log.d("MainActivity", "nbBornes=$nbBornes")
        tv.text =  nbBornes.toString() + " borne(s) près de votre position"

        //Récupération points les plus proches
        val bornes : JSONArray = json.getJSONArray("records")

        //Construction de la liste
        for (i in 0 until nbBornes){
            var borne = JSONObject(bornes[i].toString()).getJSONObject("fields")
            listBornes.add(Bornes("", borne.getString("nom_station"), "", "", "", ""))
        }
        adapter = BornesListAdapter(listBornes)
        lv?.adapter = adapter

        //Création Listeners
        button.setOnClickListener(View.OnClickListener{
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra(INTENT_EXTRA_RESULT_1, latitude)
            intent.putExtra(INTENT_EXTRA_RESULT_2, longitude)
            intent.putExtra(INTENT_EXTRA_RESULT_3, bornes.toString())
            startActivity(intent)
            finish()
        })
    }
}
