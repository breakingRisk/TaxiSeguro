package com.example.magdiel.taxiseguro;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;


public class MainActivity extends Activity implements View.OnClickListener {

    ImageView  logota;
    Animation movimiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b_inicio = (Button) findViewById(R.id.inicio);
        b_inicio.setOnClickListener(this);


        Button b_registro = (Button) findViewById(R.id.button4);
        b_registro.setOnClickListener(this);
    }

        public void onClick(View view){
            switch (view.getId()) {
                case R.id.inicio:
                    Intent inicioa = new Intent(this, Login.class);
                    startActivity(inicioa);
                    break;
                case R.id.button4:
                    Intent iniciob = new Intent(this, Registro.class);
                    startActivity(iniciob);
                    break;
            }


        }


}


