package com.example.magdiel.taxiseguro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Perfil extends Activity implements View.OnClickListener {

    private ProgressDialog pDialog;
    private Httppostaux post;
    private String recibed_name;
    private String recibed_phone;
    private String recibed_mail;
    private TextView nombre;
    private TextView phone;
    private TextView mail;

    private static final String IP = "169.254.151.69";
    private static final String IP_SERVER = "http://" + IP + "/taxiSeguro/muestrauser.php";

    Button b_modificaP;
    Button b_modificaC;

    /*  Clase interna AsyncTask
    * Usaremos esta clase para mostrar el diálogo de progreso mientras enviamos y obtenemos los datos
    * podría hacerse sin esta parte, pero si el tiempo de respuesta es demasiado, lo que podría ocurrir
    * si al conexión es lenta o el servidor tarda en responder la aplicación será inestable.
    * Además observariamos el mensaje de que la app no responde.
    */

    class asynclogin extends AsyncTask<String, String, String> {
        String email;

        protected void onPreExecute( ) {
            //para el diálogo de progreso
            pDialog = new ProgressDialog(Perfil.this);
            pDialog.setMessage("Espera un momento");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        //Valida el email que no exista ebn la base de datos
        private boolean loginstatus(String email ) {
            int logstatus=-1;

            /* Creamos un ArrayList del tipo nombre valor para agregar los datos recibidos por los parámetros anteriores
             * y mediante POST a nuestro sistema para realizar la validación */
            ArrayList<NameValuePair> postparameters2send = new ArrayList<NameValuePair>();

            postparameters2send.add(new BasicNameValuePair("email", email));

            //realizamos una petición y como respuesta obtenemos un arreglo JSON
            JSONArray jdata = post.getserverdata(postparameters2send,IP_SERVER);

            // Se puede quitar esta linea cuando esté montado en un servidor
            SystemClock.sleep(350);

            //Si lo que obtuvimos no es null
            if (jdata != null && jdata.length() > 0) {
                try {
                    JSONObject json_data = jdata.getJSONObject(0);
                    //Accedemos al valor
                    logstatus = json_data.getInt("logstatus");
                    recibed_name = json_data.getString("nombreA");
                    recibed_mail = json_data.getString("correoA");
                    recibed_phone = json_data.getString("telA");

                    System.out.println(recibed_mail + " " + recibed_name + " " + recibed_phone + "<------------------> " );

                    //Se muestra por log lo que obtuvimos
                    Log.e("loginstatus", "logstatus= " + logstatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //validamos el valor obtenido
                if(logstatus == 0) {  //[{"regstatus":"0"}]
                    Log.e("loginstatus", "no hay datos");
                    return false;
                } else {             //[{"regstatus":"1"}]
                    Log.e("loginstatus", "datos exitosos");
                    return true;
                }
            } else {        //Json obtenido invalido, verificar la parte web
                Log.e("JSON   ","ERROR");
                return false;
            }
        }

        protected String doInBackground(String... params) {
            //Obtenemos email
            email = params[0];

            //Enviamos, recibimos y analizamos los datos en segundo plano.
            if(loginstatus(email)) {
                return "OK";  //Validado con éxito
            } else {
                return "ERR"; //Validado sin éxito
            }
        }

        /*Cuando termina doInBackground() según lo que halla ocurrido pasamos a la siguiente activity
         * mostramos error
         */
        protected void onPostExecute(String result) {

            pDialog.dismiss(); //Ocultamos el diálogo de progreso
            Log.e("onPostExecute=", "" + result);

            //Asignamos el texto en las view's
            nombre.setText(recibed_name);
            phone.setText(recibed_phone);
            mail.setText(recibed_mail);

            /*if(result.equals("OK")) {
                Intent registrado = new Intent(Perfil.this, Registro2.class);

                startActivity(registrado);
            } else {
                err_login();
            }*/
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        post = new Httppostaux();

        b_modificaP = (Button) findViewById(R.id.modificarP);
        b_modificaP.setOnClickListener(this);
        b_modificaC = (Button) findViewById(R.id.modificarC);
        b_modificaC.setOnClickListener(this);
        //Identificamos los editables que cambiaremos en el view
        nombre = (TextView)findViewById(R.id.nombre);
        phone = (TextView)findViewById(R.id.telefono);
        mail = (TextView)findViewById(R.id.correo);
        //Recuperamos datos de pedirTaxi.java
        String recibidoCorreo = (String)getIntent().getExtras().getString("user");

        new asynclogin().execute(recibidoCorreo);

    }

    public void onClick(View view) {
        Intent abrir1;
        switch (view.getId()) {
            case R.id.modificarP:
                abrir1 = new Intent(this, Modifica.class);
                //Mandamos datos a la clase Modifica.java
                abrir1.putExtra("nombre", recibed_name);
                abrir1.putExtra("phone", recibed_phone);
                abrir1.putExtra("mail", recibed_mail);
                startActivity(abrir1);
                break;
            case R.id.modificarC:
                abrir1 = new Intent(this, Password.class);
                startActivity(abrir1);
                break;
        }
    }
}
