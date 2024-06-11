package com.example.proyectofinal_marcosmedina;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Historial extends AppCompatActivity {
    RecyclerView recyclerView;
    private ArrayList<String> id, fecha, lugarInicio, lugarFinal;
    CustomAdapter customAdapter;
    int idExtr = -1; // Inicialización con valor por defecto inválido

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        recyclerView = findViewById(R.id.recyclerView);

        if (recyclerView == null) {
            Log.e("Historial", "RecyclerView is null. Check your layout XML file.");
            return;
        }

        id = new ArrayList<>();
        fecha = new ArrayList<>();
        lugarInicio = new ArrayList<>();
        lugarFinal = new ArrayList<>();

        obtenerIdUsuario();

        if (idExtr != -1) {
            mostrarDatos();
        } else {
            Toast.makeText(this, "Error al obtener el ID del usuario", Toast.LENGTH_LONG).show();
        }

        customAdapter = new CustomAdapter(Historial.this, id, fecha, lugarInicio, lugarFinal);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Notificar al adaptador que los datos han cambiado
        customAdapter.notifyDataSetChanged();
    }

    void mostrarDatos() {
        Cursor crs = traerDatos();
        if (crs != null) {
            if (crs.getCount() == 0) {
                Toast.makeText(this, "No existen viajes", Toast.LENGTH_LONG).show();
            } else {
                while (crs.moveToNext()) {
                    id.add(crs.getString(0));
                    fecha.add(crs.getString(1));
                    lugarInicio.add(crs.getString(2));
                    lugarFinal.add(crs.getString(3));
                }

            }
            crs.close(); // Cierra el cursor para evitar fugas de memoria
        }
    }

    Cursor traerDatos() {
        BDConnection bd = new BDConnection(this, "bitacora", null, 1);
        SQLiteDatabase baseBitacora2 = bd.getWritableDatabase();
        StringBuilder historialQuery = new StringBuilder();
        historialQuery.append("SELECT ID_Movimiento, Fecha, DestinoSalida, DestinoLlegada ")
                .append("FROM Movimientos WHERE ID_Usuario = ").append(idExtr);
        return baseBitacora2.rawQuery(historialQuery.toString(), null);
    }

    public void obtenerIdUsuario() {
        SharedPreferences sharedPreferences = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        String obtenerIDuser = sharedPreferences.getString("UIDtemp", "");

        BDConnection bd = new BDConnection(this, "bitacora", null, 1);
        SQLiteDatabase baseBitacora = bd.getWritableDatabase();
        String loginQuery = "SELECT * FROM usuarios WHERE UID='" + obtenerIDuser + "'";
        Cursor datos = baseBitacora.rawQuery(loginQuery, null);

        if (datos.moveToFirst()) {
            idExtr = datos.getInt(0);
        } else {
            idExtr = -1; // Valor por defecto en caso de no encontrar el usuario
        }
        datos.close(); // Cierra el cursor para evitar fugas de memoria
    }
}
