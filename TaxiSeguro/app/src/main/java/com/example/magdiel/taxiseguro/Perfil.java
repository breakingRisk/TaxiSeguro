package com.example.magdiel.taxiseguro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Perfil extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        Button b_modifica = (Button) findViewById(R.id.modificar);
        b_modifica.setOnClickListener(this);
    }

    public void onClick(View view) {
        Intent abrir1 = new Intent(this, Modifica.class);
        startActivity(abrir1);
    }
}
