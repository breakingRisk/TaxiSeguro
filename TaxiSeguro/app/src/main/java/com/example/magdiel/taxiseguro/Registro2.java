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

public class Registro2 extends  AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro2);

        Button reg = (Button) findViewById(R.id.acuenta);
        reg.setOnClickListener(this);

        TextView inc = (TextView) findViewById(R.id.linklog);
        inc.setOnClickListener(this);

        initViews();
    }

    public void onClick(View view) {
        switch (view.getId()) {
           case R.id.acuenta:
               if ( validateFields()==true){
                Intent inicioa = new Intent(this, pedirTaxi.class);
                startActivity(inicioa);
                break;
               }else break;
            case R.id.linklog:
                Intent inicio = new Intent(this, Login.class);
                startActivity(inicio);
                break;
        }
    }

    private Button cuenta;
    private Resources resources;
    private EditText pass1;
    private EditText pass2;
    private static final String PATTERN_PASSWORD = "^([A-Za-z0-9]{6,20})$";
    //Expresión regular para validar password

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
        String passa = pass1.getText().toString().trim();
        String passb = pass2.getText().toString().trim();

        return (/*!isEmptyFields(passa, passb) && */hasSizeValid(passa, passb) && samepass(passa, passb) && validatepassword(passa, passb));
    }

    /*
        * Método para validar que los campos introducidos por el usuario no sean vacíos
        * @param userr el campo a validar que no sea vacío
        * œparam passs el campo a validar que no sea vacío
        * returns true si alguno de los campos está vacío, en otro caso devuelve falso
    */
    /*
    private boolean isEmptyFields(String passa1, String passb2) {

        if (TextUtils.isEmpty(passa1)) {
            pass1.requestFocus();
            pass1.setError("Campo vacío");
            return true;
        } else if (TextUtils.isEmpty(passb2)) {
            pass2.requestFocus();
            pass2.setError("Campo vacío");
            return true;
        }
        return false;
    }
*/
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
* @param passa1 y @param passb2  coinciden
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
    *
    *
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
}

