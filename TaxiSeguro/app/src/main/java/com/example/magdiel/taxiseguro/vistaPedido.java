package com.example.magdiel.taxiseguro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class vistaPedido extends AppCompatActivity implements GoogleMap.OnMapClickListener {

    String recibidoCorreo;
    GoogleMap mapa;
    MapView mapView;
    private final LatLng UPV = new LatLng(39.481106, -0.340987);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_pedido);

        /*mapView = (MapView)findViewById(R.id.mimap);
        mapView.onCreate(savedInstanceState);

        googleMap = mapView.getMap();
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(true);*/

        mapView = (MapView)findViewById(R.id.mimap);
        mapView.onCreate(savedInstanceState);

        mapa = mapView.getMap();
        mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(UPV, 18));
        mapa.setMyLocationEnabled(true);
        mapa.getUiSettings().setZoomControlsEnabled(false);
        mapa.getUiSettings().setCompassEnabled(true);
        mapa.addMarker(new MarkerOptions()
                .position(UPV)
                .title("UPV")
                .snippet("Universidad Politécnica de Valencia")
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.ic_person_pin_circle_black_24dp))
                .anchor(0.5f, 0.5f));

        setToolbar();   // Añadimos la Toolbar

        recibidoCorreo = (String)getIntent().getExtras().getString("user");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
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

    public void moveCamera(View view) {
        mapa.moveCamera(CameraUpdateFactory.newLatLng(UPV));
    }

    public void animateCamera(View view) {
        if (mapa.getMyLocation() != null)
            mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng( mapa.getMyLocation().getLatitude(), mapa.getMyLocation().getLongitude()), 18));
    }

    public void addMarker(View view) {
        mapa.addMarker(new MarkerOptions().position(
                new LatLng(mapa.getCameraPosition().target.latitude,
                        mapa.getCameraPosition().target.longitude)));
    }

    @Override
    public void onMapClick(LatLng puntoPulsado) {
        mapa.addMarker(new MarkerOptions().position(puntoPulsado).
                icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
    }

}
