package com.example.magdiel.taxiseguro;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registro extends AppCompatActivity implements View.OnClickListener {

    private String ip = "192.168.13.159";
    private String url = "http://"  +  ip + "/Taxiseguro/acces.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        Button reg = (Button) findViewById(R.id.registro);
        reg.setOnClickListener(this);

        TextView inc = (TextView) findViewById(R.id.linklog);
        inc.setOnClickListener(this);

        initViews();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registro:
                if ( validateFields()==true) {
                    Intent inicioa = new Intent(this, Registro2.class);
                    startActivity(inicioa);
                    break;
                }break;
            case R.id.linklog:
                Intent inicio = new Intent(this, Login.class);
                startActivity(inicio);
                break;
        }
    }


        private Button cuenta;
        private Resources resources;
        private EditText userr;
        private EditText phonee;
        private EditText emaill;
        private static final String PATTERN_USER = "^[A-Za-záéíóúñü]{2,}([\\s][A-Za-záéíóúñü]{2,})*$";
        //Expresión regular para validar el nombre del usuario

         private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        //Expresión regular para validar el email

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
        String usera = userr.getText().toString().trim();
        String phona = phonee.getText().toString().trim();
        String emaila = emaill.getText().toString().trim();

        return (/*!isEmptyFields(passa, passb) && */hasSizeValid(usera, phona, emaila)&& validatename(usera) && validatemail(emaila));
    }

    /*
        * Método para validar que los campos introducidos por el usuario no sean vacíos
        * @param userr el campo a validar que no sea vacío
        * œparam phonee el campo a validar que no sea vacío
        * œparam emaill el campo a validar que no sea vacío
        * returns true si alguno de los campos está vacío, en otro caso devuelve falso
    */

    /*
    private boolean isEmptyFields(String usera1, String phona1, String emaila1) {

        if (TextUtils.isEmpty(usera1)) {
            userr.requestFocus();
            userr.setError("Campo vacío");
            return true;
        } else if (TextUtils.isEmpty(phona1)) {
            phonee.requestFocus();
            phonee.setError("Campo vacío");
            return true;
        }else if (TextUtils.isEmpty(emaila1)) {
            emaill.requestFocus();
            emaill.setError("Campo vacío");
            return true;
        }
        return false;
    }
   */


    /*
        * Método para validar que los campos tengan un tamanio correspondiente
        * @param userr el campo  usera1 a validar
        * @param phonee el campo phona1  a validar
        * @param emaill el campo emaila1 a validar
        * returns false si alguno de los campos tiene un tamaño menor al correspondiente, en otro caso devuelve true
    */

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




}



