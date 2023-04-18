package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;

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

public class buscarPelicula extends AppCompatActivity {

    private EditText etMovieName;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_pelicula);

        etMovieName = findViewById(R.id.movie_name);
        btnSearch = findViewById(R.id.btn_search);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String movieName = etMovieName.getText().toString().trim();
                if (!TextUtils.isEmpty(movieName)) {
                    searchMovie(movieName);
                } else {
                    Toast.makeText(buscarPelicula.this, "Por favor ingrese el nombre de una película", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void searchMovie(String movieName) {
        String url = "https://api.themoviedb.org/3/search/movie?api_key=fd3ee47d3c563fc5782362955aba4142&query=" + movieName;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray resultsArray = response.getJSONArray("results");
                            if (resultsArray.length() > 0) {
                                // Obtener los datos de la primera película de la respuesta
                                JSONObject movieObject = resultsArray.getJSONObject(0);
                                String movieId = movieObject.getString("id");
                                String movieTitle = movieObject.getString("title");
                                String movieOverview = movieObject.getString("overview");
                                String movieReleaseDate = movieObject.getString("release_date");

                                // Crear un objeto de película para pasar a la actividad de detalles
                                Movie movie = new Movie(movieId, movieTitle, movieReleaseDate);

                                // Iniciar la actividad de detalles de la película
                                Intent intent = new Intent(buscarPelicula.this, Pelicula.class);
                                intent.putExtra("movie", movie);
                                startActivity(intent);
                            } else {
                                Toast.makeText(buscarPelicula.this, "No se encontraron resultados", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Manejar el error de la petición a la API
                Toast.makeText(buscarPelicula.this, "Error en la búsqueda de películas", Toast.LENGTH_SHORT).show();
            }
        });

        // Agregar la petición a la cola de peticiones de Volley
        //VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}