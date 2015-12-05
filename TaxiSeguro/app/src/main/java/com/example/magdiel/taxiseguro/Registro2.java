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
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registro2 extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog pDialog;
    private Httppostaux post;

    private static final String IP = "192.168.0.25";
    private static final String IP_SERVER = "http://" + IP + "/taxiSeguro/register2.php";

    private Resources resources;
    private EditText pass1;
    private EditText pass2;
    private static final String PATTERN_PASSWORD = "^([A-Za-z0-9]{6,20})$"; //Expresión regular para validar password

    private String nombre;
    private String phone;
    private String email;
    private String passa;
    private String passb;

    /*  Clase interna AsyncTask
    * Usaremos esta clase para mostrar el diálogo de progreso mientras enviamos y obtenemos los datos
    * podría hacerse sin esta parte, pero si el tiempo de respuesta es demasiado, lo que podría ocurrir
    * si al conexión es lenta o el servidor tarda en responder la aplicación será inestable.
    * Además observariamos el mensaje de que la app no responde.
    */

    class asyncregister extends AsyncTask<String, String, String> {
        private String nameC, phoneC, mailC, passC;

        protected void onPreExecute( ) {
            //para el diálogo de progreso
            pDialog = new ProgressDialog(Registro2.this);
            pDialog.setMessage("Aguanta, te estamos registrando");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        /*
        * Método para  validar el estado del registro
        * @param nombre del usuario a registrar
        * @param phone del usuario a registrar en la base de datos
        * @param email del usuario a registrar en la base de datos
        * @param password del usuario a registrar en la base de datos
        * */
        private boolean registerstatus(String nombre, String phone, String email, String pass ) {
            int logstatus=-1;

            /* Creamos un ArrayList del tipo nombre valor para agregar los datos recibidos por los parámetros anteriores
             * y mediante POST a nuestro sistema para realizar la validación */
            ArrayList<NameValuePair> postparameters2send = new ArrayList<NameValuePair>();

            postparameters2send.add(new BasicNameValuePair("email", email));
            postparameters2send.add(new BasicNameValuePair("nombre", nombre));
            postparameters2send.add(new BasicNameValuePair("phone", phone));
            postparameters2send.add(new BasicNameValuePair("pass", pass));

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
                    Log.e("loginstatus", "no se ha agregado");
                    return false;
                } else {             //[{"regstatus":"1"}]
                    Log.e("loginstatus", "se ha agregado el usuario");
                    return true;
                }
            } else {        //Json obtenido invalido, verificar la parte web
                Log.e("JSON   ","ERROR");
                return false;
            }
        }

        protected String doInBackground(String... params) {
            //Obtenemos los respectivos parámetros
            nameC = params[0];
            phoneC = params[1];
            mailC = params[2];
            passC = params[3];

            //Enviamos, recibimos y analizamos los datos en segundo plano.
            if(registerstatus(nameC, phoneC, mailC, passC)) {
                return "OK";  //Registro con éxito
            } else {
                return "ERR"; //Registro sin éxito
            }
        }

        /*Cuando termina doInBackground() según lo que halla ocurrido pasamos a la siguiente activity
         * mostramos error
         */
        protected void onPostExecute(String result) {

            pDialog.dismiss(); //Ocultamos el diálogo de progreso
            Log.e("onPostExecute=", "" + result);

            if(result.equals("OK")) {
                Intent registrado = new Intent(Registro2.this, pedirTaxi.class);
                registrado.putExtra("user", email);
                startActivity(registrado);
            } else {
                err_login();
            }
        }
    }

    //Vibra y muestra un mensaje de que son datos inválidos
    private void err_login(){
        Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibr.vibrate(200);
        Toast toast1 = Toast.makeText(getApplicationContext(), "Fallo en el registro, intentalo más tarde",
                Toast.LENGTH_SHORT);
        toast1.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro2);
        post = new Httppostaux();

        nombre = (String)getIntent().getExtras().getString("userr");  //Recibimos texto de Registro.java
        phone = (String)getIntent().getExtras().getString("phonee");  //Recibimos texto de Registro.java
        email = (String)getIntent().getExtras().getString("emaill");  //Recibimos texto de Registro.java

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

        pass1 = (EditText) findViewById(R.id.pass);
        pass1.addTextChangedListener(textWatcher);
        pass2 = (EditText) findViewById(R.id.pass2);
        pass2.addTextChangedListener(textWatcher);

        Button cuenta = (Button) findViewById(R.id.acuenta);
        cuenta.setOnClickListener(this);
    }

    private void callClearErrors(Editable s) {
        if (!s.toString().isEmpty()) {
            clearErrorFields(pass1);
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
        passa = pass1.getText().toString().trim();
        passb = pass2.getText().toString().trim();

        return (/*!isEmptyFields(passa, passb) && */hasSizeValid(passa, passb) && samepass(passa, passb) && validatepassword(passa, passb));
    }

    /*
        * Método para validar que los campos tengan un tamanio mayor a 8
        * @param passa1 el campo del passsword1 a validar
        * @param passb2 el campo del password2 a validar
        * returns false si alguno de los campos tiene un tamaño menor a 8, en otro caso devuelve true
    */

    private boolean hasSizeValid(String passa1, String passb2) {

        if (!(passa1.length() > 5)) {
            pass1.requestFocus();
            pass1.setError("Debe contener al menos 6 caracteres");
            return false;
        } else if (!(passb2.length() > 5)) {
            pass2.requestFocus();
            pass2.setError("Debe contener al menos 6 caracteres");
            return false;
        }
        return true;
    }

    /*
    * Método que sirve para validar si los campos
    * @param passa1
    * @param passb2  coinciden
    */
    private boolean samepass(String passa1, String passb2) {

        if (!passa1.equals(passb2)) {
            pass2.requestFocus();
            pass2.setError("Tu contraseña no coincide, vuelve a intentar");
            return false;
        }
        return true;
    }

    /*
    *Sirve para validar que los campos de password tengan el formato requerido.
    * @param passa1
    * @param passb2
    */
    private boolean validatepassword(String passa1, String passb2) {
        Pattern pattern = Pattern.compile(PATTERN_PASSWORD);
        Matcher matcher1 = pattern.matcher(passa1);
        boolean bandera1 = matcher1.matches();
        Matcher matcher2 = pattern.matcher(passb2);
        boolean bandera2 = matcher2.matches();
        if (!bandera1) {
            pass1.requestFocus();
            pass1.setError("Tu constraseña no debe contener caracteres invalidos");
            return false;
        } else if (!bandera2) {
            pass2.requestFocus();
            pass2.setError("Tu constraseña no debe contener caracteres invalidos");
            return false;
        }
        return true;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.acuenta:
                if ( validateFields() ){
                    /*Intent inicioa = new Intent(this, pedirTaxi.class);
                    startActivity(inicioa);
                    break;*/
                    new asyncregister().execute(nombre, phone, email, passa);
                }
        }
    }
}

