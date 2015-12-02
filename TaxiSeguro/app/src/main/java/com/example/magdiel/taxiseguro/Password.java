package com.example.magdiel.taxiseguro;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Password extends AppCompatActivity implements View.OnClickListener {

    private Button b_confirmar;
    private Resources resources;
    private EditText contraseniaVieja;
    private EditText contraseniaNueva;
    private EditText contraseniaRe;
    private static final String PATTERN_PASSWORD = "^(?=.*[a-z]).{6,15}$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
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
        contraseniaVieja = (EditText)findViewById(R.id.passwordV);
        contraseniaVieja.addTextChangedListener(textWatcher);
        contraseniaNueva = (EditText)findViewById(R.id.passwordN);
        contraseniaNueva.addTextChangedListener(textWatcher);
        contraseniaRe = (EditText)findViewById(R.id.passwordR);
        contraseniaRe.addTextChangedListener(textWatcher);

        b_confirmar = (Button)findViewById(R.id.confirmarC);
        b_confirmar.setOnClickListener(this);
    }
    private void callClearErrors(Editable s){
        if(!s.toString().isEmpty()){
            clearErrorFields(contraseniaVieja);
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
        String passwordV = contraseniaVieja.getText().toString().trim();
        String passwordN = contraseniaNueva.getText().toString().trim();
        String passwordR = contraseniaRe.getText().toString().trim();
        return (validaContraseniaV(passwordV)&& validaContraseniaN(passwordN) && validaContraseñaR(passwordR,passwordN));
    }
    /**
     * Método que valida que el nombre tenga el formato deseado usando una expresion regular
     */
    private boolean validaContraseniaV(String pass) {
        Pattern pattern = Pattern.compile(PATTERN_PASSWORD);
        Matcher matcher = pattern.matcher(pass);
        boolean bandera = matcher.matches();
        if (!bandera) {
            contraseniaVieja.requestFocus();
            contraseniaVieja.setError("Contraseña inválida");
            return false;
        }
        return true;
    }
    private boolean validaContraseniaN(String pass){
        Pattern pattern = Pattern.compile(PATTERN_PASSWORD);
        Matcher matcher = pattern.matcher(pass);
        boolean bandera = matcher.matches();
        if (!bandera){
            contraseniaNueva.requestFocus();
            contraseniaNueva.setError("Contraseña no valida");
            return false;
        }
        return true;
    }
    private boolean validaContraseñaR(String pass, String pass1){
        Pattern pattern = Pattern.compile(PATTERN_PASSWORD);
        Matcher matcher = pattern.matcher(pass);
        boolean bandera = matcher.matches();
        if (!bandera || !(pass.equals(pass1))){
            contraseniaRe.requestFocus();
            contraseniaRe.setError("Contraseña invalida");
            return false;
        }
        return true;
    }

}
