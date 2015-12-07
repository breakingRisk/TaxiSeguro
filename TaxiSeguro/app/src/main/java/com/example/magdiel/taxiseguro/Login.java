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
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Login extends AppCompatActivity implements View.OnClickListener {

    private Button inicio;
    private EditText user;
    private EditText pass;
    private Resources resources;
    private ListView listado;
    private String usera;
    private String passa;
    private ProgressDialog pDialog;
    private Httppostaux post;

    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";   //Expresión regular para validar correo
    private static final String IP = "169.254.151.69";
    private static final String IP_SERVER = "http://" + IP + "/taxiSeguro/acces.php";


    /*  Clase interna AsyncTask
    * Usaremos esta clase para mostrar el diálogo de progreso mientras enviamos y obtenemos los datos
    * podría hacerse sin esta parte, pero si el tiempo de respuesta es demasiado, lo que podría ocurrir
    * si al conexión es lenta o el servidor tarda en responder la aplicación será inestable.
    * Además observariamos el mensaje de que la app no responde.
    */

    class asynclogin extends AsyncTask<String, String, String> {
        String usuario, contr;

        protected void onPreExecute( ) {
            //para el diálogo de progreso
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Autenticando...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        //Valida el estado del logueo, sólamentenecesitamos el usario y contraseña como parámetros
        private boolean loginstatus(String username, String password ) {
            int logstatus=-1;

            /* Creamos un ArrayList del tipo nombre valor para agregar los datos recibidos por los parámetros anteriores
             * y mediante POST a nuestro sistema para realizar la validación */
            ArrayList<NameValuePair> postparameters2send = new ArrayList<NameValuePair>();

            postparameters2send.add(new BasicNameValuePair("usuario",username));
            postparameters2send.add(new BasicNameValuePair("password",password));

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
                    //Se muestra por log loq ue obtuvimos
                    Log.e("loginstatus", "logstatus= " + logstatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //validamos el valor obtenido
                if(logstatus == 0) {  //[{"Logstatus":"0"}]
                    Log.e("loginstatus", "invalido");
                    return false;
                } else {             //[{"Logstatus":"1"}]
                    Log.e("loginstatus", "valido");
                    return true;
                }
            } else {        //Json obtenido invalido, verificar la parte web
                Log.e("JSON   ","ERROR");
                return false;
            }
        }

        protected String doInBackground(String... params) {
            //Obtenemos user y pass
            usuario = params[0];
            contr = params[1];

            //Enviamos, recibimos y analizamos los datos en segundo plano.
            if(loginstatus(usuario, contr)) {
                return "OK";  //Login válido
            } else {
                return "ERR"; //Login inválido
            }
        }

        /*Cuando termina doInBackground() según lo que halla ocurrido pasamos a la siguiente activity
         * mostramos error
         */
        protected void onPostExecute(String result) {

            pDialog.dismiss(); //Ocultamos el diálogo de progreso
            Log.e("onPostExecute=", "" + result);

            if(result.equals("OK")) {
                Intent logueado = new Intent(Login.this, pedirTaxi.class);
                logueado.putExtra("user", usera);
                startActivity(logueado);
            } else {
                err_login();
            }
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        post = new Httppostaux();

        initViews();    //lanzamos método para validaciones
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

        user = (EditText)findViewById(R.id.editText2);
        user.addTextChangedListener(textWatcher);
        pass = (EditText)findViewById(R.id.editText3);
        pass.addTextChangedListener(textWatcher);


        Button inicio = (Button) findViewById(R.id.button5);
        inicio.setOnClickListener(this);
    }

    private void callClearErrors(Editable s) {
        if (!s.toString().isEmpty()) {
            clearErrorFields(user);
        }
    }

    private void clearErrorFields(EditText... editTexts) {
        for (EditText editText : editTexts) {
            editText.setError(null);
        }
    }

    /*
    * Método que pasa los campos a String
    * */
    private boolean validateFields() {
        usera = user.getText().toString().trim();
        passa = pass.getText().toString().trim();

        return (!isEmptyFields(usera, passa) && hasSizeValid(usera, passa) /*&& validateEmail(usera)*/);
    }

    /*
    * Método para validar que los campos introducidos por el usuario no sean vacíos
    * @param userr el campo a validar que no sea vacío
    * œparam passs el campo a validar que no sea vacío
    * returns true si alguno de los campos está vacío, en otro caso devuelve falso
    * */
    private boolean isEmptyFields(String userr, String passs) {

        if (TextUtils.isEmpty(userr)) {
            user.requestFocus();
            user.setError("Campo vacío");
            return true;
        } else if (TextUtils.isEmpty(passs)) {
            pass.requestFocus();
            pass.setError("Campo vacío");
            return true;
        }
        return false;
    }

    /*
    * Método para validar que los campos tengan un tamanio mayor a 3
    * @param userr el campo del usuario a validar
    * @param passs el campo del password a validar
    * returns false si alguno de los campos tiene un tamaño menor a 3, en otro caso devuelve true
    * */
    private boolean hasSizeValid(String userr, String passs) {

        if (!(userr.length() > 3)) {
            user.requestFocus();
            user.setError("Número de caracteres insuficientes");
            return false;
        } else if (!(passs.length() > 3)) {
            pass.requestFocus();
            pass.setError("Número de caracteres insuficientes");
            return false;
        }
        return true;
    }

    /*
    * Valida que el campo del usuario tenga formato de email. Ej. "example.email@domain.com" usando una expresión regular
    * @email campo a validar
    * returns true si el campoi tiene el formato adecuado, en otro caso devuelve false
    * */
    private boolean validateEmail(String email) {

        // Compiles the given regular expression into a pattern.
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);

        // Match the given input against this pattern
        Matcher matcher = pattern.matcher(email);
        boolean bandera = matcher.matches();
        if (!bandera) {
            user.requestFocus();
            user.setError("Formato no válido");
            return false;
        }
        return true;
    }

    //Vibra y muestra un mensaje de que son datos inválidos
    private void err_login(){
        Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibr.vibrate(200);
        Toast toast1 = Toast.makeText(getApplicationContext(), "Nombre de usuario o contraseña incorrectos",
                Toast.LENGTH_SHORT);
        toast1.show();
    }

    @Override
    public void onClick(View v) {
        Intent pedirTaxi;

        switch (v.getId()) {
            case R.id.button5:
                if (validateFields()) {
                    //Si pasa las validaciones de sintaxis ejecutamos las validaciones de usuario en la BDatos
                    new asynclogin().execute(usera, passa);

                    /*Toast.makeText(this, "Iniciando sesión...", Toast.LENGTH_LONG).show();
                    pedirTaxi = new Intent(this, pedirTaxi.class);
                    startActivity(pedirTaxi);*/
                }
                break;
        }

    }
}
