package com.example.magdiel.taxiseguro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Modifica extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica);

        Button b_modifica_contraseñia = (Button) findViewById(R.id.cambia_contrasenia);
        b_modifica_contraseñia.setOnClickListener(this);
    }
    public void onClick(View view) {
        Intent abrir2 = new Intent(this, Password.class);
        startActivity(abrir2);
    }
}
