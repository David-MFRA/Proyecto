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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyecto.adaptadores.TemporadaAdapter;
import com.example.proyecto.info.Show;
import com.example.proyecto.info.Season;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Serie extends AppCompatActivity {

    private ImageView imageViewPoster;
    private TextView textViewTitle;
    private TextView textViewSinopsis;
    private TextView textViewTemporadas;
    private TextView textViewGenero;
    private TextView textViewPlataforma;
    private TextView textViewPuntuacion;
    private TextView textViewEmision;
    private CheckBox checkBoxVista;
    private Button buttonAddToUser;
    private Button buttonDeleteFromUser;
    private ListView listViewTemporadas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serie);

        // Referencias de las vistas
        imageViewPoster = findViewById(R.id.serie_image);
        textViewTitle = findViewById(R.id.serie_title);
        textViewSinopsis = findViewById(R.id.serie_sinopsis);
        textViewTemporadas = findViewById(R.id.serie_temporadas);
        textViewGenero = findViewById(R.id.serie_genero);
        textViewPlataforma = findViewById(R.id.serie_plataforma);
        textViewPuntuacion = findViewById(R.id.serie_puntuacion);
        textViewEmision = findViewById(R.id.serie_emision);
        checkBoxVista = findViewById(R.id.checkBoxViewed);
        buttonAddToUser = findViewById(R.id.buttonAddToUser);
        buttonDeleteFromUser = findViewById(R.id.buttonRemoveFromUser);
        listViewTemporadas = findViewById(R.id.lista_temporadas);

        // Obtener los datos de la serie desde la API de The Movie Database
        Intent intent = getIntent();
        Show serie = intent.getParcelableExtra("serie");

        String serieId = serie.getIdSerie(); // Obtener el ID de la serie enviado desde la actividad anterior

        // Obtener una instancia de SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MiPref", Context.MODE_PRIVATE);

        // Obtener el valor String almacenado con la clave "nombreUsuario" o un valor por defecto si no se encuentra
        String nombre = sharedPreferences.getString("nombreUsuario", "David1");

        String apiKey = "fd3ee47d3c563fc5782362955aba4142"; // API key
        String url = "https://api.themoviedb.org/3/tv/" + serieId + "?api_key=" + apiKey;

        // Realizar una petición GET a la API de The Movie Database
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Obtén los datos de la serie del JSON de respuesta
                            String title = response.getString("name");
                            String sinopsis = response.getString("overview");
                            String posterPath = response.getString("poster_path");
                            String temporadas = response.getString("number_of_seasons");
                            String genero = response.getJSONArray("genres").getJSONObject(0).getString("name");
                            String plataforma = response.getJSONArray("networks").getJSONObject(0).getString("name");
                            double puntuacion = response.getDouble("vote_average");
                            String emision = response.getString("first_air_date");


                            // Obtener las temporadas de la serie desde la propiedad "seasons" del JSON de respuesta
                            JSONArray seasonsArray = response.getJSONArray("seasons");
                            int numTemporadas = seasonsArray.length();

                            // Crear una lista para almacenar las temporadas de la serie
                            List<Season> listaSeasons = new ArrayList<Season>();
                            for (int i = 0; i < numTemporadas; i++) {
                                JSONObject season = seasonsArray.getJSONObject(i);
                                String idTemporada = season.getString("id");
                                String nombreTemporada = season.getString("name");
                                String numTemporada = season.getString("season_number");
                                String numEpisodios = season.getString("episode_count");
                                String idSerie = serieId;
                                listaSeasons.add(new Season(idTemporada, nombreTemporada, numTemporada, numEpisodios, idSerie, emision, numEpisodios, sinopsis, posterPath));
                            }

                            JSONArray jsonArray = new JSONArray();
                            try {
                                for (Season temporada : listaSeasons) {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("idTemporada", temporada.getIdTemporada());
                                    jsonObject.put("numTemporada", temporada.getNumTemporada());
                                    jsonObject.put("numEpisodios", temporada.getNumEpisodios());
                                    jsonObject.put("idSerie", temporada.getIdSerie());
                                    jsonObject.put("nombreTemporada", temporada.getNombreTemporada());
                                    jsonObject.put("airDate", temporada.getAirDate());
                                    jsonObject.put("episodeCount", temporada.getEpisodeCount());
                                    jsonObject.put("overview", temporada.getOverview());
                                    jsonObject.put("posterPath", temporada.getPosterPath());
                                    jsonArray.put(jsonObject);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            // Crear un adaptador para la lista de temporadas
                            TemporadaAdapter temporadaListAdapter = new TemporadaAdapter(Serie.this, jsonArray, serieId, nombre);
                            listViewTemporadas.setAdapter(temporadaListAdapter);

                            // Mostrar los datos en la interfaz de usuario
                            textViewTitle.setText(title);
                            textViewSinopsis.setText(sinopsis);
                            textViewTemporadas.setText(temporadas + " temporadas");
                            textViewGenero.setText(genero);
                            textViewPlataforma.setText(plataforma);
                            textViewPuntuacion.setText(Double.toString(puntuacion));
                            textViewEmision.setText(emision);
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

        url = "https://10.0.2.2/php/serieVista.php?idSerie=" + serieId + "&nombre=" + nombre;

        // Realizar una petición GET a "serieVista.php"
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Obtén el estado de la vista de la serie del JSON de respuesta
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

        // Agrega la petición a la cola de peticiones de Volley
        Volley.newRequestQueue(this).add(jsonObjectRequest);

        comprobarSerie(serieId, nombre, buttonDeleteFromUser);

        // Agregar un listener al botón de eliminar para realizar la petición de eliminación
        buttonDeleteFromUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url2 = "https://10.0.2.2/php/eliminarSerie.php?idSerie=" + serieId + "&nombre=" + nombre;
                // Realizar una petición GET a "eliminarSerie.php" para eliminar la serie de la lista del usuario
                JsonObjectRequest jsonObjectRequestEliminar = new JsonObjectRequest(Request.Method.GET, url2, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    // Obtener el resultado de la eliminación del JSON de respuesta
                                    boolean eliminada = response.getBoolean("eliminada");

                                    if (eliminada) {
                                        // Actualizar la vista del botón de eliminar
                                        comprobarSerie(serieId, nombre, buttonDeleteFromUser);

                                        // Mostrar un mensaje de éxito
                                        Toast.makeText(Serie.this, "Serie eliminada de la lista", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Mostrar un mensaje de error
                                        Toast.makeText(Serie.this, "Error al eliminar la serie de la lista", Toast.LENGTH_SHORT).show();
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

                // Agregar la petición a la cola de peticiones de Volley
                Volley.newRequestQueue(Serie.this).add(jsonObjectRequestEliminar);
            }
        });

        // Agregar un listener al botón de añadir para realizar la petición de añadir la serie a la lista del usuario
        buttonAddToUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url2 = "https://10.0.2.2/php/anadirSerie.php?idSerie=" + serieId + "&nombre=" + nombre + "&vista=" + (checkBoxVista.isChecked()  ? "1" : "0");

                // Realizar una petición GET a "anadirSerie.php" para añadir la serie a la lista del usuario
                JsonObjectRequest jsonObjectRequestAnadir = new JsonObjectRequest(Request.Method.GET, url2, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    // Obtener el resultado de la adición del JSON de respuesta
                                    boolean anadida = response.getBoolean("anadida");

                                    if (anadida) {
                                        // Actualizar la vista del botón de eliminar
                                        comprobarSerie(serieId, nombre, buttonDeleteFromUser);

                                        // Mostrar un mensaje de éxito
                                        Toast.makeText(Serie.this, "Serie añadida a la lista", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Mostrar un mensaje de error
                                        Toast.makeText(Serie.this, "Error al añadir la serie a la lista", Toast.LENGTH_SHORT).show();
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

                // Agregar la petición a la cola de peticiones de Volley
                Volley.newRequestQueue(Serie.this).add(jsonObjectRequestAnadir);
            }
        });
    }

    private void comprobarSerie(String serieId, String nombre, Button buttonDeleteFromUser) {
        // Realizar una petición GET a "listaSeriessUsuario.php"
        String urlListaPelis = "https://10.0.2.2/php/listaSeriesUsuario.php?nombre=" + nombre;
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
                                if (idPeliculaEnLista.equals(serieId)) {
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
        Volley.newRequestQueue(Serie.this).add(jsonArrayRequest);
    }
}
