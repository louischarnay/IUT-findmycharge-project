package com.iut.kotlin.findmycharge

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.iut.kotlin.findmycharge.databinding.ActivityMapsBinding
import org.json.JSONArray
import org.json.JSONObject

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var lat: Double = 0.0
    private var long: Double = 0.0
    private var bornes: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Récupération des données par l'intent
        val intent = intent
        this.lat = intent.getDoubleExtra(MainActivity.INTENT_EXTRA_RESULT_1, 0.0)
        this.long = intent.getDoubleExtra(MainActivity.INTENT_EXTRA_RESULT_2, 0.0)
        this.bornes = intent.getStringExtra(MainActivity.INTENT_EXTRA_RESULT_3).toString()

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //Affichage du point de position de l'utilisateur
        val pos = LatLng(this.lat, this.long)
        mMap.addMarker(MarkerOptions().position(pos).title("Votre localisation").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))

        //Parsage des points et affichage
        val json = JSONArray(bornes)
        for (i in 0 until json.length()){
            var borne = JSONObject(json[i].toString()).getJSONObject("fields")
            var pos = LatLng(borne.getJSONArray("coordonneesxy").getDouble(0), borne.getJSONArray("coordonneesxy").getDouble(1))
            var name = borne.getString("nom_station")
            mMap.addMarker(MarkerOptions().position(pos).title(name).icon(BitmapDescriptorFactory.defaultMarker()))
        }

        //Configuration de la vue de la mMap
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pos))
        mMap.setMinZoomPreference(13.0f)
    }
}