package com.example.proyectofinal_marcosmedina;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import java.util.Random;

public class Register extends AppCompatActivity {
    EditText nombre,usuario, correo, contrasenia;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nombre = (EditText) findViewById(R.id.txtNombre);
        usuario = (EditText) findViewById(R.id.txtUsuario);
        correo = (EditText) findViewById(R.id.txtCorreo);
        contrasenia = (EditText) findViewById(R.id.txtContrasenia);
    }
    String nombreExt, uidExt, usuarioExt, correoExt, contraseniaExt;
    int i = 0;
    public void registro(View v){
        try{
            nombreExt = nombre.getText().toString();
            usuarioExt = usuario.getText().toString();
            correoExt = correo.getText().toString();
            contraseniaExt = contrasenia.getText().toString();
            i = 0;

            if(nombreExt.isEmpty()){
                nombre.setError("Introduzca su nombre!");
                i=1;
            }
            if(usuarioExt.isEmpty()) {
                usuario.setError("Introduzca su usuario!");
                i = 1;
            }
            if(correoExt.isEmpty()) {
                correo.setError("Introduzca su correo!");
                i = 1;
            }
            if(contraseniaExt.isEmpty()){
                contrasenia.setError("Introduzca su contraseña!");
                i=1;
            }

            verificarUsuario();
            verificarCorreo();
            uidExt= generarUID(30);
            verificarUID();

            if(i==0){
                BDConnection bd = new BDConnection(this,"bitacora",null,1);
                SQLiteDatabase baseBitacora = bd.getWritableDatabase();
                String insertarUsuario = "INSERT INTO usuarios(UID,Nombre,Usuario,Correo,Contraseña) VALUES('"+uidExt+"','"+nombreExt+"','"+usuarioExt+"','"+correoExt+"','"+contraseniaExt+"')";
                baseBitacora.execSQL(insertarUsuario);

                Toast.makeText(this,"Usuario Creado con Éxito", Toast.LENGTH_LONG).show();

                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", "No data");
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        }catch(Exception ex){
            Toast.makeText(this,ex.toString(), Toast.LENGTH_LONG).show();

        }


    }
    public void verificarUsuario(){
        BDConnection bd = new BDConnection(this,"bitacora",null,1);
        SQLiteDatabase baseBitacora = bd.getWritableDatabase();
        String verificarUsuarioQuery = "SELECT * FROM usuarios WHERE Usuario = '"+usuarioExt+"'";
        Cursor datos = baseBitacora.rawQuery(verificarUsuarioQuery,null);

        if(datos.moveToFirst()){
            usuario.setError("El usuario ya existe!");
            i=1;
        }
    }
    public void verificarCorreo(){
        BDConnection bd = new BDConnection(this,"bitacora",null,1);
        SQLiteDatabase baseBitacora = bd.getWritableDatabase();
        String verificarCorreoQuery = "SELECT * FROM usuarios WHERE Correo = '"+correoExt+"'";
        Cursor datos = baseBitacora.rawQuery(verificarCorreoQuery,null);

        if(datos.moveToFirst()){
            correo.setError("El correo ya existe!");
            i=1;
        }
    }
    public void verificarUID(){
        BDConnection bd = new BDConnection(this,"bitacora",null,1);
        SQLiteDatabase baseBitacora = bd.getWritableDatabase();
        String verificarUIDQuery = "SELECT * FROM usuarios WHERE UID = '"+uidExt+"'";
        Cursor datos = baseBitacora.rawQuery(verificarUIDQuery,null);

        if(datos.moveToFirst()){
            generarUID(30);
            verificarUID();
        }
    }

    private String generarUID(int length) {
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder uid = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(allowedChars.length());
            uid.append(allowedChars.charAt(index));
        }

        return uid.toString();
    }

}