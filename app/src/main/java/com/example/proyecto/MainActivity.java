package com.example.proyecto;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.*;
public class MainActivity extends AppCompatActivity {

    private EditText usuario;
    private EditText contraseña;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuario = (EditText) findViewById(R.id.username);
        contraseña  = (EditText) findViewById(R.id.password);
    }

    public void iniciar(View view) {
        String URL = "http://10.0.2.2/php/login.php";
        String password = contraseña.getText().toString().trim();
        String username = usuario.getText().toString().trim();

        if(!password.isEmpty() && !username.isEmpty()) {
            StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("OK")) {
                        Intent intent = new Intent(MainActivity.this, Exito.class);

                        // Guarda el nombre de usuario en SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("MiPref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("nombreUsuario", username);
                        editor.apply();

                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Credenciales inválidas", Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.toString().trim(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("username", username);
                    data.put("password", password);
                    return data;
                }
            };
            RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
            rq.add(sr);
        } else {
            Toast.makeText(this, "Los campos no pueden estar vacios", Toast.LENGTH_LONG).show();
        }
    }

    public void registrarse(View view) {
        String URL = "http://10.0.2.2/php/signin.php";
        String password = contraseña.getText().toString().trim();
        String username = usuario.getText().toString().trim();

        if(!password.isEmpty() && !username.isEmpty()) {
            StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("OK")) {
                        usuario.setText("");
                        contraseña.setText("");
                        Toast.makeText(MainActivity.this, "Registro correcto", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Nombre de usuario no disponible", Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.toString().trim(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("username", username);
                    data.put("password", password);
                    return data;
                }
            };
            RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
            rq.add(sr);
        } else {
            Toast.makeText(this, "Los campos no pueden estar vacios", Toast.LENGTH_LONG).show();
        }
    }
}