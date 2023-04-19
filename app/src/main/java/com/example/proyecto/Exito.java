package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.proyecto.busquedas.buscarPelicula;
import com.example.proyecto.busquedas.buscarSerie;
import com.example.proyecto.fragmentos.fragmentPeliculasLista;
import com.example.proyecto.fragmentos.fragmentPeliculasPorVer;
import com.example.proyecto.fragmentos.fragmentSeriesLista;
import com.example.proyecto.fragmentos.fragmentSeriesPorVer;

public class Exito extends AppCompatActivity {


    private fragmentPeliculasPorVer peliculasPorVerFragment;
    private fragmentPeliculasLista peliculasListaFragment;
    private fragmentSeriesPorVer seriesPorVerFragment;
    private fragmentSeriesLista seriesListaFragment;
    private boolean isPeliculasSelected = false;

    private String nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exito);

        // Obtener una instancia de SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MiPref", Context.MODE_PRIVATE);

        // Obtener el valor String almacenado con la clave "nombreUsuario" o un valor por defecto si no se encuentra
        String nombre = sharedPreferences.getString("nombreUsuario", "David1");

        // Obtener las instancias de los fragmentos
        peliculasPorVerFragment = new fragmentPeliculasPorVer();
        peliculasListaFragment = new fragmentPeliculasLista();
        seriesPorVerFragment = new fragmentSeriesPorVer();
        seriesListaFragment = new fragmentSeriesLista();

        // Cargar el fragmento "Por ver" de series en el contenedor
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, seriesPorVerFragment);
        fragmentTransaction.commit();

        // Configurar el clic del botón de "Películas" para cargar los fragmentos de películas
        Button btnMovies = findViewById(R.id.btn_movies);
        btnMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPeliculasSelected) {
                    isPeliculasSelected = true;
                    btnMovies.setSelected(true);
                    findViewById(R.id.btn_series).setSelected(false);
                    findViewById(R.id.btn_ajustes).setSelected(false);
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    if (findViewById(R.id.btn_por_ver).isSelected()) {
                        ft.replace(R.id.fragment_container, peliculasPorVerFragment);
                    } else {
                        ft.replace(R.id.fragment_container, peliculasListaFragment);
                    }
                    ft.commit();
                }
            }
        });

        // Configurar el clic del botón de "Series" para cargar los fragmentos de series
        Button btnSeries = findViewById(R.id.btn_series);
        btnSeries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPeliculasSelected) {
                    isPeliculasSelected = false;
                    btnMovies.setSelected(false);
                    btnSeries.setSelected(true);
                    findViewById(R.id.btn_ajustes).setSelected(false);
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    if (findViewById(R.id.btn_por_ver).isSelected()) {
                        ft.replace(R.id.fragment_container, seriesPorVerFragment);
                    } else {
                        ft.replace(R.id.fragment_container, seriesListaFragment);
                    }
                    ft.commit();
                }
            }
        });

        // Configurar el clic del botón de "Ajustes" para cargar la actividad de ajustes
        Button btnAjustes = findViewById(R.id.btn_ajustes);
        btnAjustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!btnAjustes.isSelected()) {
                    btnMovies.setSelected(false);
                    btnSeries.setSelected(false);
                    btnAjustes.setSelected(true);
                    // Aquí debes cargar la actividad de ajustes
                }
            }
        });

        // Configurar el clic del botón de "Por ver" para cargar el fragmento correspondiente
        Button btnPorVer = findViewById(R.id.btn_por_ver);
        btnPorVer.setSelected(true);
        btnPorVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPorVer.setSelected(true);
                findViewById(R.id.btn_lista).setSelected(false);
                FragmentTransaction ft = fragmentManager.beginTransaction();
                if (isPeliculasSelected) {
                    ft.replace(R.id.fragment_container, peliculasPorVerFragment);
                } else {
                    ft.replace(R.id.fragment_container, seriesPorVerFragment);
                }
                ft.commit();
            }
        });

        // Configurar el clic del botón de "Lista" para cargar el fragmento correspondiente
        Button btnLista = findViewById(R.id.btn_lista);
        btnLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPorVer.setSelected(false);
                btnLista.setSelected(true);
                FragmentTransaction ft = fragmentManager.beginTransaction();
                if (isPeliculasSelected) {
                    ft.replace(R.id.fragment_container, peliculasListaFragment);
                } else {
                    ft.replace(R.id.fragment_container, seriesListaFragment);
                }
                ft.commit();
            }
        });

        // Configurar el clic del botón de "Buscar" para cargar la actividad de búsqueda
        Button btnBuscar = findViewById(R.id.btn_buscar);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                if(isPeliculasSelected) {
                    intent = new Intent(Exito.this, buscarPelicula.class);
                }
                else {
                    intent = new Intent(Exito.this, buscarSerie.class);
                }
                startActivity(intent);
            }
        });

        btnSeries.setSelected(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Obtener las instancias de los fragmentos
        peliculasPorVerFragment = new fragmentPeliculasPorVer();
        peliculasListaFragment = new fragmentPeliculasLista();
        seriesPorVerFragment = new fragmentSeriesPorVer();
        seriesListaFragment = new fragmentSeriesLista();

        // Cargar el fragmento correspondiente en el contenedor
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (isPeliculasSelected) {
            if (findViewById(R.id.btn_por_ver).isSelected()) {
                fragmentTransaction.replace(R.id.fragment_container, peliculasPorVerFragment);
            } else {
                fragmentTransaction.replace(R.id.fragment_container, peliculasListaFragment);
            }
        } else {
            if (findViewById(R.id.btn_por_ver).isSelected()) {
                fragmentTransaction.replace(R.id.fragment_container, seriesPorVerFragment);
            } else {
                fragmentTransaction.replace(R.id.fragment_container, seriesListaFragment);
            }
        }
        fragmentTransaction.commit();
    }
}
