package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class Serie extends AppCompatActivity {

    private ImageView imagenSerie;
    private TextView tituloSerie;
    private TextView sinopsisSerie;
    private TextView temporadasSerie;
    private TextView plataformaSerie;
    private TextView puntuacionSerie;
    private TextView generoSerie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serie);

        // Obtener los elementos de la interfaz gráfica
        imagenSerie = findViewById(R.id.serie_image);
        tituloSerie = findViewById(R.id.serie_title);
        sinopsisSerie = findViewById(R.id.serie_sinopsis);
        temporadasSerie = findViewById(R.id.serie_temporadas);
        plataformaSerie = findViewById(R.id.serie_plataforma);
        puntuacionSerie = findViewById(R.id.serie_puntuacion);
        generoSerie = findViewById(R.id.serie_genero);

        // Obtener la serie que se debe mostrar
        Show serie = getIntent().getParcelableExtra("serie");

        tituloSerie.setText(serie.getTitulo());
        sinopsisSerie.setText(serie.getSinopsis());
        temporadasSerie.setText(serie.getTemporadas());
        plataformaSerie.setText(serie.getPlataforma());
        puntuacionSerie.setText(serie.getPuntuacion());
        generoSerie.setText(serie.getGenero());

        // Actualizar los elementos de la interfaz gráfica con la información de la serie
        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500" + serie.getImagen())
                .placeholder(R.drawable.placeholder) // Imagen de placeholder
                .error(R.drawable.placeholder) // Imagen de error
                .into(imagenSerie);
    }
}
