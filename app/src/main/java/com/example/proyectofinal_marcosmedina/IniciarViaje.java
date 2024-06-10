package com.example.proyectofinal_marcosmedina;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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


public class IniciarViaje extends AppCompatActivity {
    private TextInputEditText txtHora;
    private TextInputEditText txtFecha;
    private EditText txtLugar, txtKilometraje, txtDetalle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_iniciar_viaje);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        txtLugar = findViewById(R.id.txtLugarSalida);
        txtKilometraje = findViewById(R.id.txtKilometraje);
        txtDetalle = findViewById(R.id.txtDetalle);

        txtHora = findViewById(R.id.txtHora);
        txtHora.setFocusable(false); // Evitar que el campo de texto sea editable
        txtHora.setOnClickListener(v -> showTimePickerDialog());
        setCurrentTime();

        txtFecha = findViewById(R.id.txtFecha);
        txtFecha.setFocusable(false); // Evitar que el campo de texto sea editable
        txtFecha.setOnClickListener(v -> showDatePickerDialog());
        setCurrentDate();

    }
    int i = 0;
    private String hora, fecha, lugar,kilometraje,detalle;
    public void verificarDatos(View v){
        i=0;
        hora = String.valueOf(txtHora.getText());
        fecha = String.valueOf(txtFecha.getText());
        lugar = String.valueOf(txtLugar.getText());
        kilometraje = String.valueOf(txtKilometraje.getText());
        detalle = String.valueOf(txtDetalle.getText());

        if(fecha.isEmpty()){
            txtFecha.setError("Ingrese la fecha");
            i=1;
        }
        if(hora.isEmpty()){
            txtHora.setError("Ingrese la hora");
            i=1;
        }
        if(lugar.isEmpty()){
            txtLugar.setError("Ingrese el lugar de salida");
            i=1;
        }
        if(kilometraje.isEmpty()){
            txtKilometraje.setError("Ingrese el kilometraje inicial");
            i=1;
        }
        if(detalle.isEmpty()){
            txtDetalle.setError("Ingrese el detalle de actividad");
            i=1;
        }
        if(i==0){
            iniciarViaje();
        }
    }
    public void iniciarViaje(){
        SharedPreferences sharedPreferences = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        String extractedUIDinsert = sharedPreferences.getString("UIDtemp", "");

        BDConnection bd = new BDConnection(this,"bitacora",null,1);
        SQLiteDatabase baseBitacora = bd.getWritableDatabase();
        String insertarUsuario = "INSERT INTO TemporalesSesion(UID,KilometrajeInicio,DestinoSalida, Fecha, HoraSalida,DetalleActividad) " +
                "VALUES('"+extractedUIDinsert+"','"+kilometraje+"','"+lugar+"','"+fecha+"','"+hora+"','"+detalle+"')";
        baseBitacora.execSQL(insertarUsuario);
        Intent resultados = new Intent();
        setResult(RESULT_OK, resultados);
        finish();

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
        txtHora.setText(currentTime);
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
    private void setCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());
        txtFecha.setText(currentDate);
    }
}