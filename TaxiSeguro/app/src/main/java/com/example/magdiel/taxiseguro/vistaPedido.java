package com.example.magdiel.taxiseguro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class vistaPedido extends AppCompatActivity {

    String recibidoCorreo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_pedido);

        setToolbar();   // Añadimos la Toolbar

        recibidoCorreo = (String)getIntent().getExtras().getString("user");
    }

    /*
    * Método para crear la barra superior
    */
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
        Intent optionToolbar;

        switch (id) {
            case R.id.to_perfil:  //Nos vamos al perfil
                optionToolbar = new Intent(this, Perfil.class);
                optionToolbar.putExtra("user", recibidoCorreo);
                startActivity(optionToolbar);
                return true;

            case R.id.c_session:  //Cerramos sesión
                Toast.makeText(this, "Cerrando tu sesión...", Toast.LENGTH_SHORT).show();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
