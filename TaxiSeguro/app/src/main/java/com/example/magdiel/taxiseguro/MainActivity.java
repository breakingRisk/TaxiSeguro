package com.example.magdiel.taxiseguro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button pedir = (Button)findViewById( R.id.pedir );

        pedir.setOnClickListener(this);
    }

    public void onClick(View view) {
        Intent abrir = new Intent(this, pedirTaxi.class);
        startActivity(abrir);
    }
}
