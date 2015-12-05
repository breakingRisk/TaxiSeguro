package com.example.magdiel.taxiseguro;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class pedirTaxi extends ActionBarActivity implements View.OnClickListener {


    private ImageButton b1;
    private ImageButton b2;
    private ImageButton pop;
    private ImageButton pop1;
    private Button origenboton;
    private Button destinoboton;
    private TextView registro;
    private EditText calleOr;
    private EditText colOr;
    private EditText cpOr;
    private EditText calleDes;
    private EditText colDes;
    private EditText cpDes;
    private Resources resources;
    private TextView origenTable;
    private TextView destinoTable;
    private static final String PATTERN_ADDRESS = "^[A-Za-z0-9áéíóúñü#.-/]{2,}([\\s][A-Za-z0-9áéíóúñü#.-/]{2,})*$";
    private static final String PATTERN_POSTAL = "^([0-9]{5})$";
    private String email;
    private TextView editable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedir_taxi);

        setToolbar();   // Añadimos la Toolbar
        initViews();    //lanzamos método para validaciones

        pop = (ImageButton)findViewById(R.id.locatePop);
        pop1 = (ImageButton)findViewById(R.id.DestinoPop);
        registro = (TextView)findViewById(R.id.ubicarme);
        origenTable = (TextView)findViewById(R.id.origentexto);
        destinoTable = (TextView)findViewById(R.id.destinotexto);
        origenboton = (Button)findViewById(R.id.botonOrigen);
        destinoboton = (Button)findViewById(R.id.botonDestino);
        pop.setOnClickListener(this);
        pop1.setOnClickListener(this);
        origenTable.setOnClickListener(this);
        destinoTable.setOnClickListener(this);
        registro.setOnClickListener(this);
        Button verifOrigen = (Button)findViewById(R.id.botonOrigen);
        verifOrigen.setOnClickListener(this);
        email = (String)getIntent().getExtras().getString("user");  //Recibimos texto de Login.java

    }

    /*
    * Método para crear la barra superior
    */
    private void setToolbar() {
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent optionToolbar;

        switch (id) {
            case R.id.to_perfil:  //Nos vamos al perfil
                optionToolbar = new Intent(this, Perfil.class);
                optionToolbar.putExtra("user", email);
                startActivity(optionToolbar);
                return true;

            case R.id.c_session:  //Cerramos sesión
                Toast.makeText(this, "Cerrando tu sesión...", Toast.LENGTH_SHORT).show();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


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

        calleOr = (EditText)findViewById(R.id.txtCalleO);
        calleOr.addTextChangedListener(textWatcher);
        colOr = (EditText)findViewById(R.id.txtColO);
        colOr.addTextChangedListener(textWatcher);
        cpOr = (EditText)findViewById(R.id.txtCpO);
        cpOr.addTextChangedListener(textWatcher);
        calleDes = (EditText)findViewById(R.id.txtCalleD);
        calleDes.addTextChangedListener(textWatcher);
        colDes = (EditText)findViewById(R.id.txtColD);
        colDes.addTextChangedListener(textWatcher);
        cpDes = (EditText)findViewById(R.id.txtCpD);
        cpDes.addTextChangedListener(textWatcher);

        Button btnOrigen = (Button) findViewById(R.id.botonOrigen);
        Button btnDestino = (Button) findViewById(R.id.botonDestino);
        btnOrigen.setOnClickListener(this);
        btnDestino.setOnClickListener(this);

    }

    private void callClearErrors(Editable s) {
        if (!s.toString().isEmpty()) {
            clearErrorFields(calleOr);
        }
    }

    private boolean validateFieldsOr() {
        String calleO = calleOr.getText().toString().trim();
        String colO = colOr.getText().toString().trim();
        String cpO = cpOr.getText().toString().trim();

        return (!isEmptyFieldsOr(calleO, colO, cpO) && hasSizeValidOr(calleO, colO, cpO)
                && validateCalleOr(calleO) && validateColOr(colO) && validateCpOr(cpO));
    }

    private boolean validateFieldsDes() {
        String calleD = calleDes.getText().toString().trim();
        String colD = colDes.getText().toString().trim();
        String cpD = cpDes.getText().toString().trim();

        return (!isEmptyFieldsDes(calleD, colD, cpD) && hasSizeValidDes(calleD, colD, cpD)
        && validateCalleDes(calleD) && validateColDes(colD) && validateCpDes(cpD));
    }

    private boolean isEmptyFieldsOr(String calle, String col, String cp) {

        if (TextUtils.isEmpty(calle)) {
            calleOr.requestFocus(); //seta o foco para o campo user
            calleOr.setError(resources.getString(R.string.calleOb));
            return true;
        } else if (TextUtils.isEmpty(col)) {
            colOr.requestFocus(); //seta o foco para o campo password
            colOr.setError(resources.getString(R.string.colOb));
            return true;
        } else if (TextUtils.isEmpty(cp)) {
            cpOr.requestFocus(); //seta o foco para o campo password
            cpOr.setError(resources.getString(R.string.cpOb));
            return true;
        }
        return false;
    }

    private boolean isEmptyFieldsDes(String calle, String col, String cp) {

        if (TextUtils.isEmpty(calle)) {
            calleDes.requestFocus(); //seta o foco para o campo user
            calleDes.setError(resources.getString(R.string.calleOb));
            return true;
        } else if (TextUtils.isEmpty(col)) {
            colDes.requestFocus(); //seta o foco para o campo password
            colDes.setError(resources.getString(R.string.colOb));
            return true;
        } else if (TextUtils.isEmpty(cp)) {
            cpDes.requestFocus(); //seta o foco para o campo password
            cpDes.setError(resources.getString(R.string.cpOb));
            return true;
        }
        return false;
    }

    private boolean hasSizeValidOr(String calle, String col, String cp) {

        if (!(calle.length() > 3)) {
            calleOr.requestFocus();
            calleOr.setError(resources.getString(R.string.calleTamVal));
            return false;
        } else if (!(col.length() > 3)) {
            colOr.requestFocus();
            colOr.setError(resources.getString(R.string.colTamVal));
            return false;
        } else if (!(cp.length() > 3)) {
            cpOr.requestFocus();
            cpOr.setError(resources.getString(R.string.cpTamVal));
            return false;
        }

        return true;
    }

    private boolean hasSizeValidDes(String calle, String col, String cp) {

        if (!(calle.length() > 3)) {
            calleDes.requestFocus();
            calleDes.setError(resources.getString(R.string.calleTamVal));
            return false;
        } else if (!(col.length() > 3)) {
            colDes.requestFocus();
            colDes.setError(resources.getString(R.string.colTamVal));
            return false;
        } else if (!(cp.length() > 3)) {
            cpDes.requestFocus();
            cpDes.setError(resources.getString(R.string.cpTamVal));
            return false;
        }
        return true;
    }

    /*
    * Valida que el campo del usuario tenga formato de email. Ej. "example.email@domain.com" usando una expresión regular
    * @email campo a validar
    * returns true si el campoi tiene el formato adecuado, en otro caso devuelve false
    * */
    private boolean validateCalleOr(String calle) {

        // Compiles the given regular expression into a pattern.
        Pattern pattern = Pattern.compile(PATTERN_ADDRESS);

        // Match the given input against this pattern
        Matcher matcher = pattern.matcher(calle);
        boolean bandera = matcher.matches();
        if (!bandera) {
            calleOr.requestFocus();
            calleOr.setError("Formato no válido");
            return false;
        }
        return true;
    }

    /*
    * Valida que el campo del usuario tenga formato de email. Ej. "example.email@domain.com" usando una expresión regular
    * @email campo a validar
    * returns true si el campoi tiene el formato adecuado, en otro caso devuelve false
    * */
    private boolean validateColOr(String col) {

        // Compiles the given regular expression into a pattern.
        Pattern pattern = Pattern.compile(PATTERN_ADDRESS);

        // Match the given input against this pattern
        Matcher matcher = pattern.matcher(col);
        boolean bandera = matcher.matches();
        if (!bandera) {
            colOr.requestFocus();
            colOr.setError("Formato no válido");
            return false;
        }
        return true;
    }

    /*
    * Valida que el campo del usuario tenga formato de email. Ej. "example.email@domain.com" usando una expresión regular
    * @email campo a validar
    * returns true si el campoi tiene el formato adecuado, en otro caso devuelve false
    * */
    private boolean validateCpOr(String cp) {

        // Compiles the given regular expression into a pattern.
        Pattern pattern = Pattern.compile(PATTERN_POSTAL);

        // Match the given input against this pattern
        Matcher matcher = pattern.matcher(cp);
        boolean bandera = matcher.matches();
        if (!bandera) {
            cpOr.requestFocus();
            cpOr.setError("Debe contener 5 números");
            return false;
        }
        return true;
    }

    /*
    * Valida que el campo del usuario tenga formato de email. Ej. "example.email@domain.com" usando una expresión regular
    * @email campo a validar
    * returns true si el campoi tiene el formato adecuado, en otro caso devuelve false
    * */
    private boolean validateCalleDes(String calle) {

        // Compiles the given regular expression into a pattern.
        Pattern pattern = Pattern.compile(PATTERN_ADDRESS);

        // Match the given input against this pattern
        Matcher matcher = pattern.matcher(calle);
        boolean bandera = matcher.matches();
        if (!bandera) {
            calleDes.requestFocus();
            calleDes.setError("Formato no válido");
            return false;
        }
        return true;
    }

    /*
    * Valida que el campo del usuario tenga formato de email. Ej. "example.email@domain.com" usando una expresión regular
    * @email campo a validar
    * returns true si el campoi tiene el formato adecuado, en otro caso devuelve false
    * */
    private boolean validateColDes(String col) {

        // Compiles the given regular expression into a pattern.
        Pattern pattern = Pattern.compile(PATTERN_ADDRESS);

        // Match the given input against this pattern
        Matcher matcher = pattern.matcher(col);
        boolean bandera = matcher.matches();
        if (!bandera) {
            colDes.requestFocus();
            colDes.setError("Formato no válido");
            return false;
        }
        return true;
    }

    /*
    * Valida que el campo del usuario tenga formato de email. Ej. "example.email@domain.com" usando una expresión regular
    * @email campo a validar
    * returns true si el campoi tiene el formato adecuado, en otro caso devuelve false
    * */
    private boolean validateCpDes(String cp) {

        // Compiles the given regular expression into a pattern.
        Pattern pattern = Pattern.compile(PATTERN_POSTAL);

        // Match the given input against this pattern
        Matcher matcher = pattern.matcher(cp);
        boolean bandera = matcher.matches();
        if (!bandera) {
            cpDes.requestFocus();
            cpDes.setError("Debe contener 5 números");
            return false;
        }
        return true;
    }

    /**
     * Limpa os ícones e as mensagens de erro dos campos desejados
     *
     * @param editTexts lista de campos do tipo EditText
     */
    private void clearErrorFields(EditText... editTexts) {
        for (EditText editText : editTexts) {
            editText.setError(null);
        }
    }

    public void onClick(View view) {

        View mostrarOr = findViewById(R.id.locating);
        View mostrarDes = findViewById(R.id.destino);

        switch (view.getId()) {
            case R.id.locatePop:
                if(mostrarOr.getVisibility()== View.VISIBLE ){
                    mostrarOr.setVisibility(View.GONE);
                } else {
                    mostrarOr.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.DestinoPop:
                if(mostrarDes.getVisibility()== View.VISIBLE ){
                    mostrarDes.setVisibility(View.GONE);
                } else {
                    mostrarDes.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.botonOrigen:
                if(validateFieldsOr()) {
                    if (mostrarOr.getVisibility() == View.VISIBLE) {
                        mostrarOr.setVisibility(View.GONE);
                    } else {
                        mostrarOr.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.botonDestino:
                if(validateFieldsDes()) {
                    if (mostrarDes.getVisibility() == View.VISIBLE) {
                        mostrarDes.setVisibility(View.GONE);
                    } else {
                        mostrarDes.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.ubicarme:
                Toast.makeText(this, "Por el momento no está implementado, espere una actualización", Toast.LENGTH_SHORT).show();
                break;
            case R.id.origentexto:
                if(mostrarOr.getVisibility()== View.VISIBLE ){
                    mostrarOr.setVisibility(View.GONE);
                } else {
                    mostrarOr.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.destinotexto:
                if(mostrarDes.getVisibility()== View.VISIBLE ){
                    mostrarDes.setVisibility(View.GONE);
                } else {
                    mostrarDes.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

}
