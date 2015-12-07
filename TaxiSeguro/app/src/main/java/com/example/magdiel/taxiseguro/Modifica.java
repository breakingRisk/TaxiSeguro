package com.example.magdiel.taxiseguro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
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

public class Modifica extends Activity implements View.OnClickListener{

    private Button b_confirmar;
    private Resources resources;
    private EditText nombre;
    private EditText correo;
    private EditText telefono;
    private String nombreS;
    private String correoC;
    private String telefonoS;
    private static final String PATTERN_EMAIL = "^([\\da-z_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$";
    //Expresión regular para validar correo
    private static final String PATTERN_NOMBRE = "[A-Za-záéíóúñü]{2,}([\\s][A-Za-záéíóúñü]{2,})*$";

    private ProgressDialog pDialog;
    private Httppostaux post;

    private static final String IP = "192.168.1.71";
    private static final String IP_SERVER = "http://" + IP + "/taxiSeguro/update.php";

    /*  Clase interna AsyncTask
    * Usaremos esta clase para mostrar el diálogo de progreso mientras enviamos y obtenemos los datos
    * podría hacerse sin esta parte, pero si el tiempo de respuesta es demasiado, lo que podría ocurrir
    * si al conexión es lenta o el servidor tarda en responder la aplicación será inestable.
    * Además observariamos el mensaje de que la app no responde.
    */

    class asynclogin extends AsyncTask<String, String, String> {
        String email, nombre, phone;

        protected void onPreExecute( ) {
            //para el diálogo de progreso
            pDialog = new ProgressDialog(Modifica.this);
            pDialog.setMessage("Estamos actualizando tus datos");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        //Valida el email que no exista ebn la base de datos
        private boolean loginstatus( String email, String nombre, String tel ) {
            int logstatus=-1;

            /* Creamos un ArrayList del tipo nombre valor para agregar los datos recibidos por los parámetros anteriores
             * y mediante POST a nuestro sistema para realizar la validación */
            ArrayList<NameValuePair> postparameters2send = new ArrayList<NameValuePair>();

            postparameters2send.add(new BasicNameValuePair("email", email));
            postparameters2send.add(new BasicNameValuePair("name", nombre));
            postparameters2send.add(new BasicNameValuePair("phone", tel));

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

                    //Se muestra por log lo que obtuvimos
                    Log.e("loginstatus", "logstatus= " + logstatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //validamos el valor obtenido
                if(logstatus == 0) {  //[{"regstatus":"0"}]
                    Log.e("loginstatus", "actualización fallida");
                    return false;
                } else {             //[{"regstatus":"1"}]
                    Log.e("loginstatus", "actualización exitosa");
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
            nombre = params[1];
            phone = params[2];

            //Enviamos, recibimos y analizamos los datos en segundo plano.
            if(loginstatus(email, nombre, phone)) {
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
                Intent registrado = new Intent(Modifica.this, Perfil.class);
                registrado.putExtra("user", correoC);
                startActivity(registrado);
            } else {
                err_login();
            }
        }
    }

    private void err_login(){
        Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibr.vibrate(200);
        Toast toast1 = Toast.makeText(getApplicationContext(), "Error en la actualización",
                Toast.LENGTH_SHORT);
        toast1.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica);
        initViews();
        post = new Httppostaux();

        String recibidoNombre = (String)getIntent().getExtras().getString("nombre");
        String recibidoPhone = (String)getIntent().getExtras().getString("phone");
        String recibidoMail = (String)getIntent().getExtras().getString("mail");

        //Asignamos texto a los editables
        nombre.setHint(recibidoNombre);
        correo.setHint(recibidoMail);
        telefono.setHint(recibidoPhone);
    }
    private void initViews(){
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
        nombre = (EditText)findViewById(R.id.campoNombre);
        nombre.addTextChangedListener(textWatcher);
        correo = (EditText)findViewById(R.id.campoCorreo);
        correo.addTextChangedListener(textWatcher);
        telefono = (EditText) findViewById(R.id.campoTelefono);
        telefono.addTextChangedListener(textWatcher);

        b_confirmar = (Button) findViewById(R.id.confirmar);
        b_confirmar.setOnClickListener(this);
    }
    private void callClearErrors(Editable s){
        if(!s.toString().isEmpty()) {
            clearErrorFields(nombre);
        }
    }
    private void clearErrorFields(EditText... editTexts){
        for(EditText editText : editTexts){
            editText.setError(null);
        }
    }
    public void onClick(View view) {
        Intent abrir2;
        if(validateFields()) {
            /*abrir2 = new Intent(this, Perfil.class);
            startActivity(abrir2);*/
            new asynclogin().execute(correoC, nombreS, telefonoS);
        }
    }
    /**
     * Método que pasa los campos nombre, correo y telefono a String
     */
    private boolean validateFields(){
        nombreS = nombre.getText().toString().trim();
        correoC = correo.getText().toString().trim();
        telefonoS = telefono.getText().toString().trim();
        return (!isEmptyFields(nombreS, correoC, telefonoS)
                && hasSizeValid (nombreS, correoC, telefonoS)
                && validateEmail(correoC)
                && validaNombre(nombreS));
    }
    /**
     * Método que verifica que los campos no sean vacios
     */
    private boolean isEmptyFields(String nombreSs, String correoSs, String telefonoSs){
        if(TextUtils.isEmpty(nombreSs)){
            nombre.requestFocus();
            nombre.setError("Campo vacio");
            return true;
        } else if (TextUtils.isEmpty(correoSs)){
            correo.requestFocus();
            correo.setError("Campo vacio");
            return true;
        } else if (TextUtils.isEmpty(telefonoSs)){
            telefono.requestFocus();
            telefono.setError("Campo vacio");
            return true;
        }
        return false;
    }
    /**
     * Método que verifica que los campos tengan un tamaño minimo de 3, 8, y 10 respectivamente
     */
    private boolean hasSizeValid(String nombreSs, String correoSs, String telefonoSs){
        if(!(nombreSs.length() > 3)){
            nombre.requestFocus();
            nombre.setError("Nombre muy corto");
            return false;
        } else if (!(correoSs.length() >= 8)){
            correo.requestFocus();
            correo.setError("Correo muy corto");
            return false;
        } else if (!(telefonoSs.length() > 9)){
            telefono.requestFocus();
            telefono.setError("Teléfono inválido");
            return false;
        }
        return true;
    }
    /**
     * Método que valida que el email tenga el formato deseado usando una expresión regular
     */
    private boolean validateEmail(String email){
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);
        Matcher matcher = pattern.matcher(email);
        boolean bandera = matcher.matches();
        if (!bandera){
            correo.requestFocus();
            correo.setError("Formato invalido");
            return false;
        }
        return true;
    }
    /**
     * Método que valida que el nombre tenga el formato deseado usando una expresion regular
     */
    private boolean validaNombre(String name) {
        Pattern pattern = Pattern.compile(PATTERN_NOMBRE);
        Matcher matcher = pattern.matcher(name);
        boolean bandera = matcher.matches();
        if (!bandera){
            nombre.requestFocus();
            nombre.setError("Nombre Invalido");
            return false;
        }
        return true;
    }
}
