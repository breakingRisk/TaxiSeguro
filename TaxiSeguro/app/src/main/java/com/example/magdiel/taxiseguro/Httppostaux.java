package com.example.magdiel.taxiseguro;

import org.json.JSONArray;

import java.io.InputStream;
import java.util.ArrayList;

import cz.msebera.android.httpclient.NameValuePair;

/**
 * Clase auxiliar para el envío de peticiones a nuestro sistema y manejo de respuestas
 */
public class Httppostaux {
    InputStream is = null;
    String result = "";

    public JSONArray getserverdata(ArrayList<NameValuePair> parameters, String urlwebserver ) {

        //conecta via http y envía un post.
        //httppostconnect(parameters, urlwebserver);
        return null;
    }
}
