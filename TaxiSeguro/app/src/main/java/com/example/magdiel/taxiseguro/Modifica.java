package com.example.magdiel.taxiseguro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Modifica extends Activity implements View.OnClickListener{

    Button b_confirmar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica);

        b_confirmar = (Button) findViewById(R.id.confirmar);
        b_confirmar.setOnClickListener(this);
    }
    public void onClick(View view) {
        Intent abrir2 = new Intent(this, Perfil.class);
        startActivity(abrir2);
    }
}
