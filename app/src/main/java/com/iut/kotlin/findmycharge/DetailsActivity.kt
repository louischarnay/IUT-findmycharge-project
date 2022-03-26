package com.iut.kotlin.findmycharge

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import org.w3c.dom.Text

class DetailsActivity : AppCompatActivity() {
    companion object{
        val INTENT_EXTRA_RESULT_1 = "user_latitude"
        val INTENT_EXTRA_RESULT_2 = "user_longitude"
        val INTENT_EXTRA_RESULT_3 = "bornes_list"
    }

    lateinit var bornes : ArrayList<Bornes>
    var lat : Double? = 0.0
    var long : Double? = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        //Récupération des données par l'intent
        val intent = intent
        this.lat = intent.getDoubleExtra(MainActivity.INTENT_EXTRA_RESULT_1, 0.0)
        this.long = intent.getDoubleExtra(MainActivity.INTENT_EXTRA_RESULT_2, 0.0)
        this.bornes = intent.getSerializableExtra(MainActivity.INTENT_EXTRA_RESULT_3) as ArrayList<Bornes>

        val tv_nom = findViewById<TextView>(R.id.tv_nom)
        val tv_adresse = findViewById<TextView>(R.id.tv_adresse)
        val tv_dep = findViewById<TextView>(R.id.tv_dep)
        val tv_region = findViewById<TextView>(R.id.tv_region)
        val tv_tel = findViewById<TextView>(R.id.tv_tel)
        val tv_impl = findViewById<TextView>(R.id.tv_impl)
        val tv_points = findViewById<TextView>(R.id.tv_points)
        val bt_see = findViewById<Button>(R.id.bt_see)

        tv_nom.setText(bornes[0].name)
        tv_adresse.setText(bornes[0].address)
        tv_dep.setText(bornes[0].zip)
        tv_region.setText(bornes[0].region)
        tv_tel.setText(bornes[0].tel)
        tv_impl.setText(bornes[0].impl)
        tv_points.setText(bornes[0].nbPoints + " point(s) de charge")

        bt_see.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra(MainActivity.INTENT_EXTRA_RESULT_1, lat)
            intent.putExtra(MainActivity.INTENT_EXTRA_RESULT_2, long)
            intent.putExtra(MainActivity.INTENT_EXTRA_RESULT_3, bornes)
            startActivity(intent)
        })
    }
}