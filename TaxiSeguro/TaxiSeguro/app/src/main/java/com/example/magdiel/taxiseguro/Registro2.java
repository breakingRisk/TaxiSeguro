package com.example.magdiel.taxiseguro;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Registro2 extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro2);

        TextView inc = (TextView) findViewById(R.id.linklog);
        inc.setOnClickListener(this);
    }

    public void onClick(View view){
        Intent inicio = new Intent(this, Login.class);
        startActivity(inicio);

    }
}
