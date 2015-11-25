package com.example.magdiel.taxiseguro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button pedir = (Button)findViewById( R.id.pedir );
        Button tulogin = (Button)findViewById( R.id.login );
        pedir.setOnClickListener(this);
        tulogin.setOnClickListener(this);
}

    public void onClick(View view) {
        Intent abrir = new Intent(this, pedirTaxi.class);
        startActivity(abrir);
        //Intent to_login = new Intent(this, Login.class);
        //startActivity(to_login);
    }
}
