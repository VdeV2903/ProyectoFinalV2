package com.example.proyectofinal_marcosmedina;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Base64;

public class CustomBaseAdapter  {
    Context context;
    int[] id;
    String[] fechas;
    String[] inicios;
    String[] finales;
    LayoutInflater inflater;
    public  CustomBaseAdapter(Context ctx, int[] id, String[] fechas, String[] inicios, String[] finales) {
        this.context = ctx;
        this.id = id;
        this.fechas = fechas;
        this.inicios = inicios;
        this.finales = finales;

        inflater = LayoutInflater.from(ctx);
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.activity_custom_list_view,null);

        TextView fechaTextView = (TextView) convertView.findViewById(R.id.txtFechaHis);
        TextView inicioTextView = (TextView) convertView.findViewById(R.id.txtRutaHis);

        inicioTextView.setText(inicios[position]+" -> "+finales[position]);
        fechaTextView.setText(fechas[position]);

        return convertView;
    }
}
