package com.example.proyectofinal_marcosmedina;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    Context context;
    ArrayList<String> id,fecha, lugarInicio,lugarFinal;

    CustomAdapter(Context context,
                  ArrayList<String> id,
                  ArrayList<String> fecha,
                  ArrayList<String> lugarInicio,
                  ArrayList<String> lugarFinal){

        this.context = context;
        this.id = id;
        this.fecha = fecha;
        this.lugarInicio = lugarInicio;
        this.lugarFinal = lugarFinal;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Log.d("CustomAdapter", "onBindViewHolder position: " + position);
        holder.number.setText(id.get(position));
        holder.titulo.setText(lugarInicio.get(position) + " -> " + lugarFinal.get(position));
        holder.subtitlo.setText(fecha.get(position));
    }


    @Override
    public int getItemCount() {
        return id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView number,titulo,subtitlo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.txtNumberHis);
            titulo = itemView.findViewById(R.id.txtRutaHis);
            subtitlo = itemView.findViewById(R.id.txtFechaHis);
        }
    }
}
