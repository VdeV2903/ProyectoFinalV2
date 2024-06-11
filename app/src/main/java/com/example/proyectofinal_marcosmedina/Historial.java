package com.example.proyectofinal_marcosmedina;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;



public class Historial extends ListActivity {
    ListView listView;
    private int[] id;
    private String[] fechas;
    private String[] inicios;
    private String[] finales;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);


    }



}