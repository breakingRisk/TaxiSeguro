package com.example.magdiel.taxiseguro;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Registro extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        Button reg = (Button) findViewById(R.id.registro);
        reg.setOnClickListener(this);

        TextView inc = (TextView) findViewById(R.id.linklog);
        inc.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registro:
                Intent inicioa = new Intent(this, Registro2.class);
                startActivity(inicioa);
                break;
            case R.id.linklog:
                Intent inicio = new Intent(this, Login.class);
                startActivity(inicio);
                break;
        }
    }
}


