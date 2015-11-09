package com.example.magdiel.taxiseguro;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity implements View.OnClickListener{
    String IP="169.254.234.218";
    private final String SERVER = "http://"+ IP +"/taxiSeguro/controlador/controllerDB.php";
    String nombre;
    String contrasena;
    EditText user;
    EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Capturamos datos de entrada e identificamos de la acción del botón
        user=(EditText)findViewById(R.id.email);
        pass=(EditText)findViewById(R.id.password);
        Button log = (Button)findViewById( R.id.button_login );

        //Inicializamos la acción al dar click en el botón
        log.setOnClickListener(this);
    }

    public void onClick(View v) {
        //Obtenemos datos de pantalla
        nombre = user.getText().toString();
        contrasena = pass.getText().toString();

        httpHandler handler = new  httpHandler();
        String txt = handler.post(SERVER, nombre, contrasena);
        TextView t = (TextView) findViewById(R.id.error);
        TextView t1 = (TextView) findViewById(R.id.error2);
        TextView t2 = (TextView) findViewById(R.id.url_server);

        t.setText(txt);
        //t1.setText(contrasena);
        //t2.setText(SERVER);
    }
}
