package com.example.proyectofinal_marcosmedina;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MenuPrincipal extends AppCompatActivity {
    TextView nombre, txtNoViajeR,txtInfoLugarSalidaS,txtInfoKilometrajeS, txtInfoHoraS,txtInfoDetalle,txtInfoFechaS;
    View info;
    Button inicioDetener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_principal);
        nombre = (TextView) findViewById(R.id.txtvNombre);
        inicioDetener = (Button) findViewById(R.id.btniniciardetener);
        txtNoViajeR = findViewById(R.id.txtNoViaje);
        info = findViewById(R.id.lyInfo);

        txtInfoKilometrajeS = findViewById(R.id.txtInfoKilometraje);
        txtInfoLugarSalidaS = findViewById(R.id.txtInfoSalida);
        txtInfoHoraS = findViewById(R.id.txtInfoHora);
        txtInfoFechaS = findViewById(R.id.txtInfoFecha);

        SharedPreferences sharedPreferences = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("NombreTemp", "");
        nombre.setText(name);

        consultarViajeIniciado();
    }
    String extractedUID;
    public void consultarViajeIniciado(){
        try{
            SharedPreferences sharedPreferences = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
            extractedUID = sharedPreferences.getString("UIDtemp", "");


            BDConnection bd = new BDConnection(this,"bitacora",null,1);
            SQLiteDatabase baseBitacora = bd.getWritableDatabase();
            String loginQuery = "SELECT * FROM TemporalesSesion WHERE UID='"+extractedUID+"'";
            Cursor datos = baseBitacora.rawQuery(loginQuery,null);

            if(datos.moveToFirst()){
                txtNoViajeR.setVisibility(View.GONE);
                estadoBoton = false;
                cambiarBoton();

                txtInfoKilometrajeS.setText("Kilometraje Inicial: "+datos.getString(2)+" KM");
                txtInfoLugarSalidaS.setText("Saliste de: "+datos.getString(3));
                txtInfoHoraS.setText("Hora: "+datos.getString(4));
                txtInfoFechaS.setText("Fecha: "+datos.getString(5));
            }else{
                info.setVisibility(View.GONE);
            }
        }catch (SQLException ex){
            Toast.makeText(this,ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private static final int REQUEST_CODE = 1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                consultarViajeIniciado();
            }
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
    public void cambiarBoton(){
        if(estadoBoton){
            inicioDetener.setBackgroundColor(Color.rgb(0,33,96));
            inicioDetener.setText("INICIAR VIAJE");
            estadoBoton = true;
        }else{
            inicioDetener.setBackgroundColor(Color.RED);
            inicioDetener.setText("TERMINAR VIAJE");
            estadoBoton = false;
        }
    }
    public void iniciarViaje(View v){
        if(estadoBoton){
            Intent ins = new Intent(this,IniciarViaje.class);
            startActivityForResult(ins,1);
        }else{
            Intent ins = new Intent(this,TerminarViaje.class);
            startActivityForResult(ins,1);
        }
    }
}