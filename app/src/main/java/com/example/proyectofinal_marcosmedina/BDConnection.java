package com.example.proyectofinal_marcosmedina;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BDConnection extends SQLiteOpenHelper {

    public BDConnection(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE usuarios(" +
                "ID_Usuario INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UID TEXT NOT NULL, " +
                "Nombre TEXT NOT NULL, " +
                "Usuario TEXT NOT NULL, " +
                "Correo TEXT, " +
                "Contraseña TEXT NOT NULL)");

        db.execSQL("CREATE TABLE Movimientos (" +
                "ID_Movimiento INTEGER PRIMARY KEY AUTOINCREMENT," +
                "UID TEXT NOT NULL," +
                "Fecha TEXT NOT NULL," +
                "KilometrajeInicio TEXT NOT NULL," +
                "KilometrajeFinal TEXT NOT NULL," +
                "KilometrosRecorridos TEXT NOT NULL," +
                "DestinoSalida TEXT NOT NULL," +
                "DestinoLlegada TEXT NOT NULL," +
                "HoraSalida TEXT NOT NULL," +
                "HoraLlegada TEXT NOT NULL," +
                "DetalleActividad TEXT NOT NULL)");

        db.execSQL("CREATE TABLE MovimientosUsuarios(" +
                "ID_MovimientoUsuario INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ID_Usuario INTEGER NOT NULL, " +
                "ID_Movimiento INTEGER NOT NULL, " +
                "FOREIGN KEY(ID_Usuario) REFERENCES usuarios(ID_Usuario)," +
                "FOREIGN KEY(ID_Movimiento) REFERENCES Movimientos(ID_Movimiento))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
