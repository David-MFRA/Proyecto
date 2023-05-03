package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ajustes extends AppCompatActivity {
    private EditText currentPasswordEditText;
    private EditText newPasswordEditText;
    private TextView saludo;
    private String nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        // Obtener una instancia de SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MiPref", Context.MODE_PRIVATE);

        // Obtener el valor String almacenado con la clave "nombreUsuario" o un valor por defecto si no se encuentra
        nombre = sharedPreferences.getString("nombreUsuario", "David1");


        currentPasswordEditText = findViewById(R.id.current_password);
        newPasswordEditText = findViewById(R.id.new_password);
        saludo = findViewById(R.id.welcome_message);
        saludo.append(nombre);

        Button changePasswordButton = findViewById(R.id.change_password_button);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener las contraseñas actual y nueva
                String currentPassword = currentPasswordEditText.getText().toString();
                String newPassword = newPasswordEditText.getText().toString();

                // Llamar a la tarea asincrónica para realizar la consulta de la base de datos
                new ChangePasswordTask().execute(currentPassword, newPassword);
            }
        });
    }

    private class ChangePasswordTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            // Construir la URL para el script PHP que consulta la base de datos
            String urlString = "http://10.0.2.2/php/change_password.php";
            String currentPassword = params[0];
            String newPassword = params[1];
            try {
                urlString += "?username=" + URLEncoder.encode(nombre, "UTF-8");
                urlString += "&current_password=" + URLEncoder.encode(currentPassword, "UTF-8");
                urlString += "&new_password=" + URLEncoder.encode(newPassword, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Realizar la consulta a la URL
            String result = "";
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                InputStream stream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                reader.close();
                stream.close();
                result = builder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // Mostrar un mensaje de éxito o error según el resultado obtenido
            if (result.equals("OK")) {
                Toast.makeText(ajustes.this, "La contraseña ha sido cambiada", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ajustes.this, "La contraseña actual es incorrecta", Toast.LENGTH_SHORT).show();
            }
        }
    }
}