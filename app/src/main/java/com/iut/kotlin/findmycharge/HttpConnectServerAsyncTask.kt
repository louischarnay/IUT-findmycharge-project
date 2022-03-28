package com.iut.kotlin.findmycharge

import android.os.AsyncTask
import android.util.Log
import android.webkit.WebView
import android.widget.TextView
import com.google.android.gms.maps.model.LatLng
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class HttpConnectServerAsyncTask : AsyncTask<Any, Void, ArrayList<Bornes>>() {
    override fun doInBackground(vararg params: Any?): ArrayList<Bornes> {
        //Récupération des paramètres
        var latitude = params[0]
        var longitude = params[1]
        var rayon = params[2]
        var ville = params[3].toString().toLowerCase()
        var onlyFree = "0"
        if (params[4] == true){
            onlyFree = "1"
        }

        Log.d("AsyncTask", onlyFree)
        //Définition des variables
        var host = ""
        if (ville != ""){
            host = "https://api-adresse.data.gouv.fr/search/?q=$ville"
            var urlPos: URL? = URL(host)
            var jsonPos: String = ""
            var urlConnectionPos = urlPos?.openConnection() as HttpURLConnection

            //Récupération du json
            if (urlConnectionPos.responseCode == HttpURLConnection.HTTP_OK){
                var input = BufferedReader(InputStreamReader(urlConnectionPos.inputStream))
                jsonPos = input.readLine()
                Log.d("AsyncTask", "flux = $jsonPos")
                input.close()
            }
            urlConnectionPos.disconnect()

            var tmp = JSONObject(jsonPos).getJSONArray("features").get(0)
            var pos = JSONObject(tmp.toString()).getJSONObject("geometry").getJSONArray("coordinates")
            latitude = pos.getDouble(1)
            longitude = pos.getDouble(0)
        }

        host = "https://public.opendatasoft.com/api/records/1.0/search/?dataset=mobilityref-france-irve-202&q=&geofilter.distance=$latitude%2C+$longitude%2C+$rayon&refine.gratuit=$onlyFree"
        Log.d("AsyncTask", host)

        var result = ArrayList<Bornes>()
        var url: URL? = URL(host)
        var json: String = ""
        var urlConnection = url?.openConnection() as HttpURLConnection

        //Récupération du json
        if (urlConnection.responseCode == HttpURLConnection.HTTP_OK){
            var input = BufferedReader(InputStreamReader(urlConnection.inputStream))
            json = input.readLine()
            input.close()
        }
        urlConnection.disconnect()

        //Récupération du nombre max de bornes
        var nbBornes = JSONObject(json.toString()).getInt("nhits")
        Log.d("AsyncTask", "nbBornes =" + nbBornes.toString())

        //Redéfinition de l'url
        host = "https://public.opendatasoft.com/api/records/1.0/search/?dataset=mobilityref-france-irve-202&q=&rows=$nbBornes&geofilter.distance=$latitude%2C+$longitude%2C+$rayon&refine.gratuit=$onlyFree"

        url = URL(host)
        urlConnection = url?.openConnection() as HttpURLConnection

        //Récupération du json
        if (urlConnection.responseCode == HttpURLConnection.HTTP_OK){
            var input = BufferedReader(InputStreamReader(urlConnection.inputStream))
            json = input.readLine()
            Log.d("AsyncTask", "flux = $json")
            input.close()
        }
        urlConnection.disconnect()

        var bornes = JSONObject(json.toString()).getJSONArray("records")
        var tel = "Pas de téléphone"

        //Construction de la liste
        for (i in 0 until nbBornes){
            var borne = JSONObject(bornes[i].toString()).getJSONObject("fields")
            if (i == 0){
                var pos = borne.getJSONArray("coordonneesxy")

                try {
                    if (borne.getString("telephone_operateur") != null){
                        tel = borne.getString("telephone_operateur")
                    }
                }
                catch (e: Exception){}

                result.add(Bornes(borne.getString("id_pdc_itinerance"), borne.getString("nom_station"), pos.getString(0), pos.getString(1), borne.getString("adresse_station"), borne.getString("code_insee_commune"), borne.getString("reg_name"), borne.getString("com_arm_name"), borne.getString("nbre_pdc"), tel, borne.getString("implantation_station")))
            }
            else if (borne.getString("nom_station") != result[result.size - 1].name){
                var pos = borne.getJSONArray("coordonneesxy")

                try {
                    if (borne.getString("telephone_operateur") != null){
                        tel = borne.getString("telephone_operateur")
                    }
                }
                catch (e: Exception){}

                result.add(Bornes(borne.getString("id_pdc_itinerance"), borne.getString("nom_station"), pos.getString(0), pos.getString(1), borne.getString("adresse_station"), borne.getString("code_insee_commune"), borne.getString("reg_name"), borne.getString("com_arm_name"), borne.getString("nbre_pdc"), tel, borne.getString("implantation_station")))
            }
        }
        return result
    }
}