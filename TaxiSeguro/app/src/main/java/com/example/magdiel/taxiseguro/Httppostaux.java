package com.example.magdiel.taxiseguro;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Clase auxiliar para el envío de peticiones a nuestro sistema y manejo de respuestas
 */

public class Httppostaux {

    InputStream is = null;
    String result = "";


    public JSONArray getserverdata(ArrayList<NameValuePair> parameters, String urlwebserver ) {

        //conecta via http y envía un post.
        httppostconnect(parameters, urlwebserver);

        if(is != null) {
            getpostresponse();

            return getjsonarray();
        } else {
            return null;
        }
    }

    //peticion HTTP
    private void httppostconnect(ArrayList<NameValuePair> parametros, String urlwebserver) {

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(urlwebserver);
            httppost.setEntity(new UrlEncodedFormEntity(parametros));
            //ejecuto petición enviando datos por POST
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }

    }

    public void getpostresponse() {

        //Convierte respuesta a string
        try {

            BufferedReader reader = new BufferedReader( new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null ) {
                sb.append(line + " ");
            }
            is.close();

            result = sb.toString();
            Log.e("getpostresponse", " result= "+ sb.toString());
        } catch (Exception e) {
            Log.e("log_tag", "Error converting result " + e.toString());
        }
    }

    public JSONArray getjsonarray() {

        //parseamos el dato json
        try {
            JSONArray jArray = new JSONArray(result);

            return jArray;
        } catch (JSONException e) {
            Log.e("log_tag", "Error parseando datos " + e.toString());
            return null;

        }
    }

}
