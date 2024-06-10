package com.example.proyectofinal_marcosmedina;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity {
    EditText usuario, contrasenia;
    Switch sesion;
    String userExt, passExt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuario = (EditText) findViewById(R.id.txtUser);
        contrasenia = (EditText) findViewById(R.id.txtPassword);
        sesion = (Switch) findViewById(R.id.swSesion);

    }
    int i=0;
    public void login(View v){
        try{
            userExt = usuario.getText().toString();
            passExt = contrasenia.getText().toString();
            i=0;
            if(userExt.isEmpty()){
                usuario.setError("Ingrese el usuario!");
                i=1;
            }
            if(passExt.isEmpty()){
                contrasenia.setError("Ingrese su contraseña!");
                i=1;
            }
            if(i==0){
                BDConnection bd = new BDConnection(this,"bitacora",null,1);
                SQLiteDatabase baseBitacora = bd.getWritableDatabase();
                String loginQuery = "SELECT * FROM usuarios WHERE Usuario = '"+userExt+"' AND Contraseña = '"+passExt+"'";
                Cursor datos = baseBitacora.rawQuery(loginQuery,null);

                if(datos.moveToFirst()){
                    loginFinal();
                }else{
                    Toast.makeText(this,"Credenciales incorrectas", Toast.LENGTH_LONG).show();
                }
            }
        }catch(SQLException ex){
            Toast.makeText(this,ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    public void register(View v) {
        Intent regis = new Intent(this, Register.class);
        startActivityForResult(regis,1);
    }
    String uidQuered,nombreQuered;
    public void loginFinal(){
        try{
            BDConnection bd2 = new BDConnection(this,"bitacora",null,1);
            SQLiteDatabase baseBitacora2 = bd2.getWritableDatabase();
            String loginQuery2 = "SELECT UID,Nombre FROM usuarios WHERE Usuario = '"+userExt+"' AND Contraseña = '"+passExt+"'";
            Cursor datosLoad = baseBitacora2.rawQuery(loginQuery2,null);
            if(sesion.isChecked()){
                if (datosLoad.moveToFirst()) {
                    uidQuered = datosLoad.getString(0);

                    nombreQuered = datosLoad.getString(1);

                    SharedPreferences sharedPreferences = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("UID", uidQuered);
                    editor.putString("Nombre", nombreQuered);
                    editor.putString("UIDtemp", uidQuered);
                    editor.putString("NombreTemp", nombreQuered);
                    editor.apply();
                }
            }else{
                if (datosLoad.moveToFirst()) {
                    uidQuered = datosLoad.getString(0);
                    nombreQuered = datosLoad.getString(1);
                    SharedPreferences sharedPreferences = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("UIDtemp", uidQuered);
                    editor.putString("NombreTemp", nombreQuered);
                    editor.apply();
                }
            }

            usuario.setText("");
            contrasenia.setText("");

            Intent login = new Intent(this, MenuPrincipal.class);
            Toast.makeText(this,"Sesión iniciada", Toast.LENGTH_SHORT).show();
            startActivity(login);
            finish();
        }catch(SQLException ex){
            Toast.makeText(this,ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try{
            SharedPreferences sharedPreferences = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
            String pref = sharedPreferences.getString("UID", "");
            if(!pref.equals("")){
                this.startActivity(new Intent(this,MenuPrincipal.class));
                this.finish();
                Toast.makeText(this,"Sesión iniciada", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception ex){
            Toast.makeText(this,ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}