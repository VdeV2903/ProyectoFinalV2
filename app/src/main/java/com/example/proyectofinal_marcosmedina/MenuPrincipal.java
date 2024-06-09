package com.example.proyectofinal_marcosmedina;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MenuPrincipal extends AppCompatActivity {
    TextView nombre;
    Button inicioDetener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_principal);
        nombre = (TextView) findViewById(R.id.txtvNombre);
        inicioDetener = (Button) findViewById(R.id.btniniciardetener);

        SharedPreferences sharedPreferences = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("NombreTemp", "");
        nombre.setText(name);

        if (isConnectedToInternet()) {
            Toast.makeText(getApplicationContext(), "Conexión a Internet establecida", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "No hay conexión a Internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void cerrarSesion(View v) {
        SharedPreferences sharedPreferences = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UID", "");
        editor.apply();

        Intent cerrar = new Intent(this, MainActivity.class);
        startActivity(cerrar);

    }
    private boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }
    boolean estadoBoton = true;
    public void cambiar(View v){
        if(!estadoBoton){
            inicioDetener.setBackgroundColor(Color.rgb(0,33,96));
            inicioDetener.setText("INICIAR VIAJE");
            estadoBoton = true;
        }else{
            inicioDetener.setBackgroundColor(Color.RED);
            inicioDetener.setText("TERMINAR VIAJE");
            estadoBoton = false;
        }
    }
}