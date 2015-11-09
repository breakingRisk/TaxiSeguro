package com.example.magdiel.taxiseguro;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
/**
 *
 */
public class httpHandler {
    /*
     *Metodo para realizar una conexión al servidor para poder hacer una consulta en la base de datos
     * param @posturl dirección url al servidor al que nos vamos a conectar
     * param @user dato del usuario para conectar con la base de datos
     * param @pass del usuario para verificar su cuenta
     */
    public String post (String posturl, String user, String pass){

        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(posturl); //Recibe la url del servidor al que nos vamos a conectar

            //Se crea un arreglo para guardar los datos del usuario y poder mandarlo por medio de una petición POST
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("data", user));
            httppost.setEntity(new UrlEncodedFormEntity(params));

            //Recibimos una respuesta por parte del servidor que debe ser manejada para poder devolver un String
            HttpResponse resp = httpclient.execute(httppost);
            HttpEntity entity = resp.getEntity();
            String text =  EntityUtils.toString(entity);
            return text;
        } catch (Exception e){
            return "no";
        }

    }
}
