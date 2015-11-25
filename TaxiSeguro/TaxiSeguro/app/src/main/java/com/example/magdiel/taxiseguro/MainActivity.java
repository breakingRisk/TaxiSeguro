package com.example.magdiel.taxiseguro;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;


public class MainActivity extends Activity {
ImageView  logota;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logota = (ImageView)findViewById(R.id.logota);
    }
    protected void animando(){
        Animation movimiento;
        movimiento = AnimationUtils.loadAnimation(this,R.animator.mover);
        movimiento.reset();
        logota.startAnimation(movimiento);

    }
}
