package com.mobilesiri.volleycustomlistview;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.mobilesiri.SQLite.DispositivoOpenHelper;
import com.mobilesiri.adapter.DispositivoAdapter;
import com.mobilesiri.model.Dispositivo;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {
    private TextView label;
    private static final String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private List<Dispositivo> dispositivoList  = new ArrayList<Dispositivo>();

    private ListView listView;
    private SwipeRefreshLayout sflLista;
    private Context context;
    private DispositivoAdapter adapter;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuAlta:
                Toast.makeText(context,"Nuevo Smarlux",Toast.LENGTH_SHORT).show();
                MostrarAltaActivity();      //reload
                return true;
            case R.id.menuRefrescar:
                Toast.makeText(context,"Refrescar",Toast.LENGTH_SHORT).show();
                cargarLista();      //reload
                return true;
            case R.id.menuCerrar:
                finish();          //cerrar app
                System.exit(0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.popupmenu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            //int pos= info.position;
            case R.id.disp_detalles:
                Toast.makeText(MainActivity.this,"Ver IP "+ dispositivoList.get(info.position).getIp(),Toast.LENGTH_SHORT).show();
                // Tareas a realizar
                return true;
            case R.id.disp_eliminar:
                // Tareas a realizar
                baja(dispositivoList.get(info.position).getId());
                Toast.makeText(MainActivity.this, "Smartux eliminado", Toast.LENGTH_LONG).show();
                cargarLista();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    private void cargarLista(){
         //todo caragar base de datos, y traer id, ip y nombre y descripcion de cada dispositivo.
         dispositivoList.add(new Dispositivo(1,"Cocina","Debajo de la mesada",0, "http://192.168.0.101", "mac1"));
         //dispositivoList.add(new Dispositivo(2,"Patio","Reflector patio trasero",0, "http://192.168.0.105", "mac1"));
       // alta ();
        cargar();
        verEstado();
    }


    public void cargar(){
        DispositivoOpenHelper BaseDeDatos = new DispositivoOpenHelper(this,"administracion",null,1);
        SQLiteDatabase bd = BaseDeDatos.getReadableDatabase();
        Cursor cursor = bd.rawQuery("SELECT * FROM TDispositivos",null);
        dispositivoList.clear();
        if(cursor.moveToFirst()){
            while (cursor.isAfterLast()==false) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
                String detalles = cursor.getString(cursor.getColumnIndex("detalles"));
                String ip = cursor.getString(cursor.getColumnIndex("ip"));
                String MAC = cursor.getString(cursor.getColumnIndex("MAC"));
                dispositivoList.add(new Dispositivo(id,nombre,detalles,2,ip,MAC));
                //    Toast.makeText(MainActivity.this, "Dispositivo cargado correctaemtte", Toast.LENGTH_LONG).show();
                cursor.moveToNext();
            }
        }
        bd.close();
    }

    public void baja(long id){
        DispositivoOpenHelper BaseDeDatos = new DispositivoOpenHelper(this,"administracion",null,1);
        SQLiteDatabase bd = BaseDeDatos.getWritableDatabase();
        bd.delete("TDispositivos","id="+id,null);
        bd.close();
    }


    private void FuncionParaEsteHilo()  {
        //Esta función es llamada des de dentro del Timer
        //Para no provocar errores ejecutamos el Accion
        //Dentro del mismo Hilo
        this.runOnUiThread(Accion);
    }

    private Runnable Accion = new Runnable() {
        public void run() {
            //Aquí iría lo que queramos que haga,
            //en este caso mostrar un mensaje.
           // Toast.makeText(getApplicationContext(), "Tiempo!", Toast.LENGTH_LONG).show();
            verEstado();
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;
        listView = (ListView) findViewById(R.id.lista_dispositivos);
        registerForContextMenu(listView);
        adapter = new DispositivoAdapter(dispositivoList);
        listView.setAdapter(adapter);
        sflLista = (SwipeRefreshLayout) findViewById(R.id.sflLista);
        cargarLista();
        sflLista.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                verEstado();
        sflLista.setRefreshing(false);
            }
        });

        Timer timer = new Timer();
        //Que actue cada 3000 milisegundos
        //Empezando des de el segundo 0
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //La función que queremos ejecutar
                FuncionParaEsteHilo();
            }
        }, 0, 5000);


    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    public void verEstado(){
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Cargando...");
        pDialog.show();

        for (final Dispositivo dispositivo: dispositivoList) {
            String url = "http://"+dispositivo.getIp();
            StringRequest request = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
               @Override
                public void onResponse(String response) {
                   // Toast.makeText(MainActivity.this, "Smartlux cargados correctamente", Toast.LENGTH_LONG).show();
                    Log.d(TAG, response);
                    hidePDialog();
                    int resultado = response.indexOf("low");
                    if (resultado != -1) {  //si existe
                        dispositivo.setEstado(1);
                    }
                   resultado = response.indexOf("high");
                   if (resultado != -1) {  //si existe
                       dispositivo.setEstado(0);
                   }

                    adapter.notifyDataSetChanged();
                   Log.d("RESPONSE",response);
                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    //onConnectionFailed(volleyError.toString());
                    VolleyLog.d(TAG, "Error: " + volleyError.getMessage());
                    hidePDialog();
            //      Toast.makeText(MainActivity.this, "Error. Smartlux "+dispositivo.getNombre()+" no encontrado", Toast.LENGTH_LONG).show();
                    dispositivo.setEstado(2);
                    adapter.notifyDataSetChanged();
                }
            });
            AppController.getInstance().addToRequestQueue(request);

        }
        hidePDialog();
    }


    public void MostrarAltaActivity() {
        Intent i = new Intent(this, AltaActivity.class);
        startActivity(i);
    }
}