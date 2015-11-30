package com.example.magdiel.taxiseguro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private Button inicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inicio = (Button)findViewById(R.id.button5);
        inicio.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent pedirTaxi = new Intent(this, pedirTaxi.class);
        startActivity(pedirTaxi);
    }
}
