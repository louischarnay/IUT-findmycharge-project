package com.iut.kotlin.findmycharge

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.Switch
import com.google.android.material.textfield.TextInputEditText

class Params : AppCompatActivity() {
    companion object{
        val INTENT_EXTRA_RESULT_1 = "city_name"
        val INTENT_EXTRA_RESULT_2 = "range"
        val INTENT_EXTRA_RESULT_3 = "only_free"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_params)

        val intent = intent
        var cityName = intent.getStringExtra(MainActivity.INTENT_EXTRA_RESULT_4)
        var range = intent.getDoubleExtra(MainActivity.INTENT_EXTRA_RESULT_5, 10000.0).toInt() / 1000
        var onlyFree = intent.getBooleanExtra(MainActivity.INTENT_EXTRA_RESULT_6, false)

        val ti_city = findViewById<TextInputEditText>(R.id.ti_city)
        val sb_range = findViewById<SeekBar>(R.id.sb_range)
        val s_free = findViewById<Switch>(R.id.s_free)
        val bt_update = findViewById<Button>(R.id.bt_update)

        if (cityName != ""){
            ti_city.setText(cityName)
        }

        sb_range.setMax(50)
        sb_range.setProgress(range)

        if (onlyFree == false){
            s_free.toggle()
        }

        bt_update.setOnClickListener(View.OnClickListener {
            val intentResult = Intent()

            cityName = ti_city.text.toString()
            range = sb_range.progress
            onlyFree = !s_free.isChecked

            intentResult.putExtra(Params.INTENT_EXTRA_RESULT_1, cityName)
            intentResult.putExtra(Params.INTENT_EXTRA_RESULT_2, range.toDouble() * 1000)
            intentResult.putExtra(Params.INTENT_EXTRA_RESULT_3, onlyFree)
            setResult(RESULT_OK, intentResult)
            finish()
        })
    }
}