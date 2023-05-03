package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyecto.info.Movie;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Pelicula extends AppCompatActivity {

    private ImageView imageViewPoster;
    private TextView textViewTitle;
    private TextView textViewOverview;
    private TextView textViewDuration;
    private TextView textViewGenre;
    private TextView textViewReleaseDate;
    private CheckBox checkBoxVista;
    private Button buttonAddToUser;
    private Button buttonDeleteFromUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelicula);

        // Referencias de las vistas
        imageViewPoster = findViewById(R.id.imageViewPoster);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewOverview = findViewById(R.id.textViewOverview);
        textViewDuration = findViewById(R.id.textViewDuration);
        textViewGenre = findViewById(R.id.textViewGenre);
        textViewReleaseDate = findViewById(R.id.textViewReleaseDate);
        checkBoxVista = findViewById(R.id.checkBoxViewed);
        buttonAddToUser = findViewById(R.id.buttonAddToUser);
        buttonDeleteFromUser = findViewById(R.id.buttonRemoveFromUser);

        // Obtener los datos de la película desde la API de The Movie Database
        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra("movie");

        String movieId = movie.getIdPelicula(); // Obtener el ID de la película enviado desde la actividad anterior

        // Obtener una instancia de SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MiPref", Context.MODE_PRIVATE);

        // Obtener el valor String almacenado con la clave "nombreUsuario" o un valor por defecto si no se encuentra
        String nombre = sharedPreferences.getString("nombreUsuario", "David1");

        String apiKey = "fd3ee47d3c563fc5782362955aba4142"; // API key
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + apiKey;

        comprobarPelicula(movieId, nombre, buttonDeleteFromUser);

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
                            String duration = response.getString("runtime") + " min";
                            String genre = response.getJSONArray("genres").getJSONObject(0).getString("name");
                            String releaseDate = response.getString("release_date");

                            // Mostrar los datos en la interfaz de usuario
                            textViewTitle.setText(title);
                            textViewOverview.setText(overview);
                            textViewDuration.setText(duration);
                            textViewGenre.setText(genre);
                            textViewReleaseDate.setText(releaseDate);
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
        url = "https://10.0.2.2/php/peliculaVista.php?idPelicula=" + movieId + "&nombre=" + nombre;

         // Realizar una petición GET a "peliculaVista.php"
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Obtén el estado de la vista de la película del JSON de respuesta
                            boolean vista = response.getBoolean("vista");

                            // Marcamos o desmarcamos la casilla en función del estado de la vista
                            checkBoxVista.setChecked(vista);
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

        // Agregar la petición a la cola de peticiones de Volley
        Volley.newRequestQueue(this).add(jsonObjectRequest);



        // Agregar un listener al botón de eliminar para realizar la petición de eliminación
        buttonDeleteFromUser.setOnClickListener(v -> {
            String url2 = "https://10.0.2.2/php/eliminarPelicula.php?idPelicula=" + movieId + "&nombre=" + nombre;

            // Realizar una petición GET a "eliminarPelicula.php"
            JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, url2, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                // Obtén la respuesta del servidor
                                boolean eliminado = response.getBoolean("eliminado");

                                // Si se eliminó correctamente, actualiza el estado de la vista
                                if (eliminado) {
                                    checkBoxVista.setChecked(false);
                                    buttonDeleteFromUser.setVisibility(View.GONE);
                                }
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
            Volley.newRequestQueue(Pelicula.this).add(jsonObjectRequest2);

        });

        buttonAddToUser.setOnClickListener(v -> {
            String url3 = "http://10.0.2.2/php/anadirPelicula.php?idPelicula=" + movieId + "&nombre=" + nombre + "&vista=" + (checkBoxVista.isChecked()  ? "1" : "0");

            // Realizar una petición GET a "anadirPelicula.php"
            JsonObjectRequest jsonObjectRequest3 = new JsonObjectRequest(Request.Method.GET, url3, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                // Obtén la respuesta del servidor
                                boolean añadido = response.getBoolean("añadido");

                                // Si se añadió correctamente, actualiza el estado de la vista
                                if (añadido) {
                                    buttonDeleteFromUser.setVisibility(View.VISIBLE);
                                }
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
            Volley.newRequestQueue(Pelicula.this).add(jsonObjectRequest3);
        });
    }

    private void comprobarPelicula(String movieId, String nombre, Button buttonDeleteFromUser) {
        // Realizar una petición GET a "listaPelisUsuario.php"
        String urlListaPelis = "http://10.0.2.2/php/listaPelisUsuario.php?nombre=" + nombre;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlListaPelis, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Buscar si la película está en la lista
                            boolean peliculaEnLista = false;
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject pelicula = response.getJSONObject(i);
                                String idPeliculaEnLista = pelicula.getString("idPelicula");
                                if (idPeliculaEnLista.equals(movieId)) {
                                    peliculaEnLista = true;
                                    break;
                                }
                            }

                            // Ocultar o mostrar el botón de borrar según corresponda
                            if (peliculaEnLista) {
                                buttonDeleteFromUser.setVisibility(View.VISIBLE);
                            } else {
                                buttonDeleteFromUser.setVisibility(View.GONE);
                            }
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
        Volley.newRequestQueue(Pelicula.this).add(jsonArrayRequest);
    }
}