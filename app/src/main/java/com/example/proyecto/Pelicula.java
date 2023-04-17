package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class Pelicula extends AppCompatActivity {

    private ImageView imageViewPoster;
    private TextView textViewTitle;
    private TextView textViewOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelicula);

        // Referencias de las vistas
        imageViewPoster = findViewById(R.id.imageViewPoster);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewOverview = findViewById(R.id.textViewOverview);

        // Obtener los datos de la película desde la API de The Movie Database
        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra("movie");

        String movieId = movie.getId(); // Obtener el ID de la película enviado desde la actividad anterior


        String apiKey = "fd3ee47d3c563fc5782362955aba4142"; // API key
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + apiKey;

        // Realizar una petición GET a la API de The Movie Database
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Obtén los datos de la película del JSON de respuesta
                            String title = response.getString("original_title");
                            String overview = response.getString("overview");
                            String posterPath = response.getString("poster_path");

                            // Mostrar los datos en la interfaz de usuario
                            textViewTitle.setText(title);
                            textViewOverview.setText(overview);
                            Picasso.get().load("https://image.tmdb.org/t/p/w500" + posterPath).into(imageViewPoster);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        // Agrega la petición a la cola de peticiones de Volley
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

}