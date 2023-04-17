package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Exito extends AppCompatActivity {



    private fragmentPeliculas moviesFragment;
    private fragmentSeries seriesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exito);

        // Obtén las instancias de los fragmentos
        moviesFragment = new fragmentPeliculas();
        seriesFragment = new fragmentSeries();

        // Cargar el fragmento de películas en el contenedor correspondiente
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, moviesFragment);
        fragmentTransaction.commit();

        Button btnMovies = findViewById(R.id.btnMovies);
        Button btnSeries = findViewById(R.id.btnSeries);

        // Configurar el clic del botón de "Películas" para cargar el fragmento de películas
        findViewById(R.id.btnMovies).setOnClickListener(view -> {
            btnMovies.setSelected(true);
            btnSeries.setSelected(false);
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.fragment_container, moviesFragment);
            ft.commit();
        });

        // Configurar el clic del botón de "Series" para cargar el fragmento de series
        findViewById(R.id.btnSeries).setOnClickListener(view -> {
            btnMovies.setSelected(false);
            btnSeries.setSelected(true);
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.fragment_container, seriesFragment);
            ft.commit();
        });
    }
}
