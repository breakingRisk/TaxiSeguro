package com.example.magdiel.taxiseguro;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Registro extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        Button reg = (Button) findViewById(R.id.registro);
        reg.setOnClickListener(this);
    }

    public void onClick(View view){
                Intent inicioa = new Intent(this, Registro2.class);
                startActivity(inicioa);

    }
}
