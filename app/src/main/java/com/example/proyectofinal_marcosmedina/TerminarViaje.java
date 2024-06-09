package com.example.proyectofinal_marcosmedina;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
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

public class TerminarViaje extends AppCompatActivity {
    private TextInputEditText txtHora,txtHoraLlegada;
    private TextInputEditText txtFecha;
    EditText txtLugar, txtKilometraje, txtDetalle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_terminar_viaje);

        txtHora = findViewById(R.id.txtHora);
        txtFecha = findViewById(R.id.txtFecha);
        txtLugar = findViewById(R.id.txtLugarSalida);
        txtKilometraje = findViewById(R.id.txtKilometraje);
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