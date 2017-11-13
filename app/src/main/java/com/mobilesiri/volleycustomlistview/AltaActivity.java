package com.mobilesiri.volleycustomlistview;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobilesiri.SQLite.DispositivoOpenHelper;

public class AltaActivity extends AppCompatActivity {

    private EditText altaNombre;
    private EditText altaDescripcion;
    private EditText altaIP;
    private Button botonCargar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta2);
        //   Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //   setSupportActionBar(toolbar);
        // context = MainActivity.this;


        Button botonCargar = (Button) findViewById(R.id.botonCargar);
        botonCargar.setOnClickListener(new  View.OnClickListener(){
            public void onClick(View v) {
                cargarSmartlux();
            }
        });


    }

    public void cargarSmartlux() {
        //comprobaciones

        altaNombre = (EditText) findViewById(R.id.altaNombre);
        altaDescripcion = (EditText) findViewById(R.id.altaDescripcion);
        altaIP = (EditText) findViewById(R.id.altaIP);

        alta(altaNombre.getText().toString(),altaDescripcion.getText().toString(),altaIP.getText().toString());
        Toast.makeText(AltaActivity.this, "Smartlux agregado correctamente", Toast.LENGTH_SHORT).show();

        MostrarMainActivity();

    }


    public void alta(String nombre, String descripcion, String ip) {
        DispositivoOpenHelper BaseDeDatos = new DispositivoOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase bd = BaseDeDatos.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("nombre", nombre);
        registro.put("detalles", descripcion);
        registro.put("ip", ip);
        registro.put("MAC", "algo");
        bd.insert("TDispositivos", null, registro);
        bd.close();
        //   Toast.makeText(MainActivity.this, "Smartux dado de alta correctaemtte", Toast.LENGTH_LONG).show();
    }
    public void MostrarMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

}