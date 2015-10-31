package com.example.magdiel.taxiseguro;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

public class pedirTaxi extends Activity {

    ImageButton b1;
    ImageButton b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedir_taxi);

        //Capturamos el id de los botones sobre los que queremos realizar la acción de mostrar un elemento emergente
        b1 = (ImageButton)findViewById( R.id.locate );
        b2 = (ImageButton)findViewById( R.id.taxi );

        b1.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        popupMenu();
                    }});
    }

    private void popupMenu() {
        //Crea instancia a PopupMenu
        PopupMenu popup = new PopupMenu(this, b1);
        popup.getMenuInflater().inflate(R.menu.datos_origen, popup.getMenu());

        //registra los eventos click para cada item del menu
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_one) {
                    Toast.makeText(pedirTaxi.this,
                            "Ejecutar : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                } else if (item.getItemId() == R.id.action_two) {
                    Toast.makeText(pedirTaxi.this,
                            "Ejecutar : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                } else if (item.getItemId() == R.id.action_three) {
                    Toast.makeText(pedirTaxi.this,
                            "Ejecutar : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        popup.show();
    }
}
