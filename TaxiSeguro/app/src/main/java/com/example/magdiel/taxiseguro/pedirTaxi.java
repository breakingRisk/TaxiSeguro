package com.example.magdiel.taxiseguro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class pedirTaxi extends ActionBarActivity implements View.OnClickListener {

    ImageButton b1;
    ImageButton b2;
    private ImageButton pop;
    private ImageButton pop1;
    private TextView registro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedir_taxi);

        setToolbar();// Añadir la Toolbar

        pop = (ImageButton)findViewById(R.id.locatePop);
        pop1 = (ImageButton)findViewById(R.id.DestinoPop);
        registro = (TextView)findViewById(R.id.ubicarme);
        pop.setOnClickListener(this);
        pop1.setOnClickListener(this);
        registro.setOnClickListener(this);
    }

    private void setToolbar() {
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Intent i = new Intent(this, Home.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


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
                Toast.makeText(this, "Por el momento no está implementado, espere una actualización", Toast.LENGTH_SHORT).show();
                break;

        }
    }
}