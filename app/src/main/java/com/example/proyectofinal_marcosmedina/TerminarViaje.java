package com.example.proyectofinal_marcosmedina;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class TerminarViaje extends AppCompatActivity {
        private TextInputEditText txtHora,txtHoraLlegada;
        private TextInputEditText txtFecha;
        private EditText txtLugar, txtKilometraje, txtDetalle,txtKilometrajeFinal, txtLugarLLegada;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_terminar_viaje);

            txtHora = findViewById(R.id.txtHora);
            txtHoraLlegada = findViewById(R.id.txtHoraLlegada);
            txtFecha = findViewById(R.id.txtFecha);
            txtLugar = findViewById(R.id.txtLugarSalida);
            txtLugarLLegada = findViewById(R.id.txtLugarLlegada);
            txtKilometraje = findViewById(R.id.txtKilometraje);
            txtKilometrajeFinal = findViewById(R.id.txtKilometrajeFinal);
            txtDetalle = findViewById(R.id.txtDetalle);


            cargarDatos();

            txtHora = findViewById(R.id.txtHora);
            txtHora.setFocusable(false); // Evitar que el campo de texto sea editable
            txtHora.setOnClickListener(v -> showTimePickerDialog());

            txtHoraLlegada = findViewById(R.id.txtHoraLlegada);
            txtHoraLlegada.setFocusable(false); // Evitar que el campo de texto sea editable
            txtHoraLlegada.setOnClickListener(v -> showTimePickerDialog());
            setCurrentTime();

            txtFecha = findViewById(R.id.txtFecha);
            txtFecha.setFocusable(false); // Evitar que el campo de texto sea editable
            txtFecha.setOnClickListener(v -> showDatePickerDialog());
        }
        String extractedUID;
        public void cargarDatos(){
            try{
                SharedPreferences sharedPreferences = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
                extractedUID = sharedPreferences.getString("UIDtemp", "");

                BDConnection bd = new BDConnection(this,"bitacora",null,1);
                SQLiteDatabase baseBitacora = bd.getWritableDatabase();
                String loginQuery = "SELECT * FROM TemporalesSesion WHERE UID='"+extractedUID+"'";
                Cursor datos = baseBitacora.rawQuery(loginQuery,null);

                if(datos.moveToFirst()){

                    txtKilometraje.setText(datos.getString(2));
                    txtLugar.setText(datos.getString(3));
                    txtHora.setText(datos.getString(5));
                    txtFecha.setText(datos.getString(4));
                    txtDetalle.setText(datos.getString(6));

                }else{
                    Toast.makeText(this,"OPS!!", Toast.LENGTH_LONG).show();
                }
            }catch (SQLException ex){
                Toast.makeText(this,ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        int i = 0;
        private String hora;
        private String horaLlegada;
        private String fecha;
        private String lugarLlegada;
        private String kilometraje;
        private String kilometrajeFinal;
        private String detalle;
        private String lugarInicio;
        public void verificarDatosFinal(View v){
            i=0;
            hora = String.valueOf(txtHora.getText());
            horaLlegada = String.valueOf(txtHoraLlegada.getText());
            fecha = String.valueOf(txtFecha.getText());
            lugarInicio = String.valueOf(txtLugar.getText());
            lugarLlegada = String.valueOf(txtLugarLLegada.getText());
            kilometraje = String.valueOf(txtKilometraje.getText());
            kilometrajeFinal = String.valueOf(txtKilometrajeFinal.getText());

            detalle = String.valueOf(txtDetalle.getText());

            if(fecha.isEmpty()){
                txtFecha.setError("Ingrese la Fecha");
                i=1;
            }
            if(hora.isEmpty()){
                txtHora.setError("Ingrese la Hora de Salida");
                i=1;
            }
            if(horaLlegada.isEmpty()){
                txtHoraLlegada.setError("Ingrese la Hora de Llegada");
                i=1;
            }
            if(lugarInicio.isEmpty()){
                txtLugar.setError("Ingrese el Lugar de Salida");
                i=1;
            }
            if(lugarLlegada.isEmpty()){
                txtLugarLLegada.setError("Ingrese el Lugar de Llegada");
                i=1;
            }
            if(kilometraje.isEmpty()){
                txtKilometraje.setError("Ingrese el Kilometraje Inicial");
                i=1;
            }
            if(kilometrajeFinal.isEmpty()){
                txtKilometrajeFinal.setError("Ingrese el Kilometraje Final");
                i=1;
            }
            if(detalle.isEmpty()){
                txtDetalle.setError("Ingrese el Detalle de Actividad");
                i=1;
            }
            if(i==0){
                AlertDialog.Builder confirmar = new AlertDialog.Builder(this);
                confirmar.setMessage("¿DESEA TERMINAR EL VIAJE?");
                confirmar.setTitle("TERMINAR VIAJE");
                confirmar.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        terminarViaje();
                    }
                });

                confirmar.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialogo = confirmar.create();
                dialogo.show();
            }
        }

        public void terminarViaje(){
            SharedPreferences sharedPreferences = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
            String extractedUIDdelete = sharedPreferences.getString("UIDtemp", "");

            BDConnection bd = new BDConnection(this,"bitacora",null,1);
            SQLiteDatabase baseBitacora = bd.getWritableDatabase();
            String eliminarUsuario = "DELETE FROM TemporalesSesion WHERE UID='"+extractedUIDdelete+"'";
            baseBitacora.execSQL(eliminarUsuario);

            migrarRegistro();

            Intent resultados = new Intent();
            setResult(RESULT_OK, resultados);
            resultados.putExtra("status","terminado");
            finish();

        }
        int idExtr, kmI, kmL,kmOp;
        String kilometrosSuma;
        private void migrarRegistro(){
            try{
                obtenerIdUsuario();
                kmI = Integer.parseInt(kilometraje);
                kmL = Integer.parseInt(kilometrajeFinal);
                kmOp = kmL-kmI;
                kilometrosSuma = String.valueOf(kmOp);

                BDConnection bd = new BDConnection(this,"bitacora",null,1);
                SQLiteDatabase baseBitacora = bd.getWritableDatabase();
                String migrarRegistro = "INSERT INTO Movimientos (UID,Fecha,KilometrajeInicio,KilometrajeFinal,KilometrosRecorridos,DestinoSalida,DestinoLlegada,HoraSalida,HoraLlegada,DetalleActividad,ID_Usuario) " +
                        "VALUES ('"+generarUID(30)+"','"+fecha+"','"+kilometraje+"','"+kilometrajeFinal+"','"+kilometrosSuma+"','"+
                        lugarInicio+"','"+lugarLlegada+"','"+hora+"','"+horaLlegada+"','"+detalle+"','"+idExtr+"')";
                baseBitacora.execSQL(migrarRegistro);

            }catch(SQLException ex){
                Log.w(TAG,"PASO ESTO "+ex.getMessage());
            }
        }

        public void obtenerIdUsuario(){
            SharedPreferences sharedPreferences = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
            String obtenerIDuser = sharedPreferences.getString("UIDtemp", "");

            BDConnection bd = new BDConnection(this,"bitacora",null,1);
            SQLiteDatabase baseBitacora = bd.getWritableDatabase();
            String loginQuery = "SELECT * FROM usuarios WHERE UID='"+obtenerIDuser+"'";
            Cursor datos = baseBitacora.rawQuery(loginQuery,null);

            if(datos.moveToFirst()){
               idExtr=datos.getInt(0);
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

        private void showTimePickerDialog() {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minuteOfHour) -> {
                // Convert hourOfDay to 12-hour format and determine AM/PM
                boolean isPM = (hourOfDay >= 12);
                int hourIn12Format = (hourOfDay == 0 || hourOfDay == 12) ? 12 : hourOfDay % 12;
                String amPm = isPM ? "PM" : "AM";

                String time = String.format("%02d:%02d %s", hourIn12Format, minuteOfHour, amPm);
                txtHora.setText(time);
            }, hour, minute, false);  // false indicates 12-hour format

            timePickerDialog.show();
        }

        private void setCurrentTime() {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());  // 'hh' for 12-hour format and 'a' for AM/PM
            String currentTime = timeFormat.format(calendar.getTime());
            txtHoraLlegada.setText(currentTime);
        }
        private void showDatePickerDialog() {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, monthOfYear, dayOfMonth) -> {
                String date = String.format("%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year1);
                txtFecha.setText(date);
            }, year, month, day);

            datePickerDialog.show();
        }

}