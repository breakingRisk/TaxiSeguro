package com.example.magdiel.taxiseguro;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registro extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog pDialog;
    private Httppostaux post;

    private static final String IP = "192.168.1.71";
    private static final String IP_SERVER = "http://" + IP + "/taxiSeguro/register.php";

    private Button cuenta;
    private Resources resources;
    private EditText userr;
    private EditText phonee;
    private EditText emaill;
    private String phona;
    private String usera;
    private String emaila;
    private static final String PATTERN_USER = "^[A-Za-záéíóúñü]{2,}([\\s][A-Za-záéíóúñü]{2,})*$";
    //Expresión regular para validar el nombre del usuario

    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    //Expresión regular para validar el email

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
            pDialog = new ProgressDialog(Registro.this);
            pDialog.setMessage("Validando usuario");
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
            SystemClock.sleep(950);

            //Si lo que obtuvimos no es null
            if (jdata != null && jdata.length() > 0) {
                try {
                    JSONObject json_data = jdata.getJSONObject(0);
                    //Accedemos al valor
                    logstatus = json_data.getInt("logstatus");
                    //Se muestra por log lo que obtuvimos
                    Log.e("loginstatus", "logstatus= " + logstatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //validamos el valor obtenido
                if(logstatus == 0) {  //[{"regstatus":"0"}]
                    Log.e("loginstatus", "existente");
                    return false;
                } else {             //[{"regstatus":"1"}]
                    Log.e("loginstatus", "no existe");
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

            if(result.equals("OK")) {
                Intent registrado = new Intent(Registro.this, Registro2.class);

                //Pasamos parámetros a la siguiente activity
                registrado.putExtra("userr", usera);
                registrado.putExtra("phonee", phona);
                registrado.putExtra("emaill", emaila);
                startActivity(registrado);
            } else {
                err_login();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        post = new Httppostaux();

        Button reg = (Button) findViewById(R.id.registro);
        reg.setOnClickListener(this);

        TextView inc = (TextView) findViewById(R.id.linklog);
        inc.setOnClickListener(this);

        initViews();
    }

    private void initViews() {
        resources = getResources();

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                callClearErrors(s);
            }
        };

        userr = (EditText) findViewById(R.id.user);
        userr.addTextChangedListener(textWatcher);
        phonee = (EditText) findViewById(R.id.phone);
        phonee.addTextChangedListener(textWatcher);
        emaill = (EditText) findViewById(R.id.email);
        emaill.addTextChangedListener(textWatcher);

        Button cuenta = (Button) findViewById(R.id.registro);
        cuenta.setOnClickListener(this);
    }

    private void callClearErrors(Editable s) {
        if (!s.toString().isEmpty()) {
            clearErrorFields(userr);
        }
    }

    private void clearErrorFields(EditText... editTexts) {
        for (EditText editText : editTexts) {
            editText.setError(null);
        }
    }

    /*
     * Método que pasa los campos a String
     */
    private boolean validateFields() {
        usera = userr.getText().toString().trim();
        phona = phonee.getText().toString().trim();
        emaila = emaill.getText().toString().trim();

        return (/*!isEmptyFields(passa, passb) && */hasSizeValid(usera, phona, emaila)&& validatename(usera) && validatemail(emaila));
    }

    private boolean hasSizeValid(String usera1, String phona1,String emaila1 ) {

        if (!(usera1.length() > 2)) {
            userr.requestFocus();
            userr.setError("Formato no valido");
            return false;
        } else if (!(phona1.length() == 10)) {
            phonee.requestFocus();
            phonee.setError("Tu número debe ser de 10 digitos");
                return false;
        }else if (!(emaila1.length() > 9)) {
            emaill.requestFocus();
            emaill.setError("Correo no valido");
            return false;
        }
        return true;
    }

    /*
    * Método que valida que el usuario tenga el formato deseado usando una expresión regular.
    */
    private boolean validatename(String usera1) {
        Pattern pattern = Pattern.compile(PATTERN_USER);
        Matcher matcher1 = pattern.matcher(usera1);
        boolean bandera1 = matcher1.matches();
        if (!bandera1) {
            userr.requestFocus();
            userr.setError("Caracteres no invalidos");
            return false;
        }
        return true;
    }
/*
* Método que valida que el email tenga el formato deseado usando una expresión regular
*/

    private boolean validatemail(String emaila1) {
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);
        Matcher matcher1 = pattern.matcher(emaila1);
        boolean bandera1 = matcher1.matches();
        if (!bandera1) {
            emaill.requestFocus();
            emaill.setError("Correo no valido");
            return false;
        }
        return true;
    }

    //Vibra y muestra un mensaje de que son datos inválidos
    private void err_login(){
        Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibr.vibrate(200);
        Toast toast1 = Toast.makeText(getApplicationContext(), "Usuario ya registrado",
                Toast.LENGTH_SHORT);
        toast1.show();
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registro:
                if ( validateFields() ) {
                    /*Intent inicioa = new Intent(this, Registro2.class);
                    startActivity(inicioa);
                    break;*/
                    new asynclogin().execute(emaila);
                }break;
            case R.id.linklog:
                Intent inicio = new Intent(this, Login.class);
                startActivity(inicio);
                break;
        }
    }


}



