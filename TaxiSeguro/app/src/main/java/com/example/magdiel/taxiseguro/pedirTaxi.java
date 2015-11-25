package com.example.magdiel.taxiseguro;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class pedirTaxi extends Activity implements View.OnClickListener {

    ImageButton b1;
    ImageButton b2;
    private ImageButton pop;
    private ImageButton pop1;
    private TextView registro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedir_taxi);

        pop = (ImageButton)findViewById(R.id.locatePop);
        pop1 = (ImageButton)findViewById(R.id.DestinoPop);
        registro = (TextView)findViewById(R.id.ubicarme);
        pop.setOnClickListener(this);
        pop1.setOnClickListener(this);
        registro.setOnClickListener(this);

    }

    public void onClick(View view) {

        View mostrarOr = findViewById(R.id.locating);
        View mostrarDes = findViewById(R.id.destino);

        switch (view.getId()) {
            case R.id.locatePop:
                if(mostrarOr.getVisibility()== View.VISIBLE ){
                    mostrarOr.setVisibility(View.GONE);
                } else {
                    mostrarOr.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.DestinoPop:
                if(mostrarDes.getVisibility()== View.VISIBLE ){
                    mostrarDes.setVisibility(View.GONE);
                } else {
                    mostrarDes.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.ubicarme:
                Toast.makeText(this, "Espere...", Toast.LENGTH_SHORT).show();
                break;

        }



    }
}
