package com.mobilesiri.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mobilesiri.model.Dispositivo;
import com.mobilesiri.volleycustomlistview.AppController;
import com.mobilesiri.volleycustomlistview.MainActivity;
import com.mobilesiri.volleycustomlistview.R;

import java.util.List;

public class DispositivoAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private Context context;
    private List<Dispositivo> dispositivos;



  //  ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public DispositivoAdapter(List<Dispositivo> dispositivos) {
        this.dispositivos = dispositivos;
    }
    @Override
    public int getCount() {
        return dispositivos.size();
    }

    @Override
    public Object getItem(int position) {
        return dispositivos.get (position);
    }

    @Override
    public long getItemId(int position) {
        return dispositivos.get(position).getId();
    }


    public void CambiarEstado(int position, int prendido) {
        Dispositivo dispositivo = (Dispositivo) getItem(position);
        String url;

        if (prendido ==1) {
            url = "http://" + dispositivo.getIp() + "/LED=OFF";
            dispositivo.setEstado(1);
        }
        else {
            url = "http://" + dispositivo.getIp() + "/LED=ON";
            dispositivo.setEstado(0);
        }


        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Toast.makeText(MainActivity.this, "Smartlux cargados correctamente", Toast.LENGTH_LONG).show();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //onConnectionFailed(volleyError.toString());

            }
        });
        AppController.getInstance().addToRequestQueue(request);

    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view;

        if(convertView == null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_dispositivo,parent,false);
        }else{
            view = convertView;
        }
        Dispositivo dispositivo = (Dispositivo) getItem(position);

        TextView idNombre = (TextView) view.findViewById(R.id.idNombre);
        idNombre.setText(dispositivo.getNombre());

        TextView idDetalles = (TextView) view.findViewById(R.id.idDetalles);
        idDetalles.setText(dispositivo.getDetalles());

        final ImageView idIcono = (ImageView) view.findViewById(R.id.idIcono);

        Switch idSwitch = (Switch) view.findViewById(R.id.idSwitch);

        if (dispositivo.getEstado()==2){ //desconectado
            idSwitch.setEnabled(false);
            idIcono.setImageResource(R.mipmap.ic_foco_desconectado);
        }else {

            if (dispositivo.getEstado() == 0) { //apagado
                idSwitch.setEnabled(true);
                idSwitch.setChecked(false);
                idIcono.setImageResource(R.mipmap.ic_foco_off);
            }
            else {
                if (dispositivo.getEstado() == 1) {  //prendido
                    idSwitch.setEnabled(true);
                    idSwitch.setChecked(true);
                    idIcono.setImageResource(R.mipmap.ic_foco_on);
                }
            }
        }

        idSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    idIcono.setImageResource(R.mipmap.ic_foco_on); // do something when check is selected
                    CambiarEstado(position,1);




                } else {
                    idIcono.setImageResource(R.mipmap.ic_foco_off);
                    CambiarEstado(position,0);
                }
            }
        });

        return view;

    }


}


