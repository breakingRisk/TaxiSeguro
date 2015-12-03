package com.example.magdiel.taxiseguro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Perfil extends Activity implements View.OnClickListener {

    Button b_modificaP;
    Button b_modificaC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        b_modificaP = (Button) findViewById(R.id.modificarP);
        b_modificaP.setOnClickListener(this);
        b_modificaC = (Button) findViewById(R.id.modificarC);
        b_modificaC.setOnClickListener(this);
    }

    public void onClick(View view) {
        Intent abrir1;
        switch (view.getId()) {
            case R.id.modificarP:
                abrir1 = new Intent(this, Modifica.class);
                startActivity(abrir1);
                break;
            case R.id.modificarC:
                abrir1 = new Intent(this, Password.class);
                startActivity(abrir1);
                break;
        }
    }
}
