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
import android.widget.ImageButton
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
        val INTENT_EXTRA_RESULT_4 = "city_name"
        val INTENT_EXTRA_RESULT_5 = "range"
        val INTENT_EXTRA_RESULT_6 = "only_free"
    }
    lateinit var listBornes : ArrayList<Bornes>
    var adapter : BornesListAdapter? = null
    var tv : TextView? = null
    var latitude : Double? = 0.0
    var longitude : Double? = 0.0
    var cityName : String? = ""
    var range : Double? = 10000.0
    var onlyFree : Boolean? = false

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
        latitude = localisation?.latitude
        Log.d("MainActivity", "latitude=$latitude")
        longitude = localisation?.longitude
        Log.d("MainActivity", "longitude=$longitude")

        //Récupération composants graphiques
        val button = findViewById<Button>(R.id.button)
        val search = findViewById<ImageButton>(R.id.ib_search)
        tv = findViewById<TextView>(R.id.tv_nbBornes)
        val lv = findViewById<ListView>(R.id.lv_bornes)

        //Récupération json
        listBornes = HttpConnectServerAsyncTask().execute(latitude, longitude, range, cityName, onlyFree).get()

        //Affichage nombre max de bornes
        tv?.text = listBornes?.size.toString() + " borne(s) près de votre position"

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

        search.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, Params::class.java)
            intent.putExtra(INTENT_EXTRA_RESULT_4, cityName)
            intent.putExtra(INTENT_EXTRA_RESULT_5, range)
            intent.putExtra(INTENT_EXTRA_RESULT_6, onlyFree)
            startActivityForResult(intent, 1000)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1000){
            if (data != null) {
                cityName = data.getStringExtra(Params.INTENT_EXTRA_RESULT_1)
                range = data.getDoubleExtra(Params.INTENT_EXTRA_RESULT_2, 10000.0)
                onlyFree = data.getBooleanExtra(Params.INTENT_EXTRA_RESULT_3, false)

                listBornes.clear()
                listBornes.addAll(HttpConnectServerAsyncTask().execute(latitude, longitude, range, cityName, onlyFree).get())

                //Affichage nombre max de bornes
                tv?.text = listBornes.size.toString() + " borne(s) près de votre position"
            }
            adapter?.notifyDataSetChanged()
        }
    }
}
