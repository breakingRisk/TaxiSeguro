package com.example.magdiel.taxiseguro;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Modifica extends Activity implements View.OnClickListener{

    private Button b_confirmar;
    private Resources resources;
    private EditText nombre;
    private EditText correo;
    private EditText telefono;
    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";   //Expresión regular para validar correo
    private static final String PATTERN_NOMBRE = "[A-Za-záéíóúñü]{2,}([\\s][A-Za-záéíóúñü]{2,})*$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica);
        initViews();
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
        Intent abrir2 = new Intent(this, Perfil.class);
        startActivity(abrir2);
    }
    /**
     * Método que pasa los campos nombre, correo y telefono a String
     */
    private boolean validateFields(){
        String nombreS = nombre.getText().toString().trim();
        String correoC = correo.getText().toString().trim();
        String telefonoS = telefono.getText().toString().trim();
        return (!isEmptyFields(nombreS, correoC, telefonoS) && hasSizeValid (nombreS, correoC, telefonoS)
                && validateEmail(correoC) && validaNombre(nombreS));
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
